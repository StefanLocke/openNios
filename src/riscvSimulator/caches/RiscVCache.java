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
	/**
	 * fills the cache with the given word, at a location calculated from the address.
	 * @param address
	 * @param value
	 */
	void setWord(long address,RiscVValue value);
	/**
	 * fills the cache with the given Half, at a location calculated from the address.
	 * @param address
	 * @param value
	 */
	void setHalf(long address,RiscVValue value);
	/**
	 * fills the cache with the given Byte, at a location calculated from the address.
	 * @param address
	 * @param value
	 */
	void setByte(long address,RiscVValue value);
	
	/**
	 * Gets the Word in from the cache at the specified address.
	 * @param address
	 * @return
	 */
	RiscVValue32 getWord(long address);
	/**
	 * Gets the Half in from the cache at the specified address.
	 * @param address
	 * @return
	 */
	RiscVValue16 getHalf(long address);
	/**
	 * Gets the Byte in from the cache at the specified address.
	 * @param address
	 * @return
	 */
	RiscVValue8 getByte(long address);
	
	List<Long> getCachedAddresses();
	int getSize();
}
