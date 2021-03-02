module net.clanwolf.starmap.administration {
	requires java.desktop;
	requires net.clanwolf.starmap.logging;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.media;
	requires javafx.fxml;
	requires java.logging;

	opens net.clanwolf.client.administration.security to javafx.fxml;

	exports net.clanwolf.client.administration.security to javafx.fxml, javafx.graphics;
}
