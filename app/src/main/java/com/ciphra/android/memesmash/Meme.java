package com.ciphra.android.memesmash;

public class Meme {
    private int score;
    private int id;
    private String pictureId;


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

    public Meme(int id, int score, String pictureId){
        this.id = id;
        this.score = score;
        this.pictureId = pictureId;
    }

    public Meme(){}

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }
}
