package riscvSimulator;

import riscvSimulator.values.RiscVValue32;

public class RiscVALU {

	public static RiscVValue32 add(RiscVValue32 a, RiscVValue32 b) {
		System.out.println("add : " + a + " + " + b+ " = ");
		return new RiscVValue32(a.getSignedValue() + b.getSignedValue());
	}
	
	public static RiscVValue32 sub(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getSignedValue() - b.getSignedValue());
	}
	
	public static RiscVValue32 mul(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getSignedValue() * b.getSignedValue());
	}
	
	public static RiscVValue32 mulUpper(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32((a.getSignedValue() * b.getSignedValue()) >> 32);
	}
	public static RiscVValue32 mulUpperSignedOnUnsigned(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32((a.getSignedValue() * b.getUnsignedValue()) >> 32);
	}
	public static RiscVValue32 mulUpperUnsigned(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32((a.getUnsignedValue() * b.getUnsignedValue()) >> 32);
	}
	
	public static RiscVValue32 div(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getSignedValue() / b.getSignedValue());
	}
	public static RiscVValue32 divUnsigned(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getUnsignedValue() / b.getUnsignedValue());
	}
	
	public static RiscVValue32 rem(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getSignedValue() % b.getSignedValue());
	}
	public static RiscVValue32 remUnsigned(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getUnsignedValue() % b.getUnsignedValue());
	}
	
	public static RiscVValue32 shiftLeftLogical(RiscVValue32 a, RiscVValue32 b) {
		long binToShift = a.getUnsignedValue();
		System.out.println(String.format("%1$016x",binToShift));
		binToShift = binToShift << b.getUnsignedValue();
		//binToShift = binToShift & 0xfffffffel;
		System.out.println(String.format("%1$016x",binToShift));

		return new RiscVValue32(binToShift);
	}
	
	public static RiscVValue32 shiftRightLogical(RiscVValue32 a, RiscVValue32 b) {
		long binToShift =  a.getUnsignedValue();
		System.out.println(String.format("%1$016x",binToShift));
		binToShift = binToShift >> b.getUnsignedValue();
		//binToShift = binToShift & 0x7fffffff;
		System.out.println(String.format("%1$016x",binToShift));
		return new RiscVValue32(binToShift);
	}
	
	public static RiscVValue32 shiftRightArithmetic(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getSignedValue() >> b.getUnsignedValue());
	}
	
	public static RiscVValue32 or(RiscVValue32 a, RiscVValue32 b) {
		
		return new RiscVValue32(a.getUnsignedValue() | b.getUnsignedValue());
	}
	
	public static RiscVValue32 xor(RiscVValue32 a, RiscVValue32 b) {
		return new RiscVValue32(a.getUnsignedValue() ^ b.getUnsignedValue());
	}
	
	public static RiscVValue32 and(RiscVValue32 a, RiscVValue32 b) {
		
		return new RiscVValue32(a.getUnsignedValue() & b.getUnsignedValue());
	}
	
	public static RiscVValue32 loadUpperImmediate(RiscVValue32 a) {
		return new RiscVValue32(0 | (a.getUnsignedValue() << 12));
	}
	
	public static RiscVValue32 addUpperImmediateToPc(RiscVValue32 a,RiscVValue32 pc) {
		
		return new RiscVValue32((0 | (a.getUnsignedValue() << 12)) + pc.getSignedValue());
	}
	
	public static RiscVValue32 setLessThan(RiscVValue32 a,RiscVValue32 b) {
		if (a.getSignedValue() < b.getSignedValue())
			return new RiscVValue32(1);
		return new RiscVValue32(0);
	}
	public static RiscVValue32 setLessThanUnsigned(RiscVValue32 a,RiscVValue32 b) {
		if (a.getUnsignedValue() < b.getUnsignedValue())
			return new RiscVValue32(1);
		return new RiscVValue32(0);
	}
	
	public static RiscVValue32 setLessThanImmediate(RiscVValue32 a,RiscVValue32 b) {
		if (a.getSignedValue() < b.getSignedValue())
			return new RiscVValue32(1);
		return new RiscVValue32(0);
	}
	
	public static RiscVValue32 setLessThanImmediateUnsigned(RiscVValue32 a,RiscVValue32 b) {
		if (a.getUnsignedValue() < b.getUnsignedValue())
			return new RiscVValue32(1);
		return new RiscVValue32(0);
	}
}
