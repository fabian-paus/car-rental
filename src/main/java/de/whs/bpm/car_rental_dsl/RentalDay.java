package de.whs.bpm.car_rental_dsl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentalDay {

	private RentalRequest request;
	private int dayIndex;
	private Calendar day;
	private float price;
	
	public RentalDay(RentalRequest request, int dayIndex) {
		super();
		this.request = request;
		this.dayIndex = dayIndex;
		this.day = (Calendar)request.getStartDate().clone();
		this.day.add(Calendar.DATE, dayIndex);
	}
	
	public RentalRequest getRequest() {
		return request;
	}
	public void setRequest(RentalRequest request) {
		this.request = request;
	}
	public Calendar getDay() {
		return day;
	}
	public void setDay(Calendar day) {
		this.day = day;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	public long getDayIndex() {
		return dayIndex + 1;
	}
	
	public CarClass getCarClass() {
		return request.getCarClass();
	}
	
	public boolean isWeekend() {
		return day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format.format(day.getTime());            
		return "RentalDay [dayIndex=" + getDayIndex()
				+ ", day=" + date1 + ", price=" + price + "]";
	}
	
}
