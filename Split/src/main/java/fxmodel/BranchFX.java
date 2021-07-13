package fxmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BranchFX {
	
	private IntegerProperty idProperty;
	private StringProperty nameProperty;
	
	public BranchFX(){
		this.idProperty = new SimpleIntegerProperty();
		this.nameProperty = new SimpleStringProperty();
	}

	public BranchFX(String branchName){
		this.nameProperty = new SimpleStringProperty();
		this.nameProperty.setValue(branchName);
	}
	
	public String getName() {
		return this.nameProperty.getValue();
	}

	public void setName(String name) {
		this.nameProperty.setValue(name);
	}
	
	public StringProperty getNameProperty() {
		return this.nameProperty;
	}

	public int getId() {
		return this.idProperty.getValue();
	}

	public void setId(int id) {
		this.idProperty.setValue(id);
	}
	
	public IntegerProperty getIdProperty() {
		return this.idProperty;
	}

}
