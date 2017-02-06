package com.example.shara.passwordgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    final static String THREAD_PASS = "ThreadPasswords";
    final static String ASYNC_PASS = "AsyncPasswords";
    ProgressDialog passwordGenerationProgress;
    SeekBar forThreadPasswordCount;
    SeekBar forThreadPasswordLength;
    SeekBar forAsyncPasswordCount;
    SeekBar forAsyncPasswordLength;
    int forThreadCount = 0;
    int forThreadLength = 0;
    int forAsyncCount = 0;
    int forAsyncLength = 0;
    int finalProgressCount = 0;
    Intent sendToGeneratedPasswords;
    ArrayList<String> finalPasswordsForAsyncTask;
    ArrayList<String> finalPasswordsForThreads;
    Handler threadHandler;
    ExecutorService generatePasswordUsingThreads;
    GeneratePasswordsUsingAsyncTask generatePasswordsUsingAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendToGeneratedPasswords = new Intent(MainActivity.this,GeneratedPasswords.class);
        finalPasswordsForThreads = new ArrayList<String>();
        //Create Progress Dialogue Here
        passwordGenerationProgress = new ProgressDialog(MainActivity.this);
        passwordGenerationProgress.setMessage(getResources().getString(R.string.progressDialog));
        passwordGenerationProgress.setCancelable(false);
        passwordGenerationProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        //Handler Usage Here
        threadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d("apple","orange");

                if(msg.what == 0x00){

                    Log.d("test","before error");
                    String str = (String) msg.obj;
                   // msg.getData().getStringArray(sendThreadProgress.obj)
                    finalPasswordsForThreads.add(str);
                    Log.d("test",str);
                    Log.d("test",String.valueOf(finalProgressCount));

                    if(finalProgressCount == (forAsyncCount+forThreadCount)-1) {

                        passwordGenerationProgress.dismiss();
                        Bundle threadBundle = new Bundle();
                        threadBundle.putSerializable(THREAD_PASS, finalPasswordsForThreads);
                        sendToGeneratedPasswords.putExtras(threadBundle);
                        Bundle asyncBundle = new Bundle();
                        asyncBundle.putSerializable(ASYNC_PASS, finalPasswordsForAsyncTask);
                        sendToGeneratedPasswords.putExtras(asyncBundle);
                        startActivity(sendToGeneratedPasswords);
                    }
                    else {
                        finalProgressCount++;

                        passwordGenerationProgress.setProgress(finalProgressCount);
                    }
                }

                return false;
            }
        });

        //For Thread Section
        final SeekBar forThreadPasswordCount = (SeekBar) findViewById(R.id.passwordCountForThreadSeekbar);
        forThreadPasswordCount.setMax(10);
        final TextView forThreadSeekbarCountDisplay = (TextView) findViewById(R.id.displaySeekBarPasswordCountForThread);
        SeekBar forThreadPasswordLength = (SeekBar) findViewById(R.id.passwordLengthForThreadSeekBar);
        forThreadPasswordLength.setMax(16);
        final TextView forThreadPasswordLengthDisplay = (TextView) findViewById(R.id.displaySeekBarPasswordLengthForThread);
        forThreadPasswordCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                forThreadSeekbarCountDisplay.setText(String.valueOf(progress));
                forThreadCount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        forThreadPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                forThreadPasswordLengthDisplay.setText(String.valueOf(progress+7));
                forThreadLength = progress+7;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        //For Async Section
        SeekBar forAsyncPasswordCount = (SeekBar) findViewById(R.id.passwordCountForAsyncTaskSeeBar);
        forAsyncPasswordCount.setMax(10);
        final TextView forAsyncSeekbarCountDisplay = (TextView) findViewById(R.id.displaySeekBarPasswordCountForAsyncTask);
        SeekBar forAsyncPasswordLength = (SeekBar) findViewById(R.id.passwordLengthForAsyncTaskSeekbar);
        forAsyncPasswordLength.setMax(16);
        final TextView forAsyncPasswordLengthDisplay = (TextView) findViewById(R.id.displaySeekBarPasswordLengthForAsyncTask);
        forAsyncPasswordCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                forAsyncSeekbarCountDisplay.setText(String.valueOf(progress));
                forAsyncCount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        forAsyncPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                forAsyncPasswordLengthDisplay.setText(String.valueOf(progress+7));
                forAsyncLength = progress+7;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        //End Of Thread and Async Sections
        //Generate Section

        findViewById(R.id.generateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.d("test",String.valueOf(forThreadCount));
                Log.d("test",String.valueOf(forThreadLength));
                Log.d("test",String.valueOf(forAsyncCount));
                Log.d("test",String.valueOf(forAsyncLength));*/
                passwordGenerationProgress.setMax(forAsyncCount+forThreadCount);
                //Threads generated to Calculate Passwords
                generatePasswordUsingThreads = Executors.newFixedThreadPool(2);
                for(int j = 0; j<forThreadCount; j++) {
                  generatePasswordUsingThreads.execute(new GeneratePasswordsUsingThread());
                }
                //AsyncTask Here
                finalPasswordsForAsyncTask  = new ArrayList<String>();
                generatePasswordsUsingAsyncTask = new GeneratePasswordsUsingAsyncTask();
                generatePasswordsUsingAsyncTask.execute(forAsyncCount,forAsyncLength);
                passwordGenerationProgress.show();

            }
        });


    }
    class GeneratePasswordsUsingThread implements Runnable{
        static final int THREADEXEC = 0x00;

        @Override
        public void run() {
            Util passwordGenerator = new Util();
            Message sendThreadProgress = new Message();
            sendThreadProgress.what = THREADEXEC;
            sendThreadProgress.obj = passwordGenerator.getPassword(forThreadLength);
          //  Log.d("sendthread", sendThreadProgress.obj.toString());
            threadHandler.sendMessage(sendThreadProgress);
        }
    }

    public class GeneratePasswordsUsingAsyncTask extends AsyncTask<Integer,Integer,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Integer... params) {
            Util generatePasswordsForAsync = new Util();
            ArrayList<String> passwords = new ArrayList<String>();
            for (int i = 0 ; i< params[0] ; i++ ){
                finalPasswordsForAsyncTask.add(generatePasswordsForAsync.getPassword(params[1]));
                publishProgress(i);
            }
            return passwords;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            finalPasswordsForAsyncTask = s;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("demo",String.valueOf(finalProgressCount));
            if(finalProgressCount== (forAsyncCount+forThreadCount)-1) {

                passwordGenerationProgress.dismiss();
                Bundle threadBundle = new Bundle();
                threadBundle.putSerializable(THREAD_PASS, finalPasswordsForThreads);
                sendToGeneratedPasswords.putExtras(threadBundle);
                Bundle asyncBundle = new Bundle();
                asyncBundle.putSerializable(ASYNC_PASS, finalPasswordsForAsyncTask);
                sendToGeneratedPasswords.putExtras(asyncBundle);
                startActivity(sendToGeneratedPasswords);

            }
            else {
                finalProgressCount++;
                passwordGenerationProgress.setProgress(finalProgressCount);
            }

        }
    }

}
