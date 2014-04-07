package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class HostGame extends Activity {
    private CountDownTimer Timer;

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
        waitingForPlayer();
    }

    public void waitingForPlayer(){
        final TextView textView = (TextView) findViewById(R.id.waitingdevices_txt);
        Timer = new CountDownTimer(10000,3200) {
            @Override
            public void onTick(long l) {
                textView.setText(textView.getText()+".");
            }

            @Override
            public void onFinish() {
                CharSequence CS = "Nobody joined your game. Try again";
                showToastWithMessage(CS);
                finish();
            }
        }.start();
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
