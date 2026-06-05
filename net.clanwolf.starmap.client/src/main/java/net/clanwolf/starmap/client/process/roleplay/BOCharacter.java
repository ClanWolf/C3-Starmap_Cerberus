package net.clanwolf.starmap.client.process.roleplay;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDatainputDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.GENDERS;
import net.clanwolf.starmap.transfer.enums.catalogObjects.ICatalogObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;

public class BOCharacter {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private RolePlayCharacterDTO myCharacter;

	public BOCharacter (RolePlayCharacterDTO myChar){
		myCharacter = myChar;
	}

	public void saveCharacter(Long nextStepID) {
		GameState state = new GameState(GAMESTATEMODES.ROLEPLAY_SAVE_NEXT_STEP);
		state.addObject(myCharacter);
		state.addObject2(nextStepID);
		Nexus.fireNetworkEvent(state);
	}

	public ArrayList<String> getdataSetList(RolePlayStoryDatainputDTO di) {
		ArrayList<String> dataSets = new ArrayList<>();
		dataSets.add(di.getDataSet1());
		dataSets.add(di.getDataSet2());
		dataSets.add(di.getDataSet3());
		dataSets.add(di.getDataSet4());
		dataSets.add(di.getDataSet5());
		return dataSets;
	}

	public void handleValues(RolePlayStoryDatainputDTO di, HashMap<String, Node> guiElements, String mode) {
		for (String d : getdataSetList(di)) {
			Node n = guiElements.get(d);
			if (n instanceof TextField tf) {
				if ("CHARNAME".equals(d)) {
					switch (mode) {
						case "fillObjectWithData":
							myCharacter.setName(tf.getText());
							break;
						case "fillGuiElementsWithData":
							tf.setText(myCharacter.getName());
							break;
					}
				}
			} else if (n instanceof ComboBox<?> && "GENDER".equals(d)) {
				@SuppressWarnings("unchecked")
				ComboBox<ICatalogObject> cb = (ComboBox<ICatalogObject>) n;
				switch (mode) {
					case "fillObjectWithData":
						ICatalogObject selected = cb.getValue();
						if (selected != null) {
							try {
								myCharacter.setGender(GENDERS.valueOf(selected.getName()));
							} catch (IllegalArgumentException e) {
								logger.warn("Could not parse gender from catalog: " + selected.getName());
							}
						}
						break;
					case "fillGuiElementsWithData":
						GENDERS gender = myCharacter.getGender();
						if (gender != null) {
							for (ICatalogObject item : cb.getItems()) {
								if (gender.name().equals(item.getName())) {
									cb.setValue(item);
									break;
								}
							}
						}
						break;
				}
			}
			// ... weitere Felder wie LASTNAME
		}
	}
}
