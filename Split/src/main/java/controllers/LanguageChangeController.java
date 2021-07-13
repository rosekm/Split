package controllers;

import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.LocaleUtils;

public class LanguageChangeController {

	@FXML
	private ComboBox<Locale> languageList;

	@FXML
	private Button saveButton;

	private MainController mainController;

	@FXML
	public void initialize() {
		loadAvailableLanguageList();
	}

	@FXML
	public void setLanguageToApp() {
		Locale selectedLocale = this.languageList.getSelectionModel().getSelectedItem();
		LocaleUtils.setLocale(selectedLocale);
		mainController.reloadMainView(selectedLocale);
		Stage stage = (Stage) saveButton.getScene().getWindow();
		stage.close();
	}

	private void loadAvailableLanguageList() {
		Callback<ListView<Locale>, ListCell<Locale>> factoryLocale = getLanguageListComboBoxCellFactory();
		this.languageList.getItems().addAll(LocaleUtils.getAvailableLocaleList());
		this.languageList.setCellFactory(factoryLocale);
		this.languageList.setButtonCell(factoryLocale.call(null));
		this.languageList.getSelectionModel().select(LocaleUtils.getActiveLocale());
	}

	private Callback<ListView<Locale>, ListCell<Locale>> getLanguageListComboBoxCellFactory() {
		Callback<ListView<Locale>, ListCell<Locale>> factoryLocale = lv -> new ListCell<Locale>() {
			@Override
			protected void updateItem(Locale locale, boolean empty) {
				super.updateItem(locale, empty);
				setText(empty ? "" : locale.getLanguage());
			}
		};
		return factoryLocale;
	}

	public void setMainController(MainController controller) {
		this.mainController = controller;
	}

}
