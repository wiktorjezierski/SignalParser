package EegDataParser.starter;

import java.util.List;

import EegDataParser.algorithm.SignalGroupper;
import EegDataParser.io.FileReader;
import EegDataParser.operationaldata.Patient;

public class Starter {

	public static void main(String[] args) {
		List<Patient> patients = FileReader.readFiles(args[0]);
		SignalGroupper groupper = new SignalGroupper();
		groupper.groupSignals(patients);
	}

}
