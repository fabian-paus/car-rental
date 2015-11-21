package de.whs.bpm.car_rental_dsl.test;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkItemHandler;

/**
 * This WorkItemHandler is used in unit tests to simulate the possibles outcomes
 * of the approval tasks which require human interaction in the running program.
 * 
 * @author Fabian Paus
 *
 */
public class ApprovalWorkItemHandler implements WorkItemHandler {

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
	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		
		// This should not be called during the unit tests
		throw new RuntimeException("abortWorkItem was unexpectedly called");
	}

	@Override
	public void executeWorkItem(WorkItem item, WorkItemManager manager) {
		
		Map<String, Object> results = new HashMap<String, Object>();
		
		Map<String, Object> params = item.getParameters();
		Object taskName = params.get("TaskName");
		if (taskName.equals("Approve Novice")) {
			results.put("noviceApproved", approveNovice);
		}
		else if (taskName.equals("Approve Upgrade")) {
			results.put("upgradeApproved", approveUpgrade);
		}
		else {
			throw new RuntimeException("Unexpected task: " + taskName);
		}
		
		manager.completeWorkItem(item.getId(), results);
	}

}
