/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
 * GitHub      : https://github.com/ClanWolf                        |
 * ---------------------------------------------------------------- |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * C3 includes libraries and source code by various authors.        |
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.character;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.logging.C3Logger;
import net.clanwolf.starmap.client.util.Internationalization;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Undertaker
 */
public class CharacterPaneController extends AbstractC3Controller implements ActionCallBackListener {

	@FXML
	private Button buttonNew;
	@FXML
	private Button buttonSave;
	@FXML
	private Button buttonCancel;
	@FXML
	private Tab char_tab1;
	@FXML
	private Tab char_tab2;
	@FXML
	private TextField char_name;
	@FXML
	private TextField char_birthdate;
	@SuppressWarnings("rawtypes")
	@FXML
	private ComboBox char_owner;
	@SuppressWarnings("rawtypes")
	@FXML
	private ComboBox char_system;
	@SuppressWarnings("rawtypes")
	@FXML
	private ComboBox char_user;
	@SuppressWarnings("rawtypes")
	@FXML
	private ComboBox char_selection;
	@FXML
	private TextArea char_background;
	@FXML
	private Label labelThingsToChange;
	@FXML
	private CheckBox char_NPC;

	private boolean somethingToSave = false;
	private int mode = 0;
	private ChangeListener<? super String> editFieldChangeListener;
	private ChangeListener<? super Object> editComboBoxChangeListener;

