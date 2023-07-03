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
package net.clanwolf.starmap.bots.discordclient;

import net.clanwolf.starmap.bots.db.DBConnection;
import net.clanwolf.starmap.bots.util.CheckShutdownFlagTimerTask;
import net.clanwolf.starmap.logging.C3LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DiscordBot extends ListenerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String serverBaseDir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server").getAbsolutePath();
	public static JDA jda = null;
	private static boolean testEmbed = false;

	public DiscordBot() {
		prepareLogging();

		final Properties auth = new Properties();
		try {
			final String authFileName = "auth.properties";
			InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		String token = auth.getProperty("discordbottoken");
		jda = JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class)).addEventListeners(this).build();

		Timer checkShutdownFlag = new Timer();
		checkShutdownFlag.schedule(new CheckShutdownFlagTimerTask(serverBaseDir, "DiscordBot"), 1000, 1000 * 10);

		Timer extcomDiscordTimer = new Timer();
		ExtcomDiscordTimerTask extcomDiscordTimerTask = new ExtcomDiscordTimerTask();
		extcomDiscordTimer.schedule(extcomDiscordTimerTask, 1000, 25000);
		extcomDiscordTimerTask.setBot(this);

		//		// These commands might take a few minutes to be active after creation/update/delete
		//		CommandListUpdateAction commands = jda.updateCommands();

		//		// Moderation commands with required options
		//		commands.addCommands(Commands.slash("ban", "Ban a user from this server. Requires permission to ban users.").addOptions(new OptionData(USER, "user", "The user to ban") // USER type allows to include members of the server or other users by id
		//						.setRequired(true)) // This command requires a parameter
		//				.addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.") // This is optional
		//						.setRequiredRange(0, 7)) // Only allow values between 0 and 7 (inclusive)
		//				.addOptions(new OptionData(STRING, "reason", "The ban reason to use (default: Banned by <user>)")) // optional reason
		//				.setGuildOnly(true) // This way the command can only be executed from a guild, and not the DMs
		//				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)) // Only members with the BAN_MEMBERS permission are going to see this command
		//		);

		//		// Simple reply commands
		//		commands.addCommands(Commands.slash("say", "Makes the bot say what you tell it to").addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
		//		);
		//
		//		// Commands without any inputs
		//		commands.addCommands(Commands.slash("leave", "Make the bot leave the server").setGuildOnly(true) // this doesn't make sense in DMs
		//				.setDefaultPermissions(DefaultMemberPermissions.DISABLED) // only admins should be able to use this command.
		//		);
		//
		//		commands.addCommands(Commands.slash("prune", "Prune messages from this channel").addOption(INTEGER, "amount", "How many messages to prune (Default 100)") // simple optional argument
		//				.setGuildOnly(true).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));
		//
		//		// Send the new set of commands to discord, this will override any existing global commands with the new set provided here
		//		commands.queue();
	}

	public static void prepareLogging() {
		String logFileName = serverBaseDir + File.separator + "log" + File.separator + "DiscordBot.log";
		C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
	}

	static void sendMessage(TextChannel ch, String msg) {
		ch.sendMessage(msg).complete();
	}

	static void sendMessageEmbed(TextChannel ch, String msg) {

		// https://gist.github.com/zekroTJA/c8ed671204dafbbdf89c36fc3a1827e1

		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("This is a tilte", null);
		embed.setDescription("This is a description");

		embed.addField("Phrase", "Stuff", true);
		embed.addField("Phrase", "Stuff", true);
		embed.addField("Phrase", "Stuff", true);

		embed.addField("Field 1", "Your text here: [link123](https://www.clanwolf.net)", false);
		embed.addField("Field 2", "Your text here: [link123](https://www.clanwolf.net)", false);

		embed.setColor(Color.RED);

		embed.setImage("https://www.clanwolf.net/apps/C3/static/planets/039.png");
		embed.setThumbnail("https://www.clanwolf.net/apps/C3/static/logos/factions/FRR.png");

		embed.setFooter("Bot created by person");
		ch.sendMessageEmbeds(embed.build()).queue();
		embed.clear();
	}

	public static void sendMessageToChannel(String message) {
		List<TextChannel> channels = jda.getTextChannelsByName("c3-status", true);
		LocalDateTime date = LocalDate.now().atStartOfDay();
		LocalDateTime date2 = LocalDateTime.now();

		Instant threshhold = Instant.ofEpochSecond(date2.minusMinutes(1).toEpochSecond(ZoneOffset.UTC));
		Instant threshhold2 = Instant.ofEpochSecond(date.minusDays(2).toEpochSecond(ZoneOffset.UTC));

		for (TextChannel ch : channels) {
			if (testEmbed) {
				sendMessageEmbed(ch, "test");
				testEmbed = false;
			}

//			Guild guild = jda.getGuildById(753193674321690665L);
//			if (guild != null) {
//				List<Role> rl = guild.getRolesByName("C3", true);
//				for (Role r : rl) {
//					logger.info("Role '" + r + "' - ID: " + r.getId());
//				}
//			}

//			jda.retrieveUserById(946206988327616533L).queue(user -> {
//				if (user != null) {
//					for (Guild g : user.getMutualGuilds()) {
//						List<Role> rl2 = g.getRolesByName("C3", true);
//						for (Role r : rl2) {
//							logger.info("Role '" + r + "' - ID: " + r.getId());
//						}
//					}
//				} else {
//					logger.info("User 'Meldric' not found");
//				}
//			});

			//sendMessage(ch, "@1081654927635533834 " + message);
			sendMessage(ch, message);

			MessageHistory history = MessageHistory.getHistoryFromBeginning(ch).complete();
			List<Message> mess = history.getRetrievedHistory();
			logger.info("Found " + mess.size() + " messages.");

			LinkedList<Message> messagesRoundAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesServerStartedAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesServerOnlineSinceAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesServerGoesDownAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesRoundFinalizedAnnouncement = new LinkedList<>();

			for (Message m : mess) {
				Instant time = m.getTimeCreated().toInstant();

//				// Delete test embed messages with this information
//				logger.info("-------- Message -> (" + m.getId() + "): " + m.getContentDisplay());
//				if (m.getId().equals("1101922616480108586")
//						|| m.getId().equals("1101902861283098695")
//						|| m.getId().equals("1101922633655795762")
//						|| m.getId().equals("1101925636051845130")
//				) {
//					m.delete().complete();
//				}

				if (time.isBefore(threshhold)) {
					if ("Ulric".equals(m.getAuthor().getName())) {
						if ((m.getContentDisplay().contains("Runde ") && m.getContentDisplay().contains("Offene Kämpfe:") && m.getContentDisplay().contains("Stunden und") && m.getContentDisplay().contains("Minuten in Runde"))
								|| (m.getContentDisplay().contains("Round ") && m.getContentDisplay().contains("Open fights:") && m.getContentDisplay().contains("hours and") && m.getContentDisplay().contains("minutes left in round"))) {
							messagesRoundAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("C3-Server-") && m.getContentDisplay().contains("ist gestartet und bereit.") && m.getContentDisplay().contains("Download:")) || (m.getContentDisplay().contains("C3-Server-") && m.getContentDisplay().contains("is up and ready.") && m.getContentDisplay().contains("Download:"))) {
							messagesServerStartedAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("Runde beendet.") || m.getContentDisplay().contains("Round finalized."))) {
							messagesRoundFinalizedAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("Server ist online seit") || m.getContentDisplay().contains("Server is up since"))) {
							messagesServerOnlineSinceAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("Server fährt herunter") || m.getContentDisplay().contains("Server is going down"))) {
							messagesServerGoesDownAnnouncement.push(m);
						}
					}
				}
			}

			if (messagesRoundAnnouncement.size() > 1) {
				messagesRoundAnnouncement.removeLast();                   // remove the first (latest) entry of that type
				for (Message m : messagesRoundAnnouncement) {             // delete all the others
					m.delete().complete();
				}
			}
			if (messagesServerStartedAnnouncement.size() > 1) {
				messagesServerStartedAnnouncement.removeLast();           // remove the first (latest) entry of that type
				for (Message m : messagesServerStartedAnnouncement) {     // delete all the others
					m.delete().complete();
				}
			}
			if (messagesServerOnlineSinceAnnouncement.size() > 1) {
				messagesServerOnlineSinceAnnouncement.removeLast();       // remove the first (latest) entry of that type
				for (Message m : messagesServerOnlineSinceAnnouncement) { // delete all the others
					m.delete().complete();
				}
			}
			if (messagesRoundFinalizedAnnouncement.size() > 1) {
				messagesRoundFinalizedAnnouncement.removeLast();          // remove the first (latest) entry of that type
				for (Message m : messagesRoundFinalizedAnnouncement) {    // delete all the others
					m.delete().complete();
				}
			}
			if (messagesServerGoesDownAnnouncement.size() > 1) {
				messagesServerGoesDownAnnouncement.removeLast();          // remove the first (latest) entry of that type
				for (Message m : messagesServerGoesDownAnnouncement) {    // delete all the others
					m.delete().complete();
				}
			}

			// ----------------------------------------

			MessageHistory history2 = MessageHistory.getHistoryFromBeginning(ch).complete();
			List<Message> mess2 = history2.getRetrievedHistory();

			for (Message m : mess2) {
				Instant time = m.getTimeCreated().toInstant();
				if (time.isBefore(threshhold2)) {
					if ("Ulric".equals(m.getAuthor().getName())) {
						m.delete().complete();
					}
				}
			}
		}
	}

	//	@Override
	//	public void onMessageReceived(MessageReceivedEvent event) {
	//		if (event.getAuthor().isBot()) return;
	//
	//		User author = event.getAuthor();
	//		Message message = event.getMessage();
	//		String content = message.getContentRaw();
	//		MessageChannel channel = event.getChannel();
	//		Member member = event.getMember();
	//		String nickname = member.getNickname();
	//		Role role = event.getGuild().getPublicRole();
	//	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("US").build());
		DiscordBot bot = new DiscordBot();
	}
}
