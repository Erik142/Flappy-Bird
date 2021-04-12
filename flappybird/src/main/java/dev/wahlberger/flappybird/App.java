package dev.wahlberger.flappybird;

import java.awt.Point;
import java.net.URISyntaxException;

import dev.wahlberger.flappybird.controller.MainMenuActionListener;
import dev.wahlberger.flappybird.model.GameModel;
import dev.wahlberger.flappybird.model.GamePanelModel;
import dev.wahlberger.flappybird.model.sprite.BirdModel;
import dev.wahlberger.flappybird.model.sprite.PipesModel;
import dev.wahlberger.flappybird.model.sprite.ScoreboardModel;
import dev.wahlberger.flappybird.sprite.BirdSprite;
import dev.wahlberger.flappybird.sprite.GameOverSprite;
import dev.wahlberger.flappybird.sprite.PipePairSprite;
import dev.wahlberger.flappybird.sprite.Scoreboard;
import dev.wahlberger.flappybird.view.GamePanel;
import dev.wahlberger.flappybird.view.MainMenuPanel;
import dev.wahlberger.flappybird.view.MainWindow;

/**
 * Hello world!
 */
public final class App {
    private static final int GAMEOVER_HEIGHT = 350;
    
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        try {
            Point birdPosition = new Point((int)(MainWindow.WINDOW_WIDTH / 2), (int)(MainWindow.WINDOW_HEIGHT / 2));

            GamePanelModel gamePanelModel = new GamePanelModel(MainWindow.WINDOW_HEIGHT);
            BirdModel birdModel = new BirdModel(birdPosition, gamePanelModel.getFloorPosition());
            PipesModel pipesModel = new PipesModel(birdModel, (int)gamePanelModel.getFloorPosition(), (int)MainWindow.WINDOW_WIDTH);
            ScoreboardModel scoreboardModel = new ScoreboardModel((int)MainWindow.WINDOW_WIDTH);
            
            GameOverSprite gameOverSprite = new GameOverSprite(new Point(MainWindow.WINDOW_WIDTH/2, GAMEOVER_HEIGHT));

            GameModel gameModel = new GameModel(birdModel, pipesModel, scoreboardModel, gameOverSprite);
            
            pipesModel.generate();
            
            BirdSprite birdSprite = new BirdSprite(birdModel);
            Scoreboard scoreBoardSprite = new Scoreboard(scoreboardModel);
            
            for (PipePairSprite pipe : pipesModel) {
                gamePanelModel.addPanel(pipe);
            }
            
            gamePanelModel.addPanel(birdSprite);
            gamePanelModel.addPanel(scoreBoardSprite);
            gamePanelModel.addPanel(gameOverSprite);

            MainWindow mainWindow = new MainWindow(gameModel, gamePanelModel);
            
            MainMenuActionListener mainMenuActionListener = new MainMenuActionListener(mainWindow);

            MainMenuPanel mainMenuPanel = mainWindow.getMainMenuPanel();
            GamePanel gamePanel = mainWindow.getGamePanel();

            gameModel.addObserver(gamePanel);

            mainMenuPanel.registerListeners(mainMenuActionListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
