package com.vodkasoft.canyousinkme.dataaccess;

import com.android.volley.Request;

interface EndpointsConstants {

    static final int AUTHENTICATION_METHOD = Request.Method.GET;
    static final String AUTHENTICATION_PATH = "/auth";
    static final int EXPIRATION_MINUTES = 15;
    /** HTTP Method for the global leaderboards request */
    static final int GLOBAL_LEADERBOARDS_METHOD = Request.Method.GET;
    /** Path for the global leaderboards request */
    static final String GLOBAL_LEADERBOARDS_PATH = "/leaderboards";
    /** HTTP Method for the match creation request */
    static final int MATCH_CREATION_METHOD = Request.Method.POST;
    static final String MATCH_CREATION_PATH = "/matches";
    static final int MINUTES_TO_MILLIS = 60000;
    static final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * MINUTES_TO_MILLIS;
    /** HTTP Method for the user creation request */
    static final int USER_CREATION_METHOD = Request.Method.POST;
    static final String USER_CREATION_PATH = "/users";
    /** Base path for the user retrieval request */
    static final String USER_RETRIEVE_BASE_PATH = "/users";
    /** HTTP Method for the user retrieval request */
    static final int USER_RETRIEVE_METHOD = Request.Method.GET;
}
