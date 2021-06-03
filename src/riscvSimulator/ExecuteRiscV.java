package riscvSimulator;

public class ExecuteRiscV {

	private boolean isLast;
	private boolean hasJumped;
	private InstructionRiscV currentInstruction;
	private RegisterFileRiscV registers;

	
	public ExecuteRiscV(RegisterFileRiscV registers, boolean isLast){
		this.isLast = isLast;
		this.registers = registers;
	}
	
	public void doStep(){
		this.hasJumped = false;
		if (isLast){
			switch(currentInstruction.getOp()){
			case op : 
				switch (currentInstruction.getFunc()) {
				case add :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
					break;
				case sub :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
					break;
				case sll : //FIX TO DO ONLKY LAST 5 bits from RS2
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() << currentInstruction.getValueB().getSignedValue(), true));
					break;
				case slt :
					break;
				case sltu :
					break;
				case xor :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() ^ currentInstruction.getValueB().getSignedValue(), true));
					break;
				case srl :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() >>> currentInstruction.getValueB().getSignedValue(), true));
					break;
				case sra :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() >> currentInstruction.getValueB().getSignedValue(), true));
					break;
				case or :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() | currentInstruction.getValueB().getSignedValue(), true));
					break;
				case and :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() & currentInstruction.getValueB().getSignedValue(), true));
					break;
				case mul :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() * currentInstruction.getValueB().getSignedValue(), true));
					break;
				case mulh :
					break;
				case mulhsu :
					break;
				case mulhu :
					break;
				case div :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() / currentInstruction.getValueB().getSignedValue(), true));
					break;
				case divu :
					break;
				case rem :
					break;
				case remu :
					break;
				default:
					break;
				}
				break;
				
				
			case opimm :
				switch (currentInstruction.getFunc()) {
				case addi :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
					break;
				case slli:
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() << currentInstruction.getValueB().getSignedValue(), true));
					break;
				case slti:
					break;
				case sltiu:
					break;
				case xori:
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() ^ currentInstruction.getValueB().getSignedValue(),true));
					break;
				case srli:
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() >> currentInstruction.getValueB().getSignedValue(), true));
					break;
				case srai:
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() >> currentInstruction.getValueB().getSignedValue(), true));
					break;
				case ori:
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() | currentInstruction.getValueB().getSignedValue(), true));
					break;
				case andi:
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() & currentInstruction.getValueB().getSignedValue(), true));
					break;
				default:
					break;
				}
				break;
				
				
			case branch :
				switch (currentInstruction.getFunc()) {
				case beq :
					if (currentInstruction.getValueA().getSignedValue() == currentInstruction.getValueB().getSignedValue()) {
						System.out.println(Integer.toHexString((int)currentInstruction.getPC()) + " jumping to " + Integer.toHexString((int) (currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0))));
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue()<< 1 | 0b0), true));
						this.hasJumped = true;
					}	
					break;
				case bne :
					if (currentInstruction.getValueA().getSignedValue() != currentInstruction.getValueB().getSignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue()<< 1 | 0b0), true));
						this.hasJumped = true;
					}
					break;
				case blt :
					if (currentInstruction.getValueA().getSignedValue() < currentInstruction.getValueB().getSignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0), true));
						this.hasJumped = true;
					}
					break;
				case bge :
					if (currentInstruction.getValueA().getSignedValue() >= currentInstruction.getValueB().getSignedValue()) {
						System.out.println(currentInstruction.getPC() + " jumping to " + currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0));
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0), true));
						this.hasJumped = true;
					}
					break;
				case bltu :
					if (currentInstruction.getValueA().getUnsignedValue() < currentInstruction.getValueB().getUnsignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0), true));
						this.hasJumped = true;
					}
					break;
				case bgeu :
					if (currentInstruction.getValueA().getUnsignedValue()  >= currentInstruction.getValueB().getUnsignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0), true));
						this.hasJumped = true;
					}
					break;
				default:
					break;
				
				}
				break;
				
				
			case system ://TODO
				switch (currentInstruction.getFunc()) {
				case ecall :
					break;
				case ebreak :
					break;
				case csrrw :
					break;
				case csrrs :
					break;
				case csrrc :
					break;
				case csrrwi :
					break;
				case csrrsi :
					break;
				case csrrci :
					break;
				default:
					break;
				}
				break;
				
				
			case load :
				switch (currentInstruction.getFunc()) {
				case lb :
				case lh :
				case lw :
				case lbu :
				case lhu :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
					break;
				default:
					break;	
				}
				break;
				

			case store :
				switch (currentInstruction.getFunc()) {
				case sb :
				case sh :
				case sw :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
					break;
				default:
					break;	
				}
				break;
						
				
			case jalr:
			case jal:
				System.out.println("Instruction already managed(unconditionnal jump)");
				break;
				
				
			case miscmem : //TODO
				switch (currentInstruction.getFunc()) {
				case fence :
					break;
				case fencei :
					break;
				default :
					break;
				}
				break;
				
				
			default:
				break;
			}
		}
	}
	
	public InstructionRiscV getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(InstructionRiscV instr){
		this.currentInstruction = instr;
	}
	
	public boolean hasJumped(){
		return this.hasJumped;
	}
}
