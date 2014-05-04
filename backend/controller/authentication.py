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

from google.appengine.api import users

from controller.base import JsonRequestHandler
from model.datastore import Application
from util.crypto import create_access_token, get_application_key


class AuthenticationHandler(JsonRequestHandler):
    """ Manages authentication requests """

    def get(self):
        """ Authenticates the client and sends it an access token

            Method: GET
            Path: /auth

            Request Parameters:
            applicationKey  string              key that identifies the client as an application
            clientSecret    string              secret key to prove the client's identity
            pretty          [true|false]        whether to output in human readable format or not

            Returns:
            :return: an access token if the application key and client secret are valid; otherwise
                     it sends the client an error
        """
        application_key = self.request.get('applicationKey')
        client_secret = self.request.get('clientSecret')
        if application_key is '':
            self.write_error(400, 'No application key was provided')
            return
        if client_secret is '':
            self.write_error(400, 'No client secret was provided')
            return
        application = Application.get_by_id(application_key)
        if application is None:
            self.write_error(400, 'Invalid credentials')
            return
        if application.client_secret != client_secret:
            self.write_error(400, 'Invalid credentials')
            return
        access_token = create_access_token(application_key)
        response_key = application.server_response_key
        self.write_signed_message(200, 'accessToken', access_token, response_key)


def require_admin_login(handler_method):
    """ Ensures that the user that calls the handler is an administrator of the application

        Parameters:
        :param handler_method: the decorated handler that will be called if the called that makes
               the request is a administrator of the application

        Return:
        :return: a wrapper function
    """

    def wrapper(self, *args, **kwargs):
        """ Verifies that the calling user is an administrator of the application before calling the
            decorated handler

            Parameters:
            :param args: the arguments for the decorated function
            :param kwargs: the keyword arguments for the decorated function

            Returns:
            :return: the decorated function result if the access token was valid; otherwise it
                     send an error response and returns None
        """
        user = users.get_current_user()
        if not user:
            self.write_error(401)
        elif not users.is_current_user_admin():
            self.write_error(403)
        else:
            handler_method(self, *args, **kwargs)

    return wrapper


def access_token_required(handler_method):
    """ Ensures that a valid access token is presented before accessing a resource

        Parameters:
        :param handler_method: the decorated handler that will be called if the access token is
               valid

        Returns:
        :return: a wrapper function
    """

    def wrapper(self, *args, **kwargs):
        """ Verifies the existence and validity of an access token before calling the decorated
            handler

            Parameters:
            :param args: the arguments for the decorated function
            :param kwargs: the keyword arguments for the decorated function

            Returns:
            :return: the decorated function result if the access token was valid; otherwise it
                     send an error response and returns None
        """

        if self.request.method in ['GET', 'DELETE']:
            access_token = self.request.get('accessToken')
        else:
            try:
                access_token = loads(self.request.body).get('accessToken')
            except ValueError:
                access_token = None
        if access_token is None or len(access_token) is 0:
            self.write_error(401, 'No access token provided')
            return None
        try:
            application = get_application_key(access_token)
        except (TypeError, ValueError):
            self.write_error(401, 'Invalid access token')
            return None
        if application is not None:
            return handler_method(self, *args, **kwargs)
        else:
            self.write_error(401, 'Invalid access token')
            return None

    return wrapper
