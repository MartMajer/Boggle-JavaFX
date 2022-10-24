package com.boggle.game.model;

public class HighscoreModel implements Comparable<HighscoreModel> {

    private String name;
    private Integer index;

    public HighscoreModel(int index, String name){

        this.name=name;
        this.index=index;

    }

    public int getIndex(){
        return index;
    }


    @Override
    public int compareTo(HighscoreModel name) {
        int compareIndex=((HighscoreModel)name).getIndex();

        return compareIndex - this.index;

    }

    @Override
    public String toString() {
        return name + " - " + index.toString();
    }
}
