package com.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.Connection;
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
    Elements channels = doc.select("table");		//Extract channels from site
    
    Element table = channels.get(2);				//Get only TV Program data table
    
    Elements rows = table.select("tr");
    Elements headers = rows.select("img[alt]");
    
    print("%s", headers.get(0).attr("alt"));
   
    
        
	}
    private static void print(String msg, Object... args) {
    	System.out.println(String.format(msg, args));
    }

}
