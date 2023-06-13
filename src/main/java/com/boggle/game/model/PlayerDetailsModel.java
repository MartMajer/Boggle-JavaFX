package com.boggle.game.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerDetailsModel implements Serializable {

    private static final long serialVersionUID = 6716309859678708712L;

    public String playerName;

    private List<String> listOfCheckedWords;

    private ArrayList<String> possibleWords;

    private Integer overallPlayer1;

    private Integer round;

    private Integer score;

    public void setRoundDetails(List<String> listOfCheckedWords, ArrayList<String> possibleWords, Integer score, Integer round, Integer overall_player_1) {

        this.possibleWords = possibleWords;
        this.listOfCheckedWords = listOfCheckedWords;
        this.score = score;
        this.round = round;
        this.overallPlayer1 = overall_player_1;
    }

    /////// getters for player

    public List<String> getListOfCheckedWords() {
        return listOfCheckedWords != null ? listOfCheckedWords : new ArrayList<>();
    }

    public ArrayList<String> getPossibleWords() {
        return possibleWords != null ? possibleWords : new ArrayList<>();
    }


    public Integer getScore() {
        return score != null ? score : 0;
    }

    public Integer getScoreInt() {
        return score != null ? score : 0;
    }

    public Integer getRoundInt() {
        return round != null ? round : 0;
    }

    public Integer getOverallInt() {
        return overallPlayer1 != null ? overallPlayer1 : 0;
    }

    public PlayerDetailsModel(String playerName) {
        this.playerName = playerName;
    }

    public PlayerDetailsModel() {
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setOverall(Integer overall) {
        this.overallPlayer1 = overall;
    }

    public String getPlayerName() {
        return playerName != null ? playerName : "0";
    }

}
