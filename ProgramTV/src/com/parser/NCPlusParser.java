package com.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NCPlusParser {
    public static void main(String[] args) throws  IOException {
    	//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = "https://ncplus.pl/program-tv";
        print("Fetching %s...", url);
        
        Document doc = Jsoup.connect(url).get();
        Elements channels = doc.select("#programtvfull > div.clearfix");		//Extract channels from site
                    
        for (Element channel : channels) {
        	print("*Channel name: %s", channel.select("a[href].name-link").text());				//Get and print channel name
        	Elements programs =  channel.select("a[href = #]");									//Get all visible TV programs 
        	for(Element program : programs) {
        		print("\t*Program name: %s. Time: %s", program.select("strong").text(), program.select("div.time-age > span").text());		//Print TV program name and airing time
        	}
        }
          
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

} 