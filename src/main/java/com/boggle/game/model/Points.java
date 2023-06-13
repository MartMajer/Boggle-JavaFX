package com.boggle.game.model;

public class Points {

    public static int gameScore = 0;



    public void setPoints(String word)
    {

        if (word.length()==3 || word.length()==4 )
        {
            gameScore += 1;
        }
        else if (word.length()==5)
        {
            gameScore += 2;
        }
        else if (word.length()==6)
        {
            gameScore += 3;
        }
        else if (word.length()==7)
        {
            gameScore += 4;
        }
        else if (word.length()<=8)
        {
            gameScore += 11;
        }





    }

    public int getPoints(){ return gameScore;}
    public void setPoints(){ gameScore =0;}
}
