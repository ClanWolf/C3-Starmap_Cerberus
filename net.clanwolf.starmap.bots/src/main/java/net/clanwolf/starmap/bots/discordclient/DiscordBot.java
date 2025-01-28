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
package net.clanwolf.starmap.bots.discordclient;

import net.clanwolf.starmap.bots.db.DBConnection;
import net.clanwolf.starmap.bots.util.CheckShutdownFlagTimerTask;
import net.clanwolf.starmap.logging.C3LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.forums.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.ForumPostAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
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

public class DiscordBot extends ListenerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String serverBaseDir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server").getAbsolutePath();
	public static JDA jda = null;
	private static boolean testEmbed = false;
	private static Long historyRefreshTimestamp = null;

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

			String token = auth.getProperty("discordbottoken");
			jda = JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class)).addEventListeners(this).enableCache(CacheFlag.FORUM_TAGS).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
			jda.awaitReady();

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
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error("Error while creating jda instance.", ioe);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void prepareLogging() {
		String logFileName = serverBaseDir + File.separator + "log" + File.separator + "DiscordBot.log";
		C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
	}

	static void createAttackThread(String header, String msg, String season, String round) {
		logger.info("Text: " + msg);
		List<ForumChannel> forumChannels = jda.getForumChannelsByName("c3-invasionen", true);
		if (forumChannels.isEmpty()) {
			logger.info("c3-invasionen not found.");
			return;
		}
		logger.info("c3-invasionen found.");
		for (ForumChannel fch : forumChannels) {
			logger.info("Thread channel: " + fch.getName());

			List<BaseForumTag> baseForumTags = new ArrayList<>(fch.getAvailableTags());
			BaseForumTag bft = null;

			for (BaseForumTag baseForumTag : baseForumTags) {
				logger.info("Found base forum tag: " + baseForumTag.getName());
				if (baseForumTag.getName().equals("S" + season + "R" + round)) {
					bft = baseForumTag;
					logger.info("Correct base forum tag found.");
					break;
				}
			}
			if (bft == null) {
				logger.info("Correct base forum tag NOT found.");
				logger.info("Adding: " + "S" + season + "R" + round);
				baseForumTags.add(new ForumTagData("S" + season + "R" + round).setModerated(true));
				//baseForumTags.set(0, ForumTagData.from(baseForumTags.get(0)).setName("bug report")); // update an existing tag
				fch.getManager().setAvailableTags(baseForumTags).complete();
			}

			logger.info("---------------------------------------------------------------------");
			logger.info("Looking for tag: " + "S" + season + "R" + round);

			List<ForumTag> forumTags = fch.getAvailableTags();
			Long tagId = null;
			logger.info(forumTags.size() + " available tags found");
			for (ForumTag ft : forumTags) {
				logger.info("Found tag: " + ft.getName());
				if (ft.getName().equals("S" + season + "R" + round)) {
					logger.info("Match!");
					tagId = ft.getIdLong();
				}
			}

			if (tagId != null) {
				logger.info("TagId for ForumTag found: " + tagId);
				ForumPostAction action = fch.createForumPost(header, MessageCreateData.fromContent(msg)).setTags(ForumTagSnowflake.fromId(tagId));
				ForumPost post = action.complete();

				String serverId = fch.getManager().getGuild().getId(); // ServerId
				String channelId = fch.getId();                        // ForumId
				String postId = post.getMessage().getId();             // postId

				sendMessageToChannel("https://discord.com/channels/" + serverId + "/" + channelId + "/" + postId);
				logger.info("Discord link to invasion post: https://discord.com/channels/" + serverId + "  /  " + channelId + "  /  " + postId);
			} else {
				logger.error("TagId for ForumTag not found.");
			}
		}
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
		LocalDateTime dateStartOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime dateNow = LocalDateTime.now();

		Instant threshhold = Instant.ofEpochSecond(dateNow.minusMinutes(10).toEpochSecond(ZoneOffset.UTC));
		Instant threshhold3 = Instant.ofEpochSecond(dateNow.minusHours(8).toEpochSecond(ZoneOffset.UTC));
		Instant threshhold2 = Instant.ofEpochSecond(dateStartOfDay.minusDays(2).toEpochSecond(ZoneOffset.UTC));

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
			List<Message> messageHistoryList = history.getRetrievedHistory();
			logger.info("Found " + messageHistoryList.size() + " messages.");

			LinkedList<Message> messagesRoundAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesServerStartedAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesServerOnlineSinceAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesServerGoesDownAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesRoundFinalizedAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesLobbyOpenAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesWaitingForLobbyAnnouncement = new LinkedList<>();
			LinkedList<Message> messagesLobbyLostDropleadAnnouncement = new LinkedList<>();

			for (Message m : messageHistoryList) {
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

				if (historyRefreshTimestamp == null) {
					historyRefreshTimestamp = System.currentTimeMillis();
				}

				String log = "";
				if (m.getContentDisplay().length() > 50) {
					log = m.getContentDisplay().substring(0,46) + "...";
				} else {
					log = m.getContentDisplay();
				}
				logger.info("Message: " + m.getId() + " - " + log + " :: " + m.getContentRaw());

				long historyDifference = System.currentTimeMillis() - historyRefreshTimestamp;
				if (m.getContentDisplay().startsWith("https://www.clanwolf.net/apps/C3/seasonhistory/S1/C3_S1_map_animated.gif?")) {

					logger.info("History difference: " + historyDifference);

					if (historyRefreshTimestamp == null || historyDifference > 1000 * 60 * 10) {
						// Update timestamp to reload image
						logger.info("Update reload parameter on the history image to make it reload");
						logger.info("History image message id: " + m.getId());
						String content = m.getContentDisplay();

						int randNum = (int) ((Math.random()) * 274 + 10000000);
						String[] parts = content.split("\\?");
						content = parts[0] + "?" + randNum;

						m.editMessage(content).complete();
					} else {
						logger.info("Time diff not old enough to reload season history gif.");
					}
					historyRefreshTimestamp = System.currentTimeMillis();
				}

//				if ("Ulric".equals(m.getAuthor().getName())) {
//					if ((m.getContentDisplay().contains("Runde beendet."))) {
//						String content = "https://www.clanwolf.net/apps/C3/seasonhistory/S1/C3_S1_map_animated.gif?90185555";
//						m.editMessage(content).complete();
//					}
//				}

				if ("Ulric".equals(m.getAuthor().getName())) {
					if (time.isBefore(threshhold)) {
						if ((m.getContentDisplay().contains("Runde ") && m.getContentDisplay().contains("Offene Kämpfe:") && m.getContentDisplay().contains("Stunden und") && m.getContentDisplay().contains("Minuten in Runde"))
								|| (m.getContentDisplay().contains("Round ") && m.getContentDisplay().contains("Open fights:") && m.getContentDisplay().contains("hours and") && m.getContentDisplay().contains("minutes left in round"))) {
							messagesRoundAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("C3-Server-") && m.getContentDisplay().contains("ist gestartet und bereit.")) || (m.getContentDisplay().contains("C3-Server-") && m.getContentDisplay().contains("is up and ready."))) {
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

					if (time.isBefore(threshhold)) {
						if ((m.getContentDisplay().contains("wird vorbereitet, Lobby ist offen (") || m.getContentDisplay().contains("is in preparation, lobby open ("))) {
							messagesLobbyOpenAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("wartet auf die Lobby") || m.getContentDisplay().contains("is waiting for lobby to be opened"))) {
							messagesWaitingForLobbyAnnouncement.push(m);
						}
						if ((m.getContentDisplay().contains("hat einen oder beide Dropleader verloren. Wartet...") || m.getContentDisplay().contains("is missing one of the dropleaders. Waiting..."))) {
							messagesLobbyLostDropleadAnnouncement.push(m);
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
			if (messagesLobbyOpenAnnouncement.size() > 1) {
				messagesLobbyOpenAnnouncement.removeLast();               // remove the first (latest) entry of that type
				for (Message m : messagesLobbyOpenAnnouncement) {         // delete all the others
					m.delete().complete();
				}
			}
			if (messagesWaitingForLobbyAnnouncement.size() > 1) {
				messagesWaitingForLobbyAnnouncement.removeLast();          // remove the first (latest) entry of that type
				for (Message m : messagesWaitingForLobbyAnnouncement) {    // delete all the others
					m.delete().complete();
				}
			}
			if (messagesLobbyLostDropleadAnnouncement.size() > 1) {
				messagesLobbyLostDropleadAnnouncement.removeLast();         // remove the first (latest) entry of that type
				for (Message m : messagesLobbyLostDropleadAnnouncement) {   // delete all the others
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
						if (!m.getContentDisplay().startsWith("https://www.clanwolf.net/apps/C3/seasonhistory/S1/C3_S1_map_animated.gif?")) {
							m.delete().complete();
						}
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

	public static void main(String[] args) throws Exception {
		Locale.setDefault(new Locale.Builder().setLanguage("en").setScript("Latn").setRegion("US").build());
		DiscordBot bot = new DiscordBot();
		// DiscordBot.createAttackThread("test");
	}

//	public static void main(String[] args) throws Exception {
//
//			String content = "https://www.clanwolf.net/apps/C3/seasonhistory/S1/C3_S1_map_animated.gif?43543321";
//
//			int randNum = (int)((Math.random()) * 274 + 10000000);
//			String[] parts = content.split("\\?");
//			content = parts[0] + "?" + randNum;
//
//			System.out.println(content);
//	}
}
