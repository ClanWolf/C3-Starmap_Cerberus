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
package net.clanwolf.starmap.server;

import io.nadron.app.Game;
import io.nadron.app.GameRoom;
import io.nadron.app.impl.GameRoomSession.GameRoomSessionBuilder;
import io.nadron.app.impl.SimpleGame;
import io.nadron.handlers.netty.TextWebsocketEncoder;
import io.nadron.protocols.Protocol;
import io.nadron.protocols.impl.NettyObjectProtocol;
import io.nadron.service.LookupService;
import net.clanwolf.starmap.server.beans.C3LookupService;
import net.clanwolf.starmap.server.beans.C3Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the spring configuration for the nadron library user. The
 * only bean that is compulsory to be declared is lookupService bean. Otherwise
 * the program will terminate with a context load error from spring framework.
 * The other beans declared can also be created using **new** operator as per
 * programmer convenience.
 *
 * @author Abraham Menacherry.
 *
 */
@Configuration
@ImportResource("file:net.clanwolf.starmap.server/src/main/resources/beans/beans.xml")
//@ImportResource("classpath:beans/beans.xml")
public class SpringConfig {

    @Autowired
    @Qualifier("messageBufferProtocol")
    private Protocol messageBufferProtocol;

    @Autowired
    @Qualifier("webSocketProtocol")
    private Protocol webSocketProtocol;

    @Autowired
    @Qualifier("textWebsocketEncoder")
    private TextWebsocketEncoder textWebsocketEncoder;

    @Autowired
    @Qualifier("nettyObjectProtocol")
    private NettyObjectProtocol nettyObjectProtocol;

    public @Bean(name = "C3Game")
    Game C3Game() {
        return new SimpleGame(1, "C3Game");
    }

    public @Bean(name = "C3GameRoom")
    GameRoom C3GameRoom() {
        GameRoomSessionBuilder sessionBuilder = new GameRoomSessionBuilder();
        sessionBuilder.parentGame(C3Game()).gameRoomName("C3GameRoom").protocol(webSocketProtocol);
        C3Room room = new C3Room(sessionBuilder);
        return room;
    }

    public @Bean(name = "C3GameRoomForNettyClient")
    GameRoom c3GameRoomForNettyClient() {
        GameRoomSessionBuilder sessionBuilder = new GameRoomSessionBuilder();
        sessionBuilder.parentGame(C3Game()).gameRoomName("C3GameRoomForNettyClient").protocol(nettyObjectProtocol);
        C3Room room = new C3Room(sessionBuilder);
        return room;
    }

    public @Bean(name = "lookupService")
    LookupService lookupService() {
        Map<String, GameRoom> refKeyGameRoomMap = new HashMap<>();
        refKeyGameRoomMap.put("C3GameRoom", C3GameRoom());
        refKeyGameRoomMap.put("C3GameRoomForNettyClient", c3GameRoomForNettyClient());
        LookupService service = new C3LookupService(refKeyGameRoomMap);
        return service;
    }
}
