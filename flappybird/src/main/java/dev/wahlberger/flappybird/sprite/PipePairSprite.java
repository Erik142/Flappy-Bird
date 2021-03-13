package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import dev.wahlberger.flappybird.sprite.PipeSprite.PipeDirection;

public class PipePairSprite {
   
    private final int PIPE_GAP = 225;
    private final int PADDING = 100;

    private PipeSprite bottomPipe = null;
    private PipeSprite topPipe = null;
   
    private final int totalHeight;

    public PipePairSprite(int totalHeight, int initialXPos) throws URISyntaxException, IOException {
        Point position = new Point(initialXPos, 0);
        bottomPipe = new PipeSprite(PipeDirection.Bottom, (Point)position.clone());
        topPipe = new PipeSprite(PipeDirection.Top, (Point)position.clone());

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

        topPipe.setPosition(topPipe.getPosition().x, topPosition, true);
        bottomPipe.setPosition(bottomPipe.getPosition().x, bottomPosition, false);
    }

    public PipeSprite getTopPipe() {
        return this.topPipe;
    }

    public PipeSprite getBottomPipe() {
        return this.bottomPipe;
    }
    public int getXPosition() {
        return topPipe.getPosition().x;
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
        return topPipe.getPosition().x + topPipe.getImage().getWidth() < 0;
    }

    public void paint(Graphics g) {
        //bottomPipe.draw(g);
        topPipe.draw(g);
        bottomPipe.draw(g);
    }
}
