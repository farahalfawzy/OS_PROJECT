package modules;

import java.io.Serializable;
import java.util.ArrayList;

public class Process implements Serializable {
	private static final long serialVersionUID = 1L;
	private PCB PCB;
	private ArrayList<String> instructions = new ArrayList<>();
	private Object variable1;
	private Object variable2;
	private Object variable3;
	private String filePath="";

	public PCB getPCB() {
		return PCB;
	}

	public void setPCB(PCB pCB) {
		PCB = pCB;
	}

	public ArrayList<String> getInstructions() {
		return instructions;
	}

	public Object getVariable1() {
		return variable1;
	}

	public void setVariable1(Object variable1) {
		this.variable1 = variable1;
	}

	public Object getVariable2() {
		return variable2;
	}

	public void setVariable2(Object variable2) {
		this.variable2 = variable2;
	}

	public Object getVariable3() {
		return variable3;
	}

	public void setVariable3(Object variable3) {
		this.variable3 = variable3;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
