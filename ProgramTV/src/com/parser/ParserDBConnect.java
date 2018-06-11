package com.parser;

import java.util.List;
import java.util.Set;

import com.teamEclipse.*;

public class ParserDBConnect {
	private TVDatabase database;
	
	public ParserDBConnect() {
		database = new TVDatabase();
	}
	
	public void ExportItems(List<TVItem> items) {
				
		try {
			database.insertItems(items);
		}
		catch(NullPointerException e){
			System.err.printf("Null input list. Cannot connect to parsed site or error in parsing data.");
	        e.printStackTrace();
		}
	}
	public void ExportNetworks(Set<TVNetwork> networks) {
		
		try {
			database.insertNetworks(networks);
		}
		catch(NullPointerException e){
			System.err.printf("Null input list. Cannot connect to parsed site or error in parsing data.");
	        e.printStackTrace();
		}
	}

}
