package com.boggle.game.model;

import java.io.Serializable;


public class StoredDetails implements Serializable {

    public static String player1;

    public static Integer overallScorePlayer1;

    public StoredDetails(String player1, Integer scorePlayer1, String round) {

        StoredDetails.player1 = player1;

        overallScorePlayer1 += scorePlayer1;

    }

}
