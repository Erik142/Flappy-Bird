package dev.wahlberger.flappybird.model.sprite;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import dev.wahlberger.flappybird.observer.AbstractObservable;

public abstract class AbstractSpriteModel<T> extends AbstractObservable<T> {
    /**
    *
    */
    private static final long serialVersionUID = 5625502352762537974L;
    private final double DEFAULT_SCALE = 1.0;

    protected AbstractSpriteModel() {
        super();
    }

    protected BufferedImage getImage(String imagePath, double scaleFactor) throws IOException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
        BufferedImage image = ImageIO.read(resourceStream);

        if (scaleFactor != DEFAULT_SCALE) {
            int scaledWidth = (int) (scaleFactor * image.getWidth());
            int scaledHeight = (int) (scaleFactor * image.getHeight());

            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D imageGraphics = newImage.createGraphics();
            imageGraphics.drawImage(scaledImage, 0, 0, null);
            imageGraphics.dispose();

            image = newImage;
        }

        return image;
    }

    protected BufferedImage getImage(String imagePath) throws IOException {
        return this.getImage(imagePath, DEFAULT_SCALE);
    }

}
