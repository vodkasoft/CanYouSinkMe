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
from uuid import uuid4

from google.appengine.api.datastore_errors import TransactionFailedError, BadValueError
from google.appengine.ext import ndb
from google.net.proto.ProtocolBuffer import ProtocolBufferDecodeError
from webapp2_extras.appengine.users import admin_required

from controller.base import JsonRequestHandler
from model.datastore import Application


class ApplicationSetHandler(JsonRequestHandler):
    """ Manages requests to the applications """

    @admin_required
    def get(self):
        """ Obtains a user's data

            Method: GET
            Path: /applications

            Request Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Returns:
            :return: all the registered applications
        """
        query = Application.query().order(-Application.registration_timestamp)
        result = []
        for application in query.fetch():
            result.append({'applicationId': application.key.id(),
                           'name': application.name,
                           'apiCalls': application.api_calls,
                           'registrant': application.registrant,
                           'registered': application.registration_timestamp.isoformat()})
        self.write_message(200, result)

    def post(self):
        if not self.require_admin_login():
            return
        app_data = self.request.get('application')
        if app_data is None or app_data is '':
            self.write_message(400, {'error': 'No application data was provided'})
            return
        try:
            app_data = loads(app_data)
        except (AttributeError, ValueError):
            self.write_message(400, {'error': 'Malformed JSON'})
            return

        try:
            # Create new application
            app_key = uuid4().hex
            Application(id=app_key,
                        client_secret=uuid4().hex,
                        server_response_key=uuid4().hex,
                        name=app_data['name'],
                        description=app_data.get('description', ''),
                        registrant='Admin user').put()
            status_code = 201
            self.write_message(status_code, {'applicationKey': app_key})
        except KeyError:
            self.write_message(400, {'error': 'Missing attributes for application'})
        except TransactionFailedError:
            self.write_message(400, {'error': 'Unable to store application'})
        except BadValueError:
            # Thrown when model validations fail
            self.write_message(400, {'error': 'Invalid data'})


class ApplicationHandler(JsonRequestHandler):
    """ Manages requests to the matches associated to a user """

    @admin_required
    def get(self, app_key):
        try:
            app = Application.get_by_id(app_key)
            json_result = {'applicationKey': app_key,
                           'name': app.name,
                           'description': app.description,
                           'clientSecret': app.client_secret,
                           'serverResponseKey': app.server_response_key}
            self.write_message(200, json_result)
        except AttributeError:
            self.write_message(400, {'error': 'Invalid application key'})

    def delete(self, app_key):
        if not self.require_admin_login():
            return
        try:
            ndb.Key(Application, app_key).delete()
            self.write_message(204)
        except (ProtocolBufferDecodeError, TypeError):
            self.write_error('Invalid application key')
        except TransactionFailedError:
            self.write_error('Unable to delete application')

    def put(self, app_key):
        if not self.require_admin_login():
            return
        app_data = self.request.get('application')
        if app_data is None or app_data is '':
            self.write_message(400, {'error': 'No application data was provided'})
            return
        try:
            app_data = loads(app_data)
        except (AttributeError, ValueError):
            self.write_message(400, {'error': 'Malformed JSON'})
            return

        app = Application.get_by_id(app_key)
        try:
            if app is not None:
                # Update application
                if app_data.get('name') is not None:
                    app.name = app_data['name']
                if app_data.get('description') is not None:
                    app.description = app_data['description']
                app.put()
                status_code = 200
                message = {'applicationKey': app_key}
            else:
                status_code = 400
                message = {'error': 'Invalid application key'}
        except TransactionFailedError:
            status_code = 400
            message = {'error': 'Unable to store application'}
        except BadValueError:
            # Thrown when model validations fail
            status_code = 400
            message = {'error': 'Invalid data'}
        self.write_message(status_code, message)


class ClientSecretKeyHandler(JsonRequestHandler):
    def post(self, app_key):
        if not self.require_admin_login():
            return
        try:
            app = Application.get_by_id(app_key)
            app.client_secret = uuid4().hex
            app.put()
            json_result = {'clientSecret': app.client_secret}
            self.write_message(200, json_result)
        except AttributeError:
            self.write_message(400, {'error': 'Invalid application key'})


class ServerResponseKeyHandler(JsonRequestHandler):
    def post(self, app_key):
        if not self.require_admin_login():
            return
        try:
            app = Application.get_by_id(app_key)
            app.server_response_key = uuid4().hex
            app.put()
            json_result = {'serverResponseKey': app.server_response_key}
            self.write_message(200, json_result)
        except AttributeError:
            self.write_message(400, {'error': 'Invalid application key'})
