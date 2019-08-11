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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.process.universe;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import net.clanwolf.starmap.transfer.dtos.JumpshipDTO;

import java.util.ArrayList;

public class BOJumpship {

	private JumpshipDTO jumpshipDTO;
	private ImageView jumpshipImage;
	private Line predictedRouteLine = null;
	public Group routeLines = null;

	@SuppressWarnings("unused")
	public Line getPredictedRouteLine() {
		if (predictedRouteLine == null) {
			predictedRouteLine = new Line();
			predictedRouteLine.setStrokeWidth(3);
			predictedRouteLine.setStroke(Color.ORANGE);
			predictedRouteLine.setStrokeLineCap(StrokeLineCap.ROUND);
		}
		return predictedRouteLine;
	}

	@SuppressWarnings("unused")
	public void setPredictedRouteLine(Line predictedRouteLine) {
		this.predictedRouteLine = predictedRouteLine;
	}

	@SuppressWarnings("unused")
	public ImageView getJumpshipImage() {
		return jumpshipImage;
	}

	@SuppressWarnings("unused")
	public void setJumpshipImage(ImageView jumpshipImage) {
		this.jumpshipImage = jumpshipImage;
	}

	public BOJumpship(JumpshipDTO jumpshipDTO) {
		this.jumpshipDTO = jumpshipDTO;
	}

	public Integer getCurrentSystemID() {
		return jumpshipDTO.getCurrentSystemID();
	}

	public String getShipName() {
		return jumpshipDTO.getShipName();
	}

	public boolean isCombatReady() { return jumpshipDTO.isCombatReady(); }

	public ArrayList<Integer> getStarSystemHistoryArray() { return jumpshipDTO.getStarSystemHistoryArray(); }
}
