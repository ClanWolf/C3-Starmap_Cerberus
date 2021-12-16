package io.nadron.handlers.netty;

import io.nadron.app.Session;
import io.nadron.communication.MessageSender.Fast;
import io.nadron.communication.NettyUDPMessageSender;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.nadron.service.SessionRegistryService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.SocketAddress;

public class UDPUpstreamHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String UDP_CONNECTING = "UDP_CONNECTING";
	private SessionRegistryService<SocketAddress> udpSessionRegistry;
	private MessageBufferEventDecoder messageBufferEventDecoder;
	
	public UDPUpstreamHandler()
	{
		super();
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx,
			DatagramPacket packet) throws Exception
	{
		// Get the session using the remoteAddress.
		SocketAddress remoteAddress = packet.sender();
		Session session = udpSessionRegistry.getSession(remoteAddress);
		if (null != session) 
		{
			ByteBuf buffer = packet.content();
			Event event = (Event) messageBufferEventDecoder
					.decode(null, buffer);

			// If the session's UDP has not been connected yet then send a
			// CONNECT event.
			if (!session.isUDPEnabled()) 
			{
				if (null == session.getAttribute(UDP_CONNECTING)
						|| (!(Boolean) session.getAttribute(UDP_CONNECTING))) 
				{
					session.setAttribute(UDP_CONNECTING, true);
					event = getUDPConnectEvent(event, remoteAddress,
							(DatagramChannel) ctx.channel());
					// Pass the connect event on to the session
					session.onEvent(event);
				}
				else
				{
					logger.info("Going to discard UDP Message Event with type " + event.getType()
							+ "the UDP MessageSender is not initialized fully");
				}
			} 
			else if (event.getType() == Events.CONNECT) 
			{
				// Duplicate connect just discard.
				logger.warn("Duplicate CONNECT " + event + " received in UDP channel, "
						+ "for session: " + session + " going to discard");
			} 
			else 
			{
				// Pass the original event on to the session
				session.onEvent(event);
			}
		} 
		else 
		{
			logger.warn(
					"Packet received from unknown source address: " + remoteAddress + ", going to discard");
		}
	}

	public Event getUDPConnectEvent(Event event, SocketAddress remoteAddress,
			DatagramChannel udpChannel)
	{
		logger.warn("Incoming udp connection remote address : " + remoteAddress);
		
		if (event.getType() != Events.CONNECT)
		{
			logger.info("UDP Event with type " + event.getType() + " will get converted to a CONNECT "
					+ "event since the UDP MessageSender is not initialized till now");
		}
		Fast messageSender = new NettyUDPMessageSender(remoteAddress, udpChannel, udpSessionRegistry);
		Event connectEvent = Events.connectEvent(messageSender);
		
		return connectEvent;
	}

	public SessionRegistryService<SocketAddress> getUdpSessionRegistry()
	{
		return udpSessionRegistry;
	}

	public void setUdpSessionRegistry(
			SessionRegistryService<SocketAddress> udpSessionRegistry)
	{
		this.udpSessionRegistry = udpSessionRegistry;
	}
	
	public MessageBufferEventDecoder getMessageBufferEventDecoder() 
	{
		return messageBufferEventDecoder;
	}

	public void setMessageBufferEventDecoder(
			MessageBufferEventDecoder messageBufferEventDecoder) 
	{
		this.messageBufferEventDecoder = messageBufferEventDecoder;
	}

	
}
