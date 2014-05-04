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

from uuid import uuid4

from google.appengine.api import users
from google.appengine.api.datastore_errors import TransactionFailedError, BadValueError
from google.appengine.ext import ndb
from google.net.proto.ProtocolBuffer import ProtocolBufferDecodeError

from controller.authentication import require_admin_login
from controller.base import JsonRequestHandler
from model.datastore import Application


class ApplicationSetHandler(JsonRequestHandler):
    """ Manages requests to the applications """

    @require_admin_login
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
        self.write_message(200, 'applications', result)

    @require_admin_login
    def post(self):
        """ Creates an application

            Method: POST
            Path: /applications

            Request Parameters:
            application     JSON object         data for the application
            pretty          [true|false]        whether to output in human readable format or not

            Returns:
            :return: the application key for the new application
        """
        application_data = self.get_from_body('application')
        try:
            # Create new application
            user = users.get_current_user()
            if user is None:
                self.write_error(400, 'Unable assign application to user')
            application_key = uuid4().hex
            Application(id=application_key,
                        client_secret=uuid4().hex,
                        server_response_key=uuid4().hex,
                        name=application_data['name'],
                        description=application_data.get('description', ''),
                        registrant=user.nickname()).put()
            self.write_message(201, 'applicationKey', application_key)
        except KeyError:
            self.write_error(400, 'Missing attributes for application')
        except TransactionFailedError:
            self.write_error(400, 'Unable to store application')
        except BadValueError:
            # Thrown when model validations fail
            self.write_error(400, 'Invalid data')


class ApplicationHandler(JsonRequestHandler):
    """ Manages requests to a specific application """

    @require_admin_login
    def get(self, application_key):
        """ Obtains the data for an application

            Method: GET
            Path: /applications/{application_key}

            URI Parameters:
            application_key string              the key that identifies the application


            Request Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param application_key: the key that identifies the application

            Returns:
            :return: the data for the application
        """
        try:
            application = Application.get_by_id(application_key)
            json_result = {'applicationKey': application_key,
                           'name': application.name,
                           'description': application.description,
                           'clientSecret': application.client_secret,
                           'serverResponseKey': application.server_response_key}
            self.write_message(200, 'application', json_result)
        except AttributeError:
            self.write_error(400, 'Invalid application key')

    @require_admin_login
    def delete(self, application_key):
        """ Deletes an application

            Method: DELETE
            Path: /applications/{application_key}

            URI Parameters:
            application_key string              the key that identifies the application

            Request Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param application_key: the key that identifies the application
        """
        try:
            ndb.Key(Application, application_key).delete()
            self.write_message(204)
        except (ProtocolBufferDecodeError, TypeError):
            self.write_error(400, 'Invalid application key')
        except TransactionFailedError:
            self.write_error(400, 'Unable to delete application')

    @require_admin_login
    def put(self, application_key):
        """ Updates an application

            Method: PUT
            Path: /applications/{application_key}

            URI Parameters:
            application_key string              the key that identifies the application

            Request Parameters:
            application     JSON object         the new data for the application
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param application_key: the key that identifies the application

            Returns:
            :return: the application key
        """
        application_data = self.get_from_body('application')
        application = Application.get_by_id(application_key)
        try:
            if application is not None:
                # Update application
                if application_data.get('name') is not None:
                    application.name = application_data['name']
                if application_data.get('description') is not None:
                    application.description = application_data['description']
                application.put()
                self.write_message(200, 'applicationKey', application_key)
            else:
                self.write_error(400, 'Invalid application key')
        except TransactionFailedError:
            self.write_error(400, 'Unable to store application')
        except BadValueError:
            # Thrown when model validations fail
            self.write_error(400, 'Invalid data')


class ClientSecretKeyHandler(JsonRequestHandler):
    """ Manages request to an application's client secret key """

    @require_admin_login
    def post(self, application_key):
        """ Regenerates a client secret key

            Method: POST
            Path: /applications/{application_key}/client/secret

            URI Parameters:
            application_key string              the key that identifies the application

            Request Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param application_key: the key that identifies the application

            Returns
            :return: the new client secret key
        """
        try:
            application = Application.get_by_id(application_key)
            application.client_secret = uuid4().hex
            application.put()
            self.write_message(200, 'clientSecret', application.client_secret)
        except AttributeError:
            self.write_error(400, 'Invalid application key')


class ServerResponseKeyHandler(JsonRequestHandler):
    """ Manages request to an application's server response key """

    @require_admin_login
    def post(self, application_key):
        """ Regenerates a server response key

            Method: POST
            Path: /applications/{application_key}/server/key

            URI Parameters:
            application_key string              the key that identifies the application

            Request Parameters:
            pretty          [true|false]        whether to output in human readable format or not

            Parameters:
            :param application_key: the key that identifies the application

            Returns
            :return: the new client secret key
        """
        try:
            application = Application.get_by_id(application_key)
            application.server_response_key = uuid4().hex
            application.put()
            self.write_message(200, 'serverResponseKey', application.server_response_key)
        except AttributeError:
            self.write_message(400, 'Invalid application key')
