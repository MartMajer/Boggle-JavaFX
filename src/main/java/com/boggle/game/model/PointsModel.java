package com.boggle.game.model;

public class PointsModel {

    public static int _gameScore = 0;



    public void setPoints(String word)
    {

        if (word.length()==3 || word.length()==4 )
        {
            _gameScore += 1;
        }
        else if (word.length()==5)
        {
            _gameScore += 2;
        }
        else if (word.length()==6)
        {
            _gameScore += 3;
        }
        else if (word.length()==7)
        {
            _gameScore += 4;
        }
        else if (word.length()<=8)
        {
            _gameScore += 11;
        }





    }

    public int getPoints(){ return _gameScore;}
    public void setPoints(){ _gameScore=0;}
}
