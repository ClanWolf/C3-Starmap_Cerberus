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
package net.clanwolf.starmap.client.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class FontAwesomeIcon implements Icon {
	private static final Font awesome;
	private static final Object LOCK = new Object[0];

	private int size;
	private Integer paddingX;
	private Integer paddingY;
	private BufferedImage buffer;
	private BufferedImage bufferDisabled;

	private char code;
	private Color color = Color.BLACK;
	private Font font;

	static {
		try {
			InputStream stream = FontAwesomeIcon.class.getResourceAsStream("/de/socoto/client/app/common/ui/fontawesome-webfont.ttf");
			awesome = Font.createFont(Font.TRUETYPE_FONT, stream);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(awesome);
			stream.close();
		} catch (FontFormatException | IOException ffe) {
			throw new RuntimeException(ffe);
		}
	}

	public FontAwesomeIcon(char code) {
		this(code, 16);
	}

	public FontAwesomeIcon(char code, Color color) {
		this(code, 16, color, null, null);
	}

	public FontAwesomeIcon(char code, int size) {
		this(code, size, Color.BLACK, null, null);
	}

	public FontAwesomeIcon(char code, int size, Integer paddingX, Integer paddingY) {
		this(code, size, Color.BLACK, paddingX, paddingY);
	}

	public FontAwesomeIcon(char code, int size, Color color, Integer paddingX, Integer paddingY) {
		this.code = code;
		this.color = color;
		this.paddingX = paddingX;
		this.paddingY = paddingY;
		setSize(size);
	}


	public void paintIcon(Component c, Graphics g, int x, int y) {
		synchronized (LOCK) {
			if (buffer == null) {
				buffer = new BufferedImage(getIconWidth(), getIconHeight(),
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D g2 = (Graphics2D) buffer.getGraphics();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setFont(font);
				g2.setColor(color);

				int sy = paddingY != null ? paddingY : size - (size / 4) + (size / 16);
				int sx = paddingX != null ? paddingX : 0;
				g2.drawString(String.valueOf(code), sx, sy);

				g2.dispose();
			}

			if (bufferDisabled == null) {
				bufferDisabled = new BufferedImage(getIconWidth(), getIconHeight(),
						BufferedImage.TYPE_INT_ARGB);

				Graphics2D g2 = (Graphics2D) bufferDisabled.getGraphics();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2.setFont(font);
				g2.setColor(color);

				int sy = paddingY != null ? paddingY : size - (size / 4) + (size / 16);
				int sx = paddingX != null ? paddingX : 0;
				g2.drawString(String.valueOf(code), sx, sy);

				g2.dispose();
			}

			// Center the Image
			int mx = c.getWidth() / 2 - size / 2 + 1;
			int my = c.getHeight() / 2 - size / 2 + 1;
			if (c.isEnabled()) {
				g.drawImage(buffer, mx, my, null);
			} else {
				g.drawImage(bufferDisabled, mx, my, null);
			}
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size > 0) {
			this.size = size;
			font = awesome.deriveFont(Font.PLAIN, size);
			synchronized (LOCK) {
				buffer = null;
			}
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		synchronized (LOCK) {
			buffer = null;
		}
	}

	public int getIconHeight() {
		return size;
	}

	public int getIconWidth() {
		return size;
	}
}
