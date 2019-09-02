package com.example.hacker_machine.navigationdrawer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hacker-machine on 11/4/16.
 */

public class LyftAPIHandler extends AppCompatActivity {

    private String InternetAvailableFlag = "No Internet Connection";
    private String __LfytAccessResponse="";
    private String __LfytCostResponse="";
    private static String pickLat,pickLng,dropLat,dropLng;
    private String accessToken=null;
    private String [] LyftCostList,LyftCostListName,LyftETAList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_cabs);
        /*Button button = (Button) findViewById(R.id.lyftApiCall);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LyftAppTrigger.class);
                // pickLat="37.764728"; pickLng="-122.422999";dropLat="37.7763592";  dropLng="-122.4242038";
                intent.putExtra("pickLat","37.764728");
                intent.putExtra("pickLng","-122.422999");
                intent.putExtra("dropLat","37.7763592");
                intent.putExtra("dropLng","-122.4242038");
                startActivity(intent);
            }
        });
        */
                LyftCabAPI();
                LyftCOST();
                LyftETA();
    }
    private void LyftETA(){//to get uber cab info in given source and destination
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailableFlag = "No Internet Connection";
                /*pDialog2 = new ProgressDialog(LyftAPIHandler.this);
                pDialog2.setMessage("Getting Cabs ETA List.....");
                pDialog2.setIndeterminate(false);
                pDialog2.setCancelable(true);
                pDialog2.show();*/
            }
            @Override
            protected  String doInBackground(String...params) {
                try {
                    /************** For getting response from HTTP URL start ***************/
                    String url ="https://api.lyft.com/v1/eta?lat=" +
                            "37.7772" + "&lng=" +
                            "-122.4233";
                    //String url="https://api.lyft.com/v1/eta?lat=37.7833&lng=-122.4167";
                    URL object = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) object.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer "+accessToken);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStr = connection.getInputStream();
                        String encoding = connection.getContentEncoding() == null ? "UTF-8"
                                : connection.getContentEncoding();
                        __LfytCostResponse = org.apache.commons.io.IOUtils.toString(inputStr, encoding);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                System.out.print(__LfytCostResponse);
                try
                {
                    JSONObject jsonObject = new JSONObject(__LfytCostResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("eta_estimates");
                    LyftETAList=new String[3];
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String display_name = jsonObject1.getString("ride_type");
                        Integer eta_seconds = jsonObject1.getInt("eta_seconds") ;
                        Double eta= eta_seconds.doubleValue()/ 60;
                        for(int j=0;j<3;j++){
                            if(display_name.equals(LyftCostListName[j])){
                                LyftETAList[j]=Double.toString(eta);
                            }
                        }
                        System.out.println(eta_seconds + "  ==> " + eta);
                        //Toast.makeText(getApplicationContext(),display_name+"  "+ Double.toString(eta), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(),LyftCostListName[0]+"    "+LyftETAList[0]+"    "+LyftCostList[0]+"\n"
                            +LyftCostListName[1]+"    "+LyftETAList[1]+"    "+LyftCostList[1]+"\n"
                            +LyftCostListName[2]+"    "+LyftETAList[2]+"    "+LyftCostList[2]+"\n",Toast.LENGTH_SHORT).show();
                } catch ( JSONException e){
                    e.printStackTrace();
                }
//                pDialog2.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    private void LyftCOST(){//to get uber cab info in given source and destination
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailableFlag = "No Internet Connection";
                /*pDialog2 = new ProgressDialog(LyftAPIHandler.this);
                pDialog2.setMessage("Getting Cost List.....");
                pDialog2.setIndeterminate(false);
                pDialog2.setCancelable(true);
                pDialog2.show();*/
            }
            @Override
            protected  String doInBackground(String...params) {
                try {
                    /************** For getting response from HTTP URL start ***************/

                    String url ="https://api.lyft.com/v1/cost?start_lat=" +
                            "37.7772" + "&start_lng=" +
                            "-122.4233" + "&end_lat=" +
                            "37.7972" + "&end_lng=" +
                            "-122.4533";
                    //String url="https://api.lyft.com/v1/eta?lat=37.7833&lng=-122.4167";
                    URL object = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) object.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer "+accessToken);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStr = connection.getInputStream();
                        String encoding = connection.getContentEncoding() == null ? "UTF-8"
                                : connection.getContentEncoding();
                        __LfytCostResponse = IOUtils.toString(inputStr, encoding);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                System.out.print(__LfytCostResponse);
                try
                {
                    JSONObject jsonObject = new JSONObject(__LfytCostResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("cost_estimates");
                    LyftCostList = new String[3];
                    LyftCostListName = new String[3];
                    LyftETAList=new String[3];
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String display_name = jsonObject1.getString("ride_type");
                        String currency = jsonObject1.getString("currency");
                        Integer __min = jsonObject1.getInt("estimated_cost_cents_min") ;
                        Integer __max =  jsonObject1.getInt("estimated_cost_cents_max") ;
                        Double __m = __min.doubleValue()/100;
                        Double __M = __max.doubleValue()/100;
                        LyftCostList[i]=currency+" "+Double.toString(__m)+"-"+Double.toString(__M);
                        LyftCostListName[i]=display_name;
                        LyftETAList[i]="N/A";
                        System.out.println(__min + "  ==> " + __m + "," + __max + "  ==> " + __M);
                        // Toast.makeText(getApplicationContext(),display_name+"\n"+currency+" : "+ Double.toString(__m)+"-"+Double.toString(__M), Toast.LENGTH_SHORT).show();
                    }
                  /* Toast.makeText(getApplicationContext(),LyftCostListName[0]+"      "+LyftCostList[0]+"\n"
                           +LyftCostListName[1]+"        "+LyftCostList[1]+"\n"
                           +LyftCostListName[2]+"        "+LyftCostList[2]+"\n",Toast.LENGTH_SHORT).show();*/
                } catch ( JSONException e){
                    e.printStackTrace();
                }
                //pDialog2.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    private void LyftCabAPI(){//to get uber cab info in given source and destination
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailableFlag = "No Internet Connection";
                /*pDialog2 = new ProgressDialog(LyftAPIHandler.this);
                pDialog2.setMessage("Getting access_token.....");
                pDialog2.setIndeterminate(false);
                pDialog2.setCancelable(true);
                pDialog2.show();
                */
            }
            @Override
            protected  String doInBackground(String...params) {
                try {
                    /************** For getting response from HTTP URL start ***************/
                    String url="https://api.lyft.com/oauth/token";
                    URL object = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) object.openConnection();
                    connection.setReadTimeout(60 * 1000);
                    connection.setConnectTimeout(60 * 1000);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    String authorization="i8VynEvD81eC"+":"+"uGNgniP-9NGGvkIH8nXileg4TGymi8_y";
                    byte[] __data = authorization.getBytes("UTF-8");
                    String base64 = Base64.encodeToString(__data, Base64.DEFAULT);
                    String encodedAuth="Basic "+base64;
                    connection.setRequestProperty("Authorization", encodedAuth);

                    String data =  "{\"grant_type\": \"client_credentials\", \"scope\": \"public\"}";
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(data);
                    out.close();

                    connection.setRequestMethod("POST");
                    int responseCode = connection.getResponseCode();
                    //String responseMsg = connection.getResponseMessage();
                    //__LfytAccessResponse=responseMsg;
                    if (responseCode == 200) {
                        InputStream inputStr = connection.getInputStream();
                        String encoding = connection.getContentEncoding() == null ? "UTF-8"
                                : connection.getContentEncoding();
                        __LfytAccessResponse = IOUtils.toString(inputStr, encoding);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                System.out.print(__LfytAccessResponse);
                try {
                    JSONObject jsonObject = new JSONObject(__LfytAccessResponse);
                    accessToken = jsonObject.getString("access_token");
                } catch (JSONException e){
                    e.printStackTrace();
                }
                // Toast.makeText(getApplicationContext(), "status ->"+status, Toast.LENGTH_LONG).show();
                //pDialog2.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
}

