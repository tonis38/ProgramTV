package com.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.teamEclipse.*;

public class TVMailRuParser extends Parser {

	private List<TVItem> items;
	private List<TVNetwork> networks;

	public TVMailRuParser() {
		items = new LinkedList<TVItem>();
		networks = new LinkedList<TVNetwork>();
	}

	public void ParseData() {
		String url = "https://tv.mail.ru/varshava/";

		Document doc = null; // Create new html document
		try {
			print("Fetching %s...", url);
			doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url); // Try to fetch site
		} catch (MalformedURLException e) {
			System.err.printf("Bad URL. Check if URL is correct.");
			// e.printStackTrace();
			return;
		} catch (IOException e) {
			System.err.printf("Cannot connect to site.");
			// e.printStackTrace();
			return;
		}

		Elements channels = doc.select("div.p-channels__item"); // Extract channels from site

		for (Element channel : channels) {
			String channelname = channel.select("a[href].p-channels__item__info__title__link").text().replaceAll("live",""); // Get channel name
			Elements programs = channel.select("div.p-programms__item > div.p-programms__item__inner"); //Extract programs from channel
			networks.add(new TVNetwork(0, channelname, null, null));
			for (Element program : programs) {

				String programname = program.select("span.p-programms__item__name__link").text(); // Get program name
				String starttime = program.select("span.p-programms__item__time__value").text(); // Get program start time

				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = new Date();
				String progdate = dateFormat.format(date); // Get current date
				
				items.add(new TVItem(0, programname, 0, 0, starttime + " " + progdate, null, null, null, channelname, null)); // Add new item to list
				//print("Adding item....."); // Print on console

			}
		}
		
		print("Parsing done...");
	}
	
	public List<TVItem> getItems() {
		return items;
	}

	public List<TVNetwork> getNetworks() {
		return networks;
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

}