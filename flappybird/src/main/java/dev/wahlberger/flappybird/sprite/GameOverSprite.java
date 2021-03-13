package dev.wahlberger.flappybird.sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;

public class GameOverSprite {
    private final String IMAGE_PATH = "gameover.png";
    private final double SCALE_FACTOR = 2.0;

    private final BufferedImage image;
    private final Point position;

    public GameOverSprite(Point position) throws URISyntaxException, IOException {
        this.image = getImage();
        this.position = position;
        this.position.x -= this.image.getWidth() / 2;
    }

    private BufferedImage getImage() throws URISyntaxException, IOException {
        URL res = getClass().getClassLoader().getResource(IMAGE_PATH);
        File birdFile = Paths.get(res.toURI()).toFile();
        BufferedImage image = ImageIO.read(birdFile);
        Image scaledImage = image.getScaledInstance((int)(SCALE_FACTOR*image.getWidth()), (int)(SCALE_FACTOR*image.getHeight()), BufferedImage.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D imageGraphics = newImage.createGraphics();
        imageGraphics.drawImage(scaledImage, 0, 0, null);
        imageGraphics.dispose();

        image = newImage;
        
        return image;
    }

    public void paint(Graphics g) {
        g.drawImage(image, this.position.x, this.position.y, null);
    }
}
