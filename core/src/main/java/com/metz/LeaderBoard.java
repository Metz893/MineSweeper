package com.metz;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoard {
    private static ArrayList<Double> leaders;

    public LeaderBoard() {
        leaders = new ArrayList<>();
    }

    public ArrayList<Double> getLeaders() {
        return leaders;
    }

    public static void setLeaders(ArrayList<Double> leader) {
        leaders = leader;
    }

    public void add(double score) {
        leaders.add(score);
        Collections.sort(leaders);
    }

    public static ArrayList<Double> getLeaderBoard(){
        return leaders;
    }
}
