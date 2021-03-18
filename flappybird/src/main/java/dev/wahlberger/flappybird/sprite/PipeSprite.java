package dev.wahlberger.flappybird.sprite;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PipeSprite extends JPanel {
    public enum PipeDirection {
        Top,
        Bottom
    }

    private final int SPEED = 1;

    private final String BOTTOM_PIPE_PATH = "pipe_bottom.png";
    private final String TOP_PIPE_PATH = "pipe_top.png";

    private PipeDirection direction;
    private Point position;

    private BufferedImage pipeImage = null;

    private boolean isMoving = true;

    public PipeSprite(PipeDirection direction, Point position) throws URISyntaxException, IOException {
        this.direction = direction;
        this.position = position;

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

    public Point getPosition() {
        return this.position;
    }

    public void setXPosition(int x) {
        this.position.x = x;
    }

    public void setPosition(int x, int y, boolean debug) {
        this.position.x = x;
        this.position.y = y;
    }

    public void updatePosition() {
        if (this.isMoving) {
            this.position.x -= SPEED;
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

    public void draw(Graphics g) {
        g.drawImage(pipeImage, this.position.x, this.position.y, this);
    }
}
