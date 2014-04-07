package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;

import java.util.ArrayList;
import java.util.Timer;

public class JoinGame extends Activity {

    private ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_game);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "JustAnotherHand.ttf");
        TextView textView = (TextView) findViewById(R.id.join_game_txt);
        textView.setTypeface(typeface);
        textView.setTextSize(40);

        BleutoothManager.setActivity(this);

        ListView listView = (ListView) findViewById(R.id.listview_joingame);

        // Adapters
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice,
                BleutoothManager.getDiscoverableDevicesName());

        // ListView
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> deviceAdresses = BleutoothManager.getDiscoverableDevicesAddress();
                BluetoothDevice address = BleutoothManager.getAdapter().getRemoteDevice(deviceAdresses.get(i));
                BleutoothManager.startClientConnection(address,
                        getResources().getString(R.string.app_uuid));

                Intent intent = new Intent(JoinGame.this, CreateBoard.class);
                startActivity(intent);
            }
        });

        BleutoothManager.setSTATE("JOIN");
        BleutoothManager.findDevices();

        showAvailableHosts();
    }

    public void showAvailableHosts(){
        CountDownTimer timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                if (!BleutoothManager.isConnectionActive()){
                    CharSequence CS = "No hosts available. Try again";
                    showToastWithMessage(CS);
                }
                finish();
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.join_game, menu);
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

    public void showToastWithMessage(CharSequence ToastText){
        Toast toast = Toast.makeText(JoinGame.this, ToastText,
                Toast.LENGTH_SHORT);
        toast.show();
    }

}
