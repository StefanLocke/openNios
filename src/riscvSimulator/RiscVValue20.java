package riscvSimulator;

public class RiscVValue20 extends RiscVValue{

	
	private long value;
	
	
	public RiscVValue20(long value) {
		this.value = value;
	}
	
	public RiscVValue20(int value) {
		this.value = value;
	}
	
	@Override
	public long getSignedValue() {
		if (value>>19 != 0)
			return (long) (value - Math.pow(2, 20));
		else
			return value & 0xfffff;
	}

	@Override
	public long getUnsignedValue() {
		return value & 0xfffff;
	}

	@Override
	public RiscVValue copy() {
		return new RiscVValue20(getUnsignedValue());
	}
	
	public RiscVValue32 toValue32() {
		
		RiscVValue32 r;
		if ((value >> 19) != 0)
			  r = new RiscVValue32(value | 0xfff00000, false);
			else
				r = new RiscVValue32(value, false);
		
		System.out.println("Converting " + value + " to " + getSignedValue());
		System.out.println("Converted signed :" + r.getSignedValue() + " or unSigned :" + Long.toHexString(r.getUnsignedValue()));
		return r;
	}

}
