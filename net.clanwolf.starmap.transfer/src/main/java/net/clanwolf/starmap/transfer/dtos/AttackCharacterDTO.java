package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
		scope= AttackCharacterDTO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class AttackCharacterDTO {

//	@Column(name = "ID")
	private Long id;

//	@Column(name = "AttackID")
	private Long attackID;

//	@JoinColumn(name = "CharacterID")
	private RolePlayCharacterDTO characterID;

//	@Column(name = "Type")
	private Long type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAttackID() {
		return attackID;
	}

	public void setAttackID(Long attackID) {
		this.attackID = attackID;
	}

	public RolePlayCharacterDTO getCharacterID() {
		return characterID;
	}

	public void setCharacterID(RolePlayCharacterDTO characterID) {
		this.characterID = characterID;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
}
