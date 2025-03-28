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
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.util.ArrayList;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= RolePlayStoryPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "ROLEPLAY_STORY", catalog = "C3")
public class RolePlayStoryPOJO extends Pojo {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StoryID")
   // @JoinColumn(name = "StoryID", insertable = false, updatable = false)
    private RolePlayStoryPOJO story;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ParentStoryID")
    private RolePlayStoryPOJO parentStory;

   // @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Author")
    private Long author;

	@Column(name = "StoryName")
    private String storyName;

    @Column(name = "TypeENUM")
    @Enumerated(EnumType.STRING)
    private ROLEPLAYENTRYTYPES type;

    @Column(name = "SortOrder")
    private Integer sortOrder;

    @Column(name = "StoryText")
    private String storyText;

    @Column(name = "StoryImage")
    private String storyImage;

    @Column(name = "StoryMP3")
    private String storyMP3;

    @Column(name = "StoryIntro")
    private String StoryIntro;

    @Column(name = "StoryDescription")
    private String storyDescription;

    @Column(name = "RolePlayOff")
    private String rolePlayOff;

	@Column(name = "buttonText")
	private String buttonText;

	@Column(name = "attackerWins")
	private Boolean attackerWins;

	@Column(name = "defenderWins")
	private Boolean defenderWins;

	@Column(name = "xPosText")
	private Integer xPosText;

	@Column(name = "yPosText")
	private Integer yPosText;

	@Column(name = "heightText")
	private Integer heightText;

	@Column(name = "widthText")
	private Integer widthText;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RPID_Choice")
    private RolePlayStoryChoicePOJO rpid_choice;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RPID_Datainput")
    private RolePlayStoryDatainputPOJO rpid_datainput;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RPID_Dice")
    private RolePlayStoryDicePOJO rpid_dice;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "RPID_Keypad")
	private RolePlayStoryKeypadPOJO rpid_keypad;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "RPID_HPGMessage")
	private RolePlayStoryHPGMessagePOJO rpid_hpgmessage;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "RPID_Invasion")
	private RolePlayStoryInvasionPOJO rpid_invasion;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "NextStepID")
