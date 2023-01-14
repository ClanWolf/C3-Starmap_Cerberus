package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= FactionTypePOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "FACTIONTYPE", catalog = "C3")
public class FactionTypePOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name_en")
	private String name_en;

	@Column(name = "Name_de")
	private String name_de;

	@Column(name = "ShortName")
	private String shortName;

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getName_en() {
		return name_en;
	}

	@SuppressWarnings("unused")
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	@SuppressWarnings("unused")
	public String getName_de() {
		return name_de;
	}

	@SuppressWarnings("unused")
	public void setName_de(String name_de) {
		this.name_de = name_de;
	}

	@SuppressWarnings("unused")
	public String getShortName() {
		return shortName;
	}

	@SuppressWarnings("unused")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
