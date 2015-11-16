package de.whs.bpm.car_rental_dsl.test;

import java.util.Calendar;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;

import de.whs.bpm.car_rental_dsl.CarClass;
import de.whs.bpm.car_rental_dsl.Customer;
import de.whs.bpm.car_rental_dsl.RentalRequest;

public class SituationTest extends JbpmJUnitBaseTestCase{

	@Test
	public void testCustomerA() {
		
		// TODO: Garage modellieren
		
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
		request.setCarClass(CarClass.MIDDLE);
		
		// TODO: Objekt in Drools reinwerfen und Ergebnisse prüfen
		
		assertEquals(request.getCarClass(), CarClass.MIDDLE);
		assertTrue(request.getBasePrice() == 33250);
		assertTrue(request.getDiscount() == 1663); // Richtig runden!
		assertTrue(request.getFinalPrice() == 31587);
		assertTrue(request.getTotalPrice() == 31587);
		
		//fail("Not yet implemented");
	}

}
