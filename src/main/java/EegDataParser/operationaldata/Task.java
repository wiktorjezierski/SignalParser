package EegDataParser.operationaldata;

import java.util.ArrayList;
import java.util.List;

import EegDataParser.masterdata.Odprowadzenie;

public class Task {

	private List<Waves> waves;
	private String name;

	public Task(String name) {
		this.name = name.substring(5, 12);
		waves = new ArrayList<>();
	}
	
	public boolean addWave(String row) {
		waves.add(new Waves(row));
		return waves.size() < 64;
	}

	public List<Waves> getWaves() {
		return waves;
	}
	
	public Waves getWave(Odprowadzenie odprowadzenie) {
		return waves.get(odprowadzenie.ordinal());
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return waves.toString();
	}
	
}
