package com.example.hacker_machine.navigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hacker-machine on 5/4/16.
 */
public class MainSplashScreen extends AppCompatActivity {

    private final long DELAYSource = 2000; // in ms
    private Timer timerSource;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timerSource = new Timer();
        timerSource.schedule(new TimerTask() {
            @Override
            public void run() {
                ((ShareVariable)getApplication()).setEntryFlag(false);
              startActivity(new Intent(MainSplashScreen.this,MainActivity.class));
            }
        }, DELAYSource);
    }
}
