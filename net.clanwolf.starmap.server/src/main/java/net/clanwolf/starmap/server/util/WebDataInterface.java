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
package net.clanwolf.starmap.server.util;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.enums.SystemListTypes;
import net.clanwolf.starmap.server.persistence.EntityConverter;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.Dto;
import net.clanwolf.starmap.transfer.dtos.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.hibernate.Session;
import org.json.simple.JSONValue;

import jakarta.persistence.EntityManagerFactory;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Queries the database for starsystem data
 */
public class WebDataInterface {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static Map<String, String> selects = new HashMap<>();
	private static UniverseDTO universe;
	private static boolean initialized = false;

	public static UniverseDTO getUniverse() {
		String pattern = "dd.MM.yyyy HH:mm:ss";
		DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern(pattern);

		Long season = GameServer.getCurrentSeason();
		Long round = RoundDAO.getInstance().findBySeasonId(season).getRound();
		Long roundPhase = RoundDAO.getInstance().findBySeasonId(season).getRoundPhase();
		//LocalDateTime currentRoundStartDateTime = RoundDAO.getInstance().findBySeasonId(season).getCurrentRoundStartDate().toLocalDateTime();

		LocalDateTime currentRoundStartDateTime = null;
		String currentRoundStartDateString = RoundDAO.getInstance().findBySeasonId(season).getCurrentRoundStartDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Nexus.patternTimestamp);
		currentRoundStartDateTime = LocalDateTime.parse(currentRoundStartDateString, formatter);



		String dateS = dateTimeformatter.format(currentRoundStartDateTime);
		SeasonPOJO seasonPOJO = (SeasonPOJO) SeasonDAO.getInstance().findById(SeasonPOJO.class, season);
		Long seasonMetaPhase = seasonPOJO.getMetaPhase();

		if (universe == null) {
			universe = new UniverseDTO();
		}

		universe.currentDate = dateS;
		universe.currentSeason = season.intValue();
		universe.currentSeasonMetaPhase = seasonMetaPhase.intValue();
		universe.currentSeasonStartDate = seasonPOJO.getStartDate();
		universe.currentSeasonStartDateRealYear = seasonPOJO.getStartDateRealYear();
		universe.currentRound = round.intValue();
		universe.currentRoundPhase = roundPhase.intValue();
		universe.currentRoundStartDateTime = currentRoundStartDateTime;
		universe.currentRoundEndDateTime = currentRoundStartDateTime.plusHours((int) (seasonPOJO.getDaysInRound() * 24));
		universe.numberOfDaysInRound = seasonPOJO.getDaysInRound();
		universe.maxNumberOfRoundsForSeason = seasonPOJO.getSerpentArrivalRound().intValue();

