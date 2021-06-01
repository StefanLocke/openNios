package riscvSimulator;

public abstract class RiscVValue {
	
	public abstract long getSignedValue();
	public abstract long getUnsignedValue();
	
	public abstract RiscVValue copy();

}
