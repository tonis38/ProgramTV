package com.parser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.teamEclipse.*;

public class OnetParser {

	private static List<TVItem> items = new LinkedList<TVItem>(); // Create new list for TVItems

	public List<TVItem> ParseData() {

		String url = null;

		for (int i = -1; i <= 1; i++) {
			for (int j = 1; j <= 1; j++) {
				if (i < 0 || i == 0) {
					url = "https://programtv.onet.pl/?dzien=" + i + "&strona=" + j;
				} else if (i > 0) {
					url = "https://programtv.onet.pl/?dzien=+" + i + "&strona=" + j;
				} else {
					return null;
				}

				Document doc = null;
				try {
					print("Fetching %s...", url);
					doc = Jsoup.connect(url).get();
				} catch (IOException e) {
					System.err.printf("Connection error: cannot connect to %s. ", url); // Cannot connect to site, throw
																						// error
					e.printStackTrace();
					return null; // If cannot connect, return null list.
				}
				Elements channels = doc.select("#emissions > div.inner > div.boxTVHolder"); // Extract channels from
				
				for (Element channel : channels) {
					Elements programs = channel.select("li"); // Get all visible TV programs

					for (Element program : programs) {
						String channelname = channel.select("span.tvName").text(); // Get channel name
						String programname = program.select("span.title").text().replace("\"", "'"); // Get program name
						String starttime = program.select("span.hour").text(); // Get program start time
						String description = program.select("span.type").text(); // Get description

						// Get date
						LocalDate date;
						date = LocalDate.now().plusDays(i);
						String progdate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

						DateFormat dateFormat = new SimpleDateFormat("HH:mm");
						Date time = null;
						try {
							time = dateFormat.parse(starttime);
						} catch (ParseException e) {
							System.err.printf("Cannot parse time...");
							// e.printStackTrace();
						}

						starttime = dateFormat.format(time);

						TVItem item = new TVItem(0, programname, 0, 0, progdate + " " + starttime, null, null,
								description, channelname, null);
						items.add(item);
						 print("Adding - Channel name: %s. Program name: %s. Time: %s %s. Type: %s.",
						 channelname, programname, progdate, starttime, description);

					}
				}
			}
		}

		print("Parsing done....");
		return items; // Return list of TVItems

	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

}
