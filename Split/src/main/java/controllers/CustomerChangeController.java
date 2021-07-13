package controllers;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import fxmodel.CustomerFX;
import fxmodel.SalesmanFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.CustomerSearchModel;
import utils.DialogUtils;
import utils.UserManager;

public class CustomerChangeController {

	private final String PROPERTIES_GUI_RESOURCES = "properties.GUIResources";
	private CustomerSearchModel searchViewModel;
	private ObservableList<String> countryCodesList;

	@FXML
	private TextField vatNoTextField;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField addressTextField;

	@FXML
	private TextField postalCodeTextField;

	@FXML
	private TextField cityTextField;

	@FXML
	private ComboBox<String> countryComboBox;

	@FXML
	private ComboBox<SalesmanFX> salesmanComboBox;

	@FXML
	private Button saveButton;

	@FXML
	private Button cancelButton;

	@FXML
	public void initialize() {
		if (!UserManager.getActiveUserType().equals("ADMIN") && !UserManager.getActiveUserType().equals("SUPERUSER")) {
			this.salesmanComboBox.disableProperty().set(true);
		}
	}

	@FXML
	private void updateCustomer() {
		switch (this.searchViewModel.updateCustomerData()) {
		case 0:
			DialogUtils.errorDialog(
					ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("SearchView.customerUpdateFailed"));
			break;
		case 1:
			DialogUtils.informationDialog(
					ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("SearchView.customerUpdateSucceed"));

			break;
		}
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void abortChange() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void setSearchViewModel(CustomerSearchModel model) {
		this.searchViewModel = model;
		addItemsToSalesmanComboBox();
		addItemsToCountryComboBox();
	}

	public void setDefaultFieldsValues(CustomerFX customer) {
		setPropertiesBindings();
		this.vatNoTextField.setText(customer.getVatNoProperty().getValue());
		this.vatNoTextField.disableProperty().set(true);
		this.nameTextField.setText(customer.getNameProperty().getValue());
		this.addressTextField.setText(customer.getAddressProperty().getValue());
		this.postalCodeTextField.setText(customer.getPostalCodeProperty().getValue());
		this.cityTextField.setText(customer.getCityProperty().getValue());
		this.salesmanComboBox.getSelectionModel().select(customer.getSalesmanProperty().getValue());
		this.countryComboBox.getSelectionModel().select(customer.getCountryCodeProperty().getValue());

	}

	private void addItemsToSalesmanComboBox() {
		this.salesmanComboBox.getItems().addAll(searchViewModel.getSalesmenList());
		this.salesmanComboBox.setConverter(new StringConverter<SalesmanFX>() {

			@Override
			public String toString(SalesmanFX object) {
				return object.getNameProperty().getValue() + " " + object.getSurnameProperty().getValue();
			}

			@Override
			public SalesmanFX fromString(String string) {
				return salesmanComboBox.getItems().stream().filter(salesman -> (salesman.getNameProperty().getValue()
						+ " " + salesman.getSurnameProperty().getValue()).equals(string)).findFirst().orElse(null);
			}
		});
	}

	private void addItemsToCountryComboBox() {
		String[] countryCodesArray = Locale.getISOCountries();
		countryCodesList = FXCollections.observableArrayList();
		Arrays.stream(countryCodesArray).forEach(code -> countryCodesList.add(code));
		countryComboBox.setItems(countryCodesList);

		countryComboBox.getSelectionModel().select(Locale.getDefault().getCountry());
	}

	private void setPropertiesBindings() {
		this.nameTextField.textProperty().bindBidirectional(searchViewModel.getCustomerFX().getNameProperty());
		this.addressTextField.textProperty().bindBidirectional(searchViewModel.getCustomerFX().getAddressProperty());
		this.cityTextField.textProperty().bindBidirectional(searchViewModel.getCustomerFX().getCityProperty());
		this.postalCodeTextField.textProperty()
				.bindBidirectional(searchViewModel.getCustomerFX().getPostalCodeProperty());
		this.countryComboBox.valueProperty()
				.bindBidirectional(searchViewModel.getCustomerFX().getCountryCodeProperty());
		this.salesmanComboBox.valueProperty().bindBidirectional(searchViewModel.getCustomerFX().getSalesmanProperty());
	}

}

