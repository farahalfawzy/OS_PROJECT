package system;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class SystemCalls {
	public static void printData(String s) {
		System.out.println(s);
	}

	public static String takeInput() {
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		return input;
	}

	public static String readFile(String fileName) throws IOException {
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			String s = myReader.next();
			myReader.close();
			return s;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return "";
	}

	public static void writeFile(String fileName, String data) {
		try { // if not found create file
			File myObj = new File(fileName);
			if (myObj.createNewFile()) {
			} else {
				// System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try { // if file is found write over it
			FileWriter myWriter = new FileWriter(fileName);
			myWriter.write(data);
			myWriter.close();
			// System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}