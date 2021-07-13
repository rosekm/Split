package utils;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class FXMLLoadUtils {
	
	private static FXMLLoader loader;

	public static Pane getFXMLPane(String fxmlPath) {
		loader = new FXMLLoader(FXMLLoadUtils.class.getResource(fxmlPath));
		
		ResourceBundle guiResources = ResourceBundle.getBundle("properties.GUIResources");
		loader.setResources(guiResources);
		try {
			return loader.load();
		} catch (IOException e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object getFXMLController(String fxmlPath) throws Exception {
		if(loader.getLocation().toString().endsWith(fxmlPath)) {
			return loader.getController();
		} else {
			throw new Exception("FXMLLoad error");
		}
		
	}
}
