package com.boggle.game.model;


import java.util.ArrayList;
import java.util.List;

public class PlayerDetails {

    private String playerName;


    private List<String> _listOfCheckedWords;

    private ArrayList<String> _possibleWords;


    private Integer _score = 0;

    public void setRoundDetails(List<String> listOfCheckedWords, ArrayList<String> possibleWords, Integer score) {

        _possibleWords = possibleWords;
        _listOfCheckedWords=listOfCheckedWords;
        _score=score;
    }

    /////// getters for player
    public List<String> get_listOfCheckedWords(){ return _listOfCheckedWords;}

    public ArrayList<String> get_PossibleWords(){ return _possibleWords;}
    public String get_score(){return  _score.toString();}
    public Integer get_score_int(){return  _score;}

    public PlayerDetails(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

}
