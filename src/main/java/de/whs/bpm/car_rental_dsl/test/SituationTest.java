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
	public void testCustomerB_NoviceApproved() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 1, 2);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(20);
		customer.setDrivingLicense(3);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(1);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveNovice(true);
		runProcess(request);
		
		// Test the output data
		assertTrue(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 10);
		assertTrue(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), "Middle");
		assertEquals(request.getBasePrice(), 7000);
		assertEquals(request.getDiscount(), 1000);
		assertEquals(request.getFinalPrice(), 6000);
		assertEquals(request.getTotalPrice(), 6600);
		
		assertRemainingCars(garage, 2, 2, 0, 2);
	}
	
	@Test
	public void testCustomerB_NoviceDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 1, 2);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(20);
		customer.setDrivingLicense(3);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(1);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveNovice(false);
		runProcess(request);
		
		// Test the output data
		assertTrue(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 10);
		assertTrue(request.requiresNovicePermission());
		
		assertTrue(request.isDeclined());
		assertRemainingCars(garage, 2, 2, 1, 2);
	}
	
	@Test
	public void testCustomerC_UpgradeApproved() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(30);
		customer.setDrivingLicense(12);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(7);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), "Upper");
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Price is still calculated for "Middle" class (free upgrade)
		assertEquals(request.getCarClass(), "Middle");
		assertEquals(request.getBasePrice(), 38500);
		assertEquals(request.getDiscount(), 0);
		assertEquals(request.getFinalPrice(), 38500);
		assertEquals(request.getTotalPrice(), 38500);
		
		assertRemainingCars(garage, 2, 2, 0, 1);
	}
	
	@Test
	public void testCustomerC_UpgradeDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(30);
		customer.setDrivingLicense(12);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(7);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(false);
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), "Upper");
		assertTrue(request.isCarAvailable());
		
		assertTrue(request.isDeclined());	
		assertRemainingCars(garage, 2, 2, 0, 2);
	}
	
	@Test
	public void testCustomerD_UpgradeApproved() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 1);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(35);
		customer.setDrivingLicense(10);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(1);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), "Upper");
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Price is still calculated for "Middle" class (free upgrade)
		assertEquals(request.getCarClass(), "Middle");
		assertEquals(request.getBasePrice(), 7000);
		assertEquals(request.getDiscount(), 1000);
		assertEquals(request.getFinalPrice(), 6000);
		assertEquals(request.getTotalPrice(), 6000);
		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerD_UpgradeDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 1);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(35);
		customer.setDrivingLicense(10);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(1);
		request.setCarClass("Middle");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(false);
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), "Upper");
		assertTrue(request.isCarAvailable());
		
		assertTrue(request.isDeclined());		
		assertRemainingCars(garage, 2, 2, 0, 1);
	}
	
	@Test
	public void testCustomerE_UpperExists() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 1);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(46);
		customer.setDrivingLicense(20);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(6);
		request.setCarClass("Upper");	
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
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), "Upper");
		assertEquals(request.getBasePrice(), 49500);
		assertEquals(request.getDiscount(), 1000);
		assertEquals(request.getFinalPrice(), 48500);
		assertEquals(request.getTotalPrice(), 48500);
		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerE_DowngradePossible() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 1, 0);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(46);
		customer.setDrivingLicense(20);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(6);
		request.setCarClass("Upper");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Downgrade to "Middle" class and extra deduction
		assertEquals(request.getCarClass(), "Middle");
		assertEquals(request.getBasePrice(), 38500);
		assertEquals(request.getDiscount(), 1000);
		assertEquals(request.getFinalPrice(), 37500);
		assertEquals(request.getTotalPrice(), 33750);
		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerE_DowngradeDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 0);
		
		Customer customer = new Customer();
		customer.setNew(true);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(46);
		customer.setDrivingLicense(20);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(6);
		request.setCarClass("Upper");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertFalse(request.isCarAvailable());
		
		assertTrue(request.isDeclined());
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerF_UpperExists() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 1);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(true);
		customer.setHasSecurityTraining(true);
		customer.setAge(32);
		customer.setDrivingLicense(10);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(4);
		request.setCarClass("Upper");	
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
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), "Upper");
		assertEquals(request.getBasePrice(), 36000);
		assertEquals(request.getDiscount(), 9000);
		assertEquals(request.getFinalPrice(), 27000);
		assertEquals(request.getTotalPrice(), 27000);
		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerF_DowngradePossible() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 1, 0);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(true);
		customer.setHasSecurityTraining(true);
		customer.setAge(32);
		customer.setDrivingLicense(10);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(4);
		request.setCarClass("Upper");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Downgrade to "Middle" class and extra deduction
		assertEquals(request.getCarClass(), "Middle");
		assertEquals(request.getBasePrice(), 28000);
		assertEquals(request.getDiscount(), 7000);
		assertEquals(request.getFinalPrice(), 21000);
		assertEquals(request.getTotalPrice(), 18900);
		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerF_DowngradeDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 0);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(true);
		customer.setHasSecurityTraining(true);
		customer.setAge(32);
		customer.setDrivingLicense(10);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(4);
		request.setCarClass("Upper");	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertFalse(request.isNovice());
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertFalse(request.isCarAvailable());
		
		assertTrue(request.isDeclined());		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}

}
