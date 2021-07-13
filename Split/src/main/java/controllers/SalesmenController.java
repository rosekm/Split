package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import fxmodel.BranchFX;
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
import models.SalesmenModel;
import utils.DialogUtils;

public class SalesmenController {

	
	private SalesmenModel salesmenModel;
	private final String PROPERTIES_GUI_RESOURCES = "properties.GUIResources";
	private final String CHANGE_SALESMAN_VIEW = "/fxml/changeSalesmanData.fxml";

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField surnameTextField;

	@FXML
	private ComboBox<BranchFX> branchComboBox;

	@FXML
	private Button addButton;

	@FXML
	private Button searchButton;

	@FXML
	private TableView<SalesmanFX> salesmenTableView;

	@FXML
	private TableColumn<SalesmanFX, String> nameColumn;

	@FXML
	private TableColumn<SalesmanFX, String> surnameColumn;

	@FXML
	private TableColumn<SalesmanFX, String> branchColumn;

	@FXML
	private TableColumn<SalesmanFX, SalesmanFX> utilColumn;

	@FXML
	public void initialize() {
		this.salesmenModel = new SalesmenModel();

		setPropertiesBindings();
		setItemsForBranchesComboBox();
		setTableView();
	}

	private void setTableView() {
		salesmenTableView.setItems(salesmenModel.getSalesmanList());
		nameColumn.setCellValueFactory(salesman -> salesman.getValue().getNameProperty());
		surnameColumn.setCellValueFactory(salesman -> salesman.getValue().getSurnameProperty());
		branchColumn.setCellValueFactory(getBranchCellFactory());
		this.utilColumn.setCellValueFactory(salesman -> new SimpleObjectProperty<SalesmanFX>(salesman.getValue()));

		this.utilColumn.setCellFactory(param -> new TableCell<SalesmanFX, SalesmanFX>() {
			Button editButton = new Button();
			Button deleteButton = new Button();

			@Override
			protected void updateItem(SalesmanFX salesman, boolean empty) {
				super.updateItem(salesman, empty);
				if (!empty) {
					editButton.setText(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("EditButton.name"));
					editButton.setOnAction(event -> {
						setEditAction(salesman);
					});
					deleteButton
							.setText(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("DeleteButton.name"));
					deleteButton.setOnAction(event -> {
						setDeleteAction(salesman);
					});

					HBox pane = new HBox(deleteButton, editButton);
					setGraphic(pane);
				} else {
					setGraphic(null);
				}

			}

			
			
		});
	}

