package com.vodkasoft.canyousinkme.gamelogic;

import android.os.AsyncTask;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/21/14.
 */
public class MissileResultTask extends AsyncTask<Void, Void, Void>{

    @Override
    public void onPreExecute(){}

    @Override
    protected Void doInBackground(Void... params){

        GameManager.updateMissileResult();

        return null;
    }
}
