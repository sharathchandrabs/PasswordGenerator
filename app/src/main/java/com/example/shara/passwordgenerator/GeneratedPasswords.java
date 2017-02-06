package com.example.shara.passwordgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedPasswords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        ArrayList<String> threadPass = new ArrayList<String>();
        ArrayList<String> asyncPass = new ArrayList<String>();

        Intent getPasswords = this.getIntent();
        Bundle passwordBundle = getPasswords.getExtras();

        threadPass = (ArrayList<String>) passwordBundle.getSerializable(MainActivity.THREAD_PASS);
        asyncPass = (ArrayList<String>) passwordBundle.getSerializable(MainActivity.ASYNC_PASS);

        Log.d("demo",threadPass.toString());
        Log.d("demo",asyncPass.toString());

        LinearLayout threadLayout = (LinearLayout) findViewById(R.id.displayPasswordsFromThreadScrollView);
        LinearLayout asyncLayout = (LinearLayout) findViewById(R.id.displayPasswordsFromAsyncScrollView);

        for(String s : threadPass){
            TextView thread = new TextView(this);
            thread.setText(s);
            threadLayout.addView(thread);
        }

        for(String str: asyncPass){
            TextView async = new TextView(this);
            async.setText(str);
            asyncLayout.addView(async);
        }
    }
}
