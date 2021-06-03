package riscvSimulator;


public class DecodeRiscV {

	private RegisterFileRiscV registers;
	private boolean isLast;
	
	private InstructionRiscV currentInstruction;
	private boolean isStalled;
	private boolean jumped;

	public DecodeRiscV(RegisterFileRiscV registers){
		this.registers = registers;
		this.isLast = isLast;
	}
	
	public void doStep(){
		//Main job of this part is to access registers and extend/select immediate values
		switch(currentInstruction.getType()){
		case BTYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRd()));
			currentInstruction.setValueB(registers.get(currentInstruction.getRs1()));
			currentInstruction.setValueToStore(new RiscVValue32(((currentInstruction.getImm12() >> 11) & 0b1) == 0b1 ? 0xfffff << 12 | currentInstruction.getImm12() & 0xfff : 0x00000 << 12 | currentInstruction.getImm12() & 0xfff, true));
			break;
		case ITYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRd()));
			currentInstruction.setValueB(new RiscVValue32(currentInstruction.getImm12(), true));
			break;
		case JTYPE: // we add the correct bit to fill the 32 bit java integer so we can have a correctly signed value
			currentInstruction.setValueA(new RiscVValue20(currentInstruction.getImm20()).toValue32());
			break;
		case RTYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRd()));
			currentInstruction.setValueB(registers.get(currentInstruction.getRs1()));
			break;
		case STYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRd()));
			currentInstruction.setValueB(new RiscVValue32((new RiscVValue16(currentInstruction.getImm12())).getSignedValue(), true));
			currentInstruction.setValueToStore(registers.get(currentInstruction.getRs1()));
			break;
		case UTYPE:
			currentInstruction.setValueA(new RiscVValue32(currentInstruction.getImm20(), true));
			break;
		default:
			break;
		}
		
		
		//For unconditional jumps, we perform the jump now
		jumped = false;
		switch (currentInstruction.getOp()) {
		case jalr :
		case jal : { //TODO clean this
			//find out if signed positiv or negative by cheking the 20th bit (imm20)
			//we then add on the LSB 
			//we can then add this to the Pc to determine the target address
			//WE need to apply the offset to the address
			System.out.println("Jal :" + new RiscVValue20(currentInstruction.getImm20()).getSignedValue());
			System.out.println("Jal :" + currentInstruction.getValueA().getSignedValue());
			
			long offset = currentInstruction.getValueA().getSignedValue();
			/*System.out.println("imm " + offset);
			System.out.println("value " + currentInstruction.getValueA().getSignedValue());
			System.out.println(offset & 0xfffff);
			System.out.println(Integer.toHexString(offset & 0xfffff));*/
			offset = ((offset) << 1 | 0b0);
			/*System.out.println(offset);
			System.out.println(offset & 0xfffff);
			System.out.println(Integer.toHexString(offset & 0xfffff));
			System.out.println(currentInstruction.getPC() + " + " + offset);*/
			
			
			currentInstruction.setAluResult(registers.getPC());
			registers.setPC(new RiscVValue32(currentInstruction.getPC() + offset, false));
			jumped = true;
			break;
		}
		default:
			break;
		}
		
		
	}
	
	
		
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(InstructionRiscV instr){
		this.currentInstruction = instr;
	}
	
	
	public void setStalled(boolean stall){
		this.isStalled = stall;
	}

	public boolean isStalled(){
		return this.isStalled;
	}
	
	public boolean hasJumped(){
		return this.jumped;
	}
}
