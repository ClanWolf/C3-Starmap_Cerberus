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
package net.clanwolf.starmap.client.process.roleplay;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.net.IFileTransfer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Undertaker
 *
 */
public class BORolePlayStory {

	// Contains all selected stories
	private ArrayList<RolePlayStoryDTO> allStories;
	// Contains error text after an action (checkBeforeSave, checkBeforeDelete,...
	private String errorText;

	private ArrayList<RolePlayCharacterDTO> allCharacters;

	// Constants
	public static final String URL_RPG_BASIC = "https://c3.clanwolf.net/rpg";
	public static final String URL_RPG_RESOURSES = "https://c3.clanwolf.net/rpg/resources";

	public BORolePlayStory() {
		allStories = new ArrayList<>();
	}

	/**
	 * Register needed Actions to a ActionCallBackListener
	 * 
	 * @param callBackListener ActionCallBackListener
	 */
	public void registerActions(ActionCallBackListener callBackListener) {
		ActionManager.addActionCallbackListener(ACTIONS.SAVE_ROLEPLAY_STORY_OK, callBackListener);
		ActionManager.addActionCallbackListener(ACTIONS.SAVE_ROLEPLAY_STORY_ERR, callBackListener);
		ActionManager.addActionCallbackListener(ACTIONS.GET_ROLEPLAY_ALLSTORIES, callBackListener);
		ActionManager.addActionCallbackListener(ACTIONS.DELETE_ROLEPLAY_STORY_OK, callBackListener);
		ActionManager.addActionCallbackListener(ACTIONS.DELETE_ROLEPLAY_STORY_ERR, callBackListener);
		ActionManager.addActionCallbackListener(ACTIONS.GET_ROLEPLAY_ALLCHARACTER, callBackListener);
	}

	/**
	 * Add a new story
	 * 
	 * @return RolePlayStoryDTO
	 */
	public RolePlayStoryDTO addNewStory() {
		RolePlayStoryDTO story = new RolePlayStoryDTO();
		story.setStoryName("New story");
		story.setVariante(ROLEPLAYENTRYTYPES.C3_RP_STORY);
		story.setAuthor(Nexus.getCurrentUser().getUserId());

		// allStories.add(story);

		return story;
	}

	/**
	 * Add a new chapter for a given story
	 * 
	 * @param parentStory RolePlayStoryDTO
	 * @return RolePlayStoryDTO
	 */
	public RolePlayStoryDTO addNewChapter(RolePlayStoryDTO parentStory) {
		RolePlayStoryDTO chapter = new RolePlayStoryDTO();
		chapter.setStoryName("New chapter");
		chapter.setParentStory(parentStory);
		chapter.setStory(parentStory);
		chapter.setVariante(ROLEPLAYENTRYTYPES.C3_RP_CHAPTER);
		chapter.setAuthor(Nexus.getCurrentUser().getUserId());

		// allStories.add(chapter);

		return chapter;
	}

	/**
	 * Add a new step for a given chapter
	 * 
	 * @param parentChapter RolePlayStoryDTO
	 * @return RolePlayStoryDTO
	 */
	public RolePlayStoryDTO addNewStoryStep(RolePlayStoryDTO parentChapter) {
		RolePlayStoryDTO story = new RolePlayStoryDTO();
		story.setStoryName("New step");
		story.setParentStory(parentChapter);
		story.setStory(parentChapter.getStory());
		story.setVariante(ROLEPLAYENTRYTYPES.C3_RP_STEP_V1);
		story.setAuthor(Nexus.getCurrentUser().getUserId());

		// allStories.add(story);

		return story;
	}

	/**
	 * Add a new part of the story to the internal list with all selected stories from database
	 * 
	 * @param story RolePlayStoryDTO
	 */
	public void addStoryToList(RolePlayStoryDTO story) {

		for (RolePlayStoryDTO rp : allStories) {
			C3Logger.info("Story: " + rp.getStoryName() + " ChangedStory: " + story.getStoryName());

			//if exist an object with the same id, we remove it first
			if (rp.getId().equals(story.getId())) {
				allStories.remove(rp);
				break;
			}
		}

		allStories.add(story);
	}

