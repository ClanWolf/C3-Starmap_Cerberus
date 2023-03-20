package io.nadron.handlers.netty;

import io.nadron.app.GameEvent;
import io.nadron.app.PlayerSession;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * This class will handle on the {@link GameEvent}s by forwarding message
 * events to the associated session instance.
 * 
 * @author Abraham Menacherry
 * 
 */
public class DefaultToServerHandler extends SimpleChannelInboundHandler<Event> {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * The player session associated with this stateful business handler.
	 */
	private final PlayerSession playerSession;

	public DefaultToServerHandler(PlayerSession playerSession)
	{
		super();
		this.playerSession = playerSession;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx,
			Event msg) throws Exception
	{
		playerSession.onEvent(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Netty Channel " + ctx.channel() + " is closed.");
		if (!playerSession.isShuttingDown()) {
			// Should not send close to session, since reconnection/other
			// business logic might be in place.
			Event event = Events.event(null, Events.DISCONNECT);
			playerSession.onEvent(event);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		if (evt instanceof IdleStateEvent) {
			logger.warn("Channel " + ctx.channel() + " has been idle, exception event will be raised now: ");
			// TODO check if setting payload as non-throwable cause issue?
			Event event = Events.event(evt, Events.EXCEPTION);
			playerSession.onEvent(event);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		logger.warn("Exception during network communication: " + cause + ".");
		logger.error("Exception", cause);
		Event event = Events.event(cause, Events.EXCEPTION);
		playerSession.onEvent(event);
	}

	public PlayerSession getPlayerSession()
	{
		return playerSession;
	}

}
