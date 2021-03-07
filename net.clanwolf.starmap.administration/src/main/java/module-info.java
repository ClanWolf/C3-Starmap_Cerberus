module net.clanwolf.starmap.administration {
	requires net.clanwolf.starmap.logging;
	requires net.clanwolf.starmap.security;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.media;
	requires javafx.fxml;
	requires java.logging;
	requires net.clanwolf.starmap.transfer;

	opens net.clanwolf.client.administration.security to javafx.fxml;

	exports net.clanwolf.client.administration.security to javafx.fxml, javafx.graphics;
}
