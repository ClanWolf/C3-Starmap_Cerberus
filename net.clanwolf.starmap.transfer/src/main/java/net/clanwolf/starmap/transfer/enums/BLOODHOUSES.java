package net.clanwolf.starmap.transfer.enums;

public enum BLOODHOUSES {
	KERENSKY(1),
	WARD(2);

	public final Integer id;

	BLOODHOUSES(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
}
