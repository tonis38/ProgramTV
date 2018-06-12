package com.parser;

import com.teamEclipse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class TricolorTVParser extends Parser {

	private List<TVItem> items;
	private Set<TVNetwork> networks;

	public TricolorTVParser() {
		items = new LinkedList<TVItem>();
		networks = new HashSet<TVNetwork>();
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public void ParseData() {
		JSONObject json = null;
		try {
			json = readJsonFromUrl(
					"https://www.tricolor.tv/ajax/program/grid.php?channel=select&channel-type=20&channel-name=0&channel-format=0&date_for_now=&day=&type=grid&favoriteChannels=undefined");
		} catch (JSONException e) {
			System.err.println("Cannot read JSON from site. \n");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			System.err.println("Cannot connect to site.");
			e.printStackTrace();
			return;
		}

		JSONObject progJson = json.getJSONObject("program");

		Iterator<String> keys = progJson.keys();

		// Get all objects
		while (keys.hasNext()) {
			String key = keys.next(); // Get key

			JSONObject program = progJson.getJSONObject(key); // Get object from key

			DateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // Data format form JSON
			DateFormat newformat = new SimpleDateFormat("hh:mm dd-MM-yyyy"); // Data format in database

			Date starttime = null;
			Date endtime = null;

			try {
				starttime = oldformat.parse(program.getString("start"));
				endtime = oldformat.parse(program.getString("end"));
			} catch (JSONException e) {
				System.err.println("Cannot parse JSON file. \n");
				e.printStackTrace();
			} catch (ParseException e) {
				System.err.println("cannot parse date. \n");
				e.printStackTrace();
			}

			String airdate = newformat.format(starttime); // Get start date of program
			long duration = (endtime.getTime() - starttime.getTime()) / (1000 * 60); // Get time duration of program

			String runtime = Long.toString(duration) + " minutes";

			// Add data to item
			String network = program.getString("channelName");
			String name = program.getString("title").replaceAll("&quot;", "'");
			String image = program.getString("logo");
			String summary = program.getString("descr");

			items.add(new TVItem(0, name, 0, 0, airdate, runtime, image, summary, network, null));
			System.out.println("Adding item......");

		}

		System.out.println("Parsing done...");

	}

	public List<TVItem> getItems() {
		return items;
	}

	public Set<TVNetwork> getNetworks() {
		return networks;
	}
}