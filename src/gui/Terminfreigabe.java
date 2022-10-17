package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * Die Klasse erstellt je nach Eingangsparameter 
 * ein Terminfreigabefenster oder ein Informationsfenster
 * @author SRoot
 *
 */
public class Terminfreigabe extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblArzt;
	private JLabel lblPatient;
	private JLabel lblDatumZeit;
	private JLabel lblEingetragenAm;
	private JLabel lblZeile6;
	private JLabel lblEingetragenVon;
	private JPanel buttonPane;
	private JButton btnJa;
	private JButton btnNein;
	
	private int maID;	
	private int tfID;
	private int terminID;
	private String datum;
	
	
	/**
	 * Der Konstruktor erstellt ein Fenster zur Freigabe  
	 * der bereits belegten und noch nicht abgelaufenen Terminfenster
	 * @param maID
	 * @param tfID
	 * @param datum
	 */
	public Terminfreigabe(int maID, int tfID, String datum) {
		
		setTitle("Terminfreigabe");
		setModal(true);
		setSize(450, 250);
		setLocationRelativeTo(null);
		
		this.maID = maID;
		this.tfID = tfID;
		this.datum = Start.datumUmkehren(datum);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lblArzt = new JLabel("Arzt/Ärztin: ");
		lblArzt.setHorizontalAlignment(SwingConstants.CENTER);
		lblArzt.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblArzt.setBounds(10, 15, 416, 20);
		contentPanel.add(lblArzt);

		lblPatient = new JLabel("Patient/-in: ");
		lblPatient.setHorizontalAlignment(SwingConstants.CENTER);
		lblPatient.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPatient.setBounds(10, 35, 416, 20);
		contentPanel.add(lblPatient);
		
		lblDatumZeit = new JLabel("am ... um ...");
		lblDatumZeit.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatumZeit.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDatumZeit.setBounds(10, 55, 416, 20);
		contentPanel.add(lblDatumZeit);
		
		lblEingetragenAm = new JLabel("Eingetragen am ...");
		lblEingetragenAm.setHorizontalAlignment(SwingConstants.CENTER);
		lblEingetragenAm.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEingetragenAm.setBounds(10, 90, 416, 20);
		contentPanel.add(lblEingetragenAm);
		lblEingetragenVon = new JLabel("von ...");
		lblEingetragenVon.setHorizontalAlignment(SwingConstants.CENTER);
		lblEingetragenVon.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEingetragenVon.setBounds(10, 110, 416, 20);
		contentPanel.add(lblEingetragenVon);
		
		lblZeile6 = new JLabel("Möchten Sie den Termin freigeben?");
		lblZeile6.setHorizontalAlignment(SwingConstants.CENTER);
		lblZeile6.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblZeile6.setBounds(10, 145, 416, 20);
		contentPanel.add(lblZeile6);

		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPane.setBorder(new EmptyBorder(0, 5, 5, 5));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnJa = new JButton("Ja");
		btnJa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					/* Hier wird eine Methode aufgerufen, 
					 * die den ausgewälten Termineintrag aus der Datenbank löscht. */
					terminFreigeben();  
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		buttonPane.add(btnJa);
		getRootPane().setDefaultButton(btnJa);

		btnNein = new JButton("Nein");
		btnNein.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // Das Fenster wird geschlossen, ohne die Datenbank zu kontaktieren
			}
		});
		buttonPane.add(btnNein);
		
		/* Hier wirde eine Methode aufgerufen, die alle Termininformationen 
		 * zu einem bestimmten Termin von der Datenbank abruft */
		try {
			terminInfoEinlesen();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Der Konstruktor erstellt ein Fenster zum Einsehen aller Termindaten
	 * der berets abgelaufenen belegten Terminfenster
	 * @param maID
	 * @param tfID
	 * @param datum
	 * @param marker
	 */
	public Terminfreigabe(int maID, int tfID, String datum, boolean marker) {
		this(tfID, tfID, datum);
		buttonPane.remove(btnJa);
		contentPanel.remove(lblZeile6);
		btnNein.setText("Schließen");
		
		this.maID = maID;
		this.tfID = tfID;
		this.datum = Start.datumUmkehren(datum);
		
		try {
			terminInfoEinlesen();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Die Methode ruft von der Datenbank den Arztnamen, 
	 * den Patientennamen und die einmalige TerminID zu einer Kombination
	 * aus einer TerminfensterID, einer MitarbeiterID 
	 * und eines Datums, und gibt diese im Dialogfenster aus.
	 * @throws SQLException
	 */
	private void terminInfoEinlesen() throws SQLException { 
		
		Start.connectionAufbauen();
		
		Statement stmtTIE = Start.connection.createStatement();
		String queryTIE = "SELECT Arzttermine.TerminID, Arzttermine.Eingetragen_Am, Arzttermine.Eingetragen_Von, "
				+ "Mitarbeiter.Vorname AS \'MVorname\', Mitarbeiter.Nachname AS \'MNachname\', "
				+ "Patienten.Vorname AS \'PVorname\', Patienten.Nachname AS \'PNachname\', "
				+ "Terminfenster.Von_Bis FROM Arzttermine " 
				+ "JOIN Mitarbeiter USING(MA_ID) " 
				+ "JOIN Patienten USING(PatientID) "
				+ "JOIN Terminfenster USING(TF_ID) "
				+ "WHERE Datum = \'" + this.datum + "\' AND TF_ID = " + this.tfID + " AND MA_ID = " + this.maID;
		ResultSet rsTIE = stmtTIE.executeQuery(queryTIE);
				
		while (rsTIE.next()) {
			this.terminID = rsTIE.getInt("TerminID");
			lblArzt.setText("Arzt/Ärztin: " + rsTIE.getString("MVorname") 
				+ " " + rsTIE.getString("MNachname")); 
			lblPatient.setText("Patient/-in: " + rsTIE.getString("PVorname") 
				+ " " + rsTIE.getString("PNachname"));
			lblDatumZeit.setText("Termin am " + Start.datumUmkehren(datum) + ", " + rsTIE.getString("Von_Bis"));
			lblEingetragenAm.setText("Eingetragen am " + rsTIE.getString("Eingetragen_Am"));
			lblEingetragenVon.setText("von " + rsTIE.getString("Eingetragen_Von"));
		}		
		stmtTIE.close();
		rsTIE.close();		
	}
	
	/**
	 * Diese Methode löscht einen Datenbankeintrag aus der Tabelle mit den Ärzteterminen 
	 * zu einem einmaligen TerminID und schließt beim Erfolg das Dialogfenster
	 * @throws SQLException
	 */
	private void terminFreigeben() throws SQLException {
		
		Start.connectionAufbauen();
		
		Statement stmtTF = Start.connection.createStatement();
		String queryTF = "DELETE FROM Arzttermine WHERE TerminID = " + this.terminID;
		int x = stmtTF.executeUpdate(queryTF);
		
		if (x > 0) { 
			dispose();
		}		
	}
	
}