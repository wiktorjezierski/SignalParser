package EegDataParser.dataanalyze;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import EegDataParser.operationaldata.Wave;

public class Result {

	private int threshold;
	private String task;
	private int patients;
	private Map<Wave, Data> dane;
	
	public Result(int threshold, String task, int patients) {
		this.threshold = threshold;
		this.task = task;
		this.patients = patients;
		dane = new HashMap<>();
	}

	public void add(Wave wave, int amountOfPatients, boolean isCorrectly) {
		dane.put(wave, new Data(amountOfPatients, isCorrectly));
	}
	
	public void fillMissingValues(ExpectedWave expectedWave) {
		for (Wave wave : Wave.values()) {
			if(!dane.containsKey(wave)) {
				dane.put(wave, new Data(0, expectedWave.isAmountExpected(wave)));
			}
		}
	}
	
	@Override
	public String toString() {
		return task + " " + threshold + " " + dane.entrySet().stream().map(e -> e.getValue().toString()).collect(Collectors.joining(", "));
	}

	private static class Data {
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
}

