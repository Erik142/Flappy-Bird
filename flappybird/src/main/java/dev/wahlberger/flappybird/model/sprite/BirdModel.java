package dev.wahlberger.flappybird.model.sprite;

import dev.wahlberger.flappybird.sprite.PipePairSprite;

import java.awt.Point;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BirdModel extends AbstractSpriteModel<BirdModel> {
    public enum BirdColor {
        Yellow,
        Blue,
        Red
    }

    public enum FlapDirection {
        Up,
        Down
    }

    private final double FLOOR_POSITION;
    private final double GRAVITY_ACCEL = 0.07*9.82;
    private final double JUMP_ACCEL = -12*GRAVITY_ACCEL;
    private final double ROTATE_ANGLE = 1.2;
    private final double COLLISION_PADDING = 5;
    private final double SCALE_FACTOR = 2;

    private final String RESOURCE_FOLDER = "bird/";
    private final String MID_FLAP_PATH = "bird-midflap.png";
    private final String DOWN_FLAP_PATH = "bird-downflap.png";
    private final String UP_FLAP_PATH = "bird-upflap.png";

    private final Point INITIAL_POSITION;

    private final BirdColor BIRD_COLOR;
    
    private FlapDirection flapDirection;

    private boolean isGravityEnabled = false;
    private boolean isCrashed = false;
    private boolean isJumpEnabled = false;

    private double velocity = 0;
    private double currentAngle = 0;

    private Point position;


    private BufferedImage midFlapImage = null;
    private BufferedImage downFlapImage = null;
    private BufferedImage upFlapImage = null;
    private BufferedImage currentImage = null;

    public BirdModel(Point initialPosition, double floorPosition) throws IOException {
        super();

        this.BIRD_COLOR = generateColor();
        loadImages();

        this.FLOOR_POSITION = floorPosition;

        this.flapDirection = FlapDirection.Up;

        this.INITIAL_POSITION = initialPosition;
        this.INITIAL_POSITION.x -= currentImage.getWidth() / 2;
        this.INITIAL_POSITION.y -= currentImage.getHeight() / 2;

        this.position = initialPosition;
    }

    private BirdColor generateColor() {
        Random random = new Random();
        int colorIndex = random.nextInt(BirdColor.values().length);

        BirdColor birdColor = BirdColor.values()[colorIndex];

        return birdColor;
    }

    private void loadImages() throws IOException {
        downFlapImage = super.getImage(this.RESOURCE_FOLDER + this.BIRD_COLOR.toString().toLowerCase() + DOWN_FLAP_PATH, SCALE_FACTOR);
        midFlapImage = super.getImage(this.RESOURCE_FOLDER + this.BIRD_COLOR.toString().toLowerCase() + MID_FLAP_PATH, SCALE_FACTOR);
        upFlapImage = super.getImage(this.RESOURCE_FOLDER + this.BIRD_COLOR.toString().toLowerCase() + UP_FLAP_PATH, SCALE_FACTOR);

        currentImage = downFlapImage; 
    }

    public void reset() {
        resetPosition();

        this.currentAngle = 0;
        this.velocity = 0;
        this.isCrashed = false;
    }

    public void resetPosition() {
        this.position = (Point)INITIAL_POSITION.clone();
    }

    public Point getPosition() {
        return this.position;
    }

    public double getRotationAngle() {
        return this.currentAngle;
    }

    public void performJump() {
        if (!isJumpEnabled) {
            return;
        }

        velocity = 0;
        currentAngle = -45;
        updatePosition(JUMP_ACCEL);
    }

    public void update(boolean updateFlap) {
        if (!isCrashed) {
            if (currentAngle + ROTATE_ANGLE > 90) {
                currentAngle = 90;
            }
            else {
                currentAngle += ROTATE_ANGLE;
            }

            if (updateFlap) {
                updateCurrentImage();
            }
        }

        updatePosition(GRAVITY_ACCEL);
    }

    private void updateCurrentImage() {
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
    
    public void detectCollision(PipePairSprite pipes) {
        Point actualPosition = this.getActualPosition();

        double actualWidth = getActualWidth();
        double actualHeight = getActualHeight();

        if (pipes.getXPosition() <= (actualPosition.x + actualWidth)) {
           double minYAllowed = pipes.getTopPipe().getYPosition() + pipes.getTopPipe().getImage().getHeight();
           double maxYAllowed = pipes.getBottomPipe().getYPosition();

           if ((actualPosition.y + COLLISION_PADDING) < minYAllowed || (actualPosition.y + actualHeight - COLLISION_PADDING) > maxYAllowed) {
               isCrashed = true;
               isJumpEnabled = false;
           }
        }
    }

    public double getActualWidth() {
        return Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getWidth() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getHeight() - COLLISION_PADDING;
    }

    public double getActualHeight() {
        return Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getHeight() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getWidth() - COLLISION_PADDING;
    }
   
    public Point getActualPosition() {
        Point position = (Point)this.position.clone();
        position.x += COLLISION_PADDING;
        position.y += COLLISION_PADDING;

        if (currentAngle < 0) {
            position.y -= this.getImage().getWidth()*Math.sin(Math.toRadians(Math.abs(currentAngle)));
        } else {
            position.x -= Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getHeight();
        }

        return position;
    }
    
    public BufferedImage getImage() {
        return this.currentImage;
    }

    public boolean isGravityEnabled() {
        return isGravityEnabled;
    }
   
    public boolean isCrashed() {
        return isCrashed;
    }
    
    public void start() {
        isGravityEnabled = true;
        this.isJumpEnabled = true;
    }

    public void stop() {
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

        double actualHeight = Math.cos(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getHeight() + Math.sin(Math.toRadians(Math.abs(currentAngle)))*this.getImage().getWidth() - COLLISION_PADDING;

        velocity += acceleration;

        if ((actualYPosition + velocity) < 0) {
            this.position.y = 0;
        }
        else if ((actualYPosition + actualHeight) + velocity >= FLOOR_POSITION) {
            this.position.y += velocity;
            // Player crashed in the floor, game is ended
            this.isCrashed = true;
            this.isJumpEnabled = false;
            this.isGravityEnabled = false;
        } else {
            this.position.y += velocity;
        }
    }
}
