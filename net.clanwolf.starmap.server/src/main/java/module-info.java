module net.clanwolf.starmap.server {
	requires org.hibernate.orm.core;
	requires java.logging;
	requires spring.context;
	requires spring.beans;
	requires spring.core;
	requires io.nadron.server;
	requires com.fasterxml.jackson.annotation;
	requires java.sql;
	requires java.persistence;
	requires net.clanwolf.starmap.transfer;
	requires net.clanwolf.starmap.logging;
	requires net.clanwolf.starmap.mail;
	requires jackson.mapper.asl;
	requires json.simple;
	requires net.bytebuddy;
	requires java.xml.bind;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires java.instrument;
	requires org.slf4j;
	requires com.google.gson;
	requires kernel;
	requires layout;
	requires io;

	opens net.clanwolf.starmap.server to spring.core;
	opens net.clanwolf.starmap.server.persistence.pojos to org.hibernate.orm.core, com.fasterxml.jackson.databind;

	exports net.clanwolf.starmap.server to spring.beans, spring.context;
	exports net.clanwolf.starmap.server.persistence.pojos to com.fasterxml.jackson.databind;
}
