package com.vodkasoft.canyousinkme.gamelogic;

import android.widget.TextView;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.connectivity.BluetoothMessage;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class GameManager {

    // Constants
    private static final int INITIAL_SCORE = 0;
    private static int opponentScore = INITIAL_SCORE;
    private static int playerScore = INITIAL_SCORE;
    private static final int MISSILE_MESSAGE_KEY = 1;
    private static final int MISSILE_STATE_MESSAGE_KEY = 2;
    private static final int MISSILE_SUCCESSFUL_POINTS = 50;
    private static final int X_COORDINATE = 0;
    private static final int Y_COORDINATE = 1;
    private static final int WAIT_TIME = 30;

    public static boolean isReceivedMissile() {
        return receivedMissile;
    }

    public static void setReceivedMissile(boolean receivedMissile) {
        GameManager.receivedMissile = receivedMissile;
    }

    private static boolean receivedMissile = false;
    private static boolean receivedShotResult = false;

    public static boolean isReceivedShotResult() {
        return receivedShotResult;
    }

    public static void setReceivedShotResult(boolean receivedShotResult) {
        GameManager.receivedShotResult = receivedShotResult;
    }

    private static boolean host;
    private static Player opponent = null;
    private static Player player = null;
    private static DualMatrix playerBoard = null;
    private static DualMatrix opponentBoard = new DualMatrix();

    private static TextView textViewMissileLauncher;
    private static Integer[] missileCoordinate = new Integer[2];


    public static Player getOpponent() {
        return opponent;
    }

    public static void setOpponent(Player pOpponent) {
        opponent = pOpponent;
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

    public static void receiveMissile() {
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

    public static DualMatrix getOpponentBoard() {
        return opponentBoard;
    }

    public static void sendMissile() {
        MissileMessage missileMessage = new MissileMessage(missileCoordinate[X_COORDINATE], missileCoordinate[Y_COORDINATE]);
        String json = JsonSerializer.fromObjectToJson(missileMessage);
        BleutoothManager.sendMessage(MISSILE_MESSAGE_KEY, json);
    }

    public static void setMissileCoordinate(Integer[] missileCoordinate) {
        GameManager.missileCoordinate = missileCoordinate;
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
}
