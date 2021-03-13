package dev.wahlberger.flappybird.sprite;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class PipeSprite extends JPanel {
    public enum PipePosition {
        Top,
        Bottom
    }

    private final String BOTTOM_PIPE_PATH = "pipe_bottom.png";
    private final String TOP_PIPE_PATH = "pipe_top.png";

    private final PipePosition position;

    private BufferedImage pipeImage = null;

    public PipeSprite(PipePosition position) throws URISyntaxException, IOException {
        this.position = position;

        loadImage();
    }

    private void loadImage() throws URISyntaxException, IOException {
        String pipePath = this.position == PipePosition.Bottom ? BOTTOM_PIPE_PATH : TOP_PIPE_PATH;

        if (pipeImage == null) {
            URL res = getClass().getClassLoader().getResource(pipePath);
            File pipeFile = Paths.get(res.toURI()).toFile();
            pipeImage = ImageIO.read(pipeFile);
        }
    }
}
