module io.nadron {
	exports io.nadron.client.app;
	exports io.nadron.client.event;
	exports io.nadron.client.app.impl;
	exports io.nadron.client.communication;
	exports io.nadron.client.event.impl;
	exports io.nadron.client.protocol.impl;
	exports io.nadron.client.util;
	requires io.netty.all;
}
