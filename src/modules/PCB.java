package modules;

public class PCB {
	public static int id = 0;
	private int PID;
	private PState state;
	private int PC;
	private int min;
	private int max;

	public PCB(int min, int max) {
		this.PID = id++;
		this.state = PState.NEW;
		this.min = min;
		this.PC = min;
		this.max = max;
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

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