		return universe;
	}

	private static void initialize() {
		if (!initialized) {
			StringBuilder sb;

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

			/*sb = new StringBuilder();
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
			selects.put(SystemListTypes.HH_StarSystems.name(), sb.toString());*/
		}
	}

	private static String getJsonString(Dto dto){
		// Create json string
		ObjectMapper mapper = new ObjectMapper();
		//Convert object to JSON string and pretty print
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

		} catch (IOException e){
			logger.info("Error creating JSON string");
			return null;
		}
	}

	private static String loadAttacks(){
		universe.attacks.clear();
		universe.attackStorys.clear();

		Long seasonId = 1L;
		RoundPOJO roundPOJO = RoundDAO.getInstance().findBySeasonId(seasonId);

		AttackDAO dao = AttackDAO.getInstance();












//		ArrayList<AttackPOJO> pojoList = dao.getOpenAttacksOfASeason(seasonId);
		ArrayList<AttackPOJO> pojoList = dao.getAllAttacksOfASeasonForRound(seasonId, roundPOJO.getRound());
		ArrayList<AttackPOJO> pojoList2 = dao.getAllAttacksOfASeasonForNextRound(seasonId, roundPOJO.getRound());
		pojoList.addAll(pojoList2);










		Iterator<AttackPOJO> iter = pojoList.iterator();
		StringBuilder jsonString = new StringBuilder();

		while(iter.hasNext()) {
			AttackDTO dto = EntityConverter.convertpojo2dto(iter.next(), AttackDTO.class);
			//getCharacterFromAttack(dto);
			universe.attacks.add(dto);
			jsonString.append(getJsonString(dto));

			assert dto != null;
			if (dto.getStoryID() != null) {
				RolePlayStoryPOJO rpPojo = RolePlayStoryDAO.getInstance().findById(Nexus.DUMMY_USERID, dto.getStoryID());
				ArrayList<RolePlayStoryPOJO> storyList = RolePlayStoryDAO.getInstance().getAllStoriesByStory(rpPojo.getStory());
				for (RolePlayStoryPOJO p : storyList) {
					RolePlayStoryDTO dtoStory = EntityConverter.convertpojo2dto(p, RolePlayStoryDTO.class);
					universe.attackStorys.put(p.getId(), dtoStory);
				}
			}
		}
		return jsonString.toString();
	}

	private static void getCharacterFromAttack(AttackDTO attack){
		AttackCharacterDAO dao = AttackCharacterDAO.getInstance();
		ArrayList<AttackCharacterDTO> dtoList = new ArrayList<AttackCharacterDTO>();

		ArrayList<AttackCharacterPOJO> myList = dao.getCharacterFromAttack(attack.getId());
		for (AttackCharacterPOJO attackCharacterPOJO : myList) {
			AttackCharacterDTO dto = EntityConverter.convertpojo2dto(attackCharacterPOJO, AttackCharacterDTO.class);
			attack.getAttackCharList().add(dto);
		}
	}

	private static String loadJumpshipsAndRoutePoints(){
		universe.jumpships.clear();
		universe.routepoints.clear();

		JumpshipDAO dao = JumpshipDAO.getInstance();
		ArrayList<JumpshipPOJO> pojoList = dao.getAllJumpships();
		Iterator<JumpshipPOJO> iter = pojoList.iterator();
		StringBuilder jsonString = new StringBuilder();

		while(iter.hasNext()){
			JumpshipPOJO js = iter.next();
			JumpshipDTO dto = EntityConverter.convertpojo2dto(js, JumpshipDTO.class);
			// Add Jumpship
			universe.jumpships.put(js.getJumpshipName(),dto);
			jsonString.append(getJsonString(dto));

			// add RoutePoints
			ArrayList<RoutePointPOJO> rpList = new ArrayList<>(js.getRoutepointList());
			for (RoutePointPOJO routePointPOJO : rpList) {
				universe.routepoints.add(EntityConverter.convertpojo2dto(routePointPOJO, RoutePointDTO.class));
			}
		}
		return jsonString.toString();
	}

	private static String loadFactions(){
		universe.factions.clear();

		FactionDAO dao = FactionDAO.getInstance();
		ArrayList<FactionPOJO> pojoList = dao.getAllFactions();
		Iterator<FactionPOJO> iter = pojoList.iterator();
		StringBuilder jsonString = new StringBuilder();

		while(iter.hasNext()) {
			FactionPOJO f = iter.next();
			FactionDTO dto = EntityConverter.convertpojo2dto(f,FactionDTO.class);
			universe.factions.put(f.getShortName(),dto);
			jsonString.append(getJsonString(dto));
		}
		return jsonString.toString();
	}

	private static String load_HH_StarSystemData(){
		universe.starSystems.clear();

		StarSystemDataDAO dao =StarSystemDataDAO.getInstance();
		ArrayList<StarSystemDataPOJO> pojoList = dao.getAll_HH_StarSystemData();
		Iterator<StarSystemDataPOJO> iter = pojoList.iterator();
		StringBuilder jsonString = new StringBuilder();

		while(iter.hasNext()) {
			StarSystemDataPOJO f = iter.next();
			StarSystemDataDTO dto = EntityConverter.convertpojo2dto(f, StarSystemDataDTO.class);
			universe.starSystems.put(f.getStarSystemID().getId(),dto);
			jsonString.append(getJsonString(dto));
		}
		return jsonString.toString();
	}

	// CALLED FROM HEARTBEATTIMER
	public static void createSystemList(SystemListTypes type) {
		initialize();
		String systemsList = "";
//		logger.info("Creating universe lists: " + type.name());

		getUniverse();

		if (type == SystemListTypes.HH_Attacks) {
			systemsList = loadAttacks();
//			logger.info("Created universe classes (Attacks)...");
		}
		if (type == SystemListTypes.HH_Jumpships) {
			systemsList = loadJumpshipsAndRoutePoints();
//			logger.info("Created universe classes (Jumpships)...");
		}
		if (type == SystemListTypes.Factions) {
			systemsList = loadFactions();
//			logger.info("Created universe classes (Factions)...");
		}
		if (type == SystemListTypes.HH_StarSystems) {
			systemsList = load_HH_StarSystemData();
//			logger.info("Created universe classes (HH_StarSystems)...");
		}

		if (type == SystemListTypes.CM_StarSystems) {
			EntityManager manager = EntityManagerHelper.getNewEntityManager();
			manager.getTransaction().begin();

			Session session = manager.unwrap(Session.class);
			ResultObject result = session.doReturningWork(conn -> {
				// execute your SQL
				ResultSet rs;
				ResultObject resultObject = new ResultObject();
				String systemsList1 = null;
				try (PreparedStatement stmt = conn.prepareStatement(selects.get(type.name()), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
					rs = stmt.executeQuery();
//					logger.info("Select done...");

					// create JSON representation
					rs.beforeFirst();
					systemsList1 = getJSONFromResultSet(rs, type.name(), true);
				} catch (Exception e) {
					logger.error("Error creating mapdata file", e);
				}

				resultObject.setResultList(systemsList1);
				return resultObject;
			});

			systemsList = result.getResultList();

			manager.getTransaction().commit();
			manager.close();
		}

//		File mapDataFile = null;
		File mapDataFileHH = null;
		File mapDataFileCM = null;

		String decodedPath = "";
//		String systemsList = result.getResultList();
//		String filename = "";
		String filenameHH;
		String filenameCM;

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

//				logger.info("Writing json files for ChaosMarch to: " + pathCM);
//				logger.info("Writing json files for HammerHead to: " + pathHH);

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

//				logger.info("Wrote file: " + filename);
//				logger.info("Wrote file: " + filenameHH);
//				logger.info("Wrote file: " + filenameCM);
			}
		} catch (Exception e) {
			logger.error("Error creating mapdata file", e);
		}

