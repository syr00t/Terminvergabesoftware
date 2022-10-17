package gui;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Klasse, in der das Einloggen und dass Passwortfestlegen mit den entsprechenden
 * Überprüfungen des Benutzernamens und des Passwortes stattfinden
 * @author SRoot
 *
 */
public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfBenutzername;
	private JPasswordField tfPasswort;
	private JPasswordField tfPasswortWiederholen;
	private JLabel lblMessage;
	private JLabel lblTitel;
	private JLabel lblTitel1;
	private JLabel lblPasswortWiederholen;
	private JButton btnEinloggen;
	private JButton btnNeuesPasswort;
	private JCheckBox chckbxEinblenden;
	
	private int maID, rolleID; 
	private String vorname, nachname, strasseHausNr, plz, ort, telefonNr, eMail;
	
	/**
	 * Der Konstruktor erstellt das Login-Fenster.
	 */
	public Login() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(440, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblMessage = new JLabel();
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblMessage.setBounds(10, 240, 400, 15);
		lblMessage.setVerticalAlignment(JLabel.BOTTOM);
		lblMessage.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(this.lblMessage);
		
		JLabel lblBenutzername = new JLabel("Benutzername");
		lblBenutzername.setBounds(10, 80, 239, 13);
		contentPane.add(lblBenutzername);
		
		tfBenutzername = new JTextField();
		tfBenutzername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfPasswort.requestFocus();
					tfPasswort.selectAll();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
		tfBenutzername.setBounds(10, 95, 400, 19);
		contentPane.add(tfBenutzername);
		tfBenutzername.setColumns(10);
		
		JLabel lblPasswort = new JLabel("Passwort");
		lblPasswort.setBounds(10, 125, 239, 13);
		contentPane.add(lblPasswort);
		
		chckbxEinblenden = new JCheckBox("Einblenden");
		chckbxEinblenden.setFont(new Font("Tahoma", Font.PLAIN, 10));
		chckbxEinblenden.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				echoCharAendern();
			}
		});
		chckbxEinblenden.setBounds(327, 139, 93, 21);
		contentPane.add(chckbxEinblenden);
		
		tfPasswort = new JPasswordField();
		if (chckbxEinblenden.isSelected()) {
			tfPasswort.setEchoChar('\u0000');
		}
		tfPasswort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} 
			}
		});
		tfPasswort.setColumns(10);
		tfPasswort.setBounds(10, 140, 310, 19);
		contentPane.add(tfPasswort);
		
		btnEinloggen = new JButton("Einloggen");
		btnEinloggen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					einloggen();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnEinloggen.setBounds(45, 215, 120, 21);
		contentPane.add(btnEinloggen);
		
		lblTitel = new JLabel("Loggen Sie sich ein");
		lblTitel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel.setBounds(10, 15, 400, 19);
		contentPane.add(lblTitel);
		
		lblTitel1 = new JLabel("oder legen Sie ein neues Passwort fest");
		lblTitel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel1.setBounds(10, 35, 400, 19);
		contentPane.add(lblTitel1);
		
		lblPasswortWiederholen = new JLabel("Passwort wiederholen");
		lblPasswortWiederholen.setBounds(10, 160, 239, 13);
		lblPasswortWiederholen.setVisible(false);
		contentPane.add(lblPasswortWiederholen);
		
		tfPasswortWiederholen = new JPasswordField();
		if (chckbxEinblenden.isSelected()) {
			tfPasswortWiederholen.setEchoChar('\u0000');
		}
		tfPasswortWiederholen.setColumns(10);
		tfPasswortWiederholen.setBounds(10, 175, 400, 19);
		tfPasswortWiederholen.setVisible(false);
		contentPane.add(tfPasswortWiederholen);
		
		btnNeuesPasswort = new JButton("Passwort festlegen");
		btnNeuesPasswort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					/*Wenn das Passwortfenster nicht leer ist, das Passwortwiederholungsfenster aktiv
					 * und deren Inhalt übereinstimmt, wird die Methode zum Spechern des Passwortes
					 * aufgerufen, ansonsten die zum Passwortfestlegen*/
					if (tfPasswortWiederholen.getPassword().length > 0
					& Arrays.equals(tfPasswort.getPassword(), tfPasswortWiederholen.getPassword())) {
						passwortSpeichern();
					} else {
						passwortFestlegen();
					}					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNeuesPasswort.setBounds(205, 215, 170, 21);
		contentPane.add(btnNeuesPasswort);				
	}
		
	/**
	 * Prüft, ob der eingegebene Benutzername in der DB enthalten ist
	 * @throws SQLException
	 */
	private void benutzerPruefen() throws SQLException {
		
		Start.connectionAufbauen();
		lblMessage.setText(Start.message); // Informiert den Benutzer, ob eine Verbindung mit der Datebank erfolgte
		
		Statement stmtBP = Start.connection.createStatement();
		String queryBP = "SELECT Benutzername FROM Benutzer";
		ResultSet rsBP = stmtBP.executeQuery(queryBP);
		while (rsBP.next()) {
			/* Wenn der eingegebene Benutzername in der Datenbank zu finden ist,
			 * wird dieser in der entsprechenden Variablen in der Klasse Start gespeichert */
//			if (tfBenutzername.getText().equals(rsBP.getString("Benutzername"))) {
			if (tfBenutzername.getText().trim().equals(rsBP.getString("Benutzername"))) {
				Start.aktuellerBenutzer = rsBP.getString("Benutzername");
				tfPasswort.requestFocus();
				break;
			} 
		}
		/* Wenn der Variablen noch kein Wert zugewiesen wurde, bedeutet es, 
		 * dass dieser Benutzer in der Datenbank nicht enthalten ist */
		if (Start.aktuellerBenutzer == null) {
			lblTitel.setText("Benutzer nicht gefunden");
			lblTitel1.setText("");
			tfBenutzername.requestFocus();
			tfBenutzername.selectAll();
		}		
		rsBP.close();
		stmtBP.close();
	}
	
	/**
	 * Das eingegebene Passwort wird hier verschlüsselt, dann wird geprüft, 
	 * ob as Ergebnis mit dem in der DB gespeicherten Wert übereinstimmt 
	 * @throws SQLException
	 */
	private void einloggen() throws SQLException {
		
		String pwVerschluesselt = "";			
		
		Start.connectionAufbauen();
		lblMessage.setText(Start.message);
		
		benutzerPruefen(); //Als Erstes wird geprüft, ob der Benutzer in der Datenbank existiert
		
		Statement stmtPW = Start.connection.createStatement();
		String queryPW = "SELECT Passwort FROM Benutzer WHERE Benutzername = \'" + Start.aktuellerBenutzer + "\'";
		ResultSet rsPW = stmtPW.executeQuery(queryPW);
		
		// Hier wird die Verschlüsselungsmethode aufgerufen 
		try {
			pwVerschluesselt = passwortVerschluesseln(toString(tfPasswort.getPassword()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/* Falls das Verschlüsslungsergebnis mit dem in der Datenbank gespeicherten übereinstimmt,
		 * wird die Methode aufgerufen, die Prüft, ob die entsprechenden Benutzerdaten bereits 
		 * in der Datenbank enthalten sind.*/
		while (rsPW.next()) {
			if (pwVerschluesselt.equals(rsPW.getString("Passwort"))) {
				benutzerdatenAnfragen();
			} else {
				lblTitel.setText("Passwort stimmt nicht überein");
				lblTitel1.setText("");
				tfPasswort.requestFocus();
				tfPasswort.selectAll();
			}						
		}				
		rsPW.close();
		stmtPW.close();		
	}
		
	/**
	 * Fordert zum Eingeben und Wiederholen eines neuen Passwortes
	 * und ruft die Methode auf, die das Passwort auf Mindestanfortderungen prüft
	 * @throws SQLException
	 */
	private void passwortFestlegen() throws SQLException {
		
		Start.connectionAufbauen();
		lblMessage.setText(Start.message);
		
		benutzerPruefen(); //Als Erstes wird geprüft, ob der Benutzer in der Datenbank existiert

		/*Falls der Benutzer mit dem Benutzernamen existiert, wird geprüft, ob im Passwortfenster
		 * bereits etwas steht. Wenn ja, wird die Methode zur Passwordprüfung aufgerufen.
		 * Falls die Prüfung bestanden ist, wird das Fenster zur Passwortwiederholung eingeblendet
		 * und die Beschriftung des entsprechenden Buttons geändert. Ansonsten wird ein Informationsfenster
		 * mit Passwortsmindestanforderungen erzeugt und angezeigt.*/
		if (Start.aktuellerBenutzer != null) {
			if (toString(tfPasswort.getPassword()).equals("")) {
				lblTitel.setText("Bitte ein neues Password eingeben");
				lblTitel1.setText("");
			} else {
				if (passwortPruefen()) {
					lblTitel.setText("Bitte das neue Password wiederholen");
					lblTitel1.setText("");
					lblPasswortWiederholen.setVisible(true);
					tfPasswortWiederholen.setVisible(true);
					tfPasswortWiederholen.setText("");
					tfPasswortWiederholen.requestFocus();
					tfPasswortWiederholen.selectAll();
					btnNeuesPasswort.setText("Passwort speichern");
				} else {
					JOptionPane.showMessageDialog(null, "Passwort muss mind. 12 Zeichen lang sein, "
							+ "mind. 1 Groß- und Kleinbuchstaben, 1 Ziffer und 1 Sonderzeichen enthalten");
					tfPasswort.requestFocus();
					tfPasswort.selectAll();
				}
			}
		}		
	}	
	
	/**
	 * Verschlüsselt das neue Passwort und speichert es in die DB
	 * @throws SQLException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private void passwortSpeichern() throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		Start.connectionAufbauen();
		lblMessage.setText(Start.message);
		
		// Der Inhalt des Passwortfensters wird in ein String umgewandelt und verschlüsselt
		String zuSpeichern = passwortVerschluesseln(toString(tfPasswort.getPassword()));

		Statement stmtPS = Start.connection.createStatement();
		String queryPS = "UPDATE Benutzer SET Passwort = \'" + zuSpeichern + "\' WHERE Benutzername = \'" + tfBenutzername.getText() + "\'";
		int x = stmtPS.executeUpdate(queryPS);
		// Wenn die Speicherung gelang, öfnnet sich die Möglichkeit, sich mit dem neuen Passwort einzuloggen.
		if (x > 0) {          
			lblTitel.setText("Neues Passwort erfolgreich gespeichert.");
			lblTitel1.setText("Jetzt können Sie sich einloggen");
			btnNeuesPasswort.setText("Passwort festlegen");
			lblPasswortWiederholen.setVisible(false);
			tfPasswortWiederholen.setVisible(false);
			tfPasswortWiederholen.setText("");
			tfBenutzername.requestFocus();
			tfBenutzername.selectAll();
			Start.aktuellerBenutzer = tfBenutzername.getText();
		} else {          
			lblTitel.setText("Speicherung fehlgeschlagen");
			lblTitel1.setText("");
		}		
		stmtPS.close();		
	}
		
	/**
	 * Prüft das neue Passwort auf Mindestanforderunegen, 
	 * und zwar: Mindestlänge 12 Zeichen, mindestens 1 Ziffer, 
	 * 1 Groß- und 1 Kleinbuschtabe, 1 Sonderzeichen
	 * @return boolean
	 */
	private boolean passwortPruefen() {
		
		char ch;
		char[] passwortZeichen = tfPasswort.getPassword();		
		String str = toString(passwortZeichen);
		String sonderzeichen = "@!*#,;?+-_.=~^%(){}[]|:/";
		
		// Boolsche Flags
		boolean enthaeltGrossbuchstaben = false;
		boolean enthaeltKleinbuchstaben = false;
		boolean enthaeltZiffer = false;
		boolean enthaeltSonderzeichen = false;
		boolean bestanden = false;
		
		if (str.length() >= 12) { 												// Überprüfung auf Mindestlänge
			for (int i = 0; i < str.length(); i++) {
				ch = str.charAt(i);
				if (Character.isUpperCase(ch)) { 								// Überprüfung auf Großbuchstaben
					enthaeltGrossbuchstaben = true;
				} else if (Character.isLowerCase(ch)) { 						// Überprüfung auf Kleinbuchstaben
					enthaeltKleinbuchstaben = true;
				} else if (Character.isDigit(ch)) { 							// Überprüfung auf Ziffer
					enthaeltZiffer = true;
				} else if (sonderzeichen.contains(Character.toString(ch))) { 	// Überprüfung auf Sonderzeichen
					enthaeltSonderzeichen = true;
				}
			}
		}
		
		// Wenn alle Tests bestanden, wird das Hauptflag auf "true" umgeschaltet 
		if (enthaeltGrossbuchstaben == true & enthaeltKleinbuchstaben == true 
				& enthaeltZiffer == true & enthaeltSonderzeichen == true) {
			bestanden = true;
		} 		
		return bestanden;	
	}
		
	/**
	 * Die Methode ruft eine weitere auf, die Passwort mit SHA512 verschlüsselt
	 * @param passwort
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	
 	private String passwortVerschluesseln(String passwort) 
 			throws NoSuchAlgorithmException, UnsupportedEncodingException {
	        return SHA512(passwort);
	    }
		
 	/**
 	 * Die folgenden zwei Methoden sind von dem Projektleiter zur Verfügung 
 	 * gestellt und bilden ein SHA512-Verschlüsselungsmechanismus 
 	 * @param text
 	 * @return
 	 * @throws NoSuchAlgorithmException
 	 * @throws UnsupportedEncodingException
 	 */
	public static String SHA512(String text) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		        MessageDigest md;
		        md = MessageDigest.getInstance("SHA-512");
		        byte[] sha1hash = new byte[40];
		        md.update(text.getBytes("UTF-8"), 0, text.length());
		        sha1hash = md.digest();
		        return convertToHex(sha1hash);
		    }
	
	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer(); 
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
	
	/**
	 * Die Methode ruft die Mitarbeiterdaten aus der DB ab.
	 * Wenn es noch keine da sind, wird ein Dateneingabefenster erzeugt.
	 * Ansonsten geht es weiter zum Haupfenster der Anwendung.
	 * @throws SQLException
	 */
	private void benutzerdatenAnfragen() throws SQLException {
		
		Start.connectionAufbauen();
		lblMessage.setText(Start.message);
		
		// Alle Mitrabeiterinformationen zu dem aktuellen Benutzer abgerufen
		Statement stmtBD = Start.connection.createStatement();
		String queryBD = "SELECT Benutzer.Benutzername, Benutzer.MA_ID, Benutzer.RolleID, "
				+ "Mitarbeiter.Vorname, Mitarbeiter.Nachname, Mitarbeiter.Strasse_HausNr, "
				+ "Mitarbeiter.PLZ, Mitarbeiter.Ort, Mitarbeiter.TelefonNr, Mitarbeiter.EMail "
				+ "FROM Benutzer "
				+ "LEFT JOIN Mitarbeiter USING(MA_ID) "
				+ "WHERE Benutzername = \'" + Start.aktuellerBenutzer + "\'";
		ResultSet rsBD = stmtBD.executeQuery(queryBD);
		
		// Die abgerufene Werte werden entsprechenden Variablen zugewiesen
		while (rsBD.next()) {
			maID = rsBD.getInt("MA_ID");
			rolleID = rsBD.getInt("RolleID");
			vorname = rsBD.getString("Vorname");
			nachname = rsBD.getString("Nachname");
			strasseHausNr = rsBD.getString("Strasse_HausNr");
			plz = rsBD.getString("PLZ");
			ort = rsBD.getString("Ort");
			telefonNr = rsBD.getString("TelefonNr");
			eMail = rsBD.getString("EMail");
		}
		
		Start.aktuellerMitarbeiter = vorname + " " + nachname;
		
		// Wenn eine der Variablen keinen Inhalt hat, wird das Fenster zum Datenabgeben aufgerufen.
		// Ansonsten wird das Hauptfenster der Anwendung aufgerufen, mit dem Inhalt je nach Benutzerrolle. 
		if (strasseHausNr == null || plz == null || ort == null || telefonNr == null || eMail == null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// Ein neues Dateneingabefenster wird aufgerufen und dieses geschlossen.
						Mitarbeiterdaten datenFenster = new Mitarbeiterdaten(maID, rolleID, vorname, nachname,
								strasseHausNr, plz, ort, telefonNr, eMail);
						datenFenster.setVisible(true);
						btnEinloggen.setEnabled(false);
						btnNeuesPasswort.setEnabled(false);
						dispose();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			if (rolleID == 1) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							// Ein neues Hauptfenster für Ärzte wird aufgerufen und dieses geschlossen.
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
							// Ein neues Hauptfenster für Sprechstundenhilfen wird aufgerufen und dieses geschlossen.
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
		rsBD.close();
		stmtBD.close();
	}
	
	/**
	 *  Die Methode schaltet EchoChars bei Passworteingabe um
	 */
	private void echoCharAendern() {		
		if (chckbxEinblenden.isSelected()) { 				// Wenn die Checkbox gecheckt ist, dann Eingabe eingeblendet
			tfPasswort.setEchoChar('\u0000');
			tfPasswortWiederholen.setEchoChar('\u0000');
		} else { 											// Ansonsten die Eingabe durch EchoChars ausgeblendet			
			tfPasswort.setEchoChar('•');
			tfPasswortWiederholen.setEchoChar('•');
		}
	}
	
	/**
	 * Wandelt den Inhalt des Passwortfensters in ein String um
	 * @param a
	 * @return String
	 */
	private String toString(char[] a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
        } 
        return sb.toString();
    }
}