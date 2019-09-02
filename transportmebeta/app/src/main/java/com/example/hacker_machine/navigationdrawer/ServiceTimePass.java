package com.example.hacker_machine.navigationdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hacker-machine on 6/3/16.
 * This activity is made for 3 things..
 * 1).Entertainment
 * 2).Entertainment
 * 3).Entertainment
 */
public class ServiceTimePass extends AppCompatActivity {

    TextView info;
    Button buttonOpen;
    final static int RQS_OPEN_AUDIO_MP3 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_time_pass);

        Button btnPlayMusic = (Button) findViewById(R.id.PlayMusic);
        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setDataAndType(Uri.parse("http://example.org/video.mp4"), "video/*");
                    startActivity(in);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        buttonOpen = (Button)findViewById(R.id.open);
        buttonOpen.setOnClickListener(buttonOpenOnClickListener);
        info = (TextView)findViewById(R.id.info);


    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu; this adds items to the action barr if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }
    OnClickListener buttonOpenOnClickListener
            = new OnClickListener(){
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();
            intent.setType("audio/mp3");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(
                    intent, "Open Audio (mp3) file"), RQS_OPEN_AUDIO_MP3);
        }};
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RQS_OPEN_AUDIO_MP3) {
                Uri audioFileUri = data.getData();
                info.setText(audioFileUri.getPath());
            }
        }
    }
}

