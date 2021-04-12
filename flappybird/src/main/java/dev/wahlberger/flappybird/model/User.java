package dev.wahlberger.flappybird.model;

public class User {
    private int id;
    private String username; 
    private String token;

    public User(int id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getToken() {
        return this.token;
    }
}
