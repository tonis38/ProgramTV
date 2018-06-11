package com.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.teamEclipse.TVItem;
import com.teamEclipse.TVNetwork;

public class NTVPlusParser extends Parser {

	private List<TVItem> items;
	private Set<TVNetwork> networks;

	public NTVPlusParser() {
		items = new LinkedList<TVItem>();
		networks = new HashSet<TVNetwork>();
	}

	public void ParseData() {
		String url = "https://ntvplus.ru/tv/ajax/tv?genre=all&date=now&tz=-1&search=&channel=&offset=0"; // Address of
																											// site
		Document doc = null; // Create new HTML document

		// Try to connect to site
		try {
			print("Fetching %s...", url);
			doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		} catch (MalformedURLException e) {
			System.err.printf("Bad URL. Check if URL is correct.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			System.err.printf("Cannot connect to site.");
			e.printStackTrace();
			return;
		}

		Elements channels = doc.select("div.tv-schedule"); // Extract channels from site

		for (Element channel : channels) {
			networks.add(new TVNetwork(0, channel.select("div.channel-header--title").text(), null, null)); // Add new
																											// network
																											// to set
			String channelname = channel.select("div.channel-header--title").text(); // Get and print channel name
			Elements programs = channel.select("div.tv-schedule--item"); // Get all visible TV programs

			for (Element program : programs) {

				String programname = program.select("a").text();
				String starttime = program.select("div.tv-schedule--item-time").text();

				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = new Date();
				String progdate = dateFormat.format(date); // Get current date

				items.add(new TVItem(0, programname, 0, 0, progdate + " " + starttime, null, null, null, channelname, null));
				
				//print("Adding item....."); // Print on console
			}
		}
		
		print("Parsing done...");
	}
	
	public List<TVItem> getItems() {
		return items;
	}

	public Set<TVNetwork> getNetworks() {
		return networks;
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

}
