package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;


public class StarSystemDataDTO extends Dto {

	//@Id
	//@GeneratedValue(strategy = IDENTITY)
	//@Column(name = "ID")
	private Long id ;

	//@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	//@Column(name = "StarSystemID")
	private StarSystemDTO starSystemID;

	//@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	//@Column(name = "FactionID")
	private FactionDTO factionID;

	//@Column(name = "FactionID_Start")
	private Long factionID_Start;

	//@Column(name = "Infrastructure")
	private Long infrastructure;

	//@Column(name = "Wealth")
	private Long wealth;

	//@Column(name = "Veternacy")
	private Long veternacy;

	//@Column(name = "Class")
	private Long classe;

	//@Column(name = "Type")
	private Long type;

	//@Column(name = "Description")
	private String description;

	//@Column(name = "Active")
	private Boolean active;

	//@Column(name = "CapitalWorld")
	private Boolean capitalWorld;

	//@Column(name = "ActiveInMetaPhase")
	private Long activeInMetaPhase;

	//@Column(name = "Level")
	private Long level;

//	@Column(name = "LockedUntilRound")
	private Long lockedUntilRound;

	@SuppressWarnings("unused")
	public Long getLockedUntilRound() {
		return lockedUntilRound;
	}

	@SuppressWarnings("unused")
	public void setLockedUntilRound(Long lockedUntilRound) {
		this.lockedUntilRound = lockedUntilRound;
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
	public StarSystemDTO getStarSystemID() {
		return starSystemID;
	}

	@SuppressWarnings("unused")
	public void setStarSystemID(StarSystemDTO starSystemID) {
		this.starSystemID = starSystemID;
	}

	@SuppressWarnings("unused")
	public FactionDTO getFactionID() {
		return factionID;
	}

	@SuppressWarnings("unused")
	public void setFactionID(FactionDTO factionID) {
		this.factionID = factionID;
	}

	@SuppressWarnings("unused")
	public Long getFactionID_Start() {
		return factionID_Start;
	}

	@SuppressWarnings("unused")
	public void setFactionID_Start(Long factionID_Start) {
		this.factionID_Start = factionID_Start;
	}

	@SuppressWarnings("unused")
	public Long getInfrastructure() {
		return infrastructure;
	}

	@SuppressWarnings("unused")
	public void setInfrastructure(Long infrastructure) {
		this.infrastructure = infrastructure;
	}

	@SuppressWarnings("unused")
	public Long getWealth() {
		return wealth;
	}

	@SuppressWarnings("unused")
	public void setWealth(Long wealth) {
		this.wealth = wealth;
	}

	@SuppressWarnings("unused")
	public Long getVeternacy() {
		return veternacy;
	}

	@SuppressWarnings("unused")
	public void setVeternacy(Long veternacy) {
		this.veternacy = veternacy;
	}

	@SuppressWarnings("unused")
	public Long getClasse() {
		return classe;
	}

	@SuppressWarnings("unused")
	public void setClasse(Long classe) {
		this.classe = classe;
	}

	@SuppressWarnings("unused")
	public Long getType() {
		return type;
	}

	@SuppressWarnings("unused")
	public void setType(Long type) {
		this.type = type;
	}

	@SuppressWarnings("unused")
	public String getDescription() {
		return description;
	}

	@SuppressWarnings("unused")
	public void setDescription(String description) {
		this.description = description;
	}

	@SuppressWarnings("unused")
	public Boolean getActive() {
		return active;
	}

	@SuppressWarnings("unused")
	public void setActive(Boolean active) {
		this.active = active;
	}

	@SuppressWarnings("unused")
	public Boolean getCapitalWorld() {
		return capitalWorld;
	}

	@SuppressWarnings("unused")
	public void setCapitalWorld(Boolean capitalWorld) {
		this.capitalWorld = capitalWorld;
	}

	@SuppressWarnings("unused")
	public Long getActiveInMetaPhase() {
		return activeInMetaPhase;
	}

	@SuppressWarnings("unused")
	public void setActiveInMetaPhase(Long activeInMetaPhase) {
		this.activeInMetaPhase = activeInMetaPhase;
	}

	@SuppressWarnings("unused")
	public Long getLevel() {
		return level;
	}

	@SuppressWarnings("unused")
	public void setLevel(Long level) {
		this.level = level;
	}
}
