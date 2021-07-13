package fxmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SalesmanFX {

	private IntegerProperty idProperty;
	private StringProperty nameProperty;
	private StringProperty surnameProperty;
	private ObjectProperty<BranchFX> branchObjectProperty;
	
	public SalesmanFX() {
		idProperty = new SimpleIntegerProperty();
		nameProperty =  new SimpleStringProperty();
		surnameProperty = new SimpleStringProperty();
		branchObjectProperty = new SimpleObjectProperty<BranchFX>();
		
	}
	
	public SalesmanFX(String name, String surname) {
		idProperty = new SimpleIntegerProperty();
		nameProperty =  new SimpleStringProperty();
		surnameProperty = new SimpleStringProperty();
		branchObjectProperty = new SimpleObjectProperty<BranchFX>();
		nameProperty.set(name);
		surnameProperty.set(surname);
	}

	public StringProperty getNameProperty() {
		return nameProperty;
	}

	public void setNameProperty(String name) {
		this.nameProperty.setValue(name);
	}
		
	public StringProperty getSurnameProperty() {
		return surnameProperty;
	}

	public void setSurnameProperty(String surname) {
		this.surnameProperty.setValue(surname);;
	}

	public ObjectProperty<BranchFX> getBranchObjectProperty() {
		return branchObjectProperty;
	}

	public void setBranchObjectProperty(BranchFX branch) {
		this.branchObjectProperty.setValue(branch);
	}

	public IntegerProperty getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(Integer idProperty) {
		this.idProperty.setValue(idProperty);
	}
}
