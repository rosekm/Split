package dbModel;

import java.util.ArrayList;

public class User {
	
	private Integer id;
	private String name;
	private String surname;
	private String machineID;
	private ArrayList<Branch> branchesAuthorizedForUser;
	private UserTypeID userType;
	
	public enum UserTypeID {ADMIN, SUPERUSER, STANDARD_USER};
	
	public User() {
		branchesAuthorizedForUser = new ArrayList<Branch>();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getMachineID() {
		return machineID;
	}
	public void setMachineID(String machineID) {
		this.machineID = machineID;
	}
	public String getUserType() {
		return userType.name();
	}
	public void setUserType(String userType) {
		this.userType = UserTypeID.valueOf(userType);
	}

	public ArrayList<Branch> getBranchesAuthorizedForUser() {
		return branchesAuthorizedForUser;
	}

	public void setBranchesAuthorizedForUser(ArrayList<Branch> branchesAuthorizedForUser) {
		this.branchesAuthorizedForUser = branchesAuthorizedForUser;
	}
	
	

}