	/**
	 *
	 * @param story RolePlayStoryDTO
	 */
	public void removeStoryFromList(RolePlayStoryDTO story) {
		allStories.remove(story);
	}

	/**
	 * Set a list with selected stories
	 * 
	 * @param li ArrayList<RolePlayCharacterDTO>
	 */
	public void setStoryList(ArrayList<RolePlayStoryDTO> li) {
		allStories = li;
	}

	public RolePlayStoryDTO getStoryByID(Long id){
		for(RolePlayStoryDTO rp: allStories) {
			if (rp.getId().equals(id)) {
				C3Logger.info(String.valueOf(rp.getId()));
				return rp;
			}
		}
		return null;
	}

	/**
	 * Set a list with characters
	 * 
	 * @param li ArrayList<RolePlayCharacterDTO>
	 */
	public void setCharacterList(ArrayList<RolePlayCharacterDTO> li) {
		allCharacters = li;
	}

	public void chsngeCharacterList(ArrayList<RolePlayCharacterDTO> li){

		for (RolePlayCharacterDTO rpc : li) {

			for (RolePlayCharacterDTO rpcAll : allCharacters){
				if(rpc.getId().equals(rpcAll.getId())){
					int idx = allCharacters.indexOf(rpcAll);
					allCharacters.remove(idx);
					allCharacters.add(idx,rpc);
					break;
				}
			}
		}
	}

	/*
	 * Get a list with all characters
	 *
	 */
	public ArrayList<RolePlayCharacterDTO> getCharacterList() {
		return allCharacters;
	}

	/**
	 * Get a list with stories from a special chapter
	 * 
	 * @param chapter RolePlayStoryDTO
	 * @return ArrayList<RolePlayStoryDTO>
	 */
	public ArrayList<RolePlayStoryDTO> getStoriesFromChapter(RolePlayStoryDTO chapter) {

		ArrayList<RolePlayStoryDTO> stories = new ArrayList<>();

		if (chapter != null && chapter.getVariante() != ROLEPLAYENTRYTYPES.C3_RP_STORY && chapter.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER) {

			for (RolePlayStoryDTO rp : allStories) {
				if (rp.getParentStory() != null && rp.getVariante() != ROLEPLAYENTRYTYPES.C3_RP_CHAPTER && rp.getVariante() != ROLEPLAYENTRYTYPES.C3_RP_STORY && rp.getParentStory().getId().equals(chapter.getId())) {
					stories.add(rp);
				}
			}
		}

		return stories;

	}

	/**
	 * Return a clone of a RolePlayStoryDTO
	 * 
	 * @param original RolePlayStoryDTO
	 * @return RolePlayStoryDTO
	 */
	public RolePlayStoryDTO getCopy(RolePlayStoryDTO original) {
		RolePlayStoryDTO copy = new RolePlayStoryDTO();

		copy.setId(original.getId());
		copy.setParentStory(original.getParentStory());
		copy.setStory(original.getStory());
		copy.setStoryDescription(original.getStoryDescription());
		copy.setStoryImage(original.getStoryImage());
		copy.setStoryIntro(original.getStoryIntro());
		copy.setStoryMP3(original.getStoryMP3());
		copy.setStoryName(original.getStoryName());
		copy.setStoryText(original.getStoryText());
		copy.setVariante(original.getVariante());
		copy.setAuthor(original.getAuthor());
		copy.setRolePlayOff(original.getRolePlayOff());
		//copy.setCharacters(original.getCharacters());
		copy.setNewImageWithPath(original.getNewImageWithPath());
		copy.setNewVoiceWithPath(original.getNewVoiceWithPath());
		copy.setNewMovieWithPath(original.getNewMovieWithPath());
		copy.setSortOrder(original.getSortOrder());
		copy.setVar2ID(original.getVar2ID());
		copy.setVar3ID(original.getVar3ID());
		copy.setVar4ID(original.getVar4ID());
		copy.setNextStepID(original.getNextStepID());

		return copy;
	}

