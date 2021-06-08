package riscvSimulator.tester;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import riscvSimulator.InstructionRiscV;
import riscvSimulator.RiscVFunc;
import riscvSimulator.RiscVOpCode;
import riscvSimulator.values.RiscVValue12;
import riscvSimulator.values.RiscVValue32;

class Tests {
	InstructionRiscV i;
	TestBinaryInstructions currentBinary;
	
	
	
	
	
	
	@BeforeEach
	void setup() {
		
	}
	@Test
	void test() {
		long b = 0b100000000100;
		System.out.println("b :" + b);
		
		RiscVValue12 value = new RiscVValue12(b);
		System.out.println("b20s :" + value.getSignedValue());
		System.out.println("b20u :" + value.getUnsignedValue());
		
		RiscVValue32 value32 = value.toValue32();
		System.out.println("b32s :" + value32.getSignedValue());
		System.out.println("b32u :" + value32.getUnsignedValue());
	}
	
	@Test
	void testValue32() {
		/*long test = -32;
		RiscVValue32 value = new RiscVValue32(test);
		System.out.println("Template : " + "LLLLLLLL" + "DDDDDDDD");
		System.out.println("Signed   : " + String.format("%016x", value.getSignedValue()) + "::" + value.getSignedValue());
		System.out.println("Unsigned : " + String.format("%016x", value.getUnsignedValue()) + "::" + value.getUnsignedValue());*/
		
	}
	
	@Test
	void testValue32bis() {
		int test = -32;
		RiscVValue32 value = new RiscVValue32(test);
		System.out.println("Signed   : " + "LLLLLLLL" + "DDDDDDDD");
		System.out.println("Signed   : " + String.format("%016x", value.getSignedValue()) + "::" + value.getSignedValue());
		System.out.println("Unsigned : " + String.format("%016x", value.getUnsignedValue()) + "::" + value.getUnsignedValue());
		System.out.println("*-----*");
		RiscVValue32 value2 = new RiscVValue32(value.getSignedValue());
		System.out.println("Signed   : " + "LLLLLLLL" + "DDDDDDDD");
		System.out.println("Signed   : " + String.format("%016x", value2.getSignedValue()) + "::" + value2.getSignedValue());
		System.out.println("Unsigned : " + String.format("%016x", value2.getUnsignedValue()) + "::" + value2.getUnsignedValue());
		System.out.println("*-----*");
		RiscVValue32 value3 = new RiscVValue32(value.getUnsignedValue());
		System.out.println("Signed   : " + "LLLLLLLL" + "DDDDDDDD");
		System.out.println("Signed   : " + String.format("%016x", value3.getSignedValue()) + "::" + value3.getSignedValue());
		System.out.println("Unsigned : " + String.format("%016x", value3.getUnsignedValue()) + "::" + value3.getUnsignedValue());
		
		
		
	}
	
	@Test
	void testGetBits() {
		currentBinary = TestBinaryInstructions.add1;
		assertEquals(currentBinary.getBinary(),currentBinary.getBits(32, 0));
		assertEquals(0b0110011,currentBinary.getBits(7, 0));
	}
	
	@Test
	void testAdd() {
		currentBinary = TestBinaryInstructions.add1;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),0b00001);
		assertEquals(i.getRs1(),0b00010);
		assertEquals(i.getRs2(),0b00010);
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
		
		currentBinary = TestBinaryInstructions.add2;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),0b00001);
		assertEquals(i.getRs1(),0b00010);
		assertEquals(i.getRs2(),0b00011);
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
		
		currentBinary = TestBinaryInstructions.add3;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),0b00001);
		assertEquals(i.getRs1(),0b11111);
		assertEquals(i.getRs2(),0b11111);
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
		
		currentBinary = TestBinaryInstructions.add4;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),0b00101);
		assertEquals(i.getRs1(),0b00101);
		assertEquals(i.getRs2(),0b11010);
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
	}

	@Test
	void testAddbis() {
		currentBinary = TestBinaryInstructions.add1;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),currentBinary.getBits(5,7));
		assertEquals(i.getRs1(),currentBinary.getBits(5,15));
		assertEquals(i.getRs2(),currentBinary.getBits(5,20));
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
		
		currentBinary = TestBinaryInstructions.add2;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),currentBinary.getBits(5,7));
		assertEquals(i.getRs1(),currentBinary.getBits(5,15));
		assertEquals(i.getRs2(),currentBinary.getBits(5,20));
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
		
		currentBinary = TestBinaryInstructions.add3;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),currentBinary.getBits(5,7));
		assertEquals(i.getRs1(),currentBinary.getBits(5,15));
		assertEquals(i.getRs2(),currentBinary.getBits(5,20));
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
		
		currentBinary = TestBinaryInstructions.add4;
		i = new InstructionRiscV(currentBinary.getBinary(), 20, 1);
		
		assertEquals(i.getImm12(), 0);
		assertEquals(i.getImm20(), 0);
		assertEquals(i.getRd(),currentBinary.getBits(5,7));
		assertEquals(i.getRs1(),currentBinary.getBits(5,15));
		assertEquals(i.getRs2(),currentBinary.getBits(5,20));
		assertEquals(i.getOp(),RiscVOpCode.op);
		assertEquals(i.getFunc(),RiscVFunc.add);
	}
	

}
