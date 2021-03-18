package dev.wahlberger.flappybird.model;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.Pipe;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import dev.wahlberger.flappybird.observer.AbstractObservable;
import dev.wahlberger.flappybird.sprite.BirdSprite;
import dev.wahlberger.flappybird.sprite.GameOverSprite;
import dev.wahlberger.flappybird.sprite.PipePairSprite;
import dev.wahlberger.flappybird.sprite.Scoreboard;

public class GameModel extends AbstractObservable<GameModel> {
    private final int NUM_OF_PIPES = 7;
    private final int GAP_BETWEEN_PIPES = 275;
    private final int SCOREBOARD_HEIGHT = 50;
    private final int GAMEOVER_HEIGHT = 350;
    
    private BirdSprite bird = null;
    private final PipePairSprite[] pairSprites;

    private Thread gameThread;

    private PipePairSprite lastPipe;
    private PipePairSprite nextPipe;

    private Scoreboard scoreBoard;
    private GameOverSprite gameOverSprite;

    public GameModel() {
        super();

        pairSprites = new PipePairSprite[NUM_OF_PIPES];
    }

    public BirdSprite getBird() {
        return this.bird;
    }

    public PipePairSprite[] getPipes() {
        return this.pairSprites;
    }
    
    public Scoreboard getScoreBoard() {
        return this.scoreBoard;
    }

    public GameOverSprite getGameOverSprite() {
        return this.gameOverSprite;
    }

    public void createBird(Point initialPosition, double floorPosition) throws URISyntaxException, IOException {
        this.bird = new BirdSprite(initialPosition, floorPosition);
    }

    public void createScoreboard(int windowWidth) throws URISyntaxException, IOException {
        scoreBoard = new Scoreboard(windowWidth, SCOREBOARD_HEIGHT);
    }
    
    public void createGameOverScreen(int windowWidth) throws URISyntaxException, IOException {
        gameOverSprite = new GameOverSprite(new Point(windowWidth/2, GAMEOVER_HEIGHT));
    }

    public void createPipes(int floorPosition, int windowWidth) throws URISyntaxException, IOException {
        for (int i = 0; i < pairSprites.length; i++) {
            PipePairSprite sprite = new PipePairSprite(floorPosition, windowWidth + 300 + i*GAP_BETWEEN_PIPES);
            this.pairSprites[i] = sprite;
        }

        lastPipe = this.pairSprites[NUM_OF_PIPES - 1];
        nextPipe = this.pairSprites[0];

        updateObservers(this);
    }

    public void performJump() {
        if (!bird.isCrashed() && bird.isGravityEnabled()) {
            bird.performJump();
            new Thread(() -> {
                playSound("wing.wav");
            }).start();
        }

        updateObservers(this);
    }

    private synchronized void playSound(String path) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
            new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(path)));
            clip.open(inputStream);
            clip.start(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        this.gameThread = new Thread(() -> {
            try {
                scoreBoard.resetScore();
            } catch (URISyntaxException | IOException e1) {
                e1.printStackTrace();
                return;
            }

            for (PipePairSprite pipe : pairSprites) {
                pipe.reset();
            }

            bird.reset();

            lastPipe = pairSprites[pairSprites.length - 1];
            nextPipe = pairSprites[0];

            updateObservers(this);

            for (PipePairSprite sprite : pairSprites) {
                sprite.startMove();
            }

            bird.startUpdatePosition();

            boolean playedSound = false;
            int currentUpdate = 0;
            while (bird.isGravityEnabled()) {
                if (currentUpdate == 10) {
                    currentUpdate = 0;
                    bird.updatePosition(true);
                }
                else {
                    bird.updatePosition(false);
                }

                if (!bird.isCrashed()) {
                    bird.detectCollision(nextPipe);

                    if (bird.isCrashed()) {
                        playSound("hit.wav");
                        playSound("die.wav");
                        playedSound = true;

                        for (PipePairSprite pipe : pairSprites) {
                            pipe.stopMove();
                        }
                    }
                }

                for (int i = 0; i < this.pairSprites.length; i++) {
                    PipePairSprite sprite = pairSprites[i];
                    sprite.movePipes();
    
                    if (sprite.equals(nextPipe) && (sprite.getXPosition() + sprite.getTopPipe().getImage().getWidth()) < bird.getPosition().x) {
                        nextPipe = pairSprites[(i + 1) % NUM_OF_PIPES];
                        
                        try {
                            scoreBoard.increaseScore();
                            playSound("point.wav");
                        } catch (URISyntaxException | IOException e) {
                            e.printStackTrace();
                        }
                    }
    
                    if (sprite.isOutsideOfScreen()) {
                        sprite.setXPosition(lastPipe.getXPosition() + GAP_BETWEEN_PIPES);
                        sprite.generatePositions();
                        lastPipe = sprite;
                    }
                }

                updateObservers(this);
                
                try {
                    Thread.sleep(9);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               
                currentUpdate += 1;
            }

            bird.stopUpdatePosition();

            if (bird.isCrashed() && !playedSound) {
                if (bird.isCrashed()) {
                    playSound("hit.wav");
                    playSound("die.wav");
                }
            }
        }); 

        gameThread.start();
    }

    public void stopGame() {
        if (gameThread != null) {
            for (PipePairSprite sprite : pairSprites) {
                sprite.stopMove();
            }

            bird.stopUpdatePosition();

            try {
               gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
