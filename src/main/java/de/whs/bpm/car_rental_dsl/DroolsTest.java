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

            // go !
            Message message = new Message();
            message.setMessage("Hello World");
            message.setStatus(Message.HELLO);
            kSession.insert(message);
            kSession.fireAllRules();
            
            Calendar rentalDate = Calendar.getInstance();
            rentalDate.set(2016, 2, 21);
            RentalRequest request = new RentalRequest(rentalDate, 5, CarClass.MIDDLE);
            RentalDay[] days = new RentalDay[5];
            for (int i = 0; i < 5; ++i)
            {
            	days[i] = new RentalDay(request, i);
            	kSession.insert(days[i]);
            }
            
            kSession.fireAllRules();
            
            for (int i = 0; i < 5; ++i)
            {
            	System.out.println(days[i]);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

}
