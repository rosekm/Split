package fxmodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserFX {
	
	private SimpleIntegerProperty idProperty;
	private SimpleStringProperty nameProperty;
	private SimpleStringProperty surnameProperty;
	private SimpleStringProperty machineIDProperty;
	private SimpleStringProperty userType;
	
	
	public UserFX() {
		idProperty = new SimpleIntegerProperty();
		nameProperty = new SimpleStringProperty();
		surnameProperty = new SimpleStringProperty();
		machineIDProperty = new SimpleStringProperty();
		userType = new SimpleStringProperty();
	}


	public SimpleIntegerProperty getIdProperty() {
		return idProperty;
	}


	public void setIdProperty(Integer idProperty) {
		this.idProperty.setValue(idProperty);
	}


	public SimpleStringProperty getNameProperty() {
		return nameProperty;
	}


	public void setNameProperty(String nameProperty) {
		this.nameProperty.setValue(nameProperty);;
	}


	public SimpleStringProperty getSurnameProperty() {
		return surnameProperty;
	}


	public void setSurnameProperty(String surnameProperty) {
		this.surnameProperty.setValue(surnameProperty);
	}


	public SimpleStringProperty getMachineIDProperty() {
		return machineIDProperty;
	}


	public void setMachineIDProperty(String machineIDProperty) {
		this.machineIDProperty.setValue(machineIDProperty);;
	}


	public SimpleStringProperty getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType.setValue(userType);;
	}
}
