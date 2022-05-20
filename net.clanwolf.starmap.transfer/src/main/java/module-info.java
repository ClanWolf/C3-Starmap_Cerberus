module net.clanwolf.starmap.transfer {
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires java.sql;
	requires com.google.gson;

	exports net.clanwolf.starmap.transfer;
	exports net.clanwolf.starmap.transfer.enums;
	exports net.clanwolf.starmap.transfer.dtos;
	exports net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes;
	exports net.clanwolf.starmap.transfer.enums.catalogObjects to com.fasterxml.jackson.databind;
	exports net.clanwolf.starmap.transfer.util;
	exports net.clanwolf.starmap.constants;
    exports net.clanwolf.starmap.transfer.mwo;

	opens net.clanwolf.starmap.transfer.enums.catalogObjects to com.fasterxml.jackson.databind;
	opens net.clanwolf.starmap.transfer.mwo to com.google.gson;
}
