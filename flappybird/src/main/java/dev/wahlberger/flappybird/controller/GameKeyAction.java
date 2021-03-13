package dev.wahlberger.flappybird.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import dev.wahlberger.flappybird.model.GameModel;

public class GameKeyAction extends AbstractAction {

    private final GameModel gameModel;

    public GameKeyAction(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameModel.performJump();
    }
    
}
