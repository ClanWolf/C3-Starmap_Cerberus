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

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.gui.panes.map.Config;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import org.kynosarges.tektosyne.geometry.*;

public class VoronoiDelaunay {

	private static BOUniverse boUniverse;
//	private static ArrayList<VoronoiEdge> innerEdges = new ArrayList<>();

	public static Pane getAreas() {
		boUniverse = Nexus.getBoUniverse();

		final Pane borderPane = new Pane();
		PointD[] points = new PointD[boUniverse.starSystemBOs.size()];

		// paint the background circles
		int count = 0;
		for (BOStarSystem ss : boUniverse.starSystemBOs.values()) {
			PointD p = new PointD(ss.getScreenX(), ss.getScreenY());
			points[count] = p;
			count++;

			// create clipped circles to render the inner areas
			BOFaction faction = boUniverse.factionBOs.get(ss.getAffiliation());
			Path path;
			if (faction.getBackgroundPath() != null) {
				path = faction.getBackgroundPath();
			} else {
				path = new Path();
			}
			Circle fc = new Circle(p.x, p.y, Config.MAP_BACKGROUND_AREA_RADIUS - Config.MAP_BACKGROUND_AREA_RADIUS_BORDER_WIDTH);
			path = (Path) Path.union(path, fc);
			faction.setBackgroundPath(path);
		}

		final RectD clip = new RectD(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);
		boUniverse.voronoiResults = Voronoi.findAll(points, clip);

		// check what voronoi region contains the current star system
		for (PointD[] region : boUniverse.voronoiResults.voronoiRegions()) {
			for (BOStarSystem ss : boUniverse.starSystemBOs.values()) {
				PointD p = new PointD(ss.getScreenX(), ss.getScreenY());
				PolygonLocation location = GeoUtils.pointInPolygon(p, region);
				if ("INSIDE".equals(location.toString())) {
					BOFaction faction = boUniverse.factionBOs.get(ss.getAffiliation());
					ss.setVoronoiRegion(region);
					faction.addVoronoiRegion(region);
				}
			}
		}

		for (BOFaction faction : boUniverse.factionBOs.values()) {
			if (faction.getBackgroundPath() != null) {
				Shape shape = faction.getBackgroundPath();
				Shape regions = null;
				for (PointD[] region : faction.getVoronoiRegions()) {
					Polygon polygon = new Polygon(PointD.toDoubles(region));
					if (regions == null) {
						regions = polygon;
					} else {
						regions = Shape.union(regions, polygon);
					}
				}
				if (regions != null) {
					Shape factionBackground = Shape.intersect(shape, regions);
					String colorString = faction.getColor();
					Color color = Color.web(colorString);
					factionBackground.setFill(color.deriveColor(1,1,1,0.2));
					factionBackground.setStrokeWidth(Config.MAP_BACKGROUND_AREA_RADIUS_BORDER_WIDTH);
					factionBackground.setStrokeLineJoin(StrokeLineJoin.ROUND);
					factionBackground.setStroke(Config.MAP_BACKGROUND_AREA_BORDER_COLOR.deriveColor(.7, .7,.7, 1));
					borderPane.getChildren().add(factionBackground);
				}
			}
		}

		// draw edges of Voronoi diagram
//		for (VoronoiEdge edge : results.voronoiEdges) {
//			//  (e.lSite && e.rSite && e.lSite.type !== e.rSite.type)
//			if (edge.site1 != edge.site2) {
//				innerEdges.add(edge);
//			}
//
//			// paint the edges
//			final PointD start = results.voronoiVertices[edge.vertex1];
//			final PointD end = results.voronoiVertices[edge.vertex2];
//			final Line line = new Line(start.x, start.y, end.x, end.y);
//			line.setStroke(Color.DARKGRAY);
//			borderPane.getChildren().add(line);
//		}

		// draw edges of Delaunay triangulation
//		for (LineD edge : results.delaunayEdges()) {
//			final Line line = new Line(edge.start.x, edge.start.y, edge.end.x, edge.end.y);
//			line.getStrokeDashArray().addAll(3.0, 2.0);
//			line.setStroke(Color.BLUE);
//			borderPane.getChildren().add(line);
//		}

		boUniverse.delaunaySubdivision = boUniverse.voronoiResults.toDelaunaySubdivision(clip, true);
		boUniverse.graphManager = new GraphManager<>(boUniverse.delaunaySubdivision, 8, borderPane);

		return borderPane;
	}
}
