package com.vodkasoft.canyousinkme.dataaccess.response;

import com.google.gson.annotations.SerializedName;

public class MatchIdResponse extends AuthenticatedResponse {

    /** Id for the user */
    @SerializedName("id")
    private final String mId;

    public MatchIdResponse(String id, String messageAuthenticationCode) {
        super(messageAuthenticationCode);
        mId = id;
    }

    public String getId() {
        return mId;
    }

    @Override
    protected String getMessage() {
        return "\"" + mId + "\"";
    }
}
