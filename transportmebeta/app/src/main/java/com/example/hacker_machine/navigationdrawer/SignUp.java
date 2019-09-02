package com.example.hacker_machine.navigationdrawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;

/**
 * Created by hacker-machine on 7/3/16.
 * SignUp Activity..with plenty of DB interaction
 */

public class SignUp extends AppCompatActivity{
    ProgressDialog pDialog=null;
    String RegName,Reguser;
    EditText txtName,txtUser;
    String ReqRes="No Internet Connection";
    String VerificationCode;
    String Username=null;
    Session session = null;
    ProgressDialog pdialog = null;
    String rec,subject,textMessage;
    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        API_RandomNumber number = new API_RandomNumber();
        VerificationCode=number.getRandomNumber();
        setContentView(R.layout.signup);
        final Button REGISTER = (Button) findViewById(R.id.buttonRegister);
        REGISTER.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtName   =   (EditText) findViewById(R.id.SignupName);
                txtUser   =   (EditText) findViewById(R.id.SignupUser);

                RegName=txtName.getText().toString();
                Reguser = txtUser.getText().toString();

                //To hide soft keyboard after submit button is clicked
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                if(RegName.isEmpty() == true){
                    Toast.makeText(getApplicationContext(), "Name Empty",Toast.LENGTH_SHORT).show() ;
                }else if(Reguser.isEmpty() == true){
                    Toast.makeText(getApplicationContext(), "Email Empty",Toast.LENGTH_SHORT).show();
                }
                else insertToDatabaseRegistration();
            }
        });
    }

    private void insertToDatabaseRegistration(){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override

            protected void onPreExecute() {
                super.onPreExecute();
                ReqRes="No Internet Connection";
                pDialog = new ProgressDialog(SignUp.this);
                pDialog.setMessage("Veryfying.....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            protected String doInBackground(String... params) {

                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("Username",Reguser));
                param.add(new BasicNameValuePair("Name",RegName));
                param.add(new BasicNameValuePair("Code",VerificationCode));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://rameshchandra.net23.net/fetch.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(param));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();
                    String responseStr = EntityUtils.toString(entity);
                    ReqRes = responseStr;
                   ///Toast.makeText(getApplicationContext(),responseStr,Toast.LENGTH_SHORT).show();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (ReqRes.equals("No Internet Connection") == true){
                    Toast.makeText(getApplicationContext(),ReqRes,Toast.LENGTH_SHORT).show();
                }else if(ReqRes.charAt(0) =='0'){
                    Toast.makeText(getApplicationContext(),"Email Already Registered ", Toast.LENGTH_SHORT).show();
                } else if(ReqRes.charAt(0) == '1'){
                    Toast.makeText(getApplicationContext(),"Registered Successfully", Toast.LENGTH_SHORT).show();
                 //read hostname and hostpass from database
                    String hostname1="",hostpass1="";
                    int i=3;
                    for(;i<ReqRes.length();){
                        if(ReqRes.charAt(i)==' ') break;
                        else {hostname1+=ReqRes.charAt(i);i+=2;}
                    }
                    for(i+=2;i<ReqRes.length();){
                        if(ReqRes.charAt(i)==' ') break;
                        else {hostpass1+=ReqRes.charAt(i);i+=2;}
                    }

                    Toast.makeText(getApplicationContext(),"Sending Message",Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    subject="TransportMe Verification Code";
                    Intent __i = new Intent(SignUp.this, API_SendMail.class);
                    startActivityForResult(__i, 1);
                    __i.putExtra("hostname", hostname1);
                    __i.putExtra("hostpass", hostpass1);
                    __i.putExtra("textMessage", textMessage);
                    __i.putExtra("rec", Reguser);
                    __i.putExtra("subject", subject);


                } else {
                    Toast.makeText(getApplicationContext(),"Unknown Error, Try Again"
                            +ReqRes, Toast.LENGTH_SHORT).show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }@Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                if(result.equals("Sent") == true){
                    startActivity(new Intent(SignUp.this,EmailVerification.class));
                }else{
                    Toast.makeText(getApplicationContext(),"unknown error",Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}

