package dev.wahlberger.flappybird.config;

public interface Configuration {
    public enum AppMode {
        Standard,
        Debug
    }

    public AppMode getAppMode();
}
