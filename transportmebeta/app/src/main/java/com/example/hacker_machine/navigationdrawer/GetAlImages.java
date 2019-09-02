package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 5/4/16.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hacker-machine on 23/3/16.
 */
public class GetAlImages  {

    public static String[] imageURLs;
    public static String[] usernames;
    public static Bitmap[] bitmaps;
    public static final String JSON_ARRAY="result";
    public static final String IMAGE_URL = "url";
    public static final String  USER_NAME = "username";
    private String json;
    private JSONArray urls;
    public String __user__ ="";
    public GetAlImages(String json,String user){
        this.__user__ = user;
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(json);
            urls = jsonObject.getJSONArray(JSON_ARRAY);
            Log.i("urls","JSON url response");
            System.out.print(urls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(IMAGE_URL));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Log.i("Image Url",jo.getString(IMAGE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void getAllImages() throws JSONException {
        bitmaps = new Bitmap[1];
        imageURLs = new String[1];
        usernames = new String[1];
        int j=0;
        for(int i=0;i<urls.length();i++){
            usernames[0]=urls.getJSONObject(i).getString(USER_NAME);
            if(__user__.equals(usernames[0])){
                imageURLs[0] = urls.getJSONObject(i).getString(IMAGE_URL);
                Log.i("urls", imageURLs[0]);
                JSONObject jsonObject = urls.getJSONObject(i);
                bitmaps[0]=getImage(jsonObject);
            }
        }
    }
}