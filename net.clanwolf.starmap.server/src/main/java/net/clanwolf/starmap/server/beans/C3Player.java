/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : starmap.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
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
