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
	public static enum Status { UNKNOWN (0), FINISHED(1), RUNNING(2), NOT_YET_AIRED(3);
		private final int index;
		Status(int index) {
	        this.index = index;
	    }
		public int getValue() {
			return this.index;
		}
	}
	public static enum Day { MONDAY(1), TUESDAY(2), WEDNESDAY(4), THURSDAY(8), FRIDAY(16), SATURDAY(32), SUNDAY(64);		
		private final int index;
		Day(int index) {
	        this.index = index;
	    }
		public int getValue() {
			return this.index;
		}
	}
	public static enum Type { OTHER(0), COMEDY(1), SCI_FI(2), HORROR(3), ROMANCE(4), ACTION(5), THRILLER(6), DRAMA(7), MYSTERY(8), CRIME(9), ANIMATION(10), 
		ADVENTURE(11), FANTASY(12), COMEDY_ROMANCE(13), ACTION_COMEDY(14), SUPERHERO(15),  TALK_SHOW(16), NEWS(17), SPORTS(18), REALITY(19);
		private final int index;
		Type(int index){
			this.index = index;
		}
		public int getValue() {
			return this.index;
		}
	}
	
	private int ID;
	private String name;
	private String language;
	private Type type;
	private Status status;
	private int runtime;
	private String premiered;
	private String officialSite;
	private String summary;
	
	private String airtime;
	private EnumSet<Day> days;
	
	private double rating;
	private String image;
	private String network;
	
	public TVShow() {
		this.ID = 0;
		this.name = null;
		this.language = null;
		this.type = null;
		this.status = Status.UNKNOWN;
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
	public void setType(Type type) {this.type = type;}
	public void setStatus(Status status) { this.status = status;}
	public void setRuntime(int runtime) {this.runtime = runtime;}
	public void setPremiereDate(String premiered) {this.premiered = premiered;}
	public void setOfficialSite(String site) {this.officialSite = site;}
	public void setSummary(String summary) {this.summary = summary;}
	public void setAirtime(String airtime) {this.airtime = airtime;}
	public void addDay(Day day) {this.days.add(day);}
	public void setRating(double rating) {this.rating = rating;}
	public void setImage(String image) {this.image = image;}
	public void setNetwork(String network) {this.network = network;}
	
	public int getID() {return this.ID;}
	public String getName() {return this.name;}
	public String getLanguage() {return this.language;}
	public Type getType() {return this.type;}
	public Status getStatus() {return this.status;}
	public int getRuntime() {return this.runtime;}
	public String getPremiereDate() {return this.premiered;}
	public String getOfficialSite() {return this.officialSite;}
	public String getSummary() {return this.summary;}
	public String getAirtime() {return this.airtime;}
	public EnumSet<Day> getDays() {return this.days;}
	public int getDaysN() {
		int value = 0;
	    for (Day day : days) {
	        value |= day.getValue();
	    }
	    return value;
	}
	public double getRating() {return this.rating;}
	public String getImage() {return this.image;}
	public String getNetwork() {return this.network;}
	
	public void removeDay(Day day) {this.days.remove(day);}
	

}

