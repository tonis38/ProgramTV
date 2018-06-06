package com.teamEclipse;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import com.parser.*;

public class TVFrame extends JFrame{
	public TVFrame() {
		add(new TVPanel());
		pack();
	}
}

class TVPanel extends JPanel{
	private JPanel menuPanel;
	private List<TVItem> itemsList;
	
	public TVPanel() {
		setLayout(new BorderLayout());
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(4,1));
		ActionListener synchronize = new SynchronizeDatabaseEvent();
		addMenuButton("Synchronizuj", synchronize);
		addMenuButton("Dzisiaj", null);
		addMenuButton("Szukaj", null);
		addMenuButton("Pomoc", null);
		
		add(menuPanel, BorderLayout.WEST);
		
		InitVariables();
		
		CPParser cp = new CPParser();
		ParserDBConnect export = new ParserDBConnect();
		try {
			export.ExportToDB(cp.ParseData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TVDatabase db = new TVDatabase();
		itemsList = db.selectTVItems();
		
		add(CreateTable(), BorderLayout.CENTER);
	}
	private void InitVariables() {
		itemsList = new LinkedList<TVItem>();
	}
	
	private void addMenuButton(String label, ActionListener listener) {
		JButton button = new JButton(label);
		button.addActionListener(listener);
		menuPanel.add(button);
	}
	
	private JTable CreateTable() {
		String [] columnNames = { "TVP 1", "TVP 2", "TV 4", "POLSAT", "POLSAT News"};
		List<List<String>> values = new ArrayList<List<String>>();

		for (int i = 0; i < columnNames.length; i++) {
			List<String> networkItems = new ArrayList<String>();
			for (TVItem item : itemsList) {
				if(item.getNetwork().equals(columnNames[i])) {
					networkItems.add(item.getName());
				}
			}
			values.add(networkItems);
		}
		int longest = 0;
		for (List<String> list : values)
			if (list.size() > longest)
				longest = list.size();
		for (List<String> list : values)
			for (int i = 0; i < longest - list.size(); i++)
				list.add("");
	
		return new JTable(new MyTableModel(values, columnNames));
	}
	
	class SynchronizeDatabaseEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			CPParser cp = new CPParser();
			ParserDBConnect export = new ParserDBConnect();
			try {
				export.ExportToDB(cp.ParseData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TVDatabase db = new TVDatabase();
			itemsList = db.selectTVItems();
		}
	}
	
	class MyTableModel extends AbstractTableModel {
	    private List<List<String>> data;
	    private String[] columnNames;
	    public MyTableModel(List<List<String>> data, String[] columnNames) {
	        this.data = data;
	        this.columnNames = columnNames;
	    }
	    @Override
	    public int getRowCount() {
	        return data.size() + 1;
	    }
	    @Override
	    public int getColumnCount() {
	        return columnNames.length;
	    }
	    @Override
	    public Object getValueAt(int row, int column) {
	    	if(row == 0)
	    		return columnNames[column];
	    	
	        return data.get(row-1).get(column);
	    }
	    // optional
	    @Override
	    public void setValueAt(Object aValue, int row, int column) {
	        data.get(row).set(column, (String) aValue);
	    }
	}
}
