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
package net.clanwolf.starmap.client.gui.panes;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Abstract JavaFX Controller
 *
 * @author Meldric
 */
public abstract class AbstractC3Controller implements Initializable, ActionCallBackListener {

	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * This flag is set if there is a warning in the dialog, e.g. "There is something to save!" or similar.
	 */
	protected boolean warningActive = false;
	protected Label labelWarningIcon;
	protected Label labelWarningText;
	protected String paneName = "";
	protected Button defaultButton;

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

	/**
	 * The default button is disabled as soon as the terminal textfield has focus
	 * in order to prevent the default button to be triggered if a command is sent
	 * by hitting enter in the terminal.
	 *
	 * @param v
	 */
	public void enableDefaultButton(boolean v) {
		if (defaultButton == null) {
			defaultButton = getDefaultButton();
		}
		if (defaultButton != null) {
			defaultButton.setDefaultButton(v);
			// logger.info("DefaultButton (" + defaultButton.getText() + ") enabled: " + v);
		}
		//else {
			// logger.info("DefaultButton not found");
		//}
	}

	public abstract void setFocus();

	protected Button getDefaultButton() {
		Button defaultButton = null;
		if (anchorPane != null) {
			for (int i = 0; i < anchorPane.getChildren().size(); i++) {
				if (anchorPane.getChildren().get(i) instanceof Button) {
					if (((Button) anchorPane.getChildren().get(i)).isDefaultButton()) {
						defaultButton = (Button) anchorPane.getChildren().get(i);
						break;
					}
				}
			}
		}
		return defaultButton;
	}

	private <T> List<T> getNodesOfType(Pane parent, Class<T> type) {
		List<T> elements = new ArrayList<>();
		for (Node node : parent.getChildren()) {
			if (node instanceof Pane) {
				elements.addAll(getNodesOfType((Pane) node, type));
			} else if (type.isAssignableFrom(node.getClass())) {
				//noinspection unchecked
				elements.add((T) node);
			}
		}
		return Collections.unmodifiableList(elements);
	}

	protected void disableContextMenusForEditFields() {
		if (anchorPane != null) {
			for (TextField tf : getNodesOfType(anchorPane, TextField.class)) {
				tf.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
					@Override
					public void handle(ContextMenuEvent event) {
						event.consume();
					}
				});
			}
		}
	}

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

		ActionManager.addActionCallbackListener(ACTIONS.ENABLE_DEFAULT_BUTTON, this);
		ActionManager.addActionCallbackListener(ACTIONS.DISABLE_DEFAULT_BUTTON, this);

		disableContextMenusForEditFields();
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
