package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Die Klasse ertsellt ein GUI-Fenster mit der Monatsübersicht der Ärzttermine
 * @author SRoot
 *
 */
public class Monatsuebersicht extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblArzt;
	private JPanel ebene1;
	private JPanel ebene3;
	private JComboBox<String> aerzteComboBox;
	private JLabel monatJahr;
	private JLabel[] label = new JLabel[7];
	private JLabel[] tage = new JLabel[42];
	private JLabel[] termine = new JLabel[42];
	
	private int maID;
	private String gewaehlterMonat;
	private Calendar cal = Calendar.getInstance();
	private int monat = Calendar.getInstance().get(Calendar.MONTH);
	private int jahr = Calendar.getInstance().get(Calendar.YEAR);
	private String[] wochenTage = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
	private HashMap<Integer, String> aerzteMap = new HashMap<Integer, String>();
			
	
	/**
	 * Der Konstruktor erstellt ein Fenster mit einem Kalender, 
	 * analog dem aus der Klasse Klender, doch ohne Kalenderbuttons,
	 * in dem die Anzahl belegter Termine an jedem Tag eines 
	 * ausgewählten Monates für eine/-n ausgewählte/-n Ärztin/Ärzt
	 * angezeigt wird. 
	 */
	public Monatsuebersicht() {
		
		setTitle("Monatsübersicht");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
				
		ebene1 = new JPanel();
		ebene1.setBorder(new EmptyBorder(5, 0, 5, 0));
		ebene1.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		lblArzt = new JLabel("Arzt/Ärztin");
		lblArzt.setHorizontalAlignment(SwingConstants.RIGHT);
		ebene1.add(lblArzt);

		aerzteComboBox = new JComboBox<String>();
		aerzteComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		aerzteComboBox.setPreferredSize(new Dimension(250, 25));
		aerzteComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				maID = Mitarbeiter.getKeyFromValue(aerzteMap, aerzteComboBox.getSelectedItem());
				
				try {
					termineAnzeigen();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ebene1.add(aerzteComboBox);
		
		/* Mittleres Feld, analog dem Kalenderfeld aus der Klasse Kalender, 
		 * aus drei Leisten bzw. Paneelen bestehend */
		JPanel ebene2 = new JPanel(new BorderLayout()); 
		
		// Hier wird die obere Leiste mit Wochentagebeschriftungen erstellt
		JPanel p1 = new JPanel(new GridLayout(1,7));
		p1.setBorder(new EmptyBorder(5, 5, 5, 5));
		for (int i = 0; i < wochenTage.length; i++) {
			label[i] = new JLabel();
			label[i].setHorizontalAlignment(JLabel.CENTER);
			label[i].setVerticalAlignment(JLabel.CENTER);
			label[i].setText(wochenTage[i]);
			p1.add(label[i]);
		}
		
		/* Hier wird ein Paneel aus 42 Zellen erstellt, jede von denen
		 * aus zwei JLabel besteht: eins für dei Tagesbeschriftungen 
		 * und eins darunter für die Anzahl belegter Termine an dem Tag*/
		JPanel p2 = new JPanel(new GridLayout(6, 7));
		p2.setBorder(new EmptyBorder(5, 5, 5, 5));
		for (int i = 0; i < tage.length; i++) {
			tage[i] = new JLabel();
			tage[i].setPreferredSize(new Dimension(30, 15));
			tage[i].setHorizontalAlignment(JLabel.CENTER);
			tage[i].setOpaque(true);
			termine[i] = new JLabel();
			termine[i].setFont(new Font("Tahoma", Font.BOLD, 20));
			termine[i].setForeground(Color.gray);
			termine[i].setHorizontalAlignment(JLabel.CENTER);
			termine[i].setOpaque(true);
			if ((i + 1) % 7 == 6 | (i + 1) % 7 == 0) {
				tage[i].setBackground(Color.red);
				termine[i].setBackground(ebene2.getBackground());
			} else {
				tage[i].setBackground(Color.green);
				termine[i].setBackground(Color.white);
			}
			JPanel p = new JPanel(new BorderLayout());
			p.add(tage[i], BorderLayout.NORTH);
			p.add(termine[i], BorderLayout.CENTER);
			p.setPreferredSize(new Dimension(30, 70));
			p.setBorder(new LineBorder(p.getBackground()));
			p2.add(p);
		}
		
		// Die dritte Leiste mit den Navigationsbuttons
		JPanel p3 = new JPanel(new GridLayout(1, 3));
		p3.setBorder(new EmptyBorder(0, 5, 5, 5));

		JButton vor = new JButton("<< Vor");
		vor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monat--;
				tageBeschriften();
				try {
					termineAnzeigen();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		p3.add(vor);
		
		monatJahr = new JLabel();
		monatJahr.setOpaque(true);
		monatJahr.setBackground(Color.white);
		monatJahr.setHorizontalAlignment(JLabel.CENTER);
		p3.add(monatJahr);

		JButton nach = new JButton("Nach >>");
		nach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monat++;
				tageBeschriften();
				try {
					termineAnzeigen();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		p3.add(nach);

		ebene2.add(p1, BorderLayout.NORTH);
		ebene2.add(p2, BorderLayout.CENTER);
		ebene2.add(p3, BorderLayout.SOUTH);
		
		ebene3 = new JPanel();
		ebene3.setLayout(new FlowLayout(FlowLayout.CENTER));
		ebene3.setPreferredSize(new Dimension(450, 35));
		
		JButton btnBeenden = new JButton("Beenden");
		btnBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBeenden.setSize(100, 21);
		ebene3.add(btnBeenden);
		
		contentPane.add(ebene1, BorderLayout.NORTH);
		contentPane.add(ebene2, BorderLayout.CENTER);
		contentPane.add(ebene3, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);


		/* Hier wird eine Methode aufgerufen, die eine HaschMap mit Ärztenamen 
		 * und MitarbeiterIDs aus der DB erstellt, und mit den Ärztenamen 
		 * die entsprechende ComboBox in der GUI befüllt */		
		try {
			aerzteMapErstellen();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Hier wird eine Methode aufgerufen, die die Tagezellen beschriftet
		tageBeschriften();
		
		/* Hier wird eine Methode aufgerufen, die in jeder Zelle die Anzahl 
		 * der belegten termine für eine/-n ausgewälte/- Ärztin/Arzt anzeigt */
		try {
			termineAnzeigen();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	/**
	 * Die Methode erstellt aus der von der Datenbank erhaltenen Daten
	 * eine HashMap mit Ärztenamen und entsprechenden MitarbeiterIDs
	 * und befüllt die GUI-ComboBox mit den Namen
	 * @throws SQLException
	 */
	private void aerzteMapErstellen() throws SQLException {

		Start.connectionAufbauen();
		
		Statement stmtAM = Start.connection.createStatement();
		String queryAM = "SELECT Benutzer.MA_ID, Mitarbeiter.Vorname, Mitarbeiter.Nachname " + "FROM Benutzer "
				+ "LEFT JOIN Mitarbeiter USING(MA_ID) " + "WHERE RolleID = 1";
		ResultSet rsAM = stmtAM.executeQuery(queryAM);
		
		while (rsAM.next()) {
			aerzteMap.put(rsAM.getInt("MA_ID"), rsAM.getString("Vorname") + " " + rsAM.getString("Nachname"));
		}
		stmtAM.close();
		rsAM.close();

		for (String arzt : aerzteMap.values()) {
			aerzteComboBox.addItem(arzt);
		}
	}
	
	/**
	 * Die Methode beschriftet die Kalenderzellen in der GUI mit Monatstagenummern
	 */
	public void tageBeschriften() {

		for (int i = 0; i < tage.length; i++) 
			tage[i].setText("");							
		SimpleDateFormat datumFormat = new SimpleDateFormat("MMMM yyyy");
		SimpleDateFormat monatFormat = new SimpleDateFormat("MM");
		cal.set(jahr, monat, 1);
		int wochentag = cal.get(Calendar.DAY_OF_WEEK);
		int tageImMonat = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		for (int i = wochentag - 2, tag = 1; tag <= tageImMonat; i++, tag++) {
			if (i == -1) i = 6; 	// Damit Mo-So geliefert wird
			tage[i].setText("" + tag);
		}

		monatJahr.setText(datumFormat.format(cal.getTime()));
		gewaehlterMonat = monatFormat.format(cal.getTime());
	}
		
	/**
	 * Die Methode zeigt in jeder Kalenderzelle in der GUI an, wie viele Termine 
	 * gibt es jeden Tag im gewählten Monat für einen gewählte/-n Ärztin/Arzt.
	 * @throws SQLException
	 */
	public void termineAnzeigen() throws SQLException {
		
		Start.connectionAufbauen();

		List<Integer> termineListe = new ArrayList<>();

		// Zuerst werden alle Zellen mit den Terminanzahlen "aufgefrischt"
		for (JLabel termin : termine) {
			termin.setText("");
		}

		/* Aus der Datenbank werden alle Termindaten 
		 * zu einem in der GUI bestimmten Monat für 
		 * eine/-n gewählte/-n Ärztin/Arzt abgerufen */
		Statement stmtTA = Start.connection.createStatement();
		String queryTA = "SELECT Datum FROM Arzttermine WHERE MA_ID = " + this.maID + " AND MONTH(Datum) = \'"
				+ gewaehlterMonat + "\'";
		ResultSet rsTA = stmtTA.executeQuery(queryTA);

		/* Aus jedem Datensatz wird nur der Monatstag 
		 * "abgeschnitten" und in eine Liste gespeichert */
		while (rsTA.next()) {
			String tag = rsTA.getString("Datum").substring(rsTA.getString("Datum").length() - 2);
			termineListe.add(Integer.valueOf(tag));
		}

		/* Nun wird es durch alle Kalenderzellen iteriert und  gezählt, 
		 * wie viele Einträge es in der Liste für jeden Tag gibt*/
		for (int i = 0; i < tage.length; i++) {
			int zaehler = 0;
			for (int j : termineListe) {
				if (tage[i].getText().equals(Integer.toString(j))) {
					zaehler++;
				}
			}
			if (zaehler > 0) // Wenn die Anzahl > 0 ist, wird sie in eine entsprechende Zelle in der GUI eingetragen
				termine[i].setText(Integer.toString(zaehler));
		}
		stmtTA.close();
		rsTA.close();
	}
	
}