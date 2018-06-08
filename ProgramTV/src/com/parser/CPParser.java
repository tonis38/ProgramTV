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

public class CPParser {
	
	private static List <TVItem> items = new LinkedList<TVItem>();
	
	public List<TVItem> ParseData(){
    String url = "http://www.cyfrowypolsat.pl/redir/program-tv/program-tv-pionowy.cp";    
    Document doc = new Document(url);
    
    try {
    	System.out.printf("Fetching %s... \n", url);
    	doc = Jsoup.connect(url).get();											// Try to connect to site
    }
    catch (IOException e){
    	System.err.printf("Connection error: cannot connect to %s. ", url);		// Cannot connect to site, throw error
        //e.printStackTrace();
        return null;															// Exit, return null
    }
    
    //If connected, try to parse data
    
    Elements table = doc.select("table");		//Extract channels from site
    
    Element TVtable = table.get(2);				//Get only TV Program data table
    
    Element channels = TVtable.select("tr").first();				//Get TV channels name
    Elements headers = channels.select("img[alt]");
    
    Elements rows = TVtable.select("tr.rowWithoutHeader");			//Get table without headers
    
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");		//Get current date
	Date date = new Date();
    
    for (int i = 0; i < headers.size(); i++) {
    	for(int j = 0; j < rows.size(); j++) {
    		Element row = rows.get(j);
    		Element column = row.select("td").get(i);
    		
    		Elements programs = column.select("div.newPtvTableProgram");
    		
    		for(Element program : programs) {
    			
    			TVItem item = new TVItem();										//Make new temporary item
    			String network = headers.get(i).attr("alt");					//Get channel name
    			String progname = program.select("div.newPtvTableProgramRight > a").text().replace("\"", "'");
    			String progtime = program.select("div.newPtvTableProgramLeft , div.newPtvTableProgramLeftFuture").text().replace("od ", "");	//Get program start time
    			String airdate = dateFormat.format(date);						//Get program date
    			String description = program.select("div.newPtvTableProgramRight > span").text();	//Get description
    			
    			
    			//Fill with data
    			item.setNetwork(network);
    			item.setName(progname);
    			item.setAirDate(airdate + " " + progtime);
    			item.setSummary(description);
    			    			
    			items.add(item);			// Add item to the list   		
    		}
    	}
    }   
    	System.out.printf("Done....");
    	return items;
        
	}
}
