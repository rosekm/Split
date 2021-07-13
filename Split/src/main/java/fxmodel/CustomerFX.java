package fxmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerFX {
	
	private StringProperty vatNoProperty;
	private StringProperty nameProperty;
	private StringProperty addressProperty;
	private StringProperty postalCodeProperty;
	private StringProperty cityProperty;
	private StringProperty countryCodeProperty;
	private ObjectProperty<SalesmanFX> salesmanProperty;

	
	public CustomerFX() {
		vatNoProperty = new SimpleStringProperty();
		nameProperty = new SimpleStringProperty();
		addressProperty = new SimpleStringProperty();
		postalCodeProperty = new SimpleStringProperty();
		cityProperty = new SimpleStringProperty();
		countryCodeProperty = new SimpleStringProperty();
		salesmanProperty = new SimpleObjectProperty<SalesmanFX>();
	}


	public StringProperty getVatNoProperty() {
		return vatNoProperty;
	}


	public void setVatNoProperty(String vatNo) {
		this.vatNoProperty.setValue(vatNo);;
	}


	public StringProperty getNameProperty() {
		return nameProperty;
	}


	public void setNameProperty(String name) {
		this.nameProperty.setValue(name);;
	}


	public StringProperty getAddressProperty() {
		return addressProperty;
	}


	public void setAddressProperty(String address) {
		this.addressProperty.setValue(address);
	}


	public StringProperty getPostalCodeProperty() {
		return postalCodeProperty;
	}


	public void setPostalCodeProperty(String postalCode) {
		this.postalCodeProperty.setValue(postalCode);;
	}


	public StringProperty getCityProperty() {
		return cityProperty;
	}


	public void setCityProperty(String city) {
		this.cityProperty.setValue(city);
	}


	public StringProperty getCountryCodeProperty() {
		return countryCodeProperty;
	}


	public void setCountryCodeProperty(String countryCode) {
		this.countryCodeProperty.setValue(countryCode);
	}


	public ObjectProperty<SalesmanFX> getSalesmanProperty() {
		return salesmanProperty;
	}


	public void setSalesmanProperty(SalesmanFX salesman) {
		
		this.salesmanProperty.setValue(salesman);;
	}
	
	
}
