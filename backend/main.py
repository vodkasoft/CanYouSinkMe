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

import webapp2

from controller.user import UserHandler, UserSetHandler
from controller.match import MatchHandler, MatchSetHandler, UserMatchesHandler
from controller.leaderboard import CountryLeaderboardHandler, GlobalLeaderboardHandler

""" Application and routes for request handlers """
app = webapp2.WSGIApplication([(r'/users', UserSetHandler),
                               (r'/users/(\S*)/matches', UserMatchesHandler),
                               (r'/users/(\S*)', UserHandler),
                               (r'/matches', MatchSetHandler),
                               (r'/matches/(\S*)', MatchHandler),
                               (r'/leaderboards', GlobalLeaderboardHandler),
                               (r'/leaderboards/([a-zA-Z]{2})', CountryLeaderboardHandler)],
                              debug=False)
