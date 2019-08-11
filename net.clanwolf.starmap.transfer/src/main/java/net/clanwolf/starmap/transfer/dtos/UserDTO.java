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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

import java.sql.Timestamp;

/**
 * UserPOJO entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@JsonIdentityInfo(
		scope= UserDTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class UserDTO extends Dto {

	// Fields

	public Long id;
	private String userName;
	private String userPassword;
	private String userEMail;
	private String userAvatar;
	private String firstName;
	private String lastName;
	private String location;
	private int zipcode;
	private String website;
	private long privileges;
	private Timestamp birthDate;
	private Timestamp joinDate;
	private Timestamp lastLogin;
	private Timestamp bannedUntil;
	private String bannReason;
	private Timestamp created;
	private Timestamp lastModified;
	private RolePlayCharacterDTO currentCharacter;

	// Constructors

	/** default constructor */
	public UserDTO() {
	}

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

	public RolePlayCharacterDTO getCurrentCharacter() {
		return this.currentCharacter;
	}

	public void setCurrentCharacter(RolePlayCharacterDTO character) {
		this.currentCharacter = character;
	}

	@Override
	public String toString() {
		return userName + " (" + lastName + ", " + firstName + ") | " + privileges;
	}
}
