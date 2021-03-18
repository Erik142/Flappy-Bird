package dev.wahlberger.flappybird.config;

import java.io.InputStream;

import dev.wahlberger.flappybird.App;

public class ConfigFactory {

    private static final String CONFIG_PATH = "config.json";

    private static Configuration config = null;

    public static Configuration getConfiguration() {
        if (config == null) {
            InputStream configStream = App.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
            config = JsonConfiguration.getConfiguration(configStream);
        }

        return config;
    } 
}
