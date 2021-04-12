package dev.wahlberger.flappybird.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import dev.wahlberger.flappybird.config.ConfigFactory;
import dev.wahlberger.flappybird.config.Configuration;
import dev.wahlberger.flappybird.model.http.Response;
import dev.wahlberger.flappybird.model.http.ScoreResponse;
import dev.wahlberger.flappybird.model.http.UserResponse;
import dev.wahlberger.flappybird.observer.AbstractObservable;

public class HighScoreModel extends AbstractObservable<HighScoreModel> {
    private final String SCORE_API_PATH = "/v1/score";
    private final String USER_API_PATH = "/v1/user";

    private final String USER_TOKEN_HEADER = "usertoken";

    private HttpClient client;
    private URI apiUri;

    private String usertoken;
    private int score = 0;

    private Response latestResponse;

    public HighScoreModel() throws URISyntaxException {
        super();

        Configuration config = ConfigFactory.getConfiguration();

        apiUri = config.getHighScoreUrl().toURI();
        client = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).connectTimeout(Duration.ofSeconds(15)).build();
    }

    public void addUserAsync(String username, String password) {
        URI uri = apiUri.resolve(USER_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString("{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }")).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            UserResponse userResponse = UserResponse.fromJson(response.body());
            latestResponse = userResponse;

            usertoken = userResponse.getToken();
            updateObservers(this);
        });
    }

    public void deleteUserAsync(String usertoken) {
        URI uri = apiUri.resolve(USER_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).DELETE().header(USER_TOKEN_HEADER, usertoken).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            UserResponse userResponse = UserResponse.fromJson(response.body());
            latestResponse = userResponse;

            updateObservers(this);
        });
    }

    public boolean isUsertokenValid(String usertoken) throws IOException, InterruptedException {
        URI uri = apiUri.resolve(USER_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().header(USER_TOKEN_HEADER, usertoken).build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        UserResponse userResponse = UserResponse.fromJson(response.body());
        latestResponse = userResponse;

        return userResponse.getResult();
    }

    public void updateUserAsync(String username, String password, String usertoken) {
        URI uri = apiUri.resolve(USER_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString("{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"usertoken\": \"" + usertoken + " \" }")).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            UserResponse userResponse = UserResponse.fromJson(response.body());
            latestResponse = userResponse;

            this.usertoken = userResponse.getToken();
            updateObservers(this);
        });
    }

    public void addScoreAsync(String usertoken, int score) {
        URI uri = apiUri.resolve(SCORE_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString("{ \"usertoken\": \"" + usertoken + "\", \"score\": \"" + score + "\" }")).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            ScoreResponse scoreResponse = ScoreResponse.fromJson(response.body());
            latestResponse = scoreResponse;

            this.score = scoreResponse.getScore(); 
            updateObservers(this);
        });
    }

    public void getScoreAsync(String usertoken) {
        URI uri = apiUri.resolve(SCORE_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().header(USER_TOKEN_HEADER, usertoken).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            ScoreResponse scoreResponse = ScoreResponse.fromJson(response.body());
            latestResponse = scoreResponse;

            this.score = scoreResponse.getScore(); 
            updateObservers(this);
        });
    }

    public void deleteScoreAsync(String usertoken) {
        URI uri = apiUri.resolve(SCORE_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).DELETE().header(USER_TOKEN_HEADER, usertoken).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            ScoreResponse scoreResponse = ScoreResponse.fromJson(response.body());
            latestResponse = scoreResponse;

            this.score = 0;
            updateObservers(this);
        });
    }

    public void updateScoreAsync(String usertoken, int score) {
        URI uri = apiUri.resolve(SCORE_API_PATH);
        HttpRequest request = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString("{ \"usertoken\": \"" + usertoken + "\", \"score\": \"" + score + "\" }")).build();

        client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
            ScoreResponse scoreResponse = ScoreResponse.fromJson(response.body());
            latestResponse = scoreResponse;

            this.score = scoreResponse.getScore(); 
            updateObservers(this);
        });
    }

    public int getScore() {
        return this.score;
    }

    public String getToken() {
        return this.usertoken;
    }

    public Response getLatestResponse() {
        return this.latestResponse;
    }
}
