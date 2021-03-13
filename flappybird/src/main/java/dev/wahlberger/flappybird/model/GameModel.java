package dev.wahlberger.flappybird.model;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;

import dev.wahlberger.flappybird.observer.AbstractObservable;
import dev.wahlberger.flappybird.sprite.BirdSprite;

public class GameModel extends AbstractObservable<GameModel> {
    
    
    private BirdSprite bird = null;

    private Thread fallThread;

    public GameModel() {
        super();
    }

    public BirdSprite getBird() {
        return this.bird;
    }

    public void createBird(Point initialPosition, double floorPosition) throws URISyntaxException, IOException {
        this.bird = new BirdSprite(initialPosition, floorPosition);

        this.fallThread = new Thread(() -> {
            while (bird.isGravityEnabled()) {
                bird.updatePosition();
                updateObservers(this);
                
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        });
    }

    public void performJump() {
        bird.performJump();
        updateObservers(this);
    }

    public void startGravity() {
        if (fallThread != null) {
            bird.startUpdatePosition();
            fallThread.start();
        }
    }

    public void stopGravity() {
        if (fallThread != null) {
            try {
                bird.stopUpdatePosition();
                fallThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
