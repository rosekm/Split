package controllers;

import java.util.ResourceBundle;

import fxmodel.BranchFX;
import fxmodel.SalesmanFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.SalesmenModel;
import utils.DialogUtils;

public class SalesmanChangeController {

	private SalesmenModel salesmenModel;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField surnameTextField;

	@FXML
	private ComboBox<BranchFX> branchComboBox;

	@FXML
	private Button cancelButton;

	@FXML
	private Button saveButton;

	@FXML
	public void initialize() {
	}

	@FXML
	public void updateSalesman() {
		switch (this.salesmenModel.updateSalesmanData()) {
		case 1:
			DialogUtils.informationDialog(ResourceBundle.getBundle("properties.GUIResources")
					.getString("ChangeSalesmenView.submitDataChangesSuccess"));
			break;
		case 0:
			DialogUtils.informationDialog(ResourceBundle.getBundle("properties.GUIResources")
					.getString("ChangeSalesmenView.submitDataChangesFailure"));
			break;
		}
		Stage stage = (Stage) saveButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void abortChange() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void setSalesmenModel(SalesmenModel model) {
		this.salesmenModel = model;
		addItemsToBranchComboBox();
	}

	public void setDefaultFieldsValues(SalesmanFX salesman) {
		setPropertiesBindings();
		this.nameTextField.setText(salesman.getNameProperty().getValue());
		this.surnameTextField.setText(salesman.getSurnameProperty().getValue());
		this.branchComboBox.getSelectionModel().select(salesman.getBranchObjectProperty().getValue());
	}

	public void addItemsToBranchComboBox() {
		this.branchComboBox.getItems().addAll(salesmenModel.getBranchList());
		this.branchComboBox.setConverter(new StringConverter<BranchFX>() {

			@Override
			public String toString(BranchFX object) {
				return object.getName();
			}

			@Override
			public BranchFX fromString(String string) {
				return branchComboBox.getItems().stream().filter(br -> br.getName().equals(string)).findFirst()
						.orElse(null);
			}
		});

	}

	public void setPropertiesBindings() {
		this.nameTextField.textProperty()
				.bindBidirectional(this.salesmenModel.getSalesmanObjProp().getValue().getNameProperty());
		this.surnameTextField.textProperty()
				.bindBidirectional(this.salesmenModel.getSalesmanObjProp().getValue().getSurnameProperty());
		this.branchComboBox.valueProperty()
				.bindBidirectional(this.salesmenModel.getSalesmanObjProp().getValue().getBranchObjectProperty());

	}

}
