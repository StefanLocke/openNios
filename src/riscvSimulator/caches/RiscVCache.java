package riscvSimulator.caches;

import java.util.List;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public interface RiscVCache {
	static int setSize = 3;
	public static final int byteLocatorSize = 2;
	static int tagSize = 32 - setSize - byteLocatorSize;
	
	boolean checkCache(long address);
	void addWord(long address,RiscVValue value);
	void addHalf(long address,RiscVValue value);
	void addByte(long address,RiscVValue value);
	void updateWord(long address,RiscVValue value);
	void updateHalf(long address,RiscVValue value);
	void updateByte(long address,RiscVValue value);
	RiscVValue32 getWord(long address);
	RiscVValue16 getHalf(long address);
	RiscVValue8 getByte(long address);
	List<Long> getCachedAddresses();
	int getSize();
}
