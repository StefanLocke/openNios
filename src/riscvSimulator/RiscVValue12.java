package riscvSimulator;

public class RiscVValue12 extends RiscVValue{

		
		private long value;
		
		public RiscVValue12(long value) {
			this.value = value;
		}
		
		public RiscVValue12(int value) {
			this.value = value;
		}
		
		@Override
		public long getSignedValue() {
			if (value>>11 != 0)
				return (long) (value - Math.pow(2, 12));
			else
				return value & 0xfff;
		}

		@Override
		public long getUnsignedValue() {
			return value & 0xfff;
		}

		@Override
		public RiscVValue copy() {
			return new RiscVValue12(getUnsignedValue());
		}
}
