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
package net.clanwolf.starmap.client.gui.panes.map.tools;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.gui.panes.map.Config;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonGrid;
import org.kynosarges.tektosyne.geometry.RegularPolygon;
import org.kynosarges.tektosyne.graph.*;

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

	private BOUniverse boUniverse = Nexus.getBoUniverse();

	private final List<T> highlights = new ArrayList<>(2);
	private final Map<T, Integer> nodeCosts;

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
		final boolean success = visibility.findVisible(isOpaque, source, 0);
		locations = visibility.nodes();

		return locations;
	}

	@SuppressWarnings("unused")
	boolean setVertexNeighbors(boolean value) {
		if (!(graph instanceof final PolygonGrid grid))
			return false;

		final RegularPolygon element = grid.element();
		if (element.sides != 4 || element.vertexNeighbors == value)
			return false;

		grid.setElement(new RegularPolygon(element.length, 4, element.orientation, value));
		return true;
	}

	@Override
	public boolean relaxedRange() {
		return false;
	}

	@Override
	public boolean canMakeStep(T source, T target) {
		double distance = graph.getDistance(source, target) / Config.MAP_COORDINATES_MULTIPLICATOR;
		boolean inRange = distance < 30;
		boolean isAttacked = (boUniverse.getStarSystemByPoint((PointD)target)).isCurrentlyUnderAttack();
		boolean isAttackedNextRound = (boUniverse.getStarSystemByPoint((PointD)target)).isNextRoundUnderAttack();
		boolean isActiveInPhase = (boUniverse.getStarSystemByPoint((PointD)target)).isActiveInPhase(Nexus.getCurrentSeasonMetaPhase());
		boolean isLockedByJumpship = (boUniverse.getStarSystemByPoint((PointD)target)).isLockedByJumpship();
		boolean isLockedByPreviousAttackCooldown = (boUniverse.getStarSystemByPoint((PointD)target)).isLockedByPreviousAttackCooldown();
		// boolean withinCosts = nodeCosts.get(target) < maxCost;

		return inRange
				&& isActiveInPhase
				&& !isAttacked
				&& !isAttackedNextRound
				&& !isLockedByJumpship
				&& !isLockedByPreviousAttackCooldown;
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

	@Override
	public boolean isNearTarget(T source, T target, double distance) {
		return (distance == 0);
	}
}
