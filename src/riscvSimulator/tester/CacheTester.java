package riscvSimulator.tester;

import org.junit.jupiter.api.Test;

import riscvSimulator.RiscVMemory;
import riscvSimulator.values.RiscVValue32;

public class CacheTester {

	RiscVMemory memory = new RiscVMemory();
	
	
	@Test
	void test() {
		System.out.println(memory.getCache().toString());
		memory.getCache().checkCache(0);
		System.out.println(memory.getCache().toString());
		memory.storeWord(0, new RiscVValue32(255), true);
		memory.storeWord(4, new RiscVValue32(255), true);
		memory.storeWord(8, new RiscVValue32(255), true);
		memory.storeWord(12, new RiscVValue32(255), true);
		memory.storeWord(16, new RiscVValue32(255), true);
		System.out.println(memory.getCache().toString());
		memory.storeWord(128, new RiscVValue32(128), true);
		System.out.println(memory.getCache().toString());
		memory.storeWord(12, new RiscVValue32(254), true);
		System.out.println(memory.getCache().toString());
		memory.storeWord(128, new RiscVValue32(128), true);
		System.out.println(memory.getCache().toString());
		memory.loadWord(12, true);
		System.out.println(memory.getCache().toString());
	}
}
