package com.vodkasoft.canyousinkme.gamelogic;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class MissileMessage {

    private String playerId;
    private int xCoordinate;
    private int yCoordinate;

    public MissileMessage(String playerId, int xCoordinate, int yCoordinate) {
        this.playerId = playerId;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
