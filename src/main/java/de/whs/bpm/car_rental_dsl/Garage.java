package de.whs.bpm.car_rental_dsl;

import java.util.HashMap;
import java.util.Map;

public class Garage {
	
	public static final String SMALL = "Small";
	public static final String COMPACT = "Compact";
	public static final String MIDDLE = "Middle";
	public static final String UPPER = "Upper";
	
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

	public void takeOut(String carClass) {
		int count = getCount(carClass);
		if (count == 0)
			throw new IllegalStateException("No cars of class " + carClass + " are in the garage");
		garage.put(carClass, count - 1);
	}
	
}
