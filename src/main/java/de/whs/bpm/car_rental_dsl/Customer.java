package de.whs.bpm.car_rental_dsl;


public class Customer {

	private boolean isNew;
	private boolean hasReclamation;
	private boolean hasSecurityTraining;
	private int age;
	private int drivingLicense;
	
	private String Name;
	
	public Customer() {
		
	}
	
	public boolean isNewCustomer() {
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(int drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	
	@Override
	public String toString() {
		
		return getName();
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
}
