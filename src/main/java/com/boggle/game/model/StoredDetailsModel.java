package com.boggle.game.model;

import java.io.Serializable;


public class StoredDetailsModel implements Serializable {

    public static String P1;

    public static Integer overall_P1;

    public StoredDetailsModel(String P1, Integer score_P1, String round){

        this.P1 = P1;

        overall_P1 += score_P1;

    }

}
