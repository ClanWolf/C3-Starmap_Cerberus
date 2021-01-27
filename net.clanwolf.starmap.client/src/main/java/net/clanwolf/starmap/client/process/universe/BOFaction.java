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
package net.clanwolf.starmap.client.process.universe;

import javafx.scene.shape.Path;
import net.clanwolf.starmap.transfer.dtos.FactionDTO;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;

public class BOFaction {

	private FactionDTO factionDTO;
	private Path backgroundPath;
	private ArrayList<PointD[]> voronoiRegions = new ArrayList<>();

	public ArrayList<PointD[]> getVoronoiRegions() {
		return voronoiRegions;
	}

	public void setVoronoiRegions(ArrayList<PointD[]> voronoiRegions) {
		this.voronoiRegions = voronoiRegions;
	}

	public void addVoronoiRegion(PointD[] voronoiRegion) {
		this.voronoiRegions.add(voronoiRegion);
	}

	public Path getBackgroundPath() {
		return backgroundPath;
	}

	public void setBackgroundPath(Path backgroundPath) {
		this.backgroundPath = backgroundPath;
	}

	public BOFaction(FactionDTO factionDTO) {
		this.factionDTO = factionDTO;
	}

	public String getColor() { return factionDTO.getColor(); }
}
