package riscvSimulator.values;

public class RiscVValue16 extends RiscVValue{
	
	private long value;
	
	public RiscVValue16(long value){
		this.value = value;
	}

	@Override
	public long getSignedValue() {
		if (value>>15 != 0)
			return (long) (value - Math.pow(2, 16));
		else
			return value;
	}

	@Override
	public long getUnsignedValue() {
		return value;
	}
	
	public RiscVValue16 copy(){
		return new RiscVValue16(getSignedValue());
	}

}
