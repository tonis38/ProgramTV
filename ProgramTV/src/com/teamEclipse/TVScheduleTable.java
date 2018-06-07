package com.teamEclipse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class TVScheduleTable extends JTable{
	private TVTableModel tableModel;
	
	public TVScheduleTable() {
		super();
		tableModel = new TVTableModel();
		this.setModel(tableModel);
	}
	
	public void UpdateTable(List<TVItem> itemsList) {
		String [] columnNames = { "TVP 1", "TVP 2", "TV 4", "POLSAT", "POLSAT News"};
		List<List<String>> data = new ArrayList<List<String>>();
		List<TreeMap<Integer, TVItem>> values = new ArrayList<TreeMap<Integer,TVItem>>();
		String airTime = "";

		//Sort items by station and air time
		for (int i = 0; i < columnNames.length; i++) {
			TreeMap<Integer, TVItem> networkItems = new TreeMap<Integer, TVItem>();
			for (TVItem item : itemsList) {
				if(item.getNetwork().equals(columnNames[i])) {
					airTime = item.getAirDate().substring(11, 16);
					networkItems.put( timeToMinutes(airTime) , item);
				}
			}
			values.add(networkItems);
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:MM");
		LocalDateTime ldt = LocalDateTime.now();
		int currentTime = timeToMinutes(formatter.format(ldt));

		
		for(int i = 0; i < columnNames.length; i++)
			data.add(new ArrayList<String>());
		
		boolean foundCurrentShow;
		int network;
		for (int hour = 0; hour < 24; hour++) {
			network = 0;
			for (TreeMap<Integer, TVItem> map : values) {
				foundCurrentShow = false;
				for (Map.Entry<Integer, TVItem> entry : map.entrySet()) {
					TVItem item = entry.getValue();
					Integer nextKey = map.higherKey(entry.getKey());
					
					//If next entry's air time is higher than currentTime that means that this entry is running right now
					if (!foundCurrentShow && nextKey != null && nextKey > currentTime ) {
						if (Integer.parseInt(item.getAirDate().substring(11, 13)) == hour) {
							data.get(network).add("T - " + item.getAirDate().substring(11, 16) + " - " + item.getName());
						}
						foundCurrentShow = true;
					}
					else if (Integer.parseInt(item.getAirDate().substring(11, 13)) == hour) {
						data.get(network).add("      " + item.getAirDate().substring(11, 16) + " - " + item.getName());
					}
				}
				network++;
			}
			int longest = 0;
			for (List<String> list : data)
				if (list.size() > longest)
					longest = list.size();
		
			for (List<String> list :data) {
				int l = longest - list.size();
				for (int j = 0; j < l; j++) {
					list.add("");
				}
			}
		}
		
		tableModel.setData(data, columnNames);
		tableModel.fireTableDataChanged();
	}
	
	private Integer timeToMinutes(String time) {
		return Integer.parseInt(time.substring(0, 2))*60 + Integer.parseInt(time.substring(3));
	}
	
	
	
	class TVTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private List<List<String>> data;
	    private String[] columnNames;
	    public TVTableModel(List<List<String>> data, String[] columnNames) {
	        this.data = data;
	        this.columnNames = columnNames;
	    }
	    public TVTableModel() { 
	    	String [] columnNames = { "", "", "", "", ""};
			List<List<String>> values = new ArrayList<List<String>>();
			for (int i = 0; i < columnNames.length; i++) {
				List<String> networkItems = new ArrayList<String>();
				networkItems.add("");
				values.add(networkItems);
			}
			this.setData(values, columnNames);
			this.fireTableDataChanged();
	    }
	    public void setData(List<List<String>> data, String[] columnNames) {
	        this.data = data;
	        this.columnNames = columnNames;
	    }
	    @Override
	    public int getRowCount() {
	        return data.get(0).size() + 1;
	    }
	    @Override
	    public int getColumnCount() {
	        return columnNames.length;
	    }
	    @Override
	    public Object getValueAt(int row, int column) {
	    	if(row == 0)
	    		return columnNames[column];
	    	
	        return data.get(column).get(row-1);
	    }
	    // optional
	    @Override
	    public void setValueAt(Object aValue, int row, int column) {
	        data.get(row).set(column, (String) aValue);
	    }
	}
}