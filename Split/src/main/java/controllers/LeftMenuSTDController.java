package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import utils.UserManager;

public class LeftMenuSTDController {

	private static final String SEARCH_VIEW_FXML = "/fxml/searchCustomerView.fxml";
	private static final String ADD_VIEW_FXML = "/fxml/addCustomerView.fxml";
	private static final String SALESMEN_VIEW_FXML = "/fxml/salesmenView.fxml";
	private static final String BRANCHES_VIEW_FXML = "/fxml/branchesView.fxml";
	
	private MainController mainController;
	
	@FXML
	private ToggleGroup toggleGroupSTD;
	@FXML
	private ToggleButton searchViewButton;

	@FXML
	private ToggleButton addViewButton;
	
	@FXML
	private ToggleButton salesmenViewButton;
	
	@FXML
	private ToggleButton branchesViewButton;

	@FXML
	public void initialize() {
		setAccessRestrictions(UserManager.getActiveUserType());
	}

	@FXML
	public void loadSearchView() {
		if (toggleGroupSTD.getSelectedToggle() != null) {
			toggleGroupSTD.getSelectedToggle().setSelected(false);
		}

		mainController.setCenterView(SEARCH_VIEW_FXML);
		
	}

	@FXML
	public void loadAddView() {
		if (toggleGroupSTD.getSelectedToggle() != null) {
			toggleGroupSTD.getSelectedToggle().setSelected(false);
		}

		mainController.setCenterView(ADD_VIEW_FXML);
		
	}

	@FXML
	public void loadSalesmenView() {
		if (toggleGroupSTD.getSelectedToggle() != null) {
			toggleGroupSTD.getSelectedToggle().setSelected(false);
		}

		mainController.setCenterView(SALESMEN_VIEW_FXML);

	}
	
	@FXML
	public void loadBranchesView() {
		if (toggleGroupSTD.getSelectedToggle() != null) {
			toggleGroupSTD.getSelectedToggle().setSelected(false);
		}

		mainController.setCenterView(BRANCHES_VIEW_FXML);

	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
		
	}
	
	public void setAccessRestrictions(String userType) {
		if(userType.equals("SUPERUSER")) {
			branchesViewButton.setDisable(true);
		}
		if(userType.equals("STANDARD_USER")) {
			salesmenViewButton.setDisable(true);
		}
		if(userType.equals(null)) {
			branchesViewButton.setDisable(true);
			salesmenViewButton.setDisable(true);
			searchViewButton.setDisable(true);
			addViewButton.setDisable(true);
		}
	}
}
