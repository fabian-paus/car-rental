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
	private ApprovalWorkItemHandler workItemHandler;
	
	@Before
	public void setupKieRuntime() {
		
		Map<String, ResourceType> resources = new HashMap<String, ResourceType>();
		resources.put("process/rental-process.bpmn", ResourceType.BPMN2);
		resources.put("process/rental-rules.drl", ResourceType.DRL);
		runtimeManager = createRuntimeManager(resources);
		
		runtimeEngine = getRuntimeEngine(null);
		kSession = runtimeEngine.getKieSession();
		
		workItemHandler = new ApprovalWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
	}
	
	@After
	public void cleanupKieRuntime() {
		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	private void runProcess(RentalRequest request) {
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.put("request", request);
    	
		kSession.startProcess("de.whs.bpm.car_rental_dsl.price", parameterMap);
		kSession.fireAllRules();
	}
	
	private void assertRemainingCars(Garage garage, int small, int compact, int middle, int upper) {
		
		assertEquals(garage.getCount(Garage.SMALL), small);
		assertEquals(garage.getCount(Garage.COMPACT), compact);
		assertEquals(garage.getCount(Garage.MIDDLE), middle);
		assertEquals(garage.getCount(Garage.UPPER), upper);
	}
	
	private Garage makeGarage(int small, int compact, int middle, int upper) {
		
		Garage garage = new Garage();
		
		garage.setCount(Garage.SMALL, small);
		garage.setCount(Garage.COMPACT, compact);
		garage.setCount(Garage.MIDDLE, middle);
		garage.setCount(Garage.UPPER, upper);
		
		return garage;
	}
	
	private Calendar getStartDate() {
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 3 - 1, 21);
		return startDate;
	}

	@Test
	public void testCustomerA() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 2, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(true);
		customer.setAge(40);
		customer.setDrivingLicense(20);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(5);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertFalse(request.requiresUpdgradePermission());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), "Middle");
		assertEquals(request.getBasePrice(), 33250);
		assertEquals(request.getDiscount(), 1663);
		assertEquals(request.getFinalPrice(), 31587);
		assertEquals(request.getTotalPrice(), 31587);
		
		assertRemainingCars(garage, 2, 2, 1, 2);
	}
	
	@Test
	public void testCustomerA_DeclinedTest() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 2, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(true);
		customer.setAge(20);
		customer.setDrivingLicense(3);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(5);
		request.setCarClass("Middle");
		
		request.addCustomer(customer);
		request.setGarage(garage);
    	
    	workItemHandler.setApproveNovice(false);
		runProcess(request);
		
		assertTrue(request.isNovice());
		assertTrue(request.getExtraChargePercent() == 10);
		assertTrue(request.requiresNovicePermission());
		
		assertTrue(request.isDeclined());
	}

}
