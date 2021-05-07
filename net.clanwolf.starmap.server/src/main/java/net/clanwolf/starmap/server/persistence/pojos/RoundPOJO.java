package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;
import java.sql.Date;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= RoundPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_ROUND", catalog = "C3")
public class RoundPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Round")
	private Long round;

	@Column(name = "Season")
	private Long season;

	@Column(name = "CurrentPhase")
	private Long currentPhase;

	@Column(name = "CurrentRoundStartDate")
	private Date currentRoundStartDate;

	@SuppressWarnings("unused")
	public Date getCurrentRoundStartDate() {
		return currentRoundStartDate;
	}

	@SuppressWarnings("unused")
	public void setCurrentRoundStartDate(Date currentRoundStartDate) {
		this.currentRoundStartDate = currentRoundStartDate;
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
	public Long getRound() {
		return round;
	}

	@SuppressWarnings("unused")
	public void setRound(Long round) {
		this.round = round;
	}

	@SuppressWarnings("unused")
	public Long getSeason() {
		return season;
	}

	@SuppressWarnings("unused")
	public void setSeason(Long season) {
		this.season = season;
	}

	@SuppressWarnings("unused")
	public Long getCurrentPhase() {
		return currentPhase;
	}

	@SuppressWarnings("unused")
	public void setCurrentPhase(Long currentPhase) {
		this.currentPhase = currentPhase;
	}
}
