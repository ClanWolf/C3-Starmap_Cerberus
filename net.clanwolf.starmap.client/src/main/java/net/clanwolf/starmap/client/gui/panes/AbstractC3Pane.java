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
package net.clanwolf.starmap.client.gui.panes;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.client.util.Tools;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * @author Meldric
 */
public abstract class AbstractC3Pane extends Pane {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final Polygon backgroundPolygon;
	private final Polygon borderPolygon;
	//	private final Line separatorLine;
	//	private final MouseFollowPane mouseFollowPane;
	private boolean isDisplayed = false;
	private boolean showsPlanetRotation;
	private boolean showMouseFollow;

	protected String paneName = "SET_ME_IN_PANE_CONSTRUCTOR";
	private static boolean modal = false;

	@SuppressWarnings("unused")
	public String getPaneName() {
		return paneName;
	}

	@SuppressWarnings("unused")
	public void setModal(boolean v) {
		modal = v;
	}

	@SuppressWarnings("unused")
	public boolean isModal() {
		return modal;
	}

	@SuppressWarnings("unused")
	protected AbstractC3Controller controller = null;

	@SuppressWarnings("unused")
	public boolean showsPlanetRotation() {
		return showsPlanetRotation;
	}

	@SuppressWarnings("unused")
	public void setShowsPlanetRotation(boolean show) {
		showsPlanetRotation = show;
	}

	@SuppressWarnings("unused")
	public boolean showsMouseFollow() {
		return showMouseFollow;
	}

	@SuppressWarnings("unused")
	public void setShowsMouseFollow(boolean show) {
		showMouseFollow = show;
	}

	@SuppressWarnings("unused")
	public boolean isDisplayed() {
		return isDisplayed;
	}

	@SuppressWarnings("unused")
	public void setIsDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

	@SuppressWarnings("unused")
	public AbstractC3Controller getController() {
		return controller;
	}

	@SuppressWarnings("unused")
	public AbstractC3Pane(String name, boolean showPlanets, boolean mouseFollow) {
		showsPlanetRotation = showPlanets;
		showMouseFollow = mouseFollow;
		this.setLayoutX(126);
		this.setLayoutY(55);
		this.setPrefWidth(852);
		this.setPrefHeight(501);

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource(name));
			Parent root = loader.load();
			this.getChildren().add(root);
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException while reading FXML.", e);
		}
		backgroundPolygon = new Polygon();
		backgroundPolygon.getPoints().addAll(48.0, 5.0, 847.0, 5.0, 847.0, 495.0, 5.0, 495.0, 5.0, 104.0, 48.0, 70.0);
		backgroundPolygon.setStroke(Color.web("#99f3ff"));
		backgroundPolygon.setFill(Color.DODGERBLUE);
		backgroundPolygon.setOpacity(0.0);
		backgroundPolygon.setMouseTransparent(true);
		backgroundPolygon.setCache(true);
		backgroundPolygon.setCacheHint(CacheHint.SPEED);

		borderPolygon = new Polygon();
		borderPolygon.getPoints().addAll(48.0, 5.0, 847.0, 5.0, 847.0, 495.0, 5.0, 495.0, 5.0, 104.0, 48.0, 70.0);
		borderPolygon.setStroke(Color.web("#699ecd"));
		borderPolygon.setFill(Color.TRANSPARENT);
		borderPolygon.setStrokeWidth(1);
		borderPolygon.setOpacity(0.0);
		borderPolygon.setMouseTransparent(true);
		borderPolygon.setCache(true);
		borderPolygon.setCacheHint(CacheHint.SPEED);

