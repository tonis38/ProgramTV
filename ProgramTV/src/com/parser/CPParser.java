package com.parser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CPParser {
	public static void main(String[] args) throws  IOException {
	//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
    String url = "http://www.cyfrowypolsat.pl/redir/program-tv/program-tv-pionowy.cp";
    print("Fetching %s...", url);
    
    Document doc = Jsoup.connect(url).get();
    Elements table = doc.select("table");		//Extract channels from site
    
    Element TVtable = table.get(2);				//Get only TV Program data table
    
    Element channels = TVtable.select("tr").first();	//Get TV channels name
    Elements headers = channels.select("img[alt]");
    
    Elements rows = TVtable.select("tr.rowWithoutHeader");
    
    for (int i = 0; i < headers.size(); i++) {
    	for(int j = 0; j < rows.size(); j++) {
    		Element row = rows.get(j);
    		Element column = row.select("td").get(i);
    		
    		Elements programs = column.select("div.newPtvTableProgram");
    		
    		for(Element program : programs) {
    		
    		String time = program.select("div.newPtvTableProgramLeft , div.newPtvTableProgramLeftFuture").text();
    		String name = program.select("div.newPtvTableProgramRight > a").text();
    		String desc = program.select("div.newPtvTableProgramRight > span").text();
    		
    		print("Channel name: %s. Program name: %s. Time: %s. Description: %s.", headers.get(i).attr("alt"), name, time, desc);
    		}
    	}
    }    
        
	}
    private static void print(String msg, Object... args) {
    	System.out.println(String.format(msg, args));
    }

}
