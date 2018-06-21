package com.parser;

import java.util.List;
import java.util.Set;

import com.teamEclipse.TVItem;
import com.teamEclipse.TVNetwork;

public abstract class Parser {

	abstract void ParseData();
	abstract List<TVItem> getItems();
	abstract List<TVNetwork> getNetworks();
	
}
