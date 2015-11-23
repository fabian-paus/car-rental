package de.whs.bpm.car_rental_dsl.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import de.whs.bpm.car_rental_dsl.Customer;

public class AddCustomerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4641941153704616937L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfName;
	private JTextField tfDriving;
	private JTextField tfAge;
	private JCheckBox cbxNew;
	private JCheckBox cbxReclamation;
	private JCheckBox cbxSaftytraining;
	public boolean save = false;

	
	public Customer getCustomer()
	{
		Customer c = new Customer();
		c.setName(tfName.getText());
		c.setAge(Integer.parseInt(tfAge.getText()));
		c.setDrivingLicense(Integer.parseInt(tfDriving.getText()));
		c.setHasReclamation(cbxReclamation.isSelected());
		c.setHasSecurityTraining(cbxSaftytraining.isSelected());
		c.setNew(cbxNew.isSelected());
		return c;
	
	}

	/**
	 * Create the dialog.
	 */
	public AddCustomerDialog(JFrame owner,Customer c) {
		super(owner, true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		tfName = new JTextField();
		tfName.setBounds(69, 13, 116, 22);
		contentPanel.add(tfName);
		tfName.setColumns(10);
		
		
		final JDialog self = this;
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(12, 16, 56, 16);
		contentPanel.add(lblName);
		
		cbxNew = new JCheckBox("Neuer Kunde");
		cbxNew.setBounds(69, 44, 113, 25);
		contentPanel.add(cbxNew);
		
		
		
		cbxReclamation = new JCheckBox("Hat Reklamation");
		cbxReclamation.setBounds(69, 74, 144, 25);
		contentPanel.add(cbxReclamation);
		
		
		
		cbxSaftytraining = new JCheckBox("Hat Sicherheitstraining");
		cbxSaftytraining.setBounds(72, 104, 113, 25);
		contentPanel.add(cbxSaftytraining);
		
		
		
		JLabel lblFhrerschein = new JLabel("F\u00FChrerschein");
		lblFhrerschein.setBounds(0, 136, 88, 16);
		contentPanel.add(lblFhrerschein);
		
		tfDriving = new JTextField();
		tfDriving.setBounds(82, 138, 116, 22);
		contentPanel.add(tfDriving);
		tfDriving.setColumns(10);
		
		
		
		JLabel lblAlter = new JLabel("Alter");
		lblAlter.setBounds(0, 172, 56, 16);
		contentPanel.add(lblAlter);
		
		
		tfAge = new JTextField();
		tfAge.setBounds(69, 169, 116, 22);
		contentPanel.add(tfAge);
		tfAge.setColumns(10);
		
		if(c != null)
		{
			tfName.setText(c.getName());
			tfDriving.setText(""+c.getDrivingLicense());
			tfAge.setText(""+c.getAge());
			cbxSaftytraining.setSelected(c.isHasSecurityTraining());
			cbxReclamation.setSelected(c.isHasReclamation());
			cbxNew.setSelected(c.isNewCustomer());
		}
		
		
		
	
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						save = true;
						self.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						self.setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
