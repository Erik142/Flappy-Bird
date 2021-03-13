package dev.wahlberger.flappybird.sprite;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import dev.wahlberger.flappybird.observer.AbstractObservable;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class BirdSprite {

    private final String BIRD_PATH = "bird.png";

    private final double GRAVITY_ACCEL = 0.03*9.82;
    private final double JUMP_ACCEL = -20*GRAVITY_ACCEL;
    private final double ROTATE_ANGLE = 0.75;

    private double velocity = 0;

    private BufferedImage birdImage = null;
    private BufferedImage birdRotatedImage = null;
    private Graphics2D birdGraphics = null;

    private double currentAngle = 0;

    private Point position;
    private final Point initialPosition;

    private boolean isGravityEnabled = false;
    private boolean isCrashed = false;
    private boolean isJumpEnabled = true;
    private final double floorPosition;

    public BirdSprite(Point initialPosition, double floorPosition) throws URISyntaxException, IOException {
        super();

        this.floorPosition = floorPosition;

        loadImage();
        
        this.initialPosition = initialPosition;

        this.initialPosition.x -= birdImage.getWidth() / 2;
        this.initialPosition.y -= birdImage.getHeight() / 2;

        this.position = initialPosition;
    }

    private void loadImage() throws URISyntaxException, IOException {
        if (birdImage == null) {
            URL res = getClass().getClassLoader().getResource(BIRD_PATH);
            File birdFile = Paths.get(res.toURI()).toFile();
            birdImage = ImageIO.read(birdFile);
            birdRotatedImage = birdImage;
            birdGraphics = birdImage.createGraphics();
        }
    }

    public void performJump() {
        if (!isJumpEnabled) {
            return;
        }

        velocity = 0;
        currentAngle = -20;
        updatePosition(JUMP_ACCEL);
    }

    public void updatePosition() {
        if (!isCrashed) {
            if (currentAngle + ROTATE_ANGLE > 90) {
                currentAngle = 90;
            }
            else {
                currentAngle += ROTATE_ANGLE;
            }
        }

        updatePosition(GRAVITY_ACCEL);
    }
   
    public boolean isGravityEnabled() {
        return isGravityEnabled;
    }
    
    public void startUpdatePosition() {
        isGravityEnabled = true;
    }

    public void stopUpdatePosition() {
        isGravityEnabled = false;
    }
    
    private void updatePosition(double acceleration) {
        if (!isGravityEnabled || !isJumpEnabled) {
            return;
        }

        isCrashed = false;

        velocity += acceleration;

        if ((this.position.y + velocity) < 0) {
            this.position.y = 0;
        }
        else if ((this.position.y + birdImage.getHeight()) + velocity >= floorPosition) {
            this.position.y = (int)(floorPosition - birdImage.getHeight());
            isCrashed = true;
            isJumpEnabled = false;
        } else {
            this.position.y += velocity;
        }
    }

    private void rotateImage(Graphics2D g, double angle) {
        g.rotate(Math.toRadians(angle), position.x, position.y);
        g.drawImage(birdImage, position.x, position.y, null);
    }

    public void draw(Graphics g) {
        Graphics2D graphics = (Graphics2D)g;
        rotateImage(graphics, currentAngle); 
    }

    public BufferedImage getImage() {
        return this.birdRotatedImage;
    }

    public Graphics2D getGraphics() {
        return birdGraphics;
    }

    public void resetPosition() {
        this.position = initialPosition;
    }

    public Point getPosition() {
        return this.position;
    }
}
