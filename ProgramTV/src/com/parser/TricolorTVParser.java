package com.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TricolorTVParser {

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

  public static void main(String[] args) throws IOException, JSONException {
    JSONObject json = readJsonFromUrl("https://www.tricolor.tv/ajax/program/grid.php?channel=select&channel-type=20&channel-name=0&channel-format=0&date_for_now=&day=&type=grid&favoriteChannels=undefined");
    JSONObject progJson = json.getJSONObject("program");

    Iterator<String> keys = progJson.keys();
    
    //Get all objects
    while(keys.hasNext()){
    	
    	String key = keys.next();							//Get key
    	
    	JSONObject program = progJson.getJSONObject(key);	//Get object from key
    	
    	//Get data from object
    	String channel = program.getString("title");
    	String time = program.getString("start");
    	
    	
    	System.out.printf("Program name: %s. \t Start: %s.\n", channel, time);
    }

    
  }
}