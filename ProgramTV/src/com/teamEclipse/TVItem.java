package com.teamEclipse;


/**
 * 
 * @author zura
 * 
 * <code>TVItem</code> holds info about a single item in schedule.
 * This object stores only information specific this particular entry including name, air date, runtime and season, episode number, 
 * summary if this entry is a part of a series. Every other info is a part of TVShow class.
 * 
 * TBD
 *
 */

public class TVItem {
	private int ID;
	private String name;
	private int season;
	private int number;
	private String airDate;
	private int runtime;
	private String image;
	private String summary;
	private String network;
	private String show;
	
	
	
	public TVItem(){
		this.ID = 0;
		this.name = null;
		this.season = 0;
		this.number = 0;
		this.airDate = null;
		this.runtime = 0;
		this.image = null;
		this.summary = null;
		this.network = null;
		this.show = null;
	}
	
	public TVItem(int ID, String name, int season, int number, String airDate, int runtime, String image, String summary, String network, String show) {
		this.ID = ID;
		this.name = name;
		this.season = season;
		this.number = number;
		this.airDate = airDate;
		this.runtime = runtime;
		this.image = image;
		this.summary = summary;
		this.network = network;
		this.show = show;
	}
	
	public void setID(int ID) {this.ID = ID;}
	public void setName(String name) {this.name = name;}
	public void setSeason(int season) {this.season = season;}
	public void setNumber(int number) {this.number = number;}
	public void setAirDate(String airDate) {this.airDate = airDate;}
	public void setRuntime(int runtime) {this.runtime = runtime;}
	public void setImage(String image) {this.image = image;}
	public void setSummary(String summary) {this.summary = summary;}
	public void setNetwork(String network) {this.network = network;}
	public void setShow(String show) {this.show = show;}
	

	public int getID() {return this.ID;}
	public String getName() {return this.name;}
	public int getSeason() {return this.season;}
	public int getNumber() {return this.number;}
	public String getAirDate() {return this.airDate;}
	public int getRuntime() {return this.runtime;}
	public String getImage() {return this.image;}
	public String getSummary() {return this.summary;}
	public String getNetwork() {return this.network;}
	public String getShow() {return this.show;} 
	
}