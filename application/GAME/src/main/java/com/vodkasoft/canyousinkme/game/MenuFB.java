package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vodkasoft.canyousinkme.gamelogic.GameManager;
import com.vodkasoft.canyousinkme.gamelogic.IConstant;

public class MenuFB extends Activity implements IConstant {

    public void About_Event(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void GoToHostOrJoin(View view) {
        GameManager.setMatchType(BLUETOOTH_MATCH);
        Intent intent = new Intent(this, HostOrJoin.class);
        startActivity(intent);
    }

    public void Leaderboards_Event(View view) {
        Intent intent = new Intent(this, Leaderboards.class);
        startActivity(intent);
    }

    public void goToCreateBoard(View view) {
        GameManager.setMatchType(LOCAL_MATCH);
        Intent intent = new Intent(this, CreateBoard.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_fb);
        TextView TV = (TextView) findViewById(R.id.menufb_welcome_txt);
        TV.setText("Welcome, " + FBSession.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fb, menu);
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
