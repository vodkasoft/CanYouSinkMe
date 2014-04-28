package com.vodkasoft.canyousinkme.gamelogic;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/27/14.
 */
public interface IConstant {

    public static final int NORTH = 0;
    public static final int NORTHEAST = 1;
    public static final int EAST = 2;
    public static final int SOUTHEAST = 3;
    public static final int SOUTH = 4;
    public static final int SOUTHWEST = 5;
    public static final int WEST = 6;
    public static final int NORTHWEST = 7;

    public static final int ROWS = 15;
    public static final int COLUMNS = 10;

    public static final int X_COORDINATE = 0;
    public static final int Y_COORDINATE = 1;

    public static final int SHIPA_SIZE = 4;
    public static final int SHIPB_SIZE = 4;
    public static final int SHIPC_SIZE = 3;

    public static  final int EMPTY = 0;
    public static  final int FAIL = 5;
    public static  final int HIT = 4;

    public static final int INITIAL_SCORE = 0;
    public static final int MISSILE_SUCCESSFUL_POINTS = 50;

    public static final int MISSILE_MESSAGE_KEY = 1;
    public static final int MISSILE_STATE_MESSAGE_KEY = 2;

    public static final int WAIT_TIME = 30;

    public static final int BLUETOOTH_MATCH = 0;
    public static final int LOCAL_MATCH = 1;

}
