package com.boggle.game.model;

public class StoredDetails {

    public static String P1;
    public static String P2;

    public static Integer overall_P1;
    public static Integer overall_P2;

    public static String round;



    public StoredDetails(String P1, String P2, Integer score_P1, Integer  score_P2, String round){

        this.P1 = P1;
        this.P2 = P2;
        overall_P1 += score_P1;
        overall_P2 += score_P2;
        this.round = round;


    }



}
