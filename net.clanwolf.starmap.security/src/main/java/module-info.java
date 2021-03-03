module net.clanwolf.starmap.security {
	requires net.clanwolf.starmap.transfer;

	exports net.clanwolf.starmap.security to net.clanwolf.starmap.client;
	exports net.clanwolf.starmap.security.enums to net.clanwolf.starmap.client, net.clanwolf.starmap.administration;
}
