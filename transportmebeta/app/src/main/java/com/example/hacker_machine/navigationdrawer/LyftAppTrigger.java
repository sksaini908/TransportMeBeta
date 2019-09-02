package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 11/4/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hacker-machine on 10/4/16.
 */
public class LyftAppTrigger extends AppCompatActivity {

    private static final String TAG = "lyft:Example";
    private static final String LYFT_PACKAGE = "me.lyft.android";
    private  String pickLat="12.980712";
    private String  pickLng="74.803145";
    private String dropLat="12.963455";
    private String dropLng="74.890387";
    private String ride_type="ridetype?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_cabs);
        deepLinkIntoLyft();
    }
    private void deepLinkIntoLyft() {
        if (isPackageInstalled(this, LYFT_PACKAGE)) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String ridetype=extras.getString("Ride_Type");
                if(ridetype!=null)ride_type=ride_type+"id="+extras.getString("Ride_Type");
            }

            pickLat= ((ShareVariable)getApplication()).getSt_latitude();
            pickLng=((ShareVariable)getApplication()).getSt_longitude();
            dropLat=((ShareVariable)getApplication()).getEnd_latitude();
            dropLng= ((ShareVariable)getApplication()).getEnd_longitude();
            //partner=i8VynEvD8leC
            Toast.makeText(getApplicationContext(), "pickLat : "+pickLat+"\n"+"pickLng : "+pickLng+"\n"+
                    "dropLat : "+dropLat+"\n"+"dropLng : "+dropLng, Toast.LENGTH_SHORT).show();
            String link="lyft://" + ride_type +"&pickup[latitude]=" + pickLat+"&pickup[longitude]=" +
                    pickLng+"&destination[latitude]=" + dropLat +"&destination[longitude]=" +dropLng;
            openLink(this,link);
            Log.d(TAG, "Lyft is already installed on your phone, deeplinking.");
        } else {
            openLink(this, "https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE);

            Log.d(TAG, "Lyft is not currently installed on your phone, opening Play Store.");
        }
    }

    static void openLink(Activity activity, String link) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
        playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        playStoreIntent.setData(Uri.parse(link));
        activity.startActivity(playStoreIntent);
    }

    static boolean isPackageInstalled(Context context, String packageId) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // ignored.
        }
        return false;
    }
}

