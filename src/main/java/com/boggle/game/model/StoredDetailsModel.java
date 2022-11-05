package com.boggle.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.boggle.game.boggle.GameScreenController._charArray;
import static com.boggle.game.boggle.HelloController.getPlayerDetails;
import static com.boggle.game.model.BoggleSolverModel._wordsFound;

public class StoredDetailsModel implements Serializable {

    public static String P1;

    public static Integer overall_P1 = 0;


    public static Integer score_P1_stored = 0;


    public static String round_stored;




    public StoredDetailsModel(String P1, Integer score_P1, String round){

        this.P1 = P1;

        overall_P1 += score_P1;
        //round_stored = round;




    }



}
