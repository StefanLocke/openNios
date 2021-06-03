package riscvSimulator;

public class RiscVValue32 extends RiscVValue {
	
	private long value;
	
	public RiscVValue32(int value, boolean isSigned){
		this.value = value;
	}
	
	public RiscVValue32(long value, boolean isSigned){
		byte b = 0b00000000;
		this.value = value;
	}

	public long getSignedValue(){
		if (value>>31 != 0)
			return (long) (value - Math.pow(2, 32));
		else
			return value & 0xffffffff;
	}
	
	public long getUnsignedValue(){
		return value & 0xffffffff;
	}
	
	public RiscVValue32 copy(){
		return new RiscVValue32(getUnsignedValue(), false);
	}
	
	@Override
	public String toString() {	
		return value +"";
	}
}
