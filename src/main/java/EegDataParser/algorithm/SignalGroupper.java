package EegDataParser.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import EegDataParser.masterdata.Odprowadzenie;
import EegDataParser.operationaldata.Patient;
import EegDataParser.operationaldata.Signal;
import EegDataParser.operationaldata.Task;
import EegDataParser.operationaldata.Wave;

public class SignalGroupper {

	public List<Signal> groupSignals(List<Patient> patients) {
		return patients.parallelStream()//
		.flatMap(e -> resolvesignal(e).stream())//
		.collect(Collectors.toList());
	}

	private List<Signal> resolvesignal(Patient patient) {
		List<Signal> signals = new ArrayList<>();
		for (Task task : patient.getTasks()) {
			checkWaves(signals, task);
		}
		
		return signals;
	}

	private void checkWaves(List<Signal> signals, Task task) {
		for (Wave wave : Wave.values()) {
			checkOdprowadzenia(signals, task, wave);
		}
	}

	private void checkOdprowadzenia(List<Signal> signals, Task task, Wave wave) {
		Set<Odprowadzenie> checked = new HashSet<>();
		for (Odprowadzenie odprowadzenie : Odprowadzenie.values()) {
			Queue<Odprowadzenie> toCheck = createQueueWithInitialValue(odprowadzenie);
			Signal signal = findSignal(task, wave, toCheck, checked, null);
			if(signal != null)
				signals.add(signal);
		}
	}

	private Queue<Odprowadzenie> createQueueWithInitialValue(Odprowadzenie odprowadzenie) {
		Queue<Odprowadzenie> toCheck = new LinkedBlockingQueue<>();
		toCheck.add(odprowadzenie);
		return toCheck;
	}

	private Signal findSignal(Task task, Wave wave, Queue<Odprowadzenie> toCheck, Set<Odprowadzenie> checked, Signal signal) {
		if(toCheck.isEmpty() || checked.contains(toCheck.peek())) {
			toCheck.poll();
			return signal;
		}
		
		Odprowadzenie current = toCheck.poll();
		checked.add(current);
		
		int numberOfWavesInSignal = task.getWave(current).getWave(wave);
		if(isAtLeastOneSignal(numberOfWavesInSignal)) {
			toCheck.addAll(retrieveNeighbors(current));
			signal = createOrExtendSignal(signal, task, wave, current);
		}
		
		while(!toCheck.isEmpty()) {
			signal = findSignal(task, wave, toCheck, checked, signal);
		}
		
		return signal;
	}

	private Signal createOrExtendSignal(Signal signal, Task task, Wave wave, Odprowadzenie current) {
		if(signal == null) {
			return new Signal(task.getName(), wave, current);
		}
		signal.addOdprowadzenie(current);
		return signal;
	}

	private Collection<? extends Odprowadzenie> retrieveNeighbors(Odprowadzenie odprowadzenie) {
		return odprowadzenie.getNeighbors().stream() //
				.map(Odprowadzenie::valueOf) //
				.collect(Collectors.toList());
	}

	private boolean isAtLeastOneSignal(int numberOfWavesInSignal) {
		return numberOfWavesInSignal > 0;
	}
}
