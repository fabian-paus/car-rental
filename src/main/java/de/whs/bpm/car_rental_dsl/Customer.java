package de.whs.bpm.car_rental_dsl;

import java.util.Calendar;

public class Customer {

	private boolean isNew;
	private boolean hasReclamation;
	private boolean hasSecurityTraining;
	private Calendar birthday;
	private Calendar drivingLicense;
	
	
	
	public Customer(boolean isNew, boolean hasReclamation,
			boolean hasSecurityTraining, Calendar birthday,
			Calendar drivingLicense) {
		super();
		this.isNew = isNew;
		this.hasReclamation = hasReclamation;
		this.hasSecurityTraining = hasSecurityTraining;
		this.birthday = birthday;
		this.drivingLicense = drivingLicense;
	}
	
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public boolean isHasReclamation() {
		return hasReclamation;
	}
	public void setHasReclamation(boolean hasReclamation) {
		this.hasReclamation = hasReclamation;
	}
	public boolean isHasSecurityTraining() {
		return hasSecurityTraining;
	}
	public void setHasSecurityTraining(boolean hasSecurityTraining) {
		this.hasSecurityTraining = hasSecurityTraining;
	}
	public Calendar getBirthday() {
		return birthday;
	}
	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}
	public Calendar getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(Calendar drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	
}
