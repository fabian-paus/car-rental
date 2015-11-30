package de.whs.bpm.car_rental_dsl.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;

import de.whs.bpm.car_rental_dsl.Garage;
import de.whs.bpm.car_rental_dsl.RentalRequest;

public class BaseTest extends JbpmJUnitBaseTestCase {
	
	protected RuntimeManager runtimeManager;
	protected RuntimeEngine runtimeEngine;
	protected KieSession kSession;
	protected TestApprovalHandler workItemHandler;
	
	@Before
	public void setupKieRuntime() {
		
		Map<String, ResourceType> resources = new HashMap<String, ResourceType>();
		resources.put("process/rental-process.bpmn", ResourceType.BPMN2);
		resources.put("process/rental-language.dsl", ResourceType.DSL);
		resources.put("process/rental-rules-dsl.dslr", ResourceType.DSLR);
//		resources.put("rules/rental-rules.drl", ResourceType.DRL);
		runtimeManager = createRuntimeManager(resources);
		
		runtimeEngine = getRuntimeEngine(null);
		kSession = runtimeEngine.getKieSession();
		
		workItemHandler = new TestApprovalHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
	}
	
	@After
	public void cleanupKieRuntime() {
		
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
		runtimeManager.close();
	}
	
	protected void runProcess(RentalRequest request) {
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
    	parameterMap.put("request", request);
    	
		kSession.startProcess("de.whs.bpm.car_rental_dsl.price", parameterMap);
		kSession.fireAllRules();
	}
	
	protected void assertRemainingCars(Garage garage, int small, int compact, int middle, int upper) {
		
		assertEquals(garage.getCount(Garage.SMALL), small);
		assertEquals(garage.getCount(Garage.COMPACT), compact);
		assertEquals(garage.getCount(Garage.MIDDLE), middle);
		assertEquals(garage.getCount(Garage.UPPER), upper);
	}
	
	protected Garage makeGarage(int small, int compact, int middle, int upper) {
		
		Garage garage = new Garage();
		
		garage.setCount(Garage.SMALL, small);
		garage.setCount(Garage.COMPACT, compact);
		garage.setCount(Garage.MIDDLE, middle);
		garage.setCount(Garage.UPPER, upper);
		
		return garage;
	}
	
	protected Calendar getStartDate() {
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 3 - 1, 21);
		return startDate;
	}

}
