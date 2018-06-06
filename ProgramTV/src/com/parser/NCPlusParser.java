package com.parser;

import java.io.IOException;
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

public class NCPlusParser {
	
	public static List <TVItem> items = new LinkedList<TVItem>();
	
    public List <TVItem> ParseData() throws  IOException {
        String url = "https://ncplus.pl/program-tv";
        print("Fetching %s...", url);
        
        Document doc = Jsoup.connect(url).get();
        Elements channels = doc.select("#programtvfull > div.clearfix");		//Extract channels from site
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
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
        		
        		if(a > 1) { 
        			description = programname.substring( a );
        			programname = programname.substring(0, a - 2);
        		}
        		
        		//Fill item with data
        		item.setNetwork(channelname);
        		item.setName(programname);
        		item.setRuntime(starttime + " " + airdate);
        		item.setSummary(description);
        		print("Adding new item: Channel name: %s. Program name: %s. Time: %s %s. Description: %s", channelname, programname, starttime, airdate, description);	
        		
        		items.add(item);		//Add new item to the list
        	}
        }
        
        return items;
          
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

} 