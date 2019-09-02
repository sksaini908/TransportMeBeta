package com.example.hacker_machine.navigationdrawer;

/**
 * Created by hacker-machine on 5/4/16.
 */

/**
 * Created by hacker-machine on 23/3/16.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class ImageListView extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    public static final String GET_IMAGE_URL="http://rameshchandra.net23.net/PhotoUpload/getAllImage.php";
    public GetAlImages getAlImages;
    public static final String BITMAP_ID = "BITMAP_ID";
    public String _user_="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_view);
       _user_ = ((ShareVariable)getApplication()).getUserName();
        try {
            listView = (ListView) findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        getURLs();
    }
    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(ImageListView.this,"Downloading Profile image...","Please wait...",false,false);
                loading.setCancelable(true);
            }
            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                //Toast.makeText(ImageListView.this,"Success",Toast.LENGTH_LONG).show();
                CustomList2 customList2 = new CustomList2(ImageListView.this,GetAlImages.imageURLs,GetAlImages.bitmaps);

                listView.setAdapter(customList2);

                /*Intent intent = new Intent(ImageListView.this, ViewFullImage.class);
                intent.putExtra(BITMAP_ID,0);
                startActivity(intent);*/
            }
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAlImages.getAllImages();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }
    private void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageListView.this,"Loading...","Please Wait...",true,true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getAlImages = new GetAlImages(s,_user_);
                getImages();
            }
            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ViewFullImage.class);
        intent.putExtra(BITMAP_ID,0);
        startActivity(intent);
    }
}