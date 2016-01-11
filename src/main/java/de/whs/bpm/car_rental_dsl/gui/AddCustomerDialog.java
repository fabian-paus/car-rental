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

	private static final String CANCEL = "Abbrechen";
	private static final String OK = "OK";
	private static final String ALTER = "Alter:";
	private static final String FUEHRERSCHEIN = "F\u00FChrerschein:";
	private static final String HAT_SICHERHEITSTRAINING = "Hat Sicherheitstraining";
	private static final String HAT_REKLAMATION = "Hat Reklamation";
	private static final String NEUER_KUNDE = "Neuer Kunde";
	private static final String NAME2 = "Name:";
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
	public AddCustomerDialog(final JFrame owner,final Customer c) {
		super(owner, true);
		setBounds(100, 100, 331, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		tfName = new JTextField();
		tfName.setBounds(94, 13, 116, 22);
		contentPanel.add(tfName);
		tfName.setColumns(10);
		
		
		final JDialog self = this;
		
		JLabel lblName = new JLabel(NAME2);
		lblName.setBounds(12, 16, 56, 16);
		contentPanel.add(lblName);
		
		cbxNew = new JCheckBox(NEUER_KUNDE);
		cbxNew.setBounds(91, 44, 113, 25);
		contentPanel.add(cbxNew);
		
		
		
		cbxReclamation = new JCheckBox(HAT_REKLAMATION);
		cbxReclamation.setBounds(91, 74, 144, 25);
		contentPanel.add(cbxReclamation);
		
		
		
		cbxSaftytraining = new JCheckBox(HAT_SICHERHEITSTRAINING);
		cbxSaftytraining.setBounds(91, 104, 190, 25);
		contentPanel.add(cbxSaftytraining);
		
		
		
		JLabel lblFhrerschein = new JLabel(FUEHRERSCHEIN);
		lblFhrerschein.setBounds(12, 136, 88, 16);
		contentPanel.add(lblFhrerschein);
		
		tfDriving = new JTextField();
		tfDriving.setBounds(94, 138, 116, 22);
		contentPanel.add(tfDriving);
		tfDriving.setColumns(10);
		
		
		
		JLabel lblAlter = new JLabel(ALTER);
		lblAlter.setBounds(12, 172, 56, 16);
		contentPanel.add(lblAlter);
		
		
		tfAge = new JTextField();
		tfAge.setBounds(94, 169, 116, 22);
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
				JButton okButton = new JButton(OK);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(tfName.getText() == "" || tfAge.getText() == "" || tfDriving.getText()== ""){
							JOptionPane.showMessageDialog(owner, "Bitte alle Felder ausfüllen.");
							return;
						}
						save = true;
						c.setName(tfName.getText());
						c.setAge(Integer.parseInt(tfAge.getText()));
						c.setDrivingLicense(Integer.parseInt(tfDriving.getText()));
						c.setHasReclamation(cbxReclamation.isSelected());
						c.setHasSecurityTraining(cbxSaftytraining.isSelected());
						c.setNew(cbxNew.isSelected());
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
}
