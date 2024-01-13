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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.map.starmap.tools;

import javafx.scene.layout.Pane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.StatusTextEntryActionObject;
import net.clanwolf.starmap.client.gui.panes.map.starmap.StarmapConfig;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.client.util.Internationalization;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonGrid;
import org.kynosarges.tektosyne.geometry.RegularPolygon;
import org.kynosarges.tektosyne.graph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GraphManager<T> implements GraphAgent<T> {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final Graph<T> graph;
	private final int maxCost;
	private final PointD maxWorld;
	private final double scaleCost;
	private final List<T> highlights = new ArrayList<>(2);
	private final Map<T, Integer> nodeCosts;
	private BOUniverse boUniverse = Nexus.getBoUniverse();

	GraphManager(Graph<T> graph, int maxCost, Pane output) {
		this.graph = graph;
		this.maxCost = maxCost;
		maxWorld = new PointD(output.getWidth(), output.getHeight());

		nodeCosts = new HashMap<>(graph.nodeCount());
		for (T node : graph.nodes()) {
			nodeCosts.put(node, 1);
		}
		scaleCost = maxWorld.x + maxWorld.y;
	}

	@SuppressWarnings("unused")
	List<T> runAStar(BOStarSystem sourceSystem, BOStarSystem targetSystem) {
		List<T> locations;

		final T source = findNode(sourceSystem);
		final T target = findNode(targetSystem);

		highlights.clear();
		highlights.add(source);
		highlights.add(target);

		// find best path from source to target
		final AStar<T> aStar = new AStar<>(graph);
		aStar.useWorldDistance = true;
		aStar.setRelativeLimit(30);
		final boolean success = aStar.findBestPath(this, source, target);
		locations = aStar.nodes();

		return locations;
	}

	Double getDistance(BOStarSystem sourceSystem, BOStarSystem targetSystem) {
		return graph.getDistance(findNode(sourceSystem), findNode(targetSystem)) / StarmapConfig.MAP_COORDINATES_MULTIPLICATOR;
	}

	@SuppressWarnings("unused")
	List<T> runCoverage(BOStarSystem ss) {
		List<T> locations;
		final T source = findSource(ss);

		// find all nodes reachable from source
		// (note: scaling maximum step cost for Subdivision)
		final Coverage<T> coverage = new Coverage<>(graph);
		final boolean success = coverage.findReachable(this, source, scaleCost * 10);
		locations = coverage.nodes();

		return locations;
	}

	@SuppressWarnings("unused")
	List<T> runFloodFill(BOStarSystem ss) {
		List<T> locations;
		final T source = findSource(ss);

		// find all nodes reachable from source node
		final FloodFill<T> floodFill = new FloodFill<>(graph);
		final Predicate<T> match = (p -> nodeCosts.get(p) <= maxCost / 2);
		final boolean success = floodFill.findMatching(match, source);
		locations = floodFill.nodes();

		return locations;
	}

	@SuppressWarnings("unused")
	List<T> runVisibility(BOStarSystem ss, double threshold) {
		List<T> locations;
		final T source = findSource(ss);

		// find all nodes visible from source node
		final Visibility<T> visibility = new Visibility<>(graph);
		visibility.setThreshold(threshold);
		final Predicate<T> isOpaque = (p -> nodeCosts.get(p) >= maxCost);
		//		final Predicate<T> isOpaque = (p -> false);
		final boolean success = visibility.findVisible(isOpaque, source, 0);
		locations = visibility.nodes();

		return locations;
	}

	@SuppressWarnings("unused")
	boolean setVertexNeighbors(boolean value) {
		if (!(graph instanceof final PolygonGrid grid)) return false;

		final RegularPolygon element = grid.element();
		if (element.sides != 4 || element.vertexNeighbors == value) return false;

		grid.setElement(new RegularPolygon(element.length, 4, element.orientation, value));
		return true;
	}

	@Override
	public boolean relaxedRange() {
		return false;
	}

	public boolean canMakeStepSystems(BOStarSystem s, BOStarSystem d) {
		final T source = findNode(s);
		final T target = findNode(d);
		return canMakeStep(source, target);
	}

	@Override
	public boolean canMakeStep(T source, T target) {

		// If this return false, the System can not be jumped to... but it also triggers a "visibility" barrier for
		// future jump calculations where the ship tries to "jump around" conflict areas where it could easily just
		// jump through... therefore, this needs to be solved by costs (see below)!

		BOStarSystem hoveredSys = boUniverse.getStarSystemByPoint((PointD) target);

		Long minJumpshipLevel = hoveredSys.getLevel();
		Long currentJumpshipLevel = boUniverse.currentlyDraggedJumpship.getLevel();
		Long currentJumpshipFactionId = boUniverse.currentlyDraggedJumpship.getJumpshipFaction();
		Long currentlyHoveredSystemFactionId = hoveredSys.getFactionId();

		double distance = graph.getDistance(source, target) / StarmapConfig.MAP_COORDINATES_MULTIPLICATOR;
		boolean inRange = distance <= 30;
		boolean isAttacked = hoveredSys.isCurrentlyUnderAttack();
		boolean isAttackedNextRound = hoveredSys.isNextRoundUnderAttack();
		boolean isActiveInPhase = hoveredSys.isActiveInPhase(Nexus.getCurrentSeasonMetaPhase());
		boolean isLockedByJumpship = hoveredSys.isLockedByJumpship();
		boolean isLockedByPreviousAttackCooldown = hoveredSys.isLockedByPreviousAttackCooldown();
		boolean isLevelAllowed = (minJumpshipLevel <= currentJumpshipLevel) || currentJumpshipFactionId.equals(currentlyHoveredSystemFactionId);
		// boolean withinCosts = nodeCosts.get(target) < maxCost;

		boolean r = inRange && isActiveInPhase && !isAttacked && !isAttackedNextRound && !isLockedByJumpship && !isLockedByPreviousAttackCooldown && isLevelAllowed;

		if (!r) {
			logger.info(hoveredSys.getName() + " is in range: " + inRange
				+ " / Active (phase): " + isActiveInPhase
				+ " / Attacked THIS round: " + isAttacked
				+ " / Attacked next round: " + isAttackedNextRound
				+ " / Locked by jumpship: " + isLockedByJumpship
				+ " / Locked by cooldown: " + isLockedByPreviousAttackCooldown
				+ " / Level allowed: " + isLevelAllowed);
		}

		//		BOStarSystem ssSource = boUniverse.getStarSystemByPoint((PointD) source);
		//		BOStarSystem ssTarget = boUniverse.getStarSystemByPoint((PointD) target);
		//		logger.info("Start: " + ssSource.getName() + " | Target: " + ssTarget.getName() + " (Distance: " + distance + ")");
		//		logger.info("minJumpshipLevel: " + minJumpshipLevel + " currentJumpshipLevel: " + currentJumpshipLevel);

		if (isLockedByJumpship) {
			if (hoveredSys.getStarSystemId().equals(boUniverse.currentlyDraggedJumpship.getCurrentSystemID())) {
				logger.info("Hovered same system we started from.");
			} else {
				hoveredSys.setBlockReason(1);
				StatusTextEntryActionObject seo = new StatusTextEntryActionObject(Internationalization.getString("starmap.systeminfo_cannotbeattacked_jumpshiplock"), true);
				seo.setFlash(true);
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(seo);
			}
		} else if (isLockedByPreviousAttackCooldown) {
			hoveredSys.setBlockReason(2);
			StatusTextEntryActionObject seo = new StatusTextEntryActionObject(Internationalization.getString("starmap.systeminfo_cannotbeattacked_cooldown"), true);
			seo.setFlash(true);
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(seo);
		} else if (!isLevelAllowed) {
			hoveredSys.setBlockReason(3);
			StatusTextEntryActionObject seo = new StatusTextEntryActionObject(Internationalization.getString("starmap.systeminfo_cannotbeattacked_level"), true);
			seo.setFlash(true);
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(seo);
		} else {
			hoveredSys.setBlockReason(0);
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject("", false));
		}

		return r;
	}

	@Override
	public boolean canOccupy(T target) {
		// boolean isAttacked = (boUniverse.getStarSystemByPoint((PointD)target)).isCurrentlyUnderAttack();
		// return !isAttacked;
		return true;
	}

	@Override
	public double getStepCost(T source, T target) {
		/*
		 * Subdivision graphs must scale step costs by world distance because Graph<T>
		 * requires that the step cost is never less than the getDistance result. Step costs
		 * must be multiplied with the scaling factor (and not added) so that multiple cheap
		 * steps are preferred to a single, more expensive step.
		 *
		 * 1. Using the current distance makes pathfinding sensitive to both world distance
		 *    and step cost. For best results, we would average out the step costs of source
		 *    and target. This corresponds exactly to the visible Voronoi region shading,
		 *    as Delaunay edges are always halved by region boundaries.
		 *
		 * 2. Using a fixed value that equals or exceeds the maximum possible distance
		 *    between any two nodes makes pathfinding sensitive only to assigned step costs.
		 *    This effectively replicates the behavior on a PolygonGrid.
		 */
		//		double distance = graph.getDistance(source, target);
		//		return (distance * (nodeCosts.get(source) + nodeCosts.get(target)) / 2);
		return (30 * (nodeCosts.get(source) + nodeCosts.get(target)) / 2);
		//		return scaleCost * nodeCosts.get(target);

		//		double distance = graph.getDistance(source, target) / Config.MAP_COORDINATES_MULTIPLICATOR;
		//		boolean inRange = distance <= 30;
		//		boolean isAttacked = (boUniverse.getStarSystemByPoint((PointD) target)).isCurrentlyUnderAttack();
		//		boolean isAttackedNextRound = (boUniverse.getStarSystemByPoint((PointD) target)).isNextRoundUnderAttack();
		//		boolean isActiveInPhase = (boUniverse.getStarSystemByPoint((PointD) target)).isActiveInPhase(Nexus.getCurrentSeasonMetaPhase());
		//		boolean isLockedByJumpship = (boUniverse.getStarSystemByPoint((PointD) target)).isLockedByJumpship();
		//		boolean isLockedByPreviousAttackCooldown = (boUniverse.getStarSystemByPoint((PointD) target)).isLockedByPreviousAttackCooldown();
		//		Long minJumpshipLevel = (boUniverse.getStarSystemByPoint((PointD) target)).getLevel();
		//		Long currentJumpshipLevel = boUniverse.currentlyDraggedJumpship.getLevel();
		//		Long currentJumpshipFactionId = boUniverse.currentlyDraggedJumpship.getJumpshipFaction();
		//		Long currentlyHoveredSystemFactionId = (boUniverse.getStarSystemByPoint((PointD) target)).getFactionId();
		//
		//		//		BOStarSystem ssSource = boUniverse.getStarSystemByPoint((PointD) source);
		//		//		BOStarSystem ssTarget = boUniverse.getStarSystemByPoint((PointD) target);
		//		//		logger.info("Start: " + ssSource.getName() + " | Target: " + ssTarget.getName() + " (Distance: " + distance + ")");
		//		//		logger.info("minJumpshipLevel: " + minJumpshipLevel + " currentJumpshipLevel: " + currentJumpshipLevel);
		//
		//		boolean isLevelAllowed = (minJumpshipLevel <= currentJumpshipLevel) || currentJumpshipFactionId.equals(currentlyHoveredSystemFactionId);
		//		boolean r = inRange && isActiveInPhase && !isAttacked && !isAttackedNextRound && !isLockedByJumpship && !isLockedByPreviousAttackCooldown && isLevelAllowed;
		//		// boolean withinCosts = nodeCosts.get(target) < maxCost;
		//
		//		if (!r) {
		//			logger.info("In range: " + inRange + " / " + "Active in phase: " + isActiveInPhase + " / " + "Attacked: " + isAttacked + " / " + "Attacked next round: " + isAttackedNextRound + " / " + "Locked by jumpship: " + isLockedByJumpship + " / " + "Locked by previous attack: " + isLockedByPreviousAttackCooldown + " / " + "Level allowed: " + isLevelAllowed);
		//		}
		//
		//		if (r) {
		//			return 1.0d; // The system is jumpable, return default costs
		//		} else {
		//			return 90.0d; // The system is locked, try to prevent ships from jumping there by setting costs high (but below max to prevent visibility barrier!)
		//		}
	}

	@Override
	public boolean isNearTarget(T source, T target, double distance) {
		//		double dist = graph.getDistance(source, target) / Config.MAP_COORDINATES_MULTIPLICATOR;
		//		return (distance < 30);
		return (distance == 0);
	}

	private T findNode(BOStarSystem ss) {
		PointD p = new PointD(ss.getScreenX(), ss.getScreenY());
		return graph.findNearestNode(p);
	}

	private T findSource(BOStarSystem ss) {
		T source;
		if (!highlights.isEmpty()) {
			source = highlights.get(0);
		} else {
			source = findNode(ss);
		}

		highlights.clear();
		highlights.add(source);

		return source;
	}
}
