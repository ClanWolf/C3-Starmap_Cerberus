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
package net.clanwolf.starmap.client.util;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * This transition makes sprite animations possible (analog to animated gifs
 * with a series of images to represent movement).
 *
 * @author Meldric
 */
public class SpriteAnimation extends Transition {

	private final ImageView imageView;
	private final int count;
	private final int columns;
	private final int offsetX;
	private final int offsetY;
	private final int width;
	private final int height;

	private int lastIndex;

	/**
	 * Constructor
	 */
	public SpriteAnimation(ImageView imageView, Duration duration, int count, int columns, int offsetX, int offsetY,
	                       int width, int height) {
		this.imageView = imageView;
		this.count = count;
		this.columns = columns;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR);
	}

	/**
	 * Interpolate method
	 *
	 * @param k Value
	 */
	@Override
	protected void interpolate(double k) {
		final int index = Math.min((int) Math.floor(k * count), count - 1);
		if (index != lastIndex) {
			final int x = (index % columns) * width + offsetX;
			final int y = (index / columns) * height + offsetY;
			imageView.setViewport(new Rectangle2D(x, y, width, height));
			lastIndex = index;
		}
	}
}
