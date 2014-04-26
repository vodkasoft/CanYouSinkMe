package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vodkasoft.canyousinkme.gamelogic.DualMatrix;
import com.vodkasoft.canyousinkme.gamelogic.GameManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Gaming extends Activity {

    Boolean Mine = true; // Which board I'm looking
    private TextView textViewPlayerScore;

    public void GoToMine() throws InterruptedException, TimeoutException, ExecutionException {
        Mine = true;
        UpdateMineButtons();
        drawBoard(GameManager.getPlayerBoard());
        textViewPlayerScore.setText(String.valueOf(GameManager.getPlayerScore()));
        waitForEnemyMissile();
    }

    public void GoToOpponent() {
        Mine = false;
        UpdateOpponentButtons();
        drawBoard(GameManager.getOpponentBoard());
    }

    public void UpdateMineButtons() {
        ImageButton Shoot = (ImageButton) findViewById(R.id.shoot_btn);
        Shoot.setEnabled(false);
        Shoot.setImageResource(R.drawable.shoot_btn_disabled);

        ImageButton Opponent = (ImageButton) findViewById(R.id.opponent_btn);
        Opponent.setEnabled(true);
        Opponent.setImageResource(R.drawable.opponent_btn);

        ImageButton Mine = (ImageButton) findViewById(R.id.mine_btn);
        Mine.setEnabled(false);
        Mine.setImageResource(R.drawable.mine_btn_disabled);
    }

    public void UpdateOpponentButtons() {
        ImageButton Shoot = (ImageButton) findViewById(R.id.shoot_btn);
        Shoot.setEnabled(true);
        Shoot.setImageResource(R.drawable.shoot_btn);

        ImageButton Opponent = (ImageButton) findViewById(R.id.opponent_btn);
        Opponent.setEnabled(false);
        Opponent.setImageResource(R.drawable.opponent_btn_disabled);

        ImageButton Mine = (ImageButton) findViewById(R.id.mine_btn);
        Mine.setEnabled(true);
        Mine.setImageResource(R.drawable.mine_btn);
    }

    public void createOnClickListener(final GridView pgridView) {
        pgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameManager.setMissileCoordinate(getMatrixCoords(i));
                if (Mine) {

                } else {
                    ImageView IV = (ImageView) view;
                    IV.setImageResource(R.drawable.targeted);
                }
            }
        });
    }

    public void drawBoard(DualMatrix board) {
        //Se usa el DUALMATRIX anterior
        final GridView gridView = (GridView) findViewById(R.id.container_grid_gaming);
        final ImageAdapter imageAdapter = new ImageAdapter(Gaming.this, board.get_IMGs());
        gridView.setAdapter(imageAdapter);
        createOnClickListener(gridView);
    }

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

    public void sendMissile(View view) throws InterruptedException, TimeoutException, ExecutionException {
        GameManager.sendMissile();
        waitForMissileResult();
    }

    public void waitForEnemyMissile() throws InterruptedException, TimeoutException, ExecutionException {
        EnemyMissileTask task = new EnemyMissileTask();
        task.execute();
    }

    public void waitForMissileResult() throws InterruptedException, TimeoutException, ExecutionException {
        MissileResultTask task = new MissileResultTask();
        task.execute();
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
                GoToOpponent();
            }
        });

        buttonPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GoToMine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Host shoots first
        if (GameManager.isHost()) GoToOpponent();
        else try {
            GoToMine();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private class EnemyMissileTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute(){}

        @Override
        protected Void doInBackground(Void... params){

            GameManager.receiveMissile();

            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            GoToOpponent();
        }

    }

    private class MissileResultTask extends AsyncTask<Void, Void, Void>{

        @Override
        public void onPreExecute(){}

        @Override
        protected Void doInBackground(Void... params){

            GameManager.updateMissileResult();

            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            try {
                GoToMine();
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


