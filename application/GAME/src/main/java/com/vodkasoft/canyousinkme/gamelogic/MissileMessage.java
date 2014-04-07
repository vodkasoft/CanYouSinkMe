package com.vodkasoft.canyousinkme.gamelogic;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class MissileMessage {

    private int xCoordinate;
    private int yCoordinate;

    public MissileMessage(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
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
