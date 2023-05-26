package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import submodules.PState;
import systemModules.Interpreter;
import systemModules.Memory;
import systemModules.Mutex;
import systemModules.Scheduler;

public class OS {
	private static Queue<Integer> blockedQueue = new LinkedList<>();
	private static Queue<Integer> readyQueue = new LinkedList<>();
	private static int runningProcess = -1;
	private static int time = 0;
	private static Scheduler scheduler = new Scheduler();
	private static Memory memory = new Memory();
	private static Mutex userInput = new Mutex(1);
	private static Mutex userOutput = new Mutex(1);
	private static Mutex file = new Mutex(1);
	private static Hashtable<Integer, Object> temp= new Hashtable<Integer, Object>();
//	private static Object[] temp = new Object[3];
	static StringBuffer prints = new StringBuffer();

	public static void unloadProcess(systemModules.Process process) {
		String filePath = "programFiles/diskSerialized.ser";/// enter later
		String textFilePath = "programFiles/disk.txt";
		// write in text file
		try { // if not found create file
			File myObj = new File(textFilePath);
			if (myObj.createNewFile()) {
			} else {
				// System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try { // if file is found write over it
			FileWriter myWriter = new FileWriter(textFilePath);
			myWriter.write(process.toString());
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// serializing process
		try {
			ObjectOutputStream ObjectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
			ObjectOutputStream.writeObject(process);
			ObjectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static systemModules.Process loadProcess() {
		systemModules.Process restoredProcess = null;
		String filePath = "programFiles/diskSerialized.ser";/// enter later
		String textFilePath = "programFiles/disk.txt";

		try { // delete content of Disk.txt
			FileWriter myWriter = new FileWriter(textFilePath);
			myWriter.write("");
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
			restoredProcess = (systemModules.Process) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		restoredProcess.getPCB().setState(PState.READY);
		return restoredProcess;
	}

	public void runInterpreter(int timeSlice, int[] programOrder, int[] arrival) {
		int finished = 0;
		int arrived = 0;
		Interpreter interpreter = new Interpreter();
		scheduler.setMaxSlice(timeSlice);
		while (finished < programOrder.length) {
			// load arrived
			if (arrived < arrival.length && time == arrival[arrived]) {
				systemModules.Process process = interpreter.readProgram(programOrder[arrived++]);
				memory.loadProcess(process);
				int processId = process.getPCB().getPID();
				readyQueue.add(processId);
			}
			// dispatch a process
			if (runningProcess == -1 && !readyQueue.isEmpty()) {
				scheduler.dispatch();
				printDispatched();
			}
			else {
				System.out.println("Ready Queue : " + readyQueue);
				System.out.println("Blocked Queue : " + blockedQueue + ".\n");
				prints.append("Ready Queue : " + readyQueue + "\n");
				prints.append("Blocked Queue : " + blockedQueue + "\n");
			}
			// execute the process quantum and set running process to -1 if blocked or
			// finished
			if (runningProcess != -1) {
				
				System.out.println("At Time " + time + ": Process " + runningProcess + " is currently executing.\n");
				prints.append("At Time " + time + ": Process " + runningProcess + " is currently executing.\n");
				scheduler.decrementRunSlice();
				String instructionExecuting = memory.getInstruction(runningProcess);
				System.out.println("At Time " + time + ": Instruction " + instructionExecuting + " From Process "
						+ runningProcess + " is currently executing.\n");
				prints.append("At Time " + time + ": Instruction " + instructionExecuting + " From Process "
						+ runningProcess + " is currently executing.\n");
				interpreter.executeInstr(runningProcess, instructionExecuting);
				if (memory.getProcessState(runningProcess).equals(PState.BLOCKED)) {
					printBlocked();
					setRunningProcess(-1);
				} else if (memory.isFinished(runningProcess)) {
					finished++;
					printFinished();
					setRunningProcess(-1);
				} else if (scheduler.sliceFinished())
					scheduler.preempt();
			} else {
				
				System.out.println("At Time " + time + ": No Process is currently executing.\n");
				prints.append("At Time " + time + ": No Process is currently executing.\n");
			}
			printMemory();
			printDisk();
			time++;
		}
		printTraceToFile();
	}

	private void printFinished() {
		System.out.println("Process " + runningProcess + " finished.");
		System.out.println("Ready Queue : " + readyQueue);
		System.out.println("Blocked Queue : " + blockedQueue + ".\n");
		prints.append("Process " + runningProcess + " finished.\n");
		prints.append("Ready Queue : " + readyQueue + "\n");
		prints.append("Blocked Queue : " + blockedQueue + "\n");

	}

	private void printBlocked() {
		System.out.println("Process " + runningProcess + " blocked.");
		System.out.println("Ready Queue : " + readyQueue);
		System.out.println("Blocked Queue : " + blockedQueue + ".\n");
		prints.append("Process " + runningProcess + " blocked.\n");
		prints.append("Ready Queue : " + readyQueue + "\n");
		prints.append("Blocked Queue : " + blockedQueue + "\n");
	}

	private void printDispatched() {
		System.out.println("Process " + runningProcess + " Dispatched.");
		System.out.println("Ready Queue : " + readyQueue);
		System.out.println("Blocked Queue : " + blockedQueue + ".\n");
		prints.append("Process " + runningProcess + " Dispatched.\n");
		prints.append("Ready Queue : " + readyQueue + "\n");
		prints.append("Blocked Queue : " + blockedQueue + "\n");
	}

	private void printDisk() {
		File file = new File("programFiles/disk.txt");
		String diskContent = "At Time " + time + " Disk Contains :\n";
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("programFiles/disk.txt"));
				String line = br.readLine();
				while (line != null) {
					diskContent += line + "\n";
					line = br.readLine();
				}
				br.close();
				System.out.println(diskContent);
				prints.append(diskContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("At Time " + time + " No Processes Swapped Out On Disk. \n");
			prints.append("At Time " + time + " No Processes Swapped Out On Disk. \n");
		}
	}

	private void printMemory() {
		String s = memory.toString();
		System.out.println(s);
		prints.append(s);
	}

	private void printTraceToFile() {
		String textFilePath = "programFiles/tracing.txt";
		try {
			File myfile = new File(textFilePath);
			if (myfile.exists())
				myfile.createNewFile();
			else {
				FileWriter myWriter = new FileWriter(textFilePath);
				myWriter.write(prints.toString());
				myWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void blockProcess() {
		blockedQueue.add(runningProcess);
		getMemory().setProcessState(runningProcess, PState.BLOCKED);
	}

	public static void unBlockProcess(int processId) {
		blockedQueue.remove(processId);
		readyQueue.add(processId);
		getMemory().setProcessState(processId, PState.READY);
	}

	public static Mutex getUserInput() {
		return userInput;
	}

	public static Mutex getUserOutput() {
		return userOutput;
	}

	public static Mutex getFile() {
		return file;
	}

	public static void setMemory(Memory memory) {
		OS.memory = memory;
	}

	public static Queue<Integer> getBlockedQueue() {
		return blockedQueue;
	}

	public static Queue<Integer> getReadyQueue() {
		return readyQueue;
	}

	public static int getTime() {
		return time;
	}

	public static int getRunningProcess() {
		return runningProcess;
	}

	public static void setRunningProcess(int runningProcess) {
		OS.runningProcess = runningProcess;
	}

	public static Memory getMemory() {
		return memory;
	}

	public static Hashtable<Integer, Object> getTemp() {
		return temp;
	}

	
}