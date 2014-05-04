package com.vodkasoft.canyousinkme.dataaccess.request;

import com.google.gson.annotations.SerializedName;

public class MatchCreationRequest extends AuthenticatedRequest {

    @SerializedName("match")
    private MatchCreationRequestInnerData mMessageData;

    public MatchCreationRequest(String hostId, String guestId, int hostPoints, int guestPoints,
            String accessToken) {
        super(accessToken);
        mMessageData = new MatchCreationRequestInnerData(hostId, guestId, hostPoints, guestPoints);
    }

    private class MatchCreationRequestInnerData {

        @SerializedName("guest")
        private final String mGuestId;

        @SerializedName("guestPoints")
        private final int mGuestPoints;

        @SerializedName("host")
        private final String mHostId;

        @SerializedName("hostPoints")
        private final int mHostPoints;

        public MatchCreationRequestInnerData(String hostId, String guestId, int hostPoints,
                int guestPoints) {
            mHostId = hostId;
            mGuestId = guestId;
            mHostPoints = hostPoints;
            mGuestPoints = guestPoints;
        }
    }
}
