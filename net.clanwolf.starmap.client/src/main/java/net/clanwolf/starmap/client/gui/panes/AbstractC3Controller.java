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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract JavaFX Controller
 *
 * @author Meldric
 */
public abstract class AbstractC3Controller implements Initializable, ActionCallBackListener {

	/**
	 * This flag is set if there is a warning in the dialog, e.g. "There is something to save!" or similar.
	 */
	protected boolean warningActive = false;
	protected Label labelWarningIcon;
	protected Label labelWarningText;
	protected String paneName = "";

	@FXML
	private AnchorPane anchorPane;

	public void setPaneName(String name) {
		this.paneName = name;
	}

	private void createGeneralControls() {
		Platform.runLater(() -> {
			InputStream is = this.getClass().getResourceAsStream("/icons/alert.png");
			Image image = new Image(is, 25, 25, false, false);
			ImageView view = new ImageView(image);

			labelWarningIcon = new Label("", view);
			labelWarningIcon.setLayoutX(505);
			labelWarningIcon.setLayoutY(455);
			labelWarningIcon.setVisible(false);
			labelWarningIcon.setMouseTransparent(true);

			labelWarningText = new Label("");
			labelWarningText.setLayoutX(480);
			labelWarningText.setLayoutY(420);
			labelWarningText.setPrefWidth(300);
			labelWarningText.setPrefHeight(56);
			labelWarningText.setVisible(true);
			labelWarningText.setMouseTransparent(true);

			FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), labelWarningText);
			fadeTransition.setFromValue(1.0);
			fadeTransition.setToValue(0.0);
			fadeTransition.setAutoReverse(true);
			fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
			fadeTransition.play();

			anchorPane.getChildren().addAll(labelWarningIcon, labelWarningText);
		});
	}

	/**
	 * Set strings
	 */
	public abstract void setStrings();

	protected void setWarningOn(boolean setModal) {
		if (!warningActive) {
			Platform.runLater(() -> { labelWarningIcon.setVisible(true); });
			warningOnAction();
			warningActive = true;
		}
		AbstractC3Pane currentPane = Nexus.getCurrentlyOpenedPane();
		if (currentPane != null && setModal) {
			currentPane.setModal(true);
		}
	}

	protected void setWarningOff() {
		Platform.runLater(() -> { labelWarningIcon.setVisible(false); });
		warningOffAction();
		warningActive = false;
		AbstractC3Pane currentPane = Nexus.getCurrentlyOpenedPane();
		if (currentPane != null) {
			currentPane.setModal(false);
		}
	}

	public abstract void warningOnAction();

	public abstract void warningOffAction();

	public abstract void addActionCallBackListeners();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		createGeneralControls();
	}

	/**
	 * Handle Action
	 *
	 * @param action Action
	 * @param o      Object
	 * @return boolean
	 */
	@Override
	public abstract boolean handleAction(ACTIONS action, ActionObject o);
}
