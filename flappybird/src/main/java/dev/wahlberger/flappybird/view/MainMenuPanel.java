package dev.wahlberger.flappybird.view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import dev.wahlberger.flappybird.controller.MainMenuActionListener;

import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class MainMenuPanel extends JPanel {

    private JButton playButton;
    private JButton quitButton;

    public MainMenuPanel() {
        super();

        createPanel();
    }

    private void createPanel() {
        playButton = new JButton("New game");
        quitButton = new JButton("Quit");

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(15,5,15,5);
        constraints.ipady = 40;
        constraints.ipadx = 40;
        constraints.fill = GridBagConstraints.BOTH;

        this.setLayout(layout);
        this.add(playButton, constraints);

        constraints.gridy = 1;

        this.add(quitButton, constraints);
        this.setOpaque(false);
    }

    public void registerListeners(ActionListener actionListener) {
        playButton.setActionCommand(MainMenuActionListener.PLAY_COMMAND);
        playButton.addActionListener(actionListener);
        quitButton.setActionCommand(MainMenuActionListener.QUIT_COMMAND);
        quitButton.addActionListener(actionListener);
    }
}