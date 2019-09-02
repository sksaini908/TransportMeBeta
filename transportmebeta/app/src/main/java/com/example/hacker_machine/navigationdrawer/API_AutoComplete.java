package com.example.hacker_machine.navigationdrawer;

import android.os.AsyncTask;
import android.util.Log;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by hacker-machine on 30/3/16.
 */
public class API_AutoComplete  {
    private String Location="NoUpdated";
    private String URL="";
    private String __Result__Source="";
    private ArrayList<String> prediction__Source;

    public API_AutoComplete(String Location)
    {
        this.Location=Location;
        prediction__Source = new ArrayList<String>();
        prediction__Source.add("Select Place");
    }
    public ArrayList<String> UpdatePredictionnewList(){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, ArrayList<String> > {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            protected ArrayList<String> doInBackground(String... params) {

                // Do anything else you need to with the returned list here
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    String Place__Source=Location.replaceAll(" ", "%20");
                    URL= "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                            Place__Source + "&sensor=true&key=" + "AIzaSyCBh-dxTcC2v_zG7aPIcNdamwC4w1EIPJ8";
                    HttpPost httpPost = new HttpPost(URL);
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    String responseStr = EntityUtils.toString(entity);
                    __Result__Source = responseStr;
                } catch (UnsupportedEncodingException e) {
                    Log.i("UnsupportedEncodingExc", "UnsupportedEncodingException");
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

                try {
                    JSONObject mainobj= new JSONObject(__Result__Source);
                    JSONArray jsonArray=mainobj.getJSONArray("predictions");
                    if(jsonArray.length() == 0){
                        prediction__Source.add(Location);
                        System.out.print(prediction__Source);
                    }else{
                        System.out.printf("\n******************************************************\n");
                        prediction__Source = new ArrayList<String>();
                        prediction__Source.add("Choose place");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String plac = obj.getString("description");
                            prediction__Source.add(plac);
                            System.out.print(plac);System.out.println();
                        }
                        System.out.printf("******************************************************\n\n");
                    }
                    System.out.print(prediction__Source);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return prediction__Source;
            }
/*            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
            */
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
        return prediction__Source;
    }

}
