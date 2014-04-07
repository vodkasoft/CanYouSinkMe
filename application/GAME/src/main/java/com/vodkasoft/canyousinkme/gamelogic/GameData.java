package com.vodkasoft.canyousinkme.gamelogic;

import java.util.Date;

public class GameData {

    private String WinnerFID;

    private String LooserFID;

    private int WinnerScore;

    private int LooserScore;

    private Date Date;

    public GameData(String pWinnerFID, String pLooserFID, int pWinnerScore, int pLooserScore,
            Date pDate) {
        this.WinnerFID = pWinnerFID;
        this.LooserFID = pLooserFID;
        this.WinnerScore = pWinnerScore;
        this.LooserScore = pLooserScore;
        this.Date = pDate;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public int getLooserScore() {
        return LooserScore;
    }

    public void setLooserScore(int looserScore) {
        LooserScore = looserScore;
    }

    public int getWinnerScore() {
        return WinnerScore;
    }

    public void setWinnerScore(int winnerScore) {
        WinnerScore = winnerScore;
    }

    public String getLooserFID() {
        return LooserFID;
    }

    public void setLooserFID(String looserFID) {
        LooserFID = looserFID;
    }

    public String getWinnerFID() {
        return WinnerFID;
    }

    public void setWinnerFID(String winnerFID) {
        WinnerFID = winnerFID;
    }
}
