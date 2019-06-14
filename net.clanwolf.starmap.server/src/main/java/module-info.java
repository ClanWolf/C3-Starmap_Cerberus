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
	requires net.bytebuddy;
	requires java.xml.bind;

	opens net.clanwolf.starmap.server to spring.core;
	opens net.clanwolf.starmap.server.persistence.pojos to org.hibernate.orm.core, com.fasterxml.jackson.databind;

	exports net.clanwolf.starmap.server to spring.beans, spring.context;
	exports net.clanwolf.starmap.server.persistence.pojos to com.fasterxml.jackson.databind;
}
