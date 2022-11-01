package com.boggle.game.model;

import java.io.Serializable;
import java.util.List;

public class BoggleModel implements Serializable {

    private int _time;
    private String _P1;
    private String _P2;
    private Integer _overall_P1;
    private Integer _overall_P2;
    private String _round;


    public BoggleModel(int time){

        _time = time;

    }


}
