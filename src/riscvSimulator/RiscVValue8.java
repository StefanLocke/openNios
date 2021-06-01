package riscvSimulator;

public class RiscVValue8 extends RiscVValue{

	private long value;
	
	public RiscVValue8(long value){
		this.value = value;
	}
	
	public RiscVValue8(int value){
		this.value = value;
	}
	
	public long getSignedValue(){
		if (value>>7 != 0)
			return (int) (value - Math.pow(2, 8));
		else
			return (int) value;
	}
	
	public long getUnsignedValue(){
		return this.value;
	}

	@Override
	public RiscVValue8 copy() {
		return new RiscVValue8(this.value);
	}
}
