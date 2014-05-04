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

from webapp2 import RequestHandler

from model.datastore import Application
from util.encoding import encode_json
from util.crypto import create_mac, get_application_key


class JsonRequestHandler(RequestHandler):
    """ A request handler that outputs JSON data """

    def get_offset_and_limit(self):
        """ Obtains offset and limit parameters for paginated requests

            Returns:
            :return: a tuple with query and limit if they are valid; sends an error if either is
                     invalid
        """
        query_offset = 0
        query_limit = 10
        try:
            query_offset = int(self.request.get('offset', 0))
        except ValueError:
            self.write_signed_error(400, 'Invalid offset')
        try:
            query_limit = int(self.request.get('limit', 10))
        except ValueError:
            self.write_signed_error(400, 'Invalid limit')
        if query_offset < 0:
            self.write_error(400, 'Invalid offset')
        if query_limit < 0:
            self.write_error(400, 'Invalid limit')
        return query_offset, query_limit

    def get_from_body(self, attribute):
        """ Obtains an object from the request body and optionally decodes it from JSON

            Parameters
            :param attribute: the name of the attribute to be loaded

            Returns
            :return: the object from the JSON or None if it was not found; if the request body is
                     invalid or the JSON is malformed an error is thrown and the response is aborted
        """
        try:
            return loads(self.request.body).get(attribute)
        except ValueError:
            self.write_signed_error(400, 'Invalid request body')
        except TypeError:
            self.write_signed_error(400, 'Malformed JSON')

    def write_error(self, status_code=400, message=None):
        """ Sends an error to the client and aborts the response

            Parameters:
            :param status_code: the HTTP status code for the response
            :param message: the message to add to the response (optional)
        """
        if message:
            self.abort(status_code, detail=message)
        else:
            self.abort(status_code)

    def write_signed_error(self, status_code=400, message=None, key=None):
        """ Sends an error with a message authentication code (if the message is not empty) to the
            client and aborts the response

            Parameters:
            :param status_code: the HTTP status code for the response
            :param message: the message to add to the response (optional)
        """
        if message is not None:
            pretty_print = self.request.get('pretty').lower() == 'true'
            self.response.content_type = 'application/json'
            message_json = self.__construct_signed_message('error', message, key, pretty_print)
            encoded_message = encode_json(message_json, pretty_print)
            self.abort(status_code, detail=encoded_message)
        else:
            self.abort(status_code)

    def write_message(self, status_code, header='data', message=None):
        """ Writes a JSON formatted message to the response body that will be sent to the client

            Parameters:
            :param status_code: status code for the HTTP response
            :param header: top level element for the message data (default: 'data')
            :param message: data to be sent (optional)
        """
        self.__write_message(status_code, header, message, False)

    def write_signed_message(self, status_code, header='data', message=None, key=None):
        """ Writes a signed JSON formatted message to the response body that will be sent to the
            client

            Parameters:
            :param status_code: status code for the HTTP response
            :param header: top level element for the message data (default: 'data')
            :param message: data to be sent (optional)
            :param key: the key with which to sign the message, only relevant if the message should
                        be signed, is none is provided the response key is obtained form the
                        associated application (optional)
        """
        self.__write_message(status_code, header, message, True, key)

    def __write_message(self, status_code, header='data', message=None, sign=True, key=None):
        """ Writes a JSON formatted message to the response body that will be sent to the client

            Parameters:
            :param status_code: status code for the HTTP response
            :param header: top level element for the message data (default: 'data')
            :param message: data to be sent (optional)
            :param sign: whether the message should be signed or not (default: True)
            :param key: the key with which to sign the message, only relevant if the message should
                        be signed, is none is provided the response key is obtained form the
                        associated application (optional)
        """
        self.response.status_int = status_code
        if status_code not in [204, 205] or message is not None:
            pretty_print = self.request.get('pretty').lower() == 'true'
            self.response.content_type = 'application/json'
            message_json = {header: message}
            if sign:
                message_json = self.__construct_signed_message(header, message, key, pretty_print)
            encoded_message = encode_json(message_json, pretty_print)
            self.response.write(encoded_message)

    def __construct_signed_message(self, header, message, key=None, pretty_print=False):
        """ Creates a signed message

            Parameters:
            :param header: top level element for the message data
            :param message: data to be included
            :param key: the key with which to sign the message, is none is provided the response key
                        is obtained form the associated application (optional)

            Returns
            :return: a dictionary with the message and the authentication code
        """
        signed_message = {header: message}
        if key is None:
            application = self.__get_application()
            if application is not None:
                key = application.server_response_key
        if key is not None:
            message = encode_json(message, pretty_print)
            signed_message['MAC'] = create_mac(str(key), message)
            return signed_message
        return None

    def __get_application(self):
        """ Gets the application that made the current request

            Returns:
            :return: the application if the access token is valid; None otherwise
        """
        if self.request.method in ['GET', 'DELETE']:
            access_token = self.request.get('accessToken')
        else:
            try:
                access_token = loads(self.request.body).get('accessToken')
            except ValueError:
                access_token = None
        if access_token is None:
            return None
        application_key = get_application_key(access_token)
        if not application_key:
            return None
        return Application.get_by_id(application_key)
