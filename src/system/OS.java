package system;

import java.util.LinkedList;
import java.util.Queue;

import modules.Memory;
import modules.Mutex;
import modules.Scheduler;

public class OS {
	private static OS operatingSystem;
	private static Queue<Integer> blockedQueue = new LinkedList<>();
	private static Queue<Integer> readyQueue = new LinkedList<>();
	private static int runningProcess = 0;
	private static int time = 0;
	private Scheduler scheduler = new Scheduler();
	private Memory memory = new Memory();
	private Mutex userInput = new Mutex(1);
	private Mutex userOutput = new Mutex(1);
	private Mutex file = new Mutex(1);

	public static OS getInstance() {
		if (operatingSystem == null)
			synchronized (OS.class) {
				operatingSystem = new OS();
			}
		return operatingSystem;
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