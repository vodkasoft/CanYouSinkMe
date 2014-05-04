package com.vodkasoft.canyousinkme.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vodkasoft.canyousinkme.dataaccess.BackendServiceAccessor;
import com.vodkasoft.canyousinkme.dataaccess.model.User;

import static com.vodkasoft.canyousinkme.utils.Constant.BACKEND_HOST;
import static com.vodkasoft.canyousinkme.utils.Constant.LEADERBOARD_ERROR;

import java.util.ArrayList;
import java.util.List;

public class Leaderboards extends Activity {

    private void loadLeaderboard(){

        String appKey = getResources().getString(R.string.app_key);
        String clientSecret = getResources().getString(R.string.client_secret);
        String serverResponseKey = getResources().getString(R.string.server_response_key);

        BackendServiceAccessor backendServiceAccessor = new BackendServiceAccessor(
                BACKEND_HOST,
                appKey,
                clientSecret,
                serverResponseKey,
                this);

        backendServiceAccessor.getGlobalLeaderboards(new BackendServiceAccessor.Listener<List<User>>() {
            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(getApplicationContext(), LEADERBOARD_ERROR, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onResponse(List<User> response) {
                String[] idList = new String[(response.size())];

                int userIndex = 0;
                for(User user : response){
                    idList[userIndex++] = user.getmId();
                }

                ListView listViewLeaderboard = (ListView) findViewById(R.id.listViewLeaderboard);

                listViewLeaderboard.setAdapter(new LeaderboardAdapter(getApplicationContext(), idList));

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboards);
        loadLeaderboard();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leaderboards, menu);
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