	/**
	 *
	 * @param rp RolePlayStoryDTO
	 * @return ArrayList<RolePlayStoryDTO>
	 */
	private ArrayList<RolePlayStoryDTO> findAndSetChangedSortOrder(RolePlayStoryDTO rp) {

		Iterator<RolePlayStoryDTO> iterTest = allStories.iterator();
		ArrayList<RolePlayStoryDTO> test = new ArrayList<>();

		ArrayList<RolePlayStoryDTO> changedSortOrder = new ArrayList<>();

		while (iterTest.hasNext()) {
			RolePlayStoryDTO r = iterTest.next();
			if (r.getParentStory() != null && rp.getParentStory() != null && r.getParentStory().getId().longValue() == rp.getParentStory().getId().longValue()) {
				test.add(r);
			}
		}

		// Iterator<RolePlayStoryDTO> iter = allStories.iterator();
		Iterator<RolePlayStoryDTO> iter = test.iterator();

		int minSortOrder;
		int maxSortOrder;
		int index;

		if (rp.getSortOrder() >= rp.getSortOrderOld()) {
			minSortOrder = rp.getSortOrderOld();
			maxSortOrder = rp.getSortOrder();
			index = -1;
		} else {
			minSortOrder = rp.getSortOrder();
			maxSortOrder = rp.getSortOrderOld();
			index = 1;
		}

		while (iter.hasNext()) {

			RolePlayStoryDTO rpIter = iter.next();
			C3Logger.info("Story: " + rpIter.getStoryName() + " SortOrder: " + rpIter.getSortOrder());

			if (rpIter.getParentStory().getId().longValue() == rp.getParentStory().getId().longValue() && rpIter.getId() != rp.getId() && rpIter.getSortOrder() >= minSortOrder && rpIter.getSortOrder() <= maxSortOrder) {
				// change sortorder
				int old = rpIter.getSortOrder();
				int t = rpIter.getSortOrder() + index;
				C3Logger.info("Move: " + rpIter.getStoryName() + " / old Order: " + String.valueOf(old) + " -> new Order: " + String.valueOf(t));

				rpIter.setSortOrder(rpIter.getSortOrder() + index);
				changedSortOrder.add(rpIter);
			}
		}
		return changedSortOrder;

	}

	/**
	 *
	 * @param image String
	 * @return boolean
	 */
	private boolean checkImage(String image) {

		if (image == null || image.isEmpty())
			return true;

		Path path = Paths.get(image);

		return Files.exists(path) && path.toString().toLowerCase().endsWith(".png");

	}

	/**
	 *
	 * @param voice String
	 * @return boolean
	 */
	private boolean checkSound(String voice) {
		if (voice == null || voice.isEmpty())
			return true;

		Path path = Paths.get(voice);

		return Files.exists(path) && path.toString().toLowerCase().endsWith(".mp3");

	}

	/**
	 *
	 * @param movie String
	 * @return boolean
	 */
	private boolean checkVideo(String movie) {
		if (movie == null || movie.isEmpty())
			return true;

		Path path = Paths.get(movie);

		return Files.exists(path) && path.toString().toLowerCase().endsWith(".mp4");

	}

