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

        BackendServiceAccessor backendServiceAccessor = new BackendServiceAccessor(BACKEND_HOST, this);

        backendServiceAccessor.getGlobalLeaderboards(new BackendServiceAccessor.Listener<List<User>>() {
            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(getApplicationContext(), LEADERBOARD_ERROR, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onResponse(List<User> response) {
                List<String> idList = new ArrayList<String>(response.size());

                for(User user : response){
                    idList.add(user.getmId());
                }

                ArrayAdapter listAdapterLeaderboard =
                        new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, idList);

                ListView listViewLeaderboard = (ListView) findViewById(R.id.listViewLeaderboard);

                listViewLeaderboard.setAdapter(listAdapterLeaderboard);

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
