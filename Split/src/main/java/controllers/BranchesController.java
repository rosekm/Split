package controllers;

import java.util.ArrayList;
import java.util.ResourceBundle;

import fxmodel.BranchFX;
import fxmodel.SalesmanFX;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.BranchesModel;
import utils.DialogUtils;

public class BranchesController {

	private BranchesModel branchesModel;
	private static final String PROPERTIES_GUI_RESOURCES = "properties.GUIResources";

	@FXML
	private TextField nameTextField;

	@FXML
	private Button addButton;

	@FXML
	private TableView<BranchFX> branchTableView;

	@FXML
	private TableColumn<BranchFX, String> branchNameColumn;

	@FXML
	private TableColumn<BranchFX, BranchFX> utilColumn;

	@FXML
	public void initialize() {
		this.branchesModel = new BranchesModel();

		setPropertiesBindings();

		setTableView();
	}

	private void setTableView() {
		branchTableView.setItems(branchesModel.getBranchList());
		branchNameColumn.setCellValueFactory(branch -> branch.getValue().getNameProperty());
		this.utilColumn.setCellValueFactory(branch -> new SimpleObjectProperty<BranchFX>(branch.getValue()));
		utilColumn.setCellFactory(param -> new TableCell<BranchFX, BranchFX>() {
			Button deleteButton = new Button();

			@Override
			protected void updateItem(BranchFX branch, boolean empty) {
				super.updateItem(branch, empty);
				if (!empty) {
					deleteButton
							.setText(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("DeleteButton.name"));
					deleteButton.setOnAction(event -> {
						setDeletionAction(branch);
					});
					setGraphic(deleteButton);
				} else {
					setGraphic(null);
				}
			}

			
		});
	}

	@FXML
	public void addBranch() {
		setPropertiesBindings();
		if (nameTextField.getText() == null || nameTextField.getText().trim().isEmpty()) {
			DialogUtils.errorDialog(
					ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("BranchView.emptyFieldErrorMessage"));
		} else {
			switch (branchesModel.addBranchData()) {
			case 1:
				DialogUtils.informationDialog(
						ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES).getString("BranchView.branchAddSucceed"));

				break;
			case 0:
				DialogUtils.informationDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("BranchView.branchDeletionSucceed"));
				break;
			}
		}
	}

	private void setDeletionAction(BranchFX branch) {
		branchesModel.setBranchObjProp(branch);
		ArrayList<SalesmanFX> salesmenAttachedToBranch = branchesModel.getSalesmenListForBranch();
		if (salesmenAttachedToBranch.isEmpty()) {
			switch (branchesModel.deleteBranchFromDB()) {
			case 1:
				DialogUtils.informationDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("BranchView.branchDeletionSucceed"));
				break;
			case 0:
				DialogUtils.errorDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
						.getString("BranchView.branchDeletionFailed"));
				break;
			}
		} else {
			StringBuilder attachedSalesmenString = new StringBuilder();
			for (SalesmanFX salesman : salesmenAttachedToBranch) {
				attachedSalesmenString.append(System.lineSeparator() + salesman.getNameProperty().getValue() + " "
						+ salesman.getSurnameProperty().getValue());
			}
			DialogUtils.errorDialog(ResourceBundle.getBundle(PROPERTIES_GUI_RESOURCES)
					.getString("BranchView.branchDEeletionErrorForAttachedSalesman")
					+ attachedSalesmenString.toString());
		}
	}
	
	public void setPropertiesBindings() {
		branchesModel.getBranchObj().getNameProperty().bind(this.nameTextField.textProperty());
		;
	}

}
