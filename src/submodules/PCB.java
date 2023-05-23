package submodules;

import java.io.Serializable;

public class PCB implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int id = 0;
	private int PID;
	private PState state;
	private int PC = 0;
	private int[] kernalBound = new int[2];
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

	public int[] getKernalBound() {
		return kernalBound;
	}

	public void setKernalBound(int min, int max) {
		kernalBound[0] = min;
		kernalBound[1] = max;
	}

	public void setKernalBound(int[] kernalBound) {
		this.kernalBound = kernalBound;
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

}
