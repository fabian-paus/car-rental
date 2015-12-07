package de.whs.bpm.car_rental_dsl.test;

import org.junit.Test;

import de.whs.bpm.car_rental_dsl.Customer;
import de.whs.bpm.car_rental_dsl.Garage;
import de.whs.bpm.car_rental_dsl.RentalRequest;


public class ExtraTest extends BaseTest {

	@Test
	public void testAutomaticFee() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 2, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
		customer.setHasReclamation(false);
		customer.setHasSecurityTraining(false);
		customer.setAge(40);
		customer.setDrivingLicense(20);
		
		RentalRequest request = new RentalRequest();
		request.setStartDate(getStartDate());
		request.setDurationInDays(5);
		request.setCarClass(Garage.MIDDLE);	
		request.addCustomer(customer);
		request.setGarage(garage);
		
		request.setAutomatic(true);
		
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
		assertEquals(request.getDiscount(), 0);
		assertEquals(request.getFinalPrice(), 34913);
		assertEquals(request.getTotalPrice(), 34913);
		
		assertRemainingCars(garage, 2, 2, 1, 2);
	}
	
	@Test
	public void testBothApproval_BothApproved() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
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
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 10);
		assertTrue(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertFalse(request.isDeclined());
		
		// Free upgrade to Upper class
		assertEquals(request.getCarClass(), Garage.MIDDLE);
		assertEquals(request.getBasePrice(), 7000);
		assertEquals(request.getDiscount(), 0);
		assertEquals(request.getFinalPrice(), 7000);
		assertEquals(request.getTotalPrice(), 7700);
		
		assertRemainingCars(garage, 2, 2, 0, 1);
	}
	
	@Test
	public void testBothApproval_NoviceDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
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
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 10);
		assertTrue(request.requiresNovicePermission());
		
		assertTrue(request.isDeclined());
		assertRemainingCars(garage, 2, 2, 0, 2);
	}
	
	@Test
	public void testBothApproval_UpgradeDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
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
		workItemHandler.setApproveUpgrade(false);
		runProcess(request);
		
		// Test the output data
		assertEquals(request.getExtraChargePercent(), 10);
		assertTrue(request.requiresNovicePermission());
		
		assertEquals(request.getExtraDeductionPercent(), 0);
		assertTrue(request.hasFreeClassUpgrade());
		assertTrue(request.isCarAvailable());
		
		assertTrue(request.isDeclined());
		assertRemainingCars(garage, 2, 2, 0, 2);
	}
	
	@Test
	public void testBothApproval_BothDeclined() {
		
		// Create input data
		Garage garage = makeGarage(2, 2, 0, 2);
		
		Customer customer = new Customer();
		customer.setNew(false);
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
		workItemHandler.setApproveUpgrade(true);
		runProcess(request);
		
		// Test the output data
		assertTrue(request.isDeclined());
		assertRemainingCars(garage, 2, 2, 0, 2);
	}
}
