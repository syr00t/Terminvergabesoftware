package gui;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, die das Programm startet
 * @author SRoot
 *
 */
public class Start {
	
	public static String aktuellerBenutzer;
	public static String aktuellerMitarbeiter;
	public static boolean hFensterExistiert;
	public static String message;
	
	public static Connection connection;
	public static String url = "jdbc:mysql://localhost:3306/terminvergabedb";
	public static String user = "root";
	public static String pwd = "";
	

	/**
	 * Startet die Anwendung.
	 * Ruft das Login-Fenster auf.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login login = new Login();
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Wenn noch keine Verbindung besteht, baut eine auf.
	 */
	public static void connectionAufbauen() {
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(url, user, pwd);
				// Die Mitteilung wird später von verschiedenen GUI-Fenstern übernommen
				message = "Verbindung zur Datenbank erfolgreich aufgebaut"; 
			} catch (SQLException e) {
				message = "Verbindung zur Datenbank fehlgeschlagen";
				e.printStackTrace();
			}
		}
	}

	/**
	 * Dreht das Datum um im Sinne dd-MM-yyyy -> yyy-MM-dd und andersherum.
	 * Wird in anderen Klassen bei verschiedenen Methoden eingesetzt.
	 * 
	 * @param datum
	 * @return String
	 */
	public static String datumUmkehren(String datum) {
		String[] datumArray = datum.split("-");
		List<String> datumList = new ArrayList<String>();
		for (int i = datumArray.length - 1; i >= 0; i--) {
			datumList.add(datumArray[i]);
		}
		return String.join("-", datumList);
	}
	
}