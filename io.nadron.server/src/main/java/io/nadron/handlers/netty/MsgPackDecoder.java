package io.nadron.handlers.netty;

import io.nadron.event.Events;
import io.nadron.util.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import java.util.List;


@Sharable
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf>
{

	private MessagePack msgPack;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception
	{
		int opcode = msg.readUnsignedByte();
		if (Events.LOG_IN == opcode || Events.RECONNECT == opcode)
		{
			msg.readUnsignedByte();// To read-destroy the protocol version byte.
		}

		Value source = msgPack.read(NettyUtils.toByteArray(msg, true));
		out.add(Events.event(source, opcode));
	}

	public MessagePack getMsgPack()
	{
		return msgPack;
	}

	public void setMsgPack(MessagePack msgPack)
	{
		this.msgPack = msgPack;
	}


}
