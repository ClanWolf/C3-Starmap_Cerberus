package net.clanwolf.starmap.server.logging;

import net.clanwolf.starmap.logging.C3Logger;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class C3AppenderLog4j extends AppenderSkeleton {

	@Override
	protected void append(LoggingEvent event) {
		C3Logger.info(event.getRenderedMessage());
	}

	public void close() {

	}

	public boolean requiresLayout() {
		return false;
	}
}
