module io.nadron.server {
	exports io.nadron.server;
	exports io.nadron.app;
	exports io.nadron.app.impl;
	exports io.nadron.handlers.netty;
	exports io.nadron.protocols;
	exports io.nadron.protocols.impl;
	exports io.nadron.service;
	exports io.nadron.util;
	exports io.nadron.event;
	exports io.nadron.event.impl;
	exports io.nadron.service.impl;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires jackson.core.asl;

	requires net.clanwolf.starmap.logging;

	requires io.netty.transport;
	requires io.netty.codec;
	requires io.netty.codec.http;
	requires io.netty.handler;
	requires io.netty.common;
	requires jackson.mapper.asl;
	requires jetlang;
	requires io.netty.buffer;
	requires spring.beans;
	requires spring.context;
	requires blazeds.core;
	requires msgpack;
}
