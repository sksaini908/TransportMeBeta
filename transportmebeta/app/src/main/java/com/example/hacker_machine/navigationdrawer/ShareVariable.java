package com.example.hacker_machine.navigationdrawer;

import android.app.Application;

/**
 * Created by hacker-machine on 7/3/16.
 * This class used to provide facility to set and get variable value from any class.
 * You Can also make some more variable and method to get and set variable value to user across app
 **/
public class ShareVariable extends Application {

    private String userName=null,Destination=null,Source=null,Name="unknown";
    private  String st_latitude="",st_longitude="",end_latitude="",end_longitude="";
    private boolean EntryFlag=false;
    public boolean getEntryFlag(){return EntryFlag;}
    public String getUserName(){return userName;}
    public String getName(){return  Name;}
    public String getSource(){return Source;}
    public String getDestination(){return Destination;}
    public String getSt_latitude() {return st_latitude;}
    public String getSt_longitude(){return st_longitude;}
    public String getEnd_latitude(){return end_latitude;}
    public String getEnd_longitude(){return end_longitude;}
    public void setUserName(String username){this.userName = username; }
    public void setName(String name){this.Name = name;}
    public void setSource(String source){this.Source=source;}
    public void setDestination(String destination){this.Destination=destination;}
    public void setSt_latitude(String slatitude){this.st_latitude=slatitude;}
    public void setSt_longitude(String slongitude){this.st_longitude=slongitude;}
    public void setEnd_latitude(String elatitude){this.end_latitude=elatitude;}
    public void setEnd_longitude(String elongitude){this.end_longitude=elongitude;}
    public void setEntryFlag(boolean flag){this.EntryFlag=flag;}
    /*((ShareVariable) this.getApplication()).setSomeVariable("foo");
     *String s = ((ShareVariable) this.getApplication()).getSomeVariable();
    */
}
