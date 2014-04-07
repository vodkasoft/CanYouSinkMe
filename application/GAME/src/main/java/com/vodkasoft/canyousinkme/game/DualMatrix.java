package com.vodkasoft.canyousinkme.game;

public class DualMatrix {

    private Integer[][] _LOGIC = new Integer[15][10];

    private Integer[] _IMGs = new Integer[150];

    public DualMatrix() {
        int c = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 10; j++) {
                _LOGIC[i][j] = 0;
                _IMGs[c++] = R.drawable.game_v_rect;
            }
        }
    }

    public int getFlatLocation(Integer[] pPair) {
        int c = 0;
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 10; j++){
                if(i == pPair[0] && j == pPair[1]){
                    return c;
                }else{
                    c++;
                }
            }
        }
        return 150;
    }

    public boolean isInterfering(Integer[] pPair, boolean pOrientation, int pLenght) {
        int X = pPair[0];
        int Y = pPair[1];
        if (pOrientation) {
            while (pLenght > 0) {
                if (_LOGIC[X][Y] != 0) {
                    return false;
                }
                pLenght--;
                Y++;
            }
        } else {
            while (pLenght > 0) {
                if (_LOGIC[X][Y] != 0) {
                    return false;
                }
                pLenght--;
                X++;
            }
        }
        return true;
    }

    public void putShip(int pShipCode, Integer[] pPair, boolean pOrientation) {
        int X = pPair[0];
        int Y = pPair[1];
        switch (pShipCode) {
            case 1:
                putShipA(X, Y, pOrientation, pPair);
                break;
            case 2:
                putShipB(X, Y, pOrientation, pPair);
                break;
            case 3:
                putShipC(X, Y, pOrientation, pPair);
                break;
        }
    }

    private void putShipA(int pX, int pY, boolean pOrientation, Integer[] pPair) {
        if (pOrientation) {
            _LOGIC[pX][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_1;
            pPair[1]+=1;
            _LOGIC[pX][pY + 1] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_2;
            pPair[1]+=1;
            _LOGIC[pX][pY + 2] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_3;
            pPair[1]+=1;
            _LOGIC[pX][pY + 3] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_h_4;
        }else{
            _LOGIC[pX][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_1;
            pPair[0]+=1;
            _LOGIC[pX+1][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_2;
            pPair[0]+=1;
            _LOGIC[pX+2][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_3;
            pPair[0]+=1;
            _LOGIC[pX+3][pY] = 1;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_4;
        }
    }

    private void putShipB(int pX, int pY, boolean pOrientation, Integer[] pPair){
        if (pOrientation) {
            _LOGIC[pX][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_1;
            pPair[1]+=1;
            _LOGIC[pX][pY + 1] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_2;
            pPair[1]+=1;
            _LOGIC[pX][pY + 2] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_3;
            pPair[1]+=1;
            _LOGIC[pX][pY + 3] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_h_4;
        }else{
            _LOGIC[pX][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_v_1;
            pPair[0]+=1;
            _LOGIC[pX+1][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_v_2;
            pPair[0]+=1;
            _LOGIC[pX+2][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipb_v_3;
            pPair[0]+=1;
            _LOGIC[pX+3][pY] = 2;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipa_v_4;
        }
    }

    private void putShipC(int pX, int pY, boolean pOrientation, Integer[] pPair){
        if (pOrientation) {
            _LOGIC[pX][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_h_1;
            pPair[1]+=1;
            _LOGIC[pX][pY + 1] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_h_2;
            pPair[1]+=1;
            _LOGIC[pX][pY + 2] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_h_3;
        }else{
            _LOGIC[pX][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_v_1;
            pPair[0]+=1;
            _LOGIC[pX+1][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_v_2;
            pPair[0]+=1;
            _LOGIC[pX+2][pY] = 3;
            _IMGs[getFlatLocation(pPair)] = R.drawable.shipc_v_3;
        }
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

    public Integer[] get_IMGs() {
        return _IMGs;
    }
}
