package com.boggle.game.model;

public class Highscore implements Comparable<Highscore> {

    private String name;
    private Integer index;

    public Highscore(int index, String name){

        this.name=name;
        this.index=index;

    }

    public int getIndex(){
        return index;
    }


    @Override
    public int compareTo(Highscore name) {
        int compareIndex=((Highscore)name).getIndex();

        return compareIndex - this.index;

    }

    @Override
    public String toString() {
        return name + " - " + index.toString();
    }
}
