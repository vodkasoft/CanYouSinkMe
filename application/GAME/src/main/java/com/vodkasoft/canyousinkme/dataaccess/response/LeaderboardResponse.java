package com.vodkasoft.canyousinkme.dataaccess.response;

import com.google.gson.annotations.SerializedName;

import com.vodkasoft.canyousinkme.dataaccess.model.User;

import java.util.Arrays;
import java.util.List;

public class LeaderboardResponse extends AuthenticatedResponse {

    @SerializedName("leaderboard")
    private User[] mLeaderboard;

    public LeaderboardResponse(User[] leaderboard, String messageAuthenticationCode) {
        super(messageAuthenticationCode);
        mLeaderboard = leaderboard;
    }

    public List<User> getLeaderboard() {
        return Arrays.asList(mLeaderboard);
    }

    @Override
    protected String getMessage() {
        return null;
    }
}
