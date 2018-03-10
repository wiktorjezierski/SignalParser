package EegDataParser.dataanalyze;

import java.util.ArrayList;
import java.util.List;

import EegDataParser.operationaldata.Wave;

public final class ExpectedWave {
	
	private List<Integer> amountOfWaves;

	public ExpectedWave(int...amountOfWaves) {
		this.amountOfWaves = new ArrayList<>();
		
		for (int i : amountOfWaves) {
			this.amountOfWaves.add(i);
		}
	}
	
	public boolean isAmountExpected(Wave wave) {
		return amountOfWaves.get(wave.ordinal()).intValue() == 1;
	}

}
