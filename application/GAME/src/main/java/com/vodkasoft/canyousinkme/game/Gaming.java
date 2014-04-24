package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
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
import com.vodkasoft.canyousinkme.gamelogic.EnemyMissileTask;
import com.vodkasoft.canyousinkme.gamelogic.GameManager;

public class Gaming extends Activity {

    Boolean Mine = true; // Which board I'm looking
    private TextView textViewPlayerScore;

    public void GoToMine() {
        Mine = true;
        UpdateMineButtons();
        drawBoard(GameManager.getPlayerBoard());
        textViewPlayerScore.setText(String.valueOf(GameManager.getPlayerScore()));
        //waitForEnemyMissile();
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

    public void sendMissile(View view) {
        GameManager.sendMissile();
        waitForMissileResult();
        GoToMine();
    }

    public void waitForEnemyMissile() throws InterruptedException {
        EnemyMissileTask task = new EnemyMissileTask();
        task.execute();
        task.wait();
    }

    public void waitForMissileResult() {
        Thread waitThread = new Thread(new Runnable() {

            @Override
            public void run() {
                GameManager.updateMissileResult();
            }
        });
        waitThread.start();
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
                GoToMine();
            }
        });

        // Host shoots first
        if (GameManager.isHost()) GoToOpponent();
        else GoToMine();


    }
}
