package EegDataParser.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import EegDataParser.operationaldata.Patient;
import EegDataParser.operationaldata.Task;

public class FileReader {

	public static List<Patient> readFiles(String path) {
		try(Stream<Path> files = Files.find(Paths.get(path), Integer.MAX_VALUE,(filePath, fileAttr) -> fileAttr.isRegularFile());) {
			return files.parallel()//
					.map(e -> readFile(e))
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private static Patient readFile(Path path) {
		try {
			List<String> allLines = Files.readAllLines(path);
			boolean isNewTask = true;
			Patient patient = new Patient();
			Task task = null;
			
			for (int i = 0; i < allLines.size(); i++) {
				String line = allLines.get(i);
				if (isNewTask && i + 1 < allLines.size()) {
					if(i != 0 ) {
						line = allLines.get(++i);
					}
					task = new Task(line);
					patient.addTask(task);
					isNewTask = false;
				} else {
					isNewTask = !task.addWave(line);
				}
			}
			
			return patient;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
