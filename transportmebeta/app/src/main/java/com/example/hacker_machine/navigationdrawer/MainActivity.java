package com.example.hacker_machine.navigationdrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView list;
    EditText txtUsername,txtPassword,mEditFrom,mEditTo;
    String[] Services = {  "Cabs", "Public Transport", "Navigator", "Leisure"} ;
    Integer[] imageId = {   R.drawable.cabs, R.drawable.public_tran, R.drawable.navigator, R.drawable.leisure };
    ProgressDialog pDialog;
    String loguse1,logpass1,Source=null,Destination=null,IAMREGISTEREDALREADY="failure",
                            OnlineConnection="No Internet Connection";
    private  Boolean PredictionListUpdateSignal=false;
    private String  GeoLoactionUpdateSignal="Current";
    NavigationView navigationView;
    private String __Location__;
    private  String ____Result__Source;
    private String item__Source[]={"Choose place"};
    private String item__Destination[]={"Choose place"};
    private String Place="";
    private String ___placeSource__="";
    private String ___placeDestination__="";
    private AutoCompleteTextView textView__Source=null;
    private AutoCompleteTextView textView__Destination=null;
    private Boolean FlagSource=true;
    private Timer timerSource = new Timer();
    private Timer timerDestination = new Timer();
    private final long DELAYSource = 1000; // in ms
    private final long DELAYDestination = 1000; // in ms
    private ArrayList<String> currentList=new ArrayList<String>();
    private ArrayAdapter<String> adapter__Source;
    private ArrayAdapter<String> adapter__Destination;
    private ArrayList<String> prediction__Source;
    private String ResultSource,InternetAvailability="No Internet Connection";
    AppLocationService appLocationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentList.add("Select Place");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Update user name
        View headerLayout = navigationView.getHeaderView(0);
        TextView useremail = (TextView) headerLayout.findViewById(R.id.textView_user_id);

        if(useremail == null || ((ShareVariable) getApplication()).getUserName() == null){
        } else useremail.setText(((ShareVariable) getApplication()).getName());

        //Update user mail
        TextView useremail2 = (TextView) headerLayout.findViewById(R.id.user_mail);
        if(useremail2 == null || ((ShareVariable) getApplication()).getUserName() == null){
        } else useremail2.setText(((ShareVariable) getApplication()).getUserName());


        CustomList adapter = new  CustomList(MainActivity.this, Services, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Source = ((ShareVariable) getApplication()).getSource();
                Destination = ((ShareVariable) getApplication()).getDestination();
                //To hide soft keyboard after submit button is clicked
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                if (Services[+position].equals("Cabs")) {
                    if (Source == null) {
                        Toast.makeText(getApplicationContext(), "Source Address Empty", Toast.LENGTH_SHORT).show();
                    } else if (Destination == null) {
                        Toast.makeText(getApplicationContext(), "Destination Address Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(MainActivity.this, ServiceCabs.class));
                    }
                } else if (Services[+position].equals("Public Transport")) {
                    //startActivity(new Intent(MainActivity.this, ServicePublicTransport.class));
                   if (Destination == null) {
                        Toast.makeText(getApplicationContext(), "Destination Address Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        //startActivity(new Intent(MainActivity.this,Navigation.class));
                        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");

                        String __dest__ = ((ShareVariable)getApplication()).getDestination();
                        __dest__=__dest__.replaceAll(" ","+");
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+__dest__+"&mode=d");

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(mapIntent);
                        Toast.makeText(getApplicationContext(),"Click on Transit(2nd Tab)",Toast.LENGTH_SHORT).show();
                    }
                } else if (Services[+position].equals("Navigator")) {
                     if (Destination == null) {
                        Toast.makeText(getApplicationContext(), "Destination Address Empty", Toast.LENGTH_SHORT).show();
                    } else {

                        //startActivity(new Intent(MainActivity.this,Navigation.class));
                        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                        String __dest__ = ((ShareVariable)getApplication()).getDestination();
                        __dest__=__dest__.replaceAll(" ","+");
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+__dest__+"&mode=d");
                        Toast.makeText(getApplicationContext(),"Navigation : Your Location to "+
                                        ((ShareVariable)getApplication()).getDestination(),
                                Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(mapIntent);
                    }
                } else if (Services[+position].equals("Leisure")) {
                    startActivity(new Intent(MainActivity.this, ServiceTimePass.class));
                }

                if (imageId[+position] == R.drawable.cabs) {
                    if (Source == null ) {
                        Toast.makeText(getApplicationContext(), "Source Address Empty", Toast.LENGTH_SHORT).show();
                    } else if (Destination == null ) {
                        Toast.makeText(getApplicationContext(), "Destination Address Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(MainActivity.this, ServiceCabs.class));
                    }
                } else if (imageId[+position] == R.drawable.public_tran) {

                    //startActivity(new Intent(MainActivity.this, ServicePublicTransport.class));
                    if (Destination == null) {
                        Toast.makeText(getApplicationContext(), "Destination Address Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        //startActivity(new Intent(MainActivity.this,Navigation.class));
                        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");

                        String __dest__ = ((ShareVariable)getApplication()).getDestination();
                        __dest__=__dest__.replaceAll(" ","+");
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+__dest__+"&mode=d");
                        Toast.makeText(getApplicationContext(),"Public Transport : Your Location to "+
                                        ((ShareVariable)getApplication()).getDestination(),
                                Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(mapIntent);
                        Toast.makeText(getApplicationContext(),"Click on Transit(2nd Tab)",Toast.LENGTH_SHORT).show();
                    }
                } else if (imageId[+position] == R.drawable.navigator) {
                   if (Destination == null ) {
                        Toast.makeText(getApplicationContext(), "Destination Address Empty", Toast.LENGTH_SHORT).show();
                   } else {
                        //startActivity(new Intent(MainActivity.this,Navigation.class));
                        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                        String __dest__ = ((ShareVariable)getApplication()).getDestination();
                        __dest__=__dest__.replaceAll(" ","+");
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+__dest__+"&mode=d");
                       Toast.makeText(getApplicationContext(),"Navigation : Current Location to "+
                                       ((ShareVariable)getApplication()).getDestination(),
                               Toast.LENGTH_SHORT).show();
                       Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(mapIntent);
                   }
                } else if (imageId[+position] == R.drawable.leisure) {
                    startActivity(new Intent(MainActivity.this, ServiceTimePass.class));
                }
            }
        });


        textView__Source = (AutoCompleteTextView)findViewById(R.id.__Source);
        adapter__Source = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, item__Source);
        textView__Source.addTextChangedListener(watch__Source);
        textView__Source.setThreshold(1);
        //Set adapter to AutoCompleteTextView
        textView__Source.setAdapter(adapter__Source);
        adapter__Source.notifyDataSetChanged();
        textView__Source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                __Location__ = ___placeSource__ = adapter__Source.getItem(position);
                ((ShareVariable) getApplication()).setSource(___placeSource__);
                Toast.makeText(MainActivity.this,"Selected Source"+ adapter__Source.getItem(position)+" \n "+
                      ___placeSource__,Toast.LENGTH_LONG).show();
                GeoLoactionUpdateSignal = "Src";
                getGeoLocationLatLng();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        textView__Destination = (AutoCompleteTextView)findViewById(R.id.__Destination);
        adapter__Destination = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, item__Destination);
        textView__Destination.addTextChangedListener(watch__Destination);
        textView__Destination.setThreshold(1);
        //Set adapter to AutoCompleteTextView
        textView__Destination.setAdapter(adapter__Destination);
        adapter__Destination.notifyDataSetChanged();
        textView__Destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                __Location__ = ___placeDestination__ = adapter__Destination.getItem(position);
                ((ShareVariable) getApplication()).setDestination(___placeDestination__);
                Toast.makeText(MainActivity.this, "Selected Dest"+adapter__Destination.getItem(position)+" \n "+
                      ___placeDestination__, Toast.LENGTH_LONG).show();
                GeoLoactionUpdateSignal = "Dest";
                getGeoLocationLatLng();

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        boolean __flag=((ShareVariable)getApplication()).getEntryFlag();
        if(__flag==false){
            ((ShareVariable)getApplication()).setEntryFlag(true);
            appLocationService = new AppLocationService(MainActivity.this);
            Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                double latitude = gpsLocation.getLatitude();
                double longitude = gpsLocation.getLongitude();
                String result = "Latitude: " + gpsLocation.getLatitude() + " Longitude: " + gpsLocation.getLongitude();
                //Toast.makeText(getApplicationContext(), result,Toast.LENGTH_SHORT).show();
            } else {
                showSettingsAlert();
            }
            Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());
            } else {
                showSettingsAlert();
            }

        }
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:  Bundle bundle = message.getData();
                         locationAddress = bundle.getString("address");
                    break;
                default: locationAddress = null;
            }
            if(locationAddress!=null){
                String []string = locationAddress.split("#");
                String Lat=string[0],Lng=string[1],Add=string[2];
                if(Add!=null){
                    //Toast.makeText(getApplicationContext(),Add+"\nLat : "+Lat+"\nLng : "+Lng,Toast.LENGTH_SHORT).show();
                    textView__Source.setText(Add);
                    ((ShareVariable)getApplication()).setSource(Add);
                    ((ShareVariable)getApplication()).setSt_longitude(Lat);
                    ((ShareVariable)getApplication()).setSt_longitude(Lng);
                }
            }
        }
    }
    TextWatcher watch__Source = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            Place=s.toString();
            ___placeSource__=s.toString();
            if(timerSource != null)
                timerSource.cancel();
        }
        @Override
        public void afterTextChanged(final Editable s) {
            //avoid triggering event when text is too short
            // TODO Auto-generated method stub
            Place=s.toString();
            ___placeSource__=s.toString();
            if (s.length() >= 3) {
                timerSource = new Timer();
                timerSource.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PredictionListUpdateSignal = true;
                        __Location__ = s.toString();
                        UpdatePredictionnewList();
                    }
                }, DELAYSource);
            }
        }
    };
    TextWatcher watch__Destination = new TextWatcher(){
        @Override Toast.makeText(getApplicationContext(),"Response : "+IAMREGISTEREDALREADY+"\n"+OnlineConnection,Toast.LENGTH_LONG).show();
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count) {
            Place=s.toString();___placeDestination__=s.toString();
            if(timerDestination != null) timerDestination.cancel();}
        @Override
        public void afterTextChanged(final Editable s) {
            Place=s.toString();   ___placeDestination__=s.toString();
            if (s.length() >= 3) {
                timerDestination = new Timer();
                timerDestination.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PredictionListUpdateSignal = false;
                        __Location__ = s.toString();
                        UpdatePredictionnewList();
                    }
                }, DELAYDestination);
            }
        }
    };
    public void updateAdapter(Boolean Source){
        if(Source){
            adapter__Source = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,
                    currentList);
            textView__Source.setThreshold(1);
            textView__Source.setAdapter(adapter__Source);
            adapter__Source.notifyDataSetChanged();
        }else {
            adapter__Destination = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,
                    currentList);
            textView__Destination.setThreshold(1);
            textView__Destination.setAdapter(adapter__Destination);
            adapter__Destination.notifyDataSetChanged();
        }
    }
    private void  UpdatePredictionnewList(){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.i("UpdatePredictionnewList",__Location__);
            }
            protected String doInBackground(String... params) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    String Place__Source=__Location__.replaceAll(" ", "%20");
                    String myUrl__Source = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                            Place__Source + "&sensor=true&key=" + "AIzaSyCBh-dxTcC2v_zG7aPIcNdamwC4w1EIPJ8";
                    HttpPost httpPost = new HttpPost(myUrl__Source);
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    String responseStr = EntityUtils.toString(entity);
                    ____Result__Source = responseStr;
                } catch (UnsupportedEncodingException e) {
                    Log.i("UnsupportedEncodingExc","UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.i("ClientProtocolException","ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException","IOException");
                    e.printStackTrace();
                } catch (NullPointerException e){
                    Log.i("NullPointerException","NullPointerException");
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String __Result) {
                super.onPostExecute(__Result);
                    try {
                        JSONObject mainobj= new JSONObject(____Result__Source);
                        JSONArray jsonArray=mainobj.getJSONArray("predictions");
                        prediction__Source=new ArrayList<String>();
                        prediction__Source.add("Choose place");
                        if(jsonArray.length() == 0){
                            prediction__Source.add(__Location__);
                            System.out.print(____Result__Source);
                        }else{
                            System.out.printf("\n******************************************************\n");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String plac = obj.getString("description");
                                prediction__Source.add(plac);
                                System.out.print(plac);System.out.println();
                            }
                            System.out.printf("******************************************************\n\n");
                        }
                        currentList = prediction__Source;
                        updateAdapter(PredictionListUpdateSignal);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
   private void getGeoLocationLatLngHelper(String Lat,String Lng,String Name){
      // Toast.makeText(getApplicationContext(),Name+" "+Lat+""+Lng,Toast.LENGTH_LONG).show();
       if(GeoLoactionUpdateSignal.equals("Src")){
           ((ShareVariable)getApplication()).setSt_latitude(Lat);
           ((ShareVariable)getApplication()).setSt_longitude(Lng);
       }else if(GeoLoactionUpdateSignal.equals("Dest")){
           ((ShareVariable)getApplication()).setEnd_latitude(Lat);
           ((ShareVariable)getApplication()).setEnd_longitude(Lng);
       }else{
           Toast.makeText(getApplicationContext(),"Current Position Updated",Toast.LENGTH_SHORT).show();
       }
   }
    private void  getGeoLocationLatLng(){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailability="No Internet Connection";
            }
            protected String doInBackground(String... params) {
                try {
                    String finalPlace = __Location__.replaceAll(" ", "%20");
                    String URL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + finalPlace + "&sensor=false";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(URL);
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    String responseStr = EntityUtils.toString(entity);
                    ResultSource = responseStr;
                    InternetAvailability=responseStr;

                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(InternetAvailability.equals("No Internet Connection") == true){
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","NotCapturedLatLng");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else if(result == "success"){
                    try {
                        String latitudeHelper="",longitudrHelper="",NameHelper="";
                        JSONObject mainObj = new JSONObject(ResultSource);
                        JSONArray jarray = mainObj.getJSONArray("results");
                        for (int i = 0; i < jarray.length() ; i++) {
                            JSONObject object = jarray.getJSONObject(i);
                            NameHelper = object.getString("formatted_address");
                            JSONObject bigImage = object.getJSONObject("geometry");
                            JSONObject tiMed = bigImage.getJSONObject("location");
                            Double latitude = Double.parseDouble(tiMed.getString("lat"));
                            Double longitude = Double.parseDouble(tiMed.getString("lng"));

                            latitudeHelper=Double.toString(latitude);
                            longitudrHelper=Double.toString(longitude);

                          //  Toast.makeText(getApplicationContext(), NameHelper + "\nLatitude : " + latitudeHelper +
                            //        "\nLongitude : " + longitudrHelper+"\n", Toast.LENGTH_SHORT).show();
                            if(NameHelper.equals(__Location__)){
                                getGeoLocationLatLngHelper(latitudeHelper, longitudrHelper, NameHelper);
                                break;
                            }
                        }
                        getGeoLocationLatLngHelper(latitudeHelper, longitudrHelper, NameHelper);

                    } catch ( JSONException e){
                        Log.i("JASONArray EXEPTION", "Error in parsing");
                        Toast.makeText(getApplicationContext(),"Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
               // Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                if(result.equals("CapturedLatLng")){
                    Bundle ExtraIntent = getIntent().getExtras();
                    if(ExtraIntent != null){
                        String Lat = ExtraIntent.getString("Lat");
                        String Lng = ExtraIntent.getString("Lng");
                        String Location = ExtraIntent.getString("Location");
                        //Toast.makeText(getApplicationContext(),Location+"\n"+Lat+"\n"+Lng,Toast.LENGTH_SHORT).show();
                       if(FlagSource){
                           ((ShareVariable)getApplication()).setSt_latitude(Lat);
                           ((ShareVariable)getApplication()).setSt_longitude(Lng);
                       }else {
                           ((ShareVariable)getApplication()).setEnd_latitude(Lat);
                           ((ShareVariable)getApplication()).setEnd_longitude(Lng);
                       }
                    }else{
                        Toast.makeText(getApplicationContext(),"unknown error",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"unknown error",Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if(requestCode == 3){
            if(resultCode == Activity.RESULT_OK){
                Bundle ExtraIntent = getIntent().getExtras();
                System.out.print("CurrentList : --> "+currentList);
                if(ExtraIntent!=null) {
                    currentList= (ArrayList<String>) ExtraIntent.getStringArrayList("newList");
                    System.out.print(currentList);
                    if(FlagSource){
                        try {
                            adapter__Source = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,
                                    currentList);
                            textView__Source.setThreshold(1);
                            textView__Source.setAdapter(adapter__Source);
                            adapter__Source.notifyDataSetChanged();

                        }catch (NullPointerException e){
                            Log.i("currentList","Nullpointerexception");
                        }
                    }else{
                        try{
                            adapter__Destination = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,
                                    currentList);
                            textView__Destination.setThreshold(1);
                            textView__Destination.setAdapter(adapter__Destination);
                            adapter__Destination.notifyDataSetChanged();
                        }catch (NullPointerException e){
                            Log.i("currentList","Nullpointerexception");
                        }

                    }
                }else{
                    Log.i("ExtraIntent","Current List is empty(API_AutoComplete.java)");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
    private void insertToDatabaseLogin(){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                OnlineConnection="No Internet Connection";
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Verifying.....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            protected String doInBackground(String... params) {
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("Username", loguse1));
                param.add(new BasicNameValuePair("Password", logpass1));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://rameshchandra.net23.net/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(param));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    IAMREGISTEREDALREADY = EntityUtils.toString(entity);
                    OnlineConnection = IAMREGISTEREDALREADY;
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(OnlineConnection.equals("No Internet Connection") == true){
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else if(IAMREGISTEREDALREADY.charAt(0) =='0'){
                   Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                    ((ShareVariable) getApplication()).setUserName(loguse1);
                    String __name="";
                    for(int i=2;i<IAMREGISTEREDALREADY.length();i++){
                        if(IAMREGISTEREDALREADY.charAt(i)=='#' ||IAMREGISTEREDALREADY.charAt(i)=='<')break;
                        else __name+=IAMREGISTEREDALREADY.charAt(i);
                    }
                    if(__name != "") ((ShareVariable) getApplication()).setName(__name);
                    //Update user name
                    View headerLayout = navigationView.getHeaderView(0);
                    TextView useremail = (TextView) headerLayout.findViewById(R.id.textView_user_id);
                    if(useremail == null || ((ShareVariable) getApplication()).getUserName() == null){
                    } else useremail.setText(((ShareVariable) getApplication()).getName());
                    //Update user mail
                    TextView useremail2 = (TextView) headerLayout.findViewById(R.id.user_mail);
                    if(useremail2 == null || ((ShareVariable) getApplication()).getUserName() == null){
                    } else useremail2.setText(((ShareVariable) getApplication()).getUserName());
                } else {
                    Toast.makeText(getApplicationContext(),"username or password wrong",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_refresh:
                Toast.makeText(getApplicationContext(),"Refresh Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //if (id == R.id.nav_camera) {}  //else if (id == R.id.nav_gallery) {}
        //else if (id == R.id.nav_slideshow) {}//else if (id == R.id.nav_send) {}
         if (id == R.id.nav_share) {
             Toast.makeText(getApplicationContext(),"Share Clicked",Toast.LENGTH_SHORT).show();
             startActivity(new Intent(MainActivity.this,Share.class));
         } else if (id == R.id.nav_manage) {
             Toast.makeText(getApplicationContext(),"Tools Clicked",Toast.LENGTH_SHORT).show();
         } else if (id == R.id.nav_feedback) {
             Toast.makeText(getApplicationContext(),"Feedback Clicked",Toast.LENGTH_SHORT).show();
             startActivity(new Intent(MainActivity.this, SendFeedback.class));
         }   else if (id == R.id.nav_about_us) {
             Toast.makeText(getApplicationContext(),"About us Clicked",Toast.LENGTH_SHORT).show();
             startActivity(new Intent(MainActivity.this,AboutUs.class));
         } else if (id == R.id.nav_star) {
             Toast.makeText(getApplicationContext(),"Rate Clicked",Toast.LENGTH_SHORT).show();
             startActivity(new Intent(MainActivity.this,RateUs.class));
         }  else if (id == R.id.nav_home) {
             Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(MainActivity.this,MainActivity.class));
         }  else if (id == R.id.nav_profile) {
             //get username and password if user is already logged in.
             //and if not, then redirect to login activity
             String check_user_name = ((ShareVariable) this.getApplication()).getUserName();
             if (check_user_name != null) {
                 startActivity(new Intent(MainActivity.this, Profile.class));
             } else {
                 //user is not logged in.....redirect to login activity.

                 // Create Object of Dialog class
                 final Dialog login = new Dialog(MainActivity.this);
                 // Set GUI of login screen
                 login.setContentView(R.layout.login_dialog);
                 login.setTitle("Login to TransportMe");
                 // Init button of login GUI
                 Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
                 Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
                 Button btnSignUp = (Button) login.findViewById(R.id.btnSignup);
                 Button btnForgot =(Button)login.findViewById(R.id.btnForgotPassword);
                 txtUsername = (EditText) login.findViewById(R.id.txtUsername);
                 txtPassword = (EditText) login.findViewById(R.id.txtPassword);
                 // Attached listener for login GUI button
                 btnLogin.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (txtUsername.getText().toString().trim().length() > 0 &&
                                 txtPassword.getText().toString().trim().length() > 0) {
                             InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                             imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                             loguse1 = txtUsername.getText().toString();
                             logpass1 = txtPassword.getText().toString();
                             insertToDatabaseLogin();
                             login.dismiss();
                         } else {
                             Toast.makeText(MainActivity.this, "Please enter Username and Password Correctly",
                                     Toast.LENGTH_LONG).show();
                         }
                     }
                 });
                 btnCancel.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         login.dismiss();
                     }
                 });
                 btnSignUp.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         startActivity(new Intent(MainActivity.this, SignUp.class));
                     }
                 });
                 btnForgot.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         startActivity(new Intent(MainActivity.this,ForgotPassword.class));
                     }
                 });
                 // Make dialog box visible.
                 login.show();
             }
         }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
