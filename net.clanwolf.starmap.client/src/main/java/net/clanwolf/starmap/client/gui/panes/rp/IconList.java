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
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.InputStream;

public class IconList extends Pane {

	private ImageView isNextImage;
	private ImageView hasWarningImage;
	private ImageView rpTypeImage;
	private HBox pane;
	private static final Class<IconList> c = IconList.class;

	public IconList(ROLEPLAYENTRYTYPES rpTyp, boolean isNext, boolean hasWarning) {


		pane = new HBox(5.0);

		if (isNext) {
			// this.isNextImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPNextStepIcon.png"))));
			//this.isNextImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPNextStepIcon.png").getFile()))));
			//pane.getChildren().add(this.isNextImage);
			InputStream is = this.getClass().getResourceAsStream("/icons/RPNextStepIcon.png");
			pane.getChildren().add( new ImageView(new Image(is)));

		}

		if (hasWarning) {
			// this.hasWarningImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPWarningIcon.png"))));
			//this.hasWarningImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPWarningIcon.png").getFile()))));
			//pane.getChildren().add(this.hasWarningImage);
			InputStream is = this.getClass().getResourceAsStream("/icons/RPWarningIcon.png");
			pane.getChildren().add( new ImageView(new Image(is)));

		}

		if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1) {
			// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPEditIcon.png"))));
			//this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPEditIcon.png").getFile()))));
			//pane.getChildren().add(this.rpTypeImage);
			InputStream is = this.getClass().getResourceAsStream("/icons/RPEditIcon.png");
			pane.getChildren().add( new ImageView(new Image(is)));

		} else if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V2) {
			// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPTreeIcon.png"))));
			//this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPTreeIcon.png").getFile()))));
			//pane.getChildren().add(this.rpTypeImage);
			InputStream is = this.getClass().getResourceAsStream("/icons/RPTreeIcon.png");
			pane.getChildren().add( new ImageView(new Image(is)));

		} else if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V3) {
			// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPInputIcon.png"))));
			//this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPInputIcon.png").getFile()))));
			//pane.getChildren().add(this.rpTypeImage);
			InputStream is = this.getClass().getResourceAsStream("/icons/RPInputIcon.png");
			pane.getChildren().add( new ImageView(new Image(is)));

		} else if (rpTyp == ROLEPLAYENTRYTYPES.C3_RP_STEP_V4) {
			// this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File("D:/temp/icons/ok/RPDiceIcon.png"))));
			//this.rpTypeImage = new ImageView(new Image(new FileInputStream(new File(c.getClassLoader().getResource("icons/RPDiceIcon.png").getFile()))));
			//pane.getChildren().add(this.rpTypeImage);
			InputStream is = this.getClass().getResourceAsStream("/icons/RPDiceIcon.png");
			pane.getChildren().add( new ImageView(new Image(is)));

		}

		this.getChildren().add(pane);

	}
}
