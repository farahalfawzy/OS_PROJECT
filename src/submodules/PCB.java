package submodules;

import java.io.Serializable;
import java.util.Arrays;

public class PCB implements Serializable {


	private static final long serialVersionUID = 1L;
	public static int id = 0;
	private int PID;
	private PState state;
	private int PC = 0;
	private int[] kernelBound = new int[2];
	private int[] userBound = new int[2];

	public PCB() {
		this.PID = id++;
		this.state = PState.NEW;
	}

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
		return "PCB [PID=" + PID + ", state=" + state + ", PC=" + PC + ", kernelBound=" + Arrays.toString(kernelBound)
				+ ", userBound=" + Arrays.toString(userBound) + "]";
	}
}
