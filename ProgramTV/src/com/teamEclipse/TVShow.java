package com.teamEclipse;

import java.util.*;

/**
 * 
 * @author zura
 * 
 * <code>TVShow</code> is a class that stores full information about a TV show, drama, movie and others.
 * This class stores every info that does not fit into TVItem class. 
 * 
 * TBD
 *
 */

class TVShow {
	public static enum StatusList { UNKNOWN, FINISHED, RUNNING, NOT_YET_AIRED};
	public static enum Day {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
	public static enum Type { OTHER, COMEDY, SCI_FI, HORROR, ROMANCE, ACTION, THRILLER, DRAMA, MYSTERY, CRIME, ANIMATION, 
						ADVENTURE, FANTASY, COMEDY_ROMANCE, ACTION_COMEDY, SUPERHERO,  TALK_SHOW, NEWS, SPORTS, REALITY};
	
	private int ID;
	private String name;
	private String language;
	private EnumSet<Type> types;
	private StatusList status;
	private int runtime;
	private Date premiered;
	private String officialSite;
	private String summary;
	
	private Date airtime;
	private EnumSet<Day> days;
	
	private double rating;
	private String image;
	private TVNetwork network;
	
	public TVShow() {
		this.ID = 0;
		this.name = null;
		this.language = null;
		this.types = null;
		this.status = StatusList.UNKNOWN;
		this.runtime = 0;
		this.premiered = null;
		this.officialSite = null;
		this.summary = null;
		this.airtime = null;
		this.days = null;
		this.rating = 0;
		this.image = null;
		this.network = null;
	}
	
	public void setID(int ID) {this.ID = ID;}
	public void setName(String name) {this.name = name;}
	public void setLanguage(String language) {this.language = language;}
	public void addType(Type type) {this.types.add(type);}
	public void setStatus(StatusList status) { this.status = status;}
	public void setRuntime(int runtime) {this.runtime = runtime;}
	public void setPremiereDate(Date premiered) {this.premiered = premiered;}
	public void setOfficialSite(String site) {this.officialSite = site;}
	public void setSummary(String summary) {this.summary = summary;}
	public void setAirtime(Date airtime) {this.airtime = airtime;}
	public void addDay(Day day) {this.days.add(day);}
	public void setRating(double rating) {this.rating = rating;}
	public void setImage(String image) {this.image = image;}
	public void setNetwork(TVNetwork network) {this.network = network;}
	
	public int getID() {return this.ID;}
	public String getName() {return this.name;}
	public String getLanguage() {return this.language;}
	public EnumSet<Type> getTypes() {return this.types;}
	public StatusList getStatus() {return this.status;}
	public int getRuntime() {return this.runtime;}
	public Date getPremiereDate() {return this.premiered;}
	public String getOfficialSite() {return this.officialSite;}
	public String getSummary() {return this.summary;}
	public Date getAirtime() {return this.airtime;}
	public EnumSet<Day> getDays() {return this.days;}
	public double getRating() {return this.rating;}
	public String getImage() {return this.image;}
	public TVNetwork getNetwork() {return this.network;}
	
	public void removeDay(Day day) {this.days.remove(day);}
	public void removeType(Type type) {this.types.remove(type);}
	

}

