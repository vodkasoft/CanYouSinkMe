package com.vodkasoft.canyousinkme.gamelogic;

import android.media.Image;

public class Player {

    private String DisplayName;

    private Image Avatar;

    private int Rank;

    private int CountryCode;

    public Player(String pDisplayName, Image pAvatar, int pRank, int pCountryCode) {
        this.DisplayName = pDisplayName;
        this.Avatar = pAvatar;
        this.Rank = pRank;
        this.CountryCode = pCountryCode;
    }

    public int getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(int countryCode) {
        CountryCode = countryCode;
    }

    public Image getAvatar() {
        return Avatar;
    }

    public void setAvatar(Image avatar) {
        Avatar = avatar;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}
