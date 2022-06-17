package dev.wahlberger.flappybird.model;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import dev.wahlberger.flappybird.model.AudioHandler.AudioType;
import dev.wahlberger.flappybird.model.sprite.BirdModel;
import dev.wahlberger.flappybird.model.sprite.PipesModel;
import dev.wahlberger.flappybird.model.sprite.ScoreboardModel;
import dev.wahlberger.flappybird.observer.AbstractObservable;
import dev.wahlberger.flappybird.sprite.GameOverSprite;
import dev.wahlberger.flappybird.sprite.PipePairSprite;

public class GameModel extends AbstractObservable<GameModel> {
    private final int REFRESH_RATE = 60;
    
    private boolean isStarted = false;

    private final BirdModel BIRD_MODEL;
    private final PipesModel PIPES_MODEL;
    private final ScoreboardModel SCOREBOARD_MODEL;

    private Thread gameLogicThread;
    private Thread gamePaintThread;

    private final GameOverSprite GAME_OVER_SPRITE;
    
    private final AudioHandler AUDIO_HANDLER;

    public GameModel(BirdModel model, PipesModel pipesModel, ScoreboardModel scoreboardModel, GameOverSprite gameOverSprite) {
        super();

        this.BIRD_MODEL = model;
        this.PIPES_MODEL = pipesModel;
        this.SCOREBOARD_MODEL = scoreboardModel;
        this.GAME_OVER_SPRITE = gameOverSprite;

        this.AUDIO_HANDLER = new AudioHandler();
    }

    public void performJump() {
        if (!BIRD_MODEL.isCrashed() && BIRD_MODEL.isGravityEnabled()) {
            AUDIO_HANDLER.play(AudioType.Flap);
            BIRD_MODEL.performJump();
        }
    }

    public void startGame() {
        this.gameLogicThread = new Thread(() -> {
            try {
                SCOREBOARD_MODEL.reset();
            } catch (URISyntaxException | IOException e1) {
                e1.printStackTrace();
                return;
            }

            PIPES_MODEL.reset();

            BIRD_MODEL.reset();

            for (PipePairSprite sprite : PIPES_MODEL.getPipes()) {
                sprite.startMove();
            }

            GAME_OVER_SPRITE.setVisibility(false);
            BIRD_MODEL.start();

            boolean playedSound = false;
            int currentUpdate = 0;

            while (BIRD_MODEL.isGravityEnabled()) {
                long timeBeforeUpdate = System.currentTimeMillis();

                if (currentUpdate == 5) {
                    currentUpdate = 0;
                    BIRD_MODEL.update(true);
                }
                else {
                    BIRD_MODEL.update(false);
                }

                if (!BIRD_MODEL.isCrashed()) {
                    BIRD_MODEL.detectCollision(PIPES_MODEL.getNext());

                    if (BIRD_MODEL.isCrashed()) {
                        GAME_OVER_SPRITE.setVisibility(true);
                        AUDIO_HANDLER.play(AudioType.Crash);
                        playedSound = true;

                        for (PipePairSprite pipe : PIPES_MODEL.getPipes()) {
                            pipe.stopMove();
                        }
                    }
                }

                PipePairSprite nextPipe = PIPES_MODEL.getNext();
                PIPES_MODEL.update();
                PipePairSprite newNextPipe = PIPES_MODEL.getNext();

                if (!newNextPipe.equals(nextPipe)) {
                    try {
                    SCOREBOARD_MODEL.increase();
                    AUDIO_HANDLER.play(AudioType.IncreaseScore);
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                long timeAfterUpdate = System.currentTimeMillis();
                long timeDiff = timeAfterUpdate - timeBeforeUpdate;
                double sleepTime = 1000 / REFRESH_RATE;
                long adjustedSleepTime = (long)(sleepTime - timeDiff);

                if (adjustedSleepTime < 0) {
                    adjustedSleepTime = 0;
                }

                try {
                    Thread.sleep(adjustedSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               
                currentUpdate += 1;
            }

            BIRD_MODEL.stop();

            if (BIRD_MODEL.isCrashed() && !playedSound) {
                if (BIRD_MODEL.isCrashed()) {
                    AUDIO_HANDLER.play(AudioType.Crash);
                }
            }
            
            this.isStarted = false;
        }); 

        gamePaintThread = new Thread(() -> {
            long sleepTime = 1000 / REFRESH_RATE;
            long diffTime = 0;

            while (this.isStarted) {
                try {
                    Thread.sleep(sleepTime - diffTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                long timeBeforeUpdate = System.currentTimeMillis();
                updateObservers(this);
                long timeAfterUpdate = System.currentTimeMillis();
                diffTime = timeAfterUpdate - timeBeforeUpdate;
            }
        });

        this.isStarted = true;
        
        gameLogicThread.start();
        gamePaintThread.start();
    }

    public void stopGame() {
        if (gameLogicThread != null && gamePaintThread != null) {
            for (PipePairSprite sprite : PIPES_MODEL.getPipes()) {
                sprite.stopMove();
            }

            BIRD_MODEL.stop();

            try {
               gameLogicThread.join();
               gamePaintThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.isStarted = false;
    }
    
    public boolean isStarted() {
        return this.isStarted;
    }

}
