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
from math import sqrt

from google.appengine.api.datastore_errors import BadValueError
from google.appengine.ext import ndb


def _validate_non_negative_int(value):
    """ Validates that a number not negative

        Parameters:
        :param value: input value to be validated

        Returns:
        :return: sanitized integer value if it can sanitized

        Raises:
        :raises: BadValueError if the number is negative
    """
    if isinstance(value, basestring):
        value = float(value)
    if type(value) not in [int, float] or value < 0:
        logging.info("NOT... HERE")
        raise BadValueError
    return int(value)


""" The maximum score for a single game """
_PERFECT_SCORE = 200


class Application(ndb.Model):
    """ Application Entity """

    """ Name of the application """
    name = ndb.StringProperty('n', required=True)

    """ Small description for the application """
    description = ndb.TextProperty('d', indexed=False, required=False)

    """ Number of calls to the backend """
    api_calls = ndb.IntegerProperty('a', default=0, indexed=False, required=True,
                                    validator=lambda prop, value:
                                    _validate_non_negative_int(value))

    """ User who registered the application """
    registrant = ndb.StringProperty('r', indexed=True, required=True)

    """ Date when the application was created """
    registration_timestamp = ndb.DateTimeProperty('t', auto_now_add=True, indexed=True,
                                                  required=True)

    """ Client Secret Key for authentication """
    client_secret = ndb.StringProperty('c', indexed=False, required=True)

    """ Server Response Key for message authentication """
    server_response_key = ndb.StringProperty('s', indexed=False, required=True)


class User(ndb.Model):
    """ User Entity """

    """ Code for the country from where the user plays """
    country_code = ndb.StringProperty('c', indexed=True, required=True,
                                      validator=lambda prop, value: value.upper())

    """ Total experienced the player has earned """
    experience = ndb.IntegerProperty('e', default=0, indexed=True, required=True,
                                     validator=lambda prop, value:
                                     _validate_non_negative_int(value))

    """ Rank or level for the player, based on the experienced """
    rank = ndb.ComputedProperty(lambda self: int(sqrt(self.experience / float(_PERFECT_SCORE))))

    """ Timestamp from when the user was created """
    timestamp = ndb.DateTimeProperty('t', auto_now_add=True, indexed=False, required=True)


class Match(ndb.Model):
    """ Match Entity """

    """ Key for the player who joined the match """
    guest = ndb.KeyProperty('g', kind=User, indexed=True, required=True)

    """ Points scored by the guest player """
    guest_points = ndb.IntegerProperty('gp', default=0, required=True,
                                       validator=lambda prop, value:
                                       _validate_non_negative_int(value))

    """ Key for the player who hosted the match """
    host = ndb.KeyProperty('h', kind=User, indexed=True, required=True)

    """ Points scored by the host player """
    host_points = ndb.IntegerProperty('hp', default=0, required=True,
                                      validator=lambda prop, value:
                                      _validate_non_negative_int(value))

    """ Timestamp from where the match was created """
    timestamp = ndb.DateTimeProperty('t', auto_now_add=True, indexed=False, required=True)
