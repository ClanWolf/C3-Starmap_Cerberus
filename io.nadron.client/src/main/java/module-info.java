module io.nadron.client {
	exports io.nadron.client.app;
	exports io.nadron.client.event;
	exports io.nadron.client.app.impl;
	exports io.nadron.client.communication;
	exports io.nadron.client.event.impl;
	exports io.nadron.client.protocol.impl;
	exports io.nadron.client.util;

	requires jdk.unsupported;
	requires io.netty.buffer;
	requires io.netty.transport;
	requires io.netty.codec;
	requires io.netty.common;
	requires net.clanwolf.starmap.logging;
	requires org.slf4j;
}
