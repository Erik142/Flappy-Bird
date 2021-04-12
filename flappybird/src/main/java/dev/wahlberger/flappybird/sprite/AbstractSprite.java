package dev.wahlberger.flappybird.sprite;

import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class AbstractSprite extends JPanel {
    @Override
    public abstract void paint(Graphics g);
}
