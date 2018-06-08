package com.teamEclipse;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	LocalDate date;
	
	public TVPanel() {
		setLayout(new BorderLayout());
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(20,1));
		ActionListener synchronize = new SynchronizeDatabaseEvent();
		ActionListener today = new ShowTodayEvent();
		ActionListener yesterday = new ShowYesterdayEvent();
		addMenuButton("Synchronizuj", synchronize);
		addMenuButton("Dzisiaj", today);
		addMenuButton("Wczoraj", yesterday);
		addMenuButton("Szukaj", null);
		addMenuButton("Pomoc", null);
		
		add(menuPanel, BorderLayout.WEST);
		
		InitVariables();

		date = LocalDate.now();
		LoadDBData(date);

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
	
	private void LoadDBData(LocalDate date) {
		TVDatabase db = new TVDatabase();
		itemsList = db.selectTVItems();
		String d = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		List<TVItem> itemsToDisplay = new LinkedList<TVItem>();
		
		for (TVItem item : itemsList) {
			if (item.getAirDate().substring(0, 10).equals(d))
				itemsToDisplay.add(item);
		}
		table.UpdateTable(itemsToDisplay);
	}
	
	class SynchronizeDatabaseEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			CPParser cp = new CPParser();
			ParserDBConnect export = new ParserDBConnect();
			export.ExportToDB(cp.ParseData());
		}
	}	
	class ShowTodayEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			date = LocalDate.now();
			
			LoadDBData(date);
		}
	}
	class ShowYesterdayEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			date = LocalDate.now().minusDays(1); 
			
			LoadDBData(date);
		}
	}
}
