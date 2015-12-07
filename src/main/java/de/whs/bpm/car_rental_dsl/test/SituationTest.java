package de.whs.bpm.car_rental_dsl.test;

import org.junit.Test;

import de.whs.bpm.car_rental_dsl.Customer;
import de.whs.bpm.car_rental_dsl.Garage;
import de.whs.bpm.car_rental_dsl.RentalRequest;

public class SituationTest extends BaseTest {

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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), Garage.MIDDLE);
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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveNovice(true);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 10);
		assertTrue(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), Garage.MIDDLE);
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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveNovice(false);
		runProcess(request);
		
		// Test the output data
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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), Garage.UPPER);
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Price is still calculated for "Middle" class (free upgrade)
		assertEquals(request.getCarClass(), Garage.MIDDLE);
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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(false);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), Garage.UPPER);
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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), Garage.UPPER);
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Price is still calculated for "Middle" class (free upgrade)
		assertEquals(request.getCarClass(), Garage.MIDDLE);
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
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		workItemHandler.setApproveUpgrade(false);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertEquals(request.getUpgradeClass(), Garage.UPPER);
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
		request.setCarClass(Garage.UPPER);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), Garage.UPPER);
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
		request.setCarClass(Garage.UPPER);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Downgrade to "Middle" class and extra deduction
		assertEquals(request.getCarClass(), Garage.MIDDLE);
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
		request.setCarClass(Garage.UPPER);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
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
		request.setCarClass(Garage.UPPER);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), Garage.UPPER);
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
		request.setCarClass(Garage.UPPER);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Downgrade to "Middle" class and extra deduction
		assertEquals(request.getCarClass(), Garage.MIDDLE);
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
		request.setCarClass(Garage.UPPER);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 0);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 10);
		assertFalse(request.hasFreeClassUpgrade());
		assertFalse(request.isCarAvailable());
		
		assertTrue(request.isDeclined());		
		assertRemainingCars(garage, 2, 2, 0, 0);
	}
	
	@Test
	public void testCustomerG() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 0);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(true);
		customer.setAge(25);
		customer.setDrivingLicense(6);
		
		Customer wife = new Customer();
		wife.setNew(true);
		wife.setHasReclamation(false);
		wife.setHasSecurityTraining(false);
		wife.setAge(20);
		wife.setDrivingLicense(2);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(1);
		request.setCarClass(Garage.COMPACT);	
		
		request.addCustomer(customer);
		request.addCustomer(wife);
		
		request.setGarage(garage);
		
		// Execute the process
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 10);
		assertFalse(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertFalse(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		assertEquals(request.getCarClass(), Garage.COMPACT);
		assertEquals(request.getBasePrice(), 5000);
		assertEquals(request.getDiscount(), 0);
		assertEquals(request.getFinalPrice(), 5000);
		assertEquals(request.getTotalPrice(), 5500);
		
		assertRemainingCars(garage, 2, 1, 0, 0);
	}

}
