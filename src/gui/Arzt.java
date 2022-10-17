package gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Die Klasse erbt von der Klasse Mitarbeiter
 * und ändert die Funktionalitäten des dort 
 * erstellten Fensters ab
 * @author SRoot
 *
 */
public class Arzt extends Mitarbeiter {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Der Konstruktor ändert etwas den Inhalt 
	 * der im Oberklass-Konstruktor erstellten GUI
	 * @param vorname
	 * @param nachname
	 */
	public Arzt(String vorname, String nachname) {
		super(vorname, nachname);
		setTitle("Ärzte : Terminvergabe");
		
		lblTitel1 = new JLabel();
		ebene1.add(lblTitel1);
		lblTitel1.setText("Sie können nun Ihre Termine verwalten");
		lblTitel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel1.setBounds(0, 35, 600, 19);

		// Hier wird die Combobox mit Ärztenamen augeblendet
		aerzteComboBox.setSelectedItem((Object) (vorname + " " + nachname));
		aerzteComboBox.setVisible(false);
		lblArzt.setVisible(false);
	

		
	}

	
}



