package riscvSimulator.steps;

import riscvSimulator.InstructionRiscV;
import riscvSimulator.RegisterFileRiscV;
import riscvSimulator.RiscVALU;
import riscvSimulator.values.RiscVValue32;

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
					currentInstruction.setAluResult(RiscVALU.add(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case sub :
					currentInstruction.setAluResult(RiscVALU.sub(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case sll : //FIX TO DO ONLKY LAST 5 bits from RS2
					currentInstruction.setAluResult(RiscVALU.shiftLeftLogical(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case slt :
					currentInstruction.setAluResult(RiscVALU.setLessThan(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case sltu :
					currentInstruction.setAluResult(RiscVALU.setLessThanUnsigned(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case srl :
					currentInstruction.setAluResult(RiscVALU.shiftRightLogical(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case sra :
					currentInstruction.setAluResult(RiscVALU.shiftRightArithmetic(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case or :
					currentInstruction.setAluResult(RiscVALU.or(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case xor :
					currentInstruction.setAluResult(RiscVALU.xor(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case and :
					currentInstruction.setAluResult(RiscVALU.and(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case mul : //Mul of 2 signed 32 bits and places the lower 32 bits in the register
					currentInstruction.setAluResult(RiscVALU.mul(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case mulh ://Mul of 2 signed 32 bits and places the upper 32 bits in the register
					currentInstruction.setAluResult(RiscVALU.mulUpper(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case mulhsu ://Mul of a  signed  and unsigned 32 bits and places the upper 32 bits in the register
					currentInstruction.setAluResult(RiscVALU.mulUpperSignedOnUnsigned(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case mulhu ://Mul of 2 unsigned 32 bits and places the upper 32 bits in the register
					currentInstruction.setAluResult(RiscVALU.mulUpperUnsigned(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case div ://div of 2 signed 32 bits
					currentInstruction.setAluResult(RiscVALU.div(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case divu ://div of 2 unsigned 32 bits
					currentInstruction.setAluResult(RiscVALU.divUnsigned(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case rem ://rest of the division of 2 signed 32 bits
					currentInstruction.setAluResult(RiscVALU.rem(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case remu ://rest of the division of 2 unsigned 32 bits
					currentInstruction.setAluResult(RiscVALU.remUnsigned(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				default:
					break;
				}
				break;
				
				
			case opimm :
				switch (currentInstruction.getFunc()) {
				case addi :
					currentInstruction.setAluResult(RiscVALU.add(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case slti:
					currentInstruction.setAluResult(RiscVALU.setLessThanImmediate(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case sltiu:
					currentInstruction.setAluResult(RiscVALU.setLessThanImmediateUnsigned(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case slli:
					currentInstruction.setAluResult(RiscVALU.shiftLeftLogical(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case srli:
					currentInstruction.setAluResult(RiscVALU.shiftRightLogical(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case srai:
					currentInstruction.setAluResult(RiscVALU.shiftRightArithmetic(currentInstruction.getValueA(),currentInstruction.getValueB()));
					break;
				case ori:
					currentInstruction.setAluResult(RiscVALU.or(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case xori:
					currentInstruction.setAluResult(RiscVALU.xor(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				case andi:
					currentInstruction.setAluResult(RiscVALU.and(currentInstruction.getValueA(), currentInstruction.getValueB()));
					break;
				default:
					break;
				}
				break;
				
				
			case branch :
				switch (currentInstruction.getFunc()) {
				case beq :
					if (currentInstruction.getValueA().getSignedValue() == currentInstruction.getValueB().getSignedValue()) {
						System.out.println(Long.toHexString(currentInstruction.getPC()) + " offset is  "  + currentInstruction.getValueToStore().getSignedValue() + " ("+ Long.toHexString(currentInstruction.getValueToStore().getSignedValue()) + ")");
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue()<< 1 | 0b0)));
						this.hasJumped = true;
					}	
					break;
				case bne :
					if (currentInstruction.getValueA().getSignedValue() != currentInstruction.getValueB().getSignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue()<< 1 | 0b0)));
						this.hasJumped = true;
					}
					break;
				case blt :
					if (currentInstruction.getValueA().getSignedValue() < currentInstruction.getValueB().getSignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0)));
						this.hasJumped = true;
					}
					break;
				case bge :
					if (currentInstruction.getValueA().getSignedValue() >= currentInstruction.getValueB().getSignedValue()) {
						System.out.println(currentInstruction.getPC() + " jumping to " + currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0));
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0)));
						this.hasJumped = true;
					}
					break;
				case bltu :
					if (currentInstruction.getValueA().getUnsignedValue() < currentInstruction.getValueB().getUnsignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0)));
						this.hasJumped = true;
					}
					break;
				case bgeu :
					if (currentInstruction.getValueA().getUnsignedValue()  >= currentInstruction.getValueB().getUnsignedValue()) {
						registers.setPC(new RiscVValue32(currentInstruction.getPC() + (currentInstruction.getValueToStore().getSignedValue() << 1 | 0b0)));
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
				System.out.println("Execute - "+ currentInstruction.getOp() + "is not yet implemented.");
				break;
				
				
			case load :
				switch (currentInstruction.getFunc()) {
				case lb :
				case lh :
				case lw :
				case lbu :
				case lhu :
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue()));
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
					currentInstruction.setAluResult(new RiscVValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue()));
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
				System.out.println("Execute - "+ currentInstruction.getOp() + "is not yet implemented.");
				break;
				
			case auipc:
				currentInstruction.setAluResult(RiscVALU.addUpperImmediateToPc(currentInstruction.getValueA(),new RiscVValue32(currentInstruction.getPC())));
				break;
			case lui:
				currentInstruction.setAluResult(RiscVALU.loadUpperImmediate(currentInstruction.getValueA()));
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
