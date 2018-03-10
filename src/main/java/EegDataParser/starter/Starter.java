package EegDataParser.starter;

import java.util.List;

import EegDataParser.algorithm.SignalGroupper;
import EegDataParser.io.FileReader;
import EegDataParser.operationaldata.Patient;
import EegDataParser.operationaldata.Signal;

public class Starter {

	private static final int FILE_PATH = 0;
	private static final int THRESHOLD = 1;

	public static void main(String[] args) {
		List<Patient> patients = FileReader.readFiles(args[FILE_PATH]);
		SignalGroupper groupper = new SignalGroupper();
		List<Signal> signals = groupper.groupSignals(patients);

		int threshold = Integer.parseInt(args[THRESHOLD]);
		signals.stream()//
				.filter(s -> s.getOdprowadzenia().size() > threshold)//
				.forEach(s -> System.out.println(s.toString()));
	}

}
