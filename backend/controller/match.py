# !/usr/bin/env python

# Copyright 2014 Vodkasoft
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from json import loads

from google.appengine.ext import ndb
from google.appengine.ext.db import TransactionFailedError
from google.appengine.ext.ndb import Future
from google.net.proto.ProtocolBuffer import ProtocolBufferDecodeError
from google.appengine.api.datastore_errors import BadValueError

from controller.base import JsonRequestHandler
from model.datastore import Match, User


class UserMatchesHandler(JsonRequestHandler):
    """ Manages requests to the matches associated to a user """

    def get(self, user_id):
        """ Obtains the matches associated to a use

            Method: GET
            Path: /users/{id}/matches

            Query Parameters:
            id              string              the id of the user
            offset          int                 the number of entries to skip
            limit           int                 the maximum number of entries to return
            pretty          [true|false]        output in human readable format

            Parameters:
            :param user_id: the id of the user

            Returns:
            :return: a JSON array with the data for the requested matches
        """
        query_offset = self.request.get('offset', 0)
        query_limit = self.request.get('limit', 10)
        if query_offset < 0 or type(query_offset) is not int:
            self.write_error('Invalid offset')
        if query_limit < 0 or type(query_limit) is not int:
            self.write_error('Invalid limit')
        user_key = ndb.Key(User, user_id)
        query = Match.query(ndb.OR(Match.host == user_key, Match.guest == user_key))
        result = []
        for match in query.fetch(offset=query_offset, limit=query_limit):
            result.append({'id': match.key.urlsafe(),
                           'guest': match.guest.id(),
                           'guestPoints': match.guest_points,
                           'host': match.host.id(),
                           'hostPoints': match.host_points,
                           'timestamp': match.timestamp.isoformat()})
        self.write_message(200, result)


class MatchHandler(JsonRequestHandler):
    """ Manages request to a specific match"""

    def get(self, match_id):
        """ Obtains data for a specific match

            Method: GET
            Path: /matches/{match_id}

            Query Parameters:
            match_id        int                 the id of the match
            pretty          [true|false]        output in human readable format

            Parameters:
            :param match_id: the id of the match

            Returns:
            :return: a JSON object with the match's data
        """
        try:
            match_key = ndb.Key(urlsafe=match_id)
            match = match_key.get()
            json_result = {'id': match_id,
                           'guest': match.guest.id(),
                           'guestPoints': match.guest_points,
                           'host': match.host.id(),
                           'hostPoints': match.host_points,
                           'timestamp': match.timestamp.isoformat()}
            self.write_message(200, json_result)
        except AttributeError:
            self.write_error('Invalid match id')

    def delete(self, match_id):
        """ Deletes a match

            Method: DELETE
            Path: /matches/{match_id}

            Query Parameters:
            match_id        int                 the id of the match
            pretty          [true|false]        output in human readable format

            Parameters:
            :param match_id: the id of the match

            Returns:
            :return: Empty response
        """
        try:
            key = ndb.Key(urlsafe=match_id)
            key.delete()
            self.write_message(204)
        except (ProtocolBufferDecodeError, TypeError):
            self.write_error('Invalid match id')
        except TransactionFailedError:
            self.write_error('Unable to delete match')


@ndb.transactional(xg=True, retries=2)
def _update_entities(*entities):
    """ Updates entities in a transaction

        Parameters:
        :param entities: then entities to be updated

        Returns:
        :return: an array with the result for each entity

        Raises:
        :raises: TransactionFailedError if the transaction fails
    """
    futures = []
    for entity in entities:
        futures.append(entity.put_async())
    Future.wait_all(futures)
    results = []
    for future in futures:
        results.append(future.get_result())
    return results


class MatchSetHandler(JsonRequestHandler):
    """ Manages request to matches """

    def post(self):
        """ Creates a match

            This operation is transactional

            Method: POST
            Path: /matches

            Query Parameters:
            pretty          [true|false]        output in human readable format
            match           JSON object         the data for the match

            :return: The key for the created match
        """
        match_data = self.request.get('match')
        if match_data is '':
            self.write_error('No match data was provided')
            return
        try:
            match_data = loads(match_data)
            guest_id = match_data['guest']
            guest_points = match_data['guestPoints']
            host_id = match_data['host']
            host_points = match_data['hostPoints']
            if host_id is guest_id:
                status_code = 400
                message = {'error': 'Host and guest id cannot be equal'}
            else:
                match = Match(guest=ndb.Key(User, guest_id),
                              guest_points=guest_points,
                              host=ndb.Key(User, host_id),
                              host_points=host_points)
                # Asynchronously update player data
                guest_future = User.get_by_id_async(guest_id)
                host_future = User.get_by_id_async(host_id)
                guest = guest_future.get_result()
                host = host_future.get_result()
                guest.experience += guest_points
                host.experience += host_points
                # Update entities
                results = _update_entities(match, host, guest)
                # Prepare message
                match_key = results[0].urlsafe()
                status_code = 201
                message = {'id': match_key}
            self.write_message(status_code, message)
        except ValueError:
            self.write_error('Malformed JSON')
        except KeyError:
            self.write_error('Missing attributes for match')
        except TransactionFailedError:
            self.write_message(507, 'Unable to store match')
        except AttributeError:
            self.write_error('Invalid id')
        except BadValueError:
            # Thrown when model validations fail
            self.write_error('Invalid data')
