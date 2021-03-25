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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteCalculator {
	public static List<BOStarSystem> calculateRoute(BOStarSystem source, BOStarSystem destination) {
		BOUniverse boUniverse = Nexus.getBoUniverse();

		List<BOStarSystem> calculatedRoute = new ArrayList<>();

		// Calculating the distance between current node and current node + 2 of the route.
		// If that distance is smaller than 30LYs, the second node will be removed.
		// This needs to be done to simulate jumps instead of going through all
		// nodes.
		List<PointD> route = boUniverse.graphManager.runAStar(source, destination);
		PointD p1 = route.get(0);
		BOStarSystem s1 = boUniverse.getStarSystemByPoint(p1);
		calculatedRoute.add(s1);
		C3Logger.info("---------------------------");
		C3Logger.info("### Starting from " + s1.getName() + " (" + s1.getId() + ")");

		if (route.size() == 2) {
			PointD p2 = route.get(1);
			BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
			double distance12 = boUniverse.delaunaySubdivision.getDistance(p1, p2) / Config.MAP_COORDINATES_MULTIPLICATOR;
			calculatedRoute.add(s2);
			C3Logger.info("### Ending on " + s2.getName() + " (" + s2.getId() + ") - " + distance12 + " LY jump");
			C3Logger.info("---------------------------");
		} else if (route.size() > 2) {
			for (int i=1; i<route.size(); i++) {
				PointD pt1 = route.get(i);
				BOStarSystem st1 = boUniverse.getStarSystemByPoint(pt1);
				calculatedRoute.add(st1);
			}

			
			// ^^^^^^^ Remove
			
			
			
			
			
			
//			List<PointD> routeCleaned = new ArrayList<PointD>();
//			while (route.size() > 2) {
//				boolean removeFirst = false;
//				PointD pt1 = route.get(0);
//				PointD pt2 = route.get(1);
//				PointD pt3 = route.get(2);
//				double distance12 = boUniverse.delaunaySubdivision.getDistance(pt1, pt2) / Config.MAP_COORDINATES_MULTIPLICATOR;
//				double distance23 = boUniverse.delaunaySubdivision.getDistance(pt2, pt3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//				double distance13 = boUniverse.delaunaySubdivision.getDistance(pt1, pt3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//
//				if (distance13 < 30) {
//					// distanz zum übernächsten Punkt ist < 30, die zwischenstation entfällt
//					routeCleaned.add(route.get(0));
//					// tempRouteNew.add(2); ist beim nächsten Schritt dann 0
//				} else {
//					// distanz zum übernächsten Punkt ist > 30, die Stationen werden gebraucht
//					routeCleaned.add(route.get(0));
//					routeCleaned.add(route.get(1));
//					// tempRouteNew.add(2); ist beim nächsten Schritt dann 0
//					removeFirst = true;
//				}
//				if (removeFirst) {
//					route.remove(0);
//				}
//				route.remove(1);
//			}
//			// may need to add the last systems here!
//			for (int i = 0; i < routeCleaned.size(); i++) {
//				PointD p = routeCleaned.get(i);
//				BOStarSystem s = boUniverse.getStarSystemByPoint(p);
//				if (!calculatedRoute.contains(s)) {
//					calculatedRoute.add(s);
//					C3Logger.info("### Jumping over " + s.getName() + " (" + s.getId() + ")");
//				}
//			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			

//			PointD p2 = route.get(1);
//			PointD p3 = route.get(2);
//			BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
//			BOStarSystem s3 = boUniverse.getStarSystemByPoint(p3);
//			double distance12 = boUniverse.delaunaySubdivision.getDistance(p1, p2) / Config.MAP_COORDINATES_MULTIPLICATOR;
//			double distance23 = boUniverse.delaunaySubdivision.getDistance(p2, p3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//			double distance13 = boUniverse.delaunaySubdivision.getDistance(p1, p3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//			if (distance13 > 30) {
//				if (!calculatedRoute.contains(s2)) {
//					calculatedRoute.add(s2);
//					C3Logger.info("### Jumping over " + s2.getName() + " (" + s2.getId() + ") - " + distance12 + " LY jump");
//				}
//			}
//			if (!calculatedRoute.contains(s3)) {
//				calculatedRoute.add(s3);
//			}
//			C3Logger.info("### Ending on " + s3.getName() + " (" + s3.getId() + ") - " + distance23 + " LY jump");
//			C3Logger.info("---------------------------");
		}

//		for (int i = 0; i < route.size() - 1; i++) {
//			for (int j = i; j < route.size(); j++) {
//				if (j + 3 < route.size()) {
//					PointD p2 = route.get(j + 1);
//					PointD p3 = route.get(j + 2);
//					PointD p4 = route.get(j + 3);
//					double distance14 = boUniverse.delaunaySubdivision.getDistance(p1, p4) / Config.MAP_COORDINATES_MULTIPLICATOR;
//
//					if (distance14 <= 30) {
//						C3Logger.info("Does this ever happen?");
//						BOStarSystem s4 = boUniverse.getStarSystemByPoint(p4);
//						throw new RuntimeException("Jumped over two systems in a route within 30LYs. Should not happen!");
////						if (calculatedRoute.contains(s3)) {
////							C3Logger.info("Removing previously added 2 (" + s3.getName() + ")");
////							calculatedRoute.remove(s3);
////						}
////						if (!calculatedRoute.contains(s4)) {
////							calculatedRoute.add(s4);
////							break;
////						}
//					}
//				}
//				if (j + 2 < route.size()) {
//					PointD p2 = route.get(j + 1);
//					PointD p3 = route.get(j + 2);
//					double distance12 = boUniverse.delaunaySubdivision.getDistance(p1, p2) / Config.MAP_COORDINATES_MULTIPLICATOR;
//					double distance23 = boUniverse.delaunaySubdivision.getDistance(p2, p3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//					double distance13 = boUniverse.delaunaySubdivision.getDistance(p1, p3) / Config.MAP_COORDINATES_MULTIPLICATOR;
//
//					C3Logger.info("Distance 1-2: " + distance12);
//					C3Logger.info("Distance 2-3: " + distance23);
//					C3Logger.info("Distance 1-3: " + distance13);
//
//					if (distance12 > 30) {
//						C3Logger.info("Distance from 1 to 2 is bigger than 30 LY!");
//					}
//					if (distance13 > 30) {
//						BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
//						if (!calculatedRoute.contains(s2)) {
//							calculatedRoute.add(s2);
//						}
//						BOStarSystem s3 = boUniverse.getStarSystemByPoint(p3);
//						if (!calculatedRoute.contains(s3)) {
//							calculatedRoute.add(s3);
//						}
//						C3Logger.info("Adding 2 and 3 (" + s2.getName() + ", " + s3.getName() + ")");
//						break;
//					} else {
//						BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
//						if (calculatedRoute.contains(s2)) {
//							C3Logger.info("Removing previously added 2 (" + s2.getName() + ")");
//							calculatedRoute.remove(s2);
//						}
//						BOStarSystem s3 = boUniverse.getStarSystemByPoint(p3);
//						if (!calculatedRoute.contains(s3)) {
//							calculatedRoute.add(s3);
//						}
//						i++;
//						C3Logger.info("Adding 3 (" + s3.getName() + ")");
//						break;
//					}
//				} else if (j + 2 == route.size()) {
//					C3Logger.info("Only one jump necessary.");
//					PointD p2 = route.get(j + 1);
//					BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
//					if (!calculatedRoute.contains(s2)) {
//						calculatedRoute.add(s2);
//					}
//					double distance = boUniverse.delaunaySubdivision.getDistance(p1, p2) / Config.MAP_COORDINATES_MULTIPLICATOR;
//					C3Logger.info("Distance 1-2: " + distance);
//					if (distance > 30) {
//						C3Logger.info("Distance from 1 to 2 is bigger than 30 LY!");
//					}
//					break;
//				}
//			}
//		}
		return calculatedRoute;
	}

	private RouteCalculator() {
		// private constructor
	}
}
