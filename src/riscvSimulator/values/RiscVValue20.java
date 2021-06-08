package riscvSimulator.values;

public class RiscVValue20 extends RiscVValue{

	
	private long value;
	
	
	public RiscVValue20(long value) {
		this.value = value & 0x00000000000fffffl;
	}
	
	public RiscVValue20(int value) {
		this.value = value & 0x00000000000fffffl;;
	}
	
	@Override
	public long getSignedValue() {
		if (value>>19 != 0)
			return (long) (value - Math.pow(2, 20));
		else
			return value;
	}

	@Override
	public long getUnsignedValue() {
		return value & 0x00000000000fffffl;
	}

	@Override
	public RiscVValue copy() {
		return new RiscVValue20(getUnsignedValue());
	}
	
	public RiscVValue32 toValue32() {
		
		RiscVValue32 r;
		 if ((value >> 19) != 0) {
             long signExt = 0xfff;
             signExt = signExt << 20;
             //System.out.println("going signed : " + Long.toHexString(value | signExt));
           r = new RiscVValue32(value | signExt);
     }
         else
             r = new RiscVValue32(value);
		
		//System.out.println("Converting " + value + " to " + getSignedValue());
		//System.out.println("Converted signed :" + Long.toHexString(r.getSignedValue()) + " or unSigned :" + Long.toHexString(r.getUnsignedValue()));
		return r;
	}

}
