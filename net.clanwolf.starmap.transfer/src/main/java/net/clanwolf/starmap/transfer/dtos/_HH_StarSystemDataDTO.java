package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;


public class _HH_StarSystemDataDTO extends Dto {

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StarSystemDTO getStarSystemID() {
		return starSystemID;
	}

	public void setStarSystemID(StarSystemDTO starSystemID) {
		this.starSystemID = starSystemID;
	}

	public FactionDTO getFactionID() {
		return factionID;
	}

	public void setFactionID(FactionDTO factionID) {
		this.factionID = factionID;
	}

	public Long getFactionID_Start() {
		return factionID_Start;
	}

	public void setFactionID_Start(Long factionID_Start) {
		this.factionID_Start = factionID_Start;
	}

	public Long getInfrastructure() {
		return infrastructure;
	}

	public void setInfrastructure(Long infrastructure) {
		this.infrastructure = infrastructure;
	}

	public Long getWealth() {
		return wealth;
	}

	public void setWealth(Long wealth) {
		this.wealth = wealth;
	}

	public Long getVeternacy() {
		return veternacy;
	}

	public void setVeternacy(Long veternacy) {
		this.veternacy = veternacy;
	}

	public Long getClasse() {
		return classe;
	}

	public void setClasse(Long classe) {
		this.classe = classe;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getCapitalWorld() {
		return capitalWorld;
	}

	public void setCapitalWorld(Boolean capitalWorld) {
		this.capitalWorld = capitalWorld;
	}
}
