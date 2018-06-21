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
	private JPanel sidePanel, centralPanel, tablePanel, datePanel, settingsPanel;
	private JLabel dateLabel;
	private CardLayout cl;
	private List<TVItem> itemsList;
	private TVScheduleTable table;
	LocalDate date;
	
	public TVPanel() {
		setLayout(new BorderLayout());
		
		buildSidePanel();
		add(sidePanel, BorderLayout.WEST);
		
		cl = new CardLayout();
		centralPanel = new JPanel();
		centralPanel.setLayout(cl);
		
		buildTablePanel();
		buildSettingsPanel();
		
		centralPanel.add(tablePanel, "1");
		centralPanel.add(settingsPanel, "2");
		cl.show(centralPanel, "1");
		add(centralPanel, BorderLayout.CENTER);
	}
	
	private void buildSidePanel() {
		GridBagConstraints c = new GridBagConstraints();
		Insets buttonSpacing = new Insets(5,0,0,0);
		
		sidePanel = new JPanel();
		sidePanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		
		sidePanel.add(addMenuButton("Synchronizuj", new SynchronizeDatabaseEvent()), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = buttonSpacing;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		sidePanel.add(addMenuButton("Dzisiaj", new ShowTodayEvent()), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = buttonSpacing;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		sidePanel.add(addMenuButton("Szukaj", null), c);

			c.anchor = GridBagConstraints.NORTH;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 3;
			sidePanel.add(new JLabel(""), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTH;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		sidePanel.add(addMenuButton("Ustawienia", new ShowSettingsEvent()), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTH;
		c.weighty = 0;
		c.insets = buttonSpacing;
		c.gridx = 0;
		c.gridy = 5;
		sidePanel.add(addMenuButton("Pomoc", null), c);
	}
	
	private void buildTablePanel() {
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		datePanel = new JPanel();
		datePanel.setLayout(new BorderLayout());

		date = LocalDate.now();
		dateLabel = new SmoothLabel(date.format(DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy)")));
		dateLabel.setHorizontalAlignment(JLabel.CENTER);
		dateLabel.setFont(new Font(new JLabel().getFont().getFontName(), Font.PLAIN, 22));
		datePanel.add(dateLabel, BorderLayout.CENTER);
		datePanel.add(addMenuButton("Poprzedni", new ShowPrevDayEvent()), BorderLayout.WEST);
		datePanel.add(addMenuButton("Następny", new ShowNextDayEvent()), BorderLayout.EAST);

		tablePanel.add(datePanel, BorderLayout.NORTH);
		
		itemsList = new LinkedList<TVItem>();
		table = new TVScheduleTable();

		LoadDBData(date);

		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void buildSettingsPanel() {
		GridBagConstraints c = new GridBagConstraints();
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		Insets lineSpacing = new Insets(5,5,0,20);
		
		sidePanel = new JPanel();
		sidePanel.setLayout(new GridBagLayout());
		
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0;
		c.weightx = 0;
		c.insets = lineSpacing;
		c.gridx = 0;
		c.gridy = 0;
		settingsPanel.add(new JLabel("Źródło:"), c);
		
		JComboBox<String> source = new JComboBox<String>();
			source.addItem("Onet");
			source.addItem("NC+");
			source.addItem("CyfrowyPolsat");
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 1;
		c.gridy = 0;
		settingsPanel.add(source, c);

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 1;
		settingsPanel.add(new JLabel("Automatyczna aktualizacja:"), c);

		JComboBox<String> update = new JComboBox<String>();
			update.addItem("Nie aktualizuj automatycznie");
			update.addItem("co 30 minut");
			update.addItem("co 1 godzinę");
			update.addItem("co 3 godziny");
			update.addItem("co 24 godziny");
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 1;
		c.gridy = 1;
		settingsPanel.add(update, c);

		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 2;
		c.gridy = 2;
		settingsPanel.add(new JLabel(""), c);
	}
	
	private JButton addMenuButton(String label, ActionListener listener) {
		JButton button = new SmoothButton(label);
		button.addActionListener(listener);
		button.setBounds(0, 0, 40, 20);
		return button;
	}
	
	private void LoadDBData(LocalDate date) {
		TVDatabase db = new TVDatabase();
		itemsList = db.selectTVItems();
		String d = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		List<TVItem> itemsToDisplay = new LinkedList<TVItem>();
		
		if (itemsList != null) {
			for (TVItem item : itemsList) {
				if (item.getAirDate().substring(0, 10).equals(d))
					itemsToDisplay.add(item);
			}
			table.UpdateTable(itemsToDisplay, db.selectTVNetworks());
		}
	}
	
	class SynchronizeDatabaseEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			ParserDBConnect export = new ParserDBConnect();
			Parser parser = new OnetParser();
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
			cl.show(centralPanel, "1");
		}
	}	
	class ShowSettingsEvent implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			cl.show(centralPanel, "2");
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
