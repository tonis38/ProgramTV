package com.teamEclipse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class TVScheduleTable extends JTable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_WIDTH = 250;
	private TVTableModel tableModel;
	
	public TVScheduleTable() {
		super();
		tableModel = new TVTableModel();
		this.setModel(tableModel);
		this.setDefaultRenderer(String.class, new TVTableCellRenderer());
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setTableHeader(null);
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
					networkItems.put( (timeToMinutes(airTime) - 180) % 1440, item );
				}
			}
			values.add(networkItems);
		}
		
		LocalDateTime ldt = LocalDateTime.now();
		int currentTime = (timeToMinutes(DateTimeFormatter.ofPattern("HH:mm").format(ldt)) - 180 ) % 1440;
		String today = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(ldt);

		
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
					if (!foundCurrentShow && (nextKey == null || (nextKey > currentTime && entry.getKey() <= currentTime)) && item.getAirDate().substring(0, 10).equals(today)) {
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
		for (List<TVCellData> list : data) {
			while (list.size() < 30)
				list.add(new TVCellData());
		}
			
		
		tableModel.setData(data, columnNames);
		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumnModel().getColumn(i).setPreferredWidth(COLUMN_WIDTH);
		}
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
		private static final int HEADER_HEIGHT = 30;
		private static final int ROW_HEIGHT = 20;
		private Color headerBackground;
		private Color timeBackground;
		private Color defaultColor;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			defaultColor = table.getBackground();
			if ( (defaultColor.getRed()*0.299 + defaultColor.getGreen()*0.587 + defaultColor.getBlue()*0.114)/255 < 0.5 ) {	//Background is dark
				headerBackground = table.getBackground().brighter().brighter();
				timeBackground = table.getBackground().brighter();
			}else {	//Background is light
				headerBackground = table.getBackground().darker().darker();
				timeBackground = table.getBackground().darker();
			}
			
			Font defaultFont = new Font(new JLabel().getFont().getFontName(), Font.PLAIN, 14);
			Font headerFont = new Font(new JLabel().getFont().getFontName(), Font.PLAIN, 22);

			if(column == 0) table.setRowHeight(row, ROW_HEIGHT);
			if (row == 0) {
				if(column == 0) table.setRowHeight(0, HEADER_HEIGHT);
				
				JPanel panel = new JPanel();
				panel.setOpaque(true);
				panel.setBackground(headerBackground);
				panel.setLayout(new BorderLayout());
				
				JLabel label = new SmoothLabel((String) value);
				label.setHorizontalAlignment(row == 0 ? JLabel.CENTER : JLabel.LEFT);
				label.setFont(headerFont);
				panel.add(label, BorderLayout.CENTER);
				panel.setBorder(new MatteBorder(0,1,1,0, table.getGridColor()));
				return panel;
			}
			TVCellData data = (TVCellData) value;
			
			JPanel panel = new JPanel();
			panel.setBorder(new MatteBorder(0,1,0,0, table.getGridColor()));
			panel.setOpaque(true);
		    //panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			panel.setLayout(new BorderLayout());
			
			JLabel timeText = new SmoothLabel(" " + data.getTime() + " ");
			timeText.setFont(defaultFont);
			if (data.getName().equals(""))
				timeText.setForeground(timeBackground);
			else
				timeText.setBorder(new MatteBorder(1,0,0,0, table.getGridColor()));

			timeText.setHorizontalAlignment(JLabel.LEFT);
			timeText.setBackground(timeBackground);
			timeText.setOpaque(true);
			panel.add(timeText, BorderLayout.WEST);
			
			JLabel text = new SmoothLabel(" " + data.getName());
			text.setFont(defaultFont);
			if (!data.getName().equals("")) text.setBorder(new MatteBorder(1,0,0,0, table.getGridColor()));
			if (data.isRunning())
				text.setBorder(new MatteBorder(1,1,1,1, Color.RED));
			
			text.setOpaque(true);
			text.setHorizontalAlignment(JLabel.LEFT);
			panel.add(text, BorderLayout.CENTER);
			setShowGrid(false);
			if (!data.getName().equals("")) panel.setToolTipText(data.getName());
			
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

	class SmoothLabel extends JLabel {

	    public SmoothLabel(String text) {
	        super(text);
	    }

	    public void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	        super.paintComponent(g2d);
	    }
	}

}