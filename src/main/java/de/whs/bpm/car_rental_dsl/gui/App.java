package de.whs.bpm.car_rental_dsl.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItemHandler;

import de.whs.bpm.car_rental_dsl.ApprovalWorkItemHandler;
import de.whs.bpm.car_rental_dsl.Customer;
import de.whs.bpm.car_rental_dsl.Garage;
import de.whs.bpm.car_rental_dsl.RentalRequest;

public class App {

	private static final String GARAGE_BEARBEITEN = "Garage bearbeiten";
	private static final String ANFRAGEN = "Anfragen";
	private static final String SOLL_AUTOMATK_HABEN = "Soll Automatk haben";
	private static final String FARZEUGKLASSE = "Farzeugklasse";
	private static final String DAUER = "Dauer";
	private static final String START_DATUM = "Startdatum";
	private static final String BEARBEITEN = "Bearbeiten";
	private static final String LÖSCHEN = "L\u00F6schen";
	private static final String HINZUFÜGEN = "Hinzuf\u00FCgen";
	private JFrame frame;
	private JTextField textField;
	
	private Garage garage;

	
	public final static String SMALL = "Kleinwagen";
	public final static String COMPACT = "Kompaktwagen";
	public final static String MIDDLE = "Mittelklasse Wagen";
	public final static String UPPER = "Oberklasse Wagen";
	
	private Map<String, String> WagenklasseToCarclass;
	WorkItemHandler mWorkItemHandler;
	KieContainer kContainer;
	private JLabel lblKleinwagen;
	private JLabel lblKompaktwagen;
	private JLabel lblMittelklasseWagen;
	private JLabel lblUpperklasseWagen;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
					window.frame.setTitle("Autovermietung");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		garage = new Garage();
		garage.setCount(Garage.SMALL, 2);
		garage.setCount(Garage.COMPACT, 2);
		garage.setCount(Garage.MIDDLE, 2);
		garage.setCount(Garage.UPPER, 2);
		
		WagenklasseToCarclass = new HashMap<String, String>();
		WagenklasseToCarclass.put(SMALL, Garage.SMALL);
		WagenklasseToCarclass.put(COMPACT, Garage.COMPACT);
		WagenklasseToCarclass.put(MIDDLE, Garage.MIDDLE);
		WagenklasseToCarclass.put(UPPER, Garage.UPPER);
		
		initialize();
		
		mWorkItemHandler = new ApprovalWorkItemHandler() {
			
			@Override
			public boolean approveUpgrade() {
				//default icon, custom title
				Object[] options = {"Genehmigen",
                "Ablehnen"};
				int n = JOptionPane.showOptionDialog(
				    frame,
				    "Es steht kein Auto in der angeforderten Klasse zur Verfügung. Genehmigen Sie ein kostenloses Upgrade?",
				    "Upgrade?",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null, options,options[0]);

					return n==0; 

			}
			
			@Override
			public boolean approveNovice() {
				Object[] options = {"Genehmigen",
                "Ablehnen"};
				
				int n = JOptionPane.showOptionDialog(
					    frame,
					    "Diese Fahrzeigklasse benötigt eine Genehmigung.",
					    "Fahanfänger!",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null, options,options[0]);

						return n==0; 
			}
		};
		
