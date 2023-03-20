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

import net.clanwolf.starmap.bots.util.CheckShutdownFlagTimerTask;
import net.clanwolf.starmap.logging.C3LogUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Timer;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class DiscordBot extends ListenerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String serverBaseDir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server").getAbsolutePath();

	public DiscordBot() {
		prepareLogging();

		Timer checkShutdownFlag = new Timer();
		checkShutdownFlag.schedule(new CheckShutdownFlagTimerTask(serverBaseDir, "DiscordBot"), 1000, 1000 * 10);
	}

	public static void prepareLogging() {
		String logFileName = serverBaseDir + File.separator + "log" + File.separator + "DiscordBot.log";
		C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
	}

	public static void main(String[] args) {

		Locale.setDefault(new Locale("en", "EN"));

		// MTA4NzM3NjAxMDAzMjQ0NzU2OQ.GWCNQM.SkCZyjWHCqlxw_QdayP-xJ_mYz1tp1ZiINekTw
		JDA jda = JDABuilder.createLight("MTA4NzM3NjAxMDAzMjQ0NzU2OQ.GWCNQM.SkCZyjWHCqlxw_QdayP-xJ_mYz1tp1ZiINekTw", EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
				.addEventListeners(new DiscordBot()).build();

		// These commands might take a few minutes to be active after creation/update/delete
		CommandListUpdateAction commands = jda.updateCommands();

//		// Moderation commands with required options
//		commands.addCommands(Commands.slash("ban", "Ban a user from this server. Requires permission to ban users.").addOptions(new OptionData(USER, "user", "The user to ban") // USER type allows to include members of the server or other users by id
//						.setRequired(true)) // This command requires a parameter
//				.addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.") // This is optional
//						.setRequiredRange(0, 7)) // Only allow values between 0 and 7 (inclusive)
//				.addOptions(new OptionData(STRING, "reason", "The ban reason to use (default: Banned by <user>)")) // optional reason
//				.setGuildOnly(true) // This way the command can only be executed from a guild, and not the DMs
//				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)) // Only members with the BAN_MEMBERS permission are going to see this command
//		);

		// Simple reply commands
		commands.addCommands(Commands.slash("say", "Makes the bot say what you tell it to").addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
		);

		// Commands without any inputs
		commands.addCommands(Commands.slash("leave", "Make the bot leave the server").setGuildOnly(true) // this doesn't make sense in DMs
				.setDefaultPermissions(DefaultMemberPermissions.DISABLED) // only admins should be able to use this command.
		);

		commands.addCommands(Commands.slash("prune", "Prune messages from this channel").addOption(INTEGER, "amount", "How many messages to prune (Default 100)") // simple optional argument
				.setGuildOnly(true).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)));

		// Send the new set of commands to discord, this will override any existing global commands with the new set provided here
		commands.queue();
	}
}
