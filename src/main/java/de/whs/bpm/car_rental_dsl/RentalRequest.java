package de.whs.bpm.car_rental_dsl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RentalRequest {

	// Input
	private Calendar startDate;
	private int durationInDays;
	private String carClass;

	
	private List<Customer> customers = new ArrayList<Customer>();
	private Garage garage = new Garage();
	
	// Output
	private boolean isNovice;
	private int extraChargePercent;
	private boolean noviceCheckPassed;
	private boolean requiresNovicePermission;
	
	private boolean requiresUpdgradePermission;
	
	private int basePrice;
	private int discount;
	private int finalPrice;
	private int totalPrice;
	
	public RentalRequest(Calendar startDate, int durationInDays,
			String carClass) {
		super();
		this.startDate = startDate;
		this.durationInDays = durationInDays;
		this.carClass = carClass;
		this.basePrice = 0;
	}
	
	public RentalRequest() {
		
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
	public String getCarClass() {
		return carClass;
	}
	public void setCarClass(String carClass) {
		this.carClass = carClass;
	}

	public float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}
	
	public void addToBasePrice(int add) {
		this.basePrice += add;
	}
	

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(int finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	

	public List<Customer> getCustomers() {
		return customers;
	}

	public void addCustomer(Customer customer) {
		this.customers.add(customer);
	}

	public Garage getGarage() {
		return garage;
	}

	public void setGarage(Garage garage) {
		this.garage = garage;
	}
	
	

	public boolean isNovice() {
		return isNovice;
	}

	public void setNovice(boolean isNovice) {
		this.isNovice = isNovice;
	}

	public int getExtraChargePercent() {
		return extraChargePercent;
	}

	public void setExtraChargePercent(int extraChargePercent) {
		this.extraChargePercent = extraChargePercent;
	}

	public boolean isNoviceCheckPassed() {
		return noviceCheckPassed;
	}

	public void setNoviceCheckPassed(boolean noviceCheckPassed) {
		this.noviceCheckPassed = noviceCheckPassed;
	}

	public boolean requiresNovicePermission() {
		return requiresNovicePermission;
	}

	public void setRequiresNovicePermission(boolean requiresNovicePermission) {
		this.requiresNovicePermission = requiresNovicePermission;
	}
	
	public boolean requiresUpdgradePermission() {
		return requiresUpdgradePermission;
	}

	public void setRequiresUpdgradePermission(boolean requiresUpdgradePermission) {
		this.requiresUpdgradePermission = requiresUpdgradePermission;
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
