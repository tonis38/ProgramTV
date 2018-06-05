package com.teamEclipse;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import com.teamEclipse.TVDatabase;
import com.parser.*;

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

    		CPParser cp = new CPParser();
    		ParserDBConnect export = new ParserDBConnect();
    		try {
				export.ExportToDB(cp.ParseData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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