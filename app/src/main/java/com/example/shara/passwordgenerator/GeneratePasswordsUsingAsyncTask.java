package com.example.shara.passwordgenerator;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by shara on 1/30/2017.
 */

public class GeneratePasswordsUsingAsyncTask extends AsyncTask<Integer,Integer,String>{

    String password;
    @Override
    protected String doInBackground(Integer... params) {
        Util generatePasswordsForAsync = new Util();
        for (int i = 0 ; i< params[0] ; i++ ){
            password = generatePasswordsForAsync.getPassword(params[1]);
            publishProgress(i);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        s = password;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}