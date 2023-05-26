package systemModules;

import submodules.PCB;
import submodules.PState;
import submodules.Variable;
import system.OS;

public class Memory {
	private Object[] memory = new Object[40];

	public void loadProcess(Process process) {
		if (memory[0] == null) {
			int currentPc = process.getPCB().getPC();
			if (currentPc == 0)
				process.getPCB().setPC(10);
			else if (currentPc >= 25)
				process.getPCB().setPC(currentPc - 15);
			process.getPCB().setState(PState.READY);
			process.getPCB().setKernelBound(0, 4);
			process.getPCB().setUserBound(10, 24);
			fillKernelMemory(0, process.getPCB());
			fillUserMemory(10, process);
			return;
		} else if (memory[5] == null) {
			int currentPc = process.getPCB().getPC();
			if (currentPc == 0)
				process.getPCB().setPC(25);
			else if (currentPc < 25)
				process.getPCB().setPC(currentPc + 15);
			process.getPCB().setState(PState.READY);
			process.getPCB().setKernelBound(5, 10);
			process.getPCB().setUserBound(25, 39);
			fillKernelMemory(5, process.getPCB());
			fillUserMemory(25, process);
			return;
		} else {
			swapToDisk();
			loadProcess(process);
		}

	}

	public void isLoaded(int processID) {
		if ((memory[0] != null && memory[0].equals(processID)) || (memory[5] != null && memory[5].equals(processID)))
			return;
		Process process = OS.loadProcess();
		swapToDisk();
		System.out.println(
				"At Time " + OS.getTime() + " Process " + process.getPCB().getPID() + " is Swapped in From Disk.\n");
		loadProcess(process);
	}

	public void clearMemory(int start, int end) {
		for (int i = start; i <= end; i++)
			memory[i] = null;
	}

	public void fillUserMemory(int startUser, Process process) {
		for (String instruction : process.getInstructions())
			memory[startUser++] = instruction;
		memory[startUser++] = process.getVariable1();
		memory[startUser++] = process.getVariable2();
		memory[startUser] = process.getVariable3();
	}

	public void fillKernelMemory(int startKernel, PCB pcb) {
		memory[startKernel++] = pcb.getPID();
		memory[startKernel++] = pcb.getState();
		memory[startKernel++] = pcb.getPC();
		memory[startKernel++] = pcb.getKernelBound();
		memory[startKernel] = pcb.getUserBound();
	}

	public void swapToDisk() {
		if (memory[1].equals(PState.RUNNING) || memory[6].equals(PState.BLOCKED) || memory[6].equals(PState.FINISHED)) {
			Process process = rebuildProcess(5, 25);
			System.out.println(
					"At Time " + OS.getTime() + " Process " + process.getPCB().getPID() + " is Swapped Out To Disk.\n");
			OS.unloadProcess(process);
			clearMemory(5, 9);
			clearMemory(25, 39);
		} else {
			Process process = rebuildProcess(0, 10);
			System.out.println(
					"At Time " + OS.getTime() + " Process " + process.getPCB().getPID() + " is Swapped Out To Disk.\n");
			OS.unloadProcess(process);
			clearMemory(0, 4);
			clearMemory(10, 24);
		}
	}

	public Process rebuildProcess(int startKernel, int startUser) {
		Process process = new Process();
		process.setPCB(new PCB());
		process.getPCB().setPID((int) memory[startKernel++]);
		process.getPCB().setState((PState) memory[startKernel++]);
		process.getPCB().setPC((int) memory[startKernel++]);
		process.getPCB().setKernelBound((int[]) memory[startKernel++]);
		process.getPCB().setUserBound((int[]) memory[startKernel]);
		int temp = startUser;
		while (startUser < temp + 15) {
			if (!(memory[startUser] instanceof Variable))
				process.getInstructions().add((String) memory[startUser++]);
			else
				break;
		}
		process.setVariable1((Variable) memory[startUser++]);
		process.setVariable2((Variable) memory[startUser++]);
		process.setVariable3((Variable) memory[startUser]);
		return process;
	}

	public PCB rebuildPCB(int processId) {
		PCB pcb = new PCB();
		int startKernel = 0;
		if (memory[5] != null && memory[5].equals(processId))
			startKernel = 5;
		pcb.setPID((int) memory[startKernel++]);
		pcb.setState((PState) memory[startKernel++]);
		pcb.setPC((int) memory[startKernel++]);
		pcb.setKernelBound((int[]) memory[startKernel++]);
		pcb.setUserBound((int[]) memory[startKernel]);
		return pcb;
	}

