package riscvSimulator;

import java.util.ArrayList;

import com.sun.tools.javac.util.List;

import riscvSimulator.func3.*;

public class InstructionRiscV {

	private long pc;
	private RiscVOpCode op;
	private RiscVType type; 
	private RiscVFunc func;
	

	
	private int imm12; //for I-TYPE S-TYPE
	private int imm20; //for J-TYPE
	private int rd;
	private int r1;
	private int r2;
	
	private RiscVValue32 valueA;
	private RiscVValue32 valueB;
	private RiscVValue32 aluResult;
	private RiscVValue valueToStore;
	private RiscVValue32 valueLoaded;
	
	public boolean isDecodeDone;
	public boolean isExecuteDone;
	public boolean isMemoryDone; 
	public boolean isWritebackDone;
	
	private long cycleNumber;
	

	
	public InstructionRiscV(long binaryInstruction, long pc, long startCycle){
		this.pc = pc;
		int opBinary = (int) (binaryInstruction & 0x7F);						// get the first 7 bits of the instruction by doing a bitwise and operation
		this.setCycleNumber(startCycle);

		for (RiscVOpCode opcode : RiscVOpCode.values()){						// find the corresponding OpCode
			if (opcode.getOpCode() == opBinary){
				this.op = opcode;
				break;
			}
			
			this.op = RiscVOpCode.unknown;
		}

		
		this.isDecodeDone = false;
		this.isExecuteDone = false; 
		this.isMemoryDone = false;
		this.isWritebackDone = false;
		
		switch(op){
		
		case load: {
			this.type = RiscVType.ITYPE;
			int func3Binary = (int) ((binaryInstruction >> 12) & 0x7); //get the func3 binary value
			for (RiscVFunc func : RiscVFunc.values()) {
				if (func.getFunc3Code() == func3Binary && func.getOpCode() == op) {
					this.func = func;
					break;
				}
			}
			this.imm12 = (int) ((binaryInstruction >> 20) & 0xfff);
			this.rd = (int) ((binaryInstruction >> 7) & 0x1f);
			this.r1 = (int) ((binaryInstruction >> 15) & 0x1f);
			break;
		}
		case jalr:
			//TODO
			break;
		case miscmem :
			//TODO
			break;
		case opimm : {
			this.type = RiscVType.ITYPE;
			int func3Binary = (int) ((binaryInstruction >> 12) & 0x7); //get the func3 binary value
			for (RiscVFunc func : RiscVFunc.values()) {
				if (func.getFunc3Code() == func3Binary && func.getOpCode() == op) {
					this.func = func;
					break;
				}
			}
			this.imm12 = (int) ((binaryInstruction >> 20) & 0xfff);
			this.rd = (int) ((binaryInstruction >> 7) & 0x1f);
			this.r1 = (int) ((binaryInstruction >> 15) & 0x1f);
			break;
		}
		case system : {
			//TODO
			break;
		}
		case store : {
			//S-TYPE
			this.type = RiscVType.STYPE;
			int func3Binary = (int) ((binaryInstruction >> 12) & 0x7); //get the func3 binary value
			for (RiscVFunc func : RiscVFunc.values()) {
				
				if (func.getFunc3Code() == func3Binary && func.getOpCode() == op) {
					this.func = func;
					break;
				}
			}
			
			int firstPart = (int)((binaryInstruction >> 25) & 0x7f);
			int secondPart = (int) ((binaryInstruction >> 7) & 0x1f);
			this.imm12 = ( firstPart << 5) | secondPart;
			
			this.r1 = (int) ((binaryInstruction >> 15) & 0x1f);
			this.r2 = (int) ((binaryInstruction >> 20) & 0x1f);
			break;
		}
		case branch: {
			// B-TYPE
			this.type = RiscVType.BTYPE;
			int func3Binary = (int) ((binaryInstruction >> 12) & 0x7); //get the func3 binary value
			for (RiscVFunc func3 : RiscVFunc.values()) {
				if (func3.getFunc3Code() == func3Binary  && func.getOpCode() == op) {
					this.func = func3;
					break;
				}
			}
			
			
			int firstPart = (int) ((binaryInstruction >> 8) & 0xf);
			int secondPart = (int) ((binaryInstruction >> 25) & 0x3f);
			int thirdPart = (int) ((binaryInstruction >> 7) & 0x1);
			int fourthPart =(int) ((binaryInstruction >> 31) & 0x1);
			
			int tmp1 = ( firstPart << 6) | secondPart;
			int tmp2 = ( thirdPart << 1) | fourthPart;
			this.imm12 = ( tmp1 << 2) | tmp2;
			
			this.r1 = (int) ((binaryInstruction >> 15) & 0x1f);
			this.r2 = (int) ((binaryInstruction >> 20) & 0x1f);
			break;
		}
		case jal : {
			//J-TYPE
			this.type = RiscVType.JTYPE;
			
			int firstPart = (int) ((binaryInstruction >> 21) & 0x3ff);
			int secondPart = (int) ((binaryInstruction >> 20) & 0x1);
			int thirdPart = (int) ((binaryInstruction >> 12) & 0xff);
			int fourthPart =(int) ((binaryInstruction >> 31) & 0x1);
			
			int tmp1 = ( firstPart << 1) | secondPart;
			int tmp2 = ( thirdPart << 1) | fourthPart;
			this.imm20 = ( tmp1 << 9) | tmp2;
			
			this.rd = (int) ((binaryInstruction >> 7) & 0x1f);
			break;
		}
		case op : {
			//TODO R-TYPE
			this.type = RiscVType.RTYPE;
			int func3Binary = (int) ((binaryInstruction >> 12) & 0x7);  //get the func3 binary value
			int func7Binary = (int) ((binaryInstruction >> 25) & 0x7f); //get the func7 binary value
			for (RiscVFunc func : RiscVFunc.values()) {
				if (func.getFunc3Code() == func3Binary && func.getFunc7Code() == func7Binary && func.getOpCode() == op) {
					this.func = func;
					break;
				}
			}
			
			this.rd = (int) ((binaryInstruction >> 7) & 0x1f);
			this.r1 = (int) ((binaryInstruction >> 15) & 0x1f);
			this.r2 = (int) ((binaryInstruction >> 20) & 0x1f);
			break;
		}
		case lui :
			break;
		case auipc :
			//TODO U-TYPE
			break;
		default :
			//TODO REST
			break;
		}
		
	}
	
	
	public int getWrittenRegister(){	
		
		switch (this.getType()) {
		

		case JTYPE:
			return rd;
		case RTYPE:
			return rd;
		case UTYPE:
			return rd;
		case ITYPE:
			return rd;
		case BTYPE:
		case STYPE:
		case UNKNOWN:
			break;
		}
		return -1;
	}
	
