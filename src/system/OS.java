package system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import systemModules.Memory;
import systemModules.Mutex;
import systemModules.Scheduler;

public class OS {
	private static Queue<Integer> blockedQueue = new LinkedList<>();
	private static Queue<Integer> readyQueue = new LinkedList<>();
	private static int runningProcess = 0;
	private static int time = 0;
	private Scheduler scheduler = new Scheduler();
	private Memory memory = new Memory();
	private Mutex userInput = new Mutex(1);
	private Mutex userOutput = new Mutex(1);
	private Mutex file = new Mutex(1);

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
		return restoredProcess;
	}

	public void runInterpreter(int timeSlice, int[] programOrder, int[] arrival) {

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
}