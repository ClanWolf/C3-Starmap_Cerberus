package net.clanwolf.starmap.client.process.roleplay;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDatainputDTO;
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

	public boolean saveCharacter(){
		return true;
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

	public void setValues(RolePlayStoryDatainputDTO di, HashMap<String, Node> guiElements) {
		for (Node n : guiElements.values()) {
			for (String d : getdataSetList(di)) {
				if ("CHARNAME".equals(d)) {
					if (n instanceof TextField tf) {
						logger.info(tf.getText());
						//boChar.setCharName(tf.getText());
					}
				}
				if ("LASTNAME".equals(d)) {
					// ...
				}
			}
		}
	}
}
