package net.clanwolf.starmap.server.persistence.pojos;


import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "_HH_ATTACKRESULT", catalog = "C3")
public class HHjumpshipPOJO extends Pojo {


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "JumpshipName")
	private String jumpshipName;

	@Column(name = "JumpshipFactionID")
	private Long jumpshipFactionID;

	@Column(name = "StarSystemHistory")
	private String starSystemHistory;

	@Column(name = "LastMovedInRound")
	private Long lastMovedInRound;

	@Column(name = "AttackReady")
	private Boolean attackReady;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJumpshipName() {
		return jumpshipName;
	}

	public void setJumpshipName(String jumpshipName) {
		this.jumpshipName = jumpshipName;
	}

	public Long getJumpshipFactionID() {
		return jumpshipFactionID;
	}

	public void setJumpshipFactionID(Long jumpshipFactionID) {
		this.jumpshipFactionID = jumpshipFactionID;
	}

	public String getStarSystemHistory() {
		return starSystemHistory;
	}

	public void setStarSystemHistory(String starSystemHistory) {
		this.starSystemHistory = starSystemHistory;
	}

	public Long getLastMovedInRound() {
		return lastMovedInRound;
	}

	public void setLastMovedInRound(Long lastMovedInRound) {
		this.lastMovedInRound = lastMovedInRound;
	}

	public Boolean getAttackReady() {
		return attackReady;
	}

	public void setAttackReady(Boolean attackReady) {
		this.attackReady = attackReady;
	}
}
