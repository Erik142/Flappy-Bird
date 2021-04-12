package dev.wahlberger.flappybird.config;

import java.net.URL;

/**
 * Generic interface for the configuration file
 */
public interface Configuration {
    /**
     * Specifies if the app is runnning in standard or debug mode
     */
    public enum AppMode {
        /**
         * Standard mode
         */
        Standard,
        /**
         * Debug mode
         */
        Debug
    }

    /**
     * Retrieves the active app mode
     * @return The AppMode
     */
    public AppMode getAppMode();
    /**
     * Retrieves the URL for the high score web API
     * @return The URL for the high score web API
     */
    public URL getHighScoreUrl();
}
