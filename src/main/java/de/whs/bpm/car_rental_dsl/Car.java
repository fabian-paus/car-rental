package de.whs.bpm.car_rental_dsl;

public class Car {
	
	private boolean isAutomatic;
	private CarClass class_;
	
	public Car(boolean isAutomatic, CarClass class_) {
		super();
		this.isAutomatic = isAutomatic;
		this.class_ = class_;
	}
	
	public boolean isAutomatic() {
		return isAutomatic;
	}
	public void setAutomatic(boolean isAutomatic) {
		this.isAutomatic = isAutomatic;
	}
	
	public CarClass getClass_() {
		return class_;
	}
	public void setClass_(CarClass class_) {
		this.class_ = class_;
	}
}
