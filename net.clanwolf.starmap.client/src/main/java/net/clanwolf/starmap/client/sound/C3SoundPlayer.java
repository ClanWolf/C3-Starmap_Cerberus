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
package net.clanwolf.starmap.client.sound;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.net.HTTP;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Provides functions to play sounds.
 *
 * @author Meldric
 */
public class C3SoundPlayer {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static C3SoundPlayer instance = null;
	private static Media media = null;
	private static MediaPlayer mediaPlayer = null;
	private static MediaPlayer speechPlayer = null;
	private static MediaPlayer rpPlayer = null;
	private static double voiceVolume = 0.0;
	private static double soundVolume = 0.0;
	private static double musicVolume = 0.0;
	private static HashMap<String, AudioClip> audioClipCache = new HashMap<>();
	private static HashMap<String, Media> speechClipCache = new HashMap<>();
	private static HashMap<String, Media> rpClipCache = new HashMap<>();

	private static String lastRolePlaySampleURL = "";

	private static String languageSwitch = null;
	private static String voiceSwitch = null;

	private static final String voiceRSSUrl = "https://api.voicerss.org/";

	/**
	 * Constructor
	 */
	public C3SoundPlayer() {
	}

	public static C3SoundPlayer getInstance() {
		if (instance == null) {
			instance = new C3SoundPlayer();
		}
		return instance;
	}
	/**
	 * Start the music.
	 */
	public static void startMusic() {
		if (mediaPlayer == null) {
			String fn = "/music/DireWolf.mp3";

			URL u = getInstance().getClass().getResource(fn);

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
			logger.error(null, e);
		}
	}

	/**
	 * Retrieves a speech file from a string from online mary tts service
	 *
	 * @param s The sentence to be spoken by mary online service
	 */
	public static void getTTSFile(String s) {
		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {
			logger.info("Looking for voice sample file for string: " + s);

			String lang = Internationalization.getLanguage();
			String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + "voice" + File.separator + lang;
			File cacheFolder = new File(cacheFolderName);
			if (!cacheFolder.isDirectory()) {
				boolean success = cacheFolder.mkdirs();
				logger.info("Creating cache folder for voice files: " + success);
			}
			String fn = s.replace("%20", "_");
			fn = fn.replace(" ", "_");

			String f = cacheFolderName + File.separator + fn + ".mp3";
			File f1 = new File(f);

			if (f1.isFile()) {
				logger.info("TTS sound file was found in cache: " + f1.getAbsolutePath() + ".");
				play(f1, true);
			} else {
				//getSpeechFromMary(fn); // the old way with MaryTTS
				boolean gotSpeechFile = getSpeechFromVoiceRSS(fn); // the new way with VoiceRSS

				if (!gotSpeechFile) {
					logger.info("TTS sound file missing, VoiceTTS failed.");
					logger.info("Looking for TTS sound file in packaged resources... ");
					URL u = null;
					String voicePath = "/sound/voice/" + lang + "/" + fn + ".mp3";
					u = (getInstance()).getClass().getResource(voicePath);

					if (u != null) {
						play(voicePath, true);
						logger.info("TTS sound file was found in resources.");
					} else {
						logger.info("TTS sound file was NOT found in resources.");
					}
				} else {
					logger.info("Got file from VoiceTTS.");
				}
			}
			play("/sound/fx/beep_02.mp3", false);
		}
	}

	public static void stopSpeechPlayer() {
		if(speechPlayer != null) {
			speechPlayer.stop();
			speechPlayer = null;
		}
	}

	public static void pauseRPSound() {
		if (rpPlayer != null) {
			rpPlayer.pause();
		}
	}

