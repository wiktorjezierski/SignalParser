package EegDataParser.operationaldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Waves {

	private List<Integer> waves;
	
	public Waves(String input) {
		List<String> waves = Arrays.asList(input.split("\\s"));
		waves = new ArrayList<>(waves);
		waves.remove(0);
		waves.set(0, parse(waves.get(0)));
		this.waves = waves.stream().map(this::convert).collect(Collectors.toList());
	}

	private String parse(String wave) {
		return wave.substring(1);
	}

	private int convert(String wave) {
		return Integer.parseInt(wave);
	}

	public int getWave(Wave wave) {
		return waves.get(wave.ordinal());
	}
	
	@Override
	public String toString() {
		return waves.toString();
	}
}
