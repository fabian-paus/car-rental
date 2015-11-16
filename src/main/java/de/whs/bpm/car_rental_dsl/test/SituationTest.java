package de.whs.bpm.car_rental_dsl.test;

import java.util.Calendar;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;

import de.whs.bpm.car_rental_dsl.Customer;
import de.whs.bpm.car_rental_dsl.Garage;
import de.whs.bpm.car_rental_dsl.RentalRequest;

public class SituationTest extends JbpmJUnitBaseTestCase{

	@Test
	public void testCustomerA() {
		
		Garage garage = new Garage();
		garage.setCount("Small", 2);
		garage.setCount("Compact", 2);
		garage.setCount("Middle", 2);
		garage.setCount("Upper", 2);
		
//		\item Klasse: Mittel
//		\item Dauer: 5 Tage
//		\item Automatik: Nein
//		\item Sicherheitstraining: Ja
//		
//		\item Alter: > 21 Jahre
//		\item Neukunde: Nein
//		\item Alte Reklamation: Nein
//		\item F�hrerscheindauer: 20 Jahre
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(true);
		customer.setAge(40);
		customer.setDrivingLicense(20);
		
		RentalRequest request = new RentalRequest();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 3 - 1, 16);
		request.setStartDate(startDate);
		request.setDurationInDays(5);
		request.setCarClass("Middle");
		
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// TODO: Objekt in Drools reinwerfen und Ergebnisse pr�fen
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-rules");
		
    	kSession.insert(request);
		
		for (Customer c : request.getCustomers()) {
			kSession.insert(c);
		}
		kSession.fireAllRules();
		
		assertFalse(request.isNovice());
		assertTrue(request.getExtraChargePercent() == 0);
		assertTrue(request.isNoviceCheckPassed());
		assertFalse(request.isRequiresNovicePermission());
		
		assertEquals(request.getCarClass(), "Middle");
//		assertTrue(request.getBasePrice() == 33250);
//		assertTrue(request.getDiscount() == 1663); // Richtig runden!
//		assertTrue(request.getFinalPrice() == 31587);
//		assertTrue(request.getTotalPrice() == 31587);
		
		//fail("Not yet implemented");
	}

}
