package dev.wahlberger.flappybird.model.sprite;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import dev.wahlberger.flappybird.sprite.NumberSprite;

public class ScoreboardModel extends AbstractSpriteModel<ScoreboardModel> {
    private final int PADDING = 3;
    private final double SCALING_FACTOR = 1.5;

    private final Queue<NumberSprite> sprites;

    private final int WINDOW_WIDTH;
    private final int VERTICAL_POSITION = 50;

    private long score = 0;

    public ScoreboardModel(int windowWidth) throws IOException, URISyntaxException {
        super();

        this.WINDOW_WIDTH = windowWidth;

        this.sprites = new ConcurrentLinkedQueue<NumberSprite>();
        
        update();
    }

    public void reset() throws IOException, URISyntaxException {
        this.score = 0;

        update();
    }

    public void increase() throws IOException, URISyntaxException {
        this.score += 1;

        update();
    }

    private void update() throws IOException, URISyntaxException {
        sprites.clear();

        char[] numbersChars = Long.toString(score).toCharArray();
        NumberSprite previousSprite = null;

        for (int i = 0; i < numbersChars.length; i++) {
            char c = numbersChars[i];

            int number = Integer.parseInt(new String(new char[] { c }));

            NumberSprite sprite = new NumberSprite(number, SCALING_FACTOR);

            double middle = (numbersChars.length / 2);

            int xPos = 0;

            if (i == 0) {
                xPos = (int)((this.WINDOW_WIDTH / 2) - middle * sprite.getImage().getWidth());

                if (numbersChars.length % 2 == 1) {
                    xPos -= (int)(sprite.getImage().getWidth() / 2);
                }
            }
            else {
                xPos = previousSprite.getPosition().x + previousSprite.getImage().getWidth() + PADDING;
            }

            sprite.setPosition(new Point(xPos, this.VERTICAL_POSITION));

            sprites.add(sprite);
            previousSprite = sprite;
        }
    }

    public Collection<NumberSprite> getSprites() {
        return Collections.unmodifiableCollection(this.sprites);
    }
}
