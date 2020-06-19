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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes;

import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import net.clanwolf.starmap.client.gui.MainFrame;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;

/**
 *
 * @author Meldric
 */
public class MouseFollowPane extends Pane implements ActionCallBackListener {

	private final Arc a = WaitAnimationPane.createArc(0.0f, 0.0f, 16.0f, 16.0f, 270.0f, 250.0f, Color.web("#ffffff"), 4, Duration.seconds(1));
	private final Label labelXValueScreen = new Label("");
	private final Label labelYValueScreen = new Label("");
	private final Label labelXValueUniverse = new Label("");
	private final Label labelYValueUniverse = new Label("");
	private final Label labelXOffset = new Label("");
	private final Label labelYOffset = new Label("");
	private final Label labelZoom = new Label("");
	private final Line lx = new Line();
	private final Line ly = new Line();
	private double panePosX = 0;
	private double panePosY = 0;
	private double translatedPanePosX = 0;
	private double translatedPanePosY = 0;
	private final double universePositionX = 0;
	private final double universePositionY = 0;
	private final double offsetX = 0;
	private final double offsetY = 0;
	private final double zoom = 0;
	private boolean displayMapFactors = false;

	private boolean blocked = false;
	private int followDelay = 0;
	private int followDelayMax = 15;

	/**
	 * Toggel display map factors
	 *
	 * @param v
	 *            boolean
	 */
	public void setDisplayMapFactors(boolean v) {
		displayMapFactors = v;
	}

	/**
	 * Sets the delay of the mouse follow animation. Higher values might lead to a stuttering appeal of the crosshairs.
	 * 
	 * @param value
	 */
	public void setFollowDelay(int value) {
		followDelayMax = value;
	}

	/**
	 * Constructor
	 */
	public MouseFollowPane() {
		labelXValueScreen.setLayoutX(70.0f);
		labelXValueScreen.setLayoutY(450.0f);
		labelXValueScreen.setTextFill(Color.web("#ffffff"));

		labelYValueScreen.setLayoutX(70.0f);
		labelYValueScreen.setLayoutY(462.0f);
		labelYValueScreen.setTextFill(Color.web("#ffffff"));

		labelXValueUniverse.setLayoutX(150.0f);
		labelXValueUniverse.setLayoutY(450.0f);
		labelXValueUniverse.setTextFill(Color.web("#ffffff"));

		labelYValueUniverse.setLayoutX(150.0f);
		labelYValueUniverse.setLayoutY(462.0f);
		labelYValueUniverse.setTextFill(Color.web("#ffffff"));

		labelXOffset.setLayoutX(230.0f);
		labelXOffset.setLayoutY(450.0f);
		labelXOffset.setTextFill(Color.web("#ffffff"));

		labelYOffset.setLayoutX(230.0f);
		labelYOffset.setLayoutY(462.0f);
		labelYOffset.setTextFill(Color.web("#ffffff"));

		labelZoom.setLayoutX(330.0f);
		labelZoom.setLayoutY(450.0f);
		labelZoom.setTextFill(Color.web("#ffffff"));

		lx.setStartX(0.0f);
		lx.setStartY(0.0f);
		lx.setEndX(1000.0f);
		lx.setEndY(0.0f);
		lx.setStrokeWidth(3);
		lx.setStroke(Color.web("#699ecd"));
		lx.setStrokeType(StrokeType.CENTERED);

		ly.setStartX(0.0f);
		ly.setStartY(0.0f);
		ly.setEndX(0.0f);
		ly.setEndY(600.0f);
		ly.setStrokeWidth(3);
		ly.setStroke(Color.web("#699ecd"));
		ly.setStrokeType(StrokeType.CENTERED);

		Group g = new Group();
		g.getChildren().add(a);
		g.getChildren().add(lx);
		g.getChildren().add(ly);
		Rectangle r = new Rectangle(64, 16, 773, 422);
		g.clipProperty().set(r);

		this.getChildren().add(g);
		this.getChildren().add(labelXValueScreen);
		this.getChildren().add(labelYValueScreen);
		this.getChildren().add(labelXValueUniverse);
		this.getChildren().add(labelYValueUniverse);
		this.getChildren().add(labelXOffset);
		this.getChildren().add(labelYOffset);
		this.getChildren().add(labelZoom);
		this.setMouseTransparent(true);
		this.setOpacity(0.3f);
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);

		mousePosition();

		ActionManager.addActionCallbackListener(ACTIONS.MOUSE_MOVED, this);
	}

	private void mousePosition() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				blocked = true;
				// x - 129
				// y - 58
				// are the distance between top left corner of the
				// stage and the pane where the follower is displayed in
				panePosX = MainFrame.mouseX - 129;
				panePosY = MainFrame.mouseY - 58;

				translatedPanePosX = panePosX - 61;
				translatedPanePosY = panePosY - 12;

				if (displayMapFactors) {
					// Calculate universe coordinates
				}

				// turning cirle
				a.relocate(panePosX - 16, panePosY - 16);

				ly.relocate(panePosX, lx.getEndY());
				lx.relocate(ly.getEndX(), panePosY);

				Platform.runLater(() -> {
					if ((translatedPanePosX >= 0.0f) && (translatedPanePosX <= 775.0f)) {
						labelXValueScreen.setText("Sc/X: " + translatedPanePosX + "");
					} else {
						labelXValueScreen.setText("Sc/X: **");
					}
					if ((translatedPanePosY >= 0.0f) && (translatedPanePosY <= 426.0f)) {
						labelYValueScreen.setText("Sc/Y: " + translatedPanePosY + "");
					} else {
						labelYValueScreen.setText("Sc/Y: **");
					}

					if (displayMapFactors) {
						labelXValueUniverse.setText("Un/X: " + universePositionX + "");
						labelYValueUniverse.setText("Un/Y: " + universePositionY + "");
						labelXOffset.setText("OffS/X: " + offsetX + "");
						labelYOffset.setText("OffS/Y: " + offsetY + "");
						labelZoom.setText("Zoom: " + zoom + "");
					} else {
						labelXValueUniverse.setText("Un/X: N/A");
						labelYValueUniverse.setText("Un/Y: N/A");
						labelXOffset.setText("OffS/X: N/A");
						labelYOffset.setText("OffS/Y: N/A");
						labelZoom.setText("Zoom: N/A");
					}
				});
				followDelay = 0;
				blocked = false;
			}
		});
	}

	/**
	 * Handle Actions
	 *
	 * @param action
	 * @param o
	 * @return
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
		case MOUSE_MOVED:
			followDelay++;
			if (followDelay > followDelayMax) {
				if (!blocked) {
					mousePosition();
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
}
