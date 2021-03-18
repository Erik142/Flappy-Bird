package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Random;

import javax.imageio.ImageIO;

import dev.wahlberger.flappybird.config.ConfigFactory;
import dev.wahlberger.flappybird.config.Configuration;
import dev.wahlberger.flappybird.config.Configuration.AppMode;

public class BirdSprite {

    public enum BirdColor {
        Yellow,
        Blue,
        Red
    }

    public enum FlapDirection {
        Up,
        Down
    }

    private final String MID_FLAP_PATH = "bird-midflap.png";
    private final String DOWN_FLAP_PATH = "bird-downflap.png";
    private final String UP_FLAP_PATH = "bird-upflap.png";

    private final double GRAVITY_ACCEL = 0.03*9.82;
    private final double JUMP_ACCEL = -20*GRAVITY_ACCEL;
    private final double ROTATE_ANGLE = 0.75;
    private final double SCALE_FACTOR = 2;
    private final double COLLISION_PADDING = 5;

    private double velocity = 0;

    private BufferedImage midFlapImage = null;
    private BufferedImage downFlapImage = null;
    private BufferedImage upFlapImage = null;
    private BufferedImage currentImage = null;

    private double currentAngle = 0;

    private Point position;
    private final Point initialPosition;

    private boolean isGravityEnabled = false;
    private boolean isCrashed = false;
    private boolean isJumpEnabled = false;
    private final double floorPosition;

    private final BirdColor color;
    private FlapDirection flapDirection;

    public BirdSprite(Point initialPosition, double floorPosition) throws URISyntaxException, IOException {
        super();

        Random random = new Random();
        int colorIndex = random.nextInt(BirdColor.values().length);

        this.color = BirdColor.values()[colorIndex];

        this.floorPosition = floorPosition;

        downFlapImage = getImage(DOWN_FLAP_PATH);
        midFlapImage = getImage(MID_FLAP_PATH);
        upFlapImage = getImage(UP_FLAP_PATH);

        currentImage = downFlapImage;
        this.flapDirection = FlapDirection.Up;
        
        this.initialPosition = initialPosition;

        this.initialPosition.x -= currentImage.getWidth() / 2;
        this.initialPosition.y -= currentImage.getHeight() / 2;

        this.position = initialPosition;
    }

    private BufferedImage getImage(String path) throws URISyntaxException, IOException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("" + this.color.toString().toLowerCase() + path);
        BufferedImage image = ImageIO.read(resourceStream);
        Image scaledImage = image.getScaledInstance((int)(SCALE_FACTOR*image.getWidth()), (int)(SCALE_FACTOR*image.getHeight()), BufferedImage.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D imageGraphics = newImage.createGraphics();
        imageGraphics.drawImage(scaledImage, 0, 0, null);
        imageGraphics.dispose();

        image = newImage;
        
        return image;
    }

    public void performJump() {
        if (!isJumpEnabled) {
            return;
        }

        velocity = 0;
        currentAngle = -45;
        updatePosition(JUMP_ACCEL);
    }

    public void updatePosition(boolean updateFlap) {
        if (!isCrashed) {
            if (currentAngle + ROTATE_ANGLE > 90) {
                currentAngle = 90;
            }
            else {
                currentAngle += ROTATE_ANGLE;
            }

            if (updateFlap) {
                if (currentImage == midFlapImage) {
                    if (flapDirection == FlapDirection.Up) {
                        currentImage = upFlapImage;
                    }   
                    else {
                        currentImage = downFlapImage;
                    } 
                }
                else {
                    if (flapDirection == FlapDirection.Down) {
                        flapDirection = FlapDirection.Up;
                    }
                    else {
                        flapDirection = FlapDirection.Down;
                    }
                    currentImage = midFlapImage;
                }
            }
        }

        updatePosition(GRAVITY_ACCEL);
    }
    
    public void detectCollision(PipePairSprite pipes) {
        double actualXPosition = this.position.x + COLLISION_PADDING;
        double actualYPosition = this.position.y + COLLISION_PADDING;

        if (currentAngle < 0) {
            actualYPosition -= this.getImage().getWidth()*Math.sin(Math.toRadians(Math.abs(currentAngle)));
        } else {
            actualXPosition -= Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight();
        }

        double actualWidth = Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getWidth() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight() - COLLISION_PADDING;
        double actualHeight = Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getWidth() - COLLISION_PADDING;

        if (pipes.getXPosition() <= (actualXPosition + actualWidth)) {
           double minYAllowed = pipes.getTopPipe().getPosition().y + pipes.getTopPipe().getImage().getHeight();
           double maxYAllowed = pipes.getBottomPipe().getPosition().y;

           if ((actualYPosition + COLLISION_PADDING) < minYAllowed || (actualYPosition + actualHeight - COLLISION_PADDING) > maxYAllowed) {
               isCrashed = true;
               isJumpEnabled = false;
           }
        }
    }
   
    public boolean isGravityEnabled() {
        return isGravityEnabled;
    }
   
    public boolean isCrashed() {
        return isCrashed;
    }
    
    public void startUpdatePosition() {
        isGravityEnabled = true;
        this.isJumpEnabled = true;
    }

    public void stopUpdatePosition() {
        isGravityEnabled = false;
        this.isJumpEnabled = false;
    }
    
    private void updatePosition(double acceleration) {
        if (!isGravityEnabled) {
            return;
        }

        double actualYPosition = this.position.y + COLLISION_PADDING;

        if (currentAngle < 0) {
            actualYPosition -= this.getImage().getWidth()*Math.sin(Math.toRadians(Math.abs(currentAngle)));
        }

        double actualHeight = Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getWidth() - COLLISION_PADDING;

        velocity += acceleration;

        if ((actualYPosition + velocity) < 0) {
            this.position.y = 0;
        }
        else if ((actualYPosition + actualHeight) + velocity >= floorPosition) {
            this.position.y += velocity;
            isCrashed = true;
            isJumpEnabled = false;
            isGravityEnabled = false;
        } else {
            this.position.y += velocity;
        }
    }

    private void rotateImage(Graphics2D g, double angle) {
        g.rotate(Math.toRadians(angle), position.x, position.y);
        g.drawImage(currentImage, position.x, position.y, null);
        g.rotate(Math.toRadians(-angle), position.x, position.y);
    }

    public void draw(Graphics g) {
        Graphics2D graphics = (Graphics2D)g;
        rotateImage(graphics, currentAngle);       
        
        Configuration config = ConfigFactory.getConfiguration();

        if (config != null && config.getAppMode() == AppMode.Debug) {
            double actualXPosition = this.position.x + COLLISION_PADDING;
            double actualYPosition = this.position.y + COLLISION_PADDING;

            if (currentAngle < 0) {
                actualYPosition -= this.getImage().getWidth()*Math.sin(Math.toRadians(Math.abs(currentAngle)));
            } else {
                actualXPosition -= Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight();
            }

            double actualWidth = Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getWidth() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight() - COLLISION_PADDING;
            double actualHeight = Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getHeight() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.currentImage.getWidth() - COLLISION_PADDING;
            
            g.drawRect((int)actualXPosition, (int)actualYPosition, (int)actualWidth, (int)actualHeight); 
        }
    }

    public void reset() {
        this.position = (Point)initialPosition.clone();
        this.currentAngle = 0;
        this.velocity = 0;
        this.isCrashed = false;
    }

    public BufferedImage getImage() {
        return this.currentImage;
    }

    public void resetPosition() {
        this.position = initialPosition;
    }

    public Point getPosition() {
        return this.position;
    }
}
