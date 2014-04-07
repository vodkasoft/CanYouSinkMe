package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HostOrJoin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_host_or_join);
    }

    public void GoToJoinGame(View view){
        Intent intent = new Intent(this, JoinGame.class);
        startActivity(intent);
    }

    public void GoToHostGame(View view){
        Intent intent = new Intent(this, HostGame.class);
        startActivity(intent);
    }
}
