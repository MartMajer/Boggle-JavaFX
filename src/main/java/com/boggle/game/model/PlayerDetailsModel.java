package com.boggle.game.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerDetailsModel implements Serializable {

    private static final long serialVersionUID = 6716309859678708712L;

    public String playerName;

    private List<String> _listOfCheckedWords;

    private ArrayList<String> _possibleWords;

    private Integer overall_player_1;

    private Integer round;

    private Integer _score;

    public void setRoundDetails(List<String> listOfCheckedWords, ArrayList<String> possibleWords, Integer score, Integer round,Integer overall_player_1) {

        _possibleWords = possibleWords;
        _listOfCheckedWords=listOfCheckedWords;
        _score=score;
        this.round=round;
        this.overall_player_1 = overall_player_1;
    }

    /////// getters for player

    public List<String> get_listOfCheckedWords(){ return _listOfCheckedWords;}
    public ArrayList<String> get_PossibleWords(){ return _possibleWords;}
    public String get_score(){return  _score.toString();}
    public Integer get_score_int(){return  _score;}
    public Integer get_round_int(){return  round;}
    public Integer get_overall_int(){return overall_player_1;}

    public PlayerDetailsModel(String playerName) {
        this.playerName = playerName;
    }
    public PlayerDetailsModel() {
    }

    public String getPlayerName() {
        return playerName;
    }

}
