package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dbModel.Branch;
import dbModel.User;
import dbUtils.UserDAO;


public class UserManager {

	private static UserDAO userDao;
	private static User activeUser;

	private static void init() {
		userDao = new UserDAO();
		try {
			activeUser = userDao.userQueryByHostId(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			DialogUtils.errorDialog(
					ResourceBundle.getBundle("properties.GUIResources").getString("UserManager.loadActUserFailMsg"));
			e.printStackTrace();
		}
	}

	public static String getActiveUserType() {
		if (activeUser == null) {
			init();
		}
		return activeUser.getUserType();
	}

	public static ArrayList<Branch> getActiveUserAuthorizations() {
		if (activeUser == null) {
			init();
		}
		return activeUser.getBranchesAuthorizedForUser();
	}

	public static User getActiveUser() {
		if (activeUser == null) {
			init();
		}
		return activeUser;
	}

}
