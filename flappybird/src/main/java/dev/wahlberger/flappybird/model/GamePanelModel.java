package dev.wahlberger.flappybird.model;

import dev.wahlberger.flappybird.model.sprite.AbstractSpriteModel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

import javax.swing.JPanel;

public class GamePanelModel extends AbstractSpriteModel<GamePanelModel> {
    private final String BACKGROUND_PATH = "background/background.png";
    private final String FLOOR_PATH = "background/floor.png";

    private final BufferedImage BACKGROUND_IMAGE;
    private final BufferedImage FLOOR_IMAGE;

    private final double PARENT_HEIGHT;
    
    private final Stack<JPanel> PANELS;

    public GamePanelModel(double parentHeight) throws IOException {
        super();

        PANELS = new Stack<>();

        PARENT_HEIGHT = parentHeight;

        BACKGROUND_IMAGE = getImage(BACKGROUND_PATH);
        FLOOR_IMAGE = getImage(FLOOR_PATH);
    }
   
    public BufferedImage getBackgroundImage() {
        return this.BACKGROUND_IMAGE;
    }

    public BufferedImage getFloorImage() {
        return this.FLOOR_IMAGE;
    }
    
    public double getFloorPosition() {
        return this.PARENT_HEIGHT - this.FLOOR_IMAGE.getHeight();
    }

    public void addPanel(JPanel panel) {
        PANELS.add(panel);
    }
    
    public Collection<JPanel> getPanels() {
        return Collections.unmodifiableCollection(PANELS);
    }

    public void removePanel(JPanel panel) {
        if (PANELS.contains(panel)) {
            PANELS.remove(panel);
        }
    }
}
