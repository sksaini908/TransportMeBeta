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
public class SendFeedback extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendfeedback);
        Button button = (Button) findViewById(R.id.sendFeedback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                EditText editText = (EditText)findViewById(R.id.EditFeedback);
                String msg = editText.getText().toString();
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"rameshchandranitk2017@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback of TransportMe");
                email.putExtra(Intent.EXTRA_TEXT, "Provide us Your Valuable FeedBack \n "+msg);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }
}
