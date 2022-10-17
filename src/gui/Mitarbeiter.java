package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionEvent;

/**
 * Die Klasse erstellt das Hauptfenster der Anwendung, in der 
 * die Terminvergabe durchgeführt wird und von der aus auch 
 * die weiteren  Klassen/GUI-Fenster aufgerufen werden können
 * @author SRoot
 *
 */
public class Mitarbeiter extends JFrame {	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	protected JPanel ebene1;
	private JPanel ebene2;
	private JPanel ebene3;
	private JTextField tfDatum;
	protected JComboBox<String> aerzteComboBox;
	private JLabel lblTitel;
	protected JLabel lblTitel1;		
	protected JLabel lblArzt;
	private JLabel lblMessage;
	private JButton btnButton2;
	private JButton btnButton3;
	protected JButton btnButton4; 
	private JLabel lblTitel3;
	private JButton[] terminButtons = new JButton[13];
	
	private int maID;
	private int tfID;
	private int maIDAktuell;
	private int rolleID;
	private String arzt;
	private String datum;	
	private String vorname;
	private String nachname;
	private String strasseHausNr;
	private String plz;
	private String ort;
	private String telefonNr;
	private String eMail;
	private Calendar cal = Calendar.getInstance();
	
	private HashMap<Integer, String> aerzteMap = new HashMap<Integer, String>();
	private HashMap<Integer, String> terminfensterMap = new HashMap<Integer, String>();
	private List<Integer> belegteTerminfenster;
	private List<Integer> abgelaufeneTermine;	
		
