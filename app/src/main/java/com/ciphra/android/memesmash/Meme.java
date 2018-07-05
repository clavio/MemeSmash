package com.ciphra.android.memesmash;

public class Meme {
    private int score;
    private String id;
    private String pictureId;
    private String userId;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meme(String id, int score, String pictureId){
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
