package riscvSimulator;

import niosSimulator.NiosValue16;
import niosSimulator.NiosValue32;

public class DecodeRiscV {

	private RegisterFileRiscV registers;
	private boolean isLast;
	
	private InstructionRiscV currentInstruction;
	private boolean isStalled;
	private boolean jumped;

	public DecodeRiscV(RegisterFileRiscV registers){
		this.registers = registers;
		this.isLast = isLast;
	}
	
	public void doStep(){
		//Main job of this part is to access registers and extend/select immediate values
		switch(currentInstruction.getType()){
		case BTYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
			currentInstruction.setValueB(registers.get(currentInstruction.getRb()));
			currentInstruction.setValueToStore(new RiscVValue32(currentInstruction.getImm12(), true));
			break;
		case ITYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
			currentInstruction.setValueB(new RiscVValue32(currentInstruction.getImm12(), true));
			break;
		case JTYPE:
			currentInstruction.setValueA(new RiscVValue32(currentInstruction.getImm20(), true));
			break;
		case RTYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
			currentInstruction.setValueB(registers.get(currentInstruction.getRb()));
			break;
		case STYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
			currentInstruction.setValueB(new RiscVValue32((new RiscVValue16(currentInstruction.getImm12())).getSignedValue(), true));
			currentInstruction.setValueToStore(registers.get(currentInstruction.getRb()));
			break;
		case UTYPE:
			currentInstruction.setValueA(new RiscVValue32(currentInstruction.getImm20(), true));
			break;
		default:
			break;
		}
	}
	
	
		//For unconditional jumps, we perform the jump now
		//TODO
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(InstructionRiscV instr){
		this.currentInstruction = instr;
	}
	
	
	public void setStalled(boolean stall){
		this.isStalled = stall;
	}

	public boolean isStalled(){
		return this.isStalled;
	}
	
	public boolean hasJumped(){
		return this.jumped;
	}
}
