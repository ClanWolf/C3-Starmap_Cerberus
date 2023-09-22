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
package net.clanwolf.starmap.client.gui.panes.map.starmap.tools;

import net.clanwolf.starmap.client.gui.panes.map.starmap.Config;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import org.kynosarges.tektosyne.geometry.PointD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class RouteCalculator {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static List<BOStarSystem> calculateRoute(BOStarSystem source, BOStarSystem destination) {
		BOUniverse boUniverse = Nexus.getBoUniverse();
		List<BOStarSystem> calculatedRoute = new ArrayList<>();
		List<BOStarSystem> finalizedCalculatedRoute = new ArrayList<>();

		if (boUniverse.graphManager.getDistance(source, destination) <= 30) {
			logger.info("--------------------------- [ Direct jump ]");

			if (boUniverse.graphManager.canMakeStepSystems(source, destination)) {
				calculatedRoute.add(source);
				calculatedRoute.add(destination);
			}
		} else {
			List<PointD> route = boUniverse.graphManager.runAStar(source, destination);
			if (route.size() > 0) {
				PointD p1 = route.get(0);
				BOStarSystem s1 = boUniverse.getStarSystemByPoint(p1);
				calculatedRoute.add(s1);
				logger.info("--------------------------- [ Start Optimizing route ]");
				logger.info("### Starting from " + s1.getName() + " (" + s1.getId() + ")");

				if (route.size() == 2) {
					PointD p2 = route.get(1);
					BOStarSystem s2 = boUniverse.getStarSystemByPoint(p2);
					// double distance12 = boUniverse.delaunaySubdivision.getDistance(p1, p2) / Config.MAP_COORDINATES_MULTIPLICATOR;
					calculatedRoute.add(s2);
				} else if (route.size() > 2) {
					// The route is here cleaned up
					// If there are multiple jumps within a range of 30 LY, they are reduced to one jump in order
					// to optimize the travel
					LinkedList<PointD> routeList = new LinkedList<PointD>(route);

					PointD jumpToPoint = null;
					LinkedList<PointD> removeList = new LinkedList<>();

					PointD pt1 = routeList.get(0);
					int cc = 1;
					boolean destinationReached = false;

					//				if (boUniverse.graphManager.canMakeStep(routeList.getFirst(), routeList.getLast())) {
					//					BOStarSystem ssSource = boUniverse.getStarSystemByPoint(routeList.getFirst());
					//					BOStarSystem ssTarget = boUniverse.getStarSystemByPoint(routeList.getLast());
					//					boUniverse.graphManager.runVisibility(ssSource, 0.0d);
					//					// logger.info("Start: " + ssSource.getName() + " | Target: " + ssTarget.getName());
					//				}

					int count = 0;
					do {
						BOStarSystem st1 = boUniverse.getStarSystemByPoint(pt1);
						if (Objects.equals(st1.getId(), destination.getId())) {
							destinationReached = true;
						} else {
							logger.info("Starting from " + st1.getName());
							for (int c = cc; c < routeList.size(); c++) {
								PointD pt = routeList.get(c);
								double distance = boUniverse.delaunaySubdivision.getDistance(pt1, pt) / Config.MAP_COORDINATES_MULTIPLICATOR;

								logger.info("Considering: " + boUniverse.getStarSystemByPoint(pt).getName() + ". Distance to " + boUniverse.getStarSystemByPoint(pt1).getName() + ": " + distance);
								if (distance <= 30) {
									removeList.add(pt);
									jumpToPoint = pt;
									logger.info("-- Removing: " + boUniverse.getStarSystemByPoint(jumpToPoint).getName());
								} else {
									removeList.remove(jumpToPoint);
									logger.info("-- Keeping: " + boUniverse.getStarSystemByPoint(jumpToPoint).getName());
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

						count++;
					} while (!destinationReached && count < 50);

					if (!destinationReached) {
						// No route was found. What now?
						logger.info("No route was found!");
					}

					if (calculatedRoute.size() == 3) {
						double dist = boUniverse.graphManager.getDistance(calculatedRoute.get(0), calculatedRoute.get(2));
						if (dist <= 30) {
							calculatedRoute.remove(1);
						}
					}

					if (calculatedRoute.size() > 5) {
						for (int r = 0; r < calculatedRoute.size(); r++) {
							finalizedCalculatedRoute.add(calculatedRoute.get(r));
							if (r == 4) {
								break;
							}
						}
						calculatedRoute = finalizedCalculatedRoute;
					}
				}
				logger.info("--------------------------- [ End Optimizing route ]");
			}
		}
		return calculatedRoute;
	}

	private RouteCalculator() {
		// private constructor
	}
}
