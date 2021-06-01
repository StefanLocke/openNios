package alternateSimulator;

import java.util.HashMap;

import niosSimulator.Instruction;
import openDLX.gui.GUI_CONST;
import riscvSimulator.InstructionRiscV;

public class CycleDescription {

	private HashMap<InstructionRiscV, String> pipelineMap;
	private InstructionRiscV fetchedInstruction;
	private int cycleNumber;
	private boolean isStall;
	
	public CycleDescription(int cycleNumber){
		this.pipelineMap = new HashMap<InstructionRiscV, String>();
		isStall = false;
		this.cycleNumber = cycleNumber;
	}
	
	
	public void addPipelineState(InstructionRiscV id, String value){
		this.pipelineMap.put(id, value);
	}
	
	public HashMap<InstructionRiscV, String> getPipelineMap(){
		return pipelineMap;
	}
		
	public void setFetchedInstruction(InstructionRiscV code){
		this.fetchedInstruction = code;
	}
	
	public InstructionRiscV getFetchedInstruction(){
		return this.fetchedInstruction;
	}
	
	public void setStall(boolean stall){
		this.isStall = stall;
	}
	
	public boolean isStall(){
		return this.isStall;
	}
	
	public int getCycleNumber(){
		return this.cycleNumber;
	}
	
}
