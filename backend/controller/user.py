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

from google.appengine.ext.ndb import Key

from controller.base import JsonRequestHandler
from model.datastore import User


class UserHandler(JsonRequestHandler):
    """ Manages requests to a user's data """

    def get(self, user_id):
        """ Obtains a user's data

            Method: GET
            Path: /users/{user_id}

            URI Parameters:
            id              string              id of the user

            Request Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param user_id: id of the user

            Returns:
            :return: user data with id, avatar, countryCode, displayName, rank and experience
        """
        try:
            user = User.get_by_id(user_id)
            json_result = {'id': user_id,
                           'countryCode': user.country_code,
                           'rank': user.rank,
                           'experience': user.experience}
            self.write_message(200, json_result)
        except AttributeError:
            self.write_message(400, {'error': 'Invalid user id'})


class UserSetHandler(JsonRequestHandler):
    def get(self):
        """ Obtains user data for a list of user id's

            Method: GET
            Path: /users

            Request Parameters:
            users           JSON array          id's of the users
            pretty          [true|false]        whether to output in human readable format or not

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
                               'countryCode': user.country_code,
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

            Request Parameters:
            user            JSON object         data for the user
            pretty          [true|false]        whether to output in human readable format or not

            Returns:
            :return: a JSON object with the id of the user
        """
        user_id = loads(self.request.body).get('id')
        if user_id is None or user_id is '':
            self.write_message(400, {'error': 'No user id was provided'})
            return
        User(id=user_id,
             country_code=self.request.headers['X-Appengine-Country']).put()
        self.write_message(201, {'id': user_id})
