/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : https://www.clanwolf.net                           |
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
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Undertaker
 */
public class StoryEditorPaneController extends AbstractC3Controller implements ActionCallBackListener {

	@FXML
	Button buttonSave;
	@FXML
	Button buttonCancel;
	@FXML
	Button btNewStory;
	@FXML
	Button btNewChapter;
	@FXML
	Button btNewStep;
	@FXML
	Button btEdit;
	@FXML
	Button btRemove;
	@FXML
	Button btAddChar, btRemoveChar;
	@FXML
	Button btSelectImageFile, btSelectVoiceFile, btSelectMovieFile;
	@FXML
	Button btDeleteImageFile, btDeleteVoiceFile, btDeleteMovieFile;
	@FXML
	Button btSortOrderUp, btSortOrderDown;
	@FXML
	Button btDeleteStoryOption1, btDeleteStoryOption2, btDeleteStoryOption3;
	@FXML
	Button btDeleteStoryOptionNextStep;
	@FXML
	Label labStoryName, labDescription, labStorytext, labRolePlayOff, labStoryVariante, labImage, labVoice, labMovie, labURL;
	@FXML
	Label labPathOption1, labPathOption2, labPathOption3, labDataInputText, labDataInputDataset;
	@FXML
	Label labDiceLabel, labDiceScore, labDiceScoreLess, labDiceScoreEqual, labDiceScoreMore, labAssignedChar, labAllCharacters;
	@FXML
	TextField tfStoryName, tfImage, tfVoice, tfMovie, tfURL;
	@FXML
	ComboBox<ROLEPLAYENTRYTYPES> cbStoryVarianten;
	@FXML
	ComboBox<RolePlayStoryDTO> cbStoryPath1, cbStoryPath2, cbStoryPath3, cbNextStep1_V3,cbNextStep2_V3,cbNextStep3_V3,cbNextStep4_V3;
	@FXML
	ComboBox<RolePlayStoryDTO> cbDiceScoreLess, cbDiceScoreEqual, cbDiceScoreMore;
	@FXML
	ComboBox<RolePlayStoryDTO> cbNextStep_V1;
	@FXML
	TextField tfStoryPath1, tfStoryPath2, tfStoryPath3, tfLabelText1_V3,tfLabelText2_V3,tfLabelText3_V3,tfLabelText4_V3, tfDiceScore;
	@FXML
	TextArea taDescription, taStorytext, taRolePlayOff;
	@FXML
	private TreeView<RolePlayStoryDTO> treeStory;
	@FXML
	TabPane tabPaneStory;
	@FXML
	private Tab tabBasic, tabBasic2, tabBasic3, tabBasic4, tabBasic5, tabBasic6, tabBasic7;
	@FXML
	private ListView<RolePlayCharacterDTO> lvAllCharacters, lvAssignedChar;
	@FXML
	private Label labImageAction, labVoiceAction, labMovieAction;
	@FXML
	private TextField tfSortOrder;
	@FXML
	private TextField tfFormName;

	private TreeItem<RolePlayStoryDTO> root;
	private TreeItem<RolePlayStoryDTO> selected;
	private BORolePlayStory boRP;
	private RolePlayStoryDTO rpCopyForSaving;

	private ChangeListener<? super String> editFieldChangeListener;
	private ChangeListener<? super Object> editComboBoxChangeListener;

	private boolean doUploadImage, doUploadSound, doUploadMovie;
	private boolean doDeleteImage, doDeleteSound, doDeleteMovie;

