package riscvSimulator.tester;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import riscvSimulator.InstructionRiscV;
import riscvSimulator.RegisterFileRiscV;
import riscvSimulator.RiscVMemory;
import riscvSimulator.steps.DecodeRiscV;
import riscvSimulator.steps.ExecuteRiscV;
import riscvSimulator.steps.FetchRiscV;
import riscvSimulator.steps.MemoryRiscV;
import riscvSimulator.steps.WriteBackRiscV;
import riscvSimulator.values.RiscVValue32;


public class StepTester {
	
	RegisterFileRiscV registers;
	RiscVMemory memory;
	
	@BeforeEach
	void setup() {
		registers = new RegisterFileRiscV();
		memory = new RiscVMemory();
	
		
	}
	
	@Test
	void test1() {//slli    t1,t1,0x1
		registers.set(6, new RiscVValue32(-32));
		doSteps(TestBinaryInstructions.slli);
		System.out.println(Long.toBinaryString(registers.get(6).getUnsignedValue()));
		assertEquals(4294967232l, registers.get(6).getUnsignedValue());
		assertEquals(-64, registers.get(6).getSignedValue());
	}
	
	@Test
	void test2() {//srli    t1,t1,0x1
		registers.set(6, new RiscVValue32(-32));
		doSteps(TestBinaryInstructions.srli);
		assertEquals(2147483632, registers.get(6).getUnsignedValue());
		assertEquals(2147483632, registers.get(6).getSignedValue());
	}
	
	@Test
	void test3() { //srai   t1,t1,0x1
		registers.set(6, new RiscVValue32(-32));
		doSteps(TestBinaryInstructions.srai);
		assertEquals(-16, registers.get(6).getSignedValue());
		assertEquals(4294967280l, registers.get(6).getUnsignedValue());
	}
	
	@Test
	void test4() {//slli    t1,t1,0x1
		registers.set(6, new RiscVValue32(32));
		doSteps(TestBinaryInstructions.slli);
		System.out.println(Long.toBinaryString(registers.get(6).getUnsignedValue()));
		assertEquals(64, registers.get(6).getUnsignedValue());
		assertEquals(64, registers.get(6).getSignedValue());
	}
	
	@Test
	void test5() {//srli    t1,t1,0x1
		registers.set(6, new RiscVValue32(32));
		doSteps(TestBinaryInstructions.srli);
		assertEquals(16, registers.get(6).getUnsignedValue());
		assertEquals(16, registers.get(6).getSignedValue());
	}
	
	@Test
	void test6() { //srai   t1,t1,0x1
		registers.set(6, new RiscVValue32(32));
		doSteps(TestBinaryInstructions.srai);
		assertEquals(16, registers.get(6).getSignedValue());
		assertEquals(16, registers.get(6).getUnsignedValue());
	}
	@Test
	void test7() {//lui   t1,0x400
		doSteps(TestBinaryInstructions.lui);
		assertEquals(4194304, registers.get(6).getUnsignedValue());
		
	}
	@Test
	void test8() {//auipc   t1,0x400
		doSteps(TestBinaryInstructions.auipc);
		
		assertEquals(4194304, registers.get(6).getUnsignedValue());
	}

	private void doSteps(TestBinaryInstructions instruction) {
		System.out.println("\n\n\n");
		System.out.println("Test -Instructuon :" + instruction.name());
		memory.setWord(0, new RiscVValue32(instruction.getBinary()));
		FetchRiscV fetch = new FetchRiscV(registers, memory, true);
		fetch.doFetch(0);
		InstructionRiscV currentI = fetch.getCurrentInstruction();
		System.out.println("---FETCH-------");
		System.out.println("Test -Op : " + fetch.getCurrentInstruction().getOp());
		System.out.println("Test -Func : " + fetch.getCurrentInstruction().getFunc());
		System.out.println("Test -Rs1 : " + fetch.getCurrentInstruction().getRs1());
		System.out.println("Test -Rs2 : " + fetch.getCurrentInstruction().getRs2());
		System.out.println("Test -Rd : " + fetch.getCurrentInstruction().getRd());
		System.out.println("Test -Imm12 : " + fetch.getCurrentInstruction().getImm12());
		System.out.println("Test -Imm20 : " + fetch.getCurrentInstruction().getImm20());
		System.out.println("---------------");
		System.out.println("---DECODE------");
		DecodeRiscV decode = new DecodeRiscV(registers);
		decode.setCurrentInstruction(currentI);
		decode.doStep();
		System.out.println("Test -Value A: " +currentI.getValueA());
		System.out.println("Test -Value B : " +currentI.getValueB());
		System.out.println("---------------");
		System.out.println("---EXECUTE-----");
		ExecuteRiscV execute = new ExecuteRiscV(registers, true);
		execute.setCurrentInstruction(currentI);
		execute.doStep();
		System.out.println("Test -ALU : " + currentI.getAluResult());
		
		System.out.println("---------------");
		System.out.println("---MEMORY------");
		MemoryRiscV memoryStep = new MemoryRiscV(registers, memory, true);
		memoryStep.setCurrentInstruction(currentI);
		memoryStep.doStep();
		
		System.out.println("---------------");
		System.out.println("---WRITE BACK--");
		WriteBackRiscV writeBack = new WriteBackRiscV(registers);
		writeBack.setCurrentInstruction(currentI);
		writeBack.doStep();
		System.out.println("Test -Register 1: " + registers.get((int) currentI.getRs1()));
		System.out.println("Test -Register 2: " + registers.get((int) currentI.getRs2()));
		System.out.println("Test -Register Dest: " + registers.get((int) currentI.getRd()));
	}
}
