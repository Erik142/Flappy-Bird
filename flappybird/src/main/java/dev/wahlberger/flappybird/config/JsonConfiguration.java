package dev.wahlberger.flappybird.config;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class JsonConfiguration implements Configuration {

    private AppMode appMode;

    @Override
    public AppMode getAppMode() {
        return appMode;
    }

    public static Configuration getConfiguration(InputStream configStream) {
        Gson gson = new Gson();
        InputStreamReader configReader = new InputStreamReader(configStream);
        JsonConfiguration config = gson.fromJson(configReader, JsonConfiguration.class);

        return config;
    }
}
