package com.teamEclipse;

import javax.swing.*;

import java.awt.*;

/**
 * Created by Zura on 2018-04-30.
 */
public class Main {
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			try {
				// Check if GTK LnF is available. If not set to system defaults LnF
				boolean set = false;
				for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
						UIManager.setLookAndFeel(info.getClassName());
						set = true;
						break;
					}
				}
				if (!set)
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			JFrame frame = new TVFrame();
			frame.setTitle("Program TV");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setSize(new Dimension((int) (screenSize.width * 0.7), (int) (screenSize.width * 0.4)));
			frame.setLocationByPlatform(true);
			frame.setVisible(true);
		});
	}
}