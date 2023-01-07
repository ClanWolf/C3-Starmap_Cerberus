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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

import java.math.BigDecimal;
import java.util.HashMap;

public class StarSystemDTO extends Dto {

	//@Id
	//@GeneratedValue(strategy = IDENTITY)
	//@Column(name = "ID")
	private Long id;

	//@Column(name = "Name")
	private String name;

	//@Column(name = "x")
	private BigDecimal x;

	//@Column(name = "y")
	private BigDecimal y;

	//@Column(name = "StarType1")
	private String starType1;

	//@Column(name = "StarType2")
	private String starType2;

	//@Column(name = "StarType3")
	private String starType3;

	//@Column(name = "Population")
	private Long population;

	//@Column(name = "PopulationCountYear")
	private Long populationCountYear;

	//@Column(name = "Capital")
	private String capital;

	//@Column(name = "Temperature")
	private Long temperature;

	//@Column(name = "Position")
	private Long position;

	//@Column(name = "Moons")
	private String moons;

	//@Column(name = "Landmasses")
	private String landmasses;

	//@Column(name = "Gravitation")
	private Double gravitation;

	//@Column(name = "WaterPercentage")
	private Long waterPercentage;

	//@Column(name = "Lifeform")
	private String lifeform;

	//@Column(name = "Resources")
	private Long resources;

	//@Column(name = "MainPlanet")
	private Boolean mainPlanet;

	//@Column(name = "SystemImageName")
	private String systemImageName;

	//@Column(name = "Uninhabitable")
	private Boolean uninhabitable;

	//@Column(name = "Rating")
	private String rating;

	//@Column(name = "SurfaceMap")
	private String surfaceMap;

	//@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	//@JoinColumn(name = "FactionID")
	private Long factionID;

	//@Column(name = "FactionID_3025")
	private Long factionID_3025;

	//@Column(name = "SarnaLinkSystem")
	private String sarnaLinkSystem;

	//@Column(name = "Description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public String getStarType1() {
		return starType1;
	}

	public void setStarType1(String starType1) {
		this.starType1 = starType1;
	}

	public String getStarType2() {
		return starType2;
	}

	public void setStarType2(String starType2) {
		this.starType2 = starType2;
	}

	public String getStarType3() {
		return starType3;
	}

	public void setStarType3(String starType3) {
		this.starType3 = starType3;
	}

	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	public Long getPopulationCountYear() {
		return populationCountYear;
	}

	public void setPopulationCountYear(Long populationCountYear) {
		this.populationCountYear = populationCountYear;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public Long getTemperature() {
		return temperature;
	}

	public void setTemperature(Long temperature) {
		this.temperature = temperature;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public String getMoons() {
		return moons;
	}

	public void setMoons(String moons) {
		this.moons = moons;
	}

	public String getLandmasses() {
		return landmasses;
	}

	public void setLandmasses(String landmasses) {
		this.landmasses = landmasses;
	}

	public Double getGravitation() {
		return gravitation;
	}

	public void setGravitation(Double gravitation) {
		this.gravitation = gravitation;
	}

	public Long getWaterPercentage() {
		return waterPercentage;
	}

	public void setWaterPercentage(Long waterPercentage) {
		this.waterPercentage = waterPercentage;
	}

	public String getLifeform() {
		return lifeform;
	}

	public void setLifeform(String lifeform) {
		this.lifeform = lifeform;
	}

	public Long getResources() {
		return resources;
	}

	public void setResources(Long resources) {
		this.resources = resources;
	}

	public Boolean getMainPlanet() {
		return mainPlanet;
	}

	public void setMainPlanet(Boolean mainPlanet) {
		this.mainPlanet = mainPlanet;
	}

	public String getSystemImageName() {
		return systemImageName;
	}

	public void setSystemImageName(String systemImageName) {
		this.systemImageName = systemImageName;
	}

	public Boolean getUninhabitable() {
		return uninhabitable;
	}

	public void setUninhabitable(Boolean uninhabitable) {
		this.uninhabitable = uninhabitable;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getSurfaceMap() {
		return surfaceMap;
	}

	public void setSurfaceMap(String surfaceMap) {
		this.surfaceMap = surfaceMap;
	}

	public Long getFactionID() {
		return factionID;
	}

	public void setFactionID(Long factionID) {
		this.factionID = factionID;
	}

	public Long getFactionID_3025() {
		return factionID_3025;
	}

	public void setFactionID_3025(Long factionID_3025) {
		this.factionID_3025 = factionID_3025;
	}

	public String getSarnaLinkSystem() {
		return sarnaLinkSystem;
	}

	public void setSarnaLinkSystem(String sarnaLinkSystem) {
		this.sarnaLinkSystem = sarnaLinkSystem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	@SuppressWarnings("unused")
	public String toString() {
		return this.name;
	}
}
