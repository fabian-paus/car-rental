package de.whs.bpm.car_rental_dsl;

import java.util.Calendar;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");
            
            Calendar rentalDate = Calendar.getInstance();
            rentalDate.set(2016, 2, 21);
            RentalRequest request = new RentalRequest(rentalDate, 5, CarClass.MIDDLE);
            RentalDay[] days = new RentalDay[8];
            for (int i = 0; i < days.length; ++i)
            {
            	days[i] = new RentalDay(request, i);
            	kSession.insert(days[i]);
            }
            
            kSession.fireAllRules();
            
            for (int i = 0; i < days.length; ++i)
            {
            	System.out.println(days[i]);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
