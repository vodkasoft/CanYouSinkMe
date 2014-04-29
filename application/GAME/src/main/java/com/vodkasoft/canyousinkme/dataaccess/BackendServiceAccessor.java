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
import com.google.gson.annotations.SerializedName;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vodkasoft.canyousinkme.dataaccess.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

/**
 * Manages access to backend data
 */
public class BackendServiceAccessor {

    /** HTTP Method for the global leaderboards request */
    private static final int GLOBAL_LEADERBOARDS_METHOD = Request.Method.GET;

    /** Path for the global leaderboards request */
    private static final String GLOBAL_LEADERBOARDS_PATH = "/leaderboards?accessToken=27b807e5e21f47b04fbfa7fd34d0ccb1c38a211308cecf331c6e97ffa3343d553cf1ca412ff904d5136d1798e15d372e92dc6965a3321950e5d15f2d3b8090400a89c3a6fd2b26269ac1f2b7a1fadda77c5f4d898d1f416b8fbe592e1db66443b15126b904fb819d1d465ddc9eeaf910e4a0ad4956";

    /** HTTP Method for the user creation request */
    private static final int USER_CREATION_METHOD = Request.Method.POST;

    /** Path for the user retrieval request */
    private static final String USER_CREATION_PATH = "/users";

    /** Base path for the user retrieval request */
    private static final String USER_RETRIEVE_BASE_PATH = "/users";

    /** HTTP Method for the user retrieval request */
    private static final int USER_RETRIEVE_METHOD = Request.Method.GET;

    /** Host to which requests will be sent */
    private final String mHost;

    /** Queue for requests that will be processed */
    private final RequestQueue mRequestQueue;

    /** JSON serializer */
    private final Gson mSerializer = new Gson();

    /**
     * Creates a BackendServiceAccessor
     *
     * @param host    the host to which requests will be sent
     * @param context the context to which the requests will be relative
     */
    public BackendServiceAccessor(String host, Context context) {
        mHost = host;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Creates a new user
     *
     * @param id       the id for the user
     * @param listener the listener that will handle the callbacks
     */
    public void createUser(String id, final Listener<String> listener) {
        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(
                    USER_CREATION_METHOD,
                    mHost + USER_CREATION_PATH,
                    new JSONObject(mSerializer.toJson(new UserId(id))),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                listener.onResponse(jsonObject.getString("id"));
                            } catch (JSONException e) {
                                listener.onError("Unable to create user");
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

    /**
     * Get global leaderboards
     *
     * @param listener the listener that will handle the callbacks
     */
    public void getGlobalLeaderboards(final Listener<List<User>> listener) {
        StringRequest request = new StringRequest(
                GLOBAL_LEADERBOARDS_METHOD,
                mHost + GLOBAL_LEADERBOARDS_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonUserArray) {
                        User[] users = mSerializer.fromJson(jsonUserArray, User[].class);
                        if (users == null) {
                            listener.onError("Unable to retrieve leaderboards");
                            return;
                        }
                        listener.onResponse(Arrays.asList(users));
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
     * Gets the data for a user
     *
     * @param id       the id of the user
     * @param listener the listeners that will handle the callbacks
     */
    public void getUser(String id, final Listener<User> listener) {
        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(
                    USER_RETRIEVE_METHOD,
                    mHost + USER_RETRIEVE_BASE_PATH + '/' + id,
                    new JSONObject(mSerializer.toJson(new UserId(id))),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            User user = mSerializer.fromJson(jsonObject.toString(), User.class);
                            if (user == null) {
                                listener.onError("Unable to retrieve user data");
                                return;
                            }
                            listener.onResponse(user);
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
            listener.onError("Unable to retrieve user data");
        }
        mRequestQueue.add(request);
    }

    /**
     * Handler for backend requests callbacks
     *
     * @param <T> the type of the object sent on a successful callback
     */
    public interface Listener<T> {

        void onError(String message);

        void onResponse(T response);
    }

    /** Container for a request on user id */
    private class UserId {

        /** Id for the user */
        @SerializedName("id")
        private final String mId;

        /**
         * Creates a UserId
         *
         * @param id the id that will be wrapped
         */
        public UserId(String id) {
            mId = id;
        }
    }
}
