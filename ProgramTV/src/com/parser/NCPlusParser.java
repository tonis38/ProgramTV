package com.parser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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

public class NCPlusParser extends Parser {

	private List<TVItem> items;
	private Set<TVNetwork> networks;

	public NCPlusParser() {
		items = new LinkedList<TVItem>();
		networks = new HashSet<TVNetwork>();
	}

	public void ParseData() {
		String url = "https://ncplus.pl/program-tv";
		print("Fetching %s...", url);

		Document doc = null;
		try {

			doc = Jsoup.connect(url).get();
		} catch (IOException e1) {
			System.err.printf("Connection error: cannot connect to %s. ", url); // Cannot connect to site, throw error
			e1.printStackTrace();
			return;
		}

		Elements channels = doc.select("#programtvfull > div.clearfix"); // Extract channels from site
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();

		for (Element channel : channels) {
			String channelname = channel.select("a[href].name-link").text(); // Get channel name
			networks.add(new TVNetwork(0, channel.select("a[href].name-link").text(), null, null)); // Add
																									// channel/network
																									// name to networks
																									// set
			Elements programs = channel.select("a[href = #]"); // Select all visible TV programs

			for (Element program : programs) {
				String programname = program.select("strong").text().replaceAll("\"", "'"); // Get program name
				String time = program.select("div.time-age > span").text(); // Get program airing time

				int a = programname.indexOf("odc"); // Extract part after ','
				int b = time.indexOf('-');
				String starttime = time.substring(0, b - 1);
				String endtime = time.substring(b + 1);
				String description = " ";
				String airdate = dateFormat.format(date);

				dateFormat = new SimpleDateFormat("HH:mm");
				Date begin = null;
				Date end = null;
				try {
					begin = dateFormat.parse(starttime);
					end = dateFormat.parse(endtime);
				} catch (ParseException e) {
					System.err.printf("Cannot parse airtime from start - end time.");
					e.printStackTrace();
				}

				String runtime = dateFormat.format(end.getTime() - begin.getTime());

				if (a > 1) {
					description = programname.substring(a);
					programname = programname.substring(0, a - 2);
				}

				// Fill item with data
				// print("Adding new item: Channel name: %s. Program name: %s. Time: %s %s.",
				// channelname, programname,
				// starttime, airdate);
				items.add(new TVItem(0, programname, 0, 0, airdate + " " + starttime, runtime, null, description,
						channelname, null));
			}
		}

		print("Parsing done.... \n");
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