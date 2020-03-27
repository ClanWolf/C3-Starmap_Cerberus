package net.clanwolf.starmap.client.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TextTyper extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	public static void typeText(TextArea textArea, String inText) {
		final IntegerProperty i = new SimpleIntegerProperty(0);
		Timeline timeline = new Timeline();
		KeyFrame keyFrame = new KeyFrame(
				Duration.seconds(.02),
				event -> {
					if (i.get() > inText.length()) {
						timeline.stop();
					} else {
						String cursor = "";
						if (i.get() % 2 == 0) {
							cursor = "_";
						}

						textArea.setText(inText.substring(0, i.get()) + cursor);
						i.set(i.get() + 1);
					}
				});
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	@Override
	public void start(Stage primaryStage) {
		TextArea textArea = new TextArea();
		VBox root = new VBox(textArea);
		root.setAlignment(Pos.TOP_LEFT);
		Scene scene = new Scene(root, 330, 120, Color.WHITE);
		primaryStage.setScene(scene);
		primaryStage.show();

		typeText(textArea, "sdjkjd sjdsk jdksjdkaj ksjdaskd j");
	}
}