//		separatorLine = new Line();
//		separatorLine.setStartX();
//		separatorLine.setStartY();
//		separatorLine.setEndX();
//		separatorLine.setEndY();
//		separatorLine.setStroke(Color.web("#699ecd"));
//		separatorLine.setFill(Color.TRANSPARENT);
//		separatorLine.setStrokeWidth(1);
//		separatorLine.setOpacity(0.0);
//		separatorLine.setMouseTransparent(true);
//		separatorLine.setCache(true);
//		separatorLine.setCacheHint(CacheHint.SPEED);

		Line leftBorderLine = new Line();
		leftBorderLine.setStrokeWidth(3.6);
		leftBorderLine.setStroke(Color.web("#76b2e6"));
		leftBorderLine.setOpacity(0.3);
		leftBorderLine.setVisible(true);
		leftBorderLine.setLayoutX(160);
		leftBorderLine.setLayoutY(81);
		leftBorderLine.setStartX(-106);
		leftBorderLine.setStartY(-73);
		leftBorderLine.setEndX(-106);
		leftBorderLine.setEndY(411);

		Rectangle backgroundFrame = new Rectangle();
		backgroundFrame.setLayoutX(62);
		backgroundFrame.setLayoutY(14);
		backgroundFrame.setWidth(778);
		backgroundFrame.setHeight(473);
		backgroundFrame.setStrokeWidth(2);
		backgroundFrame.setStroke(Color.rgb(53, 89, 119, 0.6));
		backgroundFrame.setFill(Color.rgb(48, 82, 112, 0.3));

		if (showsPlanetRotation) {
			PlanetRotationPane planetRotationPane = new PlanetRotationPane();
			planetRotationPane.setMouseTransparent(true);
			planetRotationPane.setPickOnBounds(false);
			Rectangle r = new Rectangle(60, 60, 507, 438);
			r.setMouseTransparent(true);
			r.setPickOnBounds(false);
			planetRotationPane.clipProperty().set(r);
			this.getChildren().add(planetRotationPane);
			planetRotationPane.toBack();
		}

//		mouseFollowPane = new MouseFollowPane();
//		if (showMouseFollow) {
//			Rectangle r = new Rectangle(64, 16, 773, 478);
//			mouseFollowPane.clipProperty().set(r);
//			this.getChildren().add(mouseFollowPane);
//		}

		this.getChildren().add(backgroundPolygon);
		this.getChildren().add(borderPolygon);
		this.getChildren().add(leftBorderLine);
		this.getChildren().add(backgroundFrame);

		backgroundPolygon.toBack();
		borderPolygon.toBack();
		backgroundFrame.toBack();
//		mouseFollowPane.toBack();
	}

	public void paneCreation() {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("8");
		firePaneConstructionBeginsEvent();
		Tools.playGUICreationSound();

		Platform.runLater(() -> {
			this.setOpacity(1.0);

			// Fade in transition 01 (Background)
			FadeTransition fadeInTransition_01 = new FadeTransition(Duration.millis(50), backgroundPolygon);
			fadeInTransition_01.setFromValue(0.0);
			fadeInTransition_01.setToValue(0.2);
			fadeInTransition_01.setCycleCount(4);

			// Fade in transition 02 (Border)
			FadeTransition fadeInTransition_02 = new FadeTransition(Duration.millis(100), borderPolygon);
			fadeInTransition_02.setFromValue(0.0);
			fadeInTransition_02.setToValue(1.0);
			fadeInTransition_02.setCycleCount(2);

			// Transition sequence
			SequentialTransition sequentialTransition = new SequentialTransition();
			sequentialTransition.setOnFinished(event -> firePaneConstructionEvent());
			sequentialTransition.getChildren().addAll(fadeInTransition_01, fadeInTransition_02);
			sequentialTransition.setCycleCount(1);
			sequentialTransition.play();
			isDisplayed = true;
			Nexus.setCurrentlyOpenedPane(this);
		});
	}

	public void paneDestruction() {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("9");
		Tools.playGUIDestructionSound();

		Platform.runLater(() -> {
			// Fade out transition 01 (Background)
			FadeTransition FadeOutTransition_01 = new FadeTransition(Duration.millis(80), borderPolygon);
			FadeOutTransition_01.setFromValue(0.2);
			FadeOutTransition_01.setToValue(0.0);
			FadeOutTransition_01.setCycleCount(2);

			// Fade out transition 02 (Border)
			FadeTransition FadeOutTransition_02 = new FadeTransition(Duration.millis(50), this);
			FadeOutTransition_02.setFromValue(1.0);
			FadeOutTransition_02.setToValue(0.0);
			FadeOutTransition_02.setCycleCount(3);

			// Transition sequence
			ParallelTransition parallelTransition = new ParallelTransition();
			parallelTransition.setOnFinished(event -> firePaneDestructionEvent());
			parallelTransition.getChildren().addAll(FadeOutTransition_01, FadeOutTransition_02);
			parallelTransition.setCycleCount(1);
			parallelTransition.play();

			isDisplayed = false;
		});
	}

	public void firePaneConstructionBeginsEvent() {
		ActionManager.getAction(ACTIONS.PANE_CREATION_BEGINS).execute(this);
	}

	public void firePaneConstructionEvent() {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("41");
		ActionManager.getAction(ACTIONS.PANE_CREATION_FINISHED).execute(this);
	}

	public void firePaneDestructionEvent() {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("42");
		ActionManager.getAction(ACTIONS.PANE_DESTRUCTION_FINISHED).execute(this);
	}
}
