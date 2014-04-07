#API Reference

The following resources are made available by the *Can You Sink Me* API:

* [Users](#users)
* [Matches](#matches)
* [Leaderboards](#leaderbaords)

The API follows a REST design to control the access and manipulation of these resources. Currently none of the request require authentication, but in future release most will.

All calls accept an optional request argument called `pretty`, which indicates that the output should be human readable when equal to `true`. Not all calls return data, but since the argument also affects error messages it is useful for debugging.

##Users

A user represents a register player and the following properties:

| Property    | Data Type | Description                                                                             |
|:------------|-----------|:----------------------------------------------------------------------------------------|
| id          | string    | Id of the Facebook account associated with the user (*Currently any id works*)          |
| avatar      | string    | Representation of the user's display image (*Currently just a string*)                  |
| countryCode | string    | Abbreviation for the country from where the user plays, used for localized leaderboards |
| displayName | string    | String displayed instead of the user's name                                             |
| experience  | integer   | Total points earned                                                                     |
| loggedIn    | boolean   | Whether the user is logged in or not (*Currenlty not used*)                             |
| rank        | integer   | Level of the user, based on the experience                                              |
| timestamp   | datetime  | Day and time the user was created                                                       |

###Endpoints

| Method | Request URI                              | Description                               |
|:-------|:-----------------------------------------|:------------------------------------------|
| GET    | [/users](#users-1)                       | Obtains user data for a list of user id's |
| POST   | [/users](#users-1)                       | Creates or updates a user                 |
| GET    | [/users/*{id}*](#usersid)                | Obtains a user's data                     |
| PUT    | [/users/*{id}*](#usersid)                | Creates or updates a user                 |
| GET    | [/users/*{id}*/matches](#usersidmatches) | Obtains the matches associated to a use   |###/users
**Method**: GET

Obtains user data for a list of user id's**Request Parameters**:

| Parameter | Data Type  | Description                                       |
|:----------|:-----------|:--------------------------------------------------|
| users     | JSON array | Id's of the users                                 || pretty    | boolean    | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /users HTTP/1.1
Host: canyousinkme.vodkasoft.com

["facebookId1","facebookId2"]
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

| Parameter | Data Type   | Description                                       |
|:----------|:------------|:--------------------------------------------------|
| user      | JSON object | Data for the user                                 || pretty    | boolean     | Whether to output in human readable format or not |Only the following attributes are accepted for `user`:

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

| Parameter | Data Type | Description        |
|:----------|:----------|:-------------------|
| id        | string    | User's Facebook id |**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|| pretty    | boolean   | Whether to output in human readable format or not |**Example Request**

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

**URI Parameters**

| Parameter | Data Type | Description        |
|:----------|:----------|:-------------------|
| id        | string    | User's Facebook id |**Request Parameters**:

| Parameter | Data Type   | Description                                       |
|:----------|:------------|:--------------------------------------------------|
| user      | JSON object | Data for the user                                 || pretty    | boolean     | Whether to output in human readable format or not |Only the following attributes are accepted for `user`:

* id
* avatar
* countryCode
* displayName

All the parameters are required when creating a user but optional when updating it. Other attributes are ignored.**Example Request**

```HTTP
PUT /users/facebookId1 HTTP/1.1
Host: canyousinkme.vodkasoft.com

user={"avatar":"new image"}
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 19

{"id":"facebookId"}
```---

###/users/*{id}*/matches
**Method**: GET

Obtains the matches associated to a use

**URI Parameters**

| Parameter | Data Type | Description        |
|:----------|:----------|:-------------------|
| id        | string    | User's Facebook id |**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|
| offset    | integer   | Number of entries to skip                         |
| limit     | integer   | Maximum number of entries to return               || pretty    | boolean   | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /users/facebookId/matches HTTP/1.1
Host: canyousinkme.vodkasoft.com
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 184

[{"hostPoints":175,"guestPoints":100,"id":"ahNkZXZ-Y2EmptyXlvdS1zaW5rLW1lchEgVNYXRjaBIgAsSCAtICACgw","host":"facebookId","timestamp":"2014-04-07T00:42:49.382506","guest":"facebookId2"}]
```___

##Matches

A match represents a game and has the following properties:

| Property    | Data Type | Description                                               |
|:------------|-----------|:----------------------------------------------------------|
| id          | string    | the id of that uniquely identifies the match              |
| guest       | id        | the id associated with the **User** that joined the match |
| guestPoints | integer   | the points scored by the guest player                     |
| host        | id        | the id associated with the **User** that hosted the match |
| hostPoints  | integer   | the points scored by the host_player                      |

###Endpoints

| Method | Request URI                 | Description                       |
|:-------|:----------------------------|:----------------------------------|
| POST   | [/matches](#matches-1)      | Creates a match                   |
| GET    | [/matches/{id}](#matchesid) | Obtains data for a specific match |
| DELETE | [/matches/{id}](#matchesid) | Deletes a match                   |

###/matches
**Method**: POST

Creates or updates a user**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|
| offset    | integer   | Number of entries to skip                         |
| limit     | integer   | Maximum number of entries to return               || pretty    | boolean   | Whether to output in human readable format or not |The following attributes are required for `match`:

* guest
* guestPoints
* host
* hostPoints

Other attributes are ignored.**Example Request**

```HTTP
POST /matches HTTP/1.1
Host: canyousinkme.vodkasoft.com

{"guest":"facebookId2","guestPoints":100,"host":"facebookId","hostPoints":175}
```

**Example Response**
```HTTP
HTTP/1.1 201 CREATED
Content-Type: application/json; charset=utf-8
Content-Length: 64

{"id":"shNkZXZ-Y2FuLYlvdS1zaW52LY1lchILEgVNYXJjaBiAgICAgICZCmm"}
```---
###/matches/{id}
**Method**: GET

Obtains data for a specific match

**URI Parameters**

| Parameter | Data Type | Description     |
|:----------|:----------|:----------------|
| id        | string    | Id of the match |**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|| pretty    | boolean   | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /matches/shItZXZ-Y2FuLYlvdS1zaW52LY1lchILEgVNYXJjaBiAgICAgICZCmm HTTP/1.1
Host: canyousinkme.vodkasoft.com
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 19

{"hostPoints":175,"guestPoints":100,"id":"shItZXZ-Y2FuLYlvdS1zaW52LY1lchILEgVNYXJjaBiAgICAgICZCmm","host":"facebookId","timestamp":"2014-04-07T01:48:46.494089","guest":"facebookId2"}
```___
**Method**: DELETE

Deletes a match**URI Parameters**

| Parameter | Data Type | Description     |
|:----------|:----------|:----------------|
| id        | string    | Id of the match |**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|
| pretty    | boolean   | Whether to output in human readable format or not |**Example Request**

```HTTP
DELETE /matches HTTP/1.1
Host: canyousinkme.vodkasoft.com
```

**Example Response**
```HTTP
HTTP/1.1 200 NO CONTENT
Content-Type: application/json; charset=utf-8
```
---

##Leaderboards

Leaderboards contain lists of sorted Users according to their experience. There are three types of leaderboards: global, country and friends.

###Endpoints

| Method | Request URI                                             | Description                           |
|:-------|:--------------------------------------------------------|:--------------------------------------|
| GET    | [/leaderboards](#leaderboards-1)                        | Obtains the global leaderboard        |
| GET    | [/leaderboards/{countrycode}](#leaderboardscountrycode) | Obtains the leaderboard for a country |

###/leaderboards

**Method**: GET

Obtains the global leaderboard**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|
| offset    | integer   | Number of entries to skip                         |
| limit     | integer   | Maximum number of entries to return               || pretty    | boolean   | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /leaderboards HTTP/1.1
Host: canyousinkme.vodkasoft.com

["offset":10,"limit":"20"]
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 211

[{"id":"facebookId"},{"id": "facebookId1"}]
```___

###/leaderboards/{countryCode}

**Method**: GET

Obtains the leaderboard for a country

**URI Parameters**

| Parameter   | Data Type | Description                             |
|:------------|:----------|:----------------------------------------|
| countryCode | string    | Code for the country of the leaderboard |**Request Parameters**:

| Parameter | Data Type | Description                                       |
|:----------|:----------|:--------------------------------------------------|
| offset    | integer   | Number of entries to skip                         |
| limit     | integer   | Maximum number of entries to return               || pretty    | boolean   | Whether to output in human readable format or not |**Example Request**

```HTTP
GET /leaderboards/CR HTTP/1.1
Host: canyousinkme.vodkasoft.com

["offset":10,"limit":"20"]
```

**Example Response**
```HTTP
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 211

[{"id":"facebookId"},{"id": "facebookId1"}]
```___

###Friends leaderboards

The friends leaderboards includes only those player who are friends on Facebook with the player who queries the leaderboard. This feature is currently **not available** since Facebook integration is currently not supported.
