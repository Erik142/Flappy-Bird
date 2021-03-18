package dev.wahlberger.flappybird.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dev.wahlberger.flappybird.view.MainWindow;

public class MainMenuActionListener implements ActionListener {

    public static final String PLAY_COMMAND = "Play";
    public static final String QUIT_COMMAND = "Quit";

    private final MainWindow mainWindow;

    public MainMenuActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    private void startGame() {
        mainWindow.getMainMenuPanel().setVisible(false);
        mainWindow.getGamePanel().setVisible(true);
    }

    private void quit() {
        mainWindow.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
        case PLAY_COMMAND:
            startGame();
            break;
        case QUIT_COMMAND:
            quit();
            break;
        default:
        }
    }

}
