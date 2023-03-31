package net.clanwolf.starmap.exceptions;

public class MechNotFoundException extends Exception {

	private String desc = "";

	public MechNotFoundException(String desc) {
		this.desc = desc;
	}
}
