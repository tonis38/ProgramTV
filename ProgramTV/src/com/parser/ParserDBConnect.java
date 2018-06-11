package com.parser;

import java.util.List;

import com.teamEclipse.*;

public class ParserDBConnect {
	private TVDatabase database;
	
	public ParserDBConnect() {
		database = new TVDatabase();
	}
	
	public void ExportToDB(List<TVItem> items) {
				
		try {
			database.insertItems(items);
		}
		catch(NullPointerException e){
			System.err.printf("Null input list. Cannot connect to parsed site or error in parsing data.");
	        e.printStackTrace();
		}
	}

}
