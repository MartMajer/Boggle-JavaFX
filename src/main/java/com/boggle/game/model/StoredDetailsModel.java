package com.boggle.game.model;

import java.io.Serializable;


public class StoredDetailsModel implements Serializable {

    public static String player1;

    public static Integer overallScorePlayer1;

    public StoredDetailsModel(String player1, Integer scorePlayer1, String round) {

        StoredDetailsModel.player1 = player1;

        overallScorePlayer1 += scorePlayer1;

    }

}
