package riscvSimulator.tester;

import java.util.Random;

public enum TestRegisters {
	t0(5),
	t1(6),
	t2(7),
	t3(28),
	t4(29),
	t5(30),
	t6(31);
	
	
	int registerId;
	
	private TestRegisters(int number) {
		registerId = number;
	}
	
	public int getValue() {
		return registerId;
	}
	
	static public int getRandomRegister() {
		TestRegisters[] all = TestRegisters.values();
		Random r = new Random();
		
		return all[r.nextInt(all.length)].getValue();
	}
	
}
