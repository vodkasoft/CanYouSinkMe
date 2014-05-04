/*
 * Copyright 2014 Vodkasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vodkasoft.canyousinkme.dataaccess;

import com.google.gson.Gson;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vodkasoft.canyousinkme.dataaccess.model.User;
import com.vodkasoft.canyousinkme.dataaccess.request.MatchCreationRequest;
import com.vodkasoft.canyousinkme.dataaccess.request.UseCreationRequest;
import com.vodkasoft.canyousinkme.dataaccess.response.AccessTokenResponse;
import com.vodkasoft.canyousinkme.dataaccess.response.LeaderboardResponse;
import com.vodkasoft.canyousinkme.dataaccess.response.MatchIdResponse;
import com.vodkasoft.canyousinkme.dataaccess.response.UserIdResponse;
import com.vodkasoft.canyousinkme.dataaccess.response.UserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import java.util.Date;
import java.util.List;

/**
 * Manages access to backend data
 */
public class BackendServiceAccessor implements EndpointsConstants {

    /** Key that identifies the application */
    private final String mApplicationKey;

    /** Key to prove the client's identity */
    private final String mClientSecret;

    /** Host to which requests will be sent */
    private final String mHost;

    /** Queue for requests that will be processed */
    private final RequestQueue mRequestQueue;

    /** JSON serializer */
    private final Gson mSerializer = new Gson();

    /** Key to verify messages from the server */
    private final String mServerResponseKey;

    /** Access token required for authenticated requests */
    private String mAccessToken = null;

    /** Expiration date and time for the current access token */
    private Date mExpiration;

    /**
     * Creates a BackendServiceAccessor
     *
     * @param host    the host to which requests will be sent
     * @param context the context to which the requests will be relative
     */
    public BackendServiceAccessor(String host, String applicationKey,
            String clientSecret,
            String serverResponseKey,
            Context context) {
        mHost = host;
        mApplicationKey = applicationKey;
        mClientSecret = clientSecret;
        mServerResponseKey = serverResponseKey;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void createMatch(final String hostId, final String guestId, final int hostPoints,
            final int guestPoints, final Listener<String> listener) {
        refreshAccessToken(new Listener<Void>() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void onResponse(Void response) {
                JsonObjectRequest request = null;
                MatchCreationRequest requestData = new MatchCreationRequest(hostId, guestId,
                        hostPoints, guestPoints, mAccessToken);
                try {
                    request = new JsonObjectRequest(
                            MATCH_CREATION_METHOD,
                            mHost + MATCH_CREATION_PATH,
                            new JSONObject(mSerializer.toJson(requestData)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    MatchIdResponse response = mSerializer.fromJson(
                                            jsonObject.toString(), MatchIdResponse.class);
                                    if (response == null) {
                                        listener.onError("Unable to create match");
                                    } else if (!response.messageIsValid(mServerResponseKey)) {
                                        listener.onError("Invalid server response");
                                    } else {
                                        listener.onResponse(response.getId());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    listener.onError(new String(volleyError.networkResponse.data));
                                }
                            }
                    );
                } catch (JSONException e) {
                    listener.onError("Unable to create user");
                }
                mRequestQueue.add(request);
            }
        });
    }

