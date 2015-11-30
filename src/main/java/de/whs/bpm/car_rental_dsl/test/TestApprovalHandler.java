package de.whs.bpm.car_rental_dsl.test;

import de.whs.bpm.car_rental_dsl.ApprovalWorkItemHandler;

public class TestApprovalHandler extends ApprovalWorkItemHandler {
	
	private boolean approveNovice;
	private boolean approveUpgrade;

	public boolean isApproveNovice() {
		return approveNovice;
	}

	public void setApproveNovice(boolean approveNovice) {
		this.approveNovice = approveNovice;
	}

	public boolean isApproveUpgrade() {
		return approveUpgrade;
	}

	public void setApproveUpgrade(boolean approveUpgrade) {
		this.approveUpgrade = approveUpgrade;
	}

	@Override
	public boolean approveNovice() {
		return approveNovice;
	}

	@Override
	public boolean approveUpgrade() {
		return approveUpgrade;
	}

}
