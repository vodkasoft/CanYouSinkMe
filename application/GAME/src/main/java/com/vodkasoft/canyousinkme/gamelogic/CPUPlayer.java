package com.vodkasoft.canyousinkme.gamelogic;

import java.util.LinkedList;
import java.util.Random;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/26/14.
 */
public class CPUPlayer implements IConstant{


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
        int x = randomGenerator.nextInt(ROWS);
        int y = randomGenerator.nextInt(COLUMNS);
        return new Integer[]{x, y};
    }

    public Integer[] nextMissilePosition() {
        Integer[] position;

        if (lastPositionWasHit) {
            position = getSurroundingPosition();
        } else {
            position = getPosition();
        }

        if (GameManager.getPlayerBoard().isShip(position[X_COORDINATE], position[Y_COORDINATE])) {
            lastHitPosition = position;
            lastPositionWasHit = true;
        } else {
            lastPositionWasHit = false;
        }

        return position;
    }

    private boolean isInBound(Integer[] pPosition){
        return pPosition[X_COORDINATE] < ROWS &&
               pPosition[X_COORDINATE] > -1 &&
               pPosition[Y_COORDINATE] < COLUMNS &&
               pPosition[Y_COORDINATE] > -1 ?
               true : false;
    }

    private Integer[] getSurroundingPosition() {
        Integer[] position = lastHitPosition;

        switch (++lastSurroundingPositionCode) {
            case NORTH:
                position[Y_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case NORTHEAST:
                position[X_COORDINATE]++;
                position[Y_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case EAST:
                position[X_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case SOUTHEAST:
                position[X_COORDINATE]++;
                position[Y_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case SOUTH:
                position[Y_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case SOUTHWEST:
                position[X_COORDINATE]--;
                position[Y_COORDINATE]++;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case WEST:
                position[X_COORDINATE]--;
                if (isInBound(position)){
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case NORTHWEST:
                position[X_COORDINATE]--;
                position[Y_COORDINATE]--;
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
