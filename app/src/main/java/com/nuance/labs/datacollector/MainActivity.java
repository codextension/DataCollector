package com.nuance.labs.datacollector;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {
    private Switch switchBtn;
    private Intent serviceIntent;
    private ComponentName cName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchBtn = findViewById(R.id.dataCollectionToggler);
        serviceIntent= new Intent(this,BackgroundService.class);
    }

    public void onToggle(View view) {
        if(switchBtn.isChecked()){
            cName = startService(serviceIntent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }else{
            stopService(serviceIntent);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }
}