	private static final int MODE_IS_NEW = 1;
	// private static final int MODE_IS_EDIT = 2;

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
	}

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		setStrings();
		createListeners();
		enableListeners(true);
		labelThingsToChange.setVisible(false);
	}

	@Override
	public boolean handleAction(ACTIONS action, ActionObject object) {
		switch (action) {
			case CHANGE_LANGUAGE:
				setStrings();
				break;
			case PANE_CREATION_FINISHED:
				if (object.getObject().getClass() == CharacterPane.class) {
					C3Logger.info(object.getObject().toString());
					C3Logger.info("PANE_CREATION_FINISHED: fillComboBox");
					fillComboBox();
				}
				break;
			default:
				break;
		}
		return true;
	}

	/* ----------------- FXML begin ----------------- */
	@FXML
	private void handleNewButtonClick() {
		labelThingsToChange.setVisible(true);
		char_selection.setDisable(true);
		mode = MODE_IS_NEW;
		setSomethingToSave();
		resetFields();

	}

	@FXML
	private void handleSaveButtonClick() {
		labelThingsToChange.setVisible(false);
		somethingToSave = false;
		buttonSave.setDisable(true);
		buttonNew.setDisable(false);
		char_selection.setDisable(false);
		if (mode == MODE_IS_NEW) {

		}
	}

	@FXML
	private void handleCancelButtonClick() {
		resetFields();
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
	}

	/* ----------------- FXML end ----------------- */

	/**
	 *
	 */
	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			buttonNew.setText(Internationalization.getString("general_new"));
			buttonSave.setText(Internationalization.getString("general_save"));
			buttonCancel.setText(Internationalization.getString("general_cancel"));
			char_tab1.setText(Internationalization.getString("app_pane_character_tab1name"));
			char_tab2.setText(Internationalization.getString("app_pane_character_tab2name"));
		});
	}

	private void resetFields() {
		char_selection.getSelectionModel().clearSelection();
		char_owner.getSelectionModel().clearSelection();
		char_user.getSelectionModel().clearSelection();
		char_name.clear();
		char_birthdate.clear();
		char_background.clear();
		char_NPC.setSelected(false);
	}

	private void setSomethingToSave() {
		if (!somethingToSave) {
			labelThingsToChange.setVisible(true);
			buttonSave.setDisable(false);
			buttonCancel.setDisable(false);
			buttonNew.setDisable(true);
			char_selection.setEditable(false);
			char_selection.setDisable(true);
			somethingToSave = true;
		}
		AbstractC3Pane currentPane = Nexus.getCurrentlyOpenedPane();
		if (currentPane != null) {
			currentPane.setModal(true);
		}
	}

	private void createListeners() {
		editFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			setSomethingToSave();
			labelThingsToChange.setVisible(true);
		};

		editComboBoxChangeListener = (ov, old_val, new_val) -> {
			setSomethingToSave();
			labelThingsToChange.setVisible(true);
		};
	}

	@SuppressWarnings("unchecked")
	private void enableListeners(boolean enableListerners) {
		if (enableListerners) {
			char_name.textProperty().addListener(editFieldChangeListener);
			char_birthdate.textProperty().addListener(editFieldChangeListener);
			char_background.textProperty().addListener(editFieldChangeListener);
			char_owner.valueProperty().addListener(editComboBoxChangeListener);
			char_system.valueProperty().addListener(editComboBoxChangeListener);
			char_user.valueProperty().addListener(editComboBoxChangeListener);
			char_NPC.selectedProperty().addListener(editComboBoxChangeListener);
		} else {
			char_name.textProperty().removeListener(editFieldChangeListener);
			char_birthdate.textProperty().removeListener(editFieldChangeListener);
			char_background.textProperty().removeListener(editFieldChangeListener);
			char_owner.valueProperty().removeListener(editComboBoxChangeListener);
			char_system.valueProperty().removeListener(editComboBoxChangeListener);
			char_user.valueProperty().removeListener(editComboBoxChangeListener);
			char_NPC.selectedProperty().removeListener(editComboBoxChangeListener);
		}
	}

	private void fillComboBox() {
		C3Logger.info("fillComboBox");

		/* Combobox Fraktionen füllen */
		/*
		 * if (char_owner.getItems().size() == 0) { char_owner.getItems().addAll(DBWrapper.getOwnerList()); char_owner.setCellFactory( new Callback<ListView<C3_Base_Owner>, ListCell<C3_Base_Owner>>() {
		 *
		 * @Override public ListCell<C3_Base_Owner> call(ListView<C3_Base_Owner> param) { final ListCell<C3_Base_Owner> cell = new ListCell<C3_Base_Owner>() { // { // super.setPrefWidth(100); // }
		 *
		 * @Override public void updateItem(C3_Base_Owner item, boolean empty) { super.updateItem(item, empty); if (item != null) { setText(item.getOwnerType().getName() + ": " + item.getName()); } else { setText(null); }//if }//updateItem };//new
		 * ListCell return cell; }//call }); char_owner.setButtonCell(new ListCell<C3_Base_Owner>() {
		 *
		 * @Override protected void updateItem(C3_Base_Owner t, boolean bln) { super.updateItem(t, bln); if (bln) { setText(""); } else { setText(t.getName()); }//if
		 *
		 * }//updateItem }); }
		 *
		 * // Combobox UserDTO füllen /* if (char_user.getItems().size() == 0) { char_user.getItems().addAll(DBWrapper.getUserList()); char_user.setCellFactory( new Callback<ListView<C3_Base_User>, ListCell<C3_Base_User>>() {
		 *
		 * @Override public ListCell<C3_Base_User> call(ListView<C3_Base_User> param) { final ListCell<C3_Base_User> cell = new ListCell<C3_Base_User>() { // { // super.setPrefWidth(100); // }
		 *
		 * @Override public void updateItem(C3_Base_User item, boolean empty) { super.updateItem(item, empty); if (item != null) { setText(item.getUserName()); } else { setText(null); }//if }//updateItem };//new ListCell return cell; }//call });
		 * char_user.setButtonCell(new ListCell<C3_Base_User>() {
		 *
		 * @Override protected void updateItem(C3_Base_User item, boolean bln) { super.updateItem(item, bln); if (bln) { setText(""); } else { setText(item.getUserName()); }//ift.
		 *
		 * }//updateItem }); }
		 */
	}

	@Override
	public void warningOnAction() {
	}

	@Override
	public void warningOffAction() {
	}
}
