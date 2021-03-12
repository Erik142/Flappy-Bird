package dev.wahlberger.flappybird.observer;

public interface Observer<T> {
    void update(T observable);
}