package com.vodkasoft.canyousinkme.dataaccess.response;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse extends AuthenticatedResponse {

    @SerializedName("accessToken")
    private final String mAccessToken;

    public AccessTokenResponse(String accessToken, String messageAuthenticationCode) {
        super(messageAuthenticationCode);
        mAccessToken = accessToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    protected String getMessage() {
        return null;
    }
}
