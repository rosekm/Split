package utils;

import dbModel.Branch;
import fxmodel.BranchFX;

public class BranchFXConverter {

	private static BranchFX branchFX;
	private static Branch branch;
	
	public static BranchFX convertToBranchFX(Branch branch) {
		branchFX = new BranchFX();
		branchFX.setId(branch.getId());
		branchFX.setName(branch.getName());
		return branchFX;
	}
	
	public static Branch convertToBranch(BranchFX branchFX) {
		branch = new Branch();
		branch.setId(branchFX.getId());
		branch.setName(branchFX.getName());
		return branch;
	}
	
}
