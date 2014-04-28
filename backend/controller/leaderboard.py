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
import logging

from controller.base import JsonRequestHandler
from model.datastore import User, Application
from util.crypto import verify_token, create_mac


class _LeaderboardHandler(JsonRequestHandler):
    """ Base request handler for leaderboard request """

    def query_leaderboard(self, query):
        """ Executes a query on the User entity and outputs the resulting id's

            Parameters:
            :param query: query to be executed
        """
        try:
            access_token = self.request.get('accessToken')
            if access_token is None or access_token == '':
                self.write_message(401)
            application_key = verify_token(access_token)
            if not application_key:
                self.write_message(403)
            logging.info(application_key)
            response_key = Application.get_by_id(application_key).server_response_key
            query_offset = self.request.get('offset', 0)
            query_limit = self.request.get('limit', 10)
            if query_offset < 0 or type(query_offset) is not int:
                self.write_error('Invalid offset')
            if query_limit < 0 or type(query_limit) is not int:
                self.write_error('Invalid limit')
            leaderboards = []
            for user_key in query.fetch(keys_only=True, offset=query_offset, limit=query_limit):
                leaderboards.append({'id': user_key.id()})
            result = {'leaderboards': leaderboards,
                      'mac': create_mac(response_key.__str__(), leaderboards.__str__())}
            self.write_message(200, result)
        except:
            self.write_message(401)


class GlobalLeaderboardHandler(_LeaderboardHandler):
    """ Manages requests to the global leaderboard """

    def get(self):
        """ Obtains the global leaderboard

            Method: GET
            Path: /leaderboards

            Request Parameters:
            offset          int                 number of entries to skip
            limit           int                 maximum number of entries to return
            pretty          [true|false]        whether to output in human readable format or not

            Returns:
            :return: an array of user id's
        """
        query = User.query().order(-User.experience)
        self.query_leaderboard(query)


class CountryLeaderboardHandler(_LeaderboardHandler):
    """ Manages requests to the leaderboards per country """

    def get(self, country_code):
        """ Obtains the leaderboard for a country

            Method: GET
            Path: /leaderboards/{country_code}

            URI Parameters:
            country_code    sting               code for the country of the leaderboard

            Request Parameters:
            offset          int                 number of entries to skip
            limit           int                 maximum number of entries to return
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param country_code: code for the country of the leaderboard

            Returns:
            :return: an array of users id's
        """
        query = User.query().order(-User.experience).filter(User.country_code == country_code)
        self.query_leaderboard(query)
