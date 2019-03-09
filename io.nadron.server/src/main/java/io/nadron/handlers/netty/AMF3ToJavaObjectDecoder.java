package io.nadron.handlers.netty;

import flex.messaging.io.SerializationContext;
import io.nadron.convert.Transform;
import io.nadron.convert.flex.AMFDeSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.clanwolf.starmap.logging.C3Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * This class takes a {@link ByteBuf} containing AMF3 object as input and
 * creates a java object from it using the {@link AMFDeSerializer} class. 
 * 
 * @author Abraham Menacherry.
 * 
 */
public class AMF3ToJavaObjectDecoder extends ByteToMessageDecoder implements Transform<ByteBuf, Object>
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception
	{
		if(null == in)
		{
			return;
		}
		// buffer.array() will ignore the readerIndex. Hence readBytes is used
		// and then .array is called
		ByteArrayInputStream bis = new ByteArrayInputStream(in.readBytes(
				in.readableBytes()).array());
		out.add(deSerializeObjectFromStream(bis));
	}
	
	@Override
	public Object convert(ByteBuf buffer) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
		return deSerializeObjectFromStream(bis);
	}
	
	protected Object deSerializeObjectFromStream(ByteArrayInputStream bis) throws Exception
	{
		AMFDeSerializer serializer = new AMFDeSerializer(SerializationContext.getSerializationContext());
		Object o = null;
		try
		{
			// do the deserialization.
			o = serializer.fromAmf(bis);
			C3Logger.info("Serialized object: " + o);
			bis.close();
		}
		catch (IOException e)
		{
			C3Logger.warning("IO error in AMF3ToJavaObjectDecoder: " + e);
			throw e;
		}
		catch (ClassNotFoundException e)
		{
			C3Logger.warning("Error in AMF3ToJavaObjectDecoder: " + e + ".\n " +
					"Check if flash class has corresponding java class");
			throw e;
		}
		return o;
	}

}
