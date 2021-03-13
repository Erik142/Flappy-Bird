package dev.wahlberger.flappybird;

import java.io.IOException;
import java.net.URISyntaxException;
import java.awt.Point;

import dev.wahlberger.flappybird.controller.GameKeyAction;
import dev.wahlberger.flappybird.controller.MainMenuActionListener;
import dev.wahlberger.flappybird.model.GameModel;
import dev.wahlberger.flappybird.view.GamePanel;
import dev.wahlberger.flappybird.view.MainMenuPanel;
import dev.wahlberger.flappybird.view.MainWindow;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        GameModel gameModel = new GameModel();

        MainWindow mainWindow = new MainWindow(gameModel);
        
        Point birdPosition = new Point((int)(mainWindow.getWidth() / 2), (int)(mainWindow.getHeight() / 2));
        
        MainMenuActionListener mainMenuActionListener = new MainMenuActionListener(mainWindow);
        GameKeyAction gameKeyAction = new GameKeyAction(gameModel);

        MainMenuPanel mainMenuPanel = mainWindow.getMainMenuPanel();
        GamePanel gamePanel = mainWindow.getGamePanel();

        gameModel.createBird(birdPosition, gamePanel.getFloorPosition());
        gameModel.addObserver(gamePanel);

        mainMenuPanel.registerListeners(mainMenuActionListener);
        gamePanel.registerListeners(gameKeyAction);

        gameModel.startGravity();
    }
}
