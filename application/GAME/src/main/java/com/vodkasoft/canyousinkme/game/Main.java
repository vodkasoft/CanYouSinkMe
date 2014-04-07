package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
    MediaPlayer MP;
    boolean Minimized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        MP = MediaPlayer.create(this,R.raw.velkoz);
        //MP.start();
    }

    //Multiplayer_btn method
    //This method displays the Login activity
    public void GoLogin(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        Minimized = false;
    }

    //About_btn method
    //This method displays the About activity
    public void GoAbout(View view){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        Minimized = false;
    }

    //This method returns true is the application is minimized
    //or false if there is another activity showing up instead the Main one
    public boolean isMinimized(){
        return Minimized;
    }

    //This method pauses the music only if the application is being minimized
    @Override
    public void onPause(){
        super.onPause();
        if(isMinimized()){
           //MP.pause();
        }
    }

    //This method plays the music in the moment the application
    //is maximized
    @Override
    public void onResume() {
        super.onResume();
        //MP.start();
        Minimized = true;
    }
}
