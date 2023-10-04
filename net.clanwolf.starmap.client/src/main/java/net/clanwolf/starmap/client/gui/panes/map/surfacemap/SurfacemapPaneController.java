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
package net.clanwolf.starmap.client.gui.panes.map.surfacemap;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class SurfacemapPaneController extends AbstractC3Controller implements ActionCallBackListener {

	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private BOStarSystem starSystem;
	private SurfacemapPannableCanvas canvas;
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button btClose;

	@FXML
	public void handleCloseButtonClick() {
		ActionManager.getAction(ACTIONS.CLOSE_SURFACE_MAP).execute();
	}

	public void initializeSurfaceMap() {
		Platform.runLater(() -> {
			canvas = new SurfacemapPannableCanvas();

			Image surfacemapBig = starSystem.getSurfacemapBig();
			ImageView mapBackground = new ImageView(surfacemapBig);

			canvas.getChildren().add(mapBackground);

			anchorPane.getChildren().add(canvas);
			btClose.toFront();
		});
	}

	public void setStarSystem(BOStarSystem sys) {
		this.starSystem = sys;
	}

	@Override
	public void setStrings() {

	}

	@Override
	public void setFocus() {

	}

	@Override
	public void warningOnAction() {

	}

	@Override
	public void warningOffAction() {

	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);

		// Added in AbstractC3Controller:
		// ActionManager.addActionCallbackListener(ACTIONS.ENABLE_DEFAULT_BUTTON, this);
		// ActionManager.addActionCallbackListener(ACTIONS.DISABLE_DEFAULT_BUTTON, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
	}

	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case CHANGE_LANGUAGE:
				break;
			case ENABLE_DEFAULT_BUTTON:
				enableDefaultButton(true);
				break;
			case DISABLE_DEFAULT_BUTTON:
				enableDefaultButton(false);
				break;
			default:
				break;
		}
		return true;
	}
}
