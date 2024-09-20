package net.clanwolf.starmap.client.process.roleplay;

import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDatainputDTO;

public class BOCharacter {

	private RolePlayCharacterDTO myCharacter;

	public BOCharacter (RolePlayCharacterDTO myChar){
		myCharacter = myChar;
	}

	public void setVar3(RolePlayStoryDatainputDTO var3){

	}

	public boolean saveCharacter(){
		return true;
	}
}
