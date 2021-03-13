package dev.wahlberger.flappybird.view;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import dev.wahlberger.flappybird.model.GameModel;
import dev.wahlberger.flappybird.observer.Observer;
import dev.wahlberger.flappybird.sprite.BirdSprite;

public class GamePanel extends JPanel implements Observer<GameModel> {
    private final String BACKGROUND_PATH = "background.png";
    private final String FLOOR_PATH = "floor.png";

    private final JFrame parentFrame;

    private BufferedImage backgroundImage = null;
    private BufferedImage floorImage = null;

    private final GameModel gameModel;

    public GamePanel(JFrame parentFrame, GameModel gameModel) throws URISyntaxException, IOException {
        this.parentFrame = parentFrame;

        this.gameModel = gameModel;

        loadBackground();
        loadFloor();
    }

    private void loadBackground() throws URISyntaxException, IOException {
        if (backgroundImage == null) {
            URL res = GamePanel.class.getClassLoader().getResource(BACKGROUND_PATH);
		    File backgroundFile = Paths.get(res.toURI()).toFile();
            backgroundImage = ImageIO.read(backgroundFile);
        }
    }

    private void loadFloor() throws URISyntaxException, IOException {
        if (floorImage == null) {
            URL res = getClass().getClassLoader().getResource(FLOOR_PATH);
            File floorFile = Paths.get(res.toURI()).toFile();
            floorImage = ImageIO.read(floorFile);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(floorImage, 0, parentFrame.getHeight() - floorImage.getHeight(), null);

        BirdSprite bird = gameModel.getBird();

        if (bird != null) {
            Point birdPosition = bird.getPosition();
            bird.draw(g);
            //g.drawImage(bird.getImage(), birdPosition.x, birdPosition.y, null);
            //paint(bird.getGraphics());
        }
    }

    @Override
    public void update(GameModel observable) {
        this.revalidate();
        this.repaint();
    }
    
    public void registerListeners(Action keyAction) {
        setKeyBindings(keyAction);
    }

    private void setKeyBindings(Action keyAction) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "Jump");
        getActionMap().put("Jump", keyAction);
    }

    public double getFloorPosition() {
        return parentFrame.getHeight() - floorImage.getHeight();
    }
}