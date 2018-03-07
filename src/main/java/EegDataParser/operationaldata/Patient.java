package EegDataParser.operationaldata;

import java.util.ArrayList;
import java.util.List;

public class Patient {

	private List<Task> tasks;
	
	public Patient() {
		tasks = new ArrayList<>();
	}

	public List<Task> getTasks() {
		return tasks;
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
}
