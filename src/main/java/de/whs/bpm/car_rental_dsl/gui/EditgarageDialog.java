package de.whs.bpm.car_rental_dsl.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.whs.bpm.car_rental_dsl.Garage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditgarageDialog extends JDialog {

	private static final String CANCEL = "Cancel";

	private static final String OK = "OK";

	private static final long serialVersionUID = -7670790502555107368L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField tfSmall;
	private JTextField tfCompact;
	private JTextField tfmiddle;
	private JTextField tfUpper;

	private Garage garage;
	public boolean save = false;

	/**
	 * Create the dialog.
	 */
	public EditgarageDialog(JFrame owner, Garage garage) {
		super(owner, true);
		this.garage = garage;
		final JDialog self = this;
		setBounds(100, 100, 259, 256);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblKleinwagen = new JLabel(App.SMALL);
			lblKleinwagen.setBounds(12, 13, 98, 16);
			contentPanel.add(lblKleinwagen);
		}
		{
			JLabel lblCompaktwagen = new JLabel(App.COMPACT);
			lblCompaktwagen.setBounds(12, 42, 111, 16);
			contentPanel.add(lblCompaktwagen);
		}
		{
			JLabel lblMitttelklasse = new JLabel(App.MIDDLE);
			lblMitttelklasse.setBounds(12, 71, 98, 16);
			contentPanel.add(lblMitttelklasse);
		}
		{
			JLabel lblUpperklasse = new JLabel(App.UPPER);
			lblUpperklasse.setBounds(12, 100, 86, 16);
			contentPanel.add(lblUpperklasse);
		}
		{
			tfSmall = new JTextField();
			tfSmall.setText(""+garage.getCount(Garage.SMALL));
			tfSmall.setBounds(118, 10, 116, 22);
			contentPanel.add(tfSmall);
			tfSmall.setColumns(10);
		}
		{
			tfCompact = new JTextField();
			tfCompact.setText(""+garage.getCount(Garage.COMPACT));
			tfCompact.setBounds(118, 42, 116, 22);
			contentPanel.add(tfCompact);
			tfCompact.setColumns(10);
		}
		{
			tfmiddle = new JTextField();
			tfmiddle.setText(""+garage.getCount(Garage.MIDDLE));
			tfmiddle.setBounds(118, 71, 116, 22);
			contentPanel.add(tfmiddle);
			tfmiddle.setColumns(10);
		}
		{
			tfUpper = new JTextField();
			tfUpper.setText(""+garage.getCount(Garage.UPPER));
			tfUpper.setBounds(118, 97, 116, 22);
			contentPanel.add(tfUpper);
			tfUpper.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(OK);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						save = true;
						self.setVisible(false);
					}
				});
				okButton.setActionCommand(OK);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(CANCEL);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						self.setVisible(false);
					}
				});
				cancelButton.setActionCommand(CANCEL);
				buttonPane.add(cancelButton);
			}
		}
	}






	public Garage getGarage() {
		garage.setCount(Garage.SMALL,Integer.parseInt(tfSmall.getText()));
		garage.setCount(Garage.COMPACT,Integer.parseInt(tfCompact.getText()));
		garage.setCount(Garage.MIDDLE,Integer.parseInt(tfmiddle.getText()));
		garage.setCount(Garage.UPPER,Integer.parseInt(tfUpper.getText()));

		return garage;
	}

}
