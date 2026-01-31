module net.clanwolf.starmap.logging {
	requires transitive java.logging;
	requires transitive org.slf4j;
	requires transitive org.slf4j.jul;
	requires transitive org.apache.commons.logging;
	requires transitive log4j.over.slf4j;

	exports net.clanwolf.starmap.logging;
}
