package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class DialogUtils {
	
	public static void errorDialog(String errorMessage) {
		Alert errorDialog = new Alert(Alert.AlertType.ERROR);
		errorDialog.setTitle("Error!");
		errorDialog.setHeaderText("An error occured:");
		
		TextArea text = new TextArea(errorMessage);
		errorDialog.getDialogPane().setContent(text);
		
		errorDialog.showAndWait();
		
	}

	public static void informationDialog(String message) {
		Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
		infoDialog.setTitle("Information");
		infoDialog.setHeaderText("Information:");
		
		TextArea text = new TextArea(message);
		infoDialog.getDialogPane().setContent(text);
		
		infoDialog.showAndWait();
		
	}
	
}