	private void setEditAction(SalesmanFX salesman) {
		Stage editSalesmanStage = new Stage();
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CHANGE_SALESMAN_VIEW));
		ResourceBundle guiResources = ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES);
		loader.setResources(guiResources);
		Pane editSalesmanPane;
		try {
			editSalesmanPane = loader.load();
			Scene editSalesmanScene = new Scene(editSalesmanPane);
			editSalesmanStage.setScene(editSalesmanScene);

			SalesmanChangeController salesmanChangeController = loader.getController();
			salesmenModel.setSalesmanObjProp(salesman);
			salesmanChangeController.setSalesmenModel(salesmenModel);
			salesmanChangeController.setDefaultFieldsValues(salesman);

			editSalesmanStage.initModality(Modality.APPLICATION_MODAL);

			editSalesmanStage.showAndWait();

		} catch (IOException e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void setDeleteAction(SalesmanFX salesman) {
		salesmenModel.setSalesmanObjProp(salesman);
		ArrayList<CustomerFX> attachedCustomersList = salesmenModel.getCustomersAttchedToSalesman();
		if (attachedCustomersList.size() == 0) {
			switch (salesmenModel.deleteSalesmanFromDB()) {
			case 0:
				DialogUtils.errorDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("SalesmanView.salesmanDeletionFailed"));
				break;
			case 1:
				DialogUtils.informationDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("SalesmenView.salesmanDeletionSucceed"));
				break;
			}
		} else {
			StringBuffer attachedCustomersString = new StringBuffer();
			for (CustomerFX customer : attachedCustomersList) {
				attachedCustomersString.append("\n" + customer.getVatNoProperty().getValue() + " - "
						+ customer.getNameProperty().getValue());
			}
			DialogUtils.errorDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
					.getString("SalesmenView.attachedCustomersExist") + attachedCustomersString);
		}
	}
	
	private void setItemsForBranchesComboBox() {
		Callback<ListView<BranchFX>, ListCell<BranchFX>> factoryBranch = getBranchesComboBoxCellFactory();
		this.branchComboBox.getItems().addAll(salesmenModel.queryForBranches());
		this.branchComboBox.setCellFactory(factoryBranch);
		this.branchComboBox.setButtonCell(factoryBranch.call(null));
	}

	private Callback<ListView<BranchFX>, ListCell<BranchFX>> getBranchesComboBoxCellFactory() {
		Callback<ListView<BranchFX>, ListCell<BranchFX>> factoryBranch = lv -> new ListCell<BranchFX>() {
			@Override
			protected void updateItem(BranchFX branch, boolean empty) {
				super.updateItem(branch, empty);
				setText(empty ? "" : branch.getName());
			}
		};
		return factoryBranch;
	}

	@FXML
	public void searchForSalesman() {
		setPropertiesBindings();
		if ((nameTextField.getText() == null || nameTextField.getText().trim().isEmpty())
				&& (surnameTextField.getText() == null || surnameTextField.getText().trim().isEmpty())
				&& (branchComboBox.getSelectionModel().isEmpty()
						|| branchComboBox.getSelectionModel().getSelectedItem().getName().equals(""))) {
			DialogUtils.errorDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
					.getString("SalesmenView.emptyFieldsErrorMessage"));
		} else {
			salesmenModel.queryForSalesman();
			if (salesmenModel.getSalesmanList().isEmpty()) {
				DialogUtils.informationDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("SalesmenView.emptyQueryResultMessage"));
			}
		}
		clearFieldValues();

	}

	@FXML
	public void addSalesman() {
		setPropertiesBindings();
		if ((nameTextField.getText() == null || nameTextField.getText().trim().isEmpty())
				|| (surnameTextField.getText() == null || surnameTextField.getText().trim().isEmpty())
				|| (branchComboBox.getSelectionModel().isEmpty())) {
			DialogUtils.errorDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
					.getString("SalesmenView.emptyFieldErrorMessage"));
		} else {
			switch (salesmenModel.addSalesmanData()) {
			case 0:
				DialogUtils.errorDialog(
						ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("SalesmenView.salesmanAddFailed"));
				break;
			case 1:
				DialogUtils.informationDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("SalesmenView.salesmanAddSucceed"));
				clearFieldValues();
				break;
			case -1:
				DialogUtils.errorDialog(
						ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("SalesmenView.salesmanAddError"));
				break;
			}
		}

	}

	public void setPropertiesBindings() {
		salesmenModel.getSalesmanObj().getNameProperty().bind(this.nameTextField.textProperty());
		salesmenModel.getSalesmanObj().getSurnameProperty().bind(this.surnameTextField.textProperty());
		salesmenModel.getSalesmanObj().getBranchObjectProperty().bind(this.branchComboBox.valueProperty());
	}

	public Callback<CellDataFeatures<SalesmanFX, String>, ObservableValue<String>> getBranchCellFactory() {
		Callback<CellDataFeatures<SalesmanFX, String>, ObservableValue<String>> branchNameCB = lv -> {
			String branchName = lv.getValue().getBranchObjectProperty().getValue().getName();
			StringProperty branchNameProperty = new SimpleStringProperty(branchName);
			return branchNameProperty;
		};

		return branchNameCB;
	}

	public void clearFieldValues() {
		this.nameTextField.clear();
		this.surnameTextField.clear();
		this.branchComboBox.getSelectionModel().clearSelection();
	}

}