	private int mode = 0;
	private static final int MODE_IS_INIT = -1;
	private static final int MODE_IS_DEFAULT = 0;
	private static final int MODE_IS_NEW = 1;
	private static final int MODE_IS_EDIT = 2;

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);

		// Register actions from BORolePlayStory
		boRP.registerActions(this);
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

		boRP = new BORolePlayStory();

		// Register actions from BORolePlayStory
		boRP.registerActions(this);
		mode = StoryEditorPaneController.MODE_IS_INIT;

		initTreeView();
		initCombobox();

		tfDiceScore.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					tfDiceScore.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		enableButtons();
		enabledFields(false);

		setStrings();
		createListeners();
		enableListeners(true);
		setWarningOff();
	}

	@Override
	public boolean handleAction(ACTIONS action, ActionObject object) {
		switch (action) {
		case CHANGE_LANGUAGE:
			setStrings();
			break;
		case PANE_CREATION_FINISHED:
			if (object.getObject().getClass() == StoryEditorPane.class) {
				C3Logger.info(object.getObject().toString());
				C3Logger.info("PANE_CREATION_FINISHED: fillComboBox");

				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();

				//
				treeStory.setRoot(null);
				initTreeView();

				// Request all stories of the current user from server
				boRP.getAllStories();

				// Request all character from server
				boRP.getAllCharacter();

				//handleTabs();
				tabPaneStory.getTabs().remove(tabBasic2);
				tabPaneStory.getTabs().remove(tabBasic3);
				tabPaneStory.getTabs().remove(tabBasic4);
				tabPaneStory.getTabs().remove(tabBasic5);
				tabPaneStory.getTabs().remove(tabBasic6);
				tabPaneStory.getTabs().remove(tabBasic7);

			}
			break;
		case SAVE_ROLEPLAY_STORY_OK:
			C3Logger.info("SAVE_ROLEPLAY_STORY_OK");

			GameState gs = (GameState)object.getObject();

			RolePlayStoryDTO rps = (RolePlayStoryDTO)gs.getObject();
			ArrayList<RolePlayCharacterDTO> rpcList = (ArrayList<RolePlayCharacterDTO>)gs.getObject2();

			// Only need after an insert. It is not necessary after an update
			// If the RolePlayStoryDTO is new, we need this to get the object with ID from the database.
//			selected.setValue((RolePlayStoryDTO) object.getObject());
			selected.setValue(rps);
			boRP.addStoryToList(selected.getValue());
			boRP.chsngeCharacterList(rpcList);

			/*lvAssignedChar.getItems().clear();
			for (RolePlayCharacterDTO rpc : boRP.getCharacterList()) {
				if (rpc.getStory() != null && rpc.getStory().getId().equals(selected.getValue().getId())) {
					lvAssignedChar.getItems().add(rpc);
				}
			}

			lvAllCharacters.getItems().clear();
			for (RolePlayCharacterDTO rpc : boRP.getCharacterList()) {
				if (rpc.getStory() == null) {
					lvAllCharacters.getItems().add(rpc);
				}
			}*/

			// Upload image, sound and video
			fileTransfer();

			enabledFields(false);
			enableButtons();
			setStrings();

			// refresh TreeItem label
			Event.fireEvent(root, new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), selected, selected.getValue()));

			buttonSave.setDisable(true);

			break;
		case SAVE_ROLEPLAY_STORY_ERR:
			C3Logger.info("SAVE_ROLEPLAY_STORY_ERR");

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					Alert alert = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("general_failure"), Internationalization.getString("general_failure"), Internationalization.getString("app_rp_storyeditor_story_error_save"));
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						setWarningOff();
					}
				}
			});

			break;
		case DELETE_ROLEPLAY_STORY_OK:
			boRP.removeStoryFromList(selected.getValue());
			selected.getParent().getChildren().remove(selected);

			enableButtons();
			break;
		case DELETE_ROLEPLAY_STORY_ERR:
			C3Logger.info("DELETE_ROLEPLAY_STORY_ERR");
			Platform.runLater(() -> {

				Alert alert = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("general_failure"), Internationalization.getString("general_failure"), Internationalization.getString("app_rp_storyeditor_story_error_delete"));
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					setWarningOff();
				}
			});

			break;
		case GET_ROLEPLAY_ALLCHARACTER:
			Platform.runLater(() -> {
				C3Logger.info("GET_ROLEPLAY_ALLCHARACTER");

				@SuppressWarnings("unchecked")
				ArrayList<RolePlayCharacterDTO> hlpLstChar = (ArrayList<RolePlayCharacterDTO>) object.getObject();
				boRP.setCharacterList(hlpLstChar);

				lvAllCharacters.getItems().clear();
				lvAssignedChar.getItems().clear();
				for (RolePlayCharacterDTO rpc : hlpLstChar) {
					if (rpc.getStory() == null) {
						lvAllCharacters.getItems().add(rpc);
					}
				}

			});

			break;
		case GET_ROLEPLAY_ALLSTORIES:
			C3Logger.info("GET_ROLEPLAY_STORYANDCHAPTER");

			@SuppressWarnings("unchecked")
			ArrayList<RolePlayStoryDTO> hlpLst = (ArrayList<RolePlayStoryDTO>) object.getObject();
			boRP.setStoryList(hlpLst);

			// Get story
			ArrayList<RolePlayStoryDTO> liRP = boRP.getStoriesFromList();
			Iterator<RolePlayStoryDTO> iter = liRP.iterator();
			root.getChildren().clear();
			while (iter.hasNext()) {
				TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(iter.next());
				rpTreeItem.setExpanded(false);
				root.getChildren().add(rpTreeItem);

				// Get chapter
				ArrayList<RolePlayStoryDTO> liRPChapter = boRP.getChildsFromStory(rpTreeItem.getValue());
				for (RolePlayStoryDTO aLiRPChapter : liRPChapter) {
					TreeItem<RolePlayStoryDTO> rpTreeItemChapter = new TreeItem<>(aLiRPChapter);
					rpTreeItemChapter.setExpanded(false);
					rpTreeItem.getChildren().add(rpTreeItemChapter);

					// Get step
					ArrayList<RolePlayStoryDTO> liRPStep = boRP.getChildsFromStory(rpTreeItemChapter.getValue());
					for (RolePlayStoryDTO aLiRPStep : liRPStep) {
						TreeItem<RolePlayStoryDTO> rpTreeItemStep = new TreeItem<>(aLiRPStep);
						rpTreeItemStep.setExpanded(false);
						rpTreeItemChapter.getChildren().add(rpTreeItemStep);
					}
				}
			}

			mode = StoryEditorPaneController.MODE_IS_DEFAULT;
			enableButtons();
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();

		default:
			break;
		}
		return true;
	}

	/* ----------------- FXML begin ----------------- */
	@FXML
	private void handleNewStoryButtonClick() {
		C3Logger.info("handleNewButtonClick");
		TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(boRP.addNewStory());
		root.getChildren().add(rpTreeItem);

		treeStory.getSelectionModel().select(rpTreeItem);
		selected = rpTreeItem;

		mode = StoryEditorPaneController.MODE_IS_NEW;
		setWarningOn(true);
		setData();

	}

	@FXML
	private void handleNewChapterButtonClick() {
		C3Logger.info("handleNewChapterButtonClick");

		// Can be only added if parent is a story
		if (treeStory.getTreeItemLevel(selected) == 1) {
			TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(boRP.addNewChapter(selected.getValue()));
			selected.getChildren().add(rpTreeItem);
			selected.setExpanded(true);

			treeStory.getSelectionModel().select(rpTreeItem);
			selected = rpTreeItem;

			mode = StoryEditorPaneController.MODE_IS_NEW;
			setWarningOn(true);
			setData();

		}
		enableButtons();
	}

	@FXML
	private void handleNewStoryStepButtonClick() {
		C3Logger.info("handleNewStoryStepButtonClick");
		// Can be only added if parent is a chapter
		// or it is a step. In this case add it on the same level
		if (treeStory.getTreeItemLevel(selected) == 2) {

			TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(boRP.addNewStoryStep(selected.getValue()));
			selected.getChildren().add(rpTreeItem);
			selected.setExpanded(true);

			treeStory.getSelectionModel().select(rpTreeItem);
			selected = rpTreeItem;

			mode = StoryEditorPaneController.MODE_IS_NEW;
			setWarningOn(true);
			fillComboboxWithStories();
			setData();

		} else if (treeStory.getTreeItemLevel(selected) == 3) {

			TreeItem<RolePlayStoryDTO> rpTreeItem2 = new TreeItem<>(boRP.addNewStoryStep(selected.getParent().getValue()));
			selected.getParent().getChildren().add(rpTreeItem2);
			selected.setExpanded(true);

			treeStory.getSelectionModel().select(rpTreeItem2);
			selected = rpTreeItem2;

			mode = StoryEditorPaneController.MODE_IS_NEW;
			setWarningOn(true);
			setData();

		}
		enableButtons();
	}

	@FXML
	private void handleDeleteSeletedTreeItem() {
		C3Logger.info("handleDeleteSeletedTreeItem");

		Alert alert = Tools.C3Dialog(AlertType.CONFIRMATION, Internationalization.getString("app_rp_storyeditor_story_delete_question_label"), Internationalization.getString("app_rp_storyeditor_story_delete_question_label"),
				Internationalization.getString("app_rp_storyeditor_story_delete_question_text"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			if (boRP.checkBeforeDelete(selected.getValue())) {

				selected.getValue().setSortOrderOld(selected.getValue().getSortOrder());
				selected.getValue().setSortOrder(selected.getParent().getChildren().size());

				boRP.delete(selected.getValue());
			} else {
				// Do not delete
				Alert alert2 = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("general_failure"), Internationalization.getString("app_rp_storyeditor_story_delete_question_label"), boRP.getErrorText());
				alert2.showAndWait();
			}
			enableButtons();
		}
	}

	@FXML
	private void handleSelectTreeItem() {
		C3Logger.info("handleSelectTreeItem");
		if (mode == StoryEditorPaneController.MODE_IS_DEFAULT) {
			selected = treeStory.getSelectionModel().getSelectedItem();
			C3Logger.info(treeStory.getTreeItemLevel(selected) + "");

			enableListeners(false);
			enableButtons();

			fillComboboxWithStories();
			setData();

			// Test
			if (selected != null) {
				Event.fireEvent(selected.getParent(), new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), selected, selected.getValue()));
			}

			enabledFields(false);
			enableListeners(true);
		}

		tabPaneStory.getSelectionModel().select(0);

		handleTabs();
	}

	@FXML
	private void handleEditStoryButtonClick() {
		C3Logger.info("handleEditStoryButtonClick");
		C3Logger.info("Message -> Kommunikationskanal wird bereitgestellt... please hold the line :-)");
		tabPaneStory.getSelectionModel().select(0);
		mode = StoryEditorPaneController.MODE_IS_EDIT;
		setWarningOn(true);
	}

	@FXML
	private void handleSaveButtonClick() {

		// RolePlayStoryDTO copy = boRP.getCopy(selected.getValue());
		rpCopyForSaving = boRP.getCopy(selected.getValue());

		// if (boRP.checkBeforeSave(getData(boRP.getCopy(copy)))) {
		if (boRP.checkBeforeSave(getData(rpCopyForSaving))) {
			setWarningOff();
			mode = StoryEditorPaneController.MODE_IS_DEFAULT;

			boRP.save(getData(selected.getValue()));
			selected.setValue(getData(selected.getValue()));
		} else {
			Alert a = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("general_failure"), Internationalization.getString("general_failure"), boRP.getErrorText());
			a.showAndWait();
		}

	}

	@FXML
	private void handleCancelButtonClick() {

		String title;
		String header;
		String content;

		/* Set message text after click on cancel button */
		if (mode == StoryEditorPaneController.MODE_IS_NEW || mode == StoryEditorPaneController.MODE_IS_EDIT) {
			title = Internationalization.getString("app_rp_storyeditor_story_cancel_edit_title");
			header = Internationalization.getString("app_rp_storyeditor_story_canel_edit_header");
			content = Internationalization.getString("app_rp_storyeditor_story_canel_edit_message");

		} else {
			title = Internationalization.getString("app_rp_storyeditor_story_close_title");
			header = Internationalization.getString("app_rp_storyeditor_story_close_header");
			content = Internationalization.getString("app_rp_storyeditor_story_close_message");

		}

		Alert alert = Tools.C3Dialog(AlertType.CONFIRMATION, title, header, content);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			enabledFields(false);

			if (mode == StoryEditorPaneController.MODE_IS_NEW) {
				TreeItem<RolePlayStoryDTO> toDelete = selected;
				selected.getParent().getChildren().remove(toDelete);

				resetFields();
				setWarningOff();
				mode = StoryEditorPaneController.MODE_IS_DEFAULT;

				buttonSave.setDisable(true);
				enableButtons();
				setStrings();

			} else if (mode == StoryEditorPaneController.MODE_IS_EDIT) {
				resetFields();

				// reorganize tree
				TreeItem<RolePlayStoryDTO> parent = selected.getParent();
				int index = parent.getChildren().indexOf(selected);
				parent.getChildren().remove(index);
				parent.getChildren().add(selected.getValue().getSortOrder() - 1, selected);
				treeStory.getSelectionModel().select(selected);

				setData();

				setWarningOff();
				buttonSave.setDisable(true);
				mode = StoryEditorPaneController.MODE_IS_DEFAULT;
				enableButtons();
				setStrings();

			} else {
				ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
			}
		} else {
			C3Logger.info("Do Nothing");
		}
	}

	@FXML
	private void handleSelectImageFile() {
		File file = callFileChooser("Bilddatei (*.png)", "*.png");

		if (file != null) {
			C3Logger.info("File ausgewählt");
			tfImage.setText(file.getAbsolutePath());
			doUploadImage = true;
			doDeleteImage = false;

		}
	}

	@FXML
	private void handleImageOnKeyTyped() {

		if (tfImage.getText() == null) {
			doDeleteImage = true;
			doUploadImage = false;

		} else {
			doUploadImage = true;
			doDeleteImage = false;
		}

		C3Logger.info("handleImageOnKeyTyped");
	}

	@FXML
	private void handleVoiceOnKeyTyped() {
		if (tfImage.getText() == null) {
			doDeleteSound = true;
			doUploadSound = false;

		} else {
			doUploadSound = true;
			doDeleteSound = false;
		}
		C3Logger.info("handleVoiceOnKeyTyped");
	}

	@FXML
	private void handleMovieKeyTyped() {
		if (tfImage.getText() == null) {
			doDeleteMovie = true;
			doUploadMovie = false;

		} else {
			doUploadMovie = true;
			doDeleteMovie = false;
		}
		C3Logger.info("handleMovieKeyTyped");
	}

	@FXML
	private void handleSelectVoiceFile() {
		File file = callFileChooser("Bilddatei (*.mp3)", "*.mp3");

		if (file != null) {
			C3Logger.info("File ausgewählt");
			tfVoice.setText(file.getAbsolutePath());
			doUploadSound = true;
			doDeleteSound = false;
		}
	}

	@FXML
	private void handleDeleteImageFile() {
		tfImage.setText(null);
		doUploadImage = false;
		doDeleteImage = true;
		if (selected.getValue().getStoryImage() != null && !selected.getValue().getStoryImage().isEmpty()) {
			labImageAction.setVisible(true);
		}
	}

	@FXML
	private void handleDeleteVoiceFile() {
		tfVoice.setText(null);
		doUploadSound = false;
		doDeleteSound = true;
		if (selected.getValue().getStoryMP3() != null && !selected.getValue().getStoryMP3().isEmpty()) {
			labVoiceAction.setVisible(true);
		}
	}

	@FXML
	private void handleDeleteMovieFile() {
		tfMovie.setText(null);
		doUploadMovie = false;
		doDeleteMovie = true;
		if (selected.getValue().getStoryIntro() != null && !selected.getValue().getStoryIntro().isEmpty()) {
			labMovieAction.setVisible(true);
		}
	}

	@FXML
	private void handleSelectMovieFile() {
		File file = callFileChooser("Bilddatei (*.mp4)", "*.mp4");

		if (file != null) {
			C3Logger.info("File ausgew�hlt");
			tfMovie.setText(file.getAbsolutePath());
			doUploadMovie = true;
			doDeleteMovie = false;
		}
	}

	@FXML
	private void handleSelectionTabCharAssignment() {
		C3Logger.info("handleSelectionTabCharAssignment");
		if (tabBasic3.isSelected()) {
			C3Logger.info("handleSelectionTabCharAssignment -> selected");
		}
	}

	@FXML
	private void handleAddChar() {
		if (lvAllCharacters.getSelectionModel().getSelectedItem() != null) {
			lvAssignedChar.getItems().add(lvAllCharacters.getSelectionModel().getSelectedItem());
			lvAllCharacters.getItems().remove(lvAllCharacters.getSelectionModel().getSelectedItem());

			setWarningOn(true);
		}
	}

	@FXML
	private void handleRemoveChar() {
		if (lvAssignedChar.getSelectionModel().getSelectedItem() != null) {
			lvAllCharacters.getItems().add(lvAssignedChar.getSelectionModel().getSelectedItem());
			lvAssignedChar.getItems().remove(lvAssignedChar.getSelectionModel().getSelectedItem());

			setWarningOn(true);
		}
	}

	@FXML
	private void handleSortOrderUp() {
		C3Logger.info("handleSortOrderUp");
		Integer sortOrder = Integer.valueOf(tfSortOrder.getText());
		if (sortOrder < selected.getParent().getChildren().size()) {
			sortOrder++;
			tfSortOrder.setText(sortOrder.toString());

			TreeItem<RolePlayStoryDTO> parent = selected.getParent();

			int index = parent.getChildren().indexOf(selected);
			parent.getChildren().remove(index);
			parent.getChildren().add(sortOrder - 1, selected);

			treeStory.getSelectionModel().select(selected);

		}
	}

	@FXML
	private void handleSortOrderDown() {
		C3Logger.info("handleSortOrderDown");
		Integer sortOrder = Integer.valueOf(tfSortOrder.getText());
		if (sortOrder > 1) {
			sortOrder--;
			tfSortOrder.setText(sortOrder.toString());

			TreeItem<RolePlayStoryDTO> parent = selected.getParent();

			int index = parent.getChildren().indexOf(selected);
			parent.getChildren().remove(index);
			parent.getChildren().add(sortOrder - 1, selected);

			treeStory.getSelectionModel().select(selected);

		}

	}

	@FXML
	private void handleCbStoryVariantenAction() {
		C3Logger.info("handleCbStoryVariantenAction");
		handleTabs();

	}

	@FXML
	private void handleDeleteStoryOption1() {
		tfStoryPath1.setText(tfStoryPath2.getText());
		cbStoryPath1.getSelectionModel().select(cbStoryPath2.getValue());

		tfStoryPath2.setText(tfStoryPath3.getText());
		cbStoryPath2.getSelectionModel().select(cbStoryPath3.getValue());

		tfStoryPath3.clear();
		cbStoryPath3.setValue(null);
	}

	@FXML
	private void handleDeleteStoryOption2() {
		tfStoryPath2.setText(tfStoryPath3.getText());
		cbStoryPath2.getSelectionModel().select(cbStoryPath3.getValue());

		tfStoryPath3.clear();
		cbStoryPath3.setValue(null);
	}

	@FXML
	private void handleDeleteStoryOption3() {
		tfStoryPath3.clear();
		cbStoryPath3.setValue(null);

	}

	@FXML
	private void handleDeleteStoryOptionNextStep() {
		cbNextStep_V1.getSelectionModel().select(cbNextStep_V1.getValue());

		tfStoryPath3.clear();
		cbNextStep_V1.setValue(null);
	}

	@FXML
	private void handleDiceScoreTextChanged() {
		C3Logger.info("handleDiceScoreTextChanged");

		// if (!tfDiceScore.getText().matches("\\d*")) {
		// tfDiceScore.setText(tfDiceScore.getText().replaceAll("[^\\d]", ""));
		// }

	}

	/* ----------------- FXML end ----------------- */

	private File callFileChooser(String filterName, String filter) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterName, filter);
		chooser.getExtensionFilters().add(extFilter);

		return chooser.showOpenDialog(treeStory.getScene().getWindow());
	}

	private void handleTabs() {

		tabPaneStory.getTabs().remove(tabBasic2);
		tabPaneStory.getTabs().remove(tabBasic3);
		tabPaneStory.getTabs().remove(tabBasic4);
		tabPaneStory.getTabs().remove(tabBasic5);
		tabPaneStory.getTabs().remove(tabBasic6);
		tabPaneStory.getTabs().remove(tabBasic7);

		// Tab for character assignment
		if (selected != null && selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {
			if (!tabPaneStory.getTabs().contains(tabBasic3)) {
				tabPaneStory.getTabs().add(tabBasic3);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic3)) {
				tabPaneStory.getTabs().remove(tabBasic3);
			}
		}

		if (selected != null && selected.getValue().getVariante() != ROLEPLAYENTRYTYPES.C3_RP_STORY &&
		    selected != null && selected.getValue().getVariante() != ROLEPLAYENTRYTYPES.C3_RP_CHAPTER) {
			if (!tabPaneStory.getTabs().contains(tabBasic2)) {
				tabPaneStory.getTabs().add(tabBasic2);
			}
		}

		// Tab for simple step
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1) {
			if (!tabPaneStory.getTabs().contains(tabBasic7)) {
				tabPaneStory.getTabs().add(tabBasic7);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic7)) {
				tabPaneStory.getTabs().remove(tabBasic7);
			}
		}

		// Tab for story path selection
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V2) {
			if (!tabPaneStory.getTabs().contains(tabBasic4)) {
				tabPaneStory.getTabs().add(tabBasic4);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic4)) {
				tabPaneStory.getTabs().remove(tabBasic4);
			}
		}

		// Tab for datainput
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V3) {
			if (!tabPaneStory.getTabs().contains(tabBasic5)) {
				tabPaneStory.getTabs().add(tabBasic5);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic5)) {
				tabPaneStory.getTabs().remove(tabBasic5);
			}
		}

		// Tab for datainput
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V4) {
			if (!tabPaneStory.getTabs().contains(tabBasic6)) {
				tabPaneStory.getTabs().add(tabBasic6);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic6)) {
				tabPaneStory.getTabs().remove(tabBasic6);
			}
		}
	}

	/**
	 * Initalisation of treeview TreeStory
	 */
	private void initTreeView() {

		// storyRoot will be never stored in database
		RolePlayStoryDTO storyRoot = new RolePlayStoryDTO();
		storyRoot.setStoryName("All storys");

		root = new TreeItem<>(storyRoot);
		root.setExpanded(true);

		treeStory.setShowRoot(false);
		treeStory.setRoot(root);

		treeStory.setCellFactory(new Callback<TreeView<RolePlayStoryDTO>, TreeCell<RolePlayStoryDTO>>() {
			@Override
			public TreeCell<RolePlayStoryDTO> call(TreeView<RolePlayStoryDTO> p) {
				return new TreeCell<RolePlayStoryDTO>() {

					@Override
					protected void updateItem(RolePlayStoryDTO item, boolean empty) {
						super.updateItem(item, empty);
						boolean hasWarning = false;
						boolean isNextStep = false;

						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item.getStoryName());
							C3Logger.info("setCellFactory: updateItem: " + item.getStoryName());

							// Set marking, if it is a next step
							if (selected != null && boRP.isNextStep(selected.getValue(), item)) {
								isNextStep = true;

							}
							// if the step need other steps and has no entries for that, then mark it red and bold
							if (boRP.hasNoNextStepEntry(item)) {
								hasWarning = true;
								setStyle("-fx-text-fill:red; -fx-font-weight: bold;");

							} else {
								setStyle("-fx-text-fill:black; -fx-font-weight: normal;");

							}

							// set a new pane with a list of icons
							IconList il = new IconList(item.getVariante(), isNextStep, hasWarning);
							setGraphic(il);
						}
						C3Logger.info("setCellFactory: updateItem: ");
					}
				};
			}
		});

	}

	/**
	 * Initalisation of combobox cbStoryVarianten
	 */
	private void initCombobox() {
		cbStoryVarianten.getItems().setAll(ROLEPLAYENTRYTYPES.values());

	}

	private void fillComboboxWithStories() {

		if (selected != null) {
			cbStoryPath1.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbStoryPath2.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbStoryPath3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbNextStep1_V3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbNextStep2_V3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbNextStep3_V3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbNextStep4_V3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbDiceScoreEqual.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbDiceScoreLess.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbDiceScoreMore.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbNextStep_V1.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

		}
	}

	private void enableButtons() {

		if (mode == StoryEditorPaneController.MODE_IS_DEFAULT) {
			TreeItem<RolePlayStoryDTO> selected = treeStory.getSelectionModel().getSelectedItem();

			// It can be always added a new story
			btNewStory.setDisable(false);

			if (treeStory.getTreeItemLevel(selected) == 1) {
				btNewChapter.setDisable(false);
				btNewStep.setDisable(true);
				btEdit.setDisable(false);
				btRemove.setDisable(false);
				btAddChar.setDisable(true);
				btRemoveChar.setDisable(true);

			} else if (treeStory.getTreeItemLevel(selected) == 2) {
				btNewChapter.setDisable(true);
				btNewStep.setDisable(false);
				btEdit.setDisable(false);
				btRemove.setDisable(false);
				btAddChar.setDisable(true);
				btRemoveChar.setDisable(true);

			} else if (treeStory.getTreeItemLevel(selected) == 3) {
				btNewChapter.setDisable(true);
				btNewStep.setDisable(false);
				btEdit.setDisable(false);
				btRemove.setDisable(false);
				btAddChar.setDisable(true);
				btRemoveChar.setDisable(true);

			} else {
				btNewChapter.setDisable(true);
				btNewStep.setDisable(true);
				btEdit.setDisable(true);
				btRemove.setDisable(true);
				btAddChar.setDisable(true);
				btRemoveChar.setDisable(true);
			}
		} else if (mode == StoryEditorPaneController.MODE_IS_EDIT || mode == StoryEditorPaneController.MODE_IS_NEW) {
			btNewChapter.setDisable(true);
			btNewStep.setDisable(true);
			btEdit.setDisable(true);
			btRemove.setDisable(true);
			btNewStory.setDisable(true);
			if (selected.getValue().getParentStory() == null) {
				btAddChar.setDisable(false);
				btRemoveChar.setDisable(false);
			}
		} else {
			btNewChapter.setDisable(true);
			btNewStep.setDisable(true);
			btEdit.setDisable(true);
			btRemove.setDisable(true);
			btNewStory.setDisable(true);
			btAddChar.setDisable(true);
			btRemoveChar.setDisable(true);
		}
	}

	private void enabledFields(boolean enable) {
		if (enable) {
			tfStoryName.setDisable(false);
			taDescription.setDisable(false);
			taStorytext.setDisable(false);
			taRolePlayOff.setDisable(false);
			btSelectImageFile.setDisable(false);
			btSelectVoiceFile.setDisable(false);
			btSelectMovieFile.setDisable(false);
			btDeleteImageFile.setDisable(false);
			btDeleteVoiceFile.setDisable(false);
			btDeleteMovieFile.setDisable(false);

			tfImage.setDisable(false);
			tfVoice.setDisable(false);
			tfMovie.setDisable(false);
			tfURL.setDisable(false);

			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY || selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER) {
				cbStoryVarianten.setDisable(true);
			} else {
				cbStoryVarianten.setDisable(false);
			}

			btSortOrderUp.setDisable(false);
			btSortOrderDown.setDisable(false);
			tfSortOrder.setDisable(false);

			tfStoryPath1.setDisable(false);
			tfStoryPath2.setDisable(false);
			tfStoryPath3.setDisable(false);
			cbStoryPath1.setDisable(false);
			cbStoryPath2.setDisable(false);
			cbStoryPath3.setDisable(false);
			btDeleteStoryOption1.setDisable(false);
			btDeleteStoryOption2.setDisable(false);
			btDeleteStoryOption3.setDisable(false);

			tfFormName.setDisable(false);
			cbNextStep1_V3.setDisable(false);
			cbNextStep2_V3.setDisable(false);
			cbNextStep3_V3.setDisable(false);
			cbNextStep4_V3.setDisable(false);

			tfDiceScore.setDisable(false);
			cbDiceScoreEqual.setDisable(false);
			cbDiceScoreLess.setDisable(false);
			cbDiceScoreMore.setDisable(false);

			cbNextStep_V1.setDisable(false);
			btDeleteStoryOptionNextStep.setDisable(false);

		} else {
			tfStoryName.setDisable(true);
			cbStoryVarianten.setDisable(true);
			taDescription.setDisable(true);
			taStorytext.setDisable(true);
			taRolePlayOff.setDisable(true);
			btSelectImageFile.setDisable(true);
			btSelectVoiceFile.setDisable(true);
			btSelectMovieFile.setDisable(true);
			btDeleteImageFile.setDisable(true);
			btDeleteVoiceFile.setDisable(true);
			btDeleteMovieFile.setDisable(true);
			tfImage.setDisable(true);
			tfVoice.setDisable(true);
			tfMovie.setDisable(true);
			tfURL.setDisable(true);

			btSortOrderUp.setDisable(true);
			btSortOrderDown.setDisable(true);
			tfSortOrder.setDisable(true);

			tfStoryPath1.setDisable(true);
			tfStoryPath2.setDisable(true);
			tfStoryPath3.setDisable(true);
			cbStoryPath1.setDisable(true);
			cbStoryPath2.setDisable(true);
			cbStoryPath3.setDisable(true);
			btDeleteStoryOption1.setDisable(true);
			btDeleteStoryOption2.setDisable(true);
			btDeleteStoryOption3.setDisable(true);

			tfFormName.setDisable(true);
			cbNextStep1_V3.setDisable(true);
			cbNextStep2_V3.setDisable(true);
			cbNextStep3_V3.setDisable(true);
			cbNextStep4_V3.setDisable(true);

			tfDiceScore.setDisable(true);
			cbDiceScoreEqual.setDisable(true);
			cbDiceScoreLess.setDisable(true);
			cbDiceScoreMore.setDisable(true);

			cbNextStep_V1.setDisable(true);
			btDeleteStoryOptionNextStep.setDisable(true);

		}

		// Always disabled. The only way to change the fields is to use the buttons btImage, btVoice, btMovie
		labImageAction.setVisible(false);
		labVoiceAction.setVisible(false);
		labMovieAction.setVisible(false);
	}

	private void setData() {

		if (selected != null) {
			cbStoryVarianten.setValue(selected.getValue().getVariante());
			tfStoryName.setText(selected.getValue().getStoryName());
			taDescription.setText(selected.getValue().getStoryDescription());
			taStorytext.setText(selected.getValue().getStoryText());
			tfImage.setText(selected.getValue().getStoryImage());
			tfVoice.setText(selected.getValue().getStoryMP3());
			tfMovie.setText(selected.getValue().getStoryIntro());
			taRolePlayOff.setText(selected.getValue().getRolePlayOff());
			tfURL.setText(selected.getValue().getUrl());

			if (selected.getValue().getId() != null) {
				tfSortOrder.setText(selected.getValue().getSortOrder().toString());

			} else {
				int newSortOrder = selected.getParent().getChildren().indexOf(selected) + 1;
				selected.getValue().setSortOrder(newSortOrder);
				tfSortOrder.setText(String.valueOf(newSortOrder));
			}


			lvAssignedChar.getItems().clear();
			for (RolePlayCharacterDTO rpc : boRP.getCharacterList()) {
				if (rpc.getStory() != null && rpc.getStory().getStory().getId().equals(selected.getValue().getId())) {
					lvAssignedChar.getItems().add(rpc);
				}
			}

			doUploadImage = false;
			doUploadSound = false;
			doUploadMovie = false;
			doDeleteImage = false;
			doDeleteSound = false;
			doDeleteMovie = false;

			// set data for story variante 1
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 && selected.getValue().getNextStepID() != null) {
				cbNextStep_V1.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getNextStepID()));
			} else {
				cbNextStep_V1.setValue(null);
			}

			// set data for story variante 2
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V2 && selected.getValue().getVar2ID() != null) {
				tfStoryPath1.setText(selected.getValue().getVar2ID().getOption1Text());
				tfStoryPath2.setText(selected.getValue().getVar2ID().getOption2Text());
				tfStoryPath3.setText(selected.getValue().getVar2ID().getOption3Text());

				cbStoryPath1.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption1StoryID()));
				cbStoryPath2.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption2StoryID()));
				cbStoryPath3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption3StoryID()));

			} else {
				cbStoryPath1.setValue(null);
				cbStoryPath2.setValue(null);
				cbStoryPath3.setValue(null);
				tfStoryPath1.clear();
				tfStoryPath2.clear();
				tfStoryPath3.clear();
			}

			// set data for story variante 3
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V3 && selected.getValue().getVar3ID() != null) {

				tfFormName.setText(selected.getValue().getVar3ID().getFormName());
				cbNextStep1_V3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar3ID().getNextStoryID()));
				cbNextStep2_V3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar3ID().getNextStory2ID()));
				cbNextStep3_V3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar3ID().getNextStory3ID()));
				cbNextStep4_V3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar3ID().getNextStory4ID()));

				tfLabelText1_V3.setText(selected.getValue().getVar3ID().getLabelText());
				tfLabelText2_V3.setText(selected.getValue().getVar3ID().getLabelText2());
				tfLabelText3_V3.setText(selected.getValue().getVar3ID().getLabelText3());
				tfLabelText4_V3.setText(selected.getValue().getVar3ID().getLabelText4());

			} else {

				tfFormName.clear();
				cbNextStep1_V3.setValue(null);
				cbNextStep2_V3.setValue(null);
				cbNextStep3_V3.setValue(null);
				cbNextStep4_V3.setValue(null);
				tfLabelText1_V3.clear();
				tfLabelText2_V3.clear();
				tfLabelText3_V3.clear();
				tfLabelText4_V3.clear();
			}

			// set data for story variante 4
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V4 && selected.getValue().getVar4ID() != null) {

				tfDiceScore.setText(selected.getValue().getVar4ID().getScore().toString());
				cbDiceScoreEqual.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar4ID().getStoryIDScoreEqual()));
				cbDiceScoreLess.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar4ID().getStoryIDScoreLess()));
				cbDiceScoreMore.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar4ID().getStoryIDScoreMore()));

			} else {

				tfDiceScore.clear();
				cbDiceScoreEqual.setValue(null);
				cbDiceScoreLess.setValue(null);
				cbDiceScoreMore.setValue(null);
			}

		}
	}

	private RolePlayStoryDTO getData(RolePlayStoryDTO rp) {
		rp.setStoryName(tfStoryName.getText());
		rp.setVariante(cbStoryVarianten.getValue());
		rp.setStoryDescription(taDescription.getText());
		rp.setStoryText(taStorytext.getText());
		rp.setRolePlayOff(taRolePlayOff.getText());
		rp.setUrl(tfURL.getText());

		// keep the actual sort number
		rp.setSortOrderOld(rp.getSortOrder());
		rp.setSortOrder(Integer.valueOf(tfSortOrder.getText()));

		if (doUploadImage) {
			rp.setStoryImage(Paths.get(tfImage.getText()).getFileName().toString());
			rp.setNewImageWithPath(tfImage.getText());
		}

		if (doDeleteImage) {
			rp.setStoryImage(null);
			rp.setNewImageWithPath(null);
		}

		if (doUploadSound) {
			rp.setStoryMP3(Paths.get(tfVoice.getText()).getFileName().toString());
			rp.setNewVoiceWithPath(tfVoice.getText());
		}

		if (doDeleteSound) {
			rp.setStoryMP3(null);
			rp.setNewVoiceWithPath(null);
		}

		if (doUploadMovie) {
			rp.setStoryIntro(Paths.get(tfMovie.getText()).getFileName().toString());
			rp.setNewMovieWithPath(tfMovie.getText());
		}

		if (doDeleteMovie) {
			rp.setStoryIntro(null);
			rp.setNewMovieWithPath(null);
		}

		//Save RolePlayCharacter
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {
			ArrayList<Long> newCharIds = new ArrayList<>();
			// find all new added chars
			for (RolePlayCharacterDTO rpc : lvAssignedChar.getItems()) {
				if (rpc.getStory() == null) {
					C3Logger.info("Add new char to Story: " + rpc.getName());
					newCharIds.add(rpc.getId());
				} else {
					C3Logger.info("do nothing: " + rpc.getName());
				}
			}
			rp.setNewCharIDs(newCharIds);


			ArrayList<Long> removedCharIds = new ArrayList<>();
			// find all chars to remove from story
			for (RolePlayCharacterDTO rpc : lvAllCharacters.getItems()) {
				if (rpc.getStory() != null) {
					C3Logger.info("Remove from story: " + rpc.getName());
					removedCharIds.add(rpc.getId());
				} else {
					C3Logger.info("do nothing: " + rpc.getName());
				}
			}
			rp.setRemovedCharIDs(removedCharIds);
		}

		// set data for variante 1
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1) {
			rp.setNextStepID(cbNextStep_V1.getValue().getId());
		}

		// set data for variante 2
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V2) {

			RolePlayStoryVar2DTO rpVar2 = rp.getVar2ID();
			if (rpVar2 == null) {
				rpVar2 = new RolePlayStoryVar2DTO();
				rpVar2.setStory(rp.getId());
			}

			//rpVar2.setOption1StoryID(cbStoryPath1.getValue().getId());
			//rpVar2.setOption2StoryID(cbStoryPath2.getValue().getId());
			//rpVar2.setOption3StoryID(cbStoryPath3.getValue().getId());

			if(cbStoryPath1.getValue() != null) {
				rpVar2.setOption1StoryID(cbStoryPath1.getValue().getId());
			}
			if(cbStoryPath2.getValue() != null) {
				rpVar2.setOption2StoryID(cbStoryPath2.getValue().getId());
			}
			if(cbStoryPath3.getValue() != null) {
				rpVar2.setOption3StoryID(cbStoryPath3.getValue().getId());
			}

			rpVar2.setOption1Text(tfStoryPath1.getText());
			rpVar2.setOption2Text(tfStoryPath2.getText());
			rpVar2.setOption3Text(tfStoryPath3.getText());

			rp.setVar2ID(rpVar2);

		} else {
			rp.setVar2ID(null);
		}

		// set data for variante 3
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V3) {

			RolePlayStoryVar3DTO rpVar3 = rp.getVar3ID();
			if (rpVar3 == null) {
				rpVar3 = new RolePlayStoryVar3DTO();
				rpVar3.setStory(rp.getId());
			}

			rpVar3.setFormName(tfFormName.getText());
			//rpVar3.setNextStoryID(cbNextStep1_V3.getValue().getId());
			//rpVar3.setNextStory2ID(cbNextStep2_V3.getValue().getId());
			//rpVar3.setNextStory3ID(cbNextStep3_V3.getValue().getId());
			//rpVar3.setNextStory4ID(cbNextStep4_V3.getValue().getId());

			if(cbNextStep1_V3.getValue() != null) {
				rpVar3.setNextStoryID(cbNextStep1_V3.getValue().getId());
			}
			if(cbNextStep2_V3.getValue() != null) {
				rpVar3.setNextStory2ID(cbNextStep2_V3.getValue().getId());
			}
			if(cbNextStep3_V3.getValue() != null) {
				rpVar3.setNextStory3ID(cbNextStep3_V3.getValue().getId());
			}
			if(cbNextStep4_V3.getValue() != null) {
				rpVar3.setNextStory4ID(cbNextStep4_V3.getValue().getId());
			}

			rpVar3.setLabelText(tfLabelText1_V3.getText());
			rpVar3.setLabelText2(tfLabelText2_V3.getText());
			rpVar3.setLabelText3(tfLabelText3_V3.getText());
			rpVar3.setLabelText4(tfLabelText4_V3.getText());

			rp.setVar3ID(rpVar3);

		} else {
			rp.setVar3ID(null);
		}

		// set data for variante 4
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V4) {

			RolePlayStoryVar4DTO rpVar4 = rp.getVar4ID();
			if (rpVar4 == null) {
				rpVar4 = new RolePlayStoryVar4DTO();
				rpVar4.setStory(rp);
			}

			if (tfDiceScore.getText().isEmpty()) {
				rpVar4.setScore(null);
			} else {
				rpVar4.setScore(Integer.valueOf(tfDiceScore.getText()));
			}
			rpVar4.setStoryIDScoreEqual(cbDiceScoreEqual.getValue().getId());
			rpVar4.setStoryIDScoreLess(cbDiceScoreLess.getValue().getId());
			rpVar4.setStoryIDScoreMore(cbDiceScoreMore.getValue().getId());

			rp.setVar4ID(rpVar4);

		} else {
			rp.setVar4ID(null);
		}

		return rp;

	}

	private void fileTransfer() {
		boolean bOK = false;
		if (doUploadImage) {
			bOK = boRP.uploadImage(selected.getValue());
		}

		if (doDeleteImage) {
			bOK = boRP.deleteImage(selected.getValue());
		}

		if (doUploadSound) {
			bOK = boRP.uploadSound(selected.getValue());
		}

		if (doDeleteSound) {
			bOK = boRP.deleteSound(selected.getValue());
		}

		if (doUploadMovie) {
			bOK = boRP.uploadVideo(selected.getValue());
		}

		if (doDeleteMovie) {
			bOK = boRP.deleteVideo(selected.getValue());
		}

		if (bOK) C3Logger.debug("Error while upload!");
	}

	/**
	 *
	 */
	@Override
	public void setStrings() {
		/*
		 * Example... buttonNew.setText(Internationalization.getString("general_new"));
		 */
		if (mode == StoryEditorPaneController.MODE_IS_EDIT || mode == StoryEditorPaneController.MODE_IS_NEW) {
			buttonCancel.setText("Cancel");
		} else {
			// if setStrings() is called by methode handleAction() it throws an "not on fx application thread currentthread"
			// this is the solution
			// Platform.runLater(() -> buttonCancel.setText("Close"));

			// buttonCancel.setText("Close");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					buttonCancel.setText(Internationalization.getString("general_close"));
					buttonSave.setText(Internationalization.getString("general_save"));

					labStoryName.setText(Internationalization.getString("app_rp_storyeditor_storyname"));
					labDescription.setText(Internationalization.getString("app_rp_storyeditor_storydescription"));
					labStorytext.setText(Internationalization.getString("app_rp_storyeditor_storytext"));
					labRolePlayOff.setText(Internationalization.getString("app_rp_storyeditor_roleplay_off"));
					labStoryVariante.setText(Internationalization.getString("app_rp_storyeditor_story_variante"));
					labImage.setText(Internationalization.getString("app_rp_storyeditor_story_image"));
					labVoice.setText(Internationalization.getString("app_rp_storyeditor_story_voice"));
					labMovie.setText(Internationalization.getString("app_rp_storyeditor_story_movie"));
					labURL.setText(Internationalization.getString("app_rp_storyeditor_story_url"));

					tabBasic.setText(Internationalization.getString("app_rp_storyeditor_tab_basic1"));
					tabBasic2.setText(Internationalization.getString("app_rp_storyeditor_tab_basic2"));
					tabBasic3.setText(Internationalization.getString("app_rp_storyeditor_tab_character_assignment"));
					tabBasic4.setText(Internationalization.getString("app_rp_storyeditor_tab_path"));
					tabBasic5.setText(Internationalization.getString("app_rp_storyeditor_tab_input"));
					tabBasic6.setText(Internationalization.getString("app_rp_storyeditor_tab_dice"));

					labPathOption1.setText(Internationalization.getString("app_rp_storyeditor_story_path_option1"));
					labPathOption2.setText(Internationalization.getString("app_rp_storyeditor_story_path_option2"));
					labPathOption3.setText(Internationalization.getString("app_rp_storyeditor_story_path_option3"));

					labDataInputText.setText(Internationalization.getString("app_rp_storyeditor_story_datainput_text"));
					labDataInputDataset.setText(Internationalization.getString("app_rp_storyeditor_story_datainput_dataset"));
					//labDataInputStep.setText(Internationalization.getString("app_rp_storyeditor_story_datainput_step"));

					labDiceLabel.setText(Internationalization.getString("app_rp_storyeditor_story_dice_label"));
					labDiceScore.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score"));
					labDiceScoreLess.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score_less"));
					labDiceScoreEqual.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score_equal"));
					labDiceScoreMore.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score_more"));

					labAllCharacters.setText(Internationalization.getString("app_rp_storyeditor_story_character_all"));
					labAssignedChar.setText(Internationalization.getString("app_rp_storyeditor_story_character_assigned"));

				}
			});
		}
	}

	private void resetFields() {

		tfStoryName.clear();
		cbStoryVarianten.getSelectionModel().clearSelection();
		taDescription.clear();
		taStorytext.clear();
		taRolePlayOff.clear();
		tfImage.clear();
		tfVoice.clear();
		tfMovie.clear();
		tfStoryPath1.clear();
		tfStoryPath2.clear();
		tfStoryPath3.clear();
		cbStoryPath1.setValue(null);
		cbStoryPath2.setValue(null);
		cbStoryPath3.setValue(null);
		tfDiceScore.clear();
		cbDiceScoreEqual.setValue(null);
		cbDiceScoreLess.setValue(null);
		cbDiceScoreMore.setValue(null);

		cbNextStep_V1.setValue(null);

		tfFormName.clear();
		tfLabelText1_V3.clear();
		tfLabelText2_V3.clear();
		tfLabelText3_V3.clear();
		tfLabelText4_V3.clear();
		cbNextStep1_V3.setValue(null);
		cbNextStep2_V3.setValue(null);
		cbNextStep3_V3.setValue(null);
		cbNextStep4_V3.setValue(null);
	}

	private void createListeners() {
		editFieldChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				setWarningOn(true);
			}
		};

		editComboBoxChangeListener = new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> ov, Object old_val, Object new_val) {
				setWarningOn(true);
			}
		};
	}

	private void enableListeners(boolean enableListerners) {
		if (enableListerners) {
			tfStoryName.textProperty().addListener(editFieldChangeListener);
			cbStoryVarianten.valueProperty().addListener(editComboBoxChangeListener);
			taDescription.textProperty().addListener(editFieldChangeListener);
			taStorytext.textProperty().addListener(editFieldChangeListener);
			taRolePlayOff.textProperty().addListener(editFieldChangeListener);
			tfImage.textProperty().addListener(editFieldChangeListener);
			tfVoice.textProperty().addListener(editFieldChangeListener);
			tfMovie.textProperty().addListener(editFieldChangeListener);
			tfFormName.textProperty().addListener(editFieldChangeListener);
			// lvAllCharacters.getSelectionModel().selectedItemProperty().addListener(editComboBoxChangeListener);

		} else {
			tfStoryName.textProperty().removeListener(editFieldChangeListener);
			cbStoryVarianten.valueProperty().removeListener(editComboBoxChangeListener);
			taDescription.textProperty().removeListener(editFieldChangeListener);
			taStorytext.textProperty().removeListener(editFieldChangeListener);
			taRolePlayOff.textProperty().removeListener(editFieldChangeListener);
			tfImage.textProperty().removeListener(editFieldChangeListener);
			tfVoice.textProperty().removeListener(editFieldChangeListener);
			tfMovie.textProperty().removeListener(editFieldChangeListener);
			tfFormName.textProperty().removeListener(editFieldChangeListener);
			// lvAllCharacters.getSelectionModel().selectedItemProperty().removeListener(editComboBoxChangeListener);
		}
	}

	/**
	 * 
	 */
	@Override
	public void warningOnAction() {
		buttonSave.setDisable(false);
		buttonCancel.setDisable(false);

		enabledFields(true);
		enableButtons();
		setStrings();
		AbstractC3Pane currentPane = Nexus.getCurrentlyOpenedPane();
		if (currentPane != null) {
			currentPane.setModal(true);
		}
	}

	/**
	 * 
	 */
	@Override
	public void warningOffAction() {
	}
}

