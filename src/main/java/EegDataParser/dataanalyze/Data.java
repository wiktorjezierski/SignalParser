package EegDataParser.dataanalyze;

public class Data {
	private Integer amountOfPatients;
	private Boolean isCorrectly;		// true traktujemy jak OK, false jak !OK
	
	public Data(Integer amountOfPatients, Boolean isCorrectly) {
		this.amountOfPatients = amountOfPatients;
		this.isCorrectly = isCorrectly;
	}

	public Integer getAmountOfPatients() {
		return amountOfPatients;
	}

	public Boolean getIsCorrectly() {
		return isCorrectly;
	}
	
	@Override
	public String toString() {
		return amountOfPatients + " " + isCorrectly;
	}
}
