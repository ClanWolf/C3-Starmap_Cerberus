module net.clanwolf.starmap.transfer {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires jackson.annotations;
	requires java.sql;

	exports net.clanwolf.starmap.transfer;
	exports net.clanwolf.starmap.transfer.enums;
	exports net.clanwolf.starmap.transfer.dtos;
}