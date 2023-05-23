package modules;

public class Memory {
	private Object[] memory = new Object[40];

	public void loadProcess(Process process) {
		if (memory[0] == null) {
			process.setPCB(new PCB(10, 24));
			fillUserMemory(0, 10, process);
			return;
		} else if (memory[5] == null) {
			process.setPCB(new PCB(25, 39));
			fillUserMemory(5, 25, process);
			return;
		}else {
			//swap to disk thing
		}

	}

	public void clearMemory(int start, int end) {
		for (int i = start; i <= end; i++)
			memory[i] = null;
	}

	public void fillUserMemory(int startKernal, int startUser, Process process) {
		for (String instruction : process.getInstructions())
			memory[startUser++] = instruction;
		memory[startUser++] = process.getVariable1();
		memory[startUser++] = process.getVariable1();
		memory[startUser++] = process.getVariable1();

		fillKernalMemory(startKernal, process.getPCB());
	}

	public void fillKernalMemory(int startKernal, PCB pcb) {
		memory[startKernal++] = pcb.getPID();
		memory[startKernal++] = pcb.getState();
		memory[startKernal++] = pcb.getPC();
		memory[startKernal++] = pcb.getMin();
		memory[startKernal++] = pcb.getMax();
	}
}
