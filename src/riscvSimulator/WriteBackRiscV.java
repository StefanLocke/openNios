package riscvSimulator;

public class WriteBackRiscV {
	private RegisterFileRiscV registers;
	
	private InstructionRiscV currentInstruction;
	
	public WriteBackRiscV(RegisterFileRiscV registers){
		this.registers = registers;
	}
	
	public void doStep(){
		//Main job is to write the operation result in regfile
			int destRegister = currentInstruction.getWrittenRegister();
			if (destRegister != -1)
				registers.set(destRegister, currentInstruction.getAluResult());
	}
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(InstructionRiscV instr){
		this.currentInstruction = instr;
	}
}
