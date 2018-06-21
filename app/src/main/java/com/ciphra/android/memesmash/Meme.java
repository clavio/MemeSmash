package com.ciphra.android.memesmash;

public class Meme {
    private int score;
    private int id;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Meme(int id, int score){
        this.id = id;
        this.score = score;
    }

    public Meme(){}
}
