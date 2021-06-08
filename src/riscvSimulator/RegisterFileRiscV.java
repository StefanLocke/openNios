package riscvSimulator;

import java.util.ArrayList;
import java.util.HashMap;

import riscvSimulator.values.RiscVValue32;

public class RegisterFileRiscV {

	private RiscVValue32 pc;
	private HashMap<Integer, RiscVValue32> registers;
	
	public RegisterFileRiscV(){
		this.registers = new HashMap<Integer, RiscVValue32>();
		this.pc = new RiscVValue32(0);
	}
	
	public RiscVValue32 get(int index){
		if (!registers.containsKey(index))
			return new RiscVValue32(0);
		
		return registers.get(index).copy();
	}
	
	public RiscVValue32 getPC(){
		return this.pc.copy();
	}
	
	public void setPC(RiscVValue32 value){
		this.pc = value;
	}
	
	public void set(int place, RiscVValue32 value){
		this.registers.put(place, value);
	}
}
