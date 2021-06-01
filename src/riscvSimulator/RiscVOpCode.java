package riscvSimulator;

public enum RiscVOpCode {
	load(0b0000011),store(0b0100011),madd(0b1000011),branch(0b1100011),loadfp(0b0000111),storefp(0b0100111),msub(0b1000111),
	jalr(0b1100111),nmsub(0b1001011),miscmem(0b0001111),amo(0b0101111),nmadd(0b1001111),jal(0b1101111),opimm(0b0010011),
	op(0b0110011),opfp(0b1010011),system(0b1110011),auipc(0b0010111),lui(0b0110111),opimm32(0b0011011),op32(0b0111011),unknown(0b1111111); //TODO Add more ?
	
	
	private int opCodeValue;
	private RiscVOpCode(int opCodeValue){
		this.opCodeValue = opCodeValue;
	}

	public int getOpCode(){
		return opCodeValue;
	}
	
}
