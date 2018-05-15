package com.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.regex.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupTest {
    public static void main(String[] args) throws  IOException {
    	//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = "https://ncplus.pl/program-tv";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements channels = doc.select("#programtvfull");
        String result = channels.text();									//Extract text
        String temp = "poka¿ ulubione wczeœniej 20:30 20:45 21:00 21:15 21:30 21:45 22:00 22:15 22:30 póŸniej\r\n";
        result = result.substring(result.indexOf("póŸniej") + 8, result.length() - temp.length() + 1);
        
        print(" * Nazwa programu: %s", result);
        
                      
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
} 