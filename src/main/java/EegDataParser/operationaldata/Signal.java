package EegDataParser.operationaldata;

import java.util.ArrayList;
import java.util.List;

import EegDataParser.masterdata.Odprowadzenie;

public class Signal {
	
	private String name;
	private Wave wave;
	private List<Odprowadzenie> odprowadzenia;
	
	public Signal(String name, Wave wave, Odprowadzenie odprowadzenie) {
		this.name = name;
		this.wave = wave;
		odprowadzenia = new ArrayList<>();
		addOdprowadzenie(odprowadzenie);
	}

	public String getName() {
		return name;
	}

	public Wave getWave() {
		return wave;
	}

	public List<Odprowadzenie> getOdprowadzenia() {
		return odprowadzenia;
	}
	
	public void addOdprowadzenie(Odprowadzenie odprowadzenie) {
		odprowadzenia.add(odprowadzenie);
	}
}