	public static void fadeOutRPSound() {
		if (rpPlayer != null) {
			double volume = rpPlayer.getVolume();
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(700), new KeyValue(rpPlayer.volumeProperty(), 0)));
			timeline.setOnFinished(event -> {
				rpPlayer.stop();
				rpPlayer.setVolume(volume);
//				rpPlayer.dispose();
			});
		}
	}

	public static MediaPlayer getRpPlayer() {
		return rpPlayer;
	}

	public static void playRPSound(final URL url, boolean audioStartedOnce) {
		boolean playerPaused = false;
		String urlString = url.toString();

		if (rpPlayer != null) {
			if (!lastRolePlaySampleURL.equals(urlString)) {
				// This is a new sample. Another step was activated
				// The old sample is outdated and the new one must be loaded
				rpPlayer.stop();
				rpPlayer = null;
			}
			if (rpPlayer != null && rpPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)) {
				playerPaused = true;
				rpPlayer.play(); // continue the last sample that was paused by changing the pane
				return;
			} else if (rpPlayer != null) {
				rpPlayer.stop();
				rpPlayer = null;
			}
		}

		if (!audioStartedOnce) {
			Media rpClip;
			if (rpClipCache.get(urlString) != null) {
				logger.info("Playing RP clip from memory cache.");
				rpClip = rpClipCache.get(urlString);
			} else {
				logger.info("Caching RP clip.");
				rpClip = new Media(urlString);
				rpClipCache.put(url.toString(), rpClip);
			}
			rpPlayer = new MediaPlayer(rpClip);
			rpPlayer.setVolume(0.5d * 100);
			rpPlayer.setCycleCount(1);
			rpPlayer.setVolume(voiceVolume);
			if (Nexus.mainframeVolumeSlider != null) {
				rpPlayer.volumeProperty().bindBidirectional(Nexus.mainframeVolumeSlider.valueProperty());
			}
			// rpPlayer.setOnEndOfMedia( () -> ActionManager.getAction(ACTIONS.STOP_SPEECH_SPECTRUM).execute() );
			lastRolePlaySampleURL = url.toString();

			PauseTransition pause = new PauseTransition(Duration.seconds(3));
			pause.setOnFinished(event -> {
				rpPlayer.play();
			});
			pause.play();
		}
	}

	/**
	 * Play sample from URL.
	 *
	 * @param url      File to play
	 * @param isSpeech Flag to indicate speech
	 */
	public synchronized static void play(final URL url, final boolean isSpeech) {
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
							logger.info("Playing TTS clip from memory cache.");
							speechClip = speechClipCache.get(urlString);
						} else {
							logger.info("Caching TTS clip.");
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
					logger.error(null, me);
					ActionManager.getAction(ACTIONS.STOP_SPEECH_SPECTRUM).execute();
				}
			} else {
				if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_SOUND))) {
					final AudioClip soundClip;
					if (audioClipCache.get(url.toString()) != null) {
						//logger.info("Playing sound from cache...");
						soundClip = audioClipCache.get(url.toString());
						soundClip.stop();
					} else {
						String u = url.toString();
						logger.info("Caching sound. Url: " + u);
						soundClip = new AudioClip(u);
						audioClipCache.put(url.toString(), soundClip);
					}
					Platform.runLater(() -> soundClip.play(soundVolume));
				}
			}
		} else {
			logger.info("Sound resource not found (url is null).");
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

		URL u = ((C3SoundPlayer)getInstance()).getClass().getResource(soundPath);

		if (u == null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String callerClassName = stackTraceElements[3].getClassName();
			String callerMethodName = stackTraceElements[3].getMethodName();
			logger.info(callerClassName + "/" + callerMethodName + " was looking for sound: " + soundPath + ". Not found!");
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
		if (speechPlayer != null) {
//			Platform.runLater(() -> {
//				speechPlayer.setVolume(voiceVolume);
//			});
		}
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
			logger.info("Playing background music.");
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();
		}
	}

	/**
	 * Retrieves a speech file from a string from online mary tts service
	 * @param s The sentence to be spoken by mary online service
	 */
	public static boolean getSpeechFromVoiceRSS(String s) {
		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {

			// API-Doc: http://www.voicerss.org/api/
			// API-Key from property value "VoiceRSS-API-Key"
			// https://api.voicerss.org/?key=  *** API-KEY *** &hl=en-us&src=Hello%20World

			String apikey = C3Properties.getProperty(C3PROPS.VOICERSSAPIKEY);

			if (apikey != null && ("unknown".equals(apikey) || "".equals(apikey))) {
				logger.warn("Can not get voice files from VoiceRSS! VoiceRSS API-Key is missing! Check property file and make sure a key is specified!");
				return false;
			} else {
				apikey = "?key=" + apikey;
			}

			String lang = Internationalization.getLanguage();
			String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + "voice" + File.separator + lang;
			File cacheFolder = new File(cacheFolderName);
			if (!cacheFolder.isDirectory()) {
				boolean success = cacheFolder.mkdirs();
				logger.info("Creating cache folder for voice files: " + success);
			}
			String fn = s.replace("%20", "_");
			fn = fn.replace(" ", "_");
			String f = cacheFolderName + File.separator + fn + ".mp3";
			File f1 = new File(f);

			if (!f1.isFile()) {
				// use online VoiceRSS TTS
				logger.info("Online tts (VoiceRSS) requested...");

				s = s.replace(" ", "%20");
				s = s.replace("_", "%20");

				String language = "";
				String voice = "";
				String quality = "&f=44khz_16bit_stereo";
				String format = "&c=MP3";

				if (voiceSwitch != null) {
					language = "&hl=" + languageSwitch;
					voice = "&v=" + voiceSwitch;
				} else {
					if ("de".equals(lang)) {
						language = "&hl=de-de";
						voice = "&v=Hanna"; // Hanna, Lina, Jonas
					} else if ("en".equals(lang)) {
						language = "&hl=en-gb";
						voice = "&v=Alice"; // Alice, Nancy, Lily, Harry
					}
				}

				logger.info("VoiceRSS API call: " + voiceRSSUrl + " *** API-Key *** " + quality + format + voice + language + "&src=" + s);
				String u = voiceRSSUrl + apikey + quality + format + voice + language + "&src=" + s;

				try {
					HTTP.download(u, f);
					File t = new File(f);
					if (t.isFile() && t.canRead()) {
						play(new File(f), true);
						play("sound/fx/beep_02.mp3", false);
						return true;
					}
					play("sound/fx/beep_03.mp3", false);
					return false;
				} catch (Exception e) {
					// Speech could not be retrieved
					logger.info("Error getting speech data: " + e.toString());
				}
			} else {
				logger.info("TTS sound file was found in cache: " + f1.getAbsolutePath() + ".");
				play(f1, true);
				play("sound/fx/beep_02.mp3", false);
			}
			return false;
		}
		return false;
	}

