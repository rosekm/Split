package controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import fxmodel.CustomerFX;
import fxmodel.SalesmanFX;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.CustomerSearchModel;
import utils.DialogUtils;
import utils.UserManager;

public class CustomerSearchController {

	private final String PROPERTIES_GUI_RESOURCES = "properties.GUIResources";
	private final String CHANGE_CUSTOMER_VIEW = "/fxml/changeCustomerData.fxml";
	private CustomerSearchModel searchViewModel;

	@FXML
	private TextField customerNameTextField;

	@FXML
	private TextField vatNoTextField;

	@FXML
	private ComboBox<SalesmanFX> salesmanCombobox;

	@FXML
	private TableView<CustomerFX> customersTable;

	@FXML
	private TableColumn<CustomerFX, String> customerNameColumn;

	@FXML
	private TableColumn<CustomerFX, String> vatNoColumn;

	@FXML
	private TableColumn<CustomerFX, String> addressColumn;

	@FXML
	private TableColumn<CustomerFX, String> postalCodeColumn;

	@FXML
	private TableColumn<CustomerFX, String> cityColumn;

	@FXML
	private TableColumn<CustomerFX, String> countryCodeColumn;

	@FXML
	private TableColumn<CustomerFX, String> salesmanColumn;

	@FXML
	private TableColumn<CustomerFX, CustomerFX> utilColumn;

	@FXML
	public void initialize() {
		this.searchViewModel = new CustomerSearchModel();
		setPropertiesBindings();
		setItemsForSalesmanComboBox();
		setTableView();

	}

	private void setTableView() {
		customersTable.setItems(searchViewModel.getCustomerList());
		customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		vatNoColumn.setCellValueFactory(cellData -> cellData.getValue().getVatNoProperty());
		addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
		postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getPostalCodeProperty());
		cityColumn.setCellValueFactory(cellData -> cellData.getValue().getCityProperty());
		countryCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getCountryCodeProperty());
		salesmanColumn.setCellValueFactory(getSalesmanCellValueFactory());

		this.utilColumn.setCellValueFactory(customer -> new SimpleObjectProperty<CustomerFX>(customer.getValue()));

		this.utilColumn.setCellFactory(param -> new TableCell<CustomerFX, CustomerFX>() {
			Button editButton = new Button();
			Button deleteButton = new Button();

			@Override
			protected void updateItem(CustomerFX customer, boolean empty) {
				super.updateItem(customer, empty);
				if (!empty) {
					editButton.setText(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("EditButton.name"));
					editButton.setOnAction(event -> {
						setEditAction(customer);
					});

					deleteButton
							.setText(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("DeleteButton.name"));
					deleteButton.setOnAction(event -> {
						setDeletionAction(customer);
					});

					if (!UserManager.getActiveUserType().equals("ADMIN")
							&& !UserManager.getActiveUserType().equals("SUPERUSER")) {
						deleteButton.disableProperty().set(true);
					}
					HBox pane = new HBox(deleteButton, editButton);
					setGraphic(pane);
				} else {
					setGraphic(null);
				}

			}

		});
	}

	private void setEditAction(CustomerFX customer) {
		Stage editCustomerStage = new Stage();
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CHANGE_CUSTOMER_VIEW));
		ResourceBundle guiResources = ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES);
		Pane editCustomerPane;
		Scene editCustomerScene;
		CustomerChangeController customerChangeController;

		loader.setResources(guiResources);

		try {
			editCustomerPane = loader.load();
			editCustomerScene = new Scene(editCustomerPane);
			editCustomerStage.setScene(editCustomerScene);
			customerChangeController = loader.getController();
			searchViewModel.setCustomerFX(customer);
			customerChangeController.setSearchViewModel(searchViewModel);
			customerChangeController.setDefaultFieldsValues(customer);

			editCustomerStage.initModality(Modality.APPLICATION_MODAL);

			editCustomerStage.showAndWait();

		} catch (IOException e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}

	private void setDeletionAction(CustomerFX customer) {
		searchViewModel.setCustomerFX(customer);
		switch (searchViewModel.deleteCustomerFromDB()) {
		case 0:
			DialogUtils.errorDialog(
					ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("SearchView.customerDeletionFailed"));
			break;
		case 1:
			DialogUtils.informationDialog(
					ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("SearchView.customerDeletionSucceed"));
			break;
		}
	}

	private void setItemsForSalesmanComboBox() {
		Callback<ListView<SalesmanFX>, ListCell<SalesmanFX>> factorySalesman = getSalesmanComboBoxCellFactory();

		this.salesmanCombobox.getItems().addAll(searchViewModel.queryForSalesman());
		this.salesmanCombobox.setCellFactory(factorySalesman);
		this.salesmanCombobox.setButtonCell(factorySalesman.call(null));
	}

	@FXML
	public void searchCustomers() {
		setPropertiesBindings();
		if ((customerNameTextField.getText() == null || customerNameTextField.getText().trim() == "")
				&& (vatNoTextField.getText() == null || vatNoTextField.getText().trim() == "")
				&& salesmanCombobox.getSelectionModel().getSelectedItem() == null) {
			DialogUtils.errorDialog(ResourceBundle.getBundle("properties.GUIResources")
					.getString("SalesmenView.emptyFieldsErrorMessage"));
		} else {
			searchViewModel.queryForCustomers();
			if (searchViewModel.getCustomerList().isEmpty()) { // size() == 0
				DialogUtils.informationDialog(ResourceBundle.getBundle("properties.GUIResources")
						.getString("SearchView.emptyQueryResultMessage"));
			}
		}
		clearFieldsValues();
	}

	public Callback<CellDataFeatures<CustomerFX, String>, ObservableValue<String>> getSalesmanCellValueFactory() {
		Callback<CellDataFeatures<CustomerFX, String>, ObservableValue<String>> factoryLineNumber = lv -> {
			String salesmanFullName = lv.getValue().getSalesmanProperty().getValue().getNameProperty().getValue() + " "
					+ lv.getValue().getSalesmanProperty().getValue().getSurnameProperty().getValue();
			StringProperty salesmanFullNameProperty = new SimpleStringProperty(salesmanFullName);
			return salesmanFullNameProperty;
		};

		return factoryLineNumber;
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

	private void setPropertiesBindings() {
		searchViewModel.setCustomerFX(new CustomerFX());
		searchViewModel.getCustomerFX().getNameProperty().bind(customerNameTextField.textProperty());
		searchViewModel.getCustomerFX().getVatNoProperty().bind(vatNoTextField.textProperty());
		searchViewModel.getCustomerFX().getSalesmanProperty().bind(salesmanCombobox.valueProperty());
	}

	private void clearFieldsValues() {
		this.customerNameTextField.clear();
		this.vatNoTextField.clear();
		this.salesmanCombobox.getSelectionModel().clearSelection();
	}
}
