package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import dev.wahlberger.flappybird.sprite.PipeSprite.PipeDirection;

public class PipePairSprite extends AbstractSprite {
   
    private final int PIPE_GAP = 225;
    private final int PADDING = 100;

    private PipeSprite bottomPipe = null;
    private PipeSprite topPipe = null;
   
    private final int totalHeight;
    private final int initialXPos;

    public PipePairSprite(int totalHeight, int initialXPos) throws URISyntaxException, IOException {
        this.initialXPos = initialXPos;

        bottomPipe = new PipeSprite(PipeDirection.Bottom, initialXPos);
        topPipe = new PipeSprite(PipeDirection.Top, initialXPos);

        this.totalHeight = totalHeight;

        generatePositions();
    }

    public void generatePositions() {
        Random random = new Random();

        int pipeHeight = topPipe.getImage().getHeight();
        int maxRandomValue = totalHeight - PADDING;
        int minRandomValue = PADDING + PIPE_GAP;
        double randomFactor = random.nextDouble();

        int topPosition = (int)(minRandomValue + randomFactor*(maxRandomValue-minRandomValue)) - PIPE_GAP - pipeHeight;
        int bottomPosition = topPosition + PIPE_GAP + pipeHeight;

        topPipe.setPosition(topPipe.getXPosition(), topPosition);
        bottomPipe.setPosition(bottomPipe.getXPosition(), bottomPosition);
    }

    public PipeSprite getTopPipe() {
        return this.topPipe;
    }

    public PipeSprite getBottomPipe() {
        return this.bottomPipe;
    }
    public int getXPosition() {
        return (int)topPipe.getXPosition();
    }

    public void setXPosition(int xPos) {
        topPipe.setXPosition(xPos);
        bottomPipe.setXPosition(xPos);
    }

    public void startMove() {
        topPipe.startMove();
        bottomPipe.startMove();
    }

    public void stopMove() {
        topPipe.stopMove();
        bottomPipe.stopMove();
    }

    public void movePipes() {
        topPipe.updatePosition();
        bottomPipe.updatePosition();
    }

    public boolean isOutsideOfScreen() {
        return topPipe.getXPosition() + topPipe.getImage().getWidth() <= 0;
    }

    public void paint(Graphics g) {
        topPipe.paint(g);
        bottomPipe.paint(g);
    }

    public void reset() {
        this.setXPosition(initialXPos);
        this.generatePositions();
    }
}
