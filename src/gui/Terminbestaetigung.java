package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

/**
 * Die Klasse erstellt ein Terminbestätigungsdialogfenster
 * @author SRoot
 *
 */
public class Terminbestaetigung extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel lblTitel;
	private JLabel lblTitel1;
	private JPanel contentPane;
	private JLabel lblPatient;
	private JComboBox<String> patientenComboBox;
	
	private int patientID;
	private int maID;
	private int tfID;
	private String datum;	
	private String aktuellesDatum;
	private Calendar cal = Calendar.getInstance();
	private HashMap<Integer, String> patientenMap = new HashMap<Integer, String>();	

	/**
	 * Der Konstruktor erstellt ein Fenster zur endgültigen Bestätigung einer Terminbelegung
	 * @param maID
	 * @param arzt
	 * @param tfID
	 * @param terminFenster
	 * @param datum
	 */
	public Terminbestaetigung(int maID, String arzt, int tfID, String terminFenster, String datum) {
		setModal(true);
		setTitle("Terminbestätigung");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450, 200);		
		setLocationRelativeTo(null);
				
		this.maID = maID;
		this.tfID = tfID;
		this.datum = Start.datumUmkehren(datum);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		lblTitel = new JLabel("Arzt/Ärztin: " + arzt);
		lblTitel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel.setHorizontalAlignment(JLabel.CENTER);
		lblTitel.setBounds(10, 15, 416, 19);
		contentPane.add(lblTitel);
		
		lblTitel1 = new JLabel("Am " + datum + ", " + terminFenster);
		lblTitel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel1.setBounds(10, 35, 416, 19);
		contentPane.add(lblTitel1);
		
		lblPatient = new JLabel("Bestandspatient/-in");
		lblPatient.setHorizontalAlignment(SwingConstants.LEFT);
		lblPatient.setBounds(10, 75, 130, 25);
		contentPane.add(lblPatient);
		
		// Ein ComboBox mit den Patientennamen aus der Datenbank
		patientenComboBox = new JComboBox<String>();
		patientenComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientenComboBox.setBounds(150, 75, 260, 25);
		patientenComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 /* Hier wird der Vaiablen der Wert einer dem gewählten Patientennamen entsprechenden 
				  * PatientenID als eines Schlüssels aus einer weiterhin erstellten HashMap zugewiesen*/
				 patientID = Mitarbeiter.getKeyFromValue(patientenMap, patientenComboBox.getSelectedItem());
			}
		});
		contentPane.add(patientenComboBox);
		
		JButton btnBestaetigen = new JButton("Bestätigen");
		btnBestaetigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Hier wird eine Methode aufgerufen, die alle Daten zu einem ausgewählten Termin an die Datenbank übermittelt 
					datenUebermitteln(); 
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnBestaetigen.setBounds(160, 125, 100, 21);
		contentPane.add(btnBestaetigen);
		
		// Hier wird eine Methode aufgerufen, die eine HashMap mit den Patientennamen erstellt
		try {
			patientenMapErstellen();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	/**
	 * Die Methode erstellt eine HashMap mit allen Patientenamen 
	 * und den entsprechenden ParientenIDS aus der Datenbank
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
	 * Die Methode speichert Alle gesammelten Daten zu einem Termin in die Datenbank
	 * und schließt nach einem erfolgreichen Spechern das Fenster. 
	 * @throws SQLException
	 */
	private void datenUebermitteln() throws SQLException {
		
		Start.connectionAufbauen();
		
		SimpleDateFormat datumFormat = new SimpleDateFormat("dd-MM-yyyy");
		aktuellesDatum = datumFormat.format(cal.getTime());
				
		Statement stmtDU = Start.connection.createStatement();
		String queryDU = "INSERT INTO Arzttermine SET Datum = \'" + this.datum + "\', TF_ID = " 
				+ this.tfID + ", MA_ID = " + this.maID + ", PatientID = " + patientID 
				+ ", Eingetragen_Von = \'" + Start.aktuellerMitarbeiter + "\', Eingetragen_Am = \'" 
				+ aktuellesDatum + "\'";
		int x = stmtDU.executeUpdate(queryDU);
		stmtDU.close();
		
		if (x > 0) {          
			dispose();
		} else {          
			lblTitel.setText("Speicherung fehlgeschlagen");
			lblTitel1.setText("");
		}					
	}
	
}