    /**
     * Creates a new user
     *
     * @param id       the id for the user
     * @param listener the listener that will handle the callbacks
     */
    public void createUser(final String id, final Listener<String> listener) {
        refreshAccessToken(new Listener<Void>() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void onResponse(Void response) {
                JsonObjectRequest request = null;
                UseCreationRequest requestData = new UseCreationRequest(id, mAccessToken);
                try {
                    request = new JsonObjectRequest(
                            USER_CREATION_METHOD,
                            mHost + USER_CREATION_PATH,
                            new JSONObject(mSerializer.toJson(requestData)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    UserIdResponse response = mSerializer.fromJson(
                                            jsonObject.toString(), UserIdResponse.class);
                                    if (response == null) {
                                        listener.onError("Unable to create user");
                                    } else if (!response.messageIsValid(mServerResponseKey)) {
                                        listener.onError("Invalid server response");
                                    } else {
                                        listener.onResponse(response.getId());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    listener.onError(new String(volleyError.networkResponse.data));
                                }
                            }
                    );
                } catch (JSONException e) {
                    listener.onError("Unable to create user");
                }
                mRequestQueue.add(request);
            }
        });
    }

    /**
     * Get global leaderboards
     *
     * @param listener the listener that will handle the callbacks
     */
    public void getGlobalLeaderboards(final Listener<List<User>> listener) {
        refreshAccessToken(new Listener<Void>() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void onResponse(Void response) {
                StringRequest request = new StringRequest(
                        GLOBAL_LEADERBOARDS_METHOD,
                        mHost + GLOBAL_LEADERBOARDS_PATH + '?' +
                                "accessToken=" + mAccessToken.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String jsonUserArray) {
                                LeaderboardResponse response = mSerializer
                                        .fromJson(jsonUserArray, LeaderboardResponse.class);
                                if (response == null) {
                                    listener.onError("Unable to retrieve leaderboard");
                                } else if (!response.messageIsValid(mServerResponseKey)) {
                                    listener.onError("Invalid server response");
                                } else {
                                    listener.onResponse(response.getLeaderboard());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                listener.onError(new String(volleyError.networkResponse.data));
                            }
                        }
                );
                mRequestQueue.add(request);
            }
        });
    }

    /**
     * Gets the data for a user
     *
     * @param id       the id of the user
     * @param listener the listeners that will handle the callbacks
     */
    public void getUser(final String id, final Listener<User> listener) {
        refreshAccessToken(new Listener<Void>() {
            @Override
            public void onError(String error) {
                listener.onError(error);
            }

            @Override
            public void onResponse(Void response) {
                JsonObjectRequest request = new JsonObjectRequest(
                        USER_RETRIEVE_METHOD,
                        mHost + USER_RETRIEVE_BASE_PATH + '/' + id + '?' +
                                "accessToken=" + mAccessToken.toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                UserResponse response = mSerializer
                                        .fromJson(jsonObject.toString(), UserResponse.class);
                                if (response == null) {
                                    listener.onError("Unable to retrieve user");
                                } else if (!response.messageIsValid(mServerResponseKey)) {
                                    listener.onError("Invalid server response");
                                } else {
                                    listener.onResponse(response.getUser());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                listener.onError(new String(volleyError.networkResponse.data));
                            }
                        }
                );
                mRequestQueue.add(request);
            }
        });
    }

    private void refreshAccessToken(final Listener<Void> listener) {
        if (mAccessToken != null && mExpiration.after(new Date())) {
            listener.onResponse(null);
        }
        StringRequest request = new StringRequest(
                AUTHENTICATION_METHOD,
                mHost + AUTHENTICATION_PATH + '?' +
                        "applicationKey=" + mApplicationKey + '&' +
                        "clientSecret=" + mClientSecret,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonUserArray) {
                        AccessTokenResponse response = mSerializer
                                .fromJson(jsonUserArray, AccessTokenResponse.class);
                        if (response == null) {
                            listener.onError("Unable to retrieve user");
                        } else if (!response.messageIsValid(mServerResponseKey)) {
                            listener.onError("Invalid server response");
                        } else {
                            mAccessToken = response.getAccessToken();
                            mExpiration = new Date(System.currentTimeMillis() + EXPIRATION_MILLIS);
                            listener.onResponse(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onError(new String(volleyError.networkResponse.data));
                    }
                }
        );
        mRequestQueue.add(request);
    }

    /**
     * Handler for backend requests callbacks
     *
     * @param <R> the type of the object sent on a successful callback
     */
    public interface Listener<R> {

        void onError(String error);

        void onResponse(R response);
    }
}
