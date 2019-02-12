module net.clanwolf.starmap.client {
	requires net.clanwolf.starmap.transfer;

	requires io.nadron;

	requires tektosyne;
	requires commons.net;
	requires org.apache.commons.collections4;

	requires java.desktop;
	requires java.logging;
	requires java.mail;

	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.media;
	requires javafx.fxml;
	requires io.netty.common;

	opens net.clanwolf.starmap.client.gui to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes.login to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes.settings to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes.rp to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes.confirmAppClose to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes.map to javafx.fxml;
	opens net.clanwolf.starmap.client.gui.panes.userinfo to javafx.fxml;

	exports net.clanwolf.starmap.client.util;
	exports net.clanwolf.starmap.client.gui;
	exports net.clanwolf.starmap.client.gui.panes.login;
	exports net.clanwolf.starmap.client.gui.panes.settings;
	exports net.clanwolf.starmap.client.gui.panes.rp;
	exports net.clanwolf.starmap.client.gui.panes.confirmAppClose;
	exports net.clanwolf.starmap.client.gui.panes.map;
	exports net.clanwolf.starmap.client.gui.panes.userinfo;
	exports net.clanwolf.starmap.client.process.universe;
}
