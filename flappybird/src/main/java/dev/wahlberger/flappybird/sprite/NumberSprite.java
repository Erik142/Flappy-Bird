package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class NumberSprite {
    private BufferedImage image = null;

    private Point position;

    public NumberSprite(int number, double scalingFactor) throws URISyntaxException, IOException {
        loadImage(number, scalingFactor);
    } 

    private void loadImage(int number, double scalingFactor) throws URISyntaxException, IOException {
        String imagePath = "" + number + ".png";
        
        if (image == null) {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);

            image = ImageIO.read(resourceStream);
            Image scaledImage = image.getScaledInstance((int)(scalingFactor*image.getWidth()), (int)(scalingFactor*image.getHeight()), BufferedImage.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            Graphics2D imageGraphics = newImage.createGraphics();
            imageGraphics.drawImage(scaledImage, 0, 0, null);
            imageGraphics.dispose();

            image = newImage;
        }
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void paint(Graphics g) {
        g.drawImage(image, this.position.x, this.position.y, null);
    }
}
