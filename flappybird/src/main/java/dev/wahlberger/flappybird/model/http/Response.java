package dev.wahlberger.flappybird.model.http;

public abstract class Response {
    private String error;
    private boolean result;

    public String getErrorMessage() {
        return this.error;
    }

    public boolean getResult() {
        return this.result;
    }
}
