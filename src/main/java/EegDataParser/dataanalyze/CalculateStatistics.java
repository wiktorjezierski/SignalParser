package EegDataParser.dataanalyze;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import EegDataParser.operationaldata.Tasks;
import EegDataParser.operationaldata.Wave;

public class CalculateStatistics {

	public void calculate(List<Result> result, List<Integer> breakPoints, int patientsSize) {
		calculatePerTask(result, breakPoints, patientsSize);
	}

	private void calculatePerTask(List<Result> results, List<Integer> breakPoints, int patientsSize) {
		Map<Key, Result> key2result = results.stream()//
				.collect(Collectors.toMap(//
						e -> new Key(e.getTask(), e.getThreshold()), //
						e -> e));//
		
		for(Integer breakPoint : breakPoints) {
			Map<Wave, Sumator> statistics = calculateStatistics(key2result, breakPoint, patientsSize);
			display(breakPoint, statistics);
		}
	}

	private Map<Wave, Sumator> calculateStatistics(Map<Key, Result> key2result, Integer breakPoint, int patientsSize) {
		Map<Wave, Sumator> statistics = initializeStatistics(patientsSize);
		for(Tasks task : Tasks.values()) {
			Result result = findResult(key2result, breakPoint, task);

			for (Entry<Wave, Data> entry : result.getData().entrySet()) {
				Sumator sumator = statistics.get(entry.getKey());
				sumator.addValues(entry.getValue());
			}
		}
		
		return statistics;
	}

	private void display(Integer breakPoint, Map<Wave, Sumator> statistics) {
		System.out.println("\nbreakpoint " + breakPoint);
		String defaultFormat = "%s\t%s";
		String specificFormat = "%s\t\t%s";
		String format;
		for (Entry<Wave, Sumator> entry : statistics.entrySet()) {
			Wave wave = entry.getKey();
			if(wave == Wave.DELTA || wave == Wave.THETA || wave == Wave.GAMMA)  {
				format = specificFormat;
			}
			else {
				format = defaultFormat;
			}
			
			System.out.println(String.format(format, wave, entry.getValue()));
		}
		
	}

	private Result findResult(Map<Key, Result> key2result, Integer breakPoint, Tasks task) {
		Result result = key2result.get(new Key(task, breakPoint));
		if(result == null) {
			throw new RuntimeException("missing values");
		}
		return result;
	}

	private Map<Wave, Sumator> initializeStatistics(int patientsSize) {
		Map<Wave, Sumator> statistics = new EnumMap<>(Wave.class);
		for (Wave wave : Wave.values()) {
			statistics.put(wave, new Sumator(patientsSize));
		}
		
		return statistics;
	}
	
	private static class Sumator {
		private int ok;
		private int notOk;
		private int patientsSize;
		
		public Sumator(int patientsSize) {
			this.patientsSize = patientsSize;
			this.ok = 0;
			this.notOk = 0;
		}

		public Sumator addValues(Data data) {
			if(data.getIsCorrectly()) {
				ok += data.getAmountOfPatients();
				notOk += patientsSize - data.getAmountOfPatients();
			}
			else {
				notOk += data.getAmountOfPatients();
				ok += patientsSize - data.getAmountOfPatients();
			}
			return this;
		}
		
		@Override
		public String toString() {
			return String.format("%d\t%d\t%d%%\t%d%%", ok, notOk, ok/patientsSize, notOk/patientsSize);
		}
	}

	private static class Key {
		private Tasks tasks;
		private Integer breakPoint;

		public Key(Tasks tasks, Integer breakPoint) {
			this.tasks = tasks;
			this.breakPoint = breakPoint;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((breakPoint == null) ? 0 : breakPoint.hashCode());
			result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (breakPoint == null) {
				if (other.breakPoint != null)
					return false;
			} else if (!breakPoint.equals(other.breakPoint))
				return false;
			if (tasks != other.tasks)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Key tasks=" + tasks + ", breakPoint=" + breakPoint;
		}

	}
}
