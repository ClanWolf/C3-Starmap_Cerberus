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
package net.clanwolf.starmap.server.util;

import com.squareup.gifencoder.FloydSteinbergDitherer;
import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AnimatedGifCreator {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// https://genuinecoder.com/how-to-create-gif-from-multiple-images-in-java/

	public static void main(String[] args) {
		createSeasonHistoryAnimation(1L);
	}

	public static void createSeasonHistoryAnimation(Long season) {
		Runnable runnable = () -> {
			String pathName = "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/seasonhistory/S" + season;
			//String pathName = "C:\\S" + season;
			File path = new File(pathName);

			int imageWidth = 1250;
			int imageheight = 1000;
			int imagePreviewWidth = 250;
			int imagePreviewHeight = 200;
			int frameDuration = 1500;

			ArrayList<File> imagesList = new ArrayList<>();
			ArrayList<File> imagesPreviewsList = new ArrayList<>();
			for (int i = 1; i < 500; i++) {
				String fileName = path + "/" + "C3_S" + season + "_R" + i + "_map_history.png";
				String fileNamePreview = path + "/" + "C3_S" + season + "_R" + i + "_map_history_preview.png";
				File f = new File(fileName);
				File fp = new File(fileNamePreview);
				if (f.isFile() && f.canRead()) {
					logger.info("Found image: " + f.getAbsolutePath());
					imagesList.add(f);
				}
				if (fp.isFile() && fp.canRead()) {
					logger.info("Found preview image: " + fp.getAbsolutePath());
					imagesPreviewsList.add(fp);
				}
			}

			ImageOptions options = new ImageOptions();
			options.setDelay(frameDuration, TimeUnit.MILLISECONDS);
			options.setDitherer(FloydSteinbergDitherer.INSTANCE);

			try (FileOutputStream outputStream = new FileOutputStream(pathName + "/C3_S" + season + "_map_animated.gif")) {
				GifEncoder gifEncoder = new GifEncoder(outputStream, imageWidth, imageheight, 0);
				for (File file : imagesList) {
					gifEncoder.addImage(convertImageToArray(file, imageWidth, imageheight), options);
				}
				gifEncoder.finishEncoding();
				logger.info("Finished map image.");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error during animated season image creation: " + e);
			}

			try (FileOutputStream outputStream = new FileOutputStream(pathName + "/C3_S" + season + "_map_animated_preview.gif")) {
				GifEncoder gifEncoder = new GifEncoder(outputStream, imagePreviewWidth, imagePreviewHeight, 0);
				for (File file : imagesPreviewsList) {
					gifEncoder.addImage(convertImageToArray(file, imagePreviewWidth, imagePreviewHeight), options);
				}
				gifEncoder.finishEncoding();
				logger.info("Finished map image preview.");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error during animated season image creation: " + e);
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * Convert BufferedImage into RGB pixel array
	 */
	public static int[][] convertImageToArray(File file, int width, int height) throws IOException {
		BufferedImage bufferedImage = resize(ImageIO.read(file), width, height);
		int[][] rgbArray = new int[bufferedImage.getHeight()][bufferedImage.getWidth()];
		for (int i = 0; i < bufferedImage.getHeight(); i++) {
			for (int j = 0; j < bufferedImage.getWidth(); j++) {
				rgbArray[i][j] = bufferedImage.getRGB(j, i);
			}
		}
		return rgbArray;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
}
