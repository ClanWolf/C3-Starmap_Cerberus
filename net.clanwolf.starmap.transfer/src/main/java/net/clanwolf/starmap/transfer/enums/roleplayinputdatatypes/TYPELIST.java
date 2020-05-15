package net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes;

public enum TYPELIST {
	CHARACTER ("CHARACTER"),
	DROPSHIP ("DROPSHIP");

	private String labelkey;

	TYPELIST(String labelkey){
		this.labelkey = labelkey;
	}

	@Override
	public String toString() {
		return "app_rp_storyeditor_roleplayinputdatatypes_" + labelkey;
	}
}
