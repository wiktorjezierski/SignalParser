package EegDataParser.masterdata;

public enum Plat {
	PLAT_CZOLOWY("czołowy"),
	PLAT_CIEMIENIOWY("ciemieniowy"),
	PLAT_POTYLICZNY("potyliczny"),
	PLAT_SKRONIOWY("skroniowy");
	
	private String name;
	
	Plat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
