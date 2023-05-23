package systemModules;

import java.io.Serializable;
import java.util.ArrayList;

import submodules.PCB;
import submodules.Variable;

public class Process implements Serializable {
	private static final long serialVersionUID = 1L;
	private PCB PCB;
	private ArrayList<String> instructions = new ArrayList<String>();
	private Variable variable1 = new Variable();
	private Variable variable2 = new Variable();
	private Variable assignmentTemp = new Variable("temp");
	
	public PCB getPCB() {
		return PCB;
	}

	public void setPCB(PCB pCB) {
		PCB = pCB;
	}

	public ArrayList<String> getInstructions() {
		return instructions;
	}

	public Variable getVariable1() {
		return variable1;
	}

	public void setVariable1(Variable variable1) {
		this.variable1 = variable1;
	}

	public Variable getVariable2() {
		return variable2;
	}

	public void setVariable2(Variable variable2) {
		this.variable2 = variable2;
	}

	public Variable getAssignmentTemp() {
		return assignmentTemp;
	}

	public void setAssignmentTemp(Variable assignmentTemp) {
		this.assignmentTemp = assignmentTemp;
	}


}