	public ArrayList<Integer> getUsedRegisters(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		switch (this.getType()){
		case BTYPE:
			result.addAll(List.of(r1,r2));
			break;
		case ITYPE:
			result.addAll(List.of(r1));
			break;	
		case RTYPE:
			result.addAll(List.of(r1,r2));
			break;
		case STYPE:
			result.addAll(List.of(r1,r2));
			break;
		case UNKNOWN:
		case UTYPE:
		case JTYPE:	
		default:
			break;
			
		}
		
		return result;		
	}
	
	//******************************************************************************************
	//*              Getters and setters
	//******************************************************************************************

	public RiscVValue32 getValueA() {
		return valueA;
	}


	public void setValueA(RiscVValue32 valueA) {
		this.valueA = valueA;
	}


	public RiscVValue32 getValueB() {
		return valueB;
	}


	public void setValueB(RiscVValue32 valueB) {
		this.valueB = valueB;
	}


	public RiscVValue32 getAluResult() {
		return aluResult;
	}


	public void setAluResult(RiscVValue32 aluResult) {
		this.aluResult = aluResult;
	}


	public RiscVValue getValueToStore() {
		return valueToStore;
	}


	public void setValueToStore(RiscVValue valueToStore) {
		this.valueToStore = valueToStore;
	}


	public RiscVValue32 getValueLoaded() {
		return valueLoaded;
	}


	public void setValueLoaded(RiscVValue32 valueLoaded) {
		this.valueLoaded = valueLoaded;
	}


	public RiscVOpCode getOp() {
		return op;
	}


	public RiscVType getType() {
		return type;
	}


	public RiscVFunc getOpx() {
		return func;
	}


	public int getImm12() {
		return imm12;
	}


	public int getImm20() {
		return imm20;
	}


	public int getRa() {
		return rd;
	}


	public int getRb() {
		return r1;
	}


	public int getRc() {
		return r2;
	}
	
	public long getPC(){
		return this.pc;
	}

	public String toString(){ //TODO maybe just use types ?
		switch (op) {
		case op :
			return func.name() + " r" + rd + ", r" + r1 + ", r"+ r2; 
		case opimm :
			return func.name() + " r" + rd + ", r" + r1 + ", " + imm12; 
		default:
			break;
		
		}
		return "NOT YET IMPL IN TOSTRING";
		
	}

	public long getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(long cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
}
	
