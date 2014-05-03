package com.vodkasoft.canyousinkme.utils;

/**
 * Vodkasoft (R)
 * Created by jomarin on 5/3/14.
 */
public final class Constant {

    private Constant(){
        // restrict instantiation
    }

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

    public static final String WINNER_CONDITION = "WINNER";
    public static final String LOOSER_CONDITION = "LOOSER";

    public static final String BACKEND_HOST = "https://can-you-sink-me.appspot.com";

    public static final String USER_ERROR = "Create user request couldn't be completed :'(";
    public static final String LEADERBOARD_ERROR = "Get leaderboard request couldn't be completed :'(";

}
