package com.vodkasoft.canyousinkme.gamelogic;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class GameManager implements IConstant {

    private static String condition = null;
    private static CPUPlayer cpuPlayer = null;
    private static boolean host;
    private static int matchType;
    private static Integer[] missileCoordinate = new Integer[2];
    private static DualMatrix opponentBoard;
    private static int opponentHitCount;
    private static DualMatrix playerBoard = null;
    private static int playerHitCount;
    private static int playerScore = INITIAL_SCORE;

    /**
     * Increments opponent's hits
     */
    public static void addOpponentHit() {
        opponentHitCount++;
    }

    /**
     * Increments player's hits
     */
    public static void addPlayerHit() {
        playerHitCount++;
    }

    /**
     * Adds points to player score
     */
    public static void addPlayerPoints() {
        playerScore += MISSILE_SUCCESSFUL_POINTS;
    }

    /**
     * Initializes a new AI
     */
    public static void createCPUplayer() {
        cpuPlayer = new CPUPlayer();
    }

    /**
     * Gets currents battle conditions (win/loose)
     * @return
     */
    public static String getCondition() {
        return condition;
    }

    /**
     * Gets current battle type, bt or local
     * @return
     */
    public static int getMatchType() {
        return matchType;
    }

    /**
     * Sets matchtype
     * @param matchType
     */
    public static void setMatchType(int matchType) {
        GameManager.matchType = matchType;
    }

    /**
     * Gets coordinate from click event listener
     * @return
     */
    public static Integer[] getMissileCoordinate() {
        return missileCoordinate;
    }

    /**
     * Sets send missile coordinate from click listener
     * @param missileCoordinate point(x,y)
     */
    public static void setMissileCoordinate(Integer[] missileCoordinate) {
        GameManager.missileCoordinate = missileCoordinate;
    }

    /**
     * Gets opponent logic board
     * @return
     */
    public static DualMatrix getOpponentBoard() {
        return opponentBoard;
    }

    /**
     * Gets player logic board
     * @return
     */
    public static DualMatrix getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Sets player board from createboard activity
     * @param playerBoard
     */
    public static void setPlayerBoard(DualMatrix playerBoard) {
        GameManager.playerBoard = playerBoard;
    }

    /**
     * Gets player current match score
     * @return
     */
    public static int getPlayerScore() {
        return playerScore;
    }

    /**
     * Checks if there are still ships available for playing
     * @return
     */
    public static boolean isEndOfGame() {
        if (playerHitCount == SHIPA_SIZE + SHIPB_SIZE + SHIPC_SIZE) {
            condition = WINNER_CONDITION;
            return true;
        }
        if (opponentHitCount == SHIPA_SIZE + SHIPB_SIZE + SHIPC_SIZE) {
            condition = LOOSER_CONDITION;
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks which player is host
     * @return
     */
    public static boolean isHost() {
        return host;
    }

    /**
     * Sets new host from host activity
     * @param host
     */
    public static void setHost(boolean host) {
        GameManager.host = host;
    }

    public static void receiveMissile() {

        switch (matchType) {
            case LOCAL_MATCH:
                receiveLocalMissile();
                break;
            case BLUETOOTH_MATCH:
                //
                break;
        }

    }

    /**
     * Resets static members necessary for new match
     */
    public static void resetMatchData() {
        opponentBoard = new DualMatrix(false);
        playerBoard = null;
        playerScore = INITIAL_SCORE;
        playerHitCount = 0;
        opponentHitCount = 0;
        condition = "";
    }

    /**
     * Sends missile to opponents board
     */
    public static void sendMissile() {

        switch (matchType) {
            case LOCAL_MATCH:
                sendLocalMissile();
                break;
            case BLUETOOTH_MATCH:
                sendBluetoothMissile();
                break;
        }

    }

    /**
     * Simulates AI missile reception
     */
    private static void receiveLocalMissile() {

        Integer[] missilePosition = cpuPlayer.nextMissilePosition();
        if (playerBoard.isShip(missilePosition[X_COORDINATE], missilePosition[Y_COORDINATE])) {
            playerBoard.putHit(missilePosition[X_COORDINATE], missilePosition[Y_COORDINATE]);
        } else {
            playerBoard.putFail(missilePosition[X_COORDINATE], missilePosition[Y_COORDINATE]);
        }

    }

    /**
     * Sends missile via BluetoothMessage
     */
    private static void sendBluetoothMissile() {
        MissileMessage missileMessage = new MissileMessage(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
        String json = JsonSerializer.fromObjectToJson(missileMessage);
        BleutoothManager.sendMessage(MISSILE_MESSAGE_KEY, json);
    }

    /**
     * Sends missile to AI board
     */
    private static void sendLocalMissile() {
        if (cpuPlayer.getBoard().isShip(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE])) {
            opponentBoard.putHit(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
            playerScore += MISSILE_SUCCESSFUL_POINTS;
            playerHitCount++;
        } else {
            opponentBoard.putFail(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
        }
    }
}
