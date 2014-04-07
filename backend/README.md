#API Reference

The following resources are made available by the *Can You Sink Me* API:

* [Users](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#users)
* [Matches](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#matches)
* [Leaderboards](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#leaderbaords)

The API follows a REST design to control the access and manipulation of these resources. Currently none of the request require authentication, but in future release most will. Most calls accept an optional argument called `pretty`, which indicates that the output should be human readable when equal to `true`.

##Users

A user represents a register player and the following properties:

| Property | Data Type | Description |
|:---------|-----------|:------------|
| id | string | Id of the Facebook account associated with the user (*Currently any id works*)|
| avatar | string | Representation of the user's display image (*Currently just a string*) |
| countryCode | string | Abbreviation for the country from where the user plays, used for localized leaderboards |
| displayName | string | String displayed instead of the user's name |
| experience | integer | Total points earned |
| loggedIn | boolean | Whether the user is logged in or not (*Currenlty not used*) |
| rank | integer | Level of the user, based on the experience |
| timestamp | datetime | Day and time the user was created |

###Endpoints

| Method | Request URI | Description | Supports *pretty* |
|:-------|:------------|:------------|:-----------------:|
| GET | [/users](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#users-1) | Obtains user data for a list of user id's | Yes |
| POST | [/users](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#users-1) | Creates or updates a user | Yes |
| GET | [/users/*{id}*](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#usersid) | Obtains a user's data | Yes |
| PUT | [/users/*{id}*](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#usersid) | Creates or updates a user | Yes |
| GET | [/users/*{id}*/matches](https://github.com/vodkasoft/CanYouSinkMe/tree/master/backend#usersidmatches) | Obtains the matches associated to a use | Yes###/users
**Method**: GET

Obtains user data for a list of user id's**Request Parameters**:

| Parameter | Data Type | Description |
|:----------|:----------|:------------|
| users | JSON array | Id's of the users || pretty | boolean | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /users HTTP/1.1
Host: canyousinkme.vodkasoft.com

["facebookId1", "facebookId2"]
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 211

[{"rank":5,"displayName":"Example User 1","avatar":"image data","countryCode":"CR","id":"facebookId1"},{"rank":2,"displayName":"Example User 2","avatar":"other image data","countryCode":"US","id":"facebookId2"}]
```___

**Method**: POST

Creates or updates a user**Request Parameters**:

| Parameter | Data Type | Description |
|:----------|:----------|:------------|
| user | JSON object | Data for the user || pretty | boolean | Whether to output in human readable format or not |Only the following attributes are accepted:

* id
* avatar
* countryCode
* displayName

All the parameters are required when creating a user but optional when updating it. Other attributes are ignored.**Example Request**

```HTTP
POST /users HTTP/1.1
Host: canyousinkme.vodkasoft.com

user={"id":"facebookId","avatar":"image data","countryCode":"CR","displayName":"Example User"}
```

**Example Response**
```HTTP
HTTP/1.1 201 CREATED
Content-Type: application/json; charset=utf-8
Content-Length: 19

{"id":"facebookId"}
```---

###/users/*{id}*
**Method**: GET

Obtains a user's data

**URI Parameters**

| Parameter | Data Type | Description |
|:----------|:----------|:------------|
| id | string | User's Facebook id |**Request Parameters**:

| Parameter | Data Type | Description |
|:----------|:----------|:------------|| pretty | boolean | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /users/facebookId HTTP/1.1
Host: canyousinkme.vodkasoft.com
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 19

{"rank":5,"displayName":"Example User","avatar":"image data","countryCode":"CR","id":"facebookId"}
```___
**Method**: PUT

Creates or updates a user

Creates or updates a user**Request Parameters**:

| Parameter | Data Type | Description |
|:----------|:----------|:------------|
| user | JSON object | Data for the user || pretty | boolean | Whether to output in human readable format or not |Only the following attributes are accepted:

* id
* avatar
* countryCode
* displayName

All the parameters are required when creating a user but optional when updating it. Other attributes are ignored.**Example Request**

```HTTP
POST /users HTTP/1.1
Host: canyousinkme.vodkasoft.com

user={"id":"facebookId","avatar":"image data","countryCode":"CR","displayName":"Example User"}
```

**Example Response**
```HTTP
HTTP/1.1 201 CREATED
Content-Type: application/json; charset=utf-8
Content-Length: 19

{"id":"facebookId"}
```---

###/users/*{id}*/matches
**Method**: GET

Obtains the matches associated to a use

**URI Parameters**

| Parameter | Data Type | Description |
|:----------|:----------|:------------|
| id | string | User's Facebook id |**Request Parameters**:

| Parameter | Data Type | Description |
|:----------|:----------|:------------|
| offset | integer | Number of entries to skip |
| limit | integer | Maximum number of entries to return || pretty | boolean | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /users/facebookId/matches HTTP/1.1
Host: canyousinkme.vodkasoft.com
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 184

[{"hostPoints":175,"guestPoints":100,"id":"ahNkZXZ-Y2FuLXlvdS1zaW5rLW1lchILEgVNYXRjaBiAgICAgICACgw","host":"facebookId","timestamp":"2014-04-07T00:42:49.382506","guest":"facebookId2"}]
```___

##Matches

A match represents a game and has the following properties:

| Property | Data Type | Description |
|:---------|-----------|:------------|
| id | string | the id of that uniquely identifies the match |
| guest | id | the id associated with the **User** that joined the match |
| guestPoints | integer | the points scored by the guest player |
| host | id | the id associated with the **User** that hosted the match |
| hostPoints | integer | the points scored by the host_player |

##Leaderbaords

Leaderboards contain lists of sorted Users according to their experience. There are three types of leaderboards: global, country and friends.

###Global leaderboard

The global leaderboard includes all registerd players.

###Country leaderboards

The country leaderboards includes only the players registed on a country.

###Friends leaderboards

The friends leaderboards includes only those player who are friends on Facebook with the player who queries the leaderboard. This feature is currently **not available** since Facebook integration is currently not supported.
