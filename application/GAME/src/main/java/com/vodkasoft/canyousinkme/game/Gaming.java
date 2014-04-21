package com.vodkasoft.canyousinkme.game;

import com.vodkasoft.canyousinkme.gamelogic.GameManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class Gaming extends Activity {
    DualMatrix DUALMATRIX;
    Integer[] Pair;
    GameManager gameManager;
    private TextView textViewChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gaming);
        drawBoard();
    }

    public void drawBoard() {
        //Se usa el DUALMATRIX anterior
        DUALMATRIX = new DualMatrix();
        final GridView gridView = (GridView) findViewById(R.id.container_grid_gaming);
        final ImageAdapter imageAdapter = new ImageAdapter(Gaming.this, DUALMATRIX.get_IMGs());
        gridView.setAdapter(imageAdapter);
        createOnClickListener(gridView);
    }

    public void createOnClickListener(final GridView pgridView) {
        pgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.g, menu);
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
