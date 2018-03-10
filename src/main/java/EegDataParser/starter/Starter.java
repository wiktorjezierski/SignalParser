package EegDataParser.starter;

import java.util.List;

import EegDataParser.algorithm.SignalGroupper;
import EegDataParser.dataanalyze.CalculateStatistics;
import EegDataParser.dataanalyze.Result;
import EegDataParser.dataanalyze.SignalAnalyzer;
import EegDataParser.io.FileReader;
import EegDataParser.operationaldata.Patient;
import EegDataParser.operationaldata.Signal;

public class Starter {

	private static final int FILE_PATH = 0;

	public static void main(String[] args) {
		List<Patient> patients = FileReader.readFiles(args[FILE_PATH]);
		SignalGroupper groupper = new SignalGroupper();
		List<Signal> signals = groupper.groupSignals(patients);

		SignalAnalyzer analyzer = new SignalAnalyzer(10, 100, 10);
		List<Result> result = analyzer.analyze(signals, patients.size());
		
		CalculateStatistics calculateStatistics = new CalculateStatistics();
		calculateStatistics.calculate(result, analyzer.getBreakPoints());
	}

}
