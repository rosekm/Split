package models;

import java.util.Locale;

import controllers.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.DialogUtils;
import utils.FXMLLoadUtils;
import utils.LocaleUtils;

public class Main extends Application {

	Stage stage;

	public static void main(String[] args) {
		loadLocales();
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		Pane mainPane = FXMLLoadUtils.getFXMLPane("/fxml/mainView.fxml");
		MainController mainController = (MainController) FXMLLoadUtils.getFXMLController("/fxml/mainView.fxml");
		mainController.setMainApplication(this);
		Scene mainScene = new Scene(mainPane, 1300, 800);

		primaryStage.setScene(mainScene);
		primaryStage.setTitle("SPLIT");
		primaryStage.show();

	}

	private static void loadLocales() {
		LocaleUtils.addAvailableLocale(new Locale("pl", "PL"));
		LocaleUtils.addAvailableLocale(new Locale("en", "GB"));
		LocaleUtils.setDefaultLocale();
	}
	
	
	public void reloadUserView(Locale locale) {

		try {
			MainController mainController;
			Pane mainPane = FXMLLoadUtils.getFXMLPane("/fxml/mainView.fxml");
			mainController = (MainController) FXMLLoadUtils.getFXMLController("/fxml/mainView.fxml");
			mainController.setMainApplication(this);
			Scene mainScene = new Scene(mainPane, 1300, 800);
			stage.setScene(mainScene);
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}

	}
}
