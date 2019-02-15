module io.nadron.server {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;

	requires net.clanwolf.starmap.logging;

	requires io.netty.transport;
	requires io.netty.codec;
	requires io.netty.codec.http;
	requires io.netty.handler;
	requires io.netty.common;
	requires jackson.mapper.asl;
	requires jetlang;
	requires io.netty.buffer;
}