/**
 * Creating a pane with a list of several icons for the story treeview All treeItem get icons for the rp-Type or if there is a warning or it is a next step of an other step
 * 
 * @author Undertaker
 *
 */
/*public class IconList extends Pane {

	private ImageView isNextImage;
	private ImageView hasWarningImage;
	private ImageView rpTypeImage;
	private HBox pane;
	private static final Class<IconList> c = IconList.class;

	public IconList(ROLEPLAYENTRYTYPES rpTyp, boolean isNext, boolean hasWarning) {
		try {

			pane = new HBox(5.0);

			if (isNext) {
				// this.isNextImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPNextStepIcon.png"))));
				this.isNextImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPNextStepIcon.png").getFile()))));
				pane.getChildren().add(this.isNextImage);
			}

			if (hasWarning) {
				// this.hasWarningImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPWarningIcon.png"))));
				this.hasWarningImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPWarningIcon.png").getFile()))));
				pane.getChildren().add(this.hasWarningImage);
			}

			if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1) {
				// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPEditIcon.png"))));
				this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPEditIcon.png").getFile()))));
				pane.getChildren().add(this.rpTypeImage);

			} else if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V2) {
				// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPTreeIcon.png"))));
				this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPTreeIcon.png").getFile()))));
				pane.getChildren().add(this.rpTypeImage);

			} else if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V3) {
				// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPInputIcon.png"))));
				this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPInputIcon.png").getFile()))));
				pane.getChildren().add(this.rpTypeImage);

			} else if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V4) {
				// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPDiceIcon.png"))));
				this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPDiceIcon.png").getFile()))));
				pane.getChildren().add(this.rpTypeImage);

			}

			this.getChildren().add(pane);
		} catch (FileNotFoundException ex) {
			C3Logger.info("Iconlist Image nicht gefunden");
		}
	}
}*/
