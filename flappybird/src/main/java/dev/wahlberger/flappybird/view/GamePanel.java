package dev.wahlberger.flappybird.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import dev.wahlberger.flappybird.model.GameModel;
import dev.wahlberger.flappybird.model.GamePanelModel;
import dev.wahlberger.flappybird.observer.Observer;

public class GamePanel extends JPanel implements Observer<GameModel> {
    private final GameModel gameModel;
    private final GamePanelModel gamePanelModel;

    public GamePanel(GamePanelModel gamePanelModel, GameModel gameModel) throws URISyntaxException, IOException {
        this.gamePanelModel = gamePanelModel;
        this.gameModel = gameModel;

        setKeyBindings();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(gamePanelModel.getBackgroundImage(), 0, 0, null);
        g.drawImage(gamePanelModel.getFloorImage(), 0, (int)gamePanelModel.getFloorPosition(), null);

        /*
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
            bird.paint(g);
        }
        
        Scoreboard scoreboard = gameModel.getScoreBoard();
 
        if (scoreboard != null) {
            scoreboard.paint(g);
        }

        if (bird != null && bird.isCrashed()) {
            GameOverSprite gameOverSprite = gameModel.getGameOverSprite();

            gameOverSprite.paint(g);
        }
        */

        for (JPanel panel : gamePanelModel.getPanels()) {
           panel.paint(g); 
        }
        
    }

    @Override
    public void update(GameModel observable) {
        this.revalidate();
        this.repaint();
    }
    
    private void setKeyBindings() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "Jump");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "StartGame");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "PauseGame");
        

        getActionMap().put("Jump", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.performJump();
            }
            
        });

        getActionMap().put("StartGame", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                if (gameModel.getBird().isCrashed() || !gameModel.getBird().isGravityEnabled()) {
                    gameModel.startGame();
                }
                */

                if (!gameModel.isStarted()) {
                    gameModel.startGame();
                }
            }
            
        });
    }
}