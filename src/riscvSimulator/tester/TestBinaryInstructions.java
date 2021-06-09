package riscvSimulator.tester;

import java.util.Random;

import riscvSimulator.InstructionRiscV;
import riscvSimulator.RiscVFunc;
import riscvSimulator.RiscVOpCode;
import riscvSimulator.steps.DecodeRiscV;
import riscvSimulator.steps.ExecuteRiscV;
import riscvSimulator.steps.FetchRiscV;
import riscvSimulator.steps.MemoryRiscV;
import riscvSimulator.steps.WriteBackRiscV;
import riscvSimulator.values.RiscVValue32;

public enum TestBinaryInstructions {
	
	//add  0000000__________000_____0110011
	//mod  _______BBBBBAAAAA___DDDDD_______
	add1(0b00000000001000010000000010110011),
	add2(0b00000000001100010000000010110011),
	add3(0b00000001111111111000000010110011),
	add4(0b00000001101000101000001010110011),
	//sub  0000000__________000_____0110011
	beq(1),
	bne(1),
	blt(1),
	bltu(1),
	
	addi(0),
	slli(0x00131313),
	srli(0x00135313),
	srai(0x40135313),
	slti(10),
	sltiu(1),
	
	lui(0x00400337),//lui     t1,0x400
	auipc(0x00400317)//auipc   t1,0x400
	;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	long instruction;
	
	private TestBinaryInstructions(long binaryInstruction) {
		instruction = binaryInstruction;
	}
	
	public long getBinary() {
		return instruction;
	}
	
	public long getBits(int size,int start){
		//if (LSB < 0 ) throw new Exception();
		//if (MSB > 31 ) throw new Exception();
		long tmp = instruction >> start;
		//System.out.println(Long.toBinaryString(instruction) + " >> to  " + Long.toBinaryString(tmp));
		//System.out.println(Long.toBinaryString(tmp) + " >> to  " + Long.toBinaryString(tmp & (int) Math.pow(2,size)-1));
		//System.out.println("Using " + Integer.toBinaryString((int) Math.pow(2,size)-1));
		return (tmp & (((long) Math.pow(2,size))-1)) & 0xffffffff;
	}
	
	
}
