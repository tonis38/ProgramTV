package com.teamEclipse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
		List<List<String>> values = new ArrayList<List<String>>();

		for (int i = 0; i < columnNames.length; i++) {
			List<String> networkItems = new ArrayList<String>();
			for (TVItem item : itemsList) {
				if(item.getNetwork().equals(columnNames[i])) {
					networkItems.add(item.getAirDate().substring(11, 16) + " - " +  item.getName());
				}
			}
			values.add(networkItems);
		}
		
		int longest = 0;
		for (List<String> list : values)
			if (list.size() > longest)
				longest = list.size();
		
		for (List<String> list : values) {
			int l = longest - list.size();
			for (int i = 0; i < l; i++) {
				list.add("");
			}
		}
		
		tableModel.setData(values, columnNames);
		tableModel.fireTableDataChanged();
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