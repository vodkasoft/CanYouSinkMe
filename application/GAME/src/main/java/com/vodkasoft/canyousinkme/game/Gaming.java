package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.connectivity.BluetoothMessage;
import com.vodkasoft.canyousinkme.gamelogic.GameManager;
import com.vodkasoft.canyousinkme.gamelogic.MissileMessage;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

public class Gaming extends Activity {

    final int X_COORDINATE = 0;
    final int Y_COORDINATE = 1;

    DualMatrix DUALMATRIX;
    Integer[] Pair;
    GameManager gameManager;
    private TextView textViewChronometer;
    private Thread timer;

    public void createOnClickListener(final GridView pgridView) {
        pgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pair = getMatrixCoords(i);
            }
        });
    }

    public void drawBoard() {
        //Se usa el DUALMATRIX anterior
        DUALMATRIX = new DualMatrix();
        final GridView gridView = (GridView) findViewById(R.id.container_grid_gaming);
        final ImageAdapter imageAdapter = new ImageAdapter(Gaming.this, DUALMATRIX.get_IMGs());
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

    public void sendMissile(){
        MissileMessage missileMessage = new MissileMessage(Pair[X_COORDINATE], Pair[Y_COORDINATE]);
        String messageData = JsonSerializer.fromObjectToJson(missileMessage);
        BleutoothManager.sendMessage(BleutoothManager.MESSAGE_KEY, messageData);
    }

    public void waitForMissileResult(){
        timer = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean isConnected = false;
                for (int i=0; i<30; i++){
                    if (BleutoothManager.isConnectionActive()){
                        BluetoothMessage btMessage = BleutoothManager.dequeueMessage();

                        // Missile was succesful
                        if (Boolean.valueOf(btMessage.getData())){

                        } else {

                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timer.start();
    }

    public void waitForEnemyMissile(){
        timer = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean isConnected = false;
                for (int i=0; i<30; i++){
                    if (BleutoothManager.isConnectionActive()){
                        isConnected = true;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gaming);

        drawBoard();

        // Init UI variables
        textViewChronometer = (TextView) findViewById(R.id.textViewChronometer);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.g, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
