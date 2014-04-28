package com.vodkasoft.canyousinkme.gamelogic;

import com.vodkasoft.canyousinkme.game.R;

import java.util.Random;

public class DualMatrix implements IConstant{

    private final Random randomGenerator = new Random();
    private Integer[] _IMGs = new Integer[ROWS * COLUMNS];
    private Integer[][] _LOGIC = new Integer[ROWS][COLUMNS];

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

    public Integer[] getPosition() {
        int x = randomGenerator.nextInt(ROWS);
        int y = randomGenerator.nextInt(COLUMNS);
        return new Integer[]{x, y};
    }

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

    public Integer[] get_IMGs() {
        return _IMGs;
    }

    public void set_IMGs(Integer[] _IMGs) {
        this._IMGs = _IMGs;
    }

    public Integer[][] get_LOGIC() {
        return _LOGIC;
    }

    public void set_LOGIC(Integer[][] _LOGIC) {
        this._LOGIC = _LOGIC;
    }

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

    public boolean isShip(int pX, int pY) {
        return _LOGIC[pX][pY] != EMPTY;
    }

    public void putFail(int pX, int pY) {
        _LOGIC[pX][pY] = FAIL;
        _IMGs[getFlatLocation(new Integer[]{pX, pY})] = R.drawable.fail;
    }

    public void putHit(int pX, int pY) {
        _LOGIC[pX][pY] = HIT;
        _IMGs[getFlatLocation(new Integer[]{pX, pY})] = R.drawable.hit;
    }

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
