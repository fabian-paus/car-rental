package de.whs.bpm.car_rental_dsl.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import de.whs.bpm.car_rental_dsl.RentalRequest;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ResultDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	

	/**
	 * Create the dialog.
	 */
	public ResultDialog(JFrame frame,RentalRequest result) {
		super(frame);
		setBounds(100, 100, 524, 357);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		final JDialog self = this;
		
		String kosten = "";
		String klasse = "";
		String upgrade = "";
		String declined = "";
		
		if(result.isDeclined())
		{
			 declined = "Anfrage abgelehnt";
			 
		}
		else
		{
			declined = "Anfrage angeommen";
			kosten = "Die Kosten betragen: "+ String.format("%.2f", result.getTotalPrice()/100.0f)+"€";
			klasse="Sie bekommen ein Auto der Klasse: ";
			
			if (result.hasFreeClassUpgrade()) {
				klasse+= result.getUpgradeClass();
			}
			else {
				klasse+= result.getCarClass();
			}
			
			
			
		}
		
		
		
		JLabel lblAngenommen = new JLabel(declined);
		lblAngenommen.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblAngenommen.setBounds(12, 13, 482, 54);
		contentPanel.add(lblAngenommen);
		
	
		JLabel lblKosten = new JLabel(kosten);
		lblKosten.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblKosten.setBounds(12, 150, 482, 74);
		contentPanel.add(lblKosten);
		
		
		JLabel lblCarclass = new JLabel(klasse);
		lblCarclass.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblCarclass.setBounds(12, 80, 482, 54);
		contentPanel.add(lblCarclass);
		
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						self.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			
		}
	}
}
