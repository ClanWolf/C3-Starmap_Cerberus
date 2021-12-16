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
package net.clanwolf.starmap.client.net.irc;

import com.ircclouds.irc.api.Callback;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.IRCApiImpl;
import com.ircclouds.irc.api.IServerParameters;
import com.ircclouds.irc.api.domain.IRCServer;
import com.ircclouds.irc.api.state.IIRCState;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.util.Internationalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

public class IRCClient implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// https://github.com/migzai/irc-api

	private static final String ircServerUrl = "port80a.se.quakenet.org";
	private static final String ircServerChannel = "#c3.clanwolf.net";
	public static boolean connected = false;
	private static IRCApi _api;
	public static String myNick = "";
	public String nick = "C3\\" + Nexus.getCurrentUser().getUserName() + "";
	public String altNick1 = "C3\\" + Nexus.getCurrentUser().getUserName() + "_1";
	public String altNick2 = "C3\\" + Nexus.getCurrentUser().getUserName() + "_2";
	public String altNick3 = "C3\\" + Nexus.getCurrentUser().getUserName() + "_3";

	public IRCClient() {
		ActionManager.addActionCallbackListener(ACTIONS.IRC_SEND_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_CHANGE_NICK, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_SENDING_ACTION, this);
//		ActionManager.addActionCallbackListener(ACTIONS.IRC_GET_NAMELIST, this);
	}

	public void connect() {
		_api = new IRCApiImpl(true);
		_api.connect(getServerParams(nick, Arrays.asList(altNick1, altNick2, altNick3), "C3-Client IRC", "ident", ircServerUrl, false), new Callback<IIRCState>() {
			@Override
			public void onSuccess(final IIRCState aIRCState) {
				_api.addListener(new ChannelJoinListener());
				_api.addListener(new ChannelLeaveListener());
				_api.addListener(new ChannelMessageListener());

				_api.joinChannel(ircServerChannel);
				_api.message(ircServerChannel, Internationalization.getString("C3_IRC_ConnAndLogon"), new Callback<String>() {
					@Override
					public void onSuccess(String s) {
						logger.info(s);
					}

					@Override
					public void onFailure(Exception e) {
						logger.info("Error: " + e.getMessage());
					}
				});

				connected = true;
				myNick = nick;
				ActionManager.getAction(ACTIONS.IRC_CONNECTED).execute(myNick);
			}

			@Override
			public void onFailure(Exception aErrorMessage) {
				logger.info("Error connecting IRC: " + aErrorMessage.getMessage());
				logger.info("IRC not connected!");
				// aErrorMessage.printStackTrace();
				// throw new RuntimeException(aErrorMessage);
				ActionManager.getAction(ACTIONS.IRC_ERROR).execute(aErrorMessage.getMessage());
			}
		});
	}

	private void sendMessage(String source, String target, String message) {
		if (connected) {
			if ("".equals(target)) {
				if (!"".equals(message)) {
					_api.message(ircServerChannel, message, new Callback<String>() {
						@Override
						public void onSuccess(String s) {
							logger.info(s);
						}

						@Override
						public void onFailure(Exception e) {
							logger.info("Error: " + e.getMessage());
						}
					});
				}
			} else {
				if (!"".equals(message)) {
					_api.message(target, message, new Callback<String>() {
						@Override
						public void onSuccess(String s) {
							logger.info(s);
						}

						@Override
						public void onFailure(Exception e) {
							logger.info("Error: " + e.getMessage());
						}
					});
				}
			}
		}
	}

	private static IServerParameters getServerParams(final String aNickname, final List<String> aAlternativeNicks, final String aRealname, final String aIdent, final String aServerName, final Boolean aIsSSLServer) {
		return new IServerParameters() {
			@Override
			public IRCServer getServer() {
				return new IRCServer(aServerName, aIsSSLServer);
			}

			@Override
			public String getRealname() {
				return aRealname;
			}

			@Override
			public String getNickname() {
				return aNickname;
			}

			@Override
			public String getIdent() {
				return aIdent;
			}

			@Override
			public List<String> getAlternativeNicknames() {
				return aAlternativeNicks;
			}
		};
	}

	/**
	 * Handles actions.
	 *
	 * @param action incoming action to be handled
	 * @param o      the action object passed along with the action
	 * @return wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case IRC_SEND_MESSAGE:
				MessageActionObject mo = (MessageActionObject)o.getObject();
				String source = mo.getSource();
				if ("".equals(source)) {
					source = myNick;
				}
				String target = mo.getTarget();
				String message = mo.getMessage();
				sendMessage(source, target, message);
				break;

			case IRC_CHANGE_NICK:
				NickChangeObject nco = (NickChangeObject)o.getObject();
				logger.info("Changing nick to " + nco.getNewNick());
				myNick = nco.getNewNick();
				_api.changeNick(nco.getNewNick());
				break;

			case IRC_SENDING_ACTION:
				String t = (String)o.getText();
				_api.act(ircServerChannel, t);
				break;

//			case IRC_GET_NAMELIST:
//				logger.info("Getting irc name list from server");
//				_api.rawMessage("/names");
//				break;

			default:
				break;
		}
		return true;
	}
}
