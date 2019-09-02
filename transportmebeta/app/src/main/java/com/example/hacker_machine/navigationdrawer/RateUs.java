package com.example.hacker_machine.navigationdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
/**
 * Created by hacker-machine on 5/4/16.
 */
public class RateUs  extends AppCompatActivity {
    private Button button;
    private RatingBar ratingbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rateus);
        addListenerOnButtonClick();
    }

    public void addListenerOnButtonClick(){
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar);
        button=(Button)findViewById(R.id.submitRating);

        //Drawable drawable = ratingbar1.getProgressDrawable();
        //drawable.setColorFilter(Color.parseColor("#FFFDEC00"), PorterDuff.Mode.SRC_ATOP);
        //Performing action on Button Click
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating = String.valueOf(ratingbar1.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }

        });
    }
}
