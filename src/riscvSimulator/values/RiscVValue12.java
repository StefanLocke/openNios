package riscvSimulator.values;

public class RiscVValue12 extends RiscVValue{

		
		private long value;
		
		public RiscVValue12(long value) {
			this.value = value & 0x0000000000000fffl;
		}
		
		public RiscVValue12(int value) {
			this.value = value & 0x0000000000000fffl;
		}
		
		@Override
		public long getSignedValue() {
			if (value>>11 != 0)
				return (long) (value - Math.pow(2, 12));
			else
				return value;
		}

		@Override
		public long getUnsignedValue() {
			return value & 0x0000000000000fffl;
		}

		@Override
		public RiscVValue copy() {
			return new RiscVValue12(getUnsignedValue());
		}
		
		public RiscVValue32 toValue32() {
			/*
			RiscVValue32 r;
			 if ((value >> 11) != 0) {
	             long signExt = 0xfffff;
	             signExt = signExt << 12;
	           //System.out.println("12 going signed : " + Long.toHexString(value | signExt));
	           r = new RiscVValue32(((value | signExt)));
	           
	     }
	         else
	             r = new RiscVValue32(value);
			 
			System.out.println("12 Converting " + Long.toHexString(value) + " to " + Long.toHexString(getSignedValue()));
			System.out.println("12 Converting " + value + " to " + getSignedValue());
			System.out.println("12 Converted signed :" + Long.toHexString(r.getSignedValue()) + " or unSigned :" + Long.toHexString(r.getUnsignedValue()));
			System.out.println("12 Converted signed :" + r.getSignedValue() + " or unSigned :" + r.getUnsignedValue());
			long newval = value
			if (newval >>11 != 0) {
				newval = newval | 0xfffff0000l;
				System.out.println("conversion :" + String.format("%016x", newval));
				return new RiscVValue32(newval);
			}
			return new RiscVValue32(newval);
			*/
			
			RiscVValue32 r;
			 if ((value >> 11) != 0) {
	             long signExt = 0xfffff;
	             signExt = signExt << 12;
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
