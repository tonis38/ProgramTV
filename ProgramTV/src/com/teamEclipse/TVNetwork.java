package com.teamEclipse;

/**
 * 
 * @author zura
 * 
 * <code>TVNetwork</code> stores basic info about TV network / station.
 *
 */

class TVNetwork {
	private int ID;
	private String name;
	private String country;
	private String logo;
	
	public TVNetwork() {ID = 0; name = null; country = null; logo = null;}
	
	public TVNetwork(int ID, String name, String country, String logo) {
		this.ID = ID;
		this.name = name;
		this.country = country;
		this.logo = logo;
	}
	
	public void setID(int ID) {this.ID = ID;}
	public void setName(String name) {this.name = name;}
	public void setCountry(String country) {this.country = country;}
	public void setLogo(String logo) {this.logo = logo;}
	
	
	public int getID() {return this.ID;}
	public String getName() {return this.name;}
	public String getCountry() {return this.country;}
	public String getLogo() {return this.logo;}
	
}
