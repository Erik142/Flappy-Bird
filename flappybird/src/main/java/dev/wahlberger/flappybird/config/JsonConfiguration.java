package dev.wahlberger.flappybird.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

public class JsonConfiguration implements Configuration {

    private AppMode appMode;
    private URL highscoreUrl;

    @Override
    public AppMode getAppMode() {
        return this.appMode;
    }

    @Override
    public URL getHighScoreUrl() {
        return this.highscoreUrl;
    }

    public static Configuration getConfiguration(InputStream configStream) {
        Gson gson = new Gson();
        InputStreamReader configReader = new InputStreamReader(configStream);
        JsonConfiguration config = gson.fromJson(configReader, JsonConfiguration.class);

        return config;
    }
}
