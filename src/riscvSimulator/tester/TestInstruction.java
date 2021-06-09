package riscvSimulator.tester;

public class TestInstruction {
	private long instruction;
	public TestInstruction(int binary) {
		instruction = binary;
	}
	
	
	
	
	
	
	public void setBin(int binary) {
		instruction = binary;
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
