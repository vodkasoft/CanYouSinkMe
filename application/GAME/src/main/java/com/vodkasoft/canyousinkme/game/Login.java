package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class Login extends FragmentActivity {
    MediaPlayer M;
    private FacebookAuthFragment authFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            authFragment = new FacebookAuthFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, authFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            authFragment = (FacebookAuthFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }

    public void GoHostOrJoin(View view){
        Intent intent = new Intent(this, HostOrJoin.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
