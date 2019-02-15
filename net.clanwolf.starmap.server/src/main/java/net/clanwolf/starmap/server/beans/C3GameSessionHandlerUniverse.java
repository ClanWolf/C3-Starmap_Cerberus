package net.clanwolf.starmap.server.beans;

import io.nadron.app.GameRoom;
import io.nadron.app.PlayerSession;
import net.clanwolf.starmap.server.util.Log;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.Iterator;

class C3GameSessionHandlerUniverse {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void getUniverseData(PlayerSession session, GameRoom gm) {
		Log.print("Getting universe data on request of a client.");
		UniverseDTO universe = WebDataInterface.getUniverse();

		// TODO: Broadcast new UniverseDTO to logged in (HH) clients
		Iterator<PlayerSession> pl = gm.getSessions().iterator();
		while (pl.hasNext()) {
			PlayerSession plSession = pl.next();
			GameState state_universe = new GameState(GAMESTATEMODES.GET_UNIVERSE_DATA);
			state_universe.addObject(universe);
			// state_universe.setReceiver();

//			gm.sendBroadcast(Events.networkEvent(state_universe));
			C3GameSessionHandler.sendBroadCast(gm,state_universe );
		}
	}
}
