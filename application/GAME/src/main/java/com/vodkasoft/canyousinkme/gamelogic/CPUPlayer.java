package com.vodkasoft.canyousinkme.gamelogic;

import java.util.LinkedList;
import java.util.Random;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/26/14.
 */
public class CPUPlayer {


    public DualMatrix getBoard() {
        return board;
    }

    private DualMatrix board;
    private Integer[] lastHitPosition;
    private boolean lastPositionWasHit;
    private int lastSurroundingPositionCode;
    private Random randomGenerator;
    private LinkedList<Integer> usedPositions;

    public CPUPlayer() {
        board = new DualMatrix(true);
        randomGenerator = new Random();
        usedPositions = new LinkedList<Integer>();
        lastSurroundingPositionCode = 0;
        lastPositionWasHit = false;
    }

    public Integer[] getPosition() {
        int x = randomGenerator.nextInt(Constant.ROWS);
        int y = randomGenerator.nextInt(Constant.COLUMNS);
        return new Integer[]{x, y};
    }

    public Integer[] nextMissilePosition() {
        Integer[] position;

        if (lastPositionWasHit) {
            position = getSurroundingPosition();
        } else {
            position = getPosition();
        }

        if (GameManager.getPlayerBoard().isShip(position[Constant.X_COORDINATE], position[Constant.Y_COORDINATE])) {
            lastHitPosition = position;
            lastPositionWasHit = true;
        } else {
            lastPositionWasHit = false;
        }

        return position;
    }

    private boolean isInBound(Integer[] pPosition){
        return pPosition[Constant.X_COORDINATE] < Constant.ROWS &&
               pPosition[Constant.X_COORDINATE] > -1 &&
               pPosition[Constant.Y_COORDINATE] < Constant.COLUMNS &&
               pPosition[Constant.Y_COORDINATE] > -1 ?
               true : false;
    }

    private Integer[] getSurroundingPosition() {
        Integer[] position = lastHitPosition;

        switch (++lastSurroundingPositionCode) {
            case Constant.NORTH:
                position[Constant.Y_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.NORTHEAST:
                position[Constant.X_COORDINATE]++;
                position[Constant.Y_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.EAST:
                position[Constant.X_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.SOUTHEAST:
                position[Constant.X_COORDINATE]++;
                position[Constant.Y_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.SOUTH:
                position[Constant.Y_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.SOUTHWEST:
                position[Constant.X_COORDINATE]--;
                position[Constant.Y_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.WEST:
                position[Constant.X_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case Constant.NORTHWEST:
                position[Constant.X_COORDINATE]--;
                position[Constant.Y_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            default :
                lastSurroundingPositionCode = 0;
                return  getPosition();

        }

    }


}
