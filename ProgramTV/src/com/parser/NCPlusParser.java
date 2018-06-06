package com.parser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.teamEclipse.TVItem;

public class NCPlusParser {
	
	public static List <TVItem> items = new LinkedList<TVItem>();
	
    public List <TVItem> ParseData(){
        String url = "https://ncplus.pl/program-tv";
        print("Fetching %s...", url);
        
        Document doc = null;
		try {
			
			doc = Jsoup.connect(url).get();
		} catch (IOException e1) {
			System.err.printf("Connection error: cannot connect to %s. ", url);		// Cannot connect to site, throw error
			e1.printStackTrace();
			return null;		// Return nothing if catched exception
		}
		
        Elements channels = doc.select("#programtvfull > div.clearfix");		//Extract channels from site
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	Date date = new Date();
    	                    
        for (Element channel : channels) {
        	TVItem item = new TVItem();											//Create new item
        	String channelname =  channel.select("a[href].name-link").text();	//Get channel name
        	Elements programs =  channel.select("a[href = #]");					//Select all visible TV programs 
        	
        	for(Element program : programs) {
        		String programname = program.select("strong").text();			//Get program name
        		String time = program.select("div.time-age > span").text();		//Get program airing time
        		
        		int a = programname.indexOf("odc");								//Extract part after ','
        		int b = time.indexOf('-');
        		String starttime = time.substring(0,  b - 1);
        		String endtime = time.substring(b + 1);
        		String description = " ";
        		String airdate = dateFormat.format(date);
        		
        		dateFormat = new SimpleDateFormat("HH:mm");
        		Date begin = null;
        		Date end = null;
        		try {
					begin = dateFormat.parse(starttime);
					end = dateFormat.parse(endtime);
				} catch (ParseException e) {
					System.err.printf("Cannot parse airtime from start - end time.");
					e.printStackTrace();
				}
        		
        		
        		String runtime = dateFormat.format(end.getTime() - begin.getTime());
        		
        		if(a > 1) { 
        			description = programname.substring( a );
        			programname = programname.substring(0, a - 2);
        		}
        		
        		//Fill item with data
        		item.setNetwork(channelname);
        		item.setName(programname);
        		item.setAirDate(airdate + " " + starttime);
        		item.setRuntime(runtime);
        		item.setSummary(description);
        		print("Adding new item: Channel name: %s. Program name: %s. Time: %s %s.", channelname, programname, starttime, airdate);	
        		
        		items.add(item);		//Add new item to the list
        	}
        }
        
        return items;
          
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

} 