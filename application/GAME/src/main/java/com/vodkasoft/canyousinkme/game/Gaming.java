package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.connectivity.BluetoothMessage;
import com.vodkasoft.canyousinkme.gamelogic.DualMatrix;
import com.vodkasoft.canyousinkme.gamelogic.GameManager;
import com.vodkasoft.canyousinkme.gamelogic.MissileMessage;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

import static com.vodkasoft.canyousinkme.utils.Constant.BLUETOOTH_MATCH;
import static com.vodkasoft.canyousinkme.utils.Constant.LOCAL_MATCH;
import static com.vodkasoft.canyousinkme.utils.Constant.WAIT_TIME;
import static com.vodkasoft.canyousinkme.utils.Constant.MISSILE_MESSAGE_KEY;
import static com.vodkasoft.canyousinkme.utils.Constant.MISSILE_STATE_MESSAGE_KEY;
import static com.vodkasoft.canyousinkme.utils.Constant.X_COORDINATE;
import static com.vodkasoft.canyousinkme.utils.Constant.Y_COORDINATE;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Gaming extends Activity{

    private TextView textViewPlayerScore;

    /**
     * Checks if there are still enough ships for playing
     */
    public void checkGameState() {
        if (GameManager.isEndOfGame()) {
            Intent intent = new Intent(this, MatchSummary.class);
            startActivity(intent);
        }
    }

    /**
     * Gets coordinate from click events over gridview
     * @param pgridView
     */
    public void createOnClickListener(final GridView pgridView) {
        pgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameManager.setMissileCoordinate(getMatrixCoords(i));
                ImageView IV = (ImageView) view;
                IV.setImageResource(R.drawable.targeted);

            }
        });
    }

    /**
     * Refreshes gridview board with logical matrixes
     * @param board
     */
    public void drawBoard(DualMatrix board) {
        //Se usa el DUALMATRIX anterior
        final GridView gridView = (GridView) findViewById(R.id.container_grid_gaming);
        final ImageAdapter imageAdapter = new ImageAdapter(Gaming.this, board.get_IMGs());
        gridView.setAdapter(imageAdapter);
        createOnClickListener(gridView);
    }

    /**
     * Gets coordinate from flat location
     * @param C
     * @return
     */
    public Integer[] getMatrixCoords(int C) {
        Integer[] Par = new Integer[2];
        int k = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 10; j++) {
                if (k == C) {
                    Par[0] = i;
                    Par[1] = j;
                    return Par;
                } else {
                    k++;
                }
            }
        }
        return Par;
    }

    /**
     * Displays shooting board
     */
    public void goToOpponent() {
        updateTimer(0);
        updateOpponentButtons();
        drawBoard(GameManager.getOpponentBoard());
    }

    /**
     * Displays player ships with current state
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */
    public void goToPlayer() throws InterruptedException, TimeoutException, ExecutionException {
        updateTimer(0);
        updateMineButtons();

        if (GameManager.getMatchType() == BLUETOOTH_MATCH) {
            waitForEnemyMissile();
        } else {
            GameManager.receiveMissile();
            waitForNextMove();
        }

        drawBoard(GameManager.getPlayerBoard());
        textViewPlayerScore.setText(String.valueOf(GameManager.getPlayerScore()));

    }

    /**
     * Sends missile to opponent board
     * @param view
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */
    public void sendMissile(View view) throws InterruptedException, TimeoutException, ExecutionException {
        GameManager.sendMissile();
        if (GameManager.getMatchType() == BLUETOOTH_MATCH)
            waitForMissileResult();
        else {
            checkGameState();
            goToPlayer();
        }
    }

    /**
     * Configures player board ui for game events
     */
    public void updateMineButtons() {
        ImageButton Shoot = (ImageButton) findViewById(R.id.shoot_btn);
        Shoot.setEnabled(false);
        Shoot.setImageResource(R.drawable.shoot_btn_disabled);

        ImageButton Opponent = (ImageButton) findViewById(R.id.opponent_btn);
        Opponent.setEnabled(false);
        Opponent.setImageResource(R.drawable.opponent_btn_disabled);

        ImageButton Mine = (ImageButton) findViewById(R.id.mine_btn);
        Mine.setEnabled(false);
        Mine.setImageResource(R.drawable.mine_btn);
    }

    /**
     * Configures shooting board ui for game events
     */
    public void updateOpponentButtons() {
        ImageButton Shoot = (ImageButton) findViewById(R.id.shoot_btn);
        Shoot.setEnabled(true);
        Shoot.setImageResource(R.drawable.shoot_btn);

        ImageButton Opponent = (ImageButton) findViewById(R.id.opponent_btn);
        Opponent.setEnabled(false);
        Opponent.setImageResource(R.drawable.opponent_btn);

        ImageButton Mine = (ImageButton) findViewById(R.id.mine_btn);
        Mine.setEnabled(false);
        Mine.setImageResource(R.drawable.mine_btn_disabled);
    }

    /**
     * Update timer textview for each move
     * @param secondsLeft
     */
    public void updateTimer(int secondsLeft) {
        TextView textViewTimer = (TextView) findViewById(R.id.textViewChronometer);
        textViewTimer.setText(String.valueOf(secondsLeft));
    }

    /**
     * Starts missile message listening task
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */
    public void waitForEnemyMissile() throws InterruptedException, TimeoutException, ExecutionException {
        checkGameState();
        EnemyMissileTask task = new EnemyMissileTask();
        task.execute();
    }

    /**
     * Starts missile result listening task
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */
    public void waitForMissileResult() throws InterruptedException, TimeoutException, ExecutionException {
        checkGameState();
        MissileResultTask task = new MissileResultTask();
        task.execute();
    }

    /**
     * Timer for AI battles
     */
    public void waitForNextMove() {
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                updateTimer((int) l / 1000);
            }

            @Override
            public void onFinish() {
                goToOpponent();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gaming);

        drawBoard(GameManager.getPlayerBoard());

        // Init UI variables
        textViewPlayerScore = (TextView) findViewById(R.id.textViewPlayerScore);

        ImageButton buttonOpponent = (ImageButton) findViewById(R.id.opponent_btn);
        ImageButton buttonPlayer = (ImageButton) findViewById(R.id.mine_btn);

        buttonOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOpponent();
            }
        });

        buttonPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goToPlayer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (GameManager.getMatchType() == LOCAL_MATCH) {
            GameManager.createCPUplayer();
            goToOpponent();
        } else if (GameManager.isHost() && GameManager.getMatchType() == BLUETOOTH_MATCH) {
            goToOpponent();
        } else {
            try {
                goToPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private class EnemyMissileTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            int timeLeft = WAIT_TIME;
            BluetoothMessage btMessage;

            while (timeLeft > 0) {
                publishProgress(timeLeft);

                if (!BleutoothManager.messageQueueIsEmpty()) {
                    btMessage = BleutoothManager.dequeueMessage();
                    if (btMessage.getKey() == MISSILE_MESSAGE_KEY) {
                        MissileMessage mMessage = (MissileMessage) JsonSerializer.fromJsonToObject(btMessage.getData(), MissileMessage.class);

                        if(!GameManager.opponentIdIsSet()){
                            GameManager.setOpponentId(mMessage.getPlayerId());
                        }

                        if (GameManager.getPlayerBoard().isShip(mMessage.getxCoordinate(), mMessage.getyCoordinate())) {
                            BleutoothManager.sendMessage(MISSILE_STATE_MESSAGE_KEY, "true");
                            GameManager.getPlayerBoard().putHit(mMessage.getxCoordinate(), mMessage.getyCoordinate());
                            GameManager.addOpponentPoints();
                            GameManager.addOpponentHit();
                        } else {
                            BleutoothManager.sendMessage(MISSILE_STATE_MESSAGE_KEY, "false");
                            GameManager.getPlayerBoard().putFail(mMessage.getxCoordinate(), mMessage.getyCoordinate());
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

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            updateTimer(progress[0]);
        }

        @Override
        protected void onPostExecute(Void params) {
            goToOpponent();
        }

    }

    private class MissileResultTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            int timeLeft = WAIT_TIME;
            BluetoothMessage btMessage;

            while (timeLeft > 0) {
                publishProgress(timeLeft);

                if (!BleutoothManager.messageQueueIsEmpty()) {
                    btMessage = BleutoothManager.dequeueMessage();
                    if (btMessage.getKey() == MISSILE_STATE_MESSAGE_KEY) {
                        if (Boolean.valueOf(btMessage.getData())) {
                            GameManager.addPlayerPoints();
                            GameManager.addPlayerHit();
                            GameManager.getOpponentBoard().putHit(GameManager.getMissileCoordinate()[X_COORDINATE], GameManager.getMissileCoordinate()[Y_COORDINATE]);
                        } else {
                            GameManager.getOpponentBoard().putFail(GameManager.getMissileCoordinate()[X_COORDINATE], GameManager.getMissileCoordinate()[Y_COORDINATE]);
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

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            updateTimer(progress[0]);
        }

        @Override
        protected void onPostExecute(Void params) {
            try {
                goToPlayer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}


