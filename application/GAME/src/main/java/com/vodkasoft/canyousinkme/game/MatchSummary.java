package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vodkasoft.canyousinkme.connectivity.BleutoothManager;
import com.vodkasoft.canyousinkme.dataaccess.BackendServiceAccessor;
import com.vodkasoft.canyousinkme.gamelogic.GameManager;

import static com.vodkasoft.canyousinkme.utils.Constant.BACKEND_HOST;
import static com.vodkasoft.canyousinkme.utils.Constant.BLUETOOTH_MATCH;
import static com.vodkasoft.canyousinkme.utils.Constant.MATCH_ERROR;
import static com.vodkasoft.canyousinkme.utils.Constant.USER_ERROR;

public class MatchSummary extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_match_summary);

        TextView textViewScore = (TextView) findViewById(R.id.matchsummary_score_txt);
        TextView textViewCondition = (TextView) findViewById(R.id.matchsummary_condition_txt);

        textViewScore.setText(String.valueOf(GameManager.getPlayerScore()));
        textViewCondition.setText(String.valueOf(GameManager.getCondition()));


        if(GameManager.getMatchType() == BLUETOOTH_MATCH &&
           GameManager.isHost()){

            String appKey = getResources().getString(R.string.app_key);
            String clientSecret = getResources().getString(R.string.client_secret);
            String serverResponseKey = getResources().getString(R.string.server_response_key);

            final BackendServiceAccessor backendServiceAccessor = new BackendServiceAccessor(
                    BACKEND_HOST,
                    appKey,
                    clientSecret,
                    serverResponseKey,
                    this);

            backendServiceAccessor.createMatch(
                    FBSession.getFacebookID(),
                    GameManager.getOpponentId(),
                    GameManager.getPlayerScore(),
                    GameManager.getOpponentScore(),
                    new BackendServiceAccessor.Listener<String>() {
                        @Override
                        public void onError(String error) {
                            Toast toast = Toast.makeText(
                                    getApplicationContext(),
                                    MATCH_ERROR,
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }

                        @Override
                        public void onResponse(String response) {
                            // Everything worked

                        }
                    }

            );
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_summary, menu);
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
