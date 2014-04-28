from json import loads

from controller.base import JsonRequestHandler
from model.datastore import Application
from util.crypto import create_token, verify_token


class AuthenticationHandler(JsonRequestHandler):
    def get(self):
        application_key = self.request.get('applicationKey')
        client_secret = self.request.get('clientSecret')
        if application_key is '':
            self.write_message(400, {'error': 'No application key was provided'})
            return
        if client_secret is '':
            self.write_message(400, {'error': 'No client secret was provided'})
            return
        application = Application.get_by_id(application_key)
        if application is None:
            self.write_message(400, {'error': 'Invalid credentials'})
            return
        if application.client_secret != client_secret:
            self.write_message(400, {'error': 'Invalid credentials'})
            return
        self.write_message(200, {'accessToken': create_token(application_key)})

    def post(self):
        token = loads(self.request.body).get('accessToken')
        if not token:
            self.write_message(401)
            return
        if not verify_token(token):
            self.write_message(403)
            return
        self.write_message(200, {'message': 'success'})
