package gui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * Die Klasse erstellt ein Fenster, wo die Datenverwaltung sowie
 * -Speicherung der Mitarbeiterdaten stattfindet
 * @author SRoot
 *
 */
public class Mitarbeiterdaten extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblTitel;
	private JLabel lblTitel1;
	private JLabel lblStrasseHausnr;
	private JLabel lblPLZ;
	private JLabel lblOrt;
	private JLabel lblTelefonNr;
	private JLabel lblEMail;
	private JLabel lblMessage;	
	private JLabel lblWarnung;
	private JTextField tfStrasseHausNr;
	private JTextField tfPLZ;
	private JTextField tfOrt;
	private JTextField tfTelefonNr;	
	private JTextField tfEMail;
	private JButton btnBeenden;
	
	private int maID; 

	/**
	 * Der Konstruktor erstellt das Dateneingabefenster
	 * und setzt in die Eingabefelder die Daten des eingeloggten Benutzers 
	 * aus der Datenbank, falls diese da enthalten sind.
	 * @param maID
	 * @param rolleID
	 * @param vorname
	 * @param nachname
	 * @param StrasseHauNr
	 * @param plz
	 * @param ort
	 * @param telefonNr
	 * @param eMail
	 */
	public Mitarbeiterdaten(int maID, int rolleID, String vorname, String nachname, 
			String StrasseHauNr, String plz, String ort, String telefonNr, String eMail) {
		setTitle("Datenverwaltung");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.maID = maID;
		
		lblTitel = new JLabel(vorname + " " + nachname);
		lblTitel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel.setHorizontalAlignment(JLabel.CENTER);
		lblTitel.setBounds(10, 15, 466, 20);
		contentPane.add(lblTitel);
		
		lblStrasseHausnr = new JLabel("Straße / Hausnummer");
		lblStrasseHausnr.setBounds(10, 65, 160, 13);
		contentPane.add(lblStrasseHausnr);
		
		tfStrasseHausNr = new JTextField();
		tfStrasseHausNr.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfPLZ.requestFocus();
					tfPLZ.selectAll();
				} 
			}
		});
		tfStrasseHausNr.setBounds(10, 80, 466, 19);
		tfStrasseHausNr.setText(StrasseHauNr);
		tfStrasseHausNr.requestFocus();
		tfStrasseHausNr.selectAll();
		contentPane.add(tfStrasseHausNr);
		tfStrasseHausNr.setColumns(10);
		
		lblPLZ = new JLabel("PLZ");
		lblPLZ.setBounds(10, 110, 160, 13);
		contentPane.add(lblPLZ);
		
		tfPLZ = new JTextField();
		tfPLZ.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfOrt.requestFocus();
					tfOrt.selectAll();
				} 
			}
		});
		tfPLZ.setText(plz);
		tfPLZ.setColumns(10);
		tfPLZ.setBounds(10, 125, 466, 19);
		contentPane.add(tfPLZ);
		
		lblOrt = new JLabel("Ort");
		lblOrt.setBounds(10, 155, 160, 13);
		contentPane.add(lblOrt);
		
		tfOrt = new JTextField();
		tfOrt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfTelefonNr.requestFocus();
					tfTelefonNr.selectAll();
				} 
			}
		});
		tfOrt.setText(ort);
		tfOrt.setColumns(10);
		tfOrt.setBounds(10, 170, 466, 19);
		contentPane.add(tfOrt);
		
		lblTelefonNr = new JLabel("Telefonnummer");
		lblTelefonNr.setBounds(10, 200, 160, 13);
		contentPane.add(lblTelefonNr);
		
		tfTelefonNr = new JTextField();
		tfTelefonNr.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfEMail.requestFocus();
					tfEMail.selectAll();
				} 
			}
		});
		tfTelefonNr.setText(telefonNr);
		tfTelefonNr.setColumns(10);
		tfTelefonNr.setBounds(10, 215, 466, 19);
		contentPane.add(tfTelefonNr);
		
		lblEMail = new JLabel("E-Mail");
		lblEMail.setBounds(10, 245, 160, 13);
		contentPane.add(lblEMail);
		
		tfEMail = new JTextField();
		tfEMail.setText(eMail);
		tfEMail.setColumns(10);
		tfEMail.setBounds(10, 260, 466, 19);
		contentPane.add(tfEMail);
		
		// Wenn der Button angeklickt wir, wird die Methode zum Datenspeichern aufgerufen
		JButton btnDatenSpeichern = new JButton("Daten speichern");
		btnDatenSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					datenSpeichern();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDatenSpeichern.setBounds(80, 320, 130, 21);
		contentPane.add(btnDatenSpeichern);
		
		lblMessage = new JLabel();
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblMessage.setVerticalAlignment(JLabel.BOTTOM);
		lblMessage.setHorizontalAlignment(JLabel.CENTER);
		lblMessage.setBounds(10, 345, 466, 13);
		contentPane.add(lblMessage);
		
		lblWarnung = new JLabel("Alle Felder müssen ausgefüllt werden!");
		lblWarnung.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblWarnung.setHorizontalAlignment(JLabel.CENTER);
		lblWarnung.setBounds(10, 290, 466, 13);
		contentPane.add(lblWarnung);
		
		/* Wenn der Button angeklickt wird, wird es überprüft, ob das Hauptfenster
		 * bereits existiert. Wenn ja, wird das Aktuelle Fenster einfach geschlossen,
		 * ansonsten wird ein Haupfenster erzeugt unt angezeigt, mit dem Unhalt je nach 
		 * aktueller Benutzerrolle.*/
		btnBeenden = new JButton("Beenden");
		btnBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Start.hFensterExistiert) {
					dispose();
				} else {
					if (rolleID == 1) {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									// Ein Hauptfenster für Ärzte
									Mitarbeiter hFenster = new Arzt(vorname, nachname);
									hFenster.setVisible(true);
									dispose();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else if (rolleID == 2) {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									// Ein Hauptfenster für Sprechstundenhilfen
									Mitarbeiter hFenster = new Sprechstundenhilfe(vorname, nachname);
									hFenster.setVisible(true);
									dispose();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		});
		btnBeenden.setBounds(270, 320, 130, 21);
		contentPane.add(btnBeenden);
		
		lblTitel1 = new JLabel("");
		lblTitel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel1.setBounds(10, 35, 466, 20);
		contentPane.add(lblTitel1);				
	}	
	
	/**
	 * Die Methode rüft, ob alle Datenfelder ausgefüllt sind, 
	 * wenn nicht, fordert zum Ausfüllen auf,
	 * wenn ja, speichert die eingegebenen Daten in die Datenbank. 
	 * @throws SQLException
	 */
	private void datenSpeichern() throws SQLException {
		
		String message = "Füllen Sie bitte das Feld aus!";
		
		if (tfStrasseHausNr.getText().equals("") || tfStrasseHausNr.getText().equals(message)) {
			tfStrasseHausNr.setText(message);
			tfStrasseHausNr.requestFocus();
			tfStrasseHausNr.selectAll();
		} else if (tfPLZ.getText().equals("") || tfPLZ.getText().equals(message)) {
			tfPLZ.setText(message);
			tfPLZ.requestFocus();
			tfPLZ.selectAll();
		} else if (tfOrt.getText().equals("") || tfOrt.getText().equals(message)) {
			tfOrt.setText(message);
			tfOrt.requestFocus();
			tfOrt.selectAll();
		} else if (tfTelefonNr.getText().equals("") || tfTelefonNr.getText().equals(message)) {
			tfTelefonNr.setText(message);
			tfTelefonNr.requestFocus();
			tfTelefonNr.selectAll();
		} else if (tfEMail.getText().equals("") || tfEMail.getText().equals(message)) {
			tfEMail.setText(message);
			tfEMail.requestFocus();
			tfEMail.selectAll();		
		} else {
			Start.connectionAufbauen();
			lblMessage.setText(Start.message);
			Statement stmtDS = Start.connection.createStatement();
			String queryDS = "UPDATE Mitarbeiter SET Strasse_HausNr = \'" + tfStrasseHausNr.getText() 
				+ "\', PLZ = \'" + tfPLZ.getText() + "\', Ort = \'" + tfOrt.getText() + "\', TelefonNr = \'" 
					+ tfTelefonNr.getText() + "\', EMail = \'" + tfEMail.getText() + "\' WHERE MA_ID = " + maID;
			int x = stmtDS.executeUpdate(queryDS);
			if (x > 0) {          				
				lblTitel1.setText("Daten erfolgreich gespeichert");
			}
		}
	}
}