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

from math import sqrt

from google.appengine.ext import ndb


def _validate_positive_int(value):
    """ Validates that a number is positive

        Parameters:
        :param value: the input value to be validated

        Returns:
        :return: the sanitized value if it can sanitized
    """
    if isinstance(value, basestring):
        value = float(value)
    if value < 0:
        raise Exception
    return int(value)


""" The maximum score for a single game """
_PERFECT_SCORE = 200


class User(ndb.Model):
    """ User Entity """

    """ The image that is displayed for the user """
    avatar = ndb.TextProperty('a', indexed=False, required=True)

    """ The code for the country from where the user plays """
    country_code = ndb.StringProperty('c', indexed=True, required=True,
                                      validator=lambda prop, value: value.upper())

    """ The name displayed during matches and match finding """
    display_name = ndb.StringProperty('d', required=True)

    """ The total experienced the player has earned """
    experience = ndb.IntegerProperty('e', default=0, indexed=True, required=True,
                                     validator=lambda prop, value: _validate_positive_int(value))

    """ Whether the player is logged in or not """
    logged_in = ndb.BooleanProperty('l', default=False, required=True)

    """ The rank or level for the player, based on the experienced """
    rank = ndb.ComputedProperty(lambda self: int(sqrt(self.experience / float(_PERFECT_SCORE))))

    """ The timestamp from when the user was created """
    timestamp = ndb.DateTimeProperty('t', auto_now_add=True, indexed=False, required=True)


class Match(ndb.Model):
    """ Match Entity """

    """ The key for the player who joined the match """
    guest = ndb.KeyProperty('g', kind=User, indexed=True, required=True)

    """ The points scored by the guest player """
    guest_points = ndb.IntegerProperty('gp', default=0, required=True,
                                       validator=lambda prop, value: _validate_positive_int(value))

    """ The key for the player who hosted the match """
    host = ndb.KeyProperty('h', kind=User, indexed=True, required=True)

    """ The points scored by the host player """
    host_points = ndb.IntegerProperty('hp', default=0, required=True,
                                      validator=lambda prop, value: _validate_positive_int(value))

    """ The timestamp from where the match was created """
    timestamp = ndb.DateTimeProperty('t', auto_now_add=True, indexed=False, required=True)
