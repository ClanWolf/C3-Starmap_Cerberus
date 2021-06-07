package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import java.sql.Date;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= SeasonPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_SEASON", catalog = "C3")
public class SeasonPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name")
	private String name;

	@Column(name = "Ended")
	private Boolean ended;

	@Column(name = "Description")
	private String description;

	@Column(name = "StartDate")
	private Date startDate;

	@Column(name = "DaysInRound")
	private Long daysInRound;

	@SuppressWarnings("unused")
	public Long getDaysInRound() {
		return daysInRound;
	}

	@SuppressWarnings("unused")
	public void setDaysInRound(Long daysInRound) {
		this.daysInRound = daysInRound;
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
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	public Boolean getEnded() {
		return ended;
	}

	@SuppressWarnings("unused")
	public void setEnded(Boolean ended) {
		this.ended = ended;
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
	public Date getStartDate() {
		return startDate;
	}

	@SuppressWarnings("unused")
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
