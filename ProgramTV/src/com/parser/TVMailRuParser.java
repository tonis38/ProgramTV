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

public class TVMailRuParser {
    public static void main(String[] args) throws  IOException {
        String url = "https://tv.mail.ru/varshava/";
        print("Fetching %s...", url);
        
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        Elements channels = doc.select("div.p-channels__item");		//Extract channels from site
        
        for(Element channel : channels) {
        	 print("*Channel name: %s", channel.select("a[href].p-channels__item__info__title__link").text().replaceAll("live",""));			//Get channel name
        	 Elements programs = channel.select("div.p-programms__item > div.p-programms__item__inner");										//Extract channel programs
        	 for(Element program : programs) {
        		 print("\t Program name: %s. Time: %s", program.select("span.p-programms__item__name__link").text(), program.select("span.p-programms__item__time__value").text());
        	 }        	 
        }                 
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

} 