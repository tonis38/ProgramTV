package com.teamEclipse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class TVScheduleTable extends JTable{
	private TVTableModel tableModel;
	
	public TVScheduleTable() {
		super();
		tableModel = new TVTableModel();
		this.setModel(tableModel);
		this.setDefaultRenderer(String.class, new TVTableCellRenderer());

	}
	
	public void UpdateTable(List<TVItem> itemsList) {
		String [] columnNames = { "TVP 1", "TVP 2", "TV 4", "POLSAT", "POLSAT News"};
		List<List<TVCellData>> data = new ArrayList<List<TVCellData>>();
		List<TreeMap<Integer, TVItem>> values = new ArrayList<TreeMap<Integer,TVItem>>();
		String airTime = "";

		//Sort items by station and air time
		for (int i = 0; i < columnNames.length; i++) {
			TreeMap<Integer, TVItem> networkItems = new TreeMap<Integer, TVItem>();
			for (TVItem item : itemsList) {
				if(item.getNetwork().equals(columnNames[i])) {
					airTime = item.getAirDate().substring(11, 16);
					networkItems.put( timeToMinutes(airTime) , item );
				}
			}
			values.add(networkItems);
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime ldt = LocalDateTime.now();
		int currentTime = timeToMinutes(formatter.format(ldt));

		
		for(int i = 0; i < columnNames.length; i++)
			data.add(new ArrayList<TVCellData>());
		
		boolean foundCurrentShow;
		int column, longest;
		for (int hour = 0; hour < 24; hour++) {
			column = 0;
			for (TreeMap<Integer, TVItem> map : values) {
				foundCurrentShow = false;
				for (Map.Entry<Integer, TVItem> entry : map.entrySet()) {
					TVItem item = entry.getValue();
					Integer nextKey = map.higherKey(entry.getKey());
					
					//If next entry's air time is higher than currentTime that means that this entry is running right now
					if (!foundCurrentShow && nextKey != null && nextKey > currentTime ) {
						if (Integer.parseInt(item.getAirDate().substring(11, 13)) == hour) {
							data.get(column).add(new TVCellData(item.getAirDate().substring(11, 16), item.getName(), true));
						}
						foundCurrentShow = true;
					}
					else if (Integer.parseInt(item.getAirDate().substring(11, 13)) == hour) {
						data.get(column).add(new TVCellData(item.getAirDate().substring(11, 16), item.getName(), false));
					}
				}
				column++;
			}
			
			longest = 0;
			for (List<TVCellData> list : data)
				if (list.size() > longest)
					longest = list.size();

			for (List<TVCellData> list :data) {
				int l = longest - list.size();
				for (int j = 0; j < l; j++) {
					list.add( new TVCellData() );
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
		
		private List<List<TVCellData>> data;
	    private String[] columnNames;
	    
	    @Override
	    public Class<?> getColumnClass(int columnIndex){
	    	return String.class;
	    }
	    
	    public TVTableModel(List<List<TVCellData>> data, String[] columnNames) {
	        this.data = data;
	        this.columnNames = columnNames;
	    }
	    public TVTableModel() { 
	    	String [] columnNames = { "", "", "", "", ""};
			List<List<TVCellData>> values = new ArrayList<List<TVCellData>>();
			for (int i = 0; i < columnNames.length; i++) {
				List<TVCellData> networkItems = new ArrayList<TVCellData>();
				networkItems.add(new TVCellData());
				values.add(networkItems);
			}
			this.setData(values, columnNames);
	    }
	    public void setData(List<List<TVCellData>> data, String[] columnNames) {
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
	        data.get(row).set(column, (TVCellData) aValue);
	    }
	}
	
	class TVTableCellRenderer implements TableCellRenderer{
		//private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (hasFocus) {}
			if (row == 0) {
				JPanel panel = new JPanel();
				panel.setOpaque(true);
				panel.setLayout(new BorderLayout());
				
				JLabel label = new JLabel((String) value);
				label.setHorizontalAlignment(row == 0 ? JLabel.CENTER : JLabel.LEFT);
				panel.add(label, BorderLayout.CENTER);
				return panel;
			}
			TVCellData data = (TVCellData) value;
			
			JPanel panel = new JPanel();
			panel.setOpaque(true);
		    //panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			panel.setLayout(new BorderLayout());
			
			JLabel timeLabel = new JLabel(data.getTime());
			if (data.getName().equals(""))
				timeLabel.setForeground(Color.DARK_GRAY);
			timeLabel.setHorizontalAlignment(JLabel.LEFT);
			timeLabel.setBackground(Color.DARK_GRAY);
			timeLabel.setOpaque(true);
			if (row != 0) panel.add(timeLabel, BorderLayout.WEST);
			
			JLabel label = new JLabel(" " + data.getName());
			if (data.isRunning())
				label.setBackground(Color.GRAY);
			label.setOpaque(true);
			label.setHorizontalAlignment(row == 0 ? JLabel.CENTER : JLabel.LEFT);
			panel.add(label, BorderLayout.CENTER);
			return panel;
		}
	}
	class TVCellData{
		private String time;
		private String name;
		private boolean running;
		public TVCellData() {
			this.time = "00:00";
			this.name = "";
			this.running = false;
		}
		public TVCellData(String time, String name, boolean running) {
			this.time = time;
			this.name = name;
			this.running = running;
		}
		public String getTime() {return time;}
		public String getName() {return name;}
		public boolean isRunning() {return running;}
	}
}