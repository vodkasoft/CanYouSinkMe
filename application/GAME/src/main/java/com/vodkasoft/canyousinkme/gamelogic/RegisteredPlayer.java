package com.vodkasoft.canyousinkme.gamelogic;

import android.media.Image;

import java.util.List;

public class RegisteredPlayer extends Player {

    private int Points;

    private String FacebookID;

    private List<GameData> GameHistory;

    public RegisteredPlayer(String pDisplayName, Image pAvatar, int pRank,
            int pCountryCode, int pPoints, String pFacebookID, List<GameData> pGameHistory) {
        super(pDisplayName, pAvatar, pRank, pCountryCode);
        this.Points = pPoints;
        this.FacebookID = pFacebookID;
        this.GameHistory = pGameHistory;
    }

    public void updateAvatar(Image newAvatar){
        super.setAvatar(newAvatar);
    }

    public void addPoints(int pPoints){
        Points+=pPoints;
    }

    public List<GameData> getRecentMatches(int pFrom, int pTo){
        return GameHistory.subList(pFrom, pTo);
    }

    public List<GameData> getGameHistory() {
        return GameHistory;
    }

    public void setGameHistory(List<GameData> gameHistory) {
        GameHistory = gameHistory;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
