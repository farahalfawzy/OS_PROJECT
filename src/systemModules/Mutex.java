package systemModules;

import java.util.LinkedList;
import java.util.Queue;

import submodules.MutexValue;
import system.OS;

public class Mutex {
	private Queue<Integer> blockedQueue = new LinkedList<>();
	private int owner;
	private MutexValue value;

	public Mutex(int value) {
		if (value == 0)
			this.value = MutexValue.ZERO;
		else
			this.value = MutexValue.ONE;
	}

	public static void semWait(Mutex mutex) {
		if (mutex.value == MutexValue.ONE) {
			mutex.owner = OS.getRunningProcess();
			mutex.value = MutexValue.ZERO;
		} else {
			mutex.blockedQueue.add(OS.getRunningProcess());
			OS.getBlockedQueue().add(OS.getRunningProcess());
			// block it in the main blocked queue
		}
	}

	public static void semSignal(Mutex mutex) {
		if (OS.getRunningProcess() != mutex.owner)
			return;

		if (mutex.blockedQueue.isEmpty())
			mutex.value = MutexValue.ONE;
		else {
			int processId = mutex.blockedQueue.remove();
			mutex.owner = processId;
			OS.getBlockedQueue().remove(processId);
			OS.getReadyQueue().add(processId);
			// put p in readylist
		}
	}
}
