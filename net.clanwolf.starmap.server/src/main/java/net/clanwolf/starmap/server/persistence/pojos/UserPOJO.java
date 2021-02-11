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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * UserPOJO entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@JsonIdentityInfo(
		scope= UserPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "USER", catalog = "C3")
public class UserPOJO extends Pojo {

	// Fields

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long id;

	@Column(name = "UserName", nullable = false, length = 45)
	private String userName;

	@Column(name = "UserPassword", nullable = true, length = 45)
	private String userPassword;

	@Column(name = "UserPasswordWebsite", nullable = true, length = 45)
	private String userPasswordWebsite;

	@Column(name = "UserEMail", nullable = false, length = 45)
	private String userEMail;

	@Column(name = "UserAvatar", nullable = true, length = 45)
	private String userAvatar;

	@Column(name = "FirstName", nullable = true, length = 45)
	private String firstName;

	@Column(name = "LastName", nullable = true, length = 45)
	private String lastName;

	@Column(name = "Location", nullable = true, length = 45)
	private String location;

	@Column(name = "Zipcode", nullable = true, length = 11)
	private int zipcode;

	@Column(name = "Website", nullable = true, length = 45)
	private String website;

	@Column(name = "Privileges", nullable = false, length = 64)
	private long privileges;

	@Column(name = "BirthDate", nullable = true, length = 19)
	private Timestamp birthDate;

	@Column(name = "JoinDate", nullable = false, length = 19)
	private Timestamp joinDate;

	@Column(name = "LastLogin", nullable = true, length = 19)
	private Timestamp lastLogin;

	@Column(name = "BannedUntil", nullable = true, length = 19)
	private Timestamp bannedUntil;

	@Column(name = "BannReason", nullable = true, length = 45)
	private String bannReason;

	@Column(name = "Created", nullable = false, length = 19)
	private Timestamp created;

	@Column(name = "LastModified", nullable = true, length = 19)
	private Timestamp lastModified;

	@Column(name = "active", nullable = true, length = 11)
	private int active;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CurrentCharacterID")
	private RolePlayCharacterPOJO currentCharacter;

	// Constructors

	/** default constructor */
	public UserPOJO() {
	}

	/**
	 * Full Constructor
	 *
	 */
	/*public UserPOJO(String userName, String userPassword, String userEMail, String userAvatar, String firstName, String lastName, String location, int zipcode, String website, long privileges, Timestamp birthDate, Timestamp joinDate, Timestamp lastLogin,
	                Timestamp bannedUntil, String bannReason) {
		this.userName = userName;
		this.userPassword = userPassword;
		this.userEMail = userEMail;
		this.userAvatar = userAvatar;
		this.firstName = firstName;
		this.lastName = lastName;
		this.location = location;
		this.zipcode = zipcode;
		this.website = website;
		this.privileges = privileges;
		this.birthDate = birthDate;
		this.joinDate = joinDate;
		this.lastLogin = lastLogin;
		this.bannedUntil = bannedUntil;
		this.bannReason = bannReason;

		LocalDateTime ldt = LocalDateTime.now();
		Timestamp t = Timestamp.valueOf(ldt);
		this.setCreated(t);
		this.setLastModified(t);
	}*/

	// Property accessors

	public Long getUserId() {
		return this.id;
	}

	public void setUserId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEMail() {
		return this.userEMail;
	}

	public void setUserEMail(String userEMail) {
		this.userEMail = userEMail;
	}

	public String getAvatar() {
		return this.userAvatar;
	}

	public void setAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public long getPrivileges() {
		return privileges;
	}

	public void setPrivileges(long privileges) {
		this.privileges = privileges;
	}

	public Timestamp getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
	}

	public Timestamp getJoinDate() {
		return this.joinDate;
	}

	public void setJoinDate(Timestamp joinDate) {
		this.joinDate = joinDate;
	}

	public Timestamp getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Timestamp getBannedUntil() {
		return this.bannedUntil;
	}

	public void setBannedUntil(Timestamp bannedUntil) {
		this.bannedUntil = bannedUntil;
	}

	public String getBannReason() {
		return this.bannReason;
	}

	public void setBannReason(String bannReason) {
		this.bannReason = bannReason;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public RolePlayCharacterPOJO getCurrentCharacter() {
		return this.currentCharacter;
	}

	public void setCurrentCharacter(RolePlayCharacterPOJO character) {
		this.currentCharacter = character;
	}

	@Override
	public String toString() {
		return userName + " (" + lastName + ", " + firstName + ") | " + privileges;
	}
}
