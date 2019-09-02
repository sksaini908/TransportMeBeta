package com.example.hacker_machine.navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by hacker-machine on 30/3/16.
 */
public class API_GeoCoOrdinate extends AppCompatActivity{

    private String Location="";
    private String URL="";
    private String InternetAvailability="No Internet Connection";
    private String ResultSource="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            Bundle ExtraIntent = getIntent().getExtras();
            Location=ExtraIntent.getString("Location");
            Location = Location.replaceAll(" ", "%20");
            URL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + Location + "&sensor=false";
            getGeoLocationLatLng();
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Location : "+Location,Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","NotCapturedLatLng");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
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

                            Toast.makeText(getApplicationContext(), NameHelper + "\nLatitude : " + latitudeHelper +
                                        "\nLongitude : " + longitudrHelper+"\n", Toast.LENGTH_SHORT).show();
                            if(NameHelper.equals(Location)){
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result","CapturedLatLng");
                                returnIntent.putExtra("Location",NameHelper);
                                returnIntent.putExtra("Lat", latitudeHelper);
                                returnIntent.putExtra("Lng", longitudrHelper);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result","CapturedLatLng");
                        returnIntent.putExtra("Location",NameHelper);
                        returnIntent.putExtra("Lat", latitudeHelper);
                        returnIntent.putExtra("Lng", longitudrHelper);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } catch ( JSONException e){
                        Log.i("JASONArray EXEPTION", "Error in parsing");
                        Toast.makeText(getApplicationContext(),"Unknown Error, Try Again", Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result","NotCapturedLatLng");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","NotCapturedLatLng");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
}
