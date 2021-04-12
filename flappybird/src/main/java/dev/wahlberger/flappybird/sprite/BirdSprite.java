package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import dev.wahlberger.flappybird.config.ConfigFactory;
import dev.wahlberger.flappybird.config.Configuration;
import dev.wahlberger.flappybird.config.Configuration.AppMode;
import dev.wahlberger.flappybird.model.sprite.BirdModel;

public class BirdSprite extends AbstractSprite {
    private final BirdModel model;

    public BirdSprite(BirdModel model) throws URISyntaxException, IOException {
        this.model = model;
    }

    private void rotateImage(Graphics2D g, double angle) {
        Point position = model.getPosition();
        BufferedImage image = model.getImage();

        g.rotate(Math.toRadians(angle), position.x, position.y);
        g.drawImage(image, position.x, position.y, null);
        g.rotate(Math.toRadians(-angle), position.x, position.y);
    }
    
    private void drawSurroundingBox(Graphics g) {
        Point actualPosition = this.model.getActualPosition();

        double actualWidth = this.model.getActualWidth();
        double actualHeight = this.model.getActualHeight();
        
        g.drawRect(actualPosition.x, actualPosition.y, (int)actualWidth, (int)actualHeight); 
    }

    public void paint(Graphics g) {
        double angle = this.model.getRotationAngle();

        Graphics2D graphics = (Graphics2D)g;
        rotateImage(graphics, angle);       
        
        Configuration config = ConfigFactory.getConfiguration();

        if (config != null && config.getAppMode() == AppMode.Debug) {
            drawSurroundingBox(g);
        }
    }
}
