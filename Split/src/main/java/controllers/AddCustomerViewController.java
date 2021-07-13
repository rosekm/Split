package controllers;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fxmodel.SalesmanFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import models.AddCustomerViewModel;
import utils.DialogUtils;

public class AddCustomerViewController {

	private ObservableList<String> countryCodesList;
	private AddCustomerViewModel addCustomerViewModel;
	@FXML
	TextField vatNoTextField;

	@FXML
	TextField customerNameTextField;

	@FXML
	TextField addressTextField;

	@FXML
	TextField postalCodeTextField;

	@FXML
	TextField cityTextField;

	@FXML
	ComboBox<String> countryCodeComboBox;

	@FXML
	ComboBox<SalesmanFX> salesmanCombobox;

	@FXML
	Button addButton;

	@FXML
	public void initialize() {
		this.addCustomerViewModel = new AddCustomerViewModel();
		setPropertiesBindings();
		setValuesForSalesmanComboBox();
		setValuesForCountryCodesComboBox();

		vatNoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (vatFormatForPLCorrect() && countryCodeComboBox.getSelectionModel().getSelectedItem().equals("PL")) {
				addCustomerViewModel.getAddedCustomerFX().setVatNoProperty(vatNoTextField.getText().trim());
				addCustomerViewModel.getBIRCustomer();
				setPropertiesBindings();
			}

		});

	}

	@FXML
	public void addCustomerToDB() {
		if (mandatoryFieldsCompleted()) {
			if (countryCodeComboBox.getSelectionModel().getSelectedItem().equals("PL") && !vatFormatForPLCorrect()) {
				DialogUtils.errorDialog(ResourceBundle.getBundle("properties..GUIResources")
						.getString("AddView.wrongPLVatFormatMessage"));
				return;
			}
			switch (addCustomerViewModel.saveNewCustomerInDB()) {
			case -1:
				break;
			case 0:
				DialogUtils.errorDialog(ResourceBundle.getBundle("properties.GUIResources")
						.getString("AddView.alreadyExistingCustomerMessage"));
				break;
			default:
				DialogUtils.informationDialog(ResourceBundle.getBundle("properties.GUIResources")
						.getString("AddView.dataUploadSucceededMessage"));
				clearFormFields();
				break;
			}
		}
	}

	private void setValuesForCountryCodesComboBox() {
		String[] countryCodesArray = Locale.getISOCountries();
		countryCodesList = FXCollections.observableArrayList();
		Arrays.stream(countryCodesArray).forEach(code -> countryCodesList.add(code));
		countryCodeComboBox.setItems(countryCodesList);

		countryCodeComboBox.getSelectionModel().select(Locale.getDefault().getCountry());
	}

	private void setValuesForSalesmanComboBox() {
		Callback<ListView<SalesmanFX>, ListCell<SalesmanFX>> factorySalesman = getSalesmanComboBoxCellFactory();

		this.salesmanCombobox.getItems().addAll(addCustomerViewModel.getSalesmenList());
		this.salesmanCombobox.setCellFactory(factorySalesman);
		this.salesmanCombobox.setButtonCell(factorySalesman.call(null));

	}

	private Callback<ListView<SalesmanFX>, ListCell<SalesmanFX>> getSalesmanComboBoxCellFactory() {
		Callback<ListView<SalesmanFX>, ListCell<SalesmanFX>> factorySalesman = lv -> new ListCell<SalesmanFX>() {
			@Override
			protected void updateItem(SalesmanFX salesman, boolean empty) {
				super.updateItem(salesman, empty);
				setText(empty ? ""
						: salesman.getNameProperty().getValue() + " " + salesman.getSurnameProperty().getValue());
			}
		};
		return factorySalesman;
	}

	private boolean mandatoryFieldsCompleted() {
		if (vatNoTextField.getText() == null || vatNoTextField.getText().trim().isEmpty()) {
			DialogUtils.errorDialog(
					ResourceBundle.getBundle("properties.GUIResources").getString("AddView.emptyFieldErrorMessage"));
			return false;
		} else if (customerNameTextField.getText() == null || customerNameTextField.getText().trim().isEmpty()) {
			DialogUtils.errorDialog(
					ResourceBundle.getBundle("properties.GUIResources").getString("AddView.emptyFieldErrorMessage"));
			return false;
		} else if (countryCodeComboBox.getSelectionModel().isEmpty()) {
			DialogUtils.errorDialog(
					ResourceBundle.getBundle("properties.GUIResources").getString("AddView.emptyFieldErrorMessage"));
			return false;
		} else if (salesmanCombobox.getSelectionModel().isEmpty()) {
			DialogUtils.errorDialog(
					ResourceBundle.getBundle("properties.GUIResources").getString("AddView.emptyFieldErrorMessage"));
			return false;
		} else {
			return true;
		}

	}

	private boolean vatFormatForPLCorrect() {
		Pattern pattern1 = Pattern.compile("\\d{10}");
		Matcher matcher1 = pattern1.matcher(vatNoTextField.textProperty().getValue());

		Pattern pattern2 = Pattern.compile("\\d{3}-\\d{2}-\\d{2}-\\d{3}");
		Matcher matcher2 = pattern2.matcher(vatNoTextField.textProperty().getValue());

		return matcher1.matches() || matcher2.matches();

	}

	private void setPropertiesBindings() {
		vatNoTextField.textProperty().bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getVatNoProperty());
		customerNameTextField.textProperty()
				.bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getNameProperty());
		addressTextField.textProperty()
				.bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getAddressProperty());
		postalCodeTextField.textProperty()
				.bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getPostalCodeProperty());
		cityTextField.textProperty().bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getCityProperty());
		countryCodeComboBox.valueProperty()
				.bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getCountryCodeProperty());
		salesmanCombobox.valueProperty()
				.bindBidirectional(addCustomerViewModel.getAddedCustomerFX().getSalesmanProperty());
	}

	public void clearFormFields() {
		vatNoTextField.clear();
		customerNameTextField.clear();
		addressTextField.clear();
		postalCodeTextField.clear();
		cityTextField.clear();
		countryCodeComboBox.getSelectionModel().select("PL");
		salesmanCombobox.getSelectionModel().clearSelection();
	}

}
