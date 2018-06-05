package com.parser;

import java.util.List;

import com.teamEclipse.*;

public class ParserDBConnect {
	
	public void ExportToDB(List<TVItem> items) {
		List<TVItem> list = items;
		TVDatabase database = new TVDatabase();
				
		for(int i = 0 ; i < list.size(); i++) {
			database.insertItems(list.get(i));			
		}
	}

}