        KieServices ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 358, 742);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final DefaultListModel<Customer> listModel;
		listModel = new DefaultListModel<Customer>();
		final JList<Customer> list = new JList<Customer>(listModel);

	
		list.setBounds(12, 13, 314, 182);
		frame.getContentPane().add(list);
		
		final JFrame self = frame;
		
		JButton btnHinzufgen = new JButton(HINZUFÜGEN);
		btnHinzufgen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddCustomerDialog dialog = new AddCustomerDialog(self,null);
			
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				dialog.setModal(true);
				if(dialog.save)
					listModel.addElement(dialog.getCustomer());
				
			}
		});
		btnHinzufgen.setBounds(12, 197, 97, 25);
		frame.getContentPane().add(btnHinzufgen);
		
		JButton btnLschen = new JButton(LÖSCHEN);
		btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.remove(list.getSelectedIndex());
				
			}
		});
		btnLschen.setBounds(121, 197, 97, 25);
		frame.getContentPane().add(btnLschen);
		
		JButton btnBearbeiten = new JButton(BEARBEITEN);
		btnBearbeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddCustomerDialog dialog = new AddCustomerDialog(self,listModel.getElementAt(list.getSelectedIndex()));
				
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				dialog.setModal(true);
				
				if(dialog.save){
					Customer customer = dialog.getCustomer();
					Customer c = listModel.getElementAt(list.getSelectedIndex());
					c.setName(customer.getName());
					c.setAge(customer.getAge());
					c.setDrivingLicense(customer.getDrivingLicense());
					c.setHasReclamation(customer.isHasReclamation());
					c.setHasSecurityTraining(customer.isHasSecurityTraining());
					c.setNew(customer.isNewCustomer());
				}
				
			}
		});
		btnBearbeiten.setBounds(229, 197, 97, 25);
		frame.getContentPane().add(btnBearbeiten);
		
	    final JDatePickerImpl datePicker;
        UtilDateModel model = new UtilDateModel();
        
        model.setDate(2016, 2, 21);
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
      
        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
        
        datePicker.setBounds(109, 241, 217, 31);
        frame.getContentPane().add(datePicker);
        
        JLabel lblStartDatum = new JLabel(START_DATUM);
        lblStartDatum.setBounds(12, 241, 84, 16);
        frame.getContentPane().add(lblStartDatum);
        
        JLabel lblDauer = new JLabel(DAUER);
        lblDauer.setBounds(12, 294, 56, 16);
        frame.getContentPane().add(lblDauer);
        
        textField = new JTextField();
        textField.setBounds(109, 291, 67, 22);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
        JLabel lblFarzeugklasse = new JLabel(FARZEUGKLASSE);
        lblFarzeugklasse.setBounds(12, 331, 84, 16);
        frame.getContentPane().add(lblFarzeugklasse);
        
        final JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {SMALL, COMPACT, MIDDLE, UPPER}));
        comboBox.setBounds(109, 328, 217, 22);
        frame.getContentPane().add(comboBox);
        
        final JCheckBox chckbxSollAutomatkHaben = new JCheckBox(SOLL_AUTOMATK_HABEN);
        chckbxSollAutomatkHaben.setBounds(105, 367, 221, 25);
        frame.getContentPane().add(chckbxSollAutomatkHaben);
        
        JButton btnAnfragen = new JButton(ANFRAGEN);
        btnAnfragen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		RentalRequest request = new RentalRequest();
        		Date d = (Date) datePicker.getModel().getValue();
        		Calendar c = Calendar.getInstance();
        		c.setTime(d);
        		request.setStartDate(c);
        		request.setDurationInDays(Integer.parseInt(textField.getText()));
        		request.setCarClass(WagenklasseToCarclass.get(comboBox.getSelectedItem()));	
        		
        		for (Object customer : listModel.toArray())
        			request.addCustomer((Customer)customer );
        		
        		request.setGarage(garage);	
        		request.setAutomatic(chckbxSollAutomatkHaben.isSelected());
        		
            	KieSession kSession = kContainer.newKieSession("ksession-process");
            	kSession.getWorkItemManager().registerWorkItemHandler("Human Task", mWorkItemHandler);
        		Map<String, Object> parameterMap = new HashMap<String, Object>();
            	parameterMap.put("request", request);
        		kSession.startProcess("de.whs.bpm.car_rental_dsl.price", parameterMap);
        		kSession.fireAllRules();
        		kSession.dispose();	
        		
        		ResultDialog dialog = new ResultDialog(frame,request);
				
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				dialog.setModal(true);
        		
        		
        		
        		
        		updateGarageView();
        	}
        });
        btnAnfragen.setBounds(12, 402, 314, 78);
        frame.getContentPane().add(btnAnfragen);
        
       
        
        lblKleinwagen = new JLabel(SMALL+": "+garage.getCount(Garage.SMALL));
        lblKleinwagen.setBounds(12, 531, 164, 16);
        frame.getContentPane().add(lblKleinwagen);
        
        lblKompaktwagen = new JLabel(COMPACT+": "+garage.getCount(Garage.COMPACT));
        lblKompaktwagen.setBounds(12, 560, 164, 16);
        frame.getContentPane().add(lblKompaktwagen);
        
        lblMittelklasseWagen = new JLabel(MIDDLE+": "+garage.getCount(Garage.MIDDLE));
        lblMittelklasseWagen.setBounds(12, 589, 164, 16);
        frame.getContentPane().add(lblMittelklasseWagen);
        
        lblUpperklasseWagen = new JLabel(UPPER+": "+garage.getCount(Garage.UPPER));
        lblUpperklasseWagen.setBounds(12, 618, 164, 16);
        frame.getContentPane().add(lblUpperklasseWagen);
        
        
        
        JButton btnGerageBearbeiten = new JButton(GARAGE_BEARBEITEN);
        btnGerageBearbeiten.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		EditGarageDialog dialog = new EditGarageDialog(self,garage);
    			
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				dialog.setModal(true);
				
				
				
				if(dialog.save){
					garage = dialog.getGarage();
					updateGarageView();
				}
        	}

			
        });
        btnGerageBearbeiten.setBounds(12, 493, 164, 25);
        frame.getContentPane().add(btnGerageBearbeiten);
        
	}
	
	private void updateGarageView() {
		lblKleinwagen.setText(SMALL+": "+garage.getCount(Garage.SMALL));
		lblKompaktwagen.setText(COMPACT+": "+garage.getCount(Garage.COMPACT));
		lblMittelklasseWagen.setText(MIDDLE+": "+garage.getCount(Garage.MIDDLE));
		lblUpperklasseWagen.setText(UPPER+": "+garage.getCount(Garage.UPPER));
	}
	
	
}
