package com.vodkasoft.canyousinkme.gamelogic;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class GameManager {

    // Constants
    private final int BOARD_COLUMNS = 10;
    private final int BOARD_ROWS = 15;
    private final int COUNTDOWN_MILLIS = 1000;
    private final int INITIAL_SCORE = 0;
    private final int MILLIS_TO_SECONDS = 1000;
    private final int MOVE_MILLIS = 30000;
    private final int MISSILE_MESSAGE_KEY = 1;
    private final int MISSIL_STATE_MESSAGE_KEY = 2;

    // Members
    private BleutoothManager mBleutoothManager;
    private CountDownTimer missileLaunchTimer;
    private Player opponent;
    private DualMatrix opponentBoard;
    private int opponentScore;
    private Player player;
    private DualMatrix playerBoard;
    private int playerScore;

    // Timer TextView
    private TextView textViewMissileLauncher;

    public GameManager(Activity pActivity,
                       TextView textViewMissileLaunchTimer) {

        // Player initialization
        player = new Player();
        opponent = new Player();

        // Score initialization
        playerScore = INITIAL_SCORE;
        opponentScore = INITIAL_SCORE;

        // Board initialization;
        playerBoard = new DualMatrix(BOARD_ROWS, BOARD_COLUMNS);
        opponentBoard = new DualMatrix(BOARD_ROWS, BOARD_COLUMNS);

        setUpMissileLaunchTimer();

    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void initOpponentBoard() {
    }

    public void sendMissile(int pX, int pY) {
        MissileMessage missileMessage = new MissileMessage(pX, pY);
        String json = JsonSerializer.fromObjectToJson(missileMessage);
        mBleutoothManager.sendMessage(MISSILE_MESSAGE_KEY, json);
    }

    public void setUpMissileLaunchTimer() {
        missileLaunchTimer = new CountDownTimer(MOVE_MILLIS, COUNTDOWN_MILLIS) {
            @Override
            public void onTick(long millinsUntilFinished) {
                textViewMissileLauncher.setText(String.valueOf(millinsUntilFinished / MILLIS_TO_SECONDS));
            }

            @Override
            public void onFinish() {
                textViewMissileLauncher.setText(":(");
            }
        }.start();
    }

    public void updateOpponentBoard() {
    }

    public void updatePlayerBoard() {
    }
}
