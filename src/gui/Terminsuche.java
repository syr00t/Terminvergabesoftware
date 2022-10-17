package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

/**
 * Die Klasse ertstellt ein GUI-Fenster zur Suche 
 * der zukünftigen Termine für einen Patienten
 * @author SRoot
 *
 */
public class Terminsuche extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel ebene1;
	private JPanel ebene2;
	private JPanel ebene3;
	private JComboBox<String> patientenComboBox;
	private JLabel lblPatient;
	private JScrollPane scrollPane;
	private JList<String> termineListbox;
	private DefaultListModel<String> termineModell;
	
	private int patientID;
	private Calendar cal = Calendar.getInstance();
	private HashMap<Integer, String> patientenMap = new HashMap<Integer, String>();
		
	
	/**
	 * Der Konstruktor erstellt ein Fenster zur Zuche 
	 * der zukünftigen Termine für einen bestimmten Patienten. 
	 */
	public Terminsuche() {
		setTitle("Terminsuche");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		ebene1 = new JPanel();
		ebene1.setBorder(new EmptyBorder(10, 0, 0, 0));
		ebene1.setPreferredSize(new Dimension(450, 50));
		ebene1.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		ebene2 = new JPanel();
		ebene2.setMinimumSize(new Dimension(450, 100));
		ebene2.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		ebene3 = new JPanel();
		ebene3.setBorder(new EmptyBorder(0, 0, 5, 0));
		ebene3.setPreferredSize(new Dimension(450, 40));
		ebene3.setLayout(new FlowLayout(FlowLayout.CENTER));

		lblPatient = new JLabel("Bestandspatient/-in");
		lblPatient.setHorizontalAlignment(SwingConstants.LEFT);
		ebene1.add(lblPatient);
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(450, 150));
		ebene2.add(scrollPane);

		termineListbox = new JList<String>();
		scrollPane.setViewportView(termineListbox);
		termineModell = new DefaultListModel<String>();
		termineListbox.setModel(termineModell);

		/* In dem ComboBox lässt sich ein Patientenname wählen,
		 * für den es nach den zukünftigen Termnen in der
		 * Datenbank gesucht werdern wird */
		patientenComboBox = new JComboBox<String>();
		patientenComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientenComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				patientID = Mitarbeiter.getKeyFromValue(patientenMap, patientenComboBox.getSelectedItem());
				try {
					termineSuchen();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ebene1.add(patientenComboBox);
		
		JButton btnBeenden = new JButton("Beenden");
		btnBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBeenden.setSize(100, 21);
		ebene3.add(btnBeenden);
		
		/* Hier wird die Methode aufgerufen, die aus den Datenbankeintragen 
		 * eine HashMap mit den Patientenamen und den entsprechenden 
		 * PatientenIDs ersetllt*/
		try {
			patientenMapErstellen();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		contentPane.add(ebene1, BorderLayout.NORTH);
		contentPane.add(ebene2, BorderLayout.CENTER);
		contentPane.add(ebene3, BorderLayout.SOUTH);
				
		pack();
		setLocationRelativeTo(null);
				
	}
	
	/**
	 * Die Methode ruft die Patienteninformationen aus der Datenbank 
	 * und erstellt eine HashMap mit den Patientennamen 
	 * und den entsprechenden PatientenIDs sowie befüllt
	 * mit den Patientenamen die entsprechende ComboBox in der GUI.
	 * @throws SQLException
	 */
	private void patientenMapErstellen() throws SQLException {

		Start.connectionAufbauen();

		Statement stmtPM = Start.connection.createStatement();
		String queryPM = "SELECT PatientID, Vorname, Nachname FROM Patienten ";
		ResultSet rsPM = stmtPM.executeQuery(queryPM);
		while (rsPM.next()) {
			patientenMap.put(rsPM.getInt("PatientID"), rsPM.getString("Vorname") 
					+ " " + rsPM.getString("Nachname"));
		}
		stmtPM.close();
		rsPM.close();

		for (String patient : patientenMap.values()) {
			patientenComboBox.addItem(patient);
		}
	}
		
	/**
	 * Die Methode ruft alle Termininformationen zu einer in der GUI bestimmten
	 * PateintenID, bildet daraus eine Liste und gibt diese an ein entsprechendes 
	 * Element der GUI weiter.
	 * @throws SQLException
	 * @throws ParseException 
	 */
	
	private void termineSuchen() throws SQLException, ParseException {
		
		Start.connectionAufbauen();
		
		SimpleDateFormat datumFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat zeitFormat = new SimpleDateFormat("hh:mm yyyy-MM-dd");
		String heute = datumFormat.format(cal.getTime());
		
		Statement stmtTS = Start.connection.createStatement();
		String queryTS = "SELECT Arzttermine.Datum, Mitarbeiter.Vorname, "
				+ "Mitarbeiter.Nachname, Terminfenster.Von_Bis FROM Arzttermine "
				+ "JOIN Mitarbeiter USING(MA_ID) JOIN Terminfenster USING(TF_ID) "
				+ "WHERE PatientID = " + patientID + " AND Datum >= \'" 
				+ heute + "\' ORDER BY Datum";
		ResultSet rsTS = stmtTS.executeQuery(queryTS);
		
		// Hier wird die Terminliste "zurückgesetzt"
		if (termineModell != null)
			termineModell.removeAllElements();
				
		/* Hier wird für jeden abgerufenen Datenbankeintrag ein Terminstartzeitpunkt ermittelt 
		 * und überprüft, ob dieser den aktuellen Zeitpunkt überschreitet bzw. nicht schon 
		 * abgelaufen ist. Wenn ja, wird der Eintrag in eine Liste gespeichert. */
		while (rsTS.next()) {			
			java.util.Date terminStart = zeitFormat.parse(rsTS.getString("Von_Bis").substring(0, 5) 
					+ " " + rsTS.getString("Datum"));			
			if (cal.getTime().before(terminStart))
				termineModell.addElement("Am " + Start.datumUmkehren(rsTS.getString("Datum")) 
					+ ", " + rsTS.getString("Von_Bis") + ", bei " + rsTS.getString("Vorname") 
					+ " " + rsTS.getString("Nachname"));
		}		
		termineListbox.setModel(termineModell);
		revalidate();
		
		stmtTS.close();
		rsTS.close();
	}

}