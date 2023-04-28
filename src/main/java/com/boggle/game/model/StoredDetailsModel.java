package com.boggle.game.model;

import java.io.Serializable;

public class StoredDetailsModel implements Serializable {

    public static String P1;

    public static Integer overall_P1 = 0;

    public char[][] boardArray;


    public StoredDetailsModel(String P1, Integer score_P1, String round, char[][] boardArray){

        this.P1 = P1;

        overall_P1 += score_P1;

        this.boardArray=boardArray;



    }

    public StoredDetailsModel( char[][] boardArray){

        this.boardArray=boardArray;

    }


    public char[][] getBoardArray() {
        return boardArray;
    }

    public void hello(){
          System.out.println("Hello");
    }
}
