package riscvSimulator;

public enum RiscVFunc {
	
	beq(RiscVOpCode.branch,0b0000000,0b000),
	bne(RiscVOpCode.branch,0b0000000,0b001),
	blt(RiscVOpCode.branch,0b0000000,0b100),
	bge(RiscVOpCode.branch,0b0000000,0b101),
	bltu(RiscVOpCode.branch,0b0000000,0b110),
	bgeu(RiscVOpCode.branch,0b0000000,0b111),
	
	lb(RiscVOpCode.load,0b0000000,0b000),
	lh(RiscVOpCode.load,0b0000000,0b001),
	lw(RiscVOpCode.load,0b0000000,0b010),
	lbu(RiscVOpCode.load,0b0000000,0b100),
	lhu(RiscVOpCode.load,0b0000000,0b101),
	
	sb(RiscVOpCode.store,0b0000000,0b000),
	sh(RiscVOpCode.store,0b0000000,0b001),
	sw(RiscVOpCode.store,0b0000000,0b010),
	
	addi(RiscVOpCode.opimm,0b0000000,0b000),
	slli(RiscVOpCode.opimm,0b0000000,0b001),
	slti(RiscVOpCode.opimm,0b0000000,0b010),
	sltiu(RiscVOpCode.opimm,0b0000000,0b011),
	xori(RiscVOpCode.opimm,0b0000000,0b100),
	srli(RiscVOpCode.opimm,0b0000000,0b101),
	srai(RiscVOpCode.opimm,0b0100000,0b101),
	ori(RiscVOpCode.opimm,0b0000000,0b110),
	andi(RiscVOpCode.opimm,0b0000000,0b111),
	
	add(RiscVOpCode.op,0b0000000,0b000),
	sub(RiscVOpCode.op,0b0100000,0b000),
	sll(RiscVOpCode.op,0b0000000,0b001),
	slt(RiscVOpCode.op,0b0000000,0b010),
	sltu(RiscVOpCode.op,0b0000000,0b011),
	srl(RiscVOpCode.op,0b0000000,0b101),
	sra(RiscVOpCode.op,0b0100000,0b101),
	or(RiscVOpCode.op,0b0000000,0b110),
	xor(RiscVOpCode.op,0b0000000,0b100),
	and(RiscVOpCode.op,0b0000000,0b111),
	
	mul(RiscVOpCode.op,0b0000001,0b000),
	mulh(RiscVOpCode.op,0b0000001,0b001),
	mulhsu(RiscVOpCode.op,0b0000001,0b010),
	mulhu(RiscVOpCode.op,0b0000001,0b011),
	div(RiscVOpCode.op,0b0000001,0b100),
	divu(RiscVOpCode.op,0b0000001,0b101),
	rem(RiscVOpCode.op,0b0000001,0b110),
	remu(RiscVOpCode.op,0b0000001,0b111),
	
	fence(RiscVOpCode.miscmem,0b0000000,0b000),
	fencei(RiscVOpCode.miscmem,0b0000000,0b001),
	
	ecall(RiscVOpCode.system,0b0000000,0b000),
	ebreak(RiscVOpCode.system,0b0000000,0b000),
	
	csrrw(RiscVOpCode.system,0b0000000,0b001),
	csrrs(RiscVOpCode.system,0b0000000,0b010),
	csrrc(RiscVOpCode.system,0b0000000,0b011),
	csrrwi(RiscVOpCode.system,0b0000000,0b101),
	csrrsi(RiscVOpCode.system,0b0000000,0b110),
	csrrci(RiscVOpCode.system,0b0000000,0b111),
	
	lui(RiscVOpCode.lui,0b0000000,0b000),
	auipc(RiscVOpCode.auipc,0b0000000,0b000),
	
	jal(RiscVOpCode.jal,0b0000000,0b000),
	jalr(RiscVOpCode.jalr,0b0000000,0b000);
	 
	private RiscVOpCode OpCode;
	private int Func3CodeValue;
	private int Func7CodeValue;
	
	private RiscVFunc(RiscVOpCode OpCode,int Func7CodeValue,int Func3CodeValue) {
		this.Func3CodeValue = Func3CodeValue;
		this.Func7CodeValue = Func7CodeValue;
		this.OpCode = OpCode;
	}
	public RiscVOpCode getOpCode() {
		return OpCode;
	}
	public int getFunc3Code() {
		return Func3CodeValue;
	};
	public int getFunc7Code() {
		return Func7CodeValue;
	};
}
