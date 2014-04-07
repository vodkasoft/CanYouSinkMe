package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;

public class HostGame extends Activity {
    private Thread Timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_host_game);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "JustAnotherHand.ttf");
        TextView textView = (TextView) findViewById(R.id.host_game_txt);
        textView.setTypeface(typeface);
        textView.setTextSize(40);

        // Bluetooth
        BleutoothManager.setSTATE("HOST");
        BleutoothManager.setActivity(this);
        BleutoothManager.setDiscoverable();
        BleutoothManager.startServerConnection(getResources().getString(R.string.app_uuid));

        // Timer
        waitingForPlayer();
    }

    public void waitingForPlayer(){
        final TextView textView = (TextView) findViewById(R.id.waitingdevices_txt);
        Timer = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean isConnected = false;
                for (int i=0; i<30; i++){
                    Log.d("HOLIS", String.valueOf(BleutoothManager.isConnectionActive()));
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
                if (isConnected) {
                    HostGame.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(HostGame.this, CreateBoard.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        Timer.start();
    }

    private void startBoard(){
        Intent intent = new Intent(HostGame.this, CreateBoard.class);
        startActivity(intent);
    }

    public void showToastWithMessage(CharSequence ToastText){
        Toast toast = Toast.makeText(HostGame.this, ToastText,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.host_game, menu);
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
