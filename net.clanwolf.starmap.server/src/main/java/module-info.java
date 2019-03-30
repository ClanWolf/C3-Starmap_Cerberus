module net.clanwolf.starmap.server {
	requires org.hibernate.orm.core;
	requires java.logging;
	requires spring.context;
	requires spring.beans;
	requires io.nadron.server;
	requires jackson.annotations;
	requires java.sql;
	requires java.persistence;
	requires net.clanwolf.starmap.transfer;
	requires net.clanwolf.starmap.logging;
	requires jackson.mapper.asl;
	requires json.simple;
	requires mail;
	requires slf4j.simple;

	// opens net.clanwolf.starmap.server to spring.core;

	exports net.clanwolf.starmap.server to spring.beans, spring.context;
}
