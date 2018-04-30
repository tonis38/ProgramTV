package com.teamEclipse;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Zura on 2018-04-30.
 */
public class Main {
    public static void main (String [] args){
        EventQueue.invokeLater(() -> {
        	try {
            	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e){
                e.printStackTrace();
            }

            /*
            JFrame frame = new TVFrame();
            frame.setTitle("Program TV");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            */
        });
    }
}