	/**
	 * Check consistence of the object before save it true -> OK / false -> not ok
	 * 
	 * @param rpStory RolePlayStoryDTO
	 * @return boolean
	 */
	public boolean checkBeforeSave(RolePlayStoryDTO rpStory) {
		boolean ret = false;
		
		if (rpStory.getStoryName() == null || rpStory.getStoryName().isEmpty()) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message1"));

		} else if (rpStory.getStoryText() == null || rpStory.getStoryText().isEmpty()) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message2"));

		} else if (rpStory.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER && rpStory.getParentStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message3"));

		} else if (rpStory.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY && rpStory.getParentStory() != null && rpStory.getParentStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message3"));

		} else if (rpStory.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY && rpStory.getParentStory() != null && rpStory.getParentStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message3"));

		} else if (!checkImage(rpStory.getNewImageWithPath())) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message4"));

		} else if (!checkSound(rpStory.getNewVoiceWithPath())) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message5"));

		} else if (!checkVideo(rpStory.getNewMovieWithPath())) {
			setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message6"));

		} else {
			ret = true;
		}

		return ret;
	}

	/**
	 * Check the object before delete it true -> ok / false -> not ok
	 * 
	 * @param story RolePlayStoryDTO
	 * @return boolean
	 */
	public boolean checkBeforeDelete(RolePlayStoryDTO story) {

		//TODO: DELETE STORY: delete all sub entries from chapter
		//TODO: DELETE STORY: delete complete story with all sub entries
		//TODO: DELETE STORY: check if the story used by another step
		boolean ret = true;

		for (RolePlayStoryDTO allStory : allStories) {
			if (story == allStory.getParentStory()) {
				ret = false;
				setErrorText(Internationalization.getString("app_rp_storyeditor_story_check_message7"));
				break;
			}
		}

		return ret;
	}

	/**
	 * Return all RolePlayStoryDTO with type ROLEPLAYENTRYTYPES.C3_RP_STORY
	 * 
	 * @return ArrayList<RolePlayStoryDTO>
	 */
	public ArrayList<RolePlayStoryDTO> getStoriesFromList() {
		ArrayList<RolePlayStoryDTO> lsStories = new ArrayList<>();

		for (RolePlayStoryDTO rps : allStories) {
			if (rps.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {
				lsStories.add(rps);
			}
		}
		return lsStories;
	}

	/**
	 * Return all RolePlayStoryDTO where the parent node is the parameter
	 * 
	 * @param rpStory RolePlayStoryDTO
	 * @return ArrayList<RolePlayStoryDTO>
	 */
	public ArrayList<RolePlayStoryDTO> getChildsFromStory(RolePlayStoryDTO rpStory) {
		ArrayList<RolePlayStoryDTO> lsStories = new ArrayList<>();

		for (RolePlayStoryDTO rps : allStories) {
			//			if (rps.getParentStory() != null && rps.getParentStory().getId().equals(rpStory.getId())) {
			if (rps.getParentStory() != null && isSameRolePlayStore(rps.getParentStory(), rpStory)) {
				lsStories.add(rps);
			}
		}
		return lsStories;
	}

	/**
	 * Set the error text
	 * 
	 * @param txt String
	 */
	private void setErrorText(String txt) {
		errorText = txt;
	}

	/**
	 * Returns the error text
	 * 
	 * @return String
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * Checks the next step of the active step
	 *
	 * @param activeStory RolePlayStoryDTO
	 * @param nextStep RolePlayStoryDTO
	 * @return boolean
	 */
	public boolean isNextStep(RolePlayStoryDTO activeStory, RolePlayStoryDTO nextStep) {

		boolean ret = false;

		// Check next step for ROLEPLAYENTRYTYPES C3_RP_STEP_V1
		if (activeStory != null && activeStory.getNextStepID() != null) {
			if(activeStory.getNextStepID() != null && activeStory.getNextStepID().equals(nextStep.getId())){
				ret = true;
			}
		}

		// Check next step for ROLEPLAYENTRYTYPES C3_RP_STEP_V2
		if (activeStory != null && activeStory.getVar2ID() != null) {

			if ((activeStory.getVar2ID().getOption1StoryID() != null && activeStory.getVar2ID().getOption1StoryID().equals(nextStep.getId()))
					|| (activeStory.getVar2ID().getOption2StoryID() != null && activeStory.getVar2ID().getOption2StoryID().equals(nextStep.getId()))
					|| (activeStory.getVar2ID().getOption3StoryID() != null && activeStory.getVar2ID().getOption3StoryID().equals(nextStep.getId()))) {
				ret = true;
			}
		}

		// Check next step for ROLEPLAYENTRYTYPE C3_RP_STEP_V3
		if (activeStory != null && activeStory.getVar3ID() != null) {
			if (activeStory.getVar3ID().getNextStoryID() != null && activeStory.getVar3ID().getNextStoryID().equals(nextStep.getId())
					|| activeStory.getVar3ID().getNextStory2ID() != null && activeStory.getVar3ID().getNextStory2ID().equals(nextStep.getId())
					|| activeStory.getVar3ID().getNextStory3ID() != null && activeStory.getVar3ID().getNextStory3ID().equals(nextStep.getId())
					||activeStory.getVar3ID().getNextStory4ID() != null && activeStory.getVar3ID().getNextStory4ID().equals(nextStep.getId())) {
				ret = true;
			}
		}

		// Check next step for ROLEPLAYENTRYTYPE C3_RP_STEP_V4
		if (activeStory != null && activeStory.getVar4ID() != null) {

			if ((activeStory.getVar4ID().getStoryIDScoreEqual() != null && activeStory.getVar4ID().getStoryIDScoreEqual().equals(nextStep.getId()))
					|| (activeStory.getVar4ID().getStoryIDScoreLess() != null && activeStory.getVar4ID().getStoryIDScoreLess().equals(nextStep.getId()))
					|| (activeStory.getVar4ID().getStoryIDScoreMore() != null && activeStory.getVar4ID().getStoryIDScoreMore().equals(nextStep.getId()))) {
				ret = true;
			}
		}

		return ret;

	}

	/**
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean
	 */
	public boolean hasNoNextStepEntry(RolePlayStoryDTO story) {
		
		boolean nextStepFound = false;

		if(story != null && story.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 &&
				story.getNextStepID() == null){
			nextStepFound = true;

		} else if(story != null  && story.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V2
				&& story.getVar2ID().getOption1StoryID() == null ) {
			nextStepFound = true;
		
		} else if(story != null  && story.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V3
				&& story.getVar3ID().getNextStoryID() == null ) {
			nextStepFound = true;
		
		} else if(story != null  && story.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V4
				&& ( story.getVar4ID().getStoryIDScoreEqual() == null 
						|| story.getVar4ID().getStoryIDScoreLess() == null
						|| story.getVar4ID().getStoryIDScoreMore() == null)) {
			nextStepFound = true;
		}
		
		return nextStepFound;
	}

	/*--************** Send networkevents **************--*/

	/**
	 * Save methode for a story/chapter/step
	 * 
	 * @param story RolePlayStoryDTO
	 */
	public void save(RolePlayStoryDTO story) {
		GameState state = new GameState(GAMESTATEMODES.ROLEPLAY_SAVE_STORY);
		state.addObject(story);

		state.addObject2(this.findAndSetChangedSortOrder(story));

		Nexus.fireNetworkEvent(state);
	}

	/**
	 * Delete methode for a story/chapter/step
	 * 
	 * @param story RolePlayStoryDTO
	 */
	public void delete(RolePlayStoryDTO story) {
		GameState state = new GameState(GAMESTATEMODES.ROLEPLAY_DELETE_STORY);
		state.addObject(story);

		state.addObject2(this.findAndSetChangedSortOrder(story));

		Nexus.fireNetworkEvent(state);
	}

	/**
	 * Request to the server to give all stories of a user
	 * 
	 */
	public void getAllStories() {
		GameState state = new GameState();
		state.setMode(GAMESTATEMODES.ROLEPLAY_REQUEST_ALLSTORIES);
		state.addObject(Nexus.getCurrentUser());

		Nexus.fireNetworkEvent(state);
	}

	/**
	 * Request to the server to give all characters
	 * 
	 */
	public void getAllCharacter() {
		GameState state = new GameState();
		state.setMode(GAMESTATEMODES.ROLEPLAY_REQUEST_ALLCHARACTER);

		Nexus.fireNetworkEvent(state);
	}

	/**
	 *
	 * @param rpChar RolePlayCharacterDTO
	 * @param sortOrder Integer
	 */
	public void getNextChapterBySortOrder(RolePlayCharacterDTO rpChar, Integer sortOrder){
		GameState state = new GameState();
		state.setMode(GAMESTATEMODES.ROLEPLAY_GET_CHAPTER_BYSORTORDER);
		state.addObject(rpChar);
		state.addObject2(sortOrder);

		Nexus.fireNetworkEvent(state);
	}

	/**
	 *
	 * @param rpChar RolePlayCharacterDTO
	 * @param sortOrder Integer
	 */
	public void getNextStepBySortOrder(RolePlayCharacterDTO rpChar, Integer sortOrder){
		GameState state = new GameState();
		state.setMode(GAMESTATEMODES.ROLEPLAY_GET_STEP_BYSORTORDER);
		state.addObject(rpChar);
		state.addObject2(sortOrder);

		Nexus.fireNetworkEvent(state);
	}

	/**
	 * Save methode for a story/chapter/step
	 *
	 * @param rpChar RolePlayStoryDTO
	 */
	public void saveRolePlayCharacter(RolePlayCharacterDTO rpChar, Long newRPID) {
		GameState state = new GameState(GAMESTATEMODES.ROLEPLAY_SAVE_NEXT_STEP);
		state.addObject(rpChar);
		state.addObject2(newRPID);

		Nexus.fireNetworkEvent(state);
	}

	/*---------------- upload and delete files on server (image, sound and video) ----------------*/
	/**
	 * Upload the image for the given story
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean TRUE if upload was successful, otherwise FALSE
	 */
	public boolean uploadImage(RolePlayStoryDTO story) {

		IFileTransfer ft = Tools.getFileTransfer();
		String subDir = story.getId().toString();
		ft.makeDir(subDir);
		ft.deleteAllFiles(subDir, ".png");
		return ft.upload(story.getNewImageWithPath(), subDir + "/" + story.getStoryImage());
	}

	/**
	 * Upload the sound for the given story
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean TRUE if upload was successful, otherwise FALSE
	 */
	public boolean uploadSound(RolePlayStoryDTO story) {

		IFileTransfer ft = Tools.getFileTransfer();
		String subDir = story.getId().toString();
		ft.makeDir(subDir);
		ft.deleteAllFiles(subDir, ".mp3");
		return ft.upload(story.getNewVoiceWithPath(), subDir + "/" + story.getStoryMP3());
	}

	/**
	 * Upload the video for the given story
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean TRUE if upload was successful, otherwise FALSE
	 */
	public boolean uploadVideo(RolePlayStoryDTO story) {

		IFileTransfer ft = Tools.getFileTransfer();
		String subDir = story.getId().toString();
		ft.makeDir(subDir);
		ft.deleteAllFiles(subDir, ".mp4");
		return ft.upload(story.getNewMovieWithPath(), subDir + "/" + story.getStoryIntro());
	}

	/**
	 * Deletes the image for the given story
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean TRUE if delete was successful, otherwise FALSE
	 */
	public boolean deleteImage(RolePlayStoryDTO story) {

		IFileTransfer ft = Tools.getFileTransfer();
		String subDir = story.getId().toString();
		return ft.deleteAllFiles(subDir, ".png");
	}

	/**
	 * Deletes the sound for the given story
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean TRUE if delete was successful, otherwise FALSE
	 */
	public boolean deleteSound(RolePlayStoryDTO story) {

		IFileTransfer ft = Tools.getFileTransfer();
		String subDir = story.getId().toString();
		return ft.deleteAllFiles(subDir, ".mp3");
	}

	/**
	 * Deletes the video for the given story
	 *
	 * @param story RolePlayStoryDTO
	 * @return boolean TRUE if delete was successful, otherwise FALSE
	 */
	public boolean deleteVideo(RolePlayStoryDTO story) {

		IFileTransfer ft = Tools.getFileTransfer();
		String subDir = story.getId().toString();
		return ft.deleteAllFiles(subDir, ".mp4");
	}

	/*--------------- HELPER ---------------*/

	/**
	 * Checks if the objects have the same id
	 * 
	 * @param r1 RolePlayStoryDTO for check
	 * @param r2 RolePlayStoryDTO for check
	 * @return return true if both parameters are the same otherwise false
	 */
	private boolean isSameRolePlayStore(RolePlayStoryDTO r1, RolePlayStoryDTO r2) {
		return r1 != null && r2 != null && r1.getId().equals(r2.getId());

	}
}
