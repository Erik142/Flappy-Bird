package dev.wahlberger.flappybird.model.http;

import com.google.gson.Gson;

public class UserResponse extends Response {
    private String usertoken;

    public static UserResponse fromJson(String json) {
        UserResponse response = new Gson().fromJson(json, UserResponse.class);

        return response;
    }

    public String getToken() {
        return this.usertoken;
    }
}
