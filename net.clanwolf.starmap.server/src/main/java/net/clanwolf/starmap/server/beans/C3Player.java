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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.beans;

import io.nadron.app.Player;
import io.nadron.app.PlayerSession;
import io.nadron.app.impl.DefaultPlayer;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Undertaker
 */
public class C3Player implements Player {

//    private C3_Base_User userC3;
    private UserPOJO user;

    /**
     * One player can be connected to multiple games at the same time. Each
     * session in this set defines a connection to a game. TODO, each player
     * should not have multiple sessions to the same game.
     */
    private Set<PlayerSession> playerSessions;

    public C3Player() {
        playerSessions = new HashSet<PlayerSession>();
    }

    public C3Player(UserPOJO user) {
        super();
        this.user = user;
        playerSessions = new HashSet<PlayerSession>();
    }

    public void setUser(UserPOJO user) {
        this.user = user;
    }
    
    public UserPOJO getUser() {
        return this.user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((userC3.getId() == null) ? 0 : userC3.getId().hashCode());
        result = prime * result + ((user == null) ? 0 : user.getUserId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DefaultPlayer other = (DefaultPlayer) obj;
        if (user == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!user.getUserId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public Object getId() {
    	if(user != null) {
    		return user.getUserId();
    	}
    	
    	return null;
    }

    @Override
    public void setId(Object id) {
        user.setUserId((Long) id);
    }

    @Override
    public String getName() {
        return user.getUserName();
    }

    @Override
    public void setName(String name) {
        user.setUserName(name);
    }

    @Override
    public String getEmailId() {
        return "";
    }

    @Override
    public void setEmailId(String emailId) {
//        user.setEmail(emailId);
    }

    @Override
    public synchronized boolean addSession(PlayerSession session) {
        return playerSessions.add(session);
    }

    @Override
    public synchronized boolean removeSession(PlayerSession session) {
        boolean remove = playerSessions.remove(session);
        if (playerSessions.size() == 0) {
            logout(session);
        }
        return remove;
    }

    @Override
    public synchronized void logout(PlayerSession session) {
        session.close();
        if (null != playerSessions) {
            playerSessions.remove(session);
        }
    }

    public Set<PlayerSession> getPlayerSessions() {
        return playerSessions;
    }

    public void setPlayerSessions(Set<PlayerSession> playerSessions) {
        this.playerSessions = playerSessions;
    }
}
