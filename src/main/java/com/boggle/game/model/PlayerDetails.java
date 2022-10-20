package com.boggle.game.model;


import java.util.ArrayList;
import java.util.List;

public class PlayerDetails {

    private String playerName;

    private boolean isPlaying;

    private List<String> _listOfCheckedWords;
    private List<String> _words;

    public void setRoundDetails(List<String> listOfCheckedWords, List<String> words) {

        _listOfCheckedWords=listOfCheckedWords;
        _words=words;
    }

    public List<String> get_listOfCheckedWords(){ return _listOfCheckedWords;}
    public List<String> get_words(){ return _words;}


    public PlayerDetails(String playerName) {
        this.playerName = playerName;


    }

    public boolean IsPlaying()
    {
        isPlaying = true ;

       return isPlaying;
    }

    public String getPlayerName() {
        return playerName;
    }


}
