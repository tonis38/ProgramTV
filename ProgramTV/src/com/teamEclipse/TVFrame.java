package com.teamEclipse;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
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
	private TVScheduleTable table;
	
	public TVPanel() {
		setLayout(new BorderLayout());
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(20,1));
		ActionListener synchronize = new SynchronizeDatabaseEvent();
		addMenuButton("Synchronizuj", synchronize);
		addMenuButton("Dzisiaj", null);
		addMenuButton("Szukaj", null);
		addMenuButton("Pomoc", null);
		
		add(menuPanel, BorderLayout.WEST);
		
		InitVariables();
		
		LoadDBData();

		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
	}
	private void InitVariables() {
		itemsList = new LinkedList<TVItem>();
		table = new TVScheduleTable();
	}
	
	private void addMenuButton(String label, ActionListener listener) {
		JButton button = new JButton(label);
		button.addActionListener(listener);
		menuPanel.add(button);
	}
	
	private void LoadDBData() {
		TVDatabase db = new TVDatabase();
		itemsList = db.selectTVItems();

		table.UpdateTable(itemsList);
	}
	
	class SynchronizeDatabaseEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			CPParser cp = new CPParser();
			ParserDBConnect export = new ParserDBConnect();
			export.ExportToDB(cp.ParseData());
			
			LoadDBData();
		}
	}
}
