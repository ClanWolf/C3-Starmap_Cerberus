package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

public class DiplomacyDTO  extends Dto {

	//@Id
	//@GeneratedValue(strategy = IDENTITY)
	//@Column(name = "ID")
	private Long id;

	//@Column(name = "SeasonID")
	private Long seasonID;

	//@Column(name = "SeasonID")
	private Long FactionID_REQUEST;

	//@Column(name = "SeasonID")
	private Long FactionID_ACCEPTED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(Long seasonID) {
		this.seasonID = seasonID;
	}

	public Long getFactionID_REQUEST() {
		return FactionID_REQUEST;
	}

	public void setFactionID_REQUEST(Long factionID_REQUEST) {
		this.FactionID_REQUEST = factionID_REQUEST;
	}

	public Long getFactionID_ACCEPTED() {
		return FactionID_ACCEPTED;
	}

	public void setFactionID_ACCEPTED(Long factionID_ACCEPTED) {
		this.FactionID_ACCEPTED = factionID_ACCEPTED;
	}
}
