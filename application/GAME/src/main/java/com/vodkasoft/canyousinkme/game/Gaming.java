package com.vodkasoft.canyousinkme.game;

import com.vodkasoft.canyousinkme.gamelogic.GameManager;

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

public class Gaming extends Activity {
    DualMatrix DUALMATRIX;
    Integer[] Pair;
    GameManager gameManager;
    private TextView textViewChronometer;
    Boolean Mine = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gaming);
        drawBoard();
    }

    public void drawBoard() {
        //Se usa el DUALMATRIX anterior
        DUALMATRIX = new DualMatrix();
        final GridView gridView = (GridView) findViewById(R.id.container_grid_gaming);
        final ImageAdapter imageAdapter = new ImageAdapter(Gaming.this, DUALMATRIX.get_IMGs());
        gridView.setAdapter(imageAdapter);
        createOnClickListener(gridView);
    }

    public void createOnClickListener(final GridView pgridView) {
        pgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer[] Pair = getMatrixCoords(i);
                if(Mine){

                }else{
                    ImageView IV = (ImageView) view;
                    IV.setImageResource(R.drawable.targeted);
                }
            }
        });
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

    public void GoToOpponent(View view){
        Mine = false;
        UpdateOpponentButtons();
    }

    public void UpdateOpponentButtons(){
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

    public void GoToMine(View view){
        Mine = true;
        UpdateMineButtons();
    }

    public void UpdateMineButtons(){
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
}
