package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= DiplomacyPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "DIPLOMACY", catalog = "C3")
public class DiplomacyPOJO  extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SeasonID")
	private Long seasonID;

	@Column(name = "FactionID_REQUEST")
	private Long factionID_REQUEST;

	@Column(name = "FactionID_ACCEPTED")
	private Long factionID_ACCEPTED;

	@Column(name = "StartingInRound")
	private Integer startingInRound;

	@Column(name = "EndingInRound")
	private Integer endingInRound;

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
		return factionID_REQUEST;
	}

	public void setFactionID_REQUEST(Long factionID_REQUEST) {
		this.factionID_REQUEST = factionID_REQUEST;
	}

	public Long getFactionID_ACCEPTED() {
		return factionID_ACCEPTED;
	}

	public Integer getStartingInRound() {
		return startingInRound;
	}

	public void setStartingInRound(Integer startingInRound) {
		this.startingInRound = startingInRound;
	}

	public Integer getEndingInRound() {
		return endingInRound;
	}

	public void setEndingInRound(Integer endingInRound) {
		this.endingInRound = endingInRound;
	}

	public void setFactionID_ACCEPTED(Long factionID_ACCEPTED) {
		this.factionID_ACCEPTED = factionID_ACCEPTED;
	}
}
