package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import systemModules.Process;

public class Interpreter {
	public Process readProgram(int programID) {
		String filePath = "programFiles/Program_" + programID + ".txt";
		Process process = new Process();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("assign")) {
					String[] assignInstr = line.split(" ");
					if (assignInstr[2].equalsIgnoreCase("input")) {
						process.getInstructions().add("input");
						process.getInstructions().add(assignInstr[0] + " " + assignInstr[1] + " temp");
					} else if (assignInstr[2].equalsIgnoreCase("readFile")) {
						process.getInstructions().add(assignInstr[2] + " " + assignInstr[3]);
						process.getInstructions().add(assignInstr[0] + " " + assignInstr[1] + " temp");
					} else
						process.getInstructions().add(line);

				} else
					process.getInstructions().add(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(process.getInstructions().size());
		return process;

	}

	public void executeInstr(String instruction) {
		
	}
}
