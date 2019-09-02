package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 5/4/16.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hacker-machine on 23/3/16.
 */

public class ViewFullImage extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_image);
        Intent intent = getIntent();
        int i = intent.getIntExtra(ImageListView.BITMAP_ID, 0);
        imageView = (ImageView) findViewById(R.id.imageViewFull);
        imageView.setImageBitmap(GetAlImages.bitmaps[i]);

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
                startActivity(new Intent(ViewFullImage.this,MainActivity.class));
            }
        });
        final Button upload = (Button) findViewById(R.id.UploadImageDp);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ViewFullImage.this,ProfileImageHandler.class));
            }
        });
    }
}