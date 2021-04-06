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
package net.clanwolf.starmap.server.util;

import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.enums.SystemListTypes;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.transfer.dtos.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.json.simple.JSONValue;
import org.springframework.util.RouteMatcher;

import javax.persistence.EntityManager;
import java.io.*;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;

/**
 * Queries the database for starsystem data
 */
public class WebDataInterface {

	private static Map<String, String> selects = new HashMap<>();
	private static UniverseDTO universe;
	private static boolean initialized = false;

	public static UniverseDTO getUniverse() {
		return universe;
	}

	private static void initialize() {
		if (!initialized) {
			StringBuilder sb;

			sb = new StringBuilder();
			sb.append("SELECT \r\n");
			sb.append("         F.Name_en          AS name,             \r\n");
			sb.append("         F.ShortName        AS short,            \r\n");
			sb.append("         F.Color            AS color,            \r\n");
			sb.append("         F.Logo             AS logo,              \r\n");
			sb.append("         F.Id               AS id                \r\n");
			sb.append("FROM     FACTION                F;");
			selects.put(SystemListTypes.Factions.name(), sb.toString());

			sb = new StringBuilder();
			sb.append("SELECT \r\n");
			sb.append("         STS.ID              AS sid,              \r\n");
			sb.append("         STS.Name            AS name,             \r\n");
			sb.append("         STS.x               AS x,                \r\n");
			sb.append("         STS.y               AS y,                \r\n");
			//sb.append("         STS.z               AS z,                \r\n");
			sb.append("         STS.StarType1       AS startype1,        \r\n");
			sb.append("         STS.StarType2       AS startype2,        \r\n");
			sb.append("         STS.StarType3       AS startype3,        \r\n");
			sb.append("         STS.SarnaLinkSystem AS link,             \r\n");
			sb.append("         STS.SystemImageName AS systemImageName,  \r\n");
			sb.append("         F.ShortName         AS affiliation,      \r\n");
			//sb.append("         F.Name_en           AS faction_en,       \r\n");
			//sb.append("         F.Name_de           AS faction_de,       \r\n");
			//sb.append("         FT.ShortName        AS factiontype_short,\r\n");
			//sb.append("         FT.Name_en          AS factiontype_en,   \r\n");
			//sb.append("         FT.Name_de          AS factiontype_de,   \r\n");
			//sb.append("         SSD.ID              AS starsystemdataid, \r\n");
			//sb.append("         SSD.StarSystemID    AS starsystemID,     \r\n");
			sb.append("         SSD.FactionID       AS factionid,        \r\n");
			sb.append("         SSD.Infrastructure  AS infrastructure,   \r\n");
			sb.append("         SSD.Wealth          AS wealth,           \r\n");
			sb.append("         SSD.Veternacy       AS veternacy,        \r\n");
			sb.append("         SSD.Type            AS type,             \r\n");
			sb.append("         SSD.Class           AS class,            \r\n");
			sb.append("         SSD.Tonnage         AS tonnage,          \r\n");
			sb.append("         SSD.BattleValue     AS battlevalue,      \r\n");
			sb.append("         SSD.Description     AS description,      \r\n");
			sb.append("         SSD.S1_Map01ID      AS s1map1,           \r\n");
			sb.append("         SSD.S1_Map02ID      AS s1map2,           \r\n");
			sb.append("         SSD.S1_Map03ID      AS s1map3,           \r\n");
			sb.append("         SSD.S1_Map04ID      AS s1map4,           \r\n");
			sb.append("         SSD.S1_Map05ID      AS s1map5,           \r\n");
			sb.append("         SSD.S2_Map01ID      AS s2map1,           \r\n");
			sb.append("         SSD.S2_Map02ID      AS s2map2,           \r\n");
			sb.append("         SSD.S2_Map03ID      AS s2map3,           \r\n");
			sb.append("         SSD.S2_Map04ID      AS s2map4,           \r\n");
			sb.append("         SSD.S2_Map05ID      AS s2map5,           \r\n");
			sb.append("         SSD.S3_Map01ID      AS s3map1,           \r\n");
			sb.append("         SSD.S3_Map02ID      AS s3map2,           \r\n");
			sb.append("         SSD.S3_Map03ID      AS s3map3,           \r\n");
			sb.append("         SSD.S3_Map04ID      AS s3map4,           \r\n");
			sb.append("         SSD.S3_Map05ID      AS s3map5,           \r\n");
			sb.append("         SSD.S4_Map01ID      AS s4map1,           \r\n");
			sb.append("         SSD.S4_Map02ID      AS s4map2,           \r\n");
			sb.append("         SSD.S4_Map03ID      AS s4map3,           \r\n");
			sb.append("         SSD.S4_Map04ID      AS s4map4,           \r\n");
			sb.append("         SSD.S4_Map05ID      AS s4map5            \r\n");
			//sb.append("         SSD.MapfileLink     AS mapfilelink       \r\n");
			sb.append("FROM     STARSYSTEM          STS,                 \r\n");
			sb.append("         _CM_STARSYSTEMDATA  SSD,                 \r\n");
			sb.append("         FACTION             F,                   \r\n");
			sb.append("         FACTIONTYPE         FT                   \r\n");
			sb.append("WHERE    STS.ID              = SSD.StarSystemID   \r\n");
			sb.append("AND      SSD.Active          = 1                  \r\n");
			sb.append("AND      SSD.FactionID       = F.ID               \r\n");
			sb.append("AND      F.FactionTypeID     = FT.ID;");
			selects.put(SystemListTypes.CM_StarSystems.name(), sb.toString());

			sb = new StringBuilder();
			sb.append("SELECT \r\n");
			sb.append("         STS.ID              AS sid,              \r\n");
			sb.append("         STS.Name            AS name,             \r\n");
			sb.append("         STS.x               AS x,                \r\n");
			sb.append("         STS.y               AS y,                \r\n");
			//sb.append("         STS.z               AS z,                \r\n");
			sb.append("         STS.StarType1       AS startype1,        \r\n");
			//sb.append("         STS.StarType2       AS startype2,        \r\n");
			//sb.append("         STS.StarType3       AS startype3,        \r\n");
			sb.append("         STS.SarnaLinkSystem AS link,             \r\n");
			sb.append("         STS.SystemImageName AS systemImageName,  \r\n");
			sb.append("         F.ShortName         AS affiliation,      \r\n");
			//sb.append("         F.Name_en           AS faction_en,       \r\n");
			//sb.append("         F.Name_de           AS faction_de,       \r\n");
			//sb.append("         FT.ShortName        AS factiontype_short,\r\n");
			//sb.append("         FT.Name_en          AS factiontype_en,   \r\n");
			//sb.append("         FT.Name_de          AS factiontype_de,   \r\n");
			sb.append("         SSD.ID              AS starsystemdataid, \r\n");
			//sb.append("         SSD.StarSystemID    AS starsystemID,     \r\n");
			sb.append("         SSD.FactionID       AS factionid,        \r\n");
			sb.append("         SSD.Infrastructure  AS infrastructure,   \r\n");
			sb.append("         SSD.Wealth          AS wealth,           \r\n");
			sb.append("         SSD.Veternacy       AS veternacy,        \r\n");
			sb.append("         SSD.Type            AS type,             \r\n");
			sb.append("         SSD.Class           AS class,            \r\n");
			sb.append("         SSD.Description     AS description,      \r\n");
			//sb.append("         SSD.S1_Map01ID      AS s1map1,           \r\n");
			//sb.append("         SSD.S1_Map02ID      AS s1map2,           \r\n");
			//sb.append("         SSD.S1_Map03ID      AS s1map3,           \r\n");
			//sb.append("         SSD.S2_Map01ID      AS s2map1,           \r\n");
			//sb.append("         SSD.S2_Map02ID      AS s2map2,           \r\n");
			//sb.append("         SSD.S2_Map03ID      AS s2map3,           \r\n");
			//sb.append("         SSD.S3_Map01ID      AS s3map1,           \r\n");
			//sb.append("         SSD.S3_Map02ID      AS s3map2,           \r\n");
			//sb.append("         SSD.S3_Map03ID      AS s3map3,           \r\n");
			sb.append("         SSD.CapitalWorld    AS capital           \r\n");
			sb.append("FROM     STARSYSTEM          STS,                 \r\n");
			sb.append("         _HH_STARSYSTEMDATA  SSD,                 \r\n");
			sb.append("         FACTION             F,                   \r\n");
			sb.append("         FACTIONTYPE         FT                   \r\n");
			sb.append("WHERE    STS.ID              = SSD.StarSystemID   \r\n");
			sb.append("AND      SSD.FactionID       = F.ID               \r\n");
			sb.append("AND      F.FactionTypeID     = FT.ID              \r\n");
			sb.append("AND      SSD.Active          = 1;");
			selects.put(SystemListTypes.HH_StarSystems.name(), sb.toString());

			sb = new StringBuilder();
			sb.append("SELECT \r\n");
			sb.append("         A.ID                       AS aid,                    \r\n");
			sb.append("         A.Season                   AS season,                 \r\n");
			sb.append("         A.Round                    AS round,                  \r\n");
			//sb.append("         A.Priority                 AS priority,               \r\n");
			sb.append("         A.StarSystemID             AS starsystem,             \r\n");
			sb.append("         A.StarSystemDataID         AS starsystemdata,         \r\n");
			sb.append("         A.AttackedFromStarSystemID AS attackedfromstarsystem, \r\n");
			sb.append("         A.AttackTypeID             AS attacktype,             \r\n");
			//sb.append("         A.FactionID_Attacker       AS attacker,               \r\n");
			sb.append("         A.FactionID_Defender       AS defender,               \r\n");
			sb.append("         A.JumpshipID               AS jumpship                \r\n");
			sb.append("FROM     _HH_ATTACK                 A;");
			selects.put(SystemListTypes.HH_Attacks.name(), sb.toString());

			sb = new StringBuilder();
			sb.append("SELECT \r\n");
			sb.append("         JS.ID                      AS jsid,                   \r\n");
			sb.append("         JS.JumpshipName            AS jumpshipName,           \r\n");
			sb.append("         JS.JumpshipFactionID       AS jumpshipFactionID,      \r\n");
			sb.append("         JS.StarSystemHistory       AS starHist,               \r\n");
			//sb.append("         JS.LastMovedInRound        AS lastMovedInRound,       \r\n");
			sb.append("         JS.AttackReady             AS attackReady             \r\n");
			sb.append("FROM     _HH_JUMPSHIP               JS;");
			selects.put(SystemListTypes.HH_Jumpships.name(), sb.toString());

			sb = new StringBuilder();
			sb.append("SELECT \r\n");
			sb.append("         RP.ID                      AS rpid,                   \r\n");
			sb.append("         RP.SeasonID                AS seasonID,               \r\n");
			sb.append("         RP.RoundID                 AS roundID,                \r\n");
			sb.append("         RP.JumpshipID              AS jumpshipID,             \r\n");
			sb.append("         RP.SystemID                AS systemID                \r\n");
			sb.append("FROM     _HH_ROUTEPOINT             RP;");
			selects.put(SystemListTypes.HH_Routepoints.name(), sb.toString());
		}
	}

