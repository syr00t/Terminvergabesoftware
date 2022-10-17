package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * Die Klasse erbt von der Klasse Mitarbeiter
 * und ändert die Funktionalitäten des dort 
 * erstellten Fensters ab
 * @author SRoot
 *
 */
public class Sprechstundenhilfe extends Mitarbeiter {

	private static final long serialVersionUID = 1L;

	/**
	 * Der Konstruktor ändert etwas den Inhalt 
	 * der im Oberklass-Konstruktor erstellten GUI
	 * @param vorname
	 * @param nachname
	 */
	public Sprechstundenhilfe(String vorname, String nachname) {
		super(vorname, nachname);
		setTitle("Sprechstundenhilfen : Terminvergabe");

		lblTitel1 = new JLabel();
		ebene1.add(lblTitel1);
		lblTitel1.setText("Sie können nun Termine verwalten für");
		lblTitel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitel1.setBounds(0, 35, 600, 19);

		/* Hier wird ein das vorher deaktiverte Button reaktiviert und beschriftet.
		 * Durch dessen Anklicken wird ein GUI-Fenster aufgerufen, das eine Suche 
		 * nach zukünftigen Terminen für einen ausgewählten Patienten ermöglicht*/
		btnButton4.setVisible(true);
		btnButton4.setText("Zukünftige Termine suchen");
		btnButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Terminsuche tbFenster = new Terminsuche();
							tbFenster.setVisible(true);								
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
}
