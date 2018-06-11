package com.teamEclipse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.teamEclipse.TVItem;
import com.teamEclipse.TVNetwork;
import com.teamEclipse.TVShow;
import com.teamEclipse.TVShow.Day;
import com.teamEclipse.TVShow.Status;
import com.teamEclipse.TVShow.Type;

public class TVDatabase {

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:TVDatabase.db";

    private Connection conn;
    private Statement stat;
    
    public TVDatabase() {
    	try {
    		Class.forName(TVDatabase.DRIVER);
    	}catch(ClassNotFoundException e) {
            System.err.println("No JDBC driver");
            //e.printStackTrace();
    	}
    	
    	try {
    		conn = DriverManager.getConnection(DB_URL);
    		stat = conn.createStatement();
    		conn.setAutoCommit(false);
    	} catch(SQLException e) {
            System.err.println("Can't connect to database!");
            //e.printStackTrace();
    	}
    	
    	createTables();
    }
    
    public boolean clearTables() {
    	try {
    		stat.execute("drop TABLE tvitems");
    		stat.execute("drop TABLE tvshows");
    		stat.execute("drop TABLE tvnetworks");
    	} catch (SQLException e) {
            System.err.println("Error while creating tables");
            //e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
	
    private boolean createTables() {
    	try {
    		stat.execute("CREATE TABLE IF NOT EXISTS tvitems(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), season INT, number INT, airDate VARCHAR(10), runtime VARCHAR (255), image VARCHAR(255), summary VARCHAR(1023), network VARCHAR(255), tvshow VARCHAR(255))");
    		stat.execute("CREATE TABLE IF NOT EXISTS tvnetworks(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), country VARCHAR(255), logo VARCHAR(255))");
    		stat.execute("CREATE TABLE IF NOT EXISTS tvshows(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), language VARCHAR(255), types BLOB, status VARCHAR(255), runtime INT, premiered VARCHAR(10), officialsite VARCHAR(255), summary VARCHAR(1023), airtime VARCHAR(10), days INT, rating BLOB, image VARCHAR(255), network VARCHAR(255))");
    	} catch (SQLException e) {
            System.err.println("Error while creating tables");
            //e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    
    public boolean insertItems(List<TVItem> items) {
    	try {
    		PreparedStatement prepStmt = conn.prepareStatement(
    				"insert into tvitems values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
    		for (TVItem item : items) {
	        	if (checkExist("tvitems", "name", item.getName()))
	            	if (checkExist("tvitems", "airDate", item.getAirDate()))
	        			continue;
	        	
	    		prepStmt.setString(1, item.getName());
	    		prepStmt.setInt(2, item.getSeason());
	    		prepStmt.setInt(3, item.getNumber());
	    		prepStmt.setString(4, item.getAirDate());
	    		prepStmt.setString(5, item.getRuntime());
	    		prepStmt.setString(6, item.getImage());
	    		prepStmt.setString(7, item.getSummary());
	    		prepStmt.setString(8, item.getNetwork());
	    		prepStmt.setString(9, item.getShow());
	    		prepStmt.addBatch();
    		}
	    	prepStmt.executeBatch();
	    	conn.commit();
    	} catch (SQLException e) {
            System.err.println("Error while inserting TVItem");
            //e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    public boolean insertNetworks(Set<TVNetwork> networks) {
    	try {
	    	PreparedStatement prepStmt = conn.prepareStatement(
	    				"insert into tvnetworks values (NULL, ?, ?, ?);");

	    	for (TVNetwork network : networks) {
        		if (checkExist("tvnetworks", "name", network.getName()))
        			continue;
        		
	    		prepStmt.setString(1, network.getName());
	    		prepStmt.setString(2, network.getCountry());
	    		prepStmt.setString(3, network.getLogo());
	    		prepStmt.addBatch();
	    	}
    		prepStmt.executeBatch();
    		conn.commit();
    	} catch (SQLException e) {
            System.err.println("Error while inserting TVNetwork");
            e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    public boolean insertShows(List<TVShow> shows) {
    	try {
    		PreparedStatement prepStmt = conn.prepareStatement(
    				"insert into tvshows values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
    		
    		for (TVShow show : shows) {
        		if (checkExist("tvshows", "name", show.getName()))
        			if (checkExist("tvshows", "network", show.getNetwork()))
            			continue;
	    		prepStmt.setString(1, show.getName());
	    		prepStmt.setString(2, show.getLanguage());
	    		prepStmt.setInt(3, show.getType().getValue());
	    		prepStmt.setInt(4, show.getStatus().getValue());
	    		prepStmt.setInt(5, show.getRuntime());
	    		prepStmt.setString(6, show.getPremiereDate());
	    		prepStmt.setString(7, show.getOfficialSite());
	    		prepStmt.setString(8, show.getSummary());
	    		prepStmt.setString(9, show.getAirtime());
	    		prepStmt.setInt(10, show.daysToNumber());
	    		prepStmt.setDouble(11, show.getRating());
	    		prepStmt.setString(12, show.getImage());
	    		prepStmt.setString(13, show.getNetwork());
	    		prepStmt.addBatch();
    		}
    		prepStmt.executeBatch();
    		conn.commit();
    	} catch (SQLException e) {
            System.err.println("Error while inserting TVShow");
            //e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    
    public List<TVItem> selectTVItems(){
    	List<TVItem> items = new LinkedList<TVItem>();
    	
    	try {
    		ResultSet result = stat.executeQuery("SELECT * FROM tvitems");
    		int ID;
    		String name;
    		int season;
    		int number;
    		String airDate;
    		String runtime;
    		String image;
    		String summary;
    		String network;
    		String show;
    		while(result.next()) {
    			ID = result.getInt("id");
    			name = result.getString("name");
    			season = result.getInt("season");
    			number = result.getInt("number");
    			airDate = result.getString("airDate");
    			runtime = result.getString("runtime");
    			image = result.getString("image");
    			summary = result.getString("summary");
    			network = result.getString("network");
    			show = result.getString("tvshow");
        		items.add(new TVItem(ID, name, season, number, airDate, runtime, image, summary, network, show));
    		}
    	} catch (SQLException e) {
    		System.err.println("Error while selecting TVItems!");
            //e.printStackTrace();
            return null;
        }
    	return items;
    }
    public List<TVNetwork> selectTVNetworks(){
    	List<TVNetwork> networks = new LinkedList<TVNetwork>();
    	
    	try {
    		ResultSet result = stat.executeQuery("SELECT * FROM tvnetworks");
    		int ID;
    		String name;
    		String country;
    		String logo;
    		while(result.next()) {
    			ID = result.getInt("id");
    			name = result.getString("name");
    			country = result.getString("country");
    			logo = result.getString("logo");
    			networks.add(new TVNetwork(ID, name, country, logo));
    		}
    		
    	} catch (SQLException e) {
    		System.err.println("Error while selecting TVNetworks!");
            //e.printStackTrace();
            return null;
        }
    	
    	return networks;
    }
    public List<TVShow> selectTVShows(){
    	List<TVShow> shows = new LinkedList<TVShow>();
    	
    	try {
    		ResultSet result = stat.executeQuery("SELECT * FROM tvnetworks");
        	int ID;
        	String name;
        	String language;
        	Type type;
        	Status status;
        	int runtime;
        	String premiered;
        	String officialSite;
        	String summary;
        	String airtime;
        	EnumSet<Day> days;
        	double rating;
        	String image;
        	String network;
        	
        	while(result.next()) {
    			ID = result.getInt("id");
    			name = result.getString("name");
        		language = result.getString("language");
        		type = TVShow.Type.fromInteger(result.getInt("type"));
        		status = TVShow.Status.fromInteger(result.getInt("status"));
        		runtime = result.getInt("runtime");
        		premiered = result.getString("premiered");
        		officialSite = result.getString("officialsite");
        		summary = result.getString("summary");
        		airtime = result.getString("airtime");
        		days = new TVShow().numberToDays(result.getInt("days"));
        		rating = result.getDouble("rating");
        		image = result.getString("image");
        		network = result.getString("network");
        		shows.add(new TVShow(ID, name, language, type, status, runtime, premiered, officialSite, summary, airtime, days, rating, image, network));
        	}
    	} catch (SQLException e) {
    		System.err.println("Error while selecting TVShows!");
            //e.printStackTrace();
            return null;
        }
    	
    	return shows;
    }
    private boolean checkExist(String tableName, String column, String value) {
    	ResultSet result;
    	try {
    		result = stat.executeQuery("SELECT * FROM " + tableName + " WHERE " + column + "=\"" + value + "\"");
    		if (!result.next())
    			return false;
    	} catch (SQLException e) {
    		System.err.println("Error while searching for existing records!");
    		System.err.println("\t" + "SELECT * FROM " + tableName + " WHERE " + column + "=\"" + value + "\"");
            //e.printStackTrace();
            return true;
        }
    	
    	return true;
    }
}
