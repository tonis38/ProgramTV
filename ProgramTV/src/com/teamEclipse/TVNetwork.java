package com.teamEclipse;

/**
 * 
 * @author zura
 * 
 * <code>TVNetwork</code> stores basic info about TV network / station.
 *
 */

public class TVNetwork {
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
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TVNetwork))
			return false;
		TVNetwork other = (TVNetwork) obj;
		if (ID != other.ID)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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
