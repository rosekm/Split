package utils;

import dbModel.Branch;
import dbModel.User;
import fxmodel.BranchFX;
import fxmodel.UserFX;

public class UserFXConverter {

	private static UserFX userFX;
	private static User user;
	
	public static UserFX convertToBranchFX(User user) {
		userFX = new UserFX();
		userFX.setIdProperty(user.getId());
		userFX.setNameProperty(user.getName());
		userFX.setSurnameProperty(user.getSurname());
		userFX.setMachineIDProperty(user.getMachineID());
		userFX.setUserType(user.getUserType());
		return userFX;
	}
	
	public static User convertToBranch(UserFX userFX) {
		user = new User();
		user.setId(userFX.getIdProperty().get());
		user.setName(userFX.getNameProperty().get());
		user.setSurname(userFX.getSurnameProperty().get());
		user.setMachineID(userFX.getMachineIDProperty().get());
		user.setUserType(userFX.getUserType().get());
		return user;
	}
	
}
