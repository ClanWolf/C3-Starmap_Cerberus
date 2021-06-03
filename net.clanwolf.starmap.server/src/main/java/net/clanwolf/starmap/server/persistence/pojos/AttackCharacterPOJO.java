package net.clanwolf.starmap.server.persistence.pojos;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= AttackCharacterPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_ATTACK_CHARACTER", catalog = "C3")
public class AttackCharacterPOJO {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "AttackID")
	private Long attackID;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
	@JoinColumn(name = "CharacterID")
	private RolePlayCharacterPOJO characterID;

	@Column(name = "Type")
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

	public RolePlayCharacterPOJO getCharacterID() {
		return characterID;
	}

	public void setCharacterID(RolePlayCharacterPOJO characterID) {
		this.characterID = characterID;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
}