	// CALLED FROM HEARTBEATTIMER
	public static void createSystemList(SystemListTypes type) {
		initialize();
		C3Logger.print("Starting with the creation of the system list: " + type.name());

		EntityManager manager = EntityManagerHelper.getEntityManager();
		manager.getTransaction().begin();

		Session session = manager.unwrap(Session.class);
		ResultObject result = session.doReturningWork(new ReturningWork<ResultObject>() {
			@Override
			public ResultObject execute(Connection conn) throws SQLException {
				// execute your SQL
				ResultSet rs = null;
				ResultObject resultObject = new ResultObject();
				String systemsList = null;
				try (PreparedStatement stmt = conn.prepareStatement(selects.get(type.name()), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
					rs = stmt.executeQuery();
					C3Logger.print("Select done...");

					if (universe == null) {
						universe = new UniverseDTO();
					}

					if (type == SystemListTypes.Factions) {
						universe.factions.clear();
						while (rs.next()) {
							FactionDTO f = new FactionDTO();
							f.setName(rs.getString("name"));
							f.setShortName(rs.getString("short"));
							f.setColor(rs.getString("color"));
							f.setLogo(rs.getString("logo"));
							f.setId(rs.getLong("id"));

							universe.factions.put(f.getShortName(), f);
						}
						C3Logger.print("Created universe classes (Factions)...");
					}

					if (type == SystemListTypes.HH_StarSystems) {
						universe.starSystems.clear();
						while (rs.next()) {
							StarSystemDTO ss = new StarSystemDTO();
							ss.setId(rs.getLong("sid"));
							ss.setName(rs.getString("name"));
							ss.setX(rs.getBigDecimal("x"));
							ss.setY(rs.getBigDecimal("y"));
							ss.setAffiliation(rs.getString("affiliation"));
							ss.setFactionId(rs.getLong("factionid"));
							ss.setStarType1(rs.getString("startype1"));
							ss.setStarClass(rs.getString("class"));
							ss.setSarnaLink(rs.getString("link"));
							ss.setInfrastructure(rs.getString("infrastructure"));
							ss.setWealth(rs.getString("wealth"));
							ss.setVeternacy(rs.getString("veternacy"));
							ss.setType(rs.getString("type"));
							ss.setSystemImageName(rs.getString("systemImageName"));
							ss.setStarSystemDataId(rs.getLong("starsystemdataid"));

							ss.setDescription(rs.getString("description"));
							ss.setCaptial(rs.getBoolean("capital"));

//							HashMap<String, String> maps = new HashMap<>();
//							maps.put("s1map1", rs.getString("s1map1"));
//							maps.put("s1map2", rs.getString("s1map2"));
//							maps.put("s1map3", rs.getString("s1map3"));
//							maps.put("s2map1", rs.getString("s2map1"));
//							maps.put("s2map2", rs.getString("s2map2"));
//							maps.put("s2map3", rs.getString("s2map3"));
//							maps.put("s3map1", rs.getString("s3map1"));
//							maps.put("s3map2", rs.getString("s3map2"));
//							maps.put("s3map3", rs.getString("s3map3"));
//							ss.setMaps(maps);

							universe.starSystems.put(ss.getId(), ss);
						}
						C3Logger.print("Created universe classes (StarSystems)...");
					}

					if (type == SystemListTypes.HH_Attacks) {
						universe.attacks.clear();
						while (rs.next()) {
							AttackDTO a = new AttackDTO();
							a.setId(rs.getLong("aid"));
							a.setSeason(rs.getInt("season"));
							a.setRound(rs.getInt("round"));
							a.setStarSystemID(rs.getLong("starsystem"));
							a.setStarSystemDataID(rs.getLong("starsystemdata"));
							a.setAttackTypeID(rs.getLong("attackType"));
							a.setAttackedFromStarSystemID(rs.getLong("attackedfromstarsystem"));
							a.setfactionID_Defender(rs.getLong("defender"));
							a.setJumpshipID(rs.getLong("jumpship"));

							universe.attacks.add(a);
						}
						C3Logger.print("Created universe classes (Attacks)...");
					}

					if (type == SystemListTypes.HH_Routepoints) {
						universe.routepoints.clear();
						while (rs.next()) {
							RoutePointDTO rp = new RoutePointDTO();
							rp.setId(rs.getLong("rpid"));
							rp.setSystemId(rs.getLong("systemID"));
							rp.setRoundId(rs.getLong("roundID"));
							rp.setSeasonId(rs.getLong("seasonID"));
							rp.setJumpshipId(rs.getLong("jumpshipID"));

							universe.routepoints.add(rp);
						}
						C3Logger.print("Created universe classes (Jumpships)...");
					}

					if (type == SystemListTypes.HH_Jumpships) {
						universe.jumpships.clear();
						while (rs.next()) {
							JumpshipDTO js = new JumpshipDTO();
							js.setID(rs.getLong("jsid"));
							js.setJumpshipName(rs.getString("jumpshipName"));
							js.setJumpshipFactionID(rs.getLong("jumpshipFactionID"));
							js.setStarSystemHistory(rs.getString("starHist"));
							//js.setLastMovedInRound(rs.getInt("lastMovedInRound"));
							js.setAttackReady(rs.getBoolean("attackReady"));

							universe.jumpships.put(js.getJumpshipName(), js);
						}
						C3Logger.print("Created universe classes (Jumpships)...");
					}

					universe.currentSeason = 1;
					universe.currentRound = 3;
					universe.currentDate = "01.01.3052";

					// create JSON representation
					rs.beforeFirst();
					systemsList = getJSONFromResultSet(rs, type.name(), true);
				} catch (Exception e) {
					C3Logger.exception("Error creating mapdata file", e);
				}
				resultObject.setResultList(systemsList);
				return resultObject;
			}
		});

//		File mapDataFile = null;
		File mapDataFileHH = null;
		File mapDataFileCM = null;

		String decodedPath = "";
		String systemsList = result.getResultList();
//		String filename = "";
		String filenameHH = "";
		String filenameCM = "";

		try {
//			String path = WebDataInterface.class.getProtectionDomain().getCodeSource().getLocation().getPath();

			String pathHH = null;
			String pathCM = null;

			OSCheck.OSType ostype=OSCheck.getOperatingSystemType();
			switch (ostype) {
				case Windows:
					// The server seems to be running on a local windows computer, so this is likely to be a
					// debugging environment!
					File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + ".ClanWolf.net_C3" + File.separator + "httpdocs");
					String dirpath = dir.getAbsolutePath();
					pathHH = dirpath + File.separator + "starmap_HH";
					pathCM = dirpath + File.separator + "starmap_CM";
					break;
				case Linux:
					pathHH = "/var/www/vhosts/clanwolf.net/httpdocs/starmap_HH";
					pathCM = "/var/www/vhosts/clanwolf.net/httpdocs/starmap_CM";
					break;
				case MacOS: break;
				case Other: break;
			}

			if (!(pathHH == null || pathCM == null)) {

				C3Logger.print("Writing json files for ChaosMarch to: " + pathCM);
				C3Logger.print("Writing json files for HammerHead to: " + pathHH);

//				decodedPath = URLDecoder.decode(path, "UTF-8");
//				File f = new File(decodedPath);
//				String parent = f.getParent();
//				filename = parent + File.separator + "mapdata_" + type.name() + ".json";
				filenameHH = pathHH + File.separator + "mapdata_" + type.name() + ".json";
				filenameCM = pathCM + File.separator + "mapdata_" + type.name() + ".json";

//				mapDataFile = new File(filename);
				mapDataFileHH = new File(filenameHH);
				mapDataFileCM = new File(filenameCM);

//				mapDataFile.mkdirs();
				mapDataFileHH.getParentFile().mkdirs();
				mapDataFileCM.getParentFile().mkdirs();

//				C3Logger.print("Wrote file: " + filename);
				C3Logger.print("Wrote file: " + filenameHH);
				C3Logger.print("Wrote file: " + filenameCM);
			}
		} catch (Exception e) {
			C3Logger.exception("Error creating mapdata file", e);
		}

//		if (mapDataFile != null) {
//			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFile))) {
//				br.write(systemsList);
//			} catch (IOException ioe) {
//				C3Logger.exception("Error creating mapdata file", ioe);
//			}
//		} else {
//			RuntimeException rte = new RuntimeException("Could not write file: " + filename);
//			C3Logger.exception("Error creating mapdata file", rte);
//			throw rte;
//		}

		if (mapDataFileHH != null && !"".equals(mapDataFileHH)) {
			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFileHH))) {
				br.write(systemsList);
			} catch (IOException ioe) {
				C3Logger.exception("Error creating mapdata file", ioe);
			}
		} else {
			RuntimeException rte = new RuntimeException("Could not write file: " + mapDataFileHH);
			C3Logger.exception("Error creating mapdata file", rte);
			throw rte;
		}

		if (mapDataFileCM != null && !"".equals(mapDataFileCM)) {
			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFileCM))) {
				br.write(systemsList);
			} catch (IOException ioe) {
				C3Logger.exception("Error creating mapdata file", ioe);
			}
		} else {
			RuntimeException rte = new RuntimeException("Could not write file: " + mapDataFileCM);
			C3Logger.exception("Error creating mapdata file", rte);
			throw rte;
		}

		manager.getTransaction().commit();
		manager.close();
	}

	public static String getJSONFromResultSet(ResultSet rs, String keyName) {
		return getJSONFromResultSet(rs, keyName, true);
	}

	public static String getJSONFromResultSet(ResultSet rs, String keyName, boolean returnPrettyPrintedResult) {
		Map<String, List<Map<String, Object>>> json = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		if (rs != null) {
			try {
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> columnMap = new HashMap<>();
					for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
						if (rs.getString(metaData.getColumnLabel(columnIndex)) != null) {
							String v = rs.getString(metaData.getColumnLabel(columnIndex));
							Double d = null;
							Integer i = null;
							try {
								d = Double.parseDouble(v);
								if (d % 1 == 0) {
									i = d.intValue();
								}
							} catch (NumberFormatException e) {
								// do nothing
							}
							if ("x".equals(metaData.getColumnLabel(columnIndex)) || "y".equals(metaData.getColumnLabel(columnIndex)) || "z".equals(metaData.getColumnLabel(columnIndex))) {
								columnMap.put(metaData.getColumnLabel(columnIndex), d);
							} else if (i != null) {
								columnMap.put(metaData.getColumnLabel(columnIndex), i);
							} else if (d != null) {
								columnMap.put(metaData.getColumnLabel(columnIndex), d);
							} else {
								columnMap.put(metaData.getColumnLabel(columnIndex), v);
							}
						} else {
							columnMap.put(metaData.getColumnLabel(columnIndex), "");
						}
					}
					list.add(columnMap);
				}
			} catch (SQLException e) {
				C3Logger.error("Selecting systems...", e);
			}
			json.put(keyName, list);
		} else {
			C3Logger.print("Resultset was empty!");
		}

		String result = JSONValue.toJSONString(json);
		String prettyPrintedResult = null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(Feature.INDENT_OUTPUT);
		Object mappedJson = null;
		try {
			mappedJson = mapper.readValue(result, Object.class);
			prettyPrintedResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mappedJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (returnPrettyPrintedResult) {
			return prettyPrintedResult;
		} else {
			return result;
		}
	}

	private static class ResultObject {
		private String resultList;

		public ResultObject() {
			// empty constructor
		}

		public String getResultList() {
			return resultList;
		}

		public void setResultList(String list) {
			resultList = list;
		}
	}
}
