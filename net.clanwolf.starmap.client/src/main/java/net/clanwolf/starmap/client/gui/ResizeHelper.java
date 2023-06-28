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
package net.clanwolf.starmap.client.gui;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.nexus.Nexus;

//created by Alexander Berg

public class ResizeHelper {

	private static Double initialWidth = null;
	private static Double initialHeight = null;

	public static void addResizeListener(Stage stage) {
		ResizeListener resizeListener = new ResizeListener(stage);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
		stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
		ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
		for (Node child : children) {
			addListenerDeeply(child, resizeListener);
		}
	}

	public static void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
		node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
		node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
		node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
		if (node instanceof Parent) {
			Parent parent = (Parent) node;
			ObservableList<Node> children = parent.getChildrenUnmodifiable();
			for (Node child : children) {
				addListenerDeeply(child, listener);
			}
		}
	}

	static class ResizeListener implements EventHandler<MouseEvent> {
		private final Stage stage;
		private Cursor cursorEvent = Cursor.DEFAULT;
		private int border = 0;
		private double startX = 0;
		private double startY = 0;

		public ResizeListener(Stage stage) {
			this.stage = stage;
		}

		@Override
		public void handle(MouseEvent mouseEvent) {
			EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
			Scene scene = stage.getScene();

			double mouseEventX = mouseEvent.getSceneX(), mouseEventY = mouseEvent.getSceneY(), sceneWidth = scene.getWidth(), sceneHeight = scene.getHeight();

			if (mouseEvent.getTarget() instanceof Label label && label.getId() != null && label.getId().equals("ResizerControl")) {

				// We allow resize only on the resize area on the bottom right corner (South-East)
				cursorEvent = Cursor.SE_RESIZE;

				if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
					if (initialWidth == null) {
						initialWidth = stage.getWidth();
						Nexus.setInitialWidth(initialWidth);
					}
					if (initialHeight == null) {
						initialHeight = stage.getHeight();
						Nexus.setInitialHeight(initialHeight);
					}
					startX = stage.getWidth() - mouseEventX;
					startY = stage.getHeight() - mouseEventY;
				} else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
					if (!Cursor.DEFAULT.equals(cursorEvent)) {
						if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
							if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) {
								if (stage.getHeight() > initialHeight || mouseEventY < 0) {
									stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
									stage.setY(mouseEvent.getScreenY());
								}
							} else {
								if (stage.getHeight() > initialHeight || mouseEventY + startY - stage.getHeight() > 0) {
									stage.setHeight(mouseEventY + startY);
								}
							}
						}

						if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
							if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {
								if (stage.getWidth() > initialWidth || mouseEventX < 0) {
									stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
									stage.setX(mouseEvent.getScreenX());
								}
							} else {
								if (stage.getWidth() > initialWidth || mouseEventX + startX - stage.getWidth() > 0) {
									stage.setWidth(mouseEventX + startX);
								}
							}
						}
					}
				}
			}
		}
	}
}
