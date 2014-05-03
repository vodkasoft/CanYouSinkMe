package com.vodkasoft.canyousinkme.gamelogic;

import java.util.LinkedList;
import java.util.Random;

import static com.vodkasoft.canyousinkme.utils.Constant.ROWS;
import static com.vodkasoft.canyousinkme.utils.Constant.COLUMNS;
import static com.vodkasoft.canyousinkme.utils.Constant.X_COORDINATE;
import static com.vodkasoft.canyousinkme.utils.Constant.Y_COORDINATE;
import static com.vodkasoft.canyousinkme.utils.Constant.NORTH;
import static com.vodkasoft.canyousinkme.utils.Constant.NORTHEAST;
import static com.vodkasoft.canyousinkme.utils.Constant.EAST;
import static com.vodkasoft.canyousinkme.utils.Constant.SOUTHEAST;
import static com.vodkasoft.canyousinkme.utils.Constant.SOUTH;
import static com.vodkasoft.canyousinkme.utils.Constant.SOUTHWEST;
import static com.vodkasoft.canyousinkme.utils.Constant.WEST;
import static com.vodkasoft.canyousinkme.utils.Constant.NORTHWEST;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/26/14.
 */
public class CPUPlayer {


    private DualMatrix board;
    private Integer[] lastHitPosition;
    private boolean lastPositionWasHit;
    private int lastSurroundingPositionCode;
    private Random randomGenerator;
    private LinkedList<Integer> usedPositions;

    /**
     * Constructs new CPUPlayer, randomly puts ships on board
     */
    public CPUPlayer() {
        board = new DualMatrix(true);
        randomGenerator = new Random();
        usedPositions = new LinkedList<Integer>();
        lastSurroundingPositionCode = 0;
        lastPositionWasHit = false;
    }

    /**
     * Returns board
     * @return DualMatrix cpuplayer board
     */
    public DualMatrix getBoard() {
        return board;
    }

    /**
     * Returns a random generated point
     * @return Point (x,y)
     */
    public Integer[] getPosition() {
        int x = randomGenerator.nextInt(ROWS);
        int y = randomGenerator.nextInt(COLUMNS);
        return new Integer[]{x, y};
    }

    /**
     * Randomly generates a new missile position for cpu send missile turn
     * @return Point (x,y)
     */
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

    /**
     * When CPU hits a ship, it surrounds the hit position clockwise until he gets a new hit
     * position
     * @return Point (x,y)
     */
    private Integer[] getSurroundingPosition() {
        Integer[] position = lastHitPosition;

        switch (++lastSurroundingPositionCode) {
            case NORTH:
                position[Y_COORDINATE]--;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case NORTHEAST:
                position[X_COORDINATE]++;
                position[Y_COORDINATE]--;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case EAST:
                position[X_COORDINATE]++;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case SOUTHEAST:
                position[X_COORDINATE]++;
                position[Y_COORDINATE]++;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case SOUTH:
                position[Y_COORDINATE]++;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case SOUTHWEST:
                position[X_COORDINATE]--;
                position[Y_COORDINATE]++;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case WEST:
                position[X_COORDINATE]--;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            case NORTHWEST:
                position[X_COORDINATE]--;
                position[Y_COORDINATE]--;
                if (isInBound(position)) {
                    return position;
                } else {
                    getSurroundingPosition();
                }
            default:
                lastSurroundingPositionCode = 0;
                return getPosition();

        }

    }

    /**
     * Checks surrounding position isn't out of board bounds
     * @param pPosition
     * @return
     */
    private boolean isInBound(Integer[] pPosition) {
        return pPosition[X_COORDINATE] < ROWS &&
                pPosition[X_COORDINATE] > -1 &&
                pPosition[Y_COORDINATE] < COLUMNS &&
                pPosition[Y_COORDINATE] > -1 ?
                true : false;
    }


}
