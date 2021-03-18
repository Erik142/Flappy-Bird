package dev.wahlberger.flappybird.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class FrameUtil {
   public static void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = frame.getSize();
		
		int windowXPos = (int)(screenSize.getWidth() / 2 - windowSize.getWidth() / 2);
		int windowYPos = (int)(screenSize.getHeight() / 2 - windowSize.getHeight() / 2);
		
		frame.setBounds(windowXPos, windowYPos, (int)windowSize.getWidth(), (int)windowSize.getHeight());
   } 
}
