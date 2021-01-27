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
//@ImportResource("file:net.clanwolf.starmap.server/src/main/resources/beans/beans.xml") // local database, IDE, no jar
@ImportResource({
        "file:net.clanwolf.starmap.server/src/main/resources/beans/beans.xml",
        "file:net.clanwolf.starmap.server/src/main/resources/beans/server-beans.xml",
        "file:net.clanwolf.starmap.server/src/main/resources/beans/server-protocols.xml",
        "file:net.clanwolf.starmap.server/src/main/resources/beans/netty-handlers.xml",
        "file:net.clanwolf.starmap.server/src/main/resources/beans/service-beans.xml"
}) // local database, IDE, no jar
/*@ImportResource( {
		"classpath:/beans/beans.xml",
		"classpath:/beans/server-beans.xml",
		"classpath:/beans/server-protocols.xml",
		"classpath:/beans/netty-handlers.xml",
		"classpath:/beans/service-beans.xml"
} )*/
public class SpringConfigIDE {

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
