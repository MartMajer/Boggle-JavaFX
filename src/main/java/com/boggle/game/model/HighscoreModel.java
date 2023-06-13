package com.boggle.game.model;

import java.io.Serializable;

public class HighscoreModel implements Comparable<HighscoreModel>, Serializable {

    private static final long serialVersionUID = 3648685275967776802L;

    private final String name;
    private final Integer index;



    public HighscoreModel(int index, String name){

        this.name=name;
        this.index=index;

    }

    public int getIndex(){
        return index;
    }


    @Override
    public int compareTo(HighscoreModel name) {
        int compareIndex= name.getIndex();

        return compareIndex - this.index;

    }

    @Override
    public String toString() {
        return name + " - " + index.toString();
    }
}
