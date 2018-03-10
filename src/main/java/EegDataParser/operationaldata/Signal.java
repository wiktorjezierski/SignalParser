package EegDataParser.operationaldata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	@Override
	public String toString() {
		String odprowadzeniaString = odprowadzenia.stream()//
				.map(Odprowadzenie::toString)//
				.collect(Collectors.joining(", "));
		
		return String.format("%s %s %s", name, wave.toString(), odprowadzeniaString);
	}
}
