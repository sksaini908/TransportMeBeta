package com.example.hacker_machine.navigationdrawer;

import java.util.Random;

/**
 * Created by hacker-machine on 30/3/16.
 */
public class API_RandomNumber {

    private Integer RandomNumber;
    public API_RandomNumber(){
        Random rand = new Random();                                           //to generate 7 digit random number
        this.RandomNumber = rand.nextInt(9000000) + 1000000;
    }
    public String getRandomNumber(){
         return  Integer.toString(RandomNumber);
    }
    //Integer num = ((API_RandomNumber) this.getApplication()).getRandomNumber();
}
