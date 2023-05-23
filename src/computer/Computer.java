package computer;

import system.OS;

public class Computer {
	OS operatingSystem;

	public void startOS() {
		operatingSystem = new OS();
	}

	public static void main(String[] args) {
		Computer computer=new Computer();
		computer.startOS();
		int timeslice=2;
		int [] programOrder= {1,2,3};
		int [] arrivalTimes= {0,1,4};
		computer.operatingSystem.runInterpreter(timeslice, programOrder, arrivalTimes);
	}
}
