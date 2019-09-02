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

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Created by hacker-machine on 12/3/16.
 */
public class ForgotPassword extends AppCompatActivity {
    ProgressDialog pDialog;
    String Username=null;
    String ReqRes="No Internet Connection";
    String VerificationCode="Hello123";
    Session session = null;
    ProgressDialog pdialog = null;
    String rec,subject,textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        API_RandomNumber number = new API_RandomNumber();
        VerificationCode=number.getRandomNumber();

        Button button= (Button) findViewById(R.id.buttonSubmitMail);//submit email to get verification code for getting new pass
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtMail =(EditText)findViewById(R.id.textViewEmailVerified);
                Username = txtMail.getText().toString();
                //To hide soft keyboard after submit button is clicked
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if(Username.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Email Empty",Toast.LENGTH_SHORT).show();
                } else{
                    insertToDatabaseLogin();
                }
            }
        });
        Button buttonReg = (Button) findViewById(R.id.buttonSubmitRegister);//for signup
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Click Navigation Drawer, Goto Profile",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
            }
        });
    }
    class RetrieveFeedTask extends AsyncTask<String ,Void,String> {

        @Override
        protected String doInBackground(String... params){
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("rameshchandranitk2017@gmail.com"));
                message.setRecipients(RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        protected void onPostExecute(String result){
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ForgotPassword.this,EmailVerification.class));
        }
    }
    private void insertToDatabaseLogin(){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ReqRes="No Internet Connection";
                pDialog = new ProgressDialog(ForgotPassword.this);
                pDialog.setMessage("Verifying.....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            protected String doInBackground(String... params) {

                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("Username", Username));
                param.add(new BasicNameValuePair("VerificationCode", VerificationCode));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://rameshchandra.net23.net/forgotPassword.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(param));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    ReqRes = EntityUtils.toString(entity);
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
                return "success";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                    if(ReqRes.equals("No Internet Connection") == true){
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }else  if(ReqRes.charAt(0) =='0'){
                    String hostname1="",hostpass1="";
                    //hostpass and hostname extraction
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
                        textMessage=VerificationCode;
                        subject="TransportMe Verification Code";

                        Intent __i = new Intent(ForgotPassword.this, API_SendMail.class);
                        startActivityForResult(__i, 1);

                        __i.putExtra("hostname", hostname1);
                        __i.putExtra("hostpass", hostpass1);
                        __i.putExtra("textMessage", textMessage);
                        __i.putExtra("rec", Username);
                        __i.putExtra("subject", subject);


                    }else if(ReqRes.charAt(0) =='2'){
                    Toast.makeText(getApplicationContext(),"Email Not Registered",Toast.LENGTH_SHORT).show();
                }  else if(ReqRes.charAt(0) =='3'){
                    Toast.makeText(getApplicationContext(),"Verification Code Not Updated",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"unknown error",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                if(result.equals("Sent") == true){
                    startActivity(new Intent(ForgotPassword.this,EmailVerification.class));
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
