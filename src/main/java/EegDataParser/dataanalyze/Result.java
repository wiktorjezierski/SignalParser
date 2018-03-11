package EegDataParser.dataanalyze;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import EegDataParser.operationaldata.Tasks;
import EegDataParser.operationaldata.Wave;

public class Result {

	private int threshold;
	private Tasks task;
	private int patients;
	private Map<Wave, Data> dane;
	
	public Result(int threshold, Tasks task, int patients) {
		this.threshold = threshold;
		this.task = task;
		this.patients = patients;
		dane = new HashMap<>();
	}

	public void add(Wave wave, int amountOfPatients, boolean isCorrectly) {
		dane.put(wave, new Data(amountOfPatients, isCorrectly));
	}
	
	public Map<Wave, Data> getData() {
		return dane;
	}
	
	public void fillMissingValues(ExpectedWave expectedWave) {
		for (Wave wave : Wave.values()) {
			if(!dane.containsKey(wave)) {
				dane.put(wave, new Data(0, expectedWave.isAmountExpected(wave)));
			}
		}
	}
	
	public int getThreshold() {
		return threshold;
	}

	public Tasks getTask() {
		return task;
	}

	@Override
	public String toString() {
		return task + " " + threshold + " " + dane.entrySet().stream().map(e -> e.getValue().toString()).collect(Collectors.joining(", "));
	}

}

