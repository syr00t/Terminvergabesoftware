package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Die Klasse erstellt einen interaktiven Kalendermodul, 
 * der zur Datumeingabe dient und diese erleichtert
 * @author SRoot
 *
 */
public class Kalender {

	private JLabel monatJahr;	
	private JDialog dKalender;	
	private JButton[] button = new JButton[42];
	private JLabel[] label = new JLabel[7];
	
	private String tag = "";
	private int monat = Calendar.getInstance().get(Calendar.MONTH);
	private int jahr = Calendar.getInstance().get(Calendar.YEAR);
		
	/**
	 *  Erstellt ein Fenster mit dem Kalender zum Datumwählen
	 */
	public Kalender() {
		
		dKalender = new JDialog();
		dKalender.setModal(true);
		
		String[] wochenTage = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"}; 
		
		// Erzeugt die obere Leiste mit Wochentagebeschriftungen
		JPanel p1 = new JPanel(new GridLayout(1,7));
		p1.setBorder(new EmptyBorder(5, 5, 0, 5));
		for (int i = 0; i < wochenTage.length; i++) {
			label[i] = new JLabel();
			label[i].setHorizontalAlignment(JLabel.CENTER);
			label[i].setVerticalAlignment(JLabel.CENTER);
			label[i].setText(wochenTage[i]);
			p1.add(label[i]);
		}
		
		// Erzeugt das mittlere Paneel mit dem anklickbaren Kalender
		JPanel p2 = new JPanel(new GridLayout(6, 7));
		p2.setBorder(new EmptyBorder(5, 5, 5, 5));
		p2.setPreferredSize(new Dimension(400, 150));

		for (int x = 0; x < button.length; x++) {
			final int selection = x;
			button[x] = new JButton();
			button[x].setFocusPainted(false);
			if ((x + 1) % 7 == 6 | (x + 1) % 7 == 0) {
				button[x].setBackground(Color.lightGray); 	// Sa und So werden grau eingefärbt 
			} else {
				button[x].setBackground(Color.white); 		// Die Arbetstage werden weiß eingefärbt
				button[x].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tag = button[selection].getActionCommand(); 	// Der Variablen weird als Wert die Beschriftung  des angeklickten Buttons zugewiesen
						dKalender.dispose();	                        // Dann wird das Kalenderfenster geschlossen
					}
				});
			}
			p2.add(button[x]);
		}
		
		// Ein Paneel mit den Tasten, die Monate im Kalender wechseln lassen
		JPanel p3 = new JPanel(new GridLayout(1, 3));
		p3.setBorder(new EmptyBorder(0, 5, 5, 5));

		JButton vor = new JButton("<< Vor");
		vor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monat--;
				buttonsBeschriften();
			}
		});
		p3.add(vor);
		
		monatJahr = new JLabel();
		monatJahr.setHorizontalAlignment(JLabel.CENTER);
		p3.add(monatJahr);

		JButton nach = new JButton("Nach >>");
		nach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monat++;
				buttonsBeschriften();
			}
		});
		p3.add(nach);

		dKalender.add(p1, BorderLayout.NORTH);
		dKalender.add(p2, BorderLayout.CENTER);
		dKalender.add(p3, BorderLayout.SOUTH);
		dKalender.pack();
		dKalender.setLocationRelativeTo(null);
		dKalender.setLocation(new Point(dKalender.getX(), dKalender.getY() + 30)); // Damit das Kalenderfenster das Datumtextfeld im Terminvergabefenster nicht überdeckt
		buttonsBeschriften();
		dKalender.setVisible(true);
	}

	
	/** 
	 * Die Methode beschriftet die Kalenderbuttons je nach gewähltem Monat
	 */
	private void buttonsBeschriften() {

		for (int x = 0; x < button.length; x++) 
			button[x].setText("");	
		
		SimpleDateFormat datumFormat = new java.text.SimpleDateFormat("MMMM yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(jahr, monat, 1);
		int wochentag = cal.get(Calendar.DAY_OF_WEEK);
		int tageImMonat = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int x = wochentag - 2, tag = 1; tag <= tageImMonat; x++, tag++) {
			if (x == -1) x = 6; 												// Damit Mo-So geliefert wird
			button[x].setText(Integer.toString(tag));
		}
		monatJahr.setText(datumFormat.format(cal.getTime()));
		dKalender.setTitle("Datum wählen");
	}

	
	/**
	 * Die Methode wird von der Klasse Mitarbeiter aufgerufen, um das im Kalender 
	 * durchs Anklicken gewähltes Datum im entsprechenden Textfeld im Terminvergabefenster
	 * einzusetzen.
	 * @return String
	 */
	public String datumEinsetzen() {
		if (tag.equals(""))
			return tag;
		SimpleDateFormat datumFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(jahr, monat, Integer.parseInt(tag));
		return datumFormat.format(cal.getTime());
	}
}