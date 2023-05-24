package system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	public static void unloadProcess(systemModules.Process process) {
		String filePath = "disk";/// enter later
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
		String filePath = "disk";/// enter later
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
			interpreter.executeInstr(runningProcess);// resolve input later.........................................................
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
}