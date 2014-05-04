package com.vodkasoft.canyousinkme.dataaccess.response;

import com.google.gson.annotations.SerializedName;

public abstract class AuthenticatedResponse {

    @SerializedName("MAC")
    private String mMessageAuthenticationCode;

    protected AuthenticatedResponse(String messageAuthenticationCode) {
        mMessageAuthenticationCode = messageAuthenticationCode;
    }

    public boolean messageIsValid(String serverResponseKey) {
        return true;
    }

    protected abstract String getMessage();
}
