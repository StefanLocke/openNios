package riscvSimulator;

import java.util.ArrayList;
import java.util.HashMap;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class RiscVMemory {

	private HashMap<Integer, RiscVValue8> memory;
	
	public RiscVMemory(){
		this.memory = new HashMap<Integer, RiscVValue8>();
	}
	
	private RiscVValue8 loadByteAsByte(int addr){
		if (this.memory.containsKey(addr))
			return this.memory.get(addr).copy();
		else
			return new RiscVValue8(0);
	}
	
	public RiscVValue8 loadByteUnsigned(int addr){
		return this.loadByteAsByte(addr);

	}
	
	public RiscVValue8 loadByteSigned(int addr){
		//TODO
		return null;
	}
	
	public RiscVValue32 loadWord(int addr){
		RiscVValue8 v1 = this.loadByteAsByte(addr);
		RiscVValue8 v2 = this.loadByteAsByte(addr+1);
		RiscVValue8 v3 = this.loadByteAsByte(addr+2);
		RiscVValue8 v4 = this.loadByteAsByte(addr+3);
		
		return new RiscVValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24));
	}
	
	public RiscVValue32 loadWord(long addr){
		RiscVValue8 v1 = this.loadByteAsByte((int) addr);
		RiscVValue8 v2 = this.loadByteAsByte((int) (addr+1));
		RiscVValue8 v3 = this.loadByteAsByte((int) (addr+2));
		RiscVValue8 v4 = this.loadByteAsByte((int) (addr+3));
		
		return new RiscVValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24));
	}
	
	public RiscVValue32 loadHalf(long addr){
		RiscVValue8 v1 = this.loadByteAsByte((int) addr);
		RiscVValue8 v2 = this.loadByteAsByte((int) (addr+1));

		
		return new RiscVValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8));
	}
	
	public void set(int addr, RiscVValue8 value){
		this.memory.put(addr, value);
	}
	public void setWord(int addr, RiscVValue32 word){
		this.set(addr, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.set(addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
		this.set(addr+2, new RiscVValue8((word.getUnsignedValue()>>16) & 0xff));
		this.set(addr+3, new RiscVValue8((word.getUnsignedValue()>>24) & 0xff));

	}
	
	public void setWord(long addr, RiscVValue word){
		this.set((int) addr, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.set((int) addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
		this.set((int) addr+2, new RiscVValue8((word.getUnsignedValue()>>16) & 0xff));
		this.set((int) addr+3, new RiscVValue8((word.getUnsignedValue()>>24) & 0xff));
		
		System.out.println("Writing at 0x" + Long.toHexString(addr) + " : 0x" + Long.toHexString(word.getUnsignedValue()));
	}
	
	
	public void setHalf(long addr, RiscVValue word){
		this.set((int) addr, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.set((int) addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
	}
}
