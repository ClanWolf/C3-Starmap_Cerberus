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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes.ROLEPLAYINPUTDATATYPES;
import net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes.ROLEPLAYOBJECTTYPES;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * FXML Controller class
 *
 * @author Undertaker
 */
public class StoryEditorPaneController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	//------------------- Dialog -------------------
	@FXML
	Button buttonSave;
	@FXML
	Button buttonCancel;

	//------------------- Treeview -------------------
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
	private TreeView<RolePlayStoryDTO> treeStory;
	@FXML
	TabPane tabPaneStory;
	@FXML
	private Tab tabBasic, tabBasic2, tabBasic3, tabBasic4, tabBasic5, tabBasic6, tabBasic7, tabBasic8, tabBasic9;
	@FXML
	private ComboBox<RolePlayStoryDTO> cbStorySelection;
	//------------------- Char assignment -------------------
	@FXML
	Button btAddChar, btRemoveChar;
	@FXML
	private ListView<RolePlayCharacterDTO> lvAllCharacters, lvAssignedChar;
	@FXML
	Label labAssignedChar, labAllCharacters;

	//------------------- Basic rpg -------------------
	@FXML
	Button btSelectImageFile, btSelectVoiceFile, btSelectMovieFile;
	@FXML
	Button btDeleteImageFile, btDeleteVoiceFile, btDeleteMovieFile;
	@FXML
	Button btSortOrderUp, btSortOrderDown;
	@FXML
	TextField tfStoryName, tfImage, tfVoice, tfMovie, tfXPosText, tfYPosText, tfHeightText, tfWidthText;
	@FXML
	Label labStoryName, labDescription, labStorytext, labRolePlayOff, labStoryVariante, labImage, labVoice, labMovie;
	@FXML
	ComboBox<ROLEPLAYENTRYTYPES> cbStoryVarianten;
	@FXML
	TextArea taDescription, taStorytext, taRolePlayOff;
	@FXML
	private Label labImageAction, labVoiceAction, labMovieAction;
	@FXML
	private TextField tfSortOrder;

	//------------------- Path of the story -------------------
	@FXML
	ComboBox<RolePlayStoryDTO> cbStoryPath1, cbStoryPath2, cbStoryPath3, cbStoryPath4;
	@FXML
	TextField tfStoryPath1, tfStoryPath2, tfStoryPath3, tfStoryPath4;
	@FXML
	Label labPathOption1, labPathOption2, labPathOption3,labPathOption4;
	@FXML
	Button btDeleteStoryOption1, btDeleteStoryOption2, btDeleteStoryOption3, btDeleteStoryOption4;

	@FXML
	TextField tfAttackerDropVictories, tfDefenderDropVictories;

	//------------------- Special form -------------------
	@FXML
	ComboBox<ROLEPLAYINPUTDATATYPES> cbDatafield1, cbDatafield2, cbDatafield3, cbDatafield4, cbDatafield5;
	@FXML
	ComboBox<ROLEPLAYOBJECTTYPES> cbroleplayinputdatatypes;
	@FXML
	ComboBox<RolePlayStoryDTO> cbNextStep_V3;
	@FXML
	Label labDataInputDataset;

	//------------------- Dice -------------------
	@FXML
	Label labDiceLabel, labDiceScore, labDiceScoreLess, labDiceScoreEqual, labDiceScoreMore;
	@FXML
	ComboBox<RolePlayStoryDTO> cbDiceScoreLess, cbDiceScoreEqual, cbDiceScoreMore;
	@FXML
	TextField tfDiceScore;

	//------------------- Next Step -------------------
	@FXML
	ComboBox<RolePlayStoryDTO> cbNextStep_V1;
	@FXML
	Button btDeleteStoryOptionNextStep;
	@FXML
	TextField tfButtonText;
	@FXML
	CheckBox cbAttackerWins, cbDefenderWins;

	//------------------- Keypad -------------------
	@FXML
	Label labCode,labAttempt,labAttemptSuccsess,labAttemptFailure;
	@FXML
	ComboBox<RolePlayStoryDTO> cbNextStep_AttemptSuccess, cbNextStep_AttemptFailure;
	@FXML
	TextField tfCode, tfAttempt;

	//---------------------- HPG Message ----------------------
	@FXML
	private ComboBox<RolePlayStoryDTO> cbNextStepV7;
	@FXML
	private TextField tfSender,tfSenddate,tfHeader,tfServiceName;
	@FXML
	private ComboBox<BOFaction> cbFaction;

	//------------------- private fields -------------------

	private TreeItem<RolePlayStoryDTO> root;
	private TreeItem<RolePlayStoryDTO> selected;
	private BORolePlayStory boRP;

	private ChangeListener<? super String> editFieldChangeListener;
	private ChangeListener<? super Object> editComboBoxChangeListener;

	private boolean doUploadImage, doUploadSound, doUploadMovie;
	private boolean doDeleteImage, doDeleteSound, doDeleteMovie;

	private String lastImagePath;
	private String lastSoundFilePath;
	private String lastVideoFilePath;

	private int mode = 0;
	private static final int MODE_IS_INIT = -1;
	private static final int MODE_IS_DEFAULT = 0;
	private static final int MODE_IS_NEW = 1;
	private static final int MODE_IS_EDIT = 2;

	//------------------- Methoden ------------------
	//@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);

		// Register actions from BORolePlayStory
		boRP.registerActions(this);
	}

	/**
	 * Initializes the controller class
	 */

	public void initialize() {

		boRP = new BORolePlayStory();
		addActionCallBackListeners();

		// Register actions from BORolePlayStory
		//boRP.registerActions(this);
		mode = StoryEditorPaneController.MODE_IS_INIT;

		initTreeView();
		initCombobox();

		tfDiceScore.textProperty().addListener(new ChangeListener<>() {
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

		//-------------------------------------------
		if (true) {

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
			tabPaneStory.getTabs().remove(tabBasic8);
			tabPaneStory.getTabs().remove(tabBasic9);
			//-------------------------------------
		}
	}

	@Override
	public boolean handleAction(ACTIONS action, ActionObject object) {
		switch (action) {
			case SAVE_ROLEPLAY_STORY_OK:
				logger.info("SAVE_ROLEPLAY_STORY_OK -> " + this.toString());

				GameState gs = (GameState) object.getObject();

				RolePlayStoryDTO rps = (RolePlayStoryDTO) gs.getObject();
				ArrayList<RolePlayCharacterDTO> rpcList = (ArrayList<RolePlayCharacterDTO>) gs.getObject2();


				if(boRP.getStoryByID(rps.getId()) == null && rps.getVariante() == ROLEPLAYENTRYTYPES.RP_STORY) {
					cbStorySelection.getItems().add(rps);
					cbStorySelection.getSelectionModel().select(rps);
					boRP.getStoryList().add(rps);
				}

				// Only need after an insert. It is not necessary after an update
				// If the RolePlayStoryDTO is new, we need this to get the object with ID from the database.
				selected.setValue(rps);
				boRP.addStoryToList(selected.getValue());
				boRP.changeCharacterList(rpcList);

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
				logger.info("SAVE_ROLEPLAY_STORY_ERR");

				Platform.runLater(() -> {

					Alert alert = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("general_failure"), Internationalization.getString("general_failure"), Internationalization.getString("app_rp_storyeditor_story_error_save"));
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						//setWarningOff();
					}
				});

				break;
			case DELETE_ROLEPLAY_STORY_OK:
				boRP.removeStoryFromList(selected.getValue());
				selected.getParent().getChildren().remove(selected);

				enableButtons();
				break;
			case DELETE_ROLEPLAY_STORY_ERR:
				logger.info("DELETE_ROLEPLAY_STORY_ERR");
				Platform.runLater(() -> {

					Alert alert = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("general_failure"), Internationalization.getString("general_failure"), Internationalization.getString("app_rp_storyeditor_story_error_delete"));
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						//setWarningOff();
					}
				});

				break;
			case GET_ROLEPLAY_ALLCHARACTER:
				Platform.runLater(() -> {
					logger.info("GET_ROLEPLAY_ALLCHARACTER");

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
				logger.info("GET_ROLEPLAY_STORYANDCHAPTER");

				@SuppressWarnings("unchecked")
				ArrayList<RolePlayStoryDTO> hlpLst = (ArrayList<RolePlayStoryDTO>) object.getObject();
				boRP.setMainStories(hlpLst);
				cbStorySelection.getItems().setAll(boRP.getMainStories());

				mode = StoryEditorPaneController.MODE_IS_DEFAULT;
				enableButtons();
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("13");
				break;
			case GET_ROLEPLAY_STEPSBYSTORY:
				ArrayList<RolePlayStoryDTO> gs2 = (ArrayList<RolePlayStoryDTO>) object.getObject();
				gs2.add((RolePlayStoryDTO)cbStorySelection.getValue());
				boRP.setStoryList( gs2);

				Platform.runLater(() -> {
					//ArrayList<RolePlayStoryDTO> liRP = boRP.getStoryList();
					ArrayList<RolePlayStoryDTO> liRP = new ArrayList<RolePlayStoryDTO>();
					liRP.add((RolePlayStoryDTO)cbStorySelection.getValue());
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
				});
				break;
			default:
				break;
		}
		return true;
	}

	/* ----------------- FXML begin ----------------- */

	//------------------- Dialog ----------------------------
	@FXML
	private void handleOnAction_StorySelection(){
		boRP.getAllStepsByStory((RolePlayStoryDTO) cbStorySelection.getValue());
	}

	//------------------- Treeview --------------------------
	//------------------- Char assignment -------------------
	//------------------- Basic rpg -------------------------
	//------------------- Path of the story -----------------
	//------------------- Special form ----------------------
	//------------------- Dice ------------------------------
	//------------------- Next Step -------------------------
	//------------------- Keypad ----------------------------
	//------------------- HPG Message -----------------------

	@FXML
	private void handleNewStoryButtonClick() {
		logger.info("handleNewButtonClick");
		TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(boRP.addNewStory());
		root.getChildren().add(rpTreeItem);

		treeStory.getSelectionModel().select(rpTreeItem);
		selected = rpTreeItem;

		mode = StoryEditorPaneController.MODE_IS_NEW;
		warningOnAction (true);
		//setWarningOn(true);
		setData();

	}

	@FXML
	private void handleNewChapterButtonClick() {
		logger.info("handleNewChapterButtonClick");

		// Can be only added if parent is a story
		if (treeStory.getTreeItemLevel(selected) == 1) {
			TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(boRP.addNewChapter(selected.getValue()));
			selected.getChildren().add(rpTreeItem);
			selected.setExpanded(true);

			treeStory.getSelectionModel().select(rpTreeItem);
			selected = rpTreeItem;

			mode = StoryEditorPaneController.MODE_IS_NEW;
			warningOnAction (true);
			//setWarningOn(true);
			setData();

		}
		enableButtons();
	}

	@FXML
	private void handleNewStoryStepButtonClick() {
		logger.info("handleNewStoryStepButtonClick");
		// Can be only added if parent is a chapter
		// , or it is a step. In this case add it on the same level
		if (treeStory.getTreeItemLevel(selected) == 2) {

			TreeItem<RolePlayStoryDTO> rpTreeItem = new TreeItem<>(boRP.addNewStoryStep(selected.getValue()));
			selected.getChildren().add(rpTreeItem);
			selected.setExpanded(true);

			treeStory.getSelectionModel().select(rpTreeItem);
			selected = rpTreeItem;

			mode = StoryEditorPaneController.MODE_IS_NEW;
			warningOnAction (true);
			//setWarningOn(true);
			fillComboboxWithStories();
			setData();

		} else if (treeStory.getTreeItemLevel(selected) == 3) {

			TreeItem<RolePlayStoryDTO> rpTreeItem2 = new TreeItem<>(boRP.addNewStoryStep(selected.getParent().getValue()));
			selected.getParent().getChildren().add(rpTreeItem2);
			selected.setExpanded(true);

			treeStory.getSelectionModel().select(rpTreeItem2);
			selected = rpTreeItem2;

			mode = StoryEditorPaneController.MODE_IS_NEW;
			warningOnAction (true);
			//setWarningOn(true);
			setData();

		}
		enableButtons();
	}

	@FXML
	private void handleDeleteSeletedTreeItem() {
		logger.info("handleDeleteSeletedTreeItem");

		Alert alert = Tools.C3Dialog(AlertType.CONFIRMATION, Internationalization.getString("app_rp_storyeditor_story_delete_question_label"), Internationalization.getString("app_rp_storyeditor_story_delete_question_label"),
				Internationalization.getString("app_rp_storyeditor_story_delete_question_text"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {

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
		logger.info("handleSelectTreeItem");

		String currentSelectedTab = null;
		if( tabPaneStory.getSelectionModel().getSelectedItem() != null) {
			currentSelectedTab = tabPaneStory.getSelectionModel().getSelectedItem().getText();

		}
		logger.info("currentSelectedTab: " + currentSelectedTab);
		if (mode == StoryEditorPaneController.MODE_IS_DEFAULT) {


			selected = treeStory.getSelectionModel().getSelectedItem();
			logger.info(treeStory.getTreeItemLevel(selected) + "");

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

		handleTabs();

		boolean foundTab = false;
		for (Tab t : tabPaneStory.getTabs()) {
			if(t.getText().equals(currentSelectedTab)){
				tabPaneStory.getSelectionModel().select(t);
				foundTab = true;
				break;
			}
		}

		if(!foundTab) {
			tabPaneStory.getSelectionModel().select(0);
		}

		//handleTabs();
	}

	@FXML
	private void handleEditStoryButtonClick() {
		logger.info("handleEditStoryButtonClick");
		logger.info("Message -> Kommunikationskanal wird bereitgestellt... please hold the line :-)");
		tabPaneStory.getSelectionModel().select(0);
		mode = StoryEditorPaneController.MODE_IS_EDIT;
		warningOnAction (true);
		//setWarningOn(true);
	}

	@FXML
	private void handleSaveButtonClick() {

		// RolePlayStoryDTO copy = boRP.getCopy(selected.getValue());
		RolePlayStoryDTO rpCopyForSaving;
		rpCopyForSaving = boRP.getCopy(selected.getValue());

		// if (boRP.checkBeforeSave(getData(boRP.getCopy(copy)))) {
		if (boRP.checkBeforeSave(getData(rpCopyForSaving))) {
			//setWarningOff();
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
		if (result.isPresent() && result.get() == ButtonType.OK) {
			enabledFields(false);

			if (mode == StoryEditorPaneController.MODE_IS_NEW) {
				TreeItem<RolePlayStoryDTO> toDelete = selected;
				selected.getParent().getChildren().remove(toDelete);

				resetFields();
				warningOnAction(false);
				//setWarningOff();
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
				warningOnAction(false);
				//setWarningOff();
				buttonSave.setDisable(true);
				mode = StoryEditorPaneController.MODE_IS_DEFAULT;
				enableButtons();
				setStrings();

			} else {
				//ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
				if (buttonSave.getScene() != null) {
					Stage stage = (Stage) buttonSave.getScene().getWindow();
					stage.close();
				}
			}
		} else {
			logger.info("Do Nothing");
		}
	}

	@FXML
	private void handleSelectImageFile() {
		File file = callFileChooser(lastImagePath, "Bilddatei (*.png)", "*.png");

		if (file != null) {
			logger.info("File ausgewählt");
			tfImage.setText(file.getAbsolutePath());
			doUploadImage = true;
			doDeleteImage = false;
			lastImagePath = file.getParent();

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

		logger.info("handleImageOnKeyTyped");
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
		logger.info("handleVoiceOnKeyTyped");
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
		logger.info("handleMovieKeyTyped");
	}

	@FXML
	private void handleSelectVoiceFile() {
		File file = callFileChooser(lastSoundFilePath, "Bilddatei (*.mp3)", "*.mp3");

		if (file != null) {
			logger.info("File ausgewählt");
			tfVoice.setText(file.getAbsolutePath());
			doUploadSound = true;
			doDeleteSound = false;
			lastSoundFilePath = file.getParent();
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
		File file = callFileChooser(lastVideoFilePath,"Bilddatei (*.mp4)", "*.mp4");

		if (file != null) {
			logger.info("File ausgewählt");
			tfMovie.setText(file.getAbsolutePath());
			doUploadMovie = true;
			doDeleteMovie = false;

			lastVideoFilePath = file.getParent();
		}
	}

	@FXML
	private void handleSelectionTabCharAssignment() {
		logger.info("handleSelectionTabCharAssignment");
		if (tabBasic3.isSelected()) {
			logger.info("handleSelectionTabCharAssignment -> selected");
		}
	}

	@FXML
	private void handleAddChar() {
		if (lvAllCharacters.getSelectionModel().getSelectedItem() != null) {
			lvAssignedChar.getItems().add(lvAllCharacters.getSelectionModel().getSelectedItem());
			lvAllCharacters.getItems().remove(lvAllCharacters.getSelectionModel().getSelectedItem());
			warningOnAction (true);
			//setWarningOn(true);
		}
	}

	@FXML
	private void handleRemoveChar() {
		if (lvAssignedChar.getSelectionModel().getSelectedItem() != null) {
			lvAllCharacters.getItems().add(lvAssignedChar.getSelectionModel().getSelectedItem());
			lvAssignedChar.getItems().remove(lvAssignedChar.getSelectionModel().getSelectedItem());
			warningOnAction (true);
			//setWarningOn(true);
		}
	}

	@FXML
	private void handleSortOrderUp() {
		logger.info("handleSortOrderUp");
		int sortOrder = Integer.parseInt(tfSortOrder.getText());
		if (sortOrder < selected.getParent().getChildren().size()) {
			sortOrder++;
			tfSortOrder.setText(Integer.toString(sortOrder));

			TreeItem<RolePlayStoryDTO> parent = selected.getParent();

			int index = parent.getChildren().indexOf(selected);
			parent.getChildren().remove(index);
			parent.getChildren().add(sortOrder - 1, selected);

			treeStory.getSelectionModel().select(selected);

		}
	}

	@FXML
	private void handleSortOrderDown() {
		logger.info("handleSortOrderDown");
		int sortOrder = Integer.parseInt(tfSortOrder.getText());
		if (sortOrder > 1) {
			sortOrder--;
			tfSortOrder.setText(Integer.toString(sortOrder));

			TreeItem<RolePlayStoryDTO> parent = selected.getParent();

			int index = parent.getChildren().indexOf(selected);
			parent.getChildren().remove(index);
			parent.getChildren().add(sortOrder - 1, selected);

			treeStory.getSelectionModel().select(selected);

		}

	}

	@FXML
	private void handleCbStoryVariantenAction() {
		logger.info("handleCbStoryVariantenAction");
		handleTabs();

	}

	@FXML
	private void handleDeleteStoryOption1() {
		tfStoryPath1.setText(tfStoryPath2.getText());
		cbStoryPath1.getSelectionModel().select(cbStoryPath2.getValue());

		tfStoryPath2.setText(tfStoryPath3.getText());
		cbStoryPath2.getSelectionModel().select(cbStoryPath3.getValue());

		tfStoryPath3.setText(tfStoryPath4.getText());
		cbStoryPath3.getSelectionModel().select(cbStoryPath4.getValue());


		tfStoryPath4.clear();
		cbStoryPath4.setValue(null);
	}

	@FXML
	private void handleDeleteStoryOption2() {
		tfStoryPath2.setText(tfStoryPath3.getText());
		cbStoryPath2.getSelectionModel().select(cbStoryPath3.getValue());

		tfStoryPath3.setText(tfStoryPath4.getText());
		cbStoryPath3.getSelectionModel().select(cbStoryPath4.getValue());

		tfStoryPath4.clear();
		cbStoryPath4.setValue(null);
	}

	@FXML
	private void handleDeleteStoryOption3() {
		tfStoryPath3.setText(tfStoryPath4.getText());
		cbStoryPath3.getSelectionModel().select(cbStoryPath4.getValue());

		tfStoryPath4.clear();
		cbStoryPath4.setValue(null);

	}

	@FXML
	private void handleDeleteStoryOption4() {
		tfStoryPath4.clear();
		cbStoryPath4.setValue(null);

	}

	@FXML
	private void handleDeleteStoryOptionNextStep() {
		cbNextStep_V1.getSelectionModel().select(cbNextStep_V1.getValue());

		tfStoryPath3.clear();
		cbNextStep_V1.setValue(null);
	}

	@FXML
	private void handleDiceScoreTextChanged() {
		logger.info("handleDiceScoreTextChanged");
	}

	@FXML
	private void handleActionInputDataTypes(){

		fillComboBoxWithDataInputTypes();

	}

	/* ----------------- FXML end ----------------- */

	/*private File callFileChooser(String filterName, String filter) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterName, filter);
		chooser.getExtensionFilters().add(extFilter);

		return chooser.showOpenDialog(treeStory.getScene().getWindow());
	}*/

	private File callFileChooser(String dir, String filterName, String filter) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterName, filter);
		chooser.getExtensionFilters().add(extFilter);
		if(dir != null) {
			chooser.setInitialDirectory(new File(dir));
		}

		return chooser.showOpenDialog(treeStory.getScene().getWindow());
	}

	private void handleTabs() {

		tabPaneStory.getTabs().remove(tabBasic2);
		tabPaneStory.getTabs().remove(tabBasic3);
		tabPaneStory.getTabs().remove(tabBasic4);
		tabPaneStory.getTabs().remove(tabBasic5);
		tabPaneStory.getTabs().remove(tabBasic6);
		tabPaneStory.getTabs().remove(tabBasic7);
		tabPaneStory.getTabs().remove(tabBasic8);
		tabPaneStory.getTabs().remove(tabBasic9);

		// Tab for character assignment
		if (selected != null && selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_STORY) {
			if (!tabPaneStory.getTabs().contains(tabBasic3)) {
				tabPaneStory.getTabs().add(tabBasic3);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic3)) {
				tabPaneStory.getTabs().remove(tabBasic3);
			}
		}

		if (selected != null && selected.getValue().getVariante() != ROLEPLAYENTRYTYPES.RP_STORY &&
				selected.getValue().getVariante() != ROLEPLAYENTRYTYPES.RP_CHAPTER &&
				selected.getValue().getVariante() != ROLEPLAYENTRYTYPES.RP_PREPARE_BATTLE) {
			if (!tabPaneStory.getTabs().contains(tabBasic2)) {
				tabPaneStory.getTabs().add(tabBasic2);
			}
		}

		// Tab for simple step
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_SECTION ||
				cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_PREPARE_BATTLE) {
			if (!tabPaneStory.getTabs().contains(tabBasic7)) {
				tabPaneStory.getTabs().add(tabBasic7);
			}
		} else if (tabPaneStory.getTabs().contains(tabBasic7)) {
			tabPaneStory.getTabs().remove(tabBasic7);
		}

		// Tab for story path selection
		if (selected != null && ( cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_CHOICE ||
				cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_CHOICE_IMAGE_LEFT ||
				cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_INVASION)) {
			if (!tabPaneStory.getTabs().contains(tabBasic4)) {
				tabPaneStory.getTabs().add(tabBasic4);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic4)) {
				tabPaneStory.getTabs().remove(tabBasic4);
			}
		}

		// Tab for datainput
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_DATA_INPUT) {
			if (!tabPaneStory.getTabs().contains(tabBasic5)) {
				tabPaneStory.getTabs().add(tabBasic5);
			}

			fillComboBoxWithDataInputTypes();
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic5)) {
				tabPaneStory.getTabs().remove(tabBasic5);
			}
		}

		// Tab for keypad
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_DICE) {
			if (!tabPaneStory.getTabs().contains(tabBasic6)) {
				tabPaneStory.getTabs().add(tabBasic6);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic6)) {
				tabPaneStory.getTabs().remove(tabBasic6);
			}
		}

		// Tab for keypad
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_KEYPAD) {
			if (!tabPaneStory.getTabs().contains(tabBasic8)) {
				tabPaneStory.getTabs().add(tabBasic8);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic8)) {
				tabPaneStory.getTabs().remove(tabBasic8);
			}
		}

		// Tab for HPG-Message
		if (selected != null && cbStoryVarianten.getSelectionModel().getSelectedItem() == ROLEPLAYENTRYTYPES.RP_HPG_MESSAGE) {
			if (!tabPaneStory.getTabs().contains(tabBasic9)) {
				tabPaneStory.getTabs().add(tabBasic9);
			}
		} else {
			if (tabPaneStory.getTabs().contains(tabBasic9)) {
				tabPaneStory.getTabs().remove(tabBasic9);
			}
		}
	}

	/**
	 * Initalisation of treeview TreeStory
	 */
	private void initTreeView() {

		// storyRoot will never be stored in database
		RolePlayStoryDTO storyRoot = new RolePlayStoryDTO();
		storyRoot.setStoryName("All storys");

		root = new TreeItem<>(storyRoot);
		root.setExpanded(true);

		treeStory.setShowRoot(false);
		treeStory.setRoot(root);

		treeStory.setCellFactory(new Callback<>() {
			@Override
			public TreeCell<RolePlayStoryDTO> call(TreeView<RolePlayStoryDTO> p) {
				return new TreeCell<>() {

					@Override
					protected void updateItem(RolePlayStoryDTO item, boolean empty) {
						super.updateItem(item, empty);
						boolean hasWarning = false;
						boolean isNextStep = false;

						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item.getSortOrder().toString() + " " + item.getStoryName());
							logger.info("setCellFactory: updateItem: " + item.getStoryName());

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
						logger.info("setCellFactory: updateItem: ");
					}
				};
			}
		});

	}

	/**
	 * Initalisation of combobox cbStoryVarianten
	 */
	private void initCombobox() {
		cbStorySelection.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());

		cbNextStep_V1.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());

		cbStoryPath1.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());
		cbStoryPath2.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());
		cbStoryPath3.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());
		cbStoryPath4.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());

		// Variante 3
		cbroleplayinputdatatypes.setCellFactory(rpDataInputListView -> new RPCellFactoryDataInput<>());
		cbroleplayinputdatatypes.getSelectionModel().select(ROLEPLAYOBJECTTYPES.CHARACTER);
		handleActionInputDataTypes();

		cbDatafield1.setCellFactory(rpDataInputListView -> new RPCellFactoryDataInput<>());
		cbDatafield2.setCellFactory(rpDataInputListView -> new RPCellFactoryDataInput<>());
		cbDatafield3.setCellFactory(rpDataInputListView -> new RPCellFactoryDataInput<>());
		cbDatafield4.setCellFactory(rpDataInputListView -> new RPCellFactoryDataInput<>());
		cbDatafield5.setCellFactory(rpDataInputListView -> new RPCellFactoryDataInput<>());

		cbroleplayinputdatatypes.setConverter(new RPObjectTypesStringConverter());
		cbDatafield1.setConverter(new RPInputDataTypesStringConverter());
		cbDatafield2.setConverter(new RPInputDataTypesStringConverter());
		cbDatafield3.setConverter(new RPInputDataTypesStringConverter());
		cbDatafield4.setConverter(new RPInputDataTypesStringConverter());
		cbDatafield5.setConverter(new RPInputDataTypesStringConverter());

		// Variante 7
		cbNextStepV7.setCellFactory(rolePlayStoryDTOListView -> new RPCellFactory<RolePlayStoryDTO>());
		cbFaction.setCellFactory(boFactionListView -> new RPCellFactoryFaction<BOFaction>());
	}

	private void fillComboBoxWithDataInputTypes(){
		logger.info("fillComboBoxWithDataInputTypes");
		ArrayList<ROLEPLAYINPUTDATATYPES> dataList = new ArrayList<>();

		for(ROLEPLAYINPUTDATATYPES t : ROLEPLAYINPUTDATATYPES.values()) {
			if(t.types == cbroleplayinputdatatypes.getSelectionModel().getSelectedItem()){
				dataList.add(t);
			}
		}

		cbDatafield1.getItems().setAll(dataList);
		cbDatafield2.getItems().setAll(dataList);
		cbDatafield3.getItems().setAll(dataList);
		cbDatafield4.getItems().setAll(dataList);
		cbDatafield5.getItems().setAll(dataList);

		if (selected != null && selected.getValue() != null && selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_DATA_INPUT && selected.getValue().getVar3ID() != null) {
			cbDatafield1.getSelectionModel().select(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet1()));
			cbDatafield2.getSelectionModel().select(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet2()));
			cbDatafield3.getSelectionModel().select(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet3()));
			cbDatafield4.getSelectionModel().select(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet4()));
			cbDatafield5.getSelectionModel().select(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet5()));
		}
	}

	private void fillComboboxWithStories() {
		logger.info("fillComboboxWithStories");
		if (selected != null) {
			cbStoryPath1.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbStoryPath2.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbStoryPath3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbStoryPath4.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbDiceScoreEqual.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbDiceScoreLess.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbDiceScoreMore.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbNextStep_V1.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbNextStep_AttemptSuccess.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbNextStep_AttemptFailure.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbStoryVarianten.getItems().setAll(ROLEPLAYENTRYTYPES.values());

			cbroleplayinputdatatypes.getItems().setAll(ROLEPLAYOBJECTTYPES.values());

			cbDatafield1.getItems().setAll(ROLEPLAYINPUTDATATYPES.values());
			cbDatafield2.getItems().setAll(ROLEPLAYINPUTDATATYPES.values());
			cbDatafield3.getItems().setAll(ROLEPLAYINPUTDATATYPES.values());
			cbDatafield4.getItems().setAll(ROLEPLAYINPUTDATATYPES.values());
			cbDatafield5.getItems().setAll(ROLEPLAYINPUTDATATYPES.values());

			cbNextStep_V3.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));

			cbNextStepV7.getItems().setAll(boRP.getStoriesFromChapter(selected.getValue().getParentStory()));
			cbFaction.getItems().setAll(Nexus.getBoUniverse().getFactionList());
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

			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_STORY || selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_CHAPTER) {
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
			tfStoryPath4.setDisable(false);
			cbStoryPath1.setDisable(false);
			cbStoryPath2.setDisable(false);
			cbStoryPath3.setDisable(false);
			cbStoryPath4.setDisable(false);
			btDeleteStoryOption1.setDisable(false);
			btDeleteStoryOption2.setDisable(false);
			btDeleteStoryOption3.setDisable(false);

			tfAttackerDropVictories.setDisable(false);
			tfDefenderDropVictories.setDisable(false);

			cbroleplayinputdatatypes.setDisable(false);
			cbDatafield1.setDisable(false);
			cbDatafield2.setDisable(false);
			cbDatafield3.setDisable(false);
			cbDatafield4.setDisable(false);
			cbDatafield5.setDisable(false);

			cbNextStep_V3.setDisable(false);

			tfDiceScore.setDisable(false);
			cbDiceScoreEqual.setDisable(false);
			cbDiceScoreLess.setDisable(false);
			cbDiceScoreMore.setDisable(false);

			cbNextStep_V1.setDisable(false);

			cbNextStep_AttemptSuccess.setDisable(false);
			cbNextStep_AttemptFailure.setDisable(false);

			btDeleteStoryOptionNextStep.setDisable(false);

			tfXPosText.setDisable(false);
			tfYPosText.setDisable(false);
			tfWidthText.setDisable(false);
			tfHeightText.setDisable(false);

			cbNextStepV7.setDisable(false);;
			tfSender.setDisable(false);
			tfSenddate.setDisable(false);
			tfHeader.setDisable(false);
			tfServiceName.setDisable(false);
			cbFaction.setDisable(false);

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

			btSortOrderUp.setDisable(true);
			btSortOrderDown.setDisable(true);
			tfSortOrder.setDisable(true);

			tfStoryPath1.setDisable(true);
			tfStoryPath2.setDisable(true);
			tfStoryPath3.setDisable(true);
			tfStoryPath4.setDisable(true);
			cbStoryPath1.setDisable(true);
			cbStoryPath2.setDisable(true);
			cbStoryPath3.setDisable(true);
			cbStoryPath4.setDisable(true);
			btDeleteStoryOption1.setDisable(true);
			btDeleteStoryOption2.setDisable(true);
			btDeleteStoryOption3.setDisable(true);

			tfAttackerDropVictories.setDisable(true);
			tfDefenderDropVictories.setDisable(true);

			cbroleplayinputdatatypes.setDisable(true);
			cbDatafield1.setDisable(true);
			cbDatafield2.setDisable(true);
			cbDatafield3.setDisable(true);
			cbDatafield4.setDisable(true);
			cbDatafield5.setDisable(true);

			cbNextStep_V3.setDisable(true);

			tfDiceScore.setDisable(true);
			cbDiceScoreEqual.setDisable(true);
			cbDiceScoreLess.setDisable(true);
			cbDiceScoreMore.setDisable(true);

			cbNextStep_V1.setDisable(true);

			cbNextStep_AttemptSuccess.setDisable(true);
			cbNextStep_AttemptFailure.setDisable(true);

			btDeleteStoryOptionNextStep.setDisable(true);

			tfXPosText.setDisable(true);
			tfYPosText.setDisable(true);
			tfWidthText.setDisable(true);
			tfHeightText.setDisable(true);

			cbNextStepV7.setDisable(true);;
			tfSender.setDisable(true);
			tfSenddate.setDisable(true);
			tfHeader.setDisable(true);
			tfServiceName.setDisable(true);
			cbFaction.setDisable(true);

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

			if (selected.getValue().getId() != null) {
				tfSortOrder.setText(selected.getValue().getSortOrder().toString());

			} else {
				int newSortOrder = selected.getParent().getChildren().indexOf(selected) + 1;
				selected.getValue().setSortOrder(newSortOrder);
				tfSortOrder.setText(String.valueOf(newSortOrder));
			}


			lvAssignedChar.getItems().clear();
			for (RolePlayCharacterDTO rpc : boRP.getCharacterList()) {
				if (rpc.getStory() != null && selected.getValue() != null && rpc.getStory().getStory() != null && rpc.getStory().getStory().getId().equals(selected.getValue().getId())) {
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
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_SECTION ||
					selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_PREPARE_BATTLE) {
				if(selected.getValue().getNextStepID() != null){
					cbNextStep_V1.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getNextStepID()));
				}

				if(selected.getValue().getxPosText() != null){
					tfXPosText.setText(selected.getValue().getxPosText().toString());
				} else {
					tfXPosText.clear();
				}

				if(selected.getValue().getyPosText() != null){
					tfYPosText.setText(selected.getValue().getyPosText().toString());
				} else {
					tfYPosText.clear();
				}

				if(selected.getValue().getHeightText() != null){
					tfHeightText.setText(selected.getValue().getHeightText().toString());
				} else {
					tfHeightText.clear();
				}

				if(selected.getValue().getWidthText() != null){
					tfWidthText.setText(selected.getValue().getWidthText().toString());
				} else {
					tfWidthText.clear();
				}

				if(selected.getValue().getButtonText() != null){
					tfButtonText.setText(selected.getValue().getButtonText());
				} else {
					tfButtonText.clear();
				}

				cbAttackerWins.setSelected(selected.getValue().getAttackerWins() != null && selected.getValue().getAttackerWins());
				cbDefenderWins.setSelected(selected.getValue().getDefenderWins() != null && selected.getValue().getDefenderWins());

			} else {
				cbNextStep_V1.setValue(null);

				tfXPosText.clear();
				tfYPosText.clear();
				tfHeightText.clear();
				tfWidthText.clear();
			}

			// set data for story variante 2 / variante 5 / variante 9
			if ((selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_CHOICE ||
					selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_CHOICE_IMAGE_LEFT) && selected.getValue().getVar2ID() != null) {
				tfStoryPath1.setText(selected.getValue().getVar2ID().getOption1Text());
				tfStoryPath2.setText(selected.getValue().getVar2ID().getOption2Text());
				tfStoryPath3.setText(selected.getValue().getVar2ID().getOption3Text());
				tfStoryPath4.setText(selected.getValue().getVar2ID().getOption4Text());

				cbStoryPath1.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption1StoryID()));
				cbStoryPath2.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption2StoryID()));
				cbStoryPath3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption3StoryID()));
				cbStoryPath4.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar2ID().getOption4StoryID()));

			} else if ((selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_INVASION) && selected.getValue().getVar9ID() != null) {
				tfStoryPath1.setText(selected.getValue().getVar9ID().getOption1Text());
				tfStoryPath2.setText(selected.getValue().getVar9ID().getOption2Text());
				tfStoryPath3.setText(selected.getValue().getVar9ID().getOption3Text());
				tfStoryPath4.setText(selected.getValue().getVar9ID().getOption4Text());

				cbStoryPath1.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar9ID().getOption1StoryID()));
				cbStoryPath2.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar9ID().getOption2StoryID()));
				cbStoryPath3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar9ID().getOption3StoryID()));
				cbStoryPath4.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar9ID().getOption4StoryID()));

				tfAttackerDropVictories.setText(selected.getValue().getVar9ID().getAttackerDropVictories().toString());
				tfDefenderDropVictories.setText(selected.getValue().getVar9ID().getDefenderDropVictories().toString());

				} else {
					cbStoryPath1.setValue(null);
					cbStoryPath2.setValue(null);
					cbStoryPath3.setValue(null);
					cbStoryPath4.setValue(null);
					tfStoryPath1.clear();
					tfStoryPath2.clear();
					tfStoryPath3.clear();
					tfStoryPath4.clear();


					tfAttackerDropVictories.clear();
					tfDefenderDropVictories.clear();
				}

			// set data for story variante 3
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_DATA_INPUT && selected.getValue().getVar3ID() != null) {
				logger.info("setData");

				if (selected.getValue().getVar3ID().getDataSet1() != null) {
					cbroleplayinputdatatypes.getSelectionModel().select(Objects.requireNonNull(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet1())).types);
				} else if (selected.getValue().getVar3ID().getDataSet2() != null) {
					cbroleplayinputdatatypes.getSelectionModel().select(Objects.requireNonNull(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet2())).types);
				} else if (selected.getValue().getVar3ID().getDataSet3() != null) {
					cbroleplayinputdatatypes.getSelectionModel().select(Objects.requireNonNull(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet3())).types);
				} else if (selected.getValue().getVar3ID().getDataSet4() != null) {
					cbroleplayinputdatatypes.getSelectionModel().select(Objects.requireNonNull(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet4())).types);
				} else if (selected.getValue().getVar3ID().getDataSet5() != null) {
					cbroleplayinputdatatypes.getSelectionModel().select(Objects.requireNonNull(ROLEPLAYINPUTDATATYPES.getEnumForName(selected.getValue().getVar3ID().getDataSet5())).types);
				}

				cbNextStep_V3.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar3ID().getNextStoryID()));
			} else {
				cbDatafield1.setValue(null);
				cbDatafield2.setValue(null);
				cbDatafield3.setValue(null);
				cbDatafield4.setValue(null);
				cbDatafield5.setValue(null);

				cbNextStep_V3.setValue(null);
			}

			// set data for story variante 4
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_DICE && selected.getValue().getVar4ID() != null) {

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

			// set data for story variante 6
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_KEYPAD && selected.getValue().getVar6ID() != null) {
				tfCode.setText(selected.getValue().getVar6ID().getSecretCode());
				tfAttempt.setText(selected.getValue().getVar6ID().getAttempts().toString());
				cbNextStep_AttemptSuccess.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar6ID().getStoryIDSuccess()));
				cbNextStep_AttemptFailure.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar6ID().getStoryIDFailure()));
			} else {
				tfCode.clear();
				tfAttempt.clear();
				cbNextStep_AttemptSuccess.setValue(null);
				cbNextStep_AttemptFailure.setValue(null);
			}

			// set data for story variante 7
			if (selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_HPG_MESSAGE && selected.getValue().getVar7ID() != null) {
				cbNextStepV7.getSelectionModel().select(boRP.getStoryByID(selected.getValue().getVar7ID().getNextStepID()));
				tfSender.setText(selected.getValue().getVar7ID().getSender());
				tfSenddate.setText(selected.getValue().getVar7ID().getDate());
				tfHeader.setText(selected.getValue().getVar7ID().getHeader());
				tfServiceName.setText(selected.getValue().getVar7ID().getServiceName());
				cbFaction.setValue(Nexus.getBoUniverse().getFactionByID(selected.getValue().getVar7ID().getFaction()));

			} else {
				tfSender.clear();
				tfSenddate.clear();
				tfHeader.clear();
				tfServiceName.clear();
				cbNextStepV7.setValue(null);
				cbFaction.setValue(null);
			}


		}
	}

	private RolePlayStoryDTO getData (RolePlayStoryDTO rp){
		rp.setStoryName(tfStoryName.getText());
		rp.setVariante(cbStoryVarianten.getValue());
		rp.setStoryDescription(taDescription.getText());
		rp.setStoryText(taStorytext.getText());
		rp.setRolePlayOff(taRolePlayOff.getText());
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
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_STORY) {
			ArrayList<Long> newCharIds = new ArrayList<>();
			// find all new added chars
			for (RolePlayCharacterDTO rpc : lvAssignedChar.getItems()) {
				if (rpc.getStory() == null) {
					logger.info("Add new char to Story: " + rpc.getName());
					newCharIds.add(rpc.getId());
				} else {
					logger.info("do nothing: " + rpc.getName());
				}
			}
			rp.setNewCharIDs(newCharIds);


			ArrayList<Long> removedCharIds = new ArrayList<>();
			// find all chars to remove from story
			for (RolePlayCharacterDTO rpc : lvAllCharacters.getItems()) {
				if (rpc.getStory() != null) {
					logger.info("Remove from story: " + rpc.getName());
					removedCharIds.add(rpc.getId());
				} else {
					logger.info("do nothing: " + rpc.getName());
				}
			}
			rp.setRemovedCharIDs(removedCharIds);
		}

		// set data for variante 1
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_SECTION ||
				selected.getValue().getVariante() == ROLEPLAYENTRYTYPES.RP_PREPARE_BATTLE) {


			if (cbNextStep_V1.getValue() != null) {
				rp.setNextStepID(cbNextStep_V1.getValue().getId());
			} else {
				rp.setNextStepID(null);
			}

			if(tfXPosText.getText() != null && !tfXPosText.getText().equals("")){
				rp.setxPosText(Integer.valueOf(tfXPosText.getText()));
			} else {
				rp.setxPosText(null);
			}

			if(tfYPosText.getText() != null && !tfYPosText.getText().equals("")){
				rp.setyPosText(Integer.valueOf(tfYPosText.getText()));
			} else {
				rp.setyPosText(null);
			}

			if(tfHeightText.getText() != null && !tfHeightText.getText().equals("")){
				rp.setHeightText(Integer.valueOf(tfHeightText.getText()));
			} else {
				rp.setHeightText(null);
			}

			if(tfWidthText.getText() != null && !tfWidthText.getText().equals("")){
				rp.setWidthText(Integer.valueOf(tfWidthText.getText()));
			} else {
				rp.setWidthText(null);
			}

			if(tfButtonText.getText() != null && !tfButtonText.getText().equals("")){
				rp.setButtonText(tfButtonText.getText());
			} else {
				rp.setButtonText("weiter");
			}

			rp.setAttackerWins(cbAttackerWins.isSelected());
			rp.setDefenderWins(cbDefenderWins.isSelected());
		}

		// set data for variante 2
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_CHOICE || rp.getVariante() == ROLEPLAYENTRYTYPES.RP_CHOICE_IMAGE_LEFT) {

			RolePlayStoryChoiceDTO rpVar2 = rp.getVar2ID();
			if (rpVar2 == null) {
				rpVar2 = new RolePlayStoryChoiceDTO();
				rpVar2.setStory(rp.getId());
			}

			if (cbStoryPath1.getValue() != null) {
				rpVar2.setOption1StoryID(cbStoryPath1.getValue().getId());
			} else {
				rpVar2.setOption1StoryID(null);
			}
			if (cbStoryPath2.getValue() != null) {
				rpVar2.setOption2StoryID(cbStoryPath2.getValue().getId());
			} else {
				rpVar2.setOption2StoryID(null);
			}
			if (cbStoryPath3.getValue() != null) {
				rpVar2.setOption3StoryID(cbStoryPath3.getValue().getId());
			} else {
				rpVar2.setOption3StoryID(null);
			}
			if (cbStoryPath4.getValue() != null) {
				rpVar2.setOption4StoryID(cbStoryPath4.getValue().getId());
			} else {
				rpVar2.setOption4StoryID(null);
			}

			rpVar2.setOption1Text(tfStoryPath1.getText());
			rpVar2.setOption2Text(tfStoryPath2.getText());
			rpVar2.setOption3Text(tfStoryPath3.getText());
			rpVar2.setOption4Text(tfStoryPath4.getText());

			rp.setVar2ID(rpVar2);

		} else {
			rp.setVar2ID(null);
		}

		// set data for variante 3
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_DATA_INPUT) {

			RolePlayStoryDatainputDTO rpVar3 = rp.getVar3ID();
			if (rpVar3 == null) {
				rpVar3 = new RolePlayStoryDatainputDTO();
				rpVar3.setStory(rp.getId());
			}

			if (cbDatafield1.getValue() != null) {
				rpVar3.setDataSet1(cbDatafield1.getValue().name());
			}
			if (cbDatafield2.getValue() != null) {
				rpVar3.setDataSet2(cbDatafield2.getValue().name());
			}
			if (cbDatafield3.getValue() != null) {
				rpVar3.setDataSet3(cbDatafield3.getValue().name());
			}
			if (cbDatafield4.getValue() != null) {
				rpVar3.setDataSet4(cbDatafield4.getValue().name());
			}
			if (cbDatafield5.getValue() != null) {
				rpVar3.setDataSet5(cbDatafield5.getValue().name());
			}
			if (cbNextStep_V3.getValue() != null) {
				rpVar3.setNextStoryID(cbNextStep_V3.getValue().getId());
			}

			rp.setVar3ID(rpVar3);

		} else {
			rp.setVar3ID(null);
		}

		// set data for variante 4
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_DICE) {

			RolePlayStoryDiceDTO rpVar4 = rp.getVar4ID();
			if (rpVar4 == null) {
				rpVar4 = new RolePlayStoryDiceDTO();
				rpVar4.setStory(rp.getId());
			}

			if (tfDiceScore.getText().isEmpty()) {
				rpVar4.setScore(null);
			} else {
				rpVar4.setScore(Integer.valueOf(tfDiceScore.getText()));
			}
			if(cbDiceScoreEqual.getValue() != null) {
				rpVar4.setStoryIDScoreEqual(cbDiceScoreEqual.getValue().getId());
			}
			if(cbDiceScoreLess.getValue() != null) {
				rpVar4.setStoryIDScoreLess(cbDiceScoreLess.getValue().getId());
			}
			if(cbDiceScoreMore.getValue() != null) {
				rpVar4.setStoryIDScoreMore(cbDiceScoreMore.getValue().getId());
			}

			rp.setVar4ID(rpVar4);

		} else {
			rp.setVar4ID(null);
		}

		// set data for variante 6
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_KEYPAD) {
			RolePlayStoryKeypadDTO rpVar6 = rp.getVar6ID();
			if (rpVar6 == null) {
				rpVar6 = new RolePlayStoryKeypadDTO();
				rpVar6.setStory(rp.getId());
			}
			rpVar6.setSecretCode(tfCode.getText());
			rpVar6.setAttempts(Integer.valueOf(tfAttempt.getText()));

			if(cbNextStep_AttemptSuccess.getValue() != null) {
				rpVar6.setStoryIDSuccess(cbNextStep_AttemptSuccess.getValue().getId());
			} else {
				rpVar6.setStoryIDSuccess(null);
			}

			if(cbNextStep_AttemptFailure.getValue() != null) {
				rpVar6.setStoryIDFailure(cbNextStep_AttemptFailure.getValue().getId());
			} else {
				rpVar6.setStoryIDFailure(null);
			}

			rp.setVar6ID(rpVar6);
		} else {
			rp.setVar6ID(null);
		}

		// set data for variante 7
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_HPG_MESSAGE) {
			RolePlayStoryHPGMessageDTO rpVar7 = rp.getVar7ID();
			if (rpVar7 == null) {
				rpVar7 = new RolePlayStoryHPGMessageDTO();
				rpVar7.setStory(rp.getId());
			}

			rpVar7.setSender(tfSender.getText());
			rpVar7.setDate(tfSenddate.getText());
			rpVar7.setHeader(tfHeader.getText());
			rpVar7.setServiceName(tfServiceName.getText());

			if(cbNextStepV7.getValue() != null) {
				rpVar7.setNextStepID(cbNextStepV7.getValue().getId());
			} else {
				rpVar7.setNextStepID(null);
			}

			if(cbFaction.getValue() != null) {
				rpVar7.setFaction(cbFaction.getValue().getID());
			} else {
				rpVar7.setFaction(null);
			}

			rp.setVar7ID(rpVar7);
		} else {
			rp.setVar7ID(null);
		}

		// set data for variante 9
		if (rp.getVariante() == ROLEPLAYENTRYTYPES.RP_INVASION) {

			RolePlayStoryInvasionDTO rpVar9 = rp.getVar9ID();
			if (rpVar9 == null) {
				rpVar9 = new RolePlayStoryInvasionDTO();
				rpVar9.setStory(rp.getId());
			}

			if (cbStoryPath1.getValue() != null) {
				rpVar9.setOption1StoryID(cbStoryPath1.getValue().getId());
			} else {
				rpVar9.setOption1StoryID(null);
			}
			if (cbStoryPath2.getValue() != null) {
				rpVar9.setOption2StoryID(cbStoryPath2.getValue().getId());
			} else {
				rpVar9.setOption2StoryID(null);
			}
			if (cbStoryPath3.getValue() != null) {
				rpVar9.setOption3StoryID(cbStoryPath3.getValue().getId());
			} else {
				rpVar9.setOption3StoryID(null);
			}
			if (cbStoryPath4.getValue() != null) {
				rpVar9.setOption4StoryID(cbStoryPath4.getValue().getId());
			} else {
				rpVar9.setOption4StoryID(null);
			}

			rpVar9.setOption1Text(tfStoryPath1.getText());
			rpVar9.setOption2Text(tfStoryPath2.getText());
			rpVar9.setOption3Text(tfStoryPath3.getText());
			rpVar9.setOption4Text(tfStoryPath4.getText());

			rpVar9.setAttackerDropVictories(Integer.parseInt(tfAttackerDropVictories.getText()));
			rpVar9.setDefenderDropVictories(Integer.parseInt(tfDefenderDropVictories.getText()));

			rp.setVar9ID(rpVar9);

		} else {
			rp.setVar9ID(null);
		}

		return rp;
	}

	private void fileTransfer () {
		boolean bOK = false;
		try {
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

			if (!bOK) logger.info("Error while upload!");
		} catch(Exception e) {
			e.printStackTrace();
			logger.info("Exception during ftp upload!");
		}
	}

	/**
	 *
	 */
	public void setStrings () {
		/*
		 * Example... buttonNew.setText(Internationalization.getString("general_new"));
		 */
		if (mode == StoryEditorPaneController.MODE_IS_EDIT || mode == StoryEditorPaneController.MODE_IS_NEW) {
			buttonCancel.setText("Cancel");
		} else {
			Platform.runLater(() -> {
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

				tabBasic.setText(Internationalization.getString("app_rp_storyeditor_tab_basic1"));
				tabBasic2.setText(Internationalization.getString("app_rp_storyeditor_tab_basic2"));
				tabBasic3.setText(Internationalization.getString("app_rp_storyeditor_tab_character_assignment"));
				tabBasic4.setText(Internationalization.getString("app_rp_storyeditor_tab_path"));
				tabBasic5.setText(Internationalization.getString("app_rp_storyeditor_tab_input"));
				tabBasic6.setText(Internationalization.getString("app_rp_storyeditor_tab_dice"));

				labPathOption1.setText(Internationalization.getString("app_rp_storyeditor_story_path_option1"));
				labPathOption2.setText(Internationalization.getString("app_rp_storyeditor_story_path_option2"));
				labPathOption3.setText(Internationalization.getString("app_rp_storyeditor_story_path_option3"));
				labPathOption4.setText(Internationalization.getString("app_rp_storyeditor_story_path_option4"));

//					labDataInputText.setText(Internationalization.getString("app_rp_storyeditor_story_datainput_text"));
				labDataInputDataset.setText(Internationalization.getString("app_rp_storyeditor_story_datainput_dataset"));
				//labDataInputStep.setText(Internationalization.getString("app_rp_storyeditor_story_datainput_step"));

				labDiceLabel.setText(Internationalization.getString("app_rp_storyeditor_story_dice_label"));
				labDiceScore.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score"));
				labDiceScoreLess.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score_less"));
				labDiceScoreEqual.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score_equal"));
				labDiceScoreMore.setText(Internationalization.getString("app_rp_storyeditor_story_dice_score_more"));

				labAllCharacters.setText(Internationalization.getString("app_rp_storyeditor_story_character_all"));
				labAssignedChar.setText(Internationalization.getString("app_rp_storyeditor_story_character_assigned"));

			});
		}
	}

	private void resetFields () {

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
		tfStoryPath4.clear();
		cbStoryPath1.setValue(null);
		cbStoryPath2.setValue(null);
		cbStoryPath3.setValue(null);
		cbStoryPath4.setValue(null);
		tfAttackerDropVictories.clear();
		tfDefenderDropVictories.clear();
		tfDiceScore.clear();
		cbDiceScoreEqual.setValue(null);
		cbDiceScoreLess.setValue(null);
		cbDiceScoreMore.setValue(null);

		cbNextStep_V1.setValue(null);

		cbNextStep_AttemptSuccess.setValue(null);
		cbNextStep_AttemptFailure.setValue(null);
		tfCode.clear();
		tfAttempt.clear();

		tfXPosText.clear();
		tfYPosText.clear();
		tfHeightText.clear();
		tfWidthText.clear();

		cbDatafield1.setValue(null);
		cbDatafield2.setValue(null);
		cbDatafield3.setValue(null);
		cbDatafield4.setValue(null);
		cbDatafield5.setValue(null);

		cbNextStepV7.setValue(null);
		tfSender.clear();
		tfSenddate.clear();
		tfHeader.clear();
		tfServiceName.clear();
		cbFaction.setValue(null);
	}

	private void createListeners () {
		editFieldChangeListener = new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				warningOnAction (true);
				//setWarningOn(true);
			}
		};

		editComboBoxChangeListener = new ChangeListener<>() {
			@Override
		public void changed(ObservableValue<?> ov, Object old_val, Object new_val) {
				warningOnAction (true);
				//setWarningOn(true);
			}
		};
	}

	private void enableListeners ( boolean enableListerners){
		if (enableListerners) {
			tfStoryName.textProperty().addListener(editFieldChangeListener);
			cbStoryVarianten.valueProperty().addListener(editComboBoxChangeListener);
			taDescription.textProperty().addListener(editFieldChangeListener);
			taStorytext.textProperty().addListener(editFieldChangeListener);
			taRolePlayOff.textProperty().addListener(editFieldChangeListener);
			tfImage.textProperty().addListener(editFieldChangeListener);
			tfVoice.textProperty().addListener(editFieldChangeListener);
			tfMovie.textProperty().addListener(editFieldChangeListener);
			cbroleplayinputdatatypes.valueProperty().addListener(editComboBoxChangeListener);
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
			cbroleplayinputdatatypes.valueProperty().removeListener(editComboBoxChangeListener);
			// lvAllCharacters.getSelectionModel().selectedItemProperty().removeListener(editComboBoxChangeListener);
		}
	}

	/**
	 *
	 */
	public void warningOnAction (boolean enable) {
		buttonSave.setDisable(false);
		buttonCancel.setDisable(false);

		enabledFields(enable);
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
	public void warningOffAction () {
	}

	//------------------- Inner classes -------------------

	private final class RPCellFactory<RolePlayStoryDTO> extends ListCell<net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO> {

		@Override
		protected void updateItem(net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
			} else {
				setText(item.getStoryName() + " (" + item.getSortOrder().toString() + ")");
			}
		}
	}

	private final class RPCellFactoryFaction<BOFaction> extends ListCell<net.clanwolf.starmap.client.process.universe.BOFaction> {

		@Override
		protected void updateItem(net.clanwolf.starmap.client.process.universe.BOFaction item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
			} else {
				setText(item.getName());
			}
		}
	}

	private final class RPCellFactoryDataInput<ROLEPLAYINPUTDATATYPES> extends ListCell<ROLEPLAYINPUTDATATYPES> {

		@Override
		protected void updateItem(ROLEPLAYINPUTDATATYPES item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
			} else {
				setText(Internationalization.getString(item.toString()));
			}
		}
	}

	private final class RPInputDataTypesStringConverter extends StringConverter<ROLEPLAYINPUTDATATYPES> {

		@Override
		public String toString(ROLEPLAYINPUTDATATYPES object) {
			if(object != null){
				return Internationalization.getString(object.toString());
			}
			return null;
		}

		@Override
		public ROLEPLAYINPUTDATATYPES fromString(String string) {
			return null;
		}
	}

	private final class RPObjectTypesStringConverter extends StringConverter<ROLEPLAYOBJECTTYPES> {

		@Override
		public String toString(ROLEPLAYOBJECTTYPES object) {
			if(object != null){
				return Internationalization.getString(object.toString());
			}
			return null;
		}

		@Override
		public ROLEPLAYOBJECTTYPES fromString(String string) {
			return null;
		}
	}

}
