package com.vodkasoft.canyousinkme.dataaccess.request;

import com.google.gson.annotations.SerializedName;

public abstract class AuthenticatedRequest {

    @SerializedName("accessToken")
    private final String mAccessToken;

    public AuthenticatedRequest(String accessToken) {
        mAccessToken = accessToken;
    }
}
