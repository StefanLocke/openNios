package riscvSimulator.tester;

import org.junit.jupiter.api.Test;

public class MiscTest {

	@Test
	public void test() {
		long a = -1;
		long b = 8;
		long r = a << b;
		printAll(r);
		
	}
	
	
	public void printAll(long a ) {
		System.out.println(a + " is 0x" + String.format("%016x", a));
		
	}
}
