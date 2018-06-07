package com.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.teamEclipse.TVItem;


public class NTVPlusParser {
	
	private static List<TVItem> items = new LinkedList<TVItem>();
	
	public static List<TVItem> ParseData(){
	    String url = "https://ntvplus.ru/tv/ajax/tv?genre=all&date=now&tz=-1&search=&channel=&offset=0";	//Address of site        
        Document doc = null;		//Create new HTML document
        
        //Try to connect to site
		try {
			print("Fetching %s...", url);
			doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		} catch (MalformedURLException e) {
			System.err.printf("Bad URL. Check if URL is correct.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.printf("Cannot connect to site.");
			e.printStackTrace();
		}
		
	    Elements channels = doc.select("div.tv-schedule");		//Extract channels from site
	            
	    for (Element channel : channels) {	    	
	    	Elements programs =  channel.select("div.tv-schedule--item");								//Get all visible TV programs 
	    	for(Element program : programs) {
	    		
	    		String channelname = channel.select("div.channel-header--title").text();	//Get and print channel name
	    		String programname = program.select("a").text();
	    		String starttime = program.select("div.tv-schedule--item-time").text();
	    		
	    		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
 	        	Date date = new Date();
 	        	String progdate = dateFormat.format(date);											//Get current date
 	        	
 	        	TVItem item = new TVItem(0, programname, 0, 0, starttime + " " + progdate, null, null, null, channelname, null);	//Insert data to TVItem
 	        	items.add(item);
 	        	print("Adding item.....");			//Print on console
	    	}
	    }
	    
		return items;	//Return list of items
	   	
		}
	    private static void print(String msg, Object... args) {
	    	System.out.println(String.format(msg, args));
	    }

}
