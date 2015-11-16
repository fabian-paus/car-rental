package de.whs.bpm.car_rental_dsl;

import java.util.HashMap;
import java.util.Map;

public class Garage {
	
	private Map<String, Integer> garage = new HashMap<String, Integer>(); 
	
	public void setCount(String cartype, int value)
	{
		garage.put(cartype, value);
	}
	
	public boolean hasAny(String cartype)
	{
		return garage.get(cartype) != null && garage.get(cartype) > 0;
	}

	
}
