package de.whs.bpm.car_rental_dsl;

import java.util.HashMap;
import java.util.Map;

public class Garage {
	
	private Map<String, Integer> garage = new HashMap<String, Integer>(); 
	
	public void setCount(String carClass, int value)
	{
		garage.put(carClass, value);
	}
	
	public int getCount(String carClass) {
		Integer count = garage.get(carClass);
		return count == null ? 0 : count;
	}
	
	public boolean hasAny(String carClass)
	{
		return getCount(carClass) > 0;
	}

	
}
