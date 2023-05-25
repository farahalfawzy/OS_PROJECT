package submodules;

import java.io.Serializable;

public class PCB implements Serializable {

	private static final long serialVersionUID = 1L;
	private int PID;
	private PState state = PState.NEW;
	private int PC = 0;
	private int[] kernelBound = new int[2];
	private int[] userBound = new int[2];

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public PState getState() {
		return state;
	}

	public void setState(PState state) {
		this.state = state;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public int[] getKernelBound() {
		return kernelBound;
	}

	public void setKernelBound(int min, int max) {
		kernelBound[0] = min;
		kernelBound[1] = max;
	}

	public void setKernelBound(int[] kernelBound) {
		this.kernelBound = kernelBound;
	}

	public int[] getUserBound() {
		return userBound;
	}

	public void setUserBound(int min, int max) {
		userBound[0] = min;
		userBound[1] = max;

	}

	public void setUserBound(int[] userBound) {
		this.userBound = userBound;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Process ID ").append(PID).append("\n");
		sb.append("State ").append(state).append("\n");
		sb.append("PC ").append(PC).append("\n");
		sb.append("Kernel Space Bound ").append("[" + kernelBound[0] + "," + kernelBound[1] + "]").append("\n");
		sb.append("User Space Bound ").append("[" + userBound[0] + "," + userBound[1] + "]").append("\n");
		return sb.toString();
	}
}
