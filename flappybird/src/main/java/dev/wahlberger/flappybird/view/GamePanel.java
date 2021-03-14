package dev.wahlberger.flappybird.view;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import dev.wahlberger.flappybird.model.GameModel;
import dev.wahlberger.flappybird.observer.Observer;
import dev.wahlberger.flappybird.sprite.BirdSprite;
import dev.wahlberger.flappybird.sprite.GameOverSprite;
import dev.wahlberger.flappybird.sprite.PipePairSprite;
import dev.wahlberger.flappybird.sprite.Scoreboard;

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
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(BACKGROUND_PATH);
            backgroundImage = ImageIO.read(resourceStream);
        }
    }

    private void loadFloor() throws URISyntaxException, IOException {
        if (floorImage == null) {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(FLOOR_PATH);
            floorImage = ImageIO.read(resourceStream);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(floorImage, 0, (int)getFloorPosition(), null);

        BirdSprite bird = gameModel.getBird();
        
        PipePairSprite[] pipes = gameModel.getPipes();
        
        if (pipes != null) {
            for (PipePairSprite sprite : pipes) {
                if (sprite != null) {
                    sprite.paint(g);
                }
            }
        }
        
        if (bird != null) {
            bird.draw(g);
        }
        
        Scoreboard scoreboard = gameModel.getScoreBoard();
 
        if (scoreboard != null) {
            scoreboard.paint(g);
        }

        if (bird != null && bird.isCrashed()) {
            GameOverSprite gameOverSprite = gameModel.getGameOverSprite();

            gameOverSprite.paint(g);
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
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "Jump");
        getActionMap().put("Jump", keyAction);
    }

    public double getFloorPosition() {
        return parentFrame.getHeight() - floorImage.getHeight();
    }
}