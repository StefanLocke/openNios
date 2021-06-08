package riscvSimulator.steps;

import java.util.ArrayList;

import riscvSimulator.InstructionRiscV;
import riscvSimulator.RegisterFileRiscV;
import riscvSimulator.RiscVMemory;

public class MemoryRiscV {
	private RegisterFileRiscV registers;
	private RiscVMemory memory;
	private boolean isLast;
	
	private InstructionRiscV currentInstruction;
	
	public MemoryRiscV(RegisterFileRiscV registers, RiscVMemory memory, boolean isLast){
		this.registers = registers;
		this.isLast = isLast;
		this.memory = memory;
	}
	
	public void doStep(){
		//Main job is to write the operation result in regfile
		if (!isLast){
				switch (currentInstruction.getOp()) {
					case store :
						switch (currentInstruction.getFunc()) {
						case sb :
							System.err.println("Operation not implemented in memory stage !");
							break;
						case sh :
							System.err.println("Operation not implemented in memory stage !");
							break;
						case sw :
							System.out.println("writing to " + Long.toHexString(currentInstruction.getAluResult().getUnsignedValue()));
							memory.setWord(currentInstruction.getAluResult().getUnsignedValue(), currentInstruction.getValueToStore());
							break;
						default:
							break;
						}
						break;
					case load :
						switch (currentInstruction.getFunc()) {
						case lb :
							System.err.println("Operation not implemented in memory stage !");
							break;
						case lh :
							currentInstruction.setAluResult(memory.loadHalf(currentInstruction.getAluResult().getUnsignedValue()));
							break;
						case lw :
							currentInstruction.setAluResult(memory.loadWord(currentInstruction.getAluResult().getUnsignedValue()));
							break;
						case lbu :
							System.err.println("Operation not implemented in memory stage !");
							break;
						case lhu :
							System.err.println("Operation not implemented in memory stage !");
							break;
						default:
							break;
						}
						break;
					default:
						break;
				}
		}
	}
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(InstructionRiscV instr){
		this.currentInstruction = instr;
	}
}
