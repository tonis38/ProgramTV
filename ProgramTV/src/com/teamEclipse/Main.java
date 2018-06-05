package com.teamEclipse;

import javax.swing.*;
import java.awt.*;
import com.teamEclipse.TVDatabase;

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

        	//TEST
        	/////////////////////////////////////
        	TVDatabase database = new TVDatabase();
        	database.insertNetworks(new TVNetwork(0, "Warsaw Shore", "Polska", "TEST"));
        	database.insertNetworks(new TVNetwork(0, "Dlaczego Ja?!?!?", "Polska", "TEST"));
        	database.insertNetworks(new TVNetwork(0, "Warsaw Shore", "Polska", "TEST"));
        	/////////////////////////////////////
        	
        	
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