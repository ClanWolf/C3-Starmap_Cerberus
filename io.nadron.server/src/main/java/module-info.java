module io.nadron.server {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires jackson.core.asl;
	requires net.clanwolf.starmap.logging;
	requires jackson.mapper.asl;
	requires jetlang;
	requires spring.beans;
	requires spring.context;
	requires spring.core;
	requires blazeds.core;
	requires msgpack;
	requires io.netty.transport;
	requires io.netty.codec;
	requires io.netty.codec.http;
	requires io.netty.common;
	requires io.netty.buffer;
	requires io.netty.handler;
	requires org.slf4j;

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
	exports io.nadron.concurrent;
	exports io.nadron.server.netty;
	exports io.nadron.context;

	opens io.nadron.server.netty to spring.core;
	opens io.nadron.server to net.clanwolf.starmap.server;
	opens io.nadron.app to net.clanwolf.starmap.server;
	opens io.nadron.app.impl to net.clanwolf.starmap.server;
	opens io.nadron.handlers.netty to net.clanwolf.starmap.server;
	opens io.nadron.protocols to net.clanwolf.starmap.server;
	opens io.nadron.protocols.impl to net.clanwolf.starmap.server;
	opens io.nadron.service to net.clanwolf.starmap.server;
	opens io.nadron.util to net.clanwolf.starmap.server;
	opens io.nadron.event to net.clanwolf.starmap.server;
	opens io.nadron.event.impl to net.clanwolf.starmap.server;
	opens io.nadron.service.impl to net.clanwolf.starmap.server;
}
