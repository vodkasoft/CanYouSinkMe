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

from json import dumps


def encode_json(data, pretty):
    """ Formats an object as JSON

        Parameters:
        :param data: data the will be encoded
        :param pretty: whether the output should be human readable or not

        Returns:
        :return: data encoded as JSON
    """
    if pretty:
        return format_as_pretty_json(data)
    else:
        return format_as_compact_json(data)


def format_as_pretty_json(data):
    """ Encodes an object as JSON that is human readable

        Parameters:
        :param data: data that will be encoded

        Returns:
        :return: data encoded in a human readable JSON format
    """
    return dumps(data, sort_keys=True, indent=4, separators=(',', ': '))


# Formats
def format_as_compact_json(data):
    """ Encodes an object as JSON with the least amount of characters


        Parameters:
        :param data: data that will be encoded

        Returns:
        :return: data encoded in a compact JSON format
    """
    return dumps(data, separators=(',', ':'))
