package com.example.hacker_machine.navigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by hacker-machine on 5/4/16.
 */
public class Share extends AppCompatActivity{
    private static final String FORECAST_SHARE_HASHTAG = " #TransportMe1.1 ";
    private String mForecastStr="Awesome App for getting all cab info in one app";
    private EditText editText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        Button button = (Button) findViewById(R.id.appShare);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText = (EditText) findViewById(R.id.MsgToFriend);
                String MSG = editText.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, FORECAST_SHARE_HASHTAG +"\n"+ mForecastStr+"\n"+MSG+"\n");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }

}
