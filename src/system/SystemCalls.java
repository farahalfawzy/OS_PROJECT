package system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import submodules.Variable;

import java.io.File;
import java.io.FileWriter;

public class SystemCalls {
	public static void printData(Object s) {
		System.out.println(s);
	}

	public static Object takeInput() {
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		//sc.close();
		return input;
	}

	public static String readFile(String fileName) throws IOException {
		try {
			File myFile = new File("programFiles/" + fileName + ".txt");
			Scanner myReader = new Scanner(myFile);
			String s = myReader.next();
			myReader.close();
			return s;
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
		return "";
	}

	public static void writeFile(String fileName, String data) {
		try {
			fileName = "programFiles/" + fileName + ".txt";
			File myFile = new File(fileName);
			if (!myFile.exists())
				myFile.createNewFile();
			FileWriter myWriter = new FileWriter(fileName);
			myWriter.write(data);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object readFromMem(int processID, String variable) {
		return OS.getMemory().getVariable(processID, variable);
	}

	public static void writeToMem(int processID, Object data, String varName) {
		Variable v = new Variable(varName, data);
		OS.getMemory().setVariable(processID, v);
	}

}