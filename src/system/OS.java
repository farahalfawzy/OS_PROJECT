package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import application.Interpreter;
import submodules.PState;
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
	private static Object[] temp = new Object[3];

	

	public static void unloadProcess(systemModules.Process process) {
		String filePath = "programFiles/diskSerialized.ser";/// enter later
		String textFilePath="programFiles/disk.txt";
		//write in text file
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
		//serializing process
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
		String textFilePath="programFiles/disk.txt";

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
		scheduler.setMaxSlice(timeSlice);
		int finished = 0;
		int arrived = 0;
		while (true) {
			time++;
			if (time == arrival[arrived])
				break;
		}
		Interpreter interpreter = new Interpreter();
		systemModules.Process process = interpreter.readProgram(programOrder[arrived++]);
		getMemory().loadProcess(process);
		int processId = process.getPCB().getPID();
		readyQueue.add(processId);
		// !readyQueue.isEmpty() || runningProcess != -1) { // what if someone came and
		// finished and the other is not here yet
		while (finished < programOrder.length) {
			if (runningProcess == -1)
				scheduler.dispatch();
			scheduler.decrementRunSlice();
			interpreter.executeInstr(runningProcess,memory.getInstruction(runningProcess));// resolve input
			if (getMemory().isFinished(runningProcess))
				finished++;
			if (getMemory().getProcessState(runningProcess).equals(PState.BLOCKED) || getMemory().isFinished(processId))
				setRunningProcess(-1);
			else if (scheduler.sliceFinished())
				scheduler.preempt();
			if (arrived < arrival.length && time == arrival[arrived]) {
				process = interpreter.readProgram(programOrder[arrived++]);
				getMemory().loadProcess(process);
				processId = process.getPCB().getPID();
				readyQueue.add(processId);
			}
			time++;
		}

	}

	public static Mutex getUserInput() {
		return userInput;
	}

	public void setUserInput(Mutex userInput) {
		this.userInput = userInput;
	}

	public static Mutex getUserOutput() {
		return userOutput;
	}

	public void setUserOutput(Mutex userOutput) {
		this.userOutput = userOutput;
	}

	public static Mutex getFile() {
		return file;
	}

	public void setFile(Mutex file) {
		this.file = file;
	}

	public static void setMemory(Memory memory) {
		OS.memory = memory;
	}

	public static void blockProcess() {
		blockedQueue.add(runningProcess);
		getMemory().setProcessState(runningProcess, PState.BLOCKED);
		setRunningProcess(-1);
	}

	public static void unBlockProcess(int processId) {
		blockedQueue.remove(processId);
		readyQueue.add(processId);
		getMemory().setProcessState(processId, PState.READY);
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
	public static Object[] getTemp() {
		return temp;
	}

	public static void setTemp(Object[] temp) {
		OS.temp = temp;
	}
}