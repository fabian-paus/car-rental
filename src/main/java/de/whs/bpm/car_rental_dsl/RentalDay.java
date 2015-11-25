package de.whs.bpm.car_rental_dsl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentalDay {

	private int dayIndex;
	private Calendar day;
	private int price;
	
	public RentalDay(Calendar startDate, int dayIndex) {
		super();
		this.dayIndex = dayIndex;
		this.day = (Calendar)startDate.clone();
		this.day.add(Calendar.DATE, dayIndex);
	}
	
	public Calendar getDay() {
		return day;
	}
	public void setDay(Calendar day) {
		this.day = day;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public long getDayIndex() {
		return dayIndex + 1;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format.format(day.getTime());            
		return "RentalDay [dayIndex=" + getDayIndex()
				+ ", day=" + date1 + ", price=" + price + "]";
	}
	
}
