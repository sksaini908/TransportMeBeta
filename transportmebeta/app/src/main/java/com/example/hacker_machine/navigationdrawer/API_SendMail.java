package com.example.hacker_machine.navigationdrawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by hacker-machine on 30/3/16.
 */
public class API_SendMail extends AppCompatActivity {

    private Session session;
    private ProgressDialog __pdialog;
    private String hostname;
    private String hostpass;
    private String textMessage;
    private  String rec;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        Bundle ExtraIntent = getIntent().getExtras();
        if(ExtraIntent != null) {

            hostname = ExtraIntent.getString("hostname");
            hostpass = ExtraIntent.getString("hostpass");
            textMessage = ExtraIntent.getString("textMessage");
            rec = ExtraIntent.getString("rec");
            subject = ExtraIntent.getString("subject");
            System.out.println("\n" + hostname + " " + hostpass + " " + textMessage +" "+ rec +" "+ subject);

        }else{
            Log.i("Parameter not Passed","Intent sending problem");

        }

        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(hostname, hostpass);
            }
        });
        // __pdialog = ProgressDialog.show(API_SendMail.this, "", "Sending Mail.......", true);
        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute();

    }
    class RetrieveFeedTask extends AsyncTask<String ,Void,String> {
        @Override
        protected String doInBackground(String... params){
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("rameshchandranitk2017@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
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
            //__pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","Sent");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }
    }
}
