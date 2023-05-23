package systemModules;

import submodules.PState;
import system.OS;

public class Scheduler {
	private int runningSlice = 0;
	private int maxSlice = 0;

	public void dispatch() {
		OS.setRunningProcess(OS.getReadyQueue().remove());
		OS.getMemory().isLoaded(OS.getRunningProcess());
		OS.getMemory().setProcessState(OS.getRunningProcess(), PState.RUNNING);
		setRunSlice();
	}

	public void preempt() {
		OS.getReadyQueue().add(OS.getRunningProcess());
		OS.getMemory().setProcessState(OS.getRunningProcess(), PState.READY);
		OS.setRunningProcess(-1);
	}

	public void decrementRunSlice() {
		runningSlice--;
	}

	public void setRunSlice() {
		runningSlice = maxSlice;
	}

	public boolean sliceFinished() {
		if (runningSlice == 0)
			return true;
		return false;
	}

	public void setMaxSlice(int maxSlice) {
		this.maxSlice = maxSlice;
	}
}
