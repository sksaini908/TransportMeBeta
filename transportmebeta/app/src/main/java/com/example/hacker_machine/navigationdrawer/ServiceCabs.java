package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 6/3/16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hacker-machine on 3/3/16.
 */
public class ServiceCabs extends AppCompatActivity{
    // Widget Button Declare
    Button btnLoginDialog2;
    String InternetAvailability="No Internet Connection",loguse2,logpass2,IAMREGISTEREDALREADY2="Failure",__source, __destination,
            __uberResponse="",__uberResponse2="",TAG = "MyAwesomeApp";
    ProgressDialog pDialog2;
    ArrayList<Integer> UberEstimatedTime;
    ArrayList<String>UbarCabsName,UbarCabsName2,UberEstimatedPrice,UberProductID,UberProductID2,CabName,ETA,Price,ProductId;
    ListView list,listLyft;
    Integer size =1;
    String start_latitude="37.625732",start_longitude="-122.377807",end_latitude="37.785114",end_longitude="-122.406677";
    int cablistSize=0;
    private String InternetAvailableFlag = "No Internet Connection";
    private String __LfytAccessResponse="";
    private String __LfytCostResponse="";
    private String __LyftETAResponse="";
    private String accessToken=null;
    private ArrayList<String> LyftCostList,LyftCostListName,LyftETAList,LyftProductID,LyftDisplayName;
    private Integer LyftListSize=0;
    private String[] LyftRideTypes={"lyft","lyft_line","lyft_plus"};
    private String[] LyftDispName={"Lyft","Lyft Line","Lyft Plus"};
    private static final String LYFT_PACKAGE = "me.lyft.android";
    private  String pickLat="12.980712";
    private String  pickLng="74.803145";
    private String dropLat="12.963455";
    private String dropLng="74.890387";
    private String ride_type="ridetype?";
    private String Ride_Type="lyft";
    int a=0,b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_cabs);
        __source = ((ShareVariable) getApplication()).getSource();//take source and destination
        __destination = ((ShareVariable) getApplication()).getDestination();
        start_latitude = ((ShareVariable) getApplication()).getSt_latitude();
        start_longitude = ((ShareVariable) getApplication()).getSt_longitude();
        end_latitude = ((ShareVariable) getApplication()).getEnd_latitude();
        end_longitude = ((ShareVariable) getApplication()).getEnd_longitude();
        Toast.makeText(getApplicationContext(),__source+"\n"+"Lat : "+ start_latitude+"\nLng : "+start_longitude+"\n"+
                        __destination+"\nLat : "+end_latitude+"\nLng : "+end_longitude, Toast.LENGTH_LONG).show();
        LyftCostList=new ArrayList<String >();LyftCostListName=new ArrayList<String>();
        LyftETAList=new ArrayList<String>(); LyftProductID=new ArrayList<String>(); LyftDisplayName=new ArrayList<String>();
        for(int i=0;i<LyftRideTypes.length;i++){
            LyftCostList.add("N/A"); LyftETAList.add("N/A");  LyftProductID.add("N/A");
            LyftCostListName.add(LyftRideTypes[i]);
            LyftDisplayName.add(LyftDispName[i]);
        }
        LyftCabAPI(); LyftCOST(); LyftETA();
        UberCabsETA();UberCabsPrice();
        // Init Widget Button and set click listener
        btnLoginDialog2 = (Button) findViewById(R.id.btnLoginDialog);//for login dialog
        btnLoginDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  if (v == btnLoginDialog2) LoginHelper(); }
        });
    }
    private void LoginHelper(){
        //get username and password if user is already logged in.
        //and if not, then redirect to login activity
        String check_user_name = ((ShareVariable) getApplication()).getUserName();
        if (check_user_name != null) {
            Toast.makeText(getApplicationContext(), "Already Logged In", Toast.LENGTH_SHORT).show();
        } else {
            // Create Object of Dialog class
            final Dialog login2 = new Dialog(ServiceCabs.this);
            // Set GUI of login screen
            login2.setContentView(R.layout.login_dialog);
            login2.setTitle("Login to TransportMe");
            // Init button of login GUI
            Button btnLogin = (Button) login2.findViewById(R.id.btnLogin);
            Button btnCancel = (Button) login2.findViewById(R.id.btnCancel);
            Button btnSignUP = (Button) login2.findViewById(R.id.btnSignup);
            Button btnForgot = (Button) login2.findViewById(R.id.btnForgotPassword);
            final EditText txtUsername2 = (EditText) login2.findViewById(R.id.txtUsername);
            final EditText txtPassword2 = (EditText) login2.findViewById(R.id.txtPassword);
            // Attached listener for login GUI button
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtUsername2.getText().toString().trim().length() > 0 &&
                            txtPassword2.getText().toString().trim().length() > 0) {
                        loguse2 = txtUsername2.getText().toString();
                        logpass2 = txtPassword2.getText().toString();
                        VerifyingLoginInfo();
                        login2.dismiss();
                    } else {
                        Toast.makeText(ServiceCabs.this,
                                "Please enter Username and Password", Toast.LENGTH_LONG).show();
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login2.dismiss();
                }
            });
            btnSignUP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ServiceCabs.this, SignUp.class));
                }
            });
            btnForgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ServiceCabs.this, ForgotPassword.class));
                }
            });
            // Make dialog box visible.
            login2.show();
        }

    }
    private String readInputStreamToString(HttpURLConnection connection) { //get response of http request
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.i(TAG, "Error reading InputStream");
            result = null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.i(TAG, "Error closing InputStream");
                }
            }
        }
        return result;
    }
    private void UberCabsETA(){//to get uber cab info in given source and destination
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailableFlag = "No Internet Connection";
            }
            protected String doInBackground(String... params) {
                try {

                    URL url = new URL("https://api.uber.com/v1/estimates/time?" +
                            "start_latitude=" + start_latitude +
                            "&start_longitude=" + start_longitude+
                            "&server_token=csBiRmlGA3NE1ztnaDn0H9kLA08iVKrpE3SVc4jj");
                    String accessToken = "csBiRmlGA3NE1ztnaDn0H9kLA08iVKrpE3SVc4jj";
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "Token token=" + accessToken);
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(15000);
                    int httpStatus = conn.getResponseCode();
                    Log.v("UberCabsETA", "httpStatus :" + httpStatus);
                    if (httpStatus == 200 ) {
                        __uberResponse = readInputStreamToString(conn);
                        InternetAvailableFlag = __uberResponse;
                        cablistSize = 1;
                    } else{
                        cablistSize =0;
                    }
                } catch (MalformedURLException me) {
                    Log.i("MalformedURLException","MalformedURLException");
                } catch (IOException e) {
                    Log.i("IOException","IOException");
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(cablistSize == 0){
                    Toast.makeText(getApplicationContext(),"Sorry,No Cab Available",Toast.LENGTH_SHORT).show();
                }else   if(InternetAvailableFlag !=null && InternetAvailableFlag.equals("No Internet Connection") ){
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else if(result!=null && result.equals("success") ){
                    a=0;
                    try {
                        Log.i("ETA __uberResponse",__uberResponse);
                        JSONObject jsonObject = new JSONObject(__uberResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("times");

                        UberEstimatedTime = new ArrayList<Integer>();
                        UberProductID = new ArrayList<String>();
                        UbarCabsName= new ArrayList<String>();
                        for(int i=0;i<jsonArray.length();++a,i++){
                            JSONObject  jsonObject1= jsonArray.getJSONObject(i);
                            int estimate = jsonObject1.getInt("estimate");
                            String CabName__ = jsonObject1.getString("display_name");
                            String product = jsonObject1.getString("product_id");
                            UberEstimatedTime.add(estimate/60);
                            UberProductID.add(product);
                            UbarCabsName.add(CabName__);
                        }
                    } catch (JSONException jsonexception){
                        populateUberCabsList(a,b);
                        Log.i("JSONException", "JSONException");
                    }
                } else {
                    Log.i("Request Error", "Error in Request");
                    Toast.makeText(getApplicationContext(),"unknown error, refresh",Toast.LENGTH_SHORT).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    private void UberCabsPrice(){//to get uber cab info in given source and destination
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailableFlag = "No Internet Connection";
            }
            protected String doInBackground(String... params) {
                try {
                    URL url2 = new URL("https://api.uber.com/v1/estimates/price?" +
                            "start_latitude=" + start_latitude +
                            "&start_longitude=" + start_longitude +
                            "&end_latitude=" + end_latitude +
                            "&end_longitude=" + end_longitude +
                            "&server_token=csBiRmlGA3NE1ztnaDn0H9kLA08iVKrpE3SVc4jj");
                    String accessToken2 = "csBiRmlGA3NE1ztnaDn0H9kLA08iVKrpE3SVc4jj";
                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                    //conn2.setRequestProperty("Content-Type", "application/json");
                    //conn2.setRequestProperty("Authorization", "Token token=" + accessToken2);
                    conn2.setRequestMethod("GET");
                    conn2.setConnectTimeout(15000);
                    int httpStatus2 = conn2.getResponseCode();
                    Log.v("UberCabsPrice", "httpStatus :" + httpStatus2);
                    ////__uberResponse2 = readInputStreamToString(conn2);
                    ///InternetAvailableFlag = readInputStreamToString(conn2);
                    __uberResponse2="";
                    if(httpStatus2 == 200){
                        __uberResponse2 = readInputStreamToString(conn2);
                        InternetAvailableFlag = __uberResponse2;
                    }
                } catch (MalformedURLException me) {
                    Log.i("MalformedURLException","MalformedURLException");
                } catch (IOException e) {
                    Log.i("IOException","IOException");
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(cablistSize == 0){
                    Toast.makeText(getApplicationContext(),"Sorry,No Cab Available",Toast.LENGTH_SHORT).show();
                }else   if(InternetAvailableFlag !=null && InternetAvailableFlag.equals("No Internet Connection") ){
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else if(result!=null && result.equals("success") ){
                    try {
                        if(__uberResponse2 == null) {
                            Toast.makeText(getApplicationContext(),"Distance is more than 100 kms",Toast.LENGTH_SHORT).show();
                        }
                        //System.out.println(__uberResponse2);
                        if(__uberResponse2 != null)  Log.i("PRICELIST __uber", __uberResponse2);
                        JSONObject jsonObject = new JSONObject(__uberResponse2);
                        JSONArray jsonArray = jsonObject.getJSONArray("prices");
                        UberEstimatedPrice = new ArrayList<String>();
                        UberProductID2 = new ArrayList<String>();
                        UbarCabsName2 = new ArrayList<String >();
                        b=0;
                        for(int i=0;i<jsonArray.length();i++,++b){
                            JSONObject  jsonObject1= jsonArray.getJSONObject(i);
                            String localized_display_Price = jsonObject1.getString("estimate");
                            String product = jsonObject1.getString("product_id");
                            String localized_display_name = jsonObject1.getString("localized_display_name");
                            UberEstimatedPrice.add(localized_display_Price);
                            UberProductID2.add(product);
                            UbarCabsName2.add(localized_display_name);
                        }
                        size = Math.max(a,b);
                        Log.i("Cabs List","Number of cabs : "+size);
                        Log.i("A , B ==> ",a+" "+b);

                        populateUberCabsList(a,b);
                    } catch (JSONException jsonexception){
                        populateUberCabsList(a,b);
                        pDialog2.dismiss();
                        Log.i("JSONException", "JSONException");
                    } finally {
                        pDialog2.dismiss();
                    }
                } else {
                    Log.i("Request Error", "Error in Request");
                    Toast.makeText(getApplicationContext(),"unknown error, refresh",Toast.LENGTH_SHORT).show();
                }
                pDialog2.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    private void populateUberCabsList(int a,int b) {
        CabName = new ArrayList<String>();
        ETA = new ArrayList<String>();
        Price = new ArrayList<String>();
        ProductId = new ArrayList<String>();
        try{
            if (a >= b) {
                for (int i = 0; i < a; i++) {
                    CabName.add(UbarCabsName.get(i));
                    Price.add("N/A");
                    ETA.add(Integer.toString(UberEstimatedTime.get(i)));
                    ProductId.add(UberProductID.get(i));
                }
                for (int i = 0; i < b; i++) {
                    for (int j = 0; j < a; j++) {
                        if (ProductId.get(j).equals(UberProductID2.get(i))) {
                            Price.set(j, UberEstimatedPrice.get(i));
                        }
                    }
                }
                for (int i = 0; i < a; i++)
                    System.out.printf("%s \t%s \t %s\t  %s\n", CabName.get(i), Price.get(i), ETA.get(i), ProductId.get(i));
            } else {
                for (int i = 0; i < b; i++) {
                    CabName.add(UbarCabsName2.get(i));
                    Price.add(UberEstimatedPrice.get(i));
                    ETA.add("N/A");
                    ProductId.add(UberProductID2.get(i));
                }
                for (int i = 0; i < a; i++) {
                    for (int j = 0; j < b; j++) {
                        if (ProductId.get(j).equals(UberProductID.get(i))) {
                            ETA.set(j, Integer.toString(UberEstimatedTime.get(i)));
                        }
                    }
                }
                for (int i = 0; i < b; i++)
                    System.out.printf("%s \t%s \t %s\t  %s\n", CabName.get(i), Price.get(i), ETA.get(i), ProductId.get(i));
            }
         } catch ( ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        } catch ( NullPointerException e){
            e.printStackTrace();
        }
        CabsList adapter = new CabsList(ServiceCabs.this, CabName, ETA, Price, ProductId);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(), "List Clicked" + position+" "+__source+" "+__destination,
                //        Toast.LENGTH_SHORT).show();
                Log.i("Cab List Clicked", Integer.toString(position));

                Intent i = new Intent(getApplicationContext(), API_Uber.class);
                String k = (String) parent.getItemAtPosition(position);
                i.putExtra("UBERX_PRODUCT_ID", k);
                i.putExtra("PICKUP_LAT", start_latitude);
                i.putExtra("PICKUP_LONG", start_longitude);
                i.putExtra("PICKUP_NICK", __source);
                i.putExtra("PICKUP_ADDR", __source);
                i.putExtra("DROPOFF_LAT", end_latitude);
                i.putExtra("DROPOFF_LONG", end_longitude);
                i.putExtra("DROPOFF_NICK", __destination);
                i.putExtra("DROPOFF_ADDR", __destination);

                startActivity(i);
            }
        });

    }
    private void VerifyingLoginInfo(){ //login user in transport me
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailability="No Internet Connection";
                pDialog2 = new ProgressDialog(ServiceCabs.this);
                pDialog2.setMessage("Verifying.....");
                pDialog2.setIndeterminate(false);
                pDialog2.setCancelable(true);
                pDialog2.show();
            }
            protected String doInBackground(String... params) {

                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("Username", loguse2));
                param.add(new BasicNameValuePair("Password", logpass2));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://rameshchandra.net23.net/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(param));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity2 = response.getEntity();
                    String responseStr2 = EntityUtils.toString(entity2);
                    IAMREGISTEREDALREADY2 = responseStr2;
                    InternetAvailability=responseStr2;
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(InternetAvailability != null && InternetAvailability == "No Internet Connection" ){
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else if(IAMREGISTEREDALREADY2.charAt(0) =='0'){
                    Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                    ((ShareVariable) getApplication()).setUserName(loguse2);
                    String __name="";
                    for(int i=2;i<IAMREGISTEREDALREADY2.length();i++){
                        if(IAMREGISTEREDALREADY2.charAt(i)=='#' ||IAMREGISTEREDALREADY2.charAt(i)=='<')break;
                        else __name+=IAMREGISTEREDALREADY2.charAt(i);
                    }
                    if(__name != "") ((ShareVariable) getApplication()).setName(__name);

                } else {
                    Toast.makeText(getApplicationContext(),"username or password wrong",Toast.LENGTH_SHORT).show();
                }
                pDialog2.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    private void LyftETA(){//to get uber cab info in given source and destination
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InternetAvailableFlag = "No Internet Connection";
            }
            @Override
            protected  String doInBackground(String...params) {
                try {
                    /************** For getting response from HTTP URL start ***************/
                    String url ="https://api.lyft.com/v1/eta?lat=" +
                            start_latitude + "&lng=" + start_longitude;
                    //String url="https://api.lyft.com/v1/eta?lat=37.7833&lng=-122.4167";
                    URL object = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) object.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    Log.v("LyftETA", "httpStatus :" + responseCode);
                    if (responseCode == 200) {
                        InputStream inputStr = connection.getInputStream();
                        String encoding = connection.getContentEncoding() == null ? "UTF-8"
                                : connection.getContentEncoding();
                        __LyftETAResponse = org.apache.commons.io.IOUtils.toString(inputStr, encoding);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.i("__LfytETAResponse", __LyftETAResponse);
                try {
                    JSONObject jsonObject = new JSONObject(__LyftETAResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("eta_estimates");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String display_name = jsonObject1.getString("ride_type");
                        Integer eta_seconds = jsonObject1.getInt("eta_seconds") ;
                        Integer eta2 =( (eta_seconds*100)/60 );
                        Double eta= eta2.doubleValue()/ 100;
                        for(int j=0;j<LyftCostListName.size();j++){
                            if(LyftCostListName.get(j).equals(display_name)){
                                LyftETAList.set(j,Double.toString(eta));
                            }
                        }
                        System.out.println(eta_seconds + "  ==> " + eta);
                        //Toast.makeText(getApplicationContext(),display_name+"  "+ Double.toString(eta), Toast.LENGTH_SHORT).show();
                    }
                    CabsList adapterLyft = new CabsList(ServiceCabs.this,LyftDisplayName, LyftETAList,LyftCostList , LyftProductID);
                    listLyft = (ListView) findViewById(R.id.listLyft);
                    listLyft.setAdapter(adapterLyft);
                    adapterLyft.notifyDataSetChanged();
                    listLyft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("LYft Cab List Clicked", Integer.toString(position));
                            Toast.makeText(getApplicationContext(),"LYft Cab List Clicked"+Integer.toString(position),Toast.LENGTH_SHORT).show();
                            Ride_Type=LyftCostListName.get(position);
                            deepLinkIntoLyft();
                        }
                    });
                    Toast.makeText(getApplicationContext(),LyftCostListName.get(0)+"    "+LyftETAList.get(0)+"    "+LyftCostList.get(0)+"\n"
                            +LyftCostListName.get(1)+"    "+LyftETAList.get(1)+"    "+LyftCostList.get(1)+"\n"
                            +LyftCostListName.get(2)+"    "+LyftETAList.get(2)+"    "+LyftCostList.get(2)+"\n",Toast.LENGTH_SHORT).show();

                } catch ( JSONException e){
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
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
            }
            @Override
            protected  String doInBackground(String...params) {
                try {
                    /************** For getting response from HTTP URL start ***************/

                    String url ="https://api.lyft.com/v1/cost?start_lat=" +
                            start_latitude + "&start_lng=" +
                            start_longitude + "&end_lat=" +
                            end_latitude+ "&end_lng=" +
                            end_longitude;

                    //String url="https://api.lyft.com/v1/eta?lat=37.7833&lng=-122.4167";
                    URL object = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) object.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer "+accessToken);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    Log.v("LyftCOST", "httpStatus :" + responseCode);
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
                Log.i("__LfytCostResponse", __LfytCostResponse);
                try{
                    JSONObject jsonObject = new JSONObject(__LfytCostResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("cost_estimates");

                    for(int i=0;i<jsonArray.length();++LyftListSize,i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String display_name = jsonObject1.getString("ride_type");
                        String currency = "$";//jsonObject1.getString("currency");
                        Integer __min = jsonObject1.getInt("estimated_cost_cents_min") ;
                        Integer __max =  jsonObject1.getInt("estimated_cost_cents_max") ;
                        Double __m = __min.doubleValue()/100;
                        Double __M = __max.doubleValue()/100;
                        for (int j=0;j<LyftCostListName.size();j++){
                            if(LyftCostListName.get(j).equals(display_name)){
                                LyftCostList.set(j,currency+Double.toString(__m)+"-"+Double.toString(__M));
                            }
                        }
                        System.out.println(__min + "  ==> " + __m + "," + __max + "  ==> " + __M);
                        // Toast.makeText(getApplicationContext(),display_name+"\n"+currency+" : "+ Double.toString(__m)+"-"+Double.toString(__M), Toast.LENGTH_SHORT).show();
                    }
                  /* Toast.makeText(getApplicationContext(),LyftCostListName[0]+"      "+LyftCostList[0]+"\n"  +LyftCostListName[1]+"        "+LyftCostList[1]+"\n" +LyftCostListName[2]+"        "+LyftCostList[2]+"\n",Toast.LENGTH_SHORT).show();*/
                } catch ( JSONException e){
                    e.printStackTrace();
                } catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"No Lyft Cab",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
                pDialog2 = new ProgressDialog(ServiceCabs.this);
                pDialog2.setMessage("Searching Cabs.....");
                pDialog2.setIndeterminate(false);
                pDialog2.setCancelable(true);
                pDialog2.show();
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
                    Log.v("LyftAccess", "httpStatus :" + responseCode);
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

                Log.i(" __LfytAccessResponse", __LfytAccessResponse);
                try {
                    JSONObject jsonObject = new JSONObject(__LfytAccessResponse);
                    accessToken = jsonObject.getString("access_token");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    private void deepLinkIntoLyft() {
        if (isPackageInstalled(this, LYFT_PACKAGE)) {
            pickLat= ((ShareVariable)getApplication()).getSt_latitude();
            pickLng=((ShareVariable)getApplication()).getSt_longitude();
            dropLat=((ShareVariable)getApplication()).getEnd_latitude();
            dropLng= ((ShareVariable)getApplication()).getEnd_longitude();
            Toast.makeText(getApplicationContext(), "pickLat : "+pickLat+"\n"+"pickLng : "+pickLng+"\n"+
                    "dropLat : "+dropLat+"\n"+"dropLng : "+dropLng, Toast.LENGTH_SHORT).show();
            String link="lyft://ridetype?id="+Ride_Type +"&pickup[latitude]=" + start_latitude+"&pickup[longitude]=" +
                    start_longitude+"&destination[latitude]=" + end_latitude +"&destination[longitude]=" +end_longitude;
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
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu; this adds items to the action barr if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
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
                LyftCabAPI();LyftCOST(); LyftETA();
                UberCabsETA();UberCabsPrice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
