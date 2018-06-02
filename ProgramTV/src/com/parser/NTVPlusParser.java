package com.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NTVPlusParser {
	public static void main(String[] args) throws  IOException {
		//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
	    String url = "https://ntvplus.ru/tv/ajax/tv?genre=all&date=now&tz=-1&search=&channel=&offset=0";
        print("Fetching %s...", url);
        
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
	    Elements channels = doc.select("div.tv-schedule");		//Extract channels from site
	    //print("%s", channels.first());
	            
	    for (Element channel : channels) {
	    	print("*Channel name: %s", channel.select("div.channel-header--title").text());				//Get and print channel name
	    	Elements programs =  channel.select("div.tv-schedule--item");								//Get all visible TV programs 
	    	for(Element program : programs) {
	    		print("\t*Program name: %s. Time: %s.", program.select("a").text(), program.select("div.tv-schedule--item-time").text());		//Print TV program name and airing time
	    	}
	    }
	   	
		}
	    private static void print(String msg, Object... args) {
	    	System.out.println(String.format(msg, args));
	    }

}
