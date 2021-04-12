package dev.wahlberger.flappybird.sprite;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class PipeSprite extends AbstractSprite {
    public enum PipeDirection {
        Top,
        Bottom
    }

    private final double SPEED = 1.25;

    private final String BOTTOM_PIPE_PATH = "pipe/pipe_bottom.png";
    private final String TOP_PIPE_PATH = "pipe/pipe_top.png";

    private PipeDirection direction;
    private double x;
    private double y;

    private BufferedImage pipeImage = null;

    private boolean isMoving = true;

    public PipeSprite(PipeDirection direction, int initialXPos) throws URISyntaxException, IOException {
        this.direction = direction;

        this.x = initialXPos;

        loadImage();
        setOpaque(false);
    }

    private void loadImage() throws URISyntaxException, IOException {
        String pipePath = this.direction == PipeDirection.Bottom ? BOTTOM_PIPE_PATH : TOP_PIPE_PATH;

        if (pipeImage == null) {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(pipePath);
            pipeImage = ImageIO.read(resourceStream);
        }
    }
    
    public BufferedImage getImage() {
        return pipeImage;
    }

    public double getXPosition() {
        return this.x;
    }
    
    public double getYPosition() {
        return this.y;
    }

    public void setXPosition(double x) {
        this.x = x;
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition() {
        if (this.isMoving) {
            this.x -= SPEED;
        }
    }

    public void startMove() {
        this.isMoving = true;
    }

    public void stopMove() {
        this.isMoving = false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(pipeImage.getWidth(), pipeImage.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(pipeImage, (int)this.x, (int)this.y, this);
    }
}
