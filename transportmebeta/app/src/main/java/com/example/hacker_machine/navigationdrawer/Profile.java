package com.example.hacker_machine.navigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by hacker-machine on 6/3/16.
 * This class used to show profile information and get login facilities also.
 */
public class Profile extends AppCompatActivity {

    EditText mEditUser,mEditPass;
    String user,pass;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //startActivity(new Intent(this, ImageListView.class));
        //get username and password if user is already logged in.
        //and if not, then redirect to login activity
        String check_user_name = ((ShareVariable) this.getApplication()).getUserName();

            //show profile
            setContentView(R.layout.my_profile);

            //time to fetch data from DB using username and password
            //username
            ((TextView) findViewById(R.id.textViewProfileName)).setText(((ShareVariable) this.getApplication()).getUserName());
            //user profile image
            //To update user image as soon as he/she login
            //Booking history
            //update email id
            //update ola cab account info
            //update uber cab account info
            //handle logout
            final Button button = (Button) findViewById(R.id.Logout);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((ShareVariable) getApplication()).setUserName(null);
                    startActivity(new Intent(Profile.this,MainActivity.class));
                }
            });
            final Button upload = (Button) findViewById(R.id.UploadImageDp);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Profile.this,ProfileImageHandler.class));
                }
            });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
