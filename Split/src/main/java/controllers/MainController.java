package controllers;

import java.util.Locale;
import java.util.ResourceBundle;

import init.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.DialogUtils;
import utils.FXMLLoadUtils;
import utils.UserManager;

/* 
 * Controller of main view of application
 */
public class MainController {

	private	String activeUserType;
	private final String CHANGE_LANGUAGE_VIEW = "/fxml/languageChangeView.fxml";
	private Main application;

	
	@FXML
	private LeftMenuSTDController leftMenuSTDViewController;

	@FXML
	private BorderPane mainPane;
	
	@FXML
	private MenuItem changeLanguageMenuItem;

	@FXML
	public void initialize() {
		activeUserType = UserManager.getActiveUserType();
		leftMenuSTDViewController.setMainController(this);
		if(activeUserType == null) {
			 DialogUtils.errorDialog(ResourceBundle.getBundle("properties.GUIResources").getString("UserManager.activeUserNotRecognizedMsg")); 
		}
	}

	@FXML
	public void closeApp() {
		Platform.exit();
		System.exit(0);
	}
	
	@FXML
	public void openLanguageSettings() {
		
			Stage languageChangeStage = new Stage();
			Pane languageChangePane = FXMLLoadUtils.getFXMLPane(CHANGE_LANGUAGE_VIEW);
			Scene languageChangeScene = new Scene(languageChangePane);
			languageChangeStage.setScene(languageChangeScene);
			LanguageChangeController langController;
			try {
				langController = (LanguageChangeController)FXMLLoadUtils.getFXMLController(CHANGE_LANGUAGE_VIEW);
				langController.setMainController(this);
				languageChangeStage.showAndWait();
			} catch (Exception e) {
				DialogUtils.errorDialog(e.getMessage());
				e.printStackTrace();
			}
	}

	public void setCenterView(String fxmlPath) {
		Pane centerPane = FXMLLoadUtils.getFXMLPane(fxmlPath);
		mainPane.setCenter(centerPane);
	}
	
	public void reloadMainView(Locale locale) {
		application.reloadUserView(locale);
	}
	
	public void setMainApplication(Main main) {
		this.application = main;
	}

}
