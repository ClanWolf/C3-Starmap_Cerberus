/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : c3.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import javax.persistence.*;
import java.util.ArrayList;

import static javax.persistence.GenerationType.IDENTITY;

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
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Var2ID")
    private RolePlayStoryVar2POJO var2ID;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Var3ID")
    private RolePlayStoryVar3POJO var3ID;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Var4ID")
    private RolePlayStoryVar4POJO var4ID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NextStepID")
	private RolePlayStoryPOJO nextStepID;
    
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


	public RolePlayStoryPOJO getStory() {
		return story;
	}


	public void setStory(RolePlayStoryPOJO story) {
		this.story = story;
	}


	public RolePlayStoryPOJO getParentStory() {
		if(type == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER){
			return story;
		}
    	return parentStory;
	}


	public void setParentStory(RolePlayStoryPOJO parentStory) {
		if(type == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER){
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
	public RolePlayStoryVar2POJO getVar2ID() {
		return var2ID;
	}


	/**
	 * @param var2id the var2ID to set
	 */
	public void setVar2ID(RolePlayStoryVar2POJO var2id) {
		var2ID = var2id;
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
	public RolePlayStoryVar3POJO getVar3ID() {
		return var3ID;
	}


	/**
	 * @param var3id the var3ID to set
	 */
	public void setVar3ID(RolePlayStoryVar3POJO var3id) {
		var3ID = var3id;
	}


	/**
	 * @return the var4ID
	 */
	public RolePlayStoryVar4POJO getVar4ID() {
		return var4ID;
	}


	/**
	 * @param var4id the var4ID to set
	 */
	public void setVar4ID(RolePlayStoryVar4POJO var4id) {
		var4ID = var4id;
	}

	public RolePlayStoryPOJO getNextStepID() {
		return nextStepID;
	}

	public void setNextStepID(RolePlayStoryPOJO nextStepID) {
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

	@Override
	public String toString() {
		return this.getStoryName();
	}

}
