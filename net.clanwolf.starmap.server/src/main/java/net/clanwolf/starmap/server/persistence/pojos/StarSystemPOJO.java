package net.clanwolf.starmap.server.persistence.pojos;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= net.clanwolf.starmap.server.persistence.pojos.StarSystemPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "STARSYSTEM", catalog = "C3")
public class StarSystemPOJO {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name")
	private String name;

	@Column(name = "x")
	private BigDecimal x;

	@Column(name = "y")
	private BigDecimal y;

	@Column(name = "StarType1")
	private String starType1;

	@Column(name = "StarType2")
	private String starType2;

	@Column(name = "StarType3")
	private String starType3;

	@Column(name = "Population")
	private Long population;

	@Column(name = "PopulationCountYear")
	private Long populationCountYear;

	@Column(name = "Capital")
	private String capital;

	@Column(name = "Temperature")
	private Long temperature;

	@Column(name = "Position")
	private Long position;

	@Column(name = "Moons")
	private String moons;

	@Column(name = "Landmasses")
	private String landmasses;

	@Column(name = "Gravitation")
	private Double gravitation;

	@Column(name = "WaterPercentage")
	private Long waterPercentage;

	@Column(name = "Lifeform")
	private String lifeform;

	@Column(name = "Resources")
	private Long resources;

	@Column(name = "MainPlanet")
	private Boolean mainPlanet;

	@Column(name = "SystemImageName")
	private String systemImageName;

	@Column(name = "Uninhabitable")
	private Boolean uninhabitable;

	@Column(name = "Rating")
	private String rating;

	@Column(name = "SurfaceMap")
	private String surfaceMap;

	//@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "FactionID")
	private Long factionID;

	@Column(name = "FactionID_3025")
	private Long factionID_3025;

	@Column(name = "SarnaLinkSystem")
	private String sarnaLinkSystem;

	@Column(name = "Description")
	private String description;

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	public BigDecimal getX() {
		return x;
	}

	@SuppressWarnings("unused")
	public void setX(BigDecimal x) {
		this.x = x;
	}

	@SuppressWarnings("unused")
	public BigDecimal getY() {
		return y;
	}

	@SuppressWarnings("unused")
	public void setY(BigDecimal y) {
		this.y = y;
	}

	@SuppressWarnings("unused")
	public String getStarType1() {
		return starType1;
	}

	@SuppressWarnings("unused")
	public void setStarType1(String starType1) {
		this.starType1 = starType1;
	}

	@SuppressWarnings("unused")
	public String getStarType2() {
		return starType2;
	}

	@SuppressWarnings("unused")
	public void setStarType2(String starType2) {
		this.starType2 = starType2;
	}

	@SuppressWarnings("unused")
	public String getStarType3() {
		return starType3;
	}

	@SuppressWarnings("unused")
	public void setStarType3(String starType3) {
		this.starType3 = starType3;
	}

	@SuppressWarnings("unused")
	public Long getPopulation() {
		return population;
	}

	@SuppressWarnings("unused")
	public void setPopulation(Long population) {
		this.population = population;
	}

	@SuppressWarnings("unused")
	public Long getPopulationCountYear() {
		return populationCountYear;
	}

	@SuppressWarnings("unused")
	public void setPopulationCountYear(Long populationCountYear) {
		this.populationCountYear = populationCountYear;
	}

	@SuppressWarnings("unused")
	public String getCapital() {
		return capital;
	}

	@SuppressWarnings("unused")
	public void setCapital(String capital) {
		this.capital = capital;
	}

	@SuppressWarnings("unused")
	public Long getTemperature() {
		return temperature;
	}

	@SuppressWarnings("unused")
	public void setTemperature(Long temperature) {
		this.temperature = temperature;
	}

	@SuppressWarnings("unused")
	public Long getPosition() {
		return position;
	}

	@SuppressWarnings("unused")
	public void setPosition(Long position) {
		this.position = position;
	}

	@SuppressWarnings("unused")
	public String getMoons() {
		return moons;
	}

	@SuppressWarnings("unused")
	public void setMoons(String moons) {
		this.moons = moons;
	}

	@SuppressWarnings("unused")
	public String getLandmasses() {
		return landmasses;
	}

	@SuppressWarnings("unused")
	public void setLandmasses(String landmasses) {
		this.landmasses = landmasses;
	}

	@SuppressWarnings("unused")
	public Double getGravitation() {
		return gravitation;
	}

	@SuppressWarnings("unused")
	public void setGravitation(Double gravitation) {
		this.gravitation = gravitation;
	}

	@SuppressWarnings("unused")
	public Long getWaterPercentage() {
		return waterPercentage;
	}

	@SuppressWarnings("unused")
	public void setWaterPercentage(Long waterPercentage) {
		this.waterPercentage = waterPercentage;
	}

	@SuppressWarnings("unused")
	public String getLifeform() {
		return lifeform;
	}

	@SuppressWarnings("unused")
	public void setLifeform(String lifeform) {
		this.lifeform = lifeform;
	}

	@SuppressWarnings("unused")
	public Long getResources() {
		return resources;
	}

	@SuppressWarnings("unused")
	public void setResources(Long resources) {
		this.resources = resources;
	}

	@SuppressWarnings("unused")
	public Boolean getMainPlanet() {
		return mainPlanet;
	}

	@SuppressWarnings("unused")
	public void setMainPlanet(Boolean mainPlanet) {
		this.mainPlanet = mainPlanet;
	}

	@SuppressWarnings("unused")
	public String getSystemImageName() {
		return systemImageName;
	}

	@SuppressWarnings("unused")
	public void setSystemImageName(String systemImageName) {
		this.systemImageName = systemImageName;
	}

	@SuppressWarnings("unused")
	public Boolean getUninhabitable() {
		return uninhabitable;
	}

	@SuppressWarnings("unused")
	public void setUninhabitable(Boolean uninhabitable) {
		this.uninhabitable = uninhabitable;
	}

	@SuppressWarnings("unused")
	public String getRating() {
		return rating;
	}

	@SuppressWarnings("unused")
	public void setRating(String rating) {
		this.rating = rating;
	}

	@SuppressWarnings("unused")
	public String getSurfaceMap() {
		return surfaceMap;
	}

	@SuppressWarnings("unused")
	public void setSurfaceMap(String surfaceMap) {
		this.surfaceMap = surfaceMap;
	}

	@SuppressWarnings("unused")
	public Long getFactionID() {
		return factionID;
	}

	@SuppressWarnings("unused")
	public void setFactionID(Long factionID) {
		this.factionID = factionID;
	}

	@SuppressWarnings("unused")
	public Long getFactionID_3025() {
		return factionID_3025;
	}

	@SuppressWarnings("unused")
	public void setFactionID_3025(Long factionID_3025) {
		this.factionID_3025 = factionID_3025;
	}

	@SuppressWarnings("unused")
	public String getSarnaLinkSystem() {
		return sarnaLinkSystem;
	}

	@SuppressWarnings("unused")
	public void setSarnaLinkSystem(String sarnaLinkSystem) {
		this.sarnaLinkSystem = sarnaLinkSystem;
	}

	@SuppressWarnings("unused")
	public String getDescription() {
		return description;
	}

	@SuppressWarnings("unused")
	public void setDescription(String description) {
		this.description = description;
	}
}
