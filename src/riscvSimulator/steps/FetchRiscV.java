package riscvSimulator.steps;

import java.util.ArrayList;

import riscvSimulator.InstructionRiscV;
import riscvSimulator.RegisterFileRiscV;
import riscvSimulator.RiscVFunc;
import riscvSimulator.RiscVMemory;
import riscvSimulator.values.RiscVValue32;

public class FetchRiscV {

	private RiscVMemory memory;
	private boolean isLast;
	private InstructionRiscV currentInstruction;
	private RegisterFileRiscV registers;
	
	public FetchRiscV(RegisterFileRiscV registers, RiscVMemory memory, boolean isLast){
		this.memory = memory;
		this.isLast = isLast;
		this.registers = registers;
	}
	
	public void doFetch(long cycleNumber){
		long binaryInstruction = this.memory.loadWord(this.registers.getPC().getUnsignedValue()).getUnsignedValue();

		this.currentInstruction = new InstructionRiscV(binaryInstruction, this.registers.getPC().getUnsignedValue(), cycleNumber);

			//If we are not in a trap, we increment PC
		//TODO
			registers.setPC(new RiscVValue32(registers.getPC().getUnsignedValue() + 4));

	}
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}

	public void setCurrentInstruction(InstructionRiscV instr) {
		this.currentInstruction = instr;
		
	}
	
}
