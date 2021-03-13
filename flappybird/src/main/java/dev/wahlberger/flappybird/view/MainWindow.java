package dev.wahlberger.flappybird.view;

import java.awt.Container;
import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import dev.wahlberger.flappybird.model.GameModel;
import dev.wahlberger.flappybird.sprite.BirdSprite;
import dev.wahlberger.flappybird.util.FrameUtil;

public class MainWindow extends JFrame {
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 1000;

    private final MainMenuPanel mainMenuPanel;
    private final GamePanel gamePanel;

    public MainWindow(GameModel gameModel) throws URISyntaxException, IOException {
        super("Flappy Bird");
       
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        mainMenuPanel = new MainMenuPanel();
        gamePanel = new GamePanel(this, gameModel);

        createFrame();
        FrameUtil.centerFrame(this);
    }

    private void createFrame() {
        Container contentPane = this.getContentPane();
        contentPane.add(gamePanel);
        //contentPane.add(mainMenuPanel);

        this.setVisible(true);
        this.setResizable(false);
        this.pack();
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        contentPane.validate();
        contentPane.repaint();
    }
    
    public MainMenuPanel getMainMenuPanel() {
        return this.mainMenuPanel;
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }
}