package com.vodkasoft.canyousinkme.gamelogic;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.connectivity.BluetoothMessage;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class GameManager implements IConstant{

    private static CPUPlayer cpuPlayer = null;

    public static int getMatchType() {
        return matchType;
    }

    public static void setMatchType(int matchType) {
        GameManager.matchType = matchType;
    }

    private static int matchType;
    private static boolean host;
    private static Integer[] missileCoordinate = new Integer[2];
    private static Player opponent = null;
    private static DualMatrix opponentBoard;
    private static int opponentScore = INITIAL_SCORE;
    private static Player player = null;
    private static DualMatrix playerBoard = null;
    private static int playerScore = INITIAL_SCORE;
    private static boolean receivedMissile = false;
    private static boolean receivedShotResult = false;

    public static CPUPlayer getCpuPlayer() {
        return cpuPlayer;
    }

    public static void createCPUplayer(){
        cpuPlayer = new CPUPlayer();
    }

    public static void resetMatchData(){
        opponent = null;
        opponentBoard = new DualMatrix(false);
        opponentScore = INITIAL_SCORE;
        playerBoard = null;
        playerScore = INITIAL_SCORE;
    }

    public static void setCpuPlayer(CPUPlayer cpuPlayer) {
        GameManager.cpuPlayer = cpuPlayer;
    }

    public static Integer[] getMissileCoordinate() {
        return missileCoordinate;
    }

    public static void setMissileCoordinate(Integer[] missileCoordinate) {
        GameManager.missileCoordinate = missileCoordinate;
    }

    public static Player getOpponent() {
        return opponent;
    }

    public static void setOpponent(Player pOpponent) {
        opponent = pOpponent;
    }

    public static DualMatrix getOpponentBoard() {
        return opponentBoard;
    }

    public static int getOpponentScore() {
        return opponentScore;
    }

    public static void setOpponentScore(int pOpponentScore) {
        opponentScore = pOpponentScore;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player pPlayer) {
        player = pPlayer;
    }

    public static DualMatrix getPlayerBoard() {
        return playerBoard;
    }

    public static void setPlayerBoard(DualMatrix playerBoard) {
        GameManager.playerBoard = playerBoard;
    }

    public static int getPlayerScore() {
        return playerScore;
    }

    public static void setPlayerScore(int pPlayerScore) {
        playerScore = pPlayerScore;
    }

    public static boolean isHost() {
        return host;
    }

    public static void setHost(boolean host) {
        GameManager.host = host;
    }

    public static boolean isReceivedMissile() {
        return receivedMissile;
    }

    public static void setReceivedMissile(boolean receivedMissile) {
        GameManager.receivedMissile = receivedMissile;
    }

    public static boolean isReceivedShotResult() {
        return receivedShotResult;
    }

    public static void setReceivedShotResult(boolean receivedShotResult) {
        GameManager.receivedShotResult = receivedShotResult;
    }

    public static void receiveMissile() {

        switch (matchType) {
            case LOCAL_MATCH:
                receiveLocalMissile();
                break;
            case BLUETOOTH_MATCH:
                receiveBluetoothMissile();
                break;
        }

    }

    public static void sendMissile(){

        switch (matchType) {
            case LOCAL_MATCH:
                sendLocalMissile();
                break;
            case BLUETOOTH_MATCH:
                sendBluetoothMissile();
                break;
        }

    }

    private static void sendLocalMissile() {
        if (cpuPlayer.getBoard().isShip(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE])) {
            opponentBoard.putHit(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
            playerScore += MISSILE_SUCCESSFUL_POINTS;
        } else {
            opponentBoard.putFail(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
        }
    }

    private static void sendBluetoothMissile() {
        MissileMessage missileMessage = new MissileMessage(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
        String json = JsonSerializer.fromObjectToJson(missileMessage);
        BleutoothManager.sendMessage(MISSILE_MESSAGE_KEY, json);
    }

    public static void updateMissileResult() {
        int timeLeft = WAIT_TIME;
        BluetoothMessage btMessage;

        while (timeLeft > 0) {
            if (!BleutoothManager.messageQueueIsEmpty()) {
                btMessage = BleutoothManager.dequeueMessage();
                if (btMessage.getKey() == MISSILE_STATE_MESSAGE_KEY) {
                    if (Boolean.valueOf(btMessage.getData())) {
                        playerScore += MISSILE_SUCCESSFUL_POINTS;
                        opponentBoard.putHit(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
                    } else {
                        opponentBoard.putFail(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
                    }
                    break;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeLeft--;

        }
    }


    public void updateOpponentBoard() {
    }

    public void updatePlayerBoard() {
    }

    private static void receiveBluetoothMissile() {
        int timeLeft = WAIT_TIME;
        BluetoothMessage btMessage;

        while (timeLeft > 0) {
            if (!BleutoothManager.messageQueueIsEmpty()) {
                btMessage = BleutoothManager.dequeueMessage();
                if (btMessage.getKey() == MISSILE_MESSAGE_KEY) {
                    MissileMessage mMessage = (MissileMessage) JsonSerializer.fromJsonToObject(btMessage.getData(), MissileMessage.class);
                    if (playerBoard.isShip(mMessage.getxCoordinate(), mMessage.getyCoordinate())) {
                        BleutoothManager.sendMessage(MISSILE_STATE_MESSAGE_KEY, "true");
                        playerBoard.putHit(mMessage.getxCoordinate(), mMessage.getyCoordinate());
                    } else {
                        BleutoothManager.sendMessage(MISSILE_STATE_MESSAGE_KEY, "false");
                        playerBoard.putFail(mMessage.getxCoordinate(), mMessage.getyCoordinate());
                    }
                    break;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeLeft--;
        }
    }

    private static void receiveLocalMissile() {

        Integer[] missilePosition = cpuPlayer.nextMissilePosition();
        if (playerBoard.isShip(missilePosition[X_COORDINATE], missilePosition[Y_COORDINATE])) {
            playerBoard.putHit(missilePosition[X_COORDINATE], missilePosition[Y_COORDINATE]);
        } else {
            playerBoard.putFail(missilePosition[X_COORDINATE], missilePosition[Y_COORDINATE]);
        }

    }
}
