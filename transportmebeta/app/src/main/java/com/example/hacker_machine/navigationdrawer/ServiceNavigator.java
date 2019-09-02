package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 6/3/16.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Created by hacker-machine on 3/3/16.
 * Service Navigation Activity..Later this will use Google Map API...Better Luck
 */
public class ServiceNavigator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_navigator);

        startActivity(new Intent(ServiceNavigator.this,Navigation.class));
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu; this adds items to the action barr if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;


    }

}