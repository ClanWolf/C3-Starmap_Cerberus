package net.clanwolf.starmap.server.persistence.pojos;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= AttackCharacterPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_ATTACK_CHARACTER", catalog = "C3")
public class AttackCharacterPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "AttackID")
	private Long attackID;

	//@OneToOne(fetch = FetchType.EAGER, orphanRemoval = false)
	//@JoinColumn(name = "CharacterID")
	//private RolePlayCharacterPOJO characterID;
	private Long characterID;

	@Column(name = "Type")
	private Long type;

	@Column(name = "nextStoryId")
	private Long nextStoryId;

	@Column(name = "selectedAttackerWon")
	private Boolean selectedAttackerWon;

	@Column(name = "selectedDefenderWon")
	private Boolean selectedDefenderWon;

	@SuppressWarnings("unused")
	public Boolean getSelectedAttackerWon() {
		return selectedAttackerWon;
	}

	@SuppressWarnings("unused")
	public void setSelectedAttackerWon(Boolean selectedAttackerWon) {
		this.selectedAttackerWon = selectedAttackerWon;
	}

	@SuppressWarnings("unused")
	public Boolean getSelectedDefenderWon() {
		return selectedDefenderWon;
	}

	@SuppressWarnings("unused")
	public void setSelectedDefenderWon(Boolean selectedDefenderWon) {
		this.selectedDefenderWon = selectedDefenderWon;
	}

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public Long getNextStoryId() {
		return nextStoryId;
	}

	@SuppressWarnings("unused")
	public void setNextStoryId(Long nextStoryId) {
		this.nextStoryId = nextStoryId;
	}

	@SuppressWarnings("unused")
	public Long getAttackID() {
		return attackID;
	}

	@SuppressWarnings("unused")
	public void setAttackID(Long attackID) {
		this.attackID = attackID;
	}

	@SuppressWarnings("unused")
	public Long getCharacterID() {
		return characterID;
	}

	@SuppressWarnings("unused")
	public void setCharacterID(Long characterID) {
		this.characterID = characterID;
	}

	@SuppressWarnings("unused")
	public Long getType() {
		return type;
	}

	@SuppressWarnings("unused")
	public void setType(Long type) {
		this.type = type;
	}
}
