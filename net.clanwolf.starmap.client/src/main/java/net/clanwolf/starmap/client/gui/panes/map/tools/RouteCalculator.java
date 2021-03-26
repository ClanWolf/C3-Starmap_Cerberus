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
			} while(!destinationReached);







			// neuer versuch ende

//			int c = 1;
//			while (routeList.size() > 2) {
//				C3Logger.info("Pass " + c);
//				c++;
//				boolean removeFirst = false;
//				PointD pt1 = routeList.get(0);
//				PointD pt2 = routeList.get(1);
//				PointD pt3 = routeList.get(2);
//				BOStarSystem st1 = boUniverse.getStarSystemByPoint(pt1);
//				BOStarSystem st2 = boUniverse.getStarSystemByPoint(pt2);
//				BOStarSystem st3 = boUniverse.getStarSystemByPoint(pt3);
//				double distance12 = boUniverse.delaunaySubdivision.getDistance(pt1, pt2) / Config.MAP_COORDINATES_MULTIPLICATOR;
//				double distance23 = boUniverse.delaunaySubdivision.getDistance(pt2, pt3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//				double distance13 = boUniverse.delaunaySubdivision.getDistance(pt1, pt3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//
//				C3Logger.info("<-> from " + st1.getName() + " to " + st2.getName() + " " + distance12 + " LY.");
//				C3Logger.info("<-> from " + st2.getName() + " to " + st3.getName() + " " + distance23 + " LY.");
//				C3Logger.info("<-> from " + st1.getName() + " to " + st3.getName() + " " + distance13 + " LY.");
//
//				if (distance13 < 30) {
//					// distance to next but one is < 30, remove middle point
//					if (!routeCleaned.contains(routeList.get(0))) {
//						routeCleaned.add(routeList.get(0));
//						C3Logger.info("+ (<30/1) " + st1.getName());
//						C3Logger.info("- (<30/2) " + st2.getName());
//					}
//					if (!routeCleaned.contains(routeList.get(2))) {
//						routeCleaned.add(routeList.get(2));
//						C3Logger.info("+ (<30/3) " + st3.getName());
//					}
//					// if 4 is >30 from first, remove first so that calculation starts from 3
////					try {
////						PointD pt4 = routeList.get(3);
////						BOStarSystem st4 = boUniverse.getStarSystemByPoint(pt4);
////						double distance14 = boUniverse.delaunaySubdivision.getDistance(pt1, pt4) / Config.MAP_COORDINATES_MULTIPLICATOR;
////						if (distance14 > 30) {
////							removeFirst = true;
////						}
////					} catch(IndexOutOfBoundsException ioobe) {
////						// there are no further route points
////					}
//				} else {
//					// distance to next but one is > 30, middle point is needed
//					if (!routeCleaned.contains(routeList.get(0))) {
//						routeCleaned.add(routeList.get(0));
//						C3Logger.info("+ (>30/1) " + st1.getName());
//					}
//					if (!routeCleaned.contains(routeList.get(1))) {
//						routeCleaned.add(routeList.get(1));
//						C3Logger.info("+ (>30/2) " + st2.getName());
//					}
//					if (!routeCleaned.contains(routeList.get(2))) {
//						routeCleaned.add(routeList.get(2));
//						C3Logger.info("+ (>30/3) " + st3.getName());
//					}
//					removeFirst = true;
//				}
//				if (removeFirst) {
//					routeList.remove(0);
//				}
//				routeList.remove(1);
//			}
//
//			C3Logger.info("---------------------------");
//			C3Logger.info("vvv [ Begin Adding unoptimized last part ] - " + routeList.size());
//			for (int i = 0; i < routeList.size(); i++) {
//				PointD p = routeList.get(i);
//				BOStarSystem s = boUniverse.getStarSystemByPoint(p);
//				if (!routeCleaned.contains(p)) {
//					C3Logger.info("Adding: " + s.getName());
//					routeCleaned.add(p);
//				} else {
//					C3Logger.info("NOT adding: " + s.getName() + " (was already in route)");
//				}
//			}
//			C3Logger.info("^^^ [ End Adding unoptimized last part ] - " + routeList.size());
//
//			if (routeCleaned.size() > 2) {
//				C3Logger.info("---------------------------");
//				C3Logger.info("vvv [ Optimizing backwards ]");
//				PointD pt1 = routeCleaned.get(routeCleaned.size() - 1);
//				PointD pt2 = routeCleaned.get(routeCleaned.size() - 2);
//				PointD pt3 = routeCleaned.get(routeCleaned.size() - 3);
//				BOStarSystem st1 = boUniverse.getStarSystemByPoint(pt1);
//				BOStarSystem st2 = boUniverse.getStarSystemByPoint(pt2);
//				BOStarSystem st3 = boUniverse.getStarSystemByPoint(pt3);
//				double distance12 = boUniverse.delaunaySubdivision.getDistance(pt1, pt2) / Config.MAP_COORDINATES_MULTIPLICATOR;
//				double distance23 = boUniverse.delaunaySubdivision.getDistance(pt2, pt3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//				double distance13 = boUniverse.delaunaySubdivision.getDistance(pt1, pt3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//
//				C3Logger.info("<-> from " + st1.getName() + " to " + st2.getName() + " " + distance12 + " LY.");
//				C3Logger.info("<-> from " + st2.getName() + " to " + st3.getName() + " " + distance23 + " LY.");
//				C3Logger.info("<-> from " + st1.getName() + " to " + st3.getName() + " " + distance13 + " LY.");
//
//				if (distance13 < 30) {
//					routeCleaned.remove(pt2);
//					C3Logger.info("- " + st2.getName());
//				}
//				C3Logger.info("^^^ [ Optimizing from end backwards two steps ]");
//			}
//
//			for (int i = 0; i < routeCleaned.size(); i++) {
//				PointD p = routeCleaned.get(i);
//				BOStarSystem s = boUniverse.getStarSystemByPoint(p);
//				if (!calculatedRoute.contains(s)) {
//					calculatedRoute.add(s);
//				}
//			}
			C3Logger.info("--------------------------- [ End Optimizing route ]");
		}

		C3Logger.info("Switching to previous log level: " + originalLevel);
		C3Logger.setC3LogLevel(originalLevel);

		return calculatedRoute;
	}

	private RouteCalculator() {
		// private constructor
	}
}
