package com.boggle.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.boggle.game.boggle.GameScreenController._charArray;
import static com.boggle.game.boggle.HelloController.getPlayerDetails;
import static com.boggle.game.model.BoggleSolverModel._wordsFound;

public class StoredDetailsModel implements Serializable {

    public static String P1;
    public static String P2;
    public static Integer overall_P1 = 0;
    public static Integer overall_P2 = 0;

    public static Integer score_P1_stored = 0;
    public static Integer score_P2_stored = 0;

    public static String round_stored;

    public static int _time_stored;
    public static char gameBoard_stored[][];
    public static ArrayList<String> _wordsFound_stored;

    public static List<String> _P1_checked_words;

    public static List<String> _P2_checked_words;


    public StoredDetailsModel(String P1, Integer score_P1, String round){

        this.P1 = P1;

        overall_P1 += score_P1;
        round_stored = round;




    }

    public StoredDetailsModel(String P1, String P2, Integer score_P1, Integer  score_P2, String round, int time){


        gameBoard_stored = _charArray.clone();

        this.P1 = P1;
        this.P2 = P2;

        score_P1_stored += score_P1;
        score_P2_stored += score_P2;

        round_stored = round;
        _time_stored = time;

        _wordsFound_stored = (ArrayList<String>) _wordsFound.clone();

        _P1_checked_words = getPlayerDetails().get_listOfCheckedWords();





    }

}
