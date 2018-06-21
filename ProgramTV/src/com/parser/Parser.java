package com.parser;

import java.util.List;
import java.util.Set;

import com.teamEclipse.TVItem;
import com.teamEclipse.TVNetwork;

public abstract class Parser {

	public abstract void ParseData();
	public abstract List<TVItem> getItems();
	public abstract List<TVNetwork> getNetworks();
	
}