	/**
	 * Der Konstruktor erstellt das Hauptfenster, das nachher
	 * von den Konstruktoren aus den Unterklassen geändert wird.
	 * @param vorname
	 * @param nachname
	 */
	public Mitarbeiter(String vorname, String nachname) {
		
		setTitle("Terminvergabe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		
		Start.hFensterExistiert = true; // Dient als Signal, dass ein Hauptfenster bereits erstellt wurde

		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		ebene1 = new JPanel();
		ebene1.setLayout(null);
		ebene1.setPreferredSize(new Dimension(600, 150));

		ebene2 = new JPanel(new GridLayout(5, 3));
		ebene2.setPreferredSize(new Dimension(450, 200));

		ebene3 = new JPanel();
		ebene3.setLayout(null);
		ebene3.setPreferredSize(new Dimension(600, 100));

		lblMessage = new JLabel();
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblMessage.setBounds(0, 85, 600, 15);
		lblMessage.setVerticalAlignment(JLabel.BOTTOM);
		lblMessage.setHorizontalAlignment(JLabel.CENTER);
		ebene3.add(lblMessage);

		lblTitel = new JLabel();
		lblTitel.setText("Willkommen, " + vorname + " " + nachname);
		lblTitel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel.setHorizontalAlignment(JLabel.CENTER);
		lblTitel.setBounds(0, 15, 600, 20);
		ebene1.add(lblTitel);

		lblArzt = new JLabel("Arzt/Ärztin");
		lblArzt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblArzt.setBounds(50, 64, 100, 25);
		ebene1.add(lblArzt);

		/* Eine ComboBox mit Ärztenamen.
		 * Wenn eine der Namen angeklickt werden, wird diser in die Variable arzt abgelesen.
		 * Dabei wird auch die MiarbeiterID des Ärztes / der Ärztin einer HashMap entnommen,
		 * die vorher durch die Methode aerzteMapErstellen() erstellt wurde.*/
		aerzteComboBox = new JComboBox<String>();
		aerzteComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		aerzteComboBox.setBounds(170, 64, 250, 25);
		aerzteComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arzt = (String) aerzteComboBox.getSelectedItem();
				maID = getKeyFromValue(aerzteMap, aerzteComboBox.getSelectedItem());
				try {
					terminButtonsEinfaerben();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ebene1.add(aerzteComboBox);

		JLabel lblDatum = new JLabel("Termine am");
		lblDatum.setHorizontalAlignment(JLabel.RIGHT);
		lblDatum.setBounds(50, 94, 100, 25);
		ebene1.add(lblDatum);

		/* Ein Textfeld mit dem ausgewälten Datum.
		 * Als Standardmäßiges Wert wird das aktuelle Datum eingesetzt.*/
		tfDatum = new JTextField(20);
		tfDatum.setEditable(false);
		tfDatum.setBounds(170, 94, 250, 25);
		tfDatum.setHorizontalAlignment(JTextField.CENTER);
		SimpleDateFormat datumFormat = new SimpleDateFormat("dd-MM-yyyy");
		tfDatum.setText(datumFormat.format(cal.getTime()));
		datum = tfDatum.getText();
		ebene1.add(tfDatum);

		// Ein Button, das ein neues Programmfenster mit interaktivem Kalender aufruft
		JButton btnKalender = new JButton();
		btnKalender.setText("Kalender");
		btnKalender.setBounds(434, 94, 100, 24);
		btnKalender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblMessage.setVisible(false);
				tfDatum.setText(new Kalender().datumEinsetzen());
				// Wenn im Kalender kein Tag gewählt wurde, bleibt derselbe Wert im Fenster
				if (tfDatum.getText().equals("")) 	
					tfDatum.setText(datum);
				datum = tfDatum.getText();
				/* Hier wird eine Methode aufgerufen, die die weiterhin erzeugte Terminbuttons 
				 * nach dem Betätigen des Kalenderbuttons neu einfärbt*/
				try {
					terminButtonsEinfaerben();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ebene1.add(btnKalender);

		// Das mittlere Feld des Fensters, das Buttons für Terminfenster entält
		ebene2.setBorder(new EmptyBorder(0, 30, 0, 30));
		// Hier werden anklickbare Buttons für alle Terminfenster erzeugt 
		for (int i = 0; i < terminButtons.length; i++) {
			final int selection = i;
			terminButtons[i] = new JButton();
			terminButtons[i].setSize(150, 100);
			terminButtons[i].setFocusPainted(false);
			terminButtons[i].setBackground(Color.green);
			// Der Button kann verschiedene Aktionen ausführen
			terminButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						terminButtonsEinfaerben();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					/* Hier werden die Bezeichnung des selektierten Terminfensters abgelesen 
					 * sowie die entsprechende TerminfensterID, die weiter an einen Konstruktor
					 * als Parameter weitergegeben wird*/
					String terminFenster = terminButtons[selection].getActionCommand(); 							
					tfID = getKeyFromValue(terminfensterMap, terminButtons[selection].getActionCommand()); 
					/* Wenn der Index eines selektierten Buttons weder in der Liste bereits belegter 
					 * Termine noch in der Liste abgelaufebner Terminfenster enthalten ist, 
					 * wird ein Terminbestätigungsfenster aufgerufen. Die erwähnten Listen 
					 * wierden weiter durch eine Methode erstellt.*/
					if (!belegteTerminfenster.contains(selection) & !abgelaufeneTermine.contains(selection)) {	
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									Terminbestaetigung tbFenster = new Terminbestaetigung(maID, arzt, tfID,
											terminFenster, datum);
									tbFenster.setVisible(true);
									terminButtonsEinfaerben();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					/* Wenn der Index eines selektierten Buttons in der Liste bereits belegter 
					 * Termine sowie in der Liste der bereits abgelaufenen Termine enthalten ist, 
					 * wird ein Fenster mit allen Informationen zum termin aufgerufen. Die Liste 
					 * mit Indexen der belegten Termine sowie die Liste mit Indexen der bereits 
					 * abgelaufenen Termine werden weiter durch entsprechende Methoden erstellt. */	
					} else if(belegteTerminfenster.contains(selection) & abgelaufeneTermine.contains(selection)) {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									boolean abgelaufen = true;
									Terminfreigabe tfgFenster = new Terminfreigabe(maID, tfID, datum, abgelaufen);
									tfgFenster.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
									tfgFenster.setVisible(true);
									terminButtonsEinfaerben();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						/* Wenn der Index eines selektierten Buttons in der Liste bereits abgelaufener
						 * Termine aber nicht in der Liste belegter Termine zu finden ist, wird der Benutzer 
						 * informiert, dass das Terminfenster bereits abgelaufen ist */
					} else if (!belegteTerminfenster.contains(selection) & abgelaufeneTermine.contains(selection)) {
						JOptionPane.showMessageDialog(null, "Das Terminfenster ist bereits abgelaufen");
						try {
							terminButtonsEinfaerben();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					/* Ansonsten wird ein Fenster aufgerufen, das eine Freigabe eines berets belegten 
					 * Termins ermöglicht. Jedesmal werden die Terminbuttons durch die entsprechende
					 * Methode neu eingefärbt.*/
					} else {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									Terminfreigabe tfgFenster = new Terminfreigabe(maID, tfID, datum);
									tfgFenster.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
									tfgFenster.setVisible(true);
									terminButtonsEinfaerben();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			});		
			ebene2.add(terminButtons[i]);
		}
		
		// Hier wird eine Methode zum Beschriften der Terminbuttons aufgerufen 
		try {
			terminButtonsBeschriften();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Der Button ruft die Methode zum Verwalten personenbezugener Daten des aktuellen Benutzers auf
		btnButton2 = new JButton("Ihre persönlichen Daten verwalten");
		btnButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					benutzerdatenAnfragen();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnButton2.setBounds(30, 35, 230, 21);
		ebene3.add(btnButton2);
		
		// Der Button ruft ein Fenster mit Monatsübersichten belegter Termine für eine/-n ausgewählte/-n Ärztin/Arzt 
		btnButton3 = new JButton("Monatsübersicht ausgeben");
		btnButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Monatsuebersicht mueFenster = new Monatsuebersicht ();
							mueFenster.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							mueFenster.setVisible(true);														
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnButton3.setBounds(30, 60, 230, 21);
		ebene3.add(btnButton3);
		
		// Der Button wird nur im Konstruktor der Klasse Sprechstundenhilfe aktiviert
		btnButton4 = new JButton("");
		btnButton4.setBounds(340, 35, 230, 21);
		btnButton4.setVisible(false);
		ebene3.add(btnButton4);
		
		// Der Button beendet das Programm
		JButton btnBeenden = new JButton("Beenden");
		btnBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnBeenden.setBounds(470, 60, 100, 21);
		btnBeenden.setBackground(Color.red);
		ebene3.add(btnBeenden);
		
		contentPane.add(ebene1, BorderLayout.NORTH);
		contentPane.add(ebene2, BorderLayout.CENTER);
		contentPane.add(ebene3, BorderLayout.SOUTH);
		
		lblTitel3 = new JLabel();
		lblTitel3.setText("Weitere Möglichkeiten");
		lblTitel3.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel3.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel3.setBounds(0, 10, 600, 19);
		ebene3.add(lblTitel3);

		// Hier wird die Methode zum Einfärben der Terminbuttons zum ersten Mal aufgerufen
		try {
			terminButtonsEinfaerben();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/* Hier wird eine Methode aufgerufen die eine HashMap 
		 * mit Ärtenamen und ihren MitarbeiterIDs erstellt 
		 * und die entsprecehnde ComboBox mit Ärztenamen befüllt*/
		try {
			aerzteMapErstellen();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		pack();
		setLocationRelativeTo(null);
	}
	
	/**
	 * Die Methode erstellt aus der von der Datenbank erhaltenen Daten
	 * eine HashMap mit Ärztenamen und entsprechenden MitarbeiterIDs
	 * und befüllt die GUI-ComboBox mit den Namen
	 * @throws SQLException
	 */
	private void aerzteMapErstellen() throws SQLException {

		Start.connectionAufbauen();
		
		lblMessage.setText("Verbindung zur Datenbank erfolgreich aufgebaut");
		lblMessage.setVisible(true);

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
	 * Die Methode beschriftet die Terminfensterbuttons mit Terminfensterbezeichnungen aus der Datenbank 
	 * sowie erstellt eine HashMap mit TerminfensterIDs und entsprechenden Terminfenstebezeichnungen.
	 * @throws SQLException
	 */
	private void terminButtonsBeschriften() throws SQLException {
		
		Start.connectionAufbauen();
		
		lblMessage.setText("Verbindung zur Datenbank erfolgreich aufgebaut");
		lblMessage.setVisible(true);

		Statement stmtTF = Start.connection.createStatement();
		String queryTF = "SELECT TF_ID, Von_Bis FROM Terminfenster";
		ResultSet rsTF = stmtTF.executeQuery(queryTF);
		while (rsTF.next()) {
			terminfensterMap.put(rsTF.getInt("TF_ID"), rsTF.getString("Von_Bis"));
		}		
		stmtTF.close();
		rsTF.close();	
		
		for (int i = 0; i < terminfensterMap.size();  i++) {		
			terminButtons[i].setText(terminfensterMap.get(i));
		}
	}
	
	
	/**
	 * Die Methode ruft Informationen zu belegten Terminen an einem in der GUI
	 * eingegebenen Datum, dann färbt die Terminbuttons der GUI laut der erhaltenen
	 * Informationen ein. Außerdem werden hier die Terminbuttons für bereits abgelaufene 
	 * Termine deaktiviert.
	 * @throws SQLException
	 * @throws ParseException
	 */
	private void terminButtonsEinfaerben() throws SQLException, ParseException {
		
		Start.connectionAufbauen();		
		lblMessage.setText("Verbindung zur Datenbank erfolgreich aufgebaut");
		lblMessage.setVisible(true);
		
		HashMap<Integer, Integer> alleBelegtenTermine = new HashMap<Integer, Integer>();
		belegteTerminfenster = new ArrayList<Integer>();
		abgelaufeneTermine = new ArrayList<Integer>();
		SimpleDateFormat zeitFormat = new SimpleDateFormat("hh:mm:ss yyyy-MM-dd");
		String datumUmgekehrt = Start.datumUmkehren(datum);
		
		// Hier werden alle in die Datenbank eingetragene Termine zu einem Datum abgerufen
		Statement stmtTE = Start.connection.createStatement();
		String queryTE = "SELECT TF_ID, MA_ID FROM Arzttermine WHERE Datum = \'" + datumUmgekehrt + "\'";
		ResultSet rsTE = stmtTE.executeQuery(queryTE);
		
		// Aus den abgerufenen Daten wird eine HashMap erstellt
		while (rsTE.next()) {
			alleBelegtenTermine.put(rsTE.getInt("TF_ID"), rsTE.getInt("MA_ID"));
		}		
		
		// Hier wird es durch alle Terminbuttons iteriert
		for (int i = 0; i < terminButtons.length; i++) {
			// Zuerset werden alle Buttons "refrescht" und aktiviert
			terminButtons[i].setBackground(Color.green);
			terminButtons[i].setForeground(lblTitel.getForeground());
			terminButtons[i].setEnabled(true);
			// Dann wird es durch die erstellte Termine-HashMap iteriert 
			for (int j : alleBelegtenTermine.keySet()) {
				/* Wenn ein Button-Index unter TerminIDs aus den Schlüsseln zu finden ist
				 * und dabei die aktuell in der GUI gewählte MitarbeiterID mit dem entsprechenden
				 * Wert übereinstimmt, wird der Button rot eingefärbt.*/
				if (i == j & alleBelegtenTermine.get(j) == maID) {
					terminButtons[i].setBackground(Color.red);
					belegteTerminfenster.add(i);
				} 
			}			
			
			/* Weiter wird geprüft, ob einige Terminfenster bereits abgelaufen sind. 
			 * Dazu wird für jedes Button der Startzeitpunkt von der Buttonbeschriftung
			 * und dem aktuellen Datum abgeleitet und mit dem aktuellen Zeitpunkt vergliechen.
			 * Die bereits abgelaufene und dabei nicht belegte Terminfenster werden deaktiviert.
			 * Die nicht abgelaufene werden in eine extra Liste gespeichert und die entsprechenden 
			 * Buttons bleiben aktiv, um das Einsehen Der Termininformationen zu ermöglichen.*/
			java.util.Date terminStart = zeitFormat.parse(terminButtons[i].getActionCommand().substring(0, 5) 
					+ ":00 " + Start.datumUmkehren(tfDatum.getText()));	
			if (Calendar.getInstance().getTime().after(terminStart)) {
				abgelaufeneTermine.add(i);
				terminButtons[i].setForeground(Color.gray); // Damit sich ein abgelaufener belegter Termin von den noch Nichtabgelaufenen unterscheidet 
				if (!belegteTerminfenster.contains(i)) {
					terminButtons[i].setEnabled(false);
				}
			}					
		}			
		stmtTE.close();
		rsTE.close();
	}	
	
	/**
	 * Die Methode ruft die Mitarbeiterdaten aus der Datenbank ab und ruft 
	 * ein Fenster auf, in dem die Mitarbeiterdaten sich verwalte lassen.
	 * @throws SQLException
	 */
	private void benutzerdatenAnfragen() throws SQLException {

		Start.connectionAufbauen();
		
		lblMessage.setText("Verbindung zur Datenbank erfolgreich aufgebaut");
		lblMessage.setVisible(true);

		Statement stmtBD = Start.connection.createStatement();
		String queryBD = "SELECT Benutzer.Benutzername, Benutzer.MA_ID, Benutzer.RolleID, "
				+ "Mitarbeiter.Vorname, Mitarbeiter.Nachname, Mitarbeiter.Strasse_HausNr, "
				+ "Mitarbeiter.PLZ, Mitarbeiter.Ort, Mitarbeiter.TelefonNr, Mitarbeiter.EMail " + "FROM Benutzer "
				+ "LEFT JOIN Mitarbeiter USING(MA_ID) " + "WHERE Benutzername = \'" + Start.aktuellerBenutzer + "\'";
		ResultSet rsBD = stmtBD.executeQuery(queryBD);
		
		while (rsBD.next()) {
			maIDAktuell = rsBD.getInt("MA_ID");
			rolleID = rsBD.getInt("RolleID");
			vorname = rsBD.getString("Vorname");
			nachname = rsBD.getString("Nachname");
			strasseHausNr = rsBD.getString("Strasse_HausNr");
			plz = rsBD.getString("PLZ");
			ort = rsBD.getString("Ort");
			telefonNr = rsBD.getString("TelefonNr");
			eMail = rsBD.getString("EMail");	
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					Mitarbeiterdaten datenFenster = new Mitarbeiterdaten(maIDAktuell, rolleID, 
							vorname, nachname,strasseHausNr, plz, ort, telefonNr, eMail);
					datenFenster.setVisible(true);										
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		rsBD.close();
		stmtBD.close();	
	}
	
	/**
	 * Die Methode ist dem Intenet entnommen und überarbeitet.
	 * Sie ermöglicht eine Suche nach einem Schlüssel für ein Wert in einer HashMap.
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @param wert
	 * @return
	 */
	public static <K, V> K getKeyFromValue(HashMap<K, V> map, Object wert) {
		Set<K> schluesselSet = map.keySet();
		for (K schluessel : schluesselSet) {
			if (map.get(schluessel).equals(wert)) {
				return schluessel;
			}
		}
		return null;
	}

}