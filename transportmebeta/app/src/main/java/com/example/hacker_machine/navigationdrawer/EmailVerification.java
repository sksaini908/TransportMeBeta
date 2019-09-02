package com.example.hacker_machine.navigationdrawer;

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

/**
 * Created by hacker-machine on 12/3/16.
 */
public class EmailVerification extends AppCompatActivity {
   String ReqRes="No Internet Connection",Email,Verification="",flush="Flush",Pass,Repass;
    ProgressDialog pDialog;
    Integer Attempts=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification);
        Attempts=3;
        API_RandomNumber number = new API_RandomNumber();
        flush=number.getRandomNumber();

        Button buttonSubmit =(Button) findViewById(R.id.buttonSubmitVerification);//to submit verification code
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputToSubmitButton();
            }
        });
        Button button2 = (Button) findViewById(R.id.buttonResendVerificationCode);//go to get new verification code
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailVerification.this,ForgotPassword.class));
            }
        });
    }
    private void getInputToSubmitButton(){                 //reading input from user to submit to email verification
        EditText txtMail = (EditText) findViewById(R.id.textViewEmailVerification);
        EditText txtVeri = (EditText) findViewById(R.id.textViewVerificationCode);
        EditText txtPass = (EditText) findViewById(R.id.textViewPassword);
        EditText txtReps = (EditText) findViewById(R.id.textViewRePassword);


        Email  = txtMail.getText().toString();
        Verification = txtVeri.getText().toString();
        Pass = txtPass.getText().toString();
        Repass = txtReps.getText().toString();

        //To hide soft keyboard after submit button is clicked
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //checking for validation of input
        if (Email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email Empty", Toast.LENGTH_SHORT).show();
        } else if (Verification.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Verificatin Code Empty", Toast.LENGTH_SHORT).show();
        } else if (Pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password Empty", Toast.LENGTH_SHORT).show();
        } else if (Repass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Re-password Empty", Toast.LENGTH_SHORT).show();
        } else if (Pass.equals(Repass) == false) {
            Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
        } else {
            sendSetPassword();
        }
    }
    private void sendSetPassword(){               //if verification code is right, set new password for user
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                ReqRes="No Internet Connection";//for checking internet connectivity purpose
                super.onPreExecute();
                pDialog = new ProgressDialog(EmailVerification.this);//dialog to main screen while executing background process
                pDialog.setMessage("Setting New Password.....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            protected String doInBackground(String... params) {

                List<NameValuePair> param = new ArrayList<NameValuePair>();//to add required parameter while make http request
                param.add(new BasicNameValuePair("Username", Email));
                param.add(new BasicNameValuePair("Verification",Verification));
                param.add(new BasicNameValuePair("Password",Pass));

                param.add(new BasicNameValuePair("Flush",flush));//flush old verification password..to be secure

                try {//actual http request excution
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://rameshchandra.net23.net/EmailVerification.php");
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
            protected void onPostExecute(String result) {//after execution of background process of http request for verificaion
                super.onPostExecute(result);
                if(ReqRes.equals("No Internet Connection") == true){
                    Toast.makeText(getApplicationContext(),ReqRes,Toast.LENGTH_SHORT).show();
                }else if(ReqRes.charAt(0)=='0'){
                    Toast.makeText(getApplicationContext(),"New Password Set",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EmailVerification.this, MainActivity.class));
                }else if(ReqRes.charAt(0)=='1'){
                    Toast.makeText(getApplicationContext(),"New Password Not Set",Toast.LENGTH_SHORT).show();
                }  else if(ReqRes.charAt(0)=='2') {
                    Attempts--;
                    if(Attempts > 0) {
                        Toast.makeText(getApplicationContext(), "Verification Code Not Matched"+
                                Integer.toString(Attempts)+"More Attempts", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Resend Verification Code",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EmailVerification.this,ForgotPassword.class));
                    }
                }else if(ReqRes.charAt(0)=='3'){
                    Toast.makeText(getApplicationContext(),"Email Not Registered",Toast.LENGTH_SHORT).show();
                }else if(ReqRes.charAt(0)=='4'){
                    Toast.makeText(getApplicationContext(),"Verification Code Not Sent",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Unknown Error",Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
}
