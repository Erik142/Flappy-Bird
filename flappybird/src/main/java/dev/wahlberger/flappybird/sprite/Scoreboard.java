package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scoreboard {
    private final int PADDING = 3;
    private final double SCALING_FACTOR = 1.5;

    private Queue<NumberSprite> numbers = new ConcurrentLinkedQueue<NumberSprite>();

    private int currentScore = 0;
  
    private final int windowWidth;
    private final int scoreboardHeight;

    public Scoreboard(int windowWidth, int scoreboardHeight) throws URISyntaxException, IOException {
        this.windowWidth = windowWidth;
        this.scoreboardHeight = scoreboardHeight;

        resetScore();
    }

    public void resetScore() throws URISyntaxException, IOException {
        this.currentScore = 0;

        loadSprites();
    }

    public void increaseScore() throws URISyntaxException, IOException {
        this.currentScore += 1;

        loadSprites();
    }

    private void loadSprites() throws URISyntaxException, IOException {
        numbers.clear();

        String numbersString = "" + currentScore;
        char[] numbersChars = numbersString.toCharArray();
        NumberSprite previousSprite = null;

        for (int i = 0; i < numbersChars.length; i++) {
            char c = numbersChars[i];

            int number = Integer.parseInt(new String(new char[] { c }));

            NumberSprite sprite = new NumberSprite(number, SCALING_FACTOR);

            double middle = (numbersChars.length / 2);

            int xPos = 0;

            if (i == 0) {
                xPos = (int)((windowWidth / 2) - middle * sprite.getImage().getWidth());

                if (numbersChars.length % 2 == 1) {
                    xPos -= (int)(sprite.getImage().getWidth() / 2);
                }
            }
            else {
                xPos = previousSprite.getPosition().x + previousSprite.getImage().getWidth() + PADDING;
            }

            sprite.setPosition(new Point(xPos, scoreboardHeight));

            numbers.add(sprite);
            previousSprite = sprite;
        }
    }

    public void paint(Graphics g) {
        for (NumberSprite sprite : numbers) {
            sprite.paint(g);
        }
    }
}
