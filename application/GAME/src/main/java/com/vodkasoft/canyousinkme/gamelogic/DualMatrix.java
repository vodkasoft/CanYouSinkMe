package com.vodkasoft.canyousinkme.gamelogic;

import com.vodkasoft.canyousinkme.game.R;

import java.util.Random;

public class DualMatrix implements IConstant {

    private final Random randomGenerator = new Random();
    private Integer[] _IMGs = new Integer[ROWS * COLUMNS];
    private Integer[][] _LOGIC = new Integer[ROWS][COLUMNS];

    /**
     * Creates a new matrix instance, if it is AI, it automatically puts ships on board
     * @param isArtificialIntelligence
     */
    public DualMatrix(boolean isArtificialIntelligence) {
        int c = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                _LOGIC[i][j] = 0;
                _IMGs[c++] = R.drawable.game_v_rect;
            }
        }

        if (isArtificialIntelligence) {
            putShips();
        }
    }

    /**
     * Gets specific point location in flat IMG matrix
     * @param pPair
     * @return
     */
    public int getFlatLocation(Integer[] pPair) {
        int c = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (i == pPair[X_COORDINATE] && j == pPair[Y_COORDINATE]) {
                    return c;
                } else {
                    c++;
                }
            }
        }
        return 150;
    }

    /**
     * Randomly generates a new position for ships to be put on
     * @return Point(x,y)
     */
    public Integer[] getPosition() {
        int x = randomGenerator.nextInt(ROWS);
        int y = randomGenerator.nextInt(COLUMNS);
        return new Integer[]{x, y};
    }

    /**
     * Gets ship size from shipcode
     * @param pShipCode
     * @return ships integer size
     */
    public int getShipSize(int pShipCode) {
        switch (pShipCode) {
            case 1:
                return SHIPA_SIZE;
            case 2:
                return SHIPB_SIZE;
            case 3:
                return SHIPC_SIZE;
            default:
                return 0;
        }
    }

    /**
     * Gets IMGs matrix
     * @return
     */
    public Integer[] get_IMGs() {
        return _IMGs;
    }

    /**
     * Checks if the position of the ship to be put interfers with another one or is out of bounds.
     * @param pPair
     * @param isVertical
     * @param pLenght
     * @return
     */
    public boolean isInterfering(Integer[] pPair, boolean isVertical, int pLenght) {
        int row = pPair[0];
        int column = pPair[1];

        try {
            if (isVertical) {
                while (pLenght > 0) {
                    if (_LOGIC[row][column] != 0) {
                        return true;
                    }
                    pLenght--;
                    column++;
                }
            } else {
                while (pLenght > 0) {
                    if (_LOGIC[row][column] != 0) {
                        return true;
                    }
                    pLenght--;
                    row++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            return true;
        }
        return false;
    }

    /**
     * Checks if given position has a ship
     * @param pX x coordinate
     * @param pY y coordinate
     * @return
     */
    public boolean isShip(int pX, int pY) {
        return _LOGIC[pX][pY] != EMPTY;
    }

    /**
     * Puts a fail image on board
     * @param pX x coordinate
     * @param pY y coordinate
     */
    public void putFail(int pX, int pY) {
        _LOGIC[pX][pY] = FAIL;
        _IMGs[getFlatLocation(new Integer[]{pX, pY})] = R.drawable.fail;
    }

    /**
     * Puts a hit image on board
     * @param pX x coordinate
     * @param pY y coordinate
     */
    public void putHit(int pX, int pY) {
        _LOGIC[pX][pY] = HIT;
        _IMGs[getFlatLocation(new Integer[]{pX, pY})] = R.drawable.hit;
    }

    /**
     * Puts a ship on board
     * @param pShipCode ship id
     * @param pPair ship position
     * @param isVertical ship orientation
     */
    public void putShip(int pShipCode, Integer[] pPair, boolean isVertical) {
        int X = pPair[0];
        int Y = pPair[1];
        switch (pShipCode) {
            case 1:
                putShipA(X, Y, isVertical, pPair);
                break;
            case 2:
                putShipB(X, Y, isVertical, pPair);
                break;
            case 3:
                putShipC(X, Y, isVertical, pPair);
                break;
        }
    }

    /**
     * Fills CPU board with ships
     */
    public void putShips() {
        Integer[] position;
        boolean isVertical;
        int currentShipCode = 1;

        while (currentShipCode < 4) {
            position = getPosition();
            isVertical = randomGenerator.nextBoolean();

            if (!isInterfering(position, isVertical, getShipSize(currentShipCode))) {
                putShip(currentShipCode, position, isVertical);
                currentShipCode++;
            }
        }
    }

    /**
     * Puts ship A depending on position and orientation
     * @param pX x coordinate
     * @param pY y coordinate
     * @param isVertical ship orientation
     * @param pPair position
     */
    private void putShipA(int pX, int pY, boolean isVertical, Integer[] pPair) {
        if (isVertical) {
            _LOGIC[pX][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_1;
            pPair[1] += 1;
            _LOGIC[pX][pY + 1] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_2;
            pPair[1] += 1;
            _LOGIC[pX][pY + 2] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_3;
            pPair[1] += 1;
            _LOGIC[pX][pY + 3] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_4;
        } else {
            _LOGIC[pX][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_1;
            pPair[0] += 1;
            _LOGIC[pX + 1][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_2;
            pPair[0] += 1;
            _LOGIC[pX + 2][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_3;
            pPair[0] += 1;
            _LOGIC[pX + 3][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_4;
        }
    }

    /**
     * Puts ship B depending on position and orientation
     * @param pX x coordinate
     * @param pY y coordinate
     * @param isVertical ship orientation
     * @param pPair position
     */
    private void putShipB(int pX, int pY, boolean isVertical, Integer[] pPair) {
        if (isVertical) {
            _LOGIC[pX][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_1;
            pPair[1] += 1;
            _LOGIC[pX][pY + 1] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_2;
            pPair[1] += 1;
            _LOGIC[pX][pY + 2] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_3;
            pPair[1] += 1;
            _LOGIC[pX][pY + 3] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_4;
        } else {
            _LOGIC[pX][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_v_1;
            pPair[0] += 1;
            _LOGIC[pX + 1][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_v_2;
            pPair[0] += 1;
            _LOGIC[pX + 2][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_v_3;
            pPair[0] += 1;
            _LOGIC[pX + 3][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_4;
        }
    }

    /**
     * Puts ship C depending on position and orientation
     * @param pX x coordinate
     * @param pY y coordinate
     * @param isVertical ship orientation
     * @param pPair position
     */
    private void putShipC(int pX, int pY, boolean isVertical, Integer[] pPair) {
        if (isVertical) {
            _LOGIC[pX][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_h_1;
            pPair[1] += 1;
            _LOGIC[pX][pY + 1] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_h_2;
            pPair[1] += 1;
            _LOGIC[pX][pY + 2] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_h_3;
        } else {
            _LOGIC[pX][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_v_1;
            pPair[0] += 1;
            _LOGIC[pX + 1][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_v_2;
            pPair[0] += 1;
            _LOGIC[pX + 2][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_v_3;
        }
    }
}
