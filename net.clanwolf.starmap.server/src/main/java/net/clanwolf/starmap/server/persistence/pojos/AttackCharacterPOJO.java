package net.clanwolf.starmap.server.persistence.pojos;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNextStoryId() {
		return nextStoryId;
	}

	public void setNextStoryId(Long nextStoryId) {
		this.nextStoryId = nextStoryId;
	}

	public Long getAttackID() {
		return attackID;
	}

	public void setAttackID(Long attackID) {
		this.attackID = attackID;
	}

	public Long getCharacterID() {
		return characterID;
	}

	public void setCharacterID(Long characterID) {
		this.characterID = characterID;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
}
