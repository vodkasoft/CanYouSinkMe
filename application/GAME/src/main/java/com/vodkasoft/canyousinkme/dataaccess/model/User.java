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

package com.vodkasoft.canyousinkme.dataaccess.model;

import com.google.gson.annotations.SerializedName;

/**
 * A user or game player
 */
public class User {

    /** Default country code used when none is provided */
    private static final String DEFAULT_COUNTRY_CODE = "ZZ";

    /** The code for the country from where the user plays */
    @SerializedName("countryCode")
    private String mCountryCode = DEFAULT_COUNTRY_CODE;

    /** The id of the user */
    @SerializedName("id")
    private final String mId;

    /** The total points earned by the user */
    @SerializedName("experience")
    private int mExperience;

    /** The rank or level the user has obtained */
    @SerializedName("rank")
    private int mRank;

    /**
     * Creates a user
     *
     * @param id the id associated to the user
     */
    public User(String id) {
        mId = id;
    }

    /**
     * Gets the country code of the user
     *
     * @return the code of the country from where the user plays
     */
    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * Gets the experience of the user
     *
     * @return the total points earned by the user
     */
    public int getExperience() {
        return mExperience;
    }

    /**
     * Gets the rank of the user
     *
     * @return the rank or level the user has obtained
     */
    public int getRank() {
        return mRank;
    }
}
