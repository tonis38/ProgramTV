package com.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class OnetParser {
	public static void main(String[] args) throws  IOException {
		//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
	    String url = "https://programtv.onet.pl/";
	    print("Fetching %s...", url);
	    
	    Document doc = Jsoup.connect(url).get();
	    Elements channels = doc.select("#emissions > div.inner > div.boxTVHolder");		//Extract channels from site
	    print("%s", channels.first());
	              
	    for (Element channel : channels) {
	    	print("*Channel name: %s", channel.select("span.tvName").text());				//Get and print channel name
	    	Elements programs =  channel.select("li");									//Get all visible TV programs 
	    	for(Element program : programs) {
	    		print("\t*Program name: %s. Time: %s. Type: %s.", program.select("span.title").text(), program.select("span.hour").text(), program.select("span.type").text());		//Print TV program name and airing time
	    	}
	    }
	   	
		}
	    private static void print(String msg, Object... args) {
	    	System.out.println(String.format(msg, args));
	    }

}
