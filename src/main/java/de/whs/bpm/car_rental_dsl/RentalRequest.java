package de.whs.bpm.car_rental_dsl;

import java.util.Calendar;

public class RentalRequest {

	private Calendar startDate;
	private int durationInDays;
	private CarClass carClass;
	
	public RentalRequest(Calendar startDate, int durationInDays,
			CarClass carClass) {
		super();
		this.startDate = startDate;
		this.durationInDays = durationInDays;
		this.carClass = carClass;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public int getDurationInDays() {
		return durationInDays;
	}
	public void setDurationInDays(int durationInDays) {
		this.durationInDays = durationInDays;
	}
	public CarClass getCarClass() {
		return carClass;
	}
	public void setCarClass(CarClass carClass) {
		this.carClass = carClass;
	}
}
