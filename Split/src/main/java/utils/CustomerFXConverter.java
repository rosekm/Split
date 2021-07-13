package utils;

import dbModel.Customer;
import fxmodel.CustomerFX;

public class CustomerFXConverter {
	
	private static CustomerFX customerFX;
	private static Customer customer;
	
	public static CustomerFX convertToCustomerFX(Customer customer) {
		customerFX = new CustomerFX();
		customerFX.setNameProperty(customer.getName());
		customerFX.setVatNoProperty(customer.getVatNo());
		customerFX.setAddressProperty(customer.getAddress());
		customerFX.setCityProperty(customer.getCity());
		customerFX.setCountryCodeProperty(customer.getCountryCode());
		customerFX.setPostalCodeProperty(customer.getPostalCode());
		customerFX.setSalesmanProperty(SalesmanFXConverter.convertToSalesmanFX(customer.getSalesman()));
		return customerFX;
	}
	
	public static Customer convertToCustomer(CustomerFX customerFX) {
		customer = new Customer();
		customer.setAddress(customerFX.getAddressProperty().getValue());
		customer.setCity(customerFX.getCityProperty().getValue());
		customer.setCountryCode(customerFX.getCountryCodeProperty().getValue());
		customer.setName(customerFX.getNameProperty().getValue());
		customer.setPostalCode(customerFX.getPostalCodeProperty().getValue());
		customer.setVatNo(customerFX.getVatNoProperty().getValue());
		customer.setSalesman(SalesmanFXConverter.convertToSalesman(customerFX.getSalesmanProperty().getValue()));
		return customer;
	}

}
