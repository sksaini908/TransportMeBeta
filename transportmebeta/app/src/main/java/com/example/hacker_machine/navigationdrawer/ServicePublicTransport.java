package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 6/3/16.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Created by hacker-machine on 3/3/16.
 * As name describe...Public Transport Activity  class
 */
public class ServicePublicTransport extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        Button navigation = (Button)findViewById(R.id.btnSourceDestNavigation);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                String __dest__ = ((ShareVariable) getApplication()).getDestination();
                __dest__ = __dest__.replaceAll(" ", "+");
                String Lat = ((ShareVariable) getApplication()).getEnd_latitude();
                String Lng = ((ShareVariable) getApplication()).getEnd_longitude();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                        Lat + "," + Lng
                        +
                        "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(mapIntent);
            }
        });
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu; this adds items to the action barr if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }
}