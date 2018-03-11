package EegDataParser.dataanalyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import EegDataParser.masterdata.Odprowadzenie;
import EegDataParser.operationaldata.Signal;
import EegDataParser.operationaldata.Tasks;
import EegDataParser.operationaldata.Wave;

public class SignalAnalyzer {

	private int thresholdLow;
	private int thresholdHigh;
	private int thresholdStep;
	private Map<Tasks, ExpectedWave> expected;
	private List<Integer> thresholdInPercents;

	public SignalAnalyzer(int thresholdLow, int thresholdHigh, int thresholdStep) {
		this.thresholdLow = thresholdLow;
		this.thresholdHigh = thresholdHigh;
		this.thresholdStep = thresholdStep;
		this.thresholdInPercents = new ArrayList<>();
		startup();
	}

	private void startup() {
		ExpectedWave baseline1 = new ExpectedWave(0, 1, 1, 0, 0, 0, 1);
		ExpectedWave baseline2 = new ExpectedWave(0, 0, 1, 0, 0, 0, 1);
		ExpectedWave task1 = new ExpectedWave(0, 1, 1, 1, 1, 1, 1);
		ExpectedWave task2 = new ExpectedWave(0, 1, 1, 1, 1, 1, 1);
		ExpectedWave task3 = new ExpectedWave(0, 1, 1, 1, 1, 1, 1);
		ExpectedWave task4 = new ExpectedWave(0, 1, 1, 1, 1, 1, 1);
		
		expected = new HashMap<>();
		expected.put(Tasks.R01, baseline1);
		expected.put(Tasks.R02, baseline2);
		expected.put(Tasks.R03, task1);
		expected.put(Tasks.R04, task2);
		expected.put(Tasks.R05, task3);
		expected.put(Tasks.R06, task4);
		expected.put(Tasks.R07, task1);
		expected.put(Tasks.R08, task2);
		expected.put(Tasks.R09, task3);
		expected.put(Tasks.R10, task4);
		expected.put(Tasks.R11, task1);
		expected.put(Tasks.R12, task2);
		expected.put(Tasks.R13, task3);
		expected.put(Tasks.R14, task4);
	}

	public List<Result> analyze(List<Signal> signals, int patients) {
		List<Result> results = new ArrayList<>();
		List<Integer> thresholds = createThresholdsBreakPoints();

		for(int i = 0; i < thresholds.size(); i++) {
			Map<Tasks, List<Signal>> task2signals = groupSignalsByTask(signals, thresholds.get(i));
			
			for (Entry<Tasks, List<Signal>> entry : task2signals.entrySet()) {
				 Map<Wave, Set<String>> wave2patients = groupWave2patientName(entry);
				 
				 Result result = new Result(thresholdInPercents.get(i), entry.getKey(), patients);
				 results.add(result);
				 
				 for (Entry<Wave, Set<String>> entry2 : wave2patients.entrySet()) {
					 Wave wave = entry2.getKey();
					 boolean isCorrectly = expected.get(entry.getKey()).isAmountExpected(wave);
					 result.add(entry2.getKey(), entry2.getValue().size(), isCorrectly);
				 }
				 result.fillMissingValues(expected.get(entry.getKey()));
			}
		}
		
		return results;
	}

	private Map<Wave, Set<String>> groupWave2patientName(Entry<Tasks, List<Signal>> entry) {
		return entry.getValue().stream()//		jak w excelu po progowaniu
				.collect(Collectors.groupingBy(//
						Signal::getWave, //
						Collectors.mapping(//
								Signal::getName,// 
								Collectors.toSet())));
	}

	private Map<Tasks, List<Signal>> groupSignalsByTask(List<Signal> signals, Integer threshold) {
		Map<Tasks, List<Signal>> task2signals = signals.stream()//
				.filter(s -> s.getOdprowadzenia().size() >= threshold)//
				.collect(Collectors.groupingBy(s -> Tasks.valueOf(s.getName().substring(4)), Collectors.toList()));
		return new TreeMap<>(task2signals);
	}

	private List<Integer> createThresholdsBreakPoints() {
		List<Integer> thresholds = new ArrayList<>();
		int iloscOdprowadzen = Odprowadzenie.values().length;
		
		for (int i = thresholdLow; i < thresholdHigh; i += thresholdStep) {
			thresholdInPercents.add(i);
			thresholds.add((int) ((i / 100.0) * iloscOdprowadzen));
		}
		return thresholds;
	}
	
	public List<Integer> getBreakPoints() {
		return thresholdInPercents;
	}
}