	public PState getProcessState(int processId) {
		if (memory[5] != null && memory[5].equals(processId))
			return (PState) memory[6];
		else if (memory[0] != null && memory[0].equals(processId))
			return (PState) memory[1];
		else
			return null;
	}

	public void setProcessState(int processId, PState state) {
		if (memory[5] != null && memory[5].equals(processId))
			memory[6] = state;
		else if (memory[0] != null && memory[0].equals(processId))
			memory[1] = state;
	}

	public boolean isFinished(int processID) {
		int PC = -1;
		if (memory[0] != null && memory[0].equals(processID))
			PC = (Integer) memory[2];

		if (memory[5] != null && memory[5].equals(processID))
			PC = (Integer) memory[7];
		if (memory[PC] instanceof Variable) {
			setProcessState(processID, PState.FINISHED);
			return true;
		}
		return false;

	}

	public Object[] getMemory() {
		return memory;
	}

	// new
	public String getInstruction(int processID) {
		int PC = -1;
		if (memory[0] != null && memory[0].equals(processID)) {
			PC = (Integer) memory[2];
			memory[2] = (Integer) memory[2] + 1;
		}

		if (memory[5] != null && memory[5].equals(processID)) {
			PC = (Integer) memory[7];
			memory[7] = (Integer) memory[7] + 1;

		}
		return (String) memory[PC];

	}

	public Object getVariable(int processID, String variable) {
		int startUser = 0;
		int endUser = 0;
		if (memory[0] != null && memory[0].equals(processID)) {
			startUser = ((int[]) memory[4])[0];
			endUser = ((int[]) memory[4])[1];
		} else {
			startUser = ((int[]) memory[9])[0];
			endUser = ((int[]) memory[9])[1];
		}
		for (int i = startUser; i <= endUser; i++)
			if (memory[i] instanceof Variable && ((Variable) memory[i]).getName() != null
					&& ((Variable) memory[i]).getName().equalsIgnoreCase(variable))
				return ((Variable) memory[i]).getValue();
		return null;

	}

	public void setVariable(int processID, Variable variable) {
		int startUser = 0;
		int endUser = 0;
		if (memory[0] != null && memory[0].equals(processID)) {
			startUser = ((int[]) memory[4])[0];
			endUser = ((int[]) memory[4])[1];
		} else {
			startUser = ((int[]) memory[9])[0];
			endUser = ((int[]) memory[9])[1];
		}
		for (int i = startUser; i <= endUser; i++) {
			if (memory[i] instanceof Variable && ((Variable) memory[i]).getName() != null
					&& ((Variable) memory[i]).getName().equals(variable.getName())) {
				memory[i] = variable;
				return;
			}
		}
		for (int i = startUser; i <= endUser; i++) {
			if (memory[i] instanceof Variable && ((Variable) memory[i]).getName() == null) {
				memory[i] = variable;
				return;
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String[] printHelp = { "Process ID", "State", "PC", "Kernel Space Bound", "User Space Bound" };
		sb.append("At Time " + OS.getTime() + " Memory:\n").append("\n");
		sb.append("Keranl Space:").append("\n");
		for (int i = 0; i < 10; i++) {
			if (memory[i] == null)
				sb.append("Cell " + i + ": Empty").append("\n");
			else if (i < 5)
				sb.append("Cell " + i + ": " + printHelp[i] + " " + toStringHelper(memory[i])).append("\n");
			else
				sb.append("Cell " + i + ": " + printHelp[i - 5] + " " + toStringHelper(memory[i])).append("\n");
		}
		sb.append("User Space:").append("\n");
		for (int i = 10; i < 40; i++) {
			if (memory[i] == null)
				sb.append("Cell " + i + ": Empty").append("\n");
			else if (!(memory[i] instanceof Variable))
				sb.append("Cell " + i + ": Instruction " + toStringHelper(memory[i])).append("\n");
			else if (memory[i].toString().equals(""))
				sb.append("Cell " + i + ": Empty").append("\n");
			else
				sb.append("Cell " + i + ": " + toStringHelper(memory[i])).append("\n");
		}
		return sb.toString();

	}

	private String toStringHelper(Object o) {
		if (o instanceof int[]) {
			int[] bounds = (int[]) o;
			int min = bounds[0];
			int max = bounds[1];
			return "[" + min + "," + max + "]";
		} else
			return o.toString();

	}
}
