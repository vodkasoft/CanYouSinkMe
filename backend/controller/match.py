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

from google.appengine.ext import ndb
from google.appengine.ext.db import TransactionFailedError
from google.appengine.ext.ndb import Future
from google.net.proto.ProtocolBuffer import ProtocolBufferDecodeError
from google.appengine.api.datastore_errors import BadValueError, BadRequestError

from controller.authentication import access_token_required
from controller.base import JsonRequestHandler
from model.datastore import Match, User


class UserMatchesHandler(JsonRequestHandler):
    """ Manages requests to the matches associated to a user """

    @access_token_required
    def get(self, user_id):
        """ Obtains the matches associated to a user

            Method: GET
            Path: /users/{user_id}/matches

            URI Parameters:
            user_id         string              id of the user

            Request Parameters:
            accessToken     string              token required to gain access to the resource
            offset          int                 number of entries to skip
            limit           int                 maximum number of entries to return
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param user_id: id of the user

            Returns:
            :return: a JSON array with the data for the requested matches
        """
        query_offset, query_limit = self.get_offset_and_limit()
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
        self.write_signed_message(200, 'matches', result)


class MatchHandler(JsonRequestHandler):
    """ Manages request to a specific match"""

    @access_token_required
    def get(self, match_id):
        """ Obtains data for a specific match

            Method: GET
            Path: /matches/{match_id}

            URI Parameters:
            match_id        int                 id of the match

            Request Parameters:
            accessToken     string              token required to gain access to the resource
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param match_id: id of the match

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
            self.write_signed_message(200, 'match', json_result)
        except (AttributeError, BadRequestError, ProtocolBufferDecodeError):
            self.write_signed_error(400, 'Invalid match id')

    @access_token_required
    def delete(self, match_id):
        """ Deletes a match

            Method: DELETE
            Path: /matches/{match_id}

            URI Parameters:
            match_id        int                 id of the match

            Request Parameters:
            accessToken     string              token required to gain access to the resource
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param match_id: id of the match

            Returns:
            :return: Empty response
        """
        try:
            key = ndb.Key(urlsafe=match_id)
            key.delete()
            self.write_signed_message(204)
        except (ProtocolBufferDecodeError, TypeError):
            self.write_signed_error(400, 'Invalid match id')
        except TransactionFailedError:
            self.write_signed_error(400, 'Unable to delete match')


@ndb.transactional(xg=True, retries=2)
def _update_entities(*entities):
    """ Updates entities in a transaction

        Parameters:
        :param entities: entities to be updated

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

    @access_token_required
    def post(self):
        """ Creates a match

            This operation is transactional

            Method: POST
            Path: /matches

            URI Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Request Parameters:
            accessToken     string              token required to gain access to the resource
            match           JSON object         data for the match

            :return: the key for the created match
        """
        match_data = self.get_from_body('match')
        try:
            guest_id = match_data['guest']
            guest_points = match_data['guestPoints']
            host_id = match_data['host']
            host_points = match_data['hostPoints']
            if host_id is guest_id:
                self.write_signed_error(400, 'Host and guest id cannot be equal')
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
            self.write_signed_message(201, 'id', match_key)
        except KeyError:
            self.write_signed_error(400, 'Missing attributes for match')
        except TransactionFailedError:
            self.write_signed_error(507, 'Unable to store match')
        except AttributeError:
            self.write_signed_error(400, 'Invalid id')
        except BadValueError:
            # Thrown when model validations fail
            self.write_signed_error(400, 'Invalid data')
