/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.sound;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.net.HTTP;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Provides functions to play sounds.
 *
 * @author Meldric
 */
public class C3SoundPlayer {

	private static final Class<C3SoundPlayer> c = C3SoundPlayer.class;
	private static Media media = null;
	private static MediaPlayer mediaPlayer = null;
	private static MediaPlayer speechPlayer = null;
	private static double voiceVolume = 0.0;
	private static double soundVolume = 0.0;
	private static double musicVolume = 0.0;
	private static HashMap<String, AudioClip> audioClipCache = new HashMap<>();
	private static HashMap<String, Media> speechClipCache = new HashMap<>();

	/**
	 * Constructor
	 */
	public C3SoundPlayer() {
	}

	/**
	 * Start the music.
	 */
	public static void startMusic() {
		if (mediaPlayer == null) {
			String fn = "/music/DireWolf.mp3";
			URL u = c.getResource(fn);
			playBackgroundMusic(u);
		} else {
			mediaPlayer.play();
		}
	}

	/**
	 * Pause Music.
	 */
	public static void pauseMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}

	public static void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

	public static void killMediaPlayer() {
		if (mediaPlayer != null) {
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), new KeyValue(mediaPlayer.volumeProperty(), 0)));
			timeline.setOnFinished(event -> {
				mediaPlayer.stop();
				mediaPlayer.dispose();
			});
			timeline.play();
		}
	}

	/**
	 * Plays a file.
	 *
	 * @param f      File to play
	 * @param speech Flag to indicate speech
	 */
	public static void play(File f, boolean speech) {
		try {
			play(f.toURI().toURL(), speech);
		} catch (MalformedURLException e) {
			C3Logger.exception(null, e);
		}
	}

	/**
	 * Retrieves a speech file from a string from online mary tts service
	 *
	 * @param s The sentence to be spoken by mary online service
	 */
	public static void getTTSFile(String s) {
		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {
			String lang = Internationalization.getLanguage();
			String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + lang;
			File cacheFolder = new File(cacheFolderName);
			if (!cacheFolder.isDirectory()) {
				boolean success = cacheFolder.mkdirs();
				C3Logger.info("Creating cache folder for voice files: " + success);
			}
			String fn = s.replace("%20", "_");

			String f = cacheFolderName + File.separator + fn + ".wav";
			File f1 = new File(f);

			if (f1.isFile()) {
				C3Logger.info("TTS sound file was found in cache: " + f1.getAbsolutePath() + ".");
				play(f1, true);
			} else {
				getSpeechFromMary(fn);
				C3Logger.info("TTS sound file missing: " + f1.getAbsolutePath() + ".");
			}
			play("/sound/fx/beep_02.wav", false);
		}
	}

	/**
	 * Play sample from URL.
	 *
	 * @param url      File to play
	 * @param isSpeech Flag to indicate speech
	 */
	public static void play(final URL url, final boolean isSpeech) {
		if (url != null) {
			// Prevent more than one speech sample at a time
			if ((isSpeech) && (speechPlayer != null)) {
				speechPlayer.stop();
				speechPlayer = null;
			}

			if (isSpeech) {
				try {
					if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {
						ActionManager.getAction(ACTIONS.START_SPEECH_SPECTRUM).execute();
						String urlString = url.toString();

						Media speechClip;
						if (speechClipCache.get(urlString) != null) {
							C3Logger.info("Playing TTS clip from memory cache.");
							speechClip = speechClipCache.get(urlString);
						} else {
							C3Logger.info("Caching TTS clip.");
							speechClip = new Media(urlString);
							speechClipCache.put(url.toString(), speechClip);
						}

						speechPlayer = new MediaPlayer(speechClip);
						speechPlayer.setVolume(voiceVolume);
//						speechPlayer.setOnEndOfMedia(new Runnable() {
//							@Override
//							public void run() {
//								ActionManager.getAction(ACTIONS.STOP_SPEECH_SPECTRUM).execute();
//							}
//						});
						speechPlayer.setOnEndOfMedia( () -> ActionManager.getAction(ACTIONS.STOP_SPEECH_SPECTRUM).execute() );
						speechPlayer.play();
					}
				} catch (MediaException me) {
					C3Logger.exception(null, me);
					ActionManager.getAction(ACTIONS.STOP_SPEECH_SPECTRUM).execute();
				}
			} else {
				if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_SOUND))) {
					final AudioClip soundClip;
					if (audioClipCache.get(url.toString()) != null) {
						// Log.info("Playing sound from cache...");
						soundClip = audioClipCache.get(url.toString());
						soundClip.stop();
					} else {
						// Log.info("Caching sound...");
						soundClip = new AudioClip(url.toString());
						audioClipCache.put(url.toString(), soundClip);
					}
					Platform.runLater(() -> soundClip.play(soundVolume));
				}
			}
		} else {
			C3Logger.info("Sound resource not found (url is null).");
		}
	}

	/**
	 * Plays a sound
	 *
	 * @param soundPath Path of the soundfile
	 * @param isSpeech  Flag indicating speech
	 */
	public static void play(String soundPath, boolean isSpeech) {
		if (!soundPath.startsWith("/")) {
			soundPath = "/" + soundPath;
		}
		URL u = c.getResource(soundPath);
		if (u == null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String callerClassName = stackTraceElements[3].getClassName();
			String callerMethodName = stackTraceElements[3].getMethodName();
			C3Logger.info(callerClassName + "/" + callerMethodName + " was looking for sound: " + soundPath + ". Not found!");
		}
		play(u, isSpeech);
	}


	/**
	 * Set the volume of the background.
	 *
	 * @param v Volume
	 */
	public static void setBackgroundVolume(double v) {
		if (mediaPlayer == null) {
			musicVolume = v;
		} else {
			mediaPlayer.setVolume(v);
		}
	}

	/**
	 * Set the volume of the voice.
	 *
	 * @param v Volume
	 */
	public static void setVoiceVolume(double v) {
		voiceVolume = v;
	}

	/**
	 * Set the volume of the music.
	 *
	 * @param v Volume
	 */
	public static void setMusicVolume(double v) {
		musicVolume = v;
	}

	/**
	 * Set the volume of the sound.
	 *
	 * @param v Volume
	 */
	public static void setSoundVolume(double v) {
		soundVolume = v;
	}

	/**
	 * Play background music.
	 *
	 * @param mediaFile file
	 */
	public static void playBackgroundMusic(final URL mediaFile) {
		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_MUSIC))) {
			media = new Media(mediaFile.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(musicVolume / 4);
			// Log.info("Playing background music.");
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();
		}
	}

	/**
	 * Retrieves a speech file from a string from online mary tts service
	 * @param s The sentence to be spoken by mary online service
	 */
	public static void getSpeechFromMary(String s) {

		// http://mary.dfki.de:59125/documentation.html

		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {
			String lang = Internationalization.getLanguage();
			String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + lang;
			File cacheFolder = new File(cacheFolderName);
			if (!cacheFolder.isDirectory()) {
				boolean success = cacheFolder.mkdirs();
				C3Logger.info("Creating cache folder for voice files: " + success);
			}
			String fn = s.replace("%20", "_");

			String f = cacheFolderName + File.separator + fn + ".wav";
			File f1 = new File(f);

			if (!f1.isFile()) {
				// use online Mary TTS
				C3Logger.info("Online tts requested...");

				s = s.replace(" ", "%20");
				String u = "http://mary.dfki.de:59125/process?INPUT_TEXT=" + s + "&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&effect_JetPilot_selected=on&AUDIO=WAVE_FILE&LOCALE=";
				if ("de".equals(lang)) {
					u = u + "de";
				} else if ("en".equals(lang)) {
					u = u + "en_GB";
				}

				try {
					HTTP.download(u, f);
					play(new File(f), true);
				} catch (Exception e) {
					// Speech could not be retrieved
					C3Logger.info("Error getting speech data: " + e.toString());
				}
			} else {
				C3Logger.info("TTS sound file was found in cache: " + f1.getAbsolutePath() + ".");
				play(f1, true);
			}
			play("sound/fx/beep_02.wav", false);
		}
	}
}
