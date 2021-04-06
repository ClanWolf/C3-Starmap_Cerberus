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
package net.clanwolf.starmap.client.gui.panes.map.tools;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.gui.panes.map.Config;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.logging.C3Logger;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.*;
import java.util.logging.Level;

public class RouteCalculator {
	public static List<BOStarSystem> calculateRoute(BOStarSystem source, BOStarSystem destination) {
		Level originalLevel = C3Logger.getC3LogLevel();
		C3Logger.info("C3 log level: " + originalLevel);
		C3Logger.setC3LogLevel(java.util.logging.Level.FINEST);

		BOUniverse boUniverse = Nexus.getBoUniverse();
		List<BOStarSystem> calculatedRoute = new ArrayList<>();

		List<PointD> route = boUniverse.graphManager.runAStar(source, destination);
		if (route.size() > 0) {
			PointD p1 = route.get(0);
			BOStarSystem s1 = boUniverse.getStarSystemByPoint(p1);
			calculatedRoute.add(s1);
			C3Logger.info("--------------------------- [ Start Optimizing route ]");
			C3Logger.info("### Starting from " + s1.getName() + " (" + s1.getId() + ")");

			if (route.size() == 2) {
				PointD p2 = route.get(1);
				BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
				double distance12 = boUniverse.delaunaySubdivision.getDistance(p1, p2) / Config.MAP_COORDINATES_MULTIPLICATOR;
				calculatedRoute.add(s2);
			} else if (route.size() > 2) {
				// The route is here cleaned up
				// If there are multiple jumps within a range of 30 LY, they are reduced to one jump in order
				// to optimize the travel
				LinkedList<PointD> routeList = new LinkedList<PointD>(route);
				LinkedList<PointD> routeCleaned = new LinkedList<PointD>();

				// neuer versuch
				PointD jumpToPoint = null;
				LinkedList<PointD> removeList = new LinkedList<>();

				PointD pt1 = routeList.get(0);
				int cc = 1;
				boolean destinationReached = false;
				do {
					BOStarSystem st1 = boUniverse.getStarSystemByPoint(pt1);
					if (st1.getId() == destination.getId()) {
						destinationReached = true;
					} else {
						C3Logger.info("Starting from " + st1.getName());
						for (int c = cc; c < routeList.size(); c++) {
							PointD pt = routeList.get(c);
							double distance = boUniverse.delaunaySubdivision.getDistance(pt1, pt) / Config.MAP_COORDINATES_MULTIPLICATOR;

							C3Logger.info("Considering: " + boUniverse.getStarSystemByPoint(pt).getName());
							if (distance < 30) {
								removeList.add(pt);
								jumpToPoint = pt;
								C3Logger.info("-- Removing: " + boUniverse.getStarSystemByPoint(jumpToPoint).getName());
							} else {
								removeList.remove(jumpToPoint);
								C3Logger.info("-- Keeping: " + boUniverse.getStarSystemByPoint(jumpToPoint).getName());
								break;
							}
						}
					}
					if (!destinationReached) {
						routeList.removeAll(removeList);
						removeList.clear();
						pt1 = jumpToPoint;
						BOStarSystem st = boUniverse.getStarSystemByPoint(jumpToPoint);
						calculatedRoute.add(st);
						cc++;
					}
				} while (!destinationReached);

				C3Logger.info("--------------------------- [ End Optimizing route ]");
			}
		}

		C3Logger.info("Switching to previous log level: " + originalLevel);
		C3Logger.setC3LogLevel(originalLevel);

		return calculatedRoute;
	}

	private RouteCalculator() {
		// private constructor
	}
}
