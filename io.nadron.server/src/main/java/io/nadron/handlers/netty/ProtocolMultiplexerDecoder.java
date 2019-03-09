package io.nadron.handlers.netty;

import io.nadron.server.netty.ProtocolMultiplexerChannelInitializer;
import io.nadron.util.BinaryUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.clanwolf.starmap.logging.C3Logger;

import java.util.List;

/**
 * This class can be used to switch login-protocol based on the incoming bytes
 * sent by a client. So, based on the incoming bytes, it is possible to set SSL
 * enabled, normal HTTP, default nadron protocol, or custom user protocol for
 * allowing client to login to nadron. The appropriate protocol searcher needs
 * to be injected to this class. Since this class is a non-singleton, the
 * protocol searchers and other dependencies should actually be injected to
 * {@link ProtocolMultiplexerChannelInitializer} class and then passed in while
 * instantiating this class.
 * 
 * @author Abraham Menacherry
 * 
 */
public class ProtocolMultiplexerDecoder extends ByteToMessageDecoder {
	private final LoginProtocol loginProtocol;
	private final int bytesForProtocolCheck;

	public ProtocolMultiplexerDecoder(int bytesForProtocolCheck,
			LoginProtocol loginProtocol)
	{
		this.loginProtocol = loginProtocol;
		this.bytesForProtocolCheck = bytesForProtocolCheck;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception
	{
		// Will use the first bytes to detect a protocol.
		if (in.readableBytes() < bytesForProtocolCheck)
		{
			return;
		}

		ChannelPipeline pipeline = ctx.pipeline();

		if (!loginProtocol.applyProtocol(in, pipeline))
		{
			byte[] headerBytes = new byte[bytesForProtocolCheck];
			in.getBytes(in.readerIndex(), headerBytes, 0,
					bytesForProtocolCheck);
			C3Logger.warning(
					"Unknown protocol, discard everything and close the connection " + ctx.channel() + ". Incoming Bytes " + BinaryUtils.getHexString(headerBytes));
			close(in, ctx);
		}
		else
		{
			pipeline.remove(this);
		}
	}
	
	protected void close(ByteBuf buffer, ChannelHandlerContext ctx)
	{
		buffer.clear();
		ctx.close();
	}

	public LoginProtocol getLoginProtocol()
	{
		return loginProtocol;
	}

	public int getBytesForProtocolCheck()
	{
		return bytesForProtocolCheck;
	}

}
