package de.whs.bpm.car_rental_dsl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentalRequest {

	private Calendar startDate;
	private int durationInDays;
	private CarClass carClass;
	private float basePrice;
	
	public RentalRequest(Calendar startDate, int durationInDays,
			CarClass carClass) {
		super();
		this.startDate = startDate;
		this.durationInDays = durationInDays;
		this.carClass = carClass;
		this.basePrice = 0.0f;
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

	public float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}
	
	public void addToBasePrice(float add) {
		this.basePrice += add;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format.format(startDate.getTime());     
		return "RentalRequest [startDate=" + date1 + ", durationInDays="
				+ durationInDays + ", carClass=" + carClass + ", basePrice="
				+ basePrice + "]";
	}
	
	
}
