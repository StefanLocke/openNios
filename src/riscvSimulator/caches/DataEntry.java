package riscvSimulator.caches;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class DataEntry {
	RiscVValue8 byte1;
	RiscVValue8 byte2;
	RiscVValue8 byte3;
	RiscVValue8 byte4;
	
	public DataEntry() {
		
	}
	
	public void setWord(RiscVValue value) {
		byte1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		byte2 = new RiscVValue8((value.getUnsignedValue() >> 8) & 0xff);
		byte3 = new RiscVValue8((value.getUnsignedValue() >> 16) & 0xff);
		byte4 = new RiscVValue8((value.getUnsignedValue() >> 24) & 0xff);
	}
	public void setHalf(RiscVValue value,long selector) {
		if (selector == 0) {
		byte1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		byte2 = new RiscVValue8((value.getUnsignedValue()>> 8) & 0xff);
		return;
		}
		byte3 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		byte4 = new RiscVValue8((value.getUnsignedValue()>> 8) & 0xff);
	}
	public void setByte(RiscVValue value,long selector) {
		if (selector == 0) {
			byte1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
			return;
		}
		if (selector == 1) {
			byte2 = new RiscVValue8(value.getUnsignedValue() & 0xff);
			return;
		}
		if (selector == 2) {
			byte3 = new RiscVValue8(value.getUnsignedValue() & 0xff);
			return;
		}
		byte4 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		return;
	}
	
	
	
	
	
	
	public RiscVValue32 getWord() {
		return new RiscVValue32(byte4.getUnsignedValue() << 24 | (byte3.getUnsignedValue() << 16 | (byte2.getUnsignedValue() << 8 | (byte1.getUnsignedValue()))));
	}
	
	public RiscVValue16 getHalf(long selector) {
		if (selector == 0) {
			return new RiscVValue16(byte4.getUnsignedValue() << 8 | (byte3.getUnsignedValue()));
		}
		return new RiscVValue16(byte2.getUnsignedValue() << 8 | (byte1.getUnsignedValue()));
	}
	public RiscVValue8 getByte(long selector) {
		if (selector == 0) {
			return byte1;
		}
		if (selector == 1) {
			return byte2;
		}
		if (selector == 2) {
			return byte3;
		}
		return byte4;
	}
	
	@Override
	public String toString() {
		
		return "[" + byte4.getUnsignedValue() + " " + byte3.getUnsignedValue() + " " +byte2.getUnsignedValue() + " " +byte1.getUnsignedValue() + "]";
	}
}
