package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.vodkasoft.canyousinkme.gamelogic.DualMatrix;
import com.vodkasoft.canyousinkme.gamelogic.GameManager;

public class CreateBoard extends Activity {

    final int SHIPA_SIZE = 4;

    final int SHIPB_SIZE = 4;

    final int SHIPC_SIZE = 3;

    DualMatrix DUALMATRIX;

    boolean SHIPA_LOCATED = false;

    boolean SHIPB_LOCATED = false;

    boolean SHIPC_LOCATED = false;

    int Ship = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_board);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "JustAnotherHand.ttf");
        TextView textView = (TextView) findViewById(R.id.createyourboard_txt);
        textView.setTypeface(typeface);
        textView.setTextSize(20);
        GameManager.resetMatchData();
        DUALMATRIX = new DualMatrix(false);
        drawBoard();
    }

    public void GoToWaiting(View view) {
        if (SHIPA_LOCATED && SHIPB_LOCATED && SHIPC_LOCATED) {
            GameManager.setPlayerBoard(DUALMATRIX);
            CharSequence CS = "All ships placed";
            showToastWithMessage(CS);
            Intent intent = new Intent(this, Gaming.class);
            startActivity(intent);
        }else{
            CharSequence CS = "There are ships left to be placed";
            showToastWithMessage(CS);
        }
    }

    public void showToastWithMessage(CharSequence ToastText){
        Toast toast = Toast.makeText(CreateBoard.this, ToastText,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void drawBoard() {
        final GridView gridView = (GridView) findViewById(R.id.container_grid);
        final ImageAdapter imageAdapter = new ImageAdapter(CreateBoard.this, DUALMATRIX.get_IMGs());
        gridView.setAdapter(imageAdapter);
        createOnClickListener(gridView);
    }

    public void createOnClickListener(final GridView pgridView) {
        pgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer[] Pair = getMatrixCoords(i);
                CheckBox CB;
                switch (Ship) {
                    case 1:
                        CB = (CheckBox) findViewById(R.id.shipA_check);
                        if (!isACorrectPosition(Pair, CB.isChecked())) {
                            CharSequence CS = "Invalid position";
                            showToastWithMessage(CS);
                        } else {
                            if (DUALMATRIX.isInterfering(Pair, CB.isChecked(), SHIPA_SIZE)) {
                                CharSequence CS = "There is another ship in those spaces";
                                showToastWithMessage(CS);
                            } else {
                                DUALMATRIX.putShip(Ship, Pair, CB.isChecked());
                                updateAdapterAndLogic(pgridView);
                                SHIPA_LOCATED = true;
                            }
                        }
                        break;
                    case 2:
                        CB = (CheckBox) findViewById(R.id.shipB_check);
                        if (!isACorrectPosition(Pair, CB.isChecked())) {
                            CharSequence CS = "Invalid position";
                            showToastWithMessage(CS);
                        } else {
                            if (DUALMATRIX.isInterfering(Pair, CB.isChecked(), SHIPB_SIZE)) {
                                CharSequence CS = "There is another ship in those spaces";
                                showToastWithMessage(CS);
                            } else {
                                DUALMATRIX.putShip(Ship, Pair, CB.isChecked());
                                updateAdapterAndLogic(pgridView);
                                SHIPB_LOCATED = true;
                            }
                        }
                        break;
                    case 3:
                        CB = (CheckBox) findViewById(R.id.shipC_check);
                        if (!isACorrectPosition(Pair, CB.isChecked())) {
                            CharSequence CS = "Invalid position";
                            showToastWithMessage(CS);
                        } else {
                            if (DUALMATRIX.isInterfering(Pair, CB.isChecked(), SHIPC_SIZE)) {
                                CharSequence CS = "There is another ship in those spaces";
                                showToastWithMessage(CS);
                            } else {
                                DUALMATRIX.putShip(Ship, Pair, CB.isChecked());
                                updateAdapterAndLogic(pgridView);
                                SHIPC_LOCATED = true;
                            }
                        }
                        break;
                }
                Ship = 0;
            }
        });
    }

    public void updateAdapterAndLogic(GridView gridView) {
        ImageAdapter IA = new ImageAdapter(CreateBoard.this,
                DUALMATRIX.get_IMGs());
        gridView.setAdapter(IA);
        ImageButton imageButton;
        TextView textView;
        CheckBox checkBox;
        switch (Ship) {
            case 1:
                imageButton = (ImageButton) findViewById(
                        R.id.shipA_btn);
                textView = (TextView) findViewById(R.id.txtshipA);
                checkBox = (CheckBox) findViewById(R.id.shipA_check);
                checkBox.setEnabled(false);
                textView.setEnabled(false);
                imageButton.setEnabled(false);
                break;
            case 2:
                imageButton = (ImageButton) findViewById(
                        R.id.shipB_btn);
                textView = (TextView) findViewById(R.id.txtshipB);
                checkBox = (CheckBox) findViewById(R.id.shipB_check);
                checkBox.setEnabled(false);
                textView.setEnabled(false);
                imageButton.setEnabled(false);
                break;
            case 3:
                imageButton = (ImageButton) findViewById(
                        R.id.shipC_btn);
                textView = (TextView) findViewById(R.id.txtshipC);
                checkBox = (CheckBox) findViewById(R.id.shipC_check);
                checkBox.setEnabled(false);
                textView.setEnabled(false);
                imageButton.setEnabled(false);
                break;
        }
    }

    public boolean isACorrectPosition(Integer[] Pair, boolean Orientation) {
        switch (Ship) {
            case 1:
                if (Orientation) {
                    if (Pair[1] > 6) {
                        return false;
                    }
                } else {
                    if (Pair[0] > 11) {
                        return false;
                    }
                }
                break;
            case 2:
                if (Orientation) {
                    if (Pair[1] > 6) {
                        return false;
                    }
                } else {
                    if (Pair[0] > 11) {
                        return false;
                    }
                }
            case 3:
                if (Orientation) {
                    if (Pair[1] > 7) {
                        return false;
                    }
                } else {
                    if (Pair[0] > 12) {
                        return false;
                    }
                }
        }
        return true;
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

    public void putShipA(View view) {
        Ship = 1;
    }

    public void putShipB(View view) {
        Ship = 2;
    }

    public void putShipC(View view) {
        Ship = 3;
    }
}
