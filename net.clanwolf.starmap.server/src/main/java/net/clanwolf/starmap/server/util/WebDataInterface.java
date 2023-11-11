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

import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.persistence.EntityConverter;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Queries the database for starsystem data
 */
public class
WebDataInterface {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	//	private static Map<String, String> selects = new HashMap<>();
	private static volatile UniverseDTO universe;
	//	private static volatile boolean initialized = false;
	private static volatile boolean universeCreationInProgress = false;

	public static synchronized UniverseDTO getUniverse() {
		return universe;
	}

	public static synchronized UniverseDTO initUniverse() {
		universeCreationInProgress = true;

		String pattern = "dd.MM.yyyy HH:mm:ss";
		DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern(pattern);

		Long season = GameServer.getCurrentSeason();
		Long round = RoundDAO.getInstance().findBySeasonId(season).getRound();
		Long roundPhase = RoundDAO.getInstance().findBySeasonId(season).getRoundPhase();
		//LocalDateTime currentRoundStartDateTime = RoundDAO.getInstance().findBySeasonId(season).getCurrentRoundStartDate().toLocalDateTime();

		LocalDateTime currentRoundStartDateTime = null;
		String currentRoundStartDateString = RoundDAO.getInstance().findBySeasonId(season).getCurrentRoundStartDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ServerNexus.patternTimestamp);
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

		universeCreationInProgress = false;
		return universe;
	}

	public static synchronized void loadAttacks(Long seasonId) {
		universe.attacks.clear();
		universe.attackStorys.clear();

		RoundPOJO roundPOJO = RoundDAO.getInstance().findBySeasonId(seasonId);

		AttackDAO dao = AttackDAO.getInstance();

		//		ArrayList<AttackPOJO> pojoList = dao.getOpenAttacksOfASeason(seasonId);
		ArrayList<AttackPOJO> pojoList = dao.getAllAttacksOfASeasonForRound(seasonId, roundPOJO.getRound());
		ArrayList<AttackPOJO> pojoList2 = dao.getAllAttacksOfASeasonForNextRound(seasonId, roundPOJO.getRound());
		pojoList.addAll(pojoList2);

		//		StringBuilder jsonString = new StringBuilder();

		for (AttackPOJO attackPOJO : pojoList) {
			AttackDTO dto = EntityConverter.convertpojo2dto(attackPOJO, AttackDTO.class);
			//getCharacterFromAttack(dto);
			universe.attacks.add(dto);
			//			jsonString.append(getJsonString(dto));

			assert dto != null;
			if (dto.getStoryID() != null) {
				RolePlayStoryPOJO rpPojo = RolePlayStoryDAO.getInstance().findById(ServerNexus.DUMMY_USERID, dto.getStoryID());
				ArrayList<RolePlayStoryPOJO> storyList = RolePlayStoryDAO.getInstance().getAllStoriesByStory(rpPojo.getStory());
				for (RolePlayStoryPOJO p : storyList) {
					RolePlayStoryDTO dtoStory = EntityConverter.convertpojo2dto(p, RolePlayStoryDTO.class);
					universe.attackStorys.put(p.getId(), dtoStory);
				}
			}
		}
		//		return jsonString.toString();
	}

	//	private static void getCharacterFromAttack(AttackDTO attack){
	//		AttackCharacterDAO dao = AttackCharacterDAO.getInstance();
	//		ArrayList<AttackCharacterDTO> dtoList = new ArrayList<AttackCharacterDTO>();
	//
	//		ArrayList<AttackCharacterPOJO> myList = dao.getCharactersFromAttack(attack.getId());
	//		for (AttackCharacterPOJO attackCharacterPOJO : myList) {
	//			AttackCharacterDTO dto = EntityConverter.convertpojo2dto(attackCharacterPOJO, AttackCharacterDTO.class);
	//			attack.getAttackCharList().add(dto);
	//		}
	//	}

	public static synchronized void loadJumpshipsAndRoutePoints() {
		universe.jumpships.clear();
		universe.routepoints.clear();

		JumpshipDAO dao = JumpshipDAO.getInstance();
		ArrayList<JumpshipPOJO> pojoList = dao.getAllJumpships();
		//		StringBuilder jsonString = new StringBuilder();

		for (JumpshipPOJO js : pojoList) {
			JumpshipDTO dto = EntityConverter.convertpojo2dto(js, JumpshipDTO.class);
			// Add Jumpship
			universe.jumpships.put(js.getJumpshipName(), dto);
			//			jsonString.append(getJsonString(dto));

			// add RoutePoints
			ArrayList<RoutePointPOJO> rpList = new ArrayList<>(js.getRoutepointList());
			for (RoutePointPOJO routePointPOJO : rpList) {
				universe.routepoints.add(EntityConverter.convertpojo2dto(routePointPOJO, RoutePointDTO.class));
			}
		}
		//		return jsonString.toString();
	}

	public static synchronized void loadFactions() {
		universe.factions.clear();

		FactionDAO dao = FactionDAO.getInstance();
		ArrayList<FactionPOJO> pojoList = dao.getAllFactions();
		//		StringBuilder jsonString = new StringBuilder();

		for (FactionPOJO f : pojoList) {
			FactionDTO dto = EntityConverter.convertpojo2dto(f, FactionDTO.class);
			universe.factions.put(f.getShortName(), dto);
			//			jsonString.append(getJsonString(dto));
		}
		//		return jsonString.toString();
	}

	public static synchronized void load_HH_StarSystemData() {
		universe.starSystems.clear();

		StarSystemDataDAO dao = StarSystemDataDAO.getInstance();
		ArrayList<StarSystemDataPOJO> pojoList = dao.getAll_HH_StarSystemData();
		//		StringBuilder jsonString = new StringBuilder();

		for (StarSystemDataPOJO f : pojoList) {
			StarSystemDataDTO dto = EntityConverter.convertpojo2dto(f, StarSystemDataDTO.class);
			universe.starSystems.put(f.getStarSystemID().getId(), dto);
			//			jsonString.append(getJsonString(dto));
		}
		//		return jsonString.toString();
	}

	public static synchronized void loadDiplomacy(Long seasonId) {
		universe.diplomacy.clear();

		RoundPOJO currentRound = RoundDAO.getInstance().findBySeasonId(GameServer.getCurrentSeason());

		DiplomacyDAO dao = DiplomacyDAO.getInstance();
		ArrayList<DiplomacyPOJO> pojoList = dao.getDiplomacyForSeason(seasonId);

		for (DiplomacyPOJO f : pojoList) {
			DiplomacyDTO dto = EntityConverter.convertpojo2dto(f, DiplomacyDTO.class);
			universe.diplomacy.add(dto);
		}
	}

	// CALLED FROM HEARTBEATTIMER
	//	public static synchronized void createSystemList(SystemListTypes type) {
	//		initialize();
	//		String systemsList = "";
	//		logger.info("Creating universe lists: " + type.name());

	//		getUniverse();

	//		if (type == SystemListTypes.HH_Attacks) {
	//			systemsList = loadAttacks();
	////			logger.info("Created universe classes (Attacks)...");
	//		}
	//		if (type == SystemListTypes.HH_Jumpships) {
	//			systemsList = loadJumpshipsAndRoutePoints();
	////			logger.info("Created universe classes (Jumpships)...");
	//		}
	//		if (type == SystemListTypes.Factions) {
	//			systemsList = loadFactions();
	////			logger.info("Created universe classes (Factions)...");
	//		}
	//		if (type == SystemListTypes.HH_StarSystems) {
	//			systemsList = load_HH_StarSystemData();
	////			logger.info("Created universe classes (HH_StarSystems)...");
	//		}

	//		if (type == SystemListTypes.CM_StarSystems) {
	//			EntityManager manager = EntityManagerHelper.getNewEntityManager();
	//			manager.getTransaction().begin();
	//
	//			Session session = manager.unwrap(Session.class);
	//			ResultObject result = session.doReturningWork(conn -> {
	//				// execute your SQL
	//				ResultSet rs;
	//				ResultObject resultObject = new ResultObject();
	//				String systemsList1 = null;
	//				try (PreparedStatement stmt = conn.prepareStatement(selects.get(type.name()), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
	//					rs = stmt.executeQuery();
	////					logger.info("Select done...");
	//
	//					// create JSON representation
	//					rs.beforeFirst();
	//					systemsList1 = getJSONFromResultSet(rs, type.name(), true);
	//				} catch (Exception e) {
	//					logger.error("Error creating mapdata file", e);
	//				}
	//
	//				resultObject.setResultList(systemsList1);
	//				return resultObject;
	//			});
	//
	//			systemsList = result.getResultList();
	//
	//			manager.getTransaction().commit();
	//			manager.close();
	//		}

	////		File mapDataFile = null;
	//		File mapDataFileHH = null;
	//		File mapDataFileCM = null;
	//
	//		String decodedPath = "";
	////		String systemsList = result.getResultList();
	////		String filename = "";
	//		String filenameHH;
	//		String filenameCM;

	//		try {
	////			String path = WebDataInterface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	//
	//			String pathHH = null;
	//			String pathCM = null;
	//
	//			OSCheck.OSType ostype=OSCheck.getOperatingSystemType();
	//			switch (ostype) {
	//				case Windows:
	//					// The server seems to be running on a local windows computer, so this is likely to be a
	//					// debugging environment!
	//					File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + ".ClanWolf.net_C3" + File.separator + "httpdocs");
	//					String dirpath = dir.getAbsolutePath();
	//					pathHH = dirpath + File.separator + "starmap_HH";
	//					pathCM = dirpath + File.separator + "starmap_CM";
	//					break;
	//				case Linux:
	//					pathHH = "/var/www/vhosts/clanwolf.net/httpdocs/starmap_HH";
	//					pathCM = "/var/www/vhosts/clanwolf.net/httpdocs/starmap_CM";
	//					break;
	//				case MacOS: break;
	//				case Other: break;
	//			}
	//
	//			if (!(pathHH == null || pathCM == null)) {
	//
	////				logger.info("Writing json files for ChaosMarch to: " + pathCM);
	////				logger.info("Writing json files for HammerHead to: " + pathHH);
	//
	////				decodedPath = URLDecoder.decode(path, "UTF-8");
	////				File f = new File(decodedPath);
	////				String parent = f.getParent();
	////				filename = parent + File.separator + "mapdata_" + type.name() + ".json";
	//				filenameHH = pathHH + File.separator + "mapdata_" + type.name() + ".json";
	//				filenameCM = pathCM + File.separator + "mapdata_" + type.name() + ".json";
	//
	////				mapDataFile = new File(filename);
	//				mapDataFileHH = new File(filenameHH);
	//				mapDataFileCM = new File(filenameCM);
	//
	////				mapDataFile.mkdirs();
	//				mapDataFileHH.getParentFile().mkdirs();
	//				mapDataFileCM.getParentFile().mkdirs();
	//
	////				logger.info("Wrote file: " + filename);
	////				logger.info("Wrote file: " + filenameHH);
	////				logger.info("Wrote file: " + filenameCM);
	//			}
	//		} catch (Exception e) {
	//			logger.error("Error creating mapdata file", e);
	//		}

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

	//		if (mapDataFileHH != null && !"".equals(mapDataFileHH)) {
	//			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFileHH))) {
	//				br.write(systemsList);
	//			} catch (IOException ioe) {
	//				logger.error("Error creating mapdata file", ioe);
	//			}
	//		} else {
	//			RuntimeException rte = new RuntimeException("Could not write file: " + mapDataFileHH);
	//			logger.error("Error creating mapdata file", rte);
	//			throw rte;
	//		}

	//		if (mapDataFileCM != null && !"".equals(mapDataFileCM)) {
	//			try (BufferedWriter br = new BufferedWriter(new FileWriter(mapDataFileCM))) {
	//				br.write(systemsList);
	//			} catch (IOException ioe) {
	//				logger.error("Error creating mapdata file", ioe);
	//			}
	//		} else {
	//			RuntimeException rte = new RuntimeException("Could not write file: " + mapDataFileCM);
	//			logger.error("Error creating mapdata file", rte);
	//			throw rte;
	//		}

	//	}

	//	public static String getJSONFromResultSet(ResultSet rs, String keyName) {
	//		return getJSONFromResultSet(rs, keyName, true);
	//	}

	//	public static String getJSONFromResultSet(ResultSet rs, String keyName, boolean returnPrettyPrintedResult) {
	//		Map<String, List<Map<String, Object>>> json = new HashMap<>();
	//		List<Map<String, Object>> list = new ArrayList<>();
	//		if (rs != null) {
	//			try {
	//				ResultSetMetaData metaData = rs.getMetaData();
	//				while (rs.next()) {
	//					Map<String, Object> columnMap = new HashMap<>();
	//					for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
	//						if (rs.getString(metaData.getColumnLabel(columnIndex)) != null) {
	//							String v = rs.getString(metaData.getColumnLabel(columnIndex));
	//							Double d = null;
	//							Integer i = null;
	//							try {
	//								d = Double.parseDouble(v);
	//								if (d % 1 == 0) {
	//									i = d.intValue();
	//								}
	//							} catch (NumberFormatException e) {
	//								// do nothing
	//							}
	//							if ("x".equals(metaData.getColumnLabel(columnIndex)) || "y".equals(metaData.getColumnLabel(columnIndex)) || "z".equals(metaData.getColumnLabel(columnIndex))) {
	//								columnMap.put(metaData.getColumnLabel(columnIndex), d);
	//							} else if (i != null) {
	//								columnMap.put(metaData.getColumnLabel(columnIndex), i);
	//							} else if (d != null) {
	//								columnMap.put(metaData.getColumnLabel(columnIndex), d);
	//							} else {
	//								columnMap.put(metaData.getColumnLabel(columnIndex), v);
	//							}
	//						} else {
	//							columnMap.put(metaData.getColumnLabel(columnIndex), "");
	//						}
	//					}
	//					list.add(columnMap);
	//				}
	//			} catch (SQLException e) {
	//				logger.error("Selecting systems...", e);
	//			}
	//			json.put(keyName, list);
	//		} else {
	//			logger.info("Resultset was empty!");
	//		}
	//
	//		String result = JSONValue.toJSONString(json);
	//		String prettyPrintedResult = null;
	//
	//		ObjectMapper mapper = new ObjectMapper();
	//		mapper.enable(Feature.INDENT_OUTPUT);
	//		Object mappedJson;
	//		try {
	//			mappedJson = mapper.readValue(result, Object.class);
	//			prettyPrintedResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mappedJson);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//		if (returnPrettyPrintedResult) {
	//			return prettyPrintedResult;
	//		} else {
	//			return result;
	//		}
	//	}

	//	private static class ResultObject {
	//		private String resultList;
	//
	//		public ResultObject() {
	//			// empty constructor
	//		}
	//
	//		public String getResultList() {
	//			return resultList;
	//		}
	//
	//		public void setResultList(String list) {
	//			resultList = list;
	//		}
	//	}
}
