package riscvSimulator.values;

public class RiscVValue32 extends RiscVValue {
	
	private long value;
	
	public RiscVValue32(int value){
		this.value = value & 0x00000000ffffffffl;
		
	}
	
	public RiscVValue32(long value){
		this.value = value & 0x00000000ffffffffl;
	}

	public long getSignedValue(){
		if (value>>31 != 0)
			return (long) (value - Math.pow(2, 32));
		else
			return value;
	}
	
	public long getUnsignedValue(){
		return value & 0x00000000ffffffffl;
	}
	
	public RiscVValue32 copy(){
		return new RiscVValue32(getUnsignedValue());
	}
	
	@Override
	public String toString() {	
		return value +"";
	}
}
