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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

import java.sql.Timestamp;

/**
 * UserSessionPOJO entity.
 * 
 * @author MyEclipse Persistence Tools
 */
//@JsonIdentityInfo(
//		scope= UserSessionPOJO.class,
//		generator=ObjectIdGenerators.PropertyGenerator.class,
//		property = "id")
//@Entity
//@Table(name = "USER_SESSION", catalog = "C3")
public class UserSessionDTO extends Dto {

	// Fields

//	@Id
//	@GeneratedValue(strategy = IDENTITY)
//	@Column(name = "ID", unique = true, nullable = false)
	public Long id;

//	@Column(name = "UserId", nullable = false)
	private Long userId;

//	@Column(name = "ClientVersion")
	private String clientVersion;

//	@Column(name = "IP")
	private String ip;

//	@Column(name = "lastActivity")
	private Timestamp lastActivity;

//	@Column(name = "LoginTime", nullable = true)
	private Timestamp loginTime;

//	@Column(name = "LogoutTime", nullable = true)
	private Timestamp logoutTime;
	// Constructors

	/** default constructor */
	public UserSessionDTO() {
	}

	// Property accessors

	@SuppressWarnings("unused")
	public Long getUserId() {
		return this.id;
	}

	@SuppressWarnings("unused")
	public void setUserId(Long id) {
		this.id = id;
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
	public String getIp() {
		return ip;
	}

	@SuppressWarnings("unused")
	public void setIp(String ip) {
		this.ip = ip;
	}

	@SuppressWarnings("unused")
	public Timestamp getLoginTime() {
		return loginTime;
	}

	@SuppressWarnings("unused")
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	@SuppressWarnings("unused")
	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	@SuppressWarnings("unused")
	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	@SuppressWarnings("unused")
	public Timestamp getLastActivity() {
		return lastActivity;
	}

	@SuppressWarnings("unused")
	public void setLastActivity(Timestamp lastActivity) {
		this.lastActivity = lastActivity;
	}

	@SuppressWarnings("unused")
	public String getClientVersion() {
		return clientVersion;
	}

	@SuppressWarnings("unused")
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
}
