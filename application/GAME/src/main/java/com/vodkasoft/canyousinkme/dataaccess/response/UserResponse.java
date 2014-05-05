package com.vodkasoft.canyousinkme.dataaccess.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.vodkasoft.canyousinkme.dataaccess.model.User;

public class UserResponse extends AuthenticatedResponse {

    @SerializedName("user")
    private final User mUser;

    public UserResponse(User user, String messageAuthenticationCode) {
        super(messageAuthenticationCode);
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    @Override
    protected String getMessage() {
        return new Gson().toJson(mUser);
    }
}
