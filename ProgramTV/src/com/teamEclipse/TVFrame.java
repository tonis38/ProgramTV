package com.teamEclipse;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;
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
	private JPanel centralPanel;
	private JPanel datePanel;
	private JLabel dateLabel;
	private List<TVItem> itemsList;
	private TVScheduleTable table;
	LocalDate date;
	
	public TVPanel() {
		setLayout(new BorderLayout());
		
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(20,1));
		
		menuPanel.add(addMenuButton("Synchronizuj", new SynchronizeDatabaseEvent()));
		menuPanel.add(addMenuButton("Dzisiaj", new ShowTodayEvent()));
		menuPanel.add(addMenuButton("Wczoraj", new ShowYesterdayEvent()));
		menuPanel.add(addMenuButton("Szukaj", null));
		menuPanel.add(addMenuButton("Pomoc", null));
		add(menuPanel, BorderLayout.WEST);
		
		centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		datePanel = new JPanel();
		datePanel.setLayout(new BorderLayout());

		date = LocalDate.now();
		dateLabel = new SmoothLabel(date.format(DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy)")));
		dateLabel.setHorizontalAlignment(JLabel.CENTER);
		dateLabel.setFont(new Font(new JLabel().getFont().getFontName(), Font.PLAIN, 22));
		datePanel.add(dateLabel, BorderLayout.CENTER);
		datePanel.add(addMenuButton("Poprzedni", new ShowPrevDayEvent()), BorderLayout.WEST);
		datePanel.add(addMenuButton("Następny", new ShowNextDayEvent()), BorderLayout.EAST);
		

		centralPanel.add(datePanel, BorderLayout.NORTH);
		
		itemsList = new LinkedList<TVItem>();
		table = new TVScheduleTable();

		LoadDBData(date);

		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		centralPanel.add(scrollPane, BorderLayout.CENTER);
		add(centralPanel, BorderLayout.CENTER);
	}
	
	private JButton addMenuButton(String label, ActionListener listener) {
		JButton button = new SmoothButton(label);
		button.addActionListener(listener);
		return button;
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
		table.UpdateTable(itemsToDisplay, db.selectTVNetworks());
	}
	
	class SynchronizeDatabaseEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
//			CPParser cp = new CPParser();
			ParserDBConnect export = new ParserDBConnect();
			OnetParser parser = new OnetParser();
			parser.ParseData();
			export.ExportItems(parser.getItems());
			export.ExportNetworks(parser.getNetworks());
			LoadDBData(date);
		}
	}	
	class ShowTodayEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			date = LocalDate.now();
			dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy)")));
			LoadDBData(date);
		}
	}
	class ShowYesterdayEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			date = LocalDate.now().minusDays(1);
			dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy)")));
			LoadDBData(date);
		}
	}
	class ShowNextDayEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			date = date.plusDays(1);
			dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy)")));
			LoadDBData(date);
		}
	}
	class ShowPrevDayEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			date = date.minusDays(1);
			dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy)")));
			LoadDBData(date);
		}
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
	class SmoothButton extends JButton {

	    public SmoothButton(String text) {
	        super(text);
	    }

	    public void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	        super.paintComponent(g2d);
	    }
	}
}
