package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;

import dev.wahlberger.flappybird.model.sprite.ScoreboardModel;

public class Scoreboard extends AbstractSprite {
    private final ScoreboardModel MODEL;

    public Scoreboard(ScoreboardModel scoreBoardModel) {
        this.MODEL = scoreBoardModel;

    }

    public void paint(Graphics g) {
        for (NumberSprite sprite : MODEL.getSprites()) {
            sprite.paint(g);
        }
    }
}
