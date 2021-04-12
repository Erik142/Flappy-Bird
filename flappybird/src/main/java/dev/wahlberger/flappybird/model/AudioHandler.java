package dev.wahlberger.flappybird.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioHandler {
    public enum AudioType {
        Unknown,
        IncreaseScore,
        Crash,
        Flap
    }
   
    private final String SCORE_FILE = "audio/point.wav";
    private final String CRASH_FILE = "audio/hit.wav";
    private final String DIE_FILE = "audio/die.wav";
    private final String FLAP_FILE = "audio/wing.wav";

    private final Map<AudioType,String[]> CLIPS;

    public AudioHandler() {
        this.CLIPS = new HashMap<>();

        loadClips();
    }

    private void loadClips() {
        for (AudioType type : AudioType.values()) {
            String[] filePaths = new String[0];

            switch (type) {
                case IncreaseScore:
                    filePaths = new String[] { SCORE_FILE };
                    break;
                case Crash:
                    filePaths = new String[] { CRASH_FILE, DIE_FILE };
                    break;
                case Flap:
                    filePaths = new String[] { FLAP_FILE };
                    break;
                default:
            }

            if (filePaths.length > 0) {
                List<AudioInputStream> streams = new ArrayList<>();
                
                for (String path : filePaths) {
                    try {
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(path)));
                        streams.add(inputStream);

                    } catch (UnsupportedAudioFileException | IOException e) {
                      e.printStackTrace();
                    }
                }

                CLIPS.put(type, filePaths);
            }
        }
    }

    public void play(AudioType audioType) {
        new Thread(() -> {
            if (CLIPS.keySet().contains(audioType)) {
                String[] paths = CLIPS.get(audioType);

                for (String path : paths) {
                    AudioInputStream stream;

                    try {
                        stream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(path)));
                        
                        new Thread(() -> {
                            playSound(stream);
                        }).start();
                    } catch (UnsupportedAudioFileException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    private synchronized void playSound(AudioInputStream stream) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
