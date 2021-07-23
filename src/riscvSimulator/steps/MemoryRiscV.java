package riscvSimulator.steps;

import java.util.ArrayList;

import openSim.util.Statistics;
import riscvSimulator.InstructionRiscV;
import riscvSimulator.RegisterFileRiscV;
import riscvSimulator.RiscVMemory;
import riscvSimulator.values.RiscVValue32;

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
	
	public boolean doStep(){
		//Main job is to write the operation result in regfile
		boolean hit = true;
		if (isLast){
				switch (currentInstruction.getOp()) {
					case store :
						switch (currentInstruction.getFunc()) {
						case sb :
							memory.storeByte(currentInstruction.getAluResult().getUnsignedValue(), currentInstruction.getValueToStore(), true);
							break;
						case sh :
							memory.storeHalf(currentInstruction.getAluResult().getUnsignedValue(), currentInstruction.getValueToStore(), true);
							break;
						case sw :
							memory.storeWord(currentInstruction.getAluResult().getUnsignedValue(), currentInstruction.getValueToStore(),true);
							break;
						default:
							break;
						}
						System.out.println("Store stage done");
						Statistics.getInstance().countMemWrite();
						break;
					case load :
						switch (currentInstruction.getFunc()) {
						case lb :
							currentInstruction.setAluResult(new RiscVValue32(memory.loadByte(currentInstruction.getAluResult().getUnsignedValue(), true).getUnsignedValue()));
							hit = memory.getCache().getLastHit();
							break;
						case lh :
							currentInstruction.setAluResult(new RiscVValue32(memory.loadHalf(currentInstruction.getAluResult().getUnsignedValue(), true).getUnsignedValue()));
							hit = memory.getCache().getLastHit();
							break;
						case lw :
							currentInstruction.setAluResult(memory.loadWord(currentInstruction.getAluResult().getUnsignedValue(),true));
							hit = memory.getCache().getLastHit();
							break;
						case lbu :
							System.err.println("Operation not implemented in memory stage !");
							hit = memory.getCache().getLastHit();
							break;
						case lhu :
							System.err.println("Operation not implemented in memory stage !");
							hit = memory.getCache().getLastHit();
							break;
						default:
							break;
						}
						System.out.println("Load stage done");
						Statistics.getInstance().countMemRead();
						if (hit) Statistics.getInstance().countCacheHits();
						else Statistics.getInstance().countCacheMisses();
						break;
					default:
						break;
				}
				
		}
		return hit;
	}
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(InstructionRiscV instr){
		this.currentInstruction = instr;
	}
}