//	/**
//	 * Retrieves a speech file from a string from online mary tts service
//	 * @param s The sentence to be spoken by mary online service
//	 */
//	public static void getSpeechFromMary(String s) {
//
//		// http://mary.dfki.de:59125/documentation.html
//
//		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {
//			String lang = Internationalization.getLanguage();
//			String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + lang;
//			File cacheFolder = new File(cacheFolderName);
//			if (!cacheFolder.isDirectory()) {
//				boolean success = cacheFolder.mkdirs();
//				logger.info("Creating cache folder for voice files: " + success);
//			}
//			String fn = s.replace("%20", "_");
//			String f = cacheFolderName + File.separator + fn + ".mp3";
//			File f1 = new File(f);
//
//			if (!f1.isFile()) {
//				// use online Mary TTS
//				logger.info("Online tts (Mary) requested...");
//
//				s = s.replace(" ", "%20");
//				String u = "http://mary.dfki.de:59125/process?INPUT_TEXT=" + s + "&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&effect_JetPilot_selected=on&AUDIO=WAVE_FILE&LOCALE=";
//				if ("de".equals(lang)) {
//					u = u + "de";
//				} else if ("en".equals(lang)) {
//					u = u + "en_GB";
//				}
//
//				try {
//					HTTP.download(u, f);
//					play(new File(f), true);
//				} catch (Exception e) {
//					// Speech could not be retrieved
//					logger.info("Error getting speech data: " + e.toString());
//				}
//			} else {
//				logger.info("TTS sound file was found in cache: " + f1.getAbsolutePath() + ".");
//				play(f1, true);
//			}
//			play("sound/fx/beep_02.mp3", false);
//		}
//	}

	public static void getSamples() {
//		voiceSwitch = "Lina";
		voiceSwitch = "Jonas";
		languageSwitch = "de-de";
//		getTTSFile("Der Planet wurde erobert. Die Verteidiger haben es nicht geschafft, die Invasion zu verhindern und wurden aufgerieben. Es weht eine neue Fahne über der Hauptstadt");
//		getTTSFile("Die Invasion wurde zurück geschlagen. Der Planet bleibt im Besitz der bisherigen Fraktion und die stolzen Verteidiger reihen diesen Triumph in ihre Geschichte ein");

		voiceSwitch = "Mary";
		languageSwitch = "en-us";
//		getTTSFile("Mining Collective");

		voiceSwitch = null;
		languageSwitch = null;
	}
}