//		if (mapDataFile != null) {
//			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFile))) {
//				br.write(systemsList);
//			} catch (IOException ioe) {
//				logger.error("Error creating mapdata file", ioe);
//			}
//		} else {
//			RuntimeException rte = new RuntimeException("Could not write file: " + filename);
//			logger.error("Error creating mapdata file", rte);
//			throw rte;
//		}

		if (mapDataFileHH != null && !"".equals(mapDataFileHH)) {
			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFileHH))) {
				br.write(systemsList);
			} catch (IOException ioe) {
				logger.error("Error creating mapdata file", ioe);
			}
		} else {
			RuntimeException rte = new RuntimeException("Could not write file: " + mapDataFileHH);
			logger.error("Error creating mapdata file", rte);
			throw rte;
		}

		if (mapDataFileCM != null && !"".equals(mapDataFileCM)) {
			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFileCM))) {
				br.write(systemsList);
			} catch (IOException ioe) {
				logger.error("Error creating mapdata file", ioe);
			}
		} else {
			RuntimeException rte = new RuntimeException("Could not write file: " + mapDataFileCM);
			logger.error("Error creating mapdata file", rte);
			throw rte;
		}

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
				logger.error("Selecting systems...", e);
			}
			json.put(keyName, list);
		} else {
			logger.info("Resultset was empty!");
		}

		String result = JSONValue.toJSONString(json);
		String prettyPrintedResult = null;

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(Feature.INDENT_OUTPUT);
		Object mappedJson;
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
