package com.vodkasoft.canyousinkme.dataaccess.request;

import com.google.gson.annotations.SerializedName;

/** Container for a request on user id */
public class UseCreationRequest extends AuthenticatedRequest {

    /** Id for the user */
    @SerializedName("id")
    private final String mId;

    /**
     * Creates a UserId
     *
     * @param id the id that will be wrapped
     */
    public UseCreationRequest(String id, String accessToken) {
        super(accessToken);
        mId = id;
    }
}
