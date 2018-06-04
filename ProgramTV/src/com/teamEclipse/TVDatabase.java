package com.teamEclipse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.EnumSet;

import com.teamEclipse.TVItem;
import com.teamEclipse.TVNetwork;
import com.teamEclipse.TVShow;
import com.teamEclipse.TVShow.*;

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
            e.printStackTrace();
    	}
    	
    	try {
    		conn = DriverManager.getConnection(DB_URL);
    		stat = conn.createStatement();
    	} catch(SQLException e) {
            System.err.println("Can't connect to database!");
            e.printStackTrace();
    	}
    	
    	createTables();
    }
	
    private boolean createTables() {
    	try {
    		stat.execute("CREATE TABLE IF NOT EXISTS tvitems(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), season INT, number INT, airDate VARCHAR(10), runtime INT, image VARCHAR(255), summary VARCHAR(1023), tvshow INT)");
    		stat.execute("CREATE TABLE IF NOT EXISTS tvnetworks(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), country VARCHAR(255), logo VARCHAR(255))");
    		stat.execute("CREATE TABLE IF NOT EXISTS tvshows(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), language VARCHAR(255), types BLOB, status VARCHAR(255), runtime INT, premiered VARCHAR(10), officialsite VARCHAR(255), summary VARCHAR(1023), airtime VARCHAR(10), days INT, rating BLOB, image VARCHAR(255), network VARCHAR(255)");
    	} catch (SQLException e) {
            System.err.println("Error while creating tables");
            e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    
    public boolean insertItems(TVItem item) {
    	try {
    		PreparedStatement prepStmt = conn.prepareStatement(
    				"insert into tvitems values (NULL, ?, ?, ?, ?, ?, ?, ?, ?);");
    		prepStmt.setString(1, item.getName());
    		prepStmt.setInt(2, item.getSeason());
    		prepStmt.setInt(3, item.getNumber());
    		prepStmt.setString(4, item.getAirDate());
    		prepStmt.setInt(5, item.getRuntime());
    		prepStmt.setString(6, item.getImage());
    		prepStmt.setString(7, item.getSummary());
    		prepStmt.setInt(8, item.getShow().getID());
    		prepStmt.execute();
    	} catch (SQLException e) {
            System.err.println("Error while inserting TVItem");
            e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    public boolean insertNetworks(TVNetwork network) {
    	try {
    		PreparedStatement prepStmt = conn.prepareStatement(
    				"insert into tvnetworks values (NULL, ?, ?, ?);");
    		prepStmt.setString(1, network.getName());
    		prepStmt.setString(2, network.getCountry());
    		prepStmt.setString(3, network.getLogo());
    		prepStmt.execute();
    	} catch (SQLException e) {
            System.err.println("Error while inserting TVNetwork");
            e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
    public boolean insertShows(TVShow show) {
    	try {
    		PreparedStatement prepStmt = conn.prepareStatement(
    				"insert into tvshows values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
    		prepStmt.setString(1, show.getName());
    		prepStmt.setString(2, show.getLanguage());
    		prepStmt.setInt(3, show.getType().getValue());
    		prepStmt.setInt(4, show.getStatus().getValue());
    		prepStmt.setInt(5, show.getRuntime());
    		prepStmt.setString(6, show.getPremiereDate());
    		prepStmt.setString(7, show.getOfficialSite());
    		prepStmt.setString(8, show.getSummary());
    		prepStmt.setString(9, show.getAirtime());
    		prepStmt.setInt(10, show.getDaysN());
    		prepStmt.setDouble(11, show.getRating());
    		prepStmt.setString(12, show.getImage());
    		prepStmt.setString(13, show.getNetwork());
    	} catch (SQLException e) {
            System.err.println("Error while inserting TVShow");
            e.printStackTrace();
            return false;
        }
    	
    	return true;
    }
}
