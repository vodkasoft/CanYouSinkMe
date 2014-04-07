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

from google.appengine.ext.db import TransactionFailedError
from google.appengine.ext.ndb import Key

from controller.base import JsonRequestHandler
from model.datastore import User


class _BaseUserHandler(JsonRequestHandler):
    """ Base request handler for user data request """

    def _create_or_update_user_from_json(self, user_data):
        """ Creates or updates a user using data from a JSON object

            Parameters:
            :param user_data: JSON representation of the user data
        """
        try:
            user_data = loads(user_data)
            user_id = user_data['id']
        except (AttributeError, ValueError):
            self.write_message(400, {'error': 'Malformed JSON'})
            return
        self._create_or_update_user(user_id, user_data)

    def _create_or_update_user(self, user_id, user_data):
        """ Creates or updates a user

            Parameters:
            :param user_id: the id of the user
            :param user_data: the data associated to the user
        """
        user = User.get_by_id(user_id)
        try:
            if user is not None:
                # Update user
                if user_data.get('avatar') is not None:
                    user.avatar = user_data['avatar']
                if user_data.get('countryCode') is not None:
                    user.country_code = user_data['countryCode']
                if user_data.get('displayName') is not None:
                    user.display_name = user_data['displayName']
                user.put()
                status_code = 200
            else:
                # Create new user
                User(id=user_id,
                     avatar=user_data['avatar'],
                     country_code=user_data['countryCode'],
                     display_name=user_data['displayName']).put()
                status_code = 201
            self.write_message(status_code, {'id': user_id})
        except KeyError:
            self.write_message(400, {'error': 'Missing attributes for user'})
        except TransactionFailedError:
            self.write_message(400, {'error': 'Unable to store user'})
        except Exception:
            # Thrown when model validations fail
            self.write_message(400, {'error': 'Invalid data'})


class UserHandler(_BaseUserHandler):
    """ Manages requests to a user's data """

    def get(self, user_id):
        """ Obtains a user's data

            Method: GET
            Path: /users/{user_id}

            URI Parameters:
            id              string              the id of the user
            pretty          [true|false]        output in human readable format

            Parameters:
            :param user_id: the id of the user

            Returns:
            :return: user data with id, avatar, countryCode, displayName, rank and experience
        """
        try:
            user = User.get_by_id(user_id)
            json_result = {'id': user_id,
                           'avatar': user.avatar,
                           'countryCode': user.country_code,
                           'displayName': user.display_name,
                           'rank': user.rank,
                           'experience': user.experience}
            self.write_message(200, json_result)
        except AttributeError:
            self.write_message(400, {'error': 'Invalid user id'})

    def put(self, user_id):
        """ Creates or updates a user

            Method: PUT
            Path: /users/{user_id}

            URI Parameters:
            id              string              the id of the user

            Query Parameters:
            user            JSON object         the data for the user
            pretty          [true|false]        output in human readable format

            Parameters:
            :param user_id: the id of the user

            Returns:
            :return: a JSON object with the id of the user
        """
        user_data = self.request.get('user')
        if user_data is None or user_data is '':
            self.write_message(400, {'error': 'No user data was provided'})
            return
        try:
            user_data = loads(user_data)
        except (AttributeError, ValueError):
            self.write_message(400, {'error': 'Malformed JSON'})
            return
        self._create_or_update_user(user_id, user_data)


class UserSetHandler(_BaseUserHandler):
    def get(self):
        """ Obtains user data for a list of user id's

            Method: GET
            Path: /users

            Query Parameters:
            users           JSON array          id's of the users
            pretty          [true|false]        output in human readable format

            Returns:
            :return: a list with the data of the requested users
        """
        users_data = self.request.get('users')
        if users_data is '':
            self.write_message(400, {'error': 'No users data was provided'})
        try:
            user_keys = []
            for user_id in loads(users_data):
                user_keys.append(Key(User, user_id))
            query = User.query(User.key.IN(user_keys))
            result = []
            for user in query.fetch():
                result.append({'id': user.key.id(),
                               'avatar': user.avatar,
                               'countryCode': user.country_code,
                               'displayName': user.display_name,
                               'rank': user.rank})
            self.write_message(200, result)
        except ValueError:
            self.write_message(400, {'error': 'Malformed JSON'})
        except AttributeError:
            self.write_message(400, {'error': 'Invalid data'})

    def post(self):
        """ Creates or updates a user

            Method: POST
            Path: /users

            Query Parameters:
            user            JSON object         the data for the user
            pretty          [true|false]        output in human readable format

            Returns:
            :return: a JSON object with the id of the user
        """
        user_data = self.request.get('user')
        if user_data is None or user_data is '':
            self.write_message(400, {'error': 'No user data was provided'})
            return
        self._create_or_update_user_from_json(user_data)
