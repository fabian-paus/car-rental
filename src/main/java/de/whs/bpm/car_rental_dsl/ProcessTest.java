package de.whs.bpm.car_rental_dsl;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * This is a sample file to launch a process.
 */
public class ProcessTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-process");

            // start a new process instance
            kSession.startProcess("de.whs.bpm.car_rental_dsl.hello");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
