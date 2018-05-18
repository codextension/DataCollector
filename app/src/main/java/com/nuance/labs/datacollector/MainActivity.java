package com.nuance.labs.datacollector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    private static final int RSS_JOB_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent= new Intent(this,BackgroundService.class);
        startService(serviceIntent);
    }
}
