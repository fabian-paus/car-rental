package de.whs.bpm.car_rental_dsl.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;

import de.whs.bpm.car_rental_dsl.Customer;
import de.whs.bpm.car_rental_dsl.Garage;
import de.whs.bpm.car_rental_dsl.RentalRequest;

public class SituationTest extends JbpmJUnitBaseTestCase {
	
	private RuntimeManager runtimeManager;
	private RuntimeEngine runtimeEngine;
	private KieSession kSession;
	
	@Before
	public void setupKieRuntime() {
		
		Map<String, ResourceType> resources = new HashMap<String, ResourceType>();
		resources.put("process/rental-process.bpmn", ResourceType.BPMN2);
		resources.put("process/rental-rules.drl", ResourceType.DRL);
		runtimeManager = createRuntimeManager(resources);
		
		runtimeEngine = getRuntimeEngine(null);
		kSession = runtimeEngine.getKieSession();
	}
	
	@After
	public void cleanupKieRuntime() {
		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}

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
//		\item Führerscheindauer: 20 Jahre
		
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
    	
    	kSession.insert(request);
		
		for (Customer c : request.getCustomers()) {
			kSession.insert(c);
		}
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.put("request", request);
    	
		kSession.startProcess("de.whs.bpm.car_rental_dsl.price", parameterMap);
		kSession.fireAllRules();
		
		assertFalse(request.isNovice());
		assertTrue(request.getExtraChargePercent() == 0);
		assertTrue(request.isNoviceCheckPassed());
		assertFalse(request.isRequiresNovicePermission());
		
		System.out.println("Unit Test");
		System.out.println(request.isNovice());
		System.out.println(request.getExtraChargePercent());
		System.out.println(request.isNoviceCheckPassed());
		System.out.println(request.isRequiresNovicePermission());
		
		assertEquals(request.getCarClass(), "Middle");
//		assertTrue(request.getBasePrice() == 33250);
//		assertTrue(request.getDiscount() == 1663); // Richtig runden!
//		assertTrue(request.getFinalPrice() == 31587);
//		assertTrue(request.getTotalPrice() == 31587);
	}
	
	@Test
	public void testCustomerA_2() {
		
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
//		\item Führerscheindauer: 20 Jahre
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(true);
		customer.setAge(20);
		customer.setDrivingLicense(3);
		
		RentalRequest request = new RentalRequest();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 3 - 1, 16);
		request.setStartDate(startDate);
		request.setDurationInDays(5);
		request.setCarClass("Middle");
		
		request.addCustomer(customer);
		request.setGarage(garage);
    	
    	kSession.insert(request);
		
		for (Customer c : request.getCustomers()) {
			kSession.insert(c);
		}
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.put("request", request);
    	
		kSession.startProcess("de.whs.bpm.car_rental_dsl.price", parameterMap);
		kSession.fireAllRules();
		
		assertTrue(request.isNovice());
		assertTrue(request.getExtraChargePercent() == 10);
		assertFalse(request.isNoviceCheckPassed());
		assertTrue(request.isRequiresNovicePermission());
		
		assertEquals(request.getCarClass(), "Middle");
//		assertTrue(request.getBasePrice() == 33250);
//		assertTrue(request.getDiscount() == 1663); // Richtig runden!
//		assertTrue(request.getFinalPrice() == 31587);
//		assertTrue(request.getTotalPrice() == 31587);
	}

}