//	private RolePlayStoryPOJO nextStepID;
	private Long nextStepID;

    @Column(name = "URL")
    private String url;

	@Transient
	private String newImageWithPath;
	@Transient
	private String newVoiceWithPath;
	@Transient
	private String newMovieWithPath;
	@Transient
	private Integer sortOrderOld;
	@Transient
	private ArrayList<Long> newCharIDs;
	@Transient
	private ArrayList<Long> removedCharIDs;

    public RolePlayStoryPOJO() {
        // do nothing
    } // BFUser


    /* -- Getter -- */
    public Long getId() {
        return id;

    } // getId

	public Boolean getDefenderWins() {
		return defenderWins;
	}

	public void setDefenderWins(Boolean defenderWins) {
		this.defenderWins = defenderWins;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public Boolean getAttackerWins() {
		return attackerWins;
	}

	public void setAttackerWins(Boolean attackerWins) {
		this.attackerWins = attackerWins;
	}

	public RolePlayStoryPOJO getStory() {
		return story;
	}


	public void setStory(RolePlayStoryPOJO story) {
		this.story = story;
	}


	public RolePlayStoryPOJO getParentStory() {
		if(type == ROLEPLAYENTRYTYPES.RP_CHAPTER){
			return story;
		}
    	return parentStory;
	}


	public void setParentStory(RolePlayStoryPOJO parentStory) {
		if(type == ROLEPLAYENTRYTYPES.RP_CHAPTER){
			this.parentStory = null;
		} else {
			this.parentStory = parentStory;
		}

	}


	public String getStoryName() {
		return storyName;
	}


	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}


	public ROLEPLAYENTRYTYPES getVariante() {
		return type;
	}


	public void setVariante(ROLEPLAYENTRYTYPES variante) {
		this.type = variante;
	}


	public String getStoryText() {
		return storyText;
	}


	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}


	public String getStoryImage() {
		return storyImage;
	}


	public void setStoryImage(String storyImage) {
		this.storyImage = storyImage;
	}


	public String getStoryMP3() {
		return storyMP3;
	}


	public void setStoryMP3(String storyMP3) {
		this.storyMP3 = storyMP3;
	}


	public String getStoryIntro() {
		return StoryIntro;
	}


	public void setStoryIntro(String storyIntro) {
		StoryIntro = storyIntro;
	}


	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the storyDescription
	 */
	public String getStoryDescription() {
		return storyDescription;
	}


	/**
	 * @param storyDescription the storyDescription to set
	 */
	public void setStoryDescription(String storyDescription) {
		this.storyDescription = storyDescription;
	}



	/**
	 * @return the author
	 */
	public Long getAuthor() {
		return author;
	}


	/**
	 * @param author the author to set
	 */
	public void setAuthor(Long author) {
		this.author = author;
	}


	/**
	 * @return the rolePlayOff
	 */
	public String getRolePlayOff() {
		return rolePlayOff;
	}


	/**
	 * @param rolePlayOff the rolePlayOff to set
	 */
	public void setRolePlayOff(String rolePlayOff) {
		this.rolePlayOff = rolePlayOff;
	}

	/**
	 * @return the newImageWithPath
	 */
	public String getNewImageWithPath() {
		return newImageWithPath;
	}


	/**
	 * @param newImageWithPath the newImageWithPath to set
	 */
	public void setNewImageWithPath(String newImageWithPath) {
		this.newImageWithPath = newImageWithPath;
	}

	/**
	 * @return the newVoiceWithPath
	 */
	public String getNewVoiceWithPath() {
		return newVoiceWithPath;
	}


	/**
	 * @param newVoiceWithPath the newVoiceWithPath to set
	 */
	public void setNewVoiceWithPath(String newVoiceWithPath) {
		this.newVoiceWithPath = newVoiceWithPath;
	}


	/**
	 * @return the newMovieWithPath
	 */
	public String getNewMovieWithPath() {
		return newMovieWithPath;
	}


	/**
	 * @param newMovieWithPath the newMovieWithPath to set
	 */
	public void setNewMovieWithPath(String newMovieWithPath) {
		this.newMovieWithPath = newMovieWithPath;
	}

	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}


	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the sortOrderOld
	 */
	public Integer getSortOrderOld() {
		return sortOrderOld;
	}


	/**
	 * @param sortOrderOld the sortOrderOld to set
	 */
	public void setSortOrderOld(Integer sortOrderOld) {
		this.sortOrderOld = sortOrderOld;
	}

	/**
	 * @return the var2ID
	 */
	public RolePlayStoryChoicePOJO getVar2ID() {
		return rpid_choice;
	}


	/**
	 * @param var2id the var2ID to set
	 */
	public void setVar2ID(RolePlayStoryChoicePOJO var2id) {
		rpid_choice = var2id;
	}


	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return the var3ID
	 */
	public RolePlayStoryDatainputPOJO getVar3ID() {
		return rpid_datainput;
	}


	/**
	 * @param var3id the var3ID to set
	 */
	public void setVar3ID(RolePlayStoryDatainputPOJO var3id) {
		rpid_datainput = var3id;
	}


	/**
	 * @return the var4ID
	 */
	public RolePlayStoryDicePOJO getVar4ID() {
		return rpid_dice;
	}


	/**
	 * @param var4id the var4ID to set
	 */
	public void setVar4ID(RolePlayStoryDicePOJO var4id) {
		rpid_dice = var4id;
	}

	public Long getNextStepID() {
		return nextStepID;
	}

	public void setNextStepID(Long nextStepID) {
		this.nextStepID = nextStepID;
	}

	public ArrayList<Long> getNewCharIDs() {
		return newCharIDs;
	}

	public void setNewCharIDs(ArrayList<Long> newCharIDs) {
		this.newCharIDs = newCharIDs;
	}

	public ArrayList<Long> getRemovedCharIDs() {
		return removedCharIDs;
	}

	public void setRemovedCharIDs(ArrayList<Long> removedCharIDs) {
		this.removedCharIDs = removedCharIDs;
	}

	public RolePlayStoryKeypadPOJO getVar6ID() {
		return rpid_keypad;
	}

	public void setVar6ID(RolePlayStoryKeypadPOJO var6ID) {
		this.rpid_keypad = var6ID;
	}

	public Integer getxPosText() {
		return xPosText;
	}

	public void setxPosText(Integer xPosText) {
		this.xPosText = xPosText;
	}

	public Integer getyPosText() {
		return yPosText;
	}

	public void setyPosText(Integer yPosText) {
		this.yPosText = yPosText;
	}

	public Integer getHeightText() {
		return heightText;
	}

	public void setHeightText(Integer heightText) {
		this.heightText = heightText;
	}

	public Integer getWidthText() {
		return widthText;
	}

	public void setWidthText(Integer widthText) {
		this.widthText = widthText;
	}

	public RolePlayStoryHPGMessagePOJO getVar7ID() {
		return rpid_hpgmessage;
	}

	public void setVar7ID(RolePlayStoryHPGMessagePOJO var7ID) {
		rpid_hpgmessage = var7ID;
	}

	public RolePlayStoryInvasionPOJO getVar9ID() {
		return rpid_invasion;
	}

	public void setVar9ID(RolePlayStoryInvasionPOJO var9ID) {
		rpid_invasion = var9ID;
	}

	@Override
	public String toString() {
		return this.getStoryName();
	}

}
