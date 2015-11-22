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
	private boolean automatic;

	
	private List<Customer> customers = new ArrayList<Customer>();
	private Garage garage = new Garage();
	
	// Output
	private boolean isNovice;
	private int extraChargePercent;
	private boolean requiresNovicePermission;
	
	private int extraDeductionPercent;
	private String upgradeClass;
	private boolean carAvailable;
	
	private boolean declined;
	
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
	
	public RentalDay[] getDays() {
		RentalDay[] days = new RentalDay[durationInDays];
        for (int i = 0; i < days.length; ++i)
        {
        	days[i] = new RentalDay(this, i);
        }
        return days;
	}
	
	public void addToBasePrice(int add) {
		this.basePrice += add;
	}
	
	public void addToDiscount(int add) {
		this.discount += add;
	}
	
	public void addToFinalPrice(int add) {
		this.finalPrice += add;
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

	public boolean isAutomatic() {
		return automatic;
	}

	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
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

	public boolean requiresNovicePermission() {
		return requiresNovicePermission;
	}

	public void setRequiresNovicePermission(boolean requiresNovicePermission) {
		this.requiresNovicePermission = requiresNovicePermission;
	}

	public int getExtraDeductionPercent() {
		return extraDeductionPercent;
	}

	public void setExtraDeductionPercent(int extraDeductionPercent) {
		this.extraDeductionPercent = extraDeductionPercent;
	}

	public boolean hasFreeClassUpgrade() {
		return upgradeClass != null;
	}

	public boolean isCarAvailable() {
		return carAvailable;
	}

	public void setCarAvailable(boolean carAvailable) {
		this.carAvailable = carAvailable;
	}
	
	public String getUpgradeClass() {
		return upgradeClass;
	}

	public void setUpgradeClass(String upgradeClass) {
		this.upgradeClass = upgradeClass;
	}

	public boolean isDeclined() {
		return declined;
	}

	public void setDeclined(boolean declined) {
		this.declined = declined;
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
