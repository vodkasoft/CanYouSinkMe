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

from webapp2 import RequestHandler

from util.format import encode_json


class JsonRequestHandler(RequestHandler):
    """ A request handler that outputs JSON data """

    def write_error(self, message='Unknown error'):
        """ Outputs an error and sends a 400 status code

            Parameters:
            :param message: the error message, default 'Unknown error'
        """
        self.write_message(400, {'error': message})

    def write_message(self, status_code, message=None):
        """ Outputs a message with a status code as a JSON object

            Parameters:
            :param status_code: the status code for the HTTP response
            :param message: the message, optional
        """
        pretty_print = self.request.get('pretty') == 'true'
        self.response.status = status_code
        if message is not None or message is not '' or status_code not in [204, 205]:
            self.response.content_type = 'application/json'
            output = encode_json(message, pretty_print)
            self.response.write(output)
