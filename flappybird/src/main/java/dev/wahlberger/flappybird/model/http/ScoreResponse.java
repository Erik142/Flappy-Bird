package dev.wahlberger.flappybird.model.http;

import com.google.gson.Gson;

public class ScoreResponse extends Response {
    private int score;
    private String usertoken;

    public static ScoreResponse fromJson(String json) {
        ScoreResponse response = new Gson().fromJson(json, ScoreResponse.class);

        return response;
    }

    public int getScore() {
        return this.score;
    }
    
    public String getToken() {
        return this.usertoken;
    }
}
