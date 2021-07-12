package riscvSimulator.caches;

import java.util.List;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public interface RiscVCache {
	
	
	public static enum CacheType {
		nWayCache,
		LineCache
	}
	static final int selectorSize = 2;
	public static final int NO_ACTION = 0;
	public static final int READ_ACTION = 1;
	public static final int WRITE_ACTION = 2;
	
	public boolean checkCache(long address);
	/**
	 * fills the cache with the given word, at a location calculated from the address.
	 * @param address
	 * @param value
	 */
	public void setWord(long address,RiscVValue value);
	/**
	 * fills the cache with the given Half, at a location calculated from the address.
	 * @param address
	 * @param value
	 */
	public void setHalf(long address,RiscVValue value);
	/**
	 * fills the cache with the given Byte, at a location calculated from the address.
	 * @param address
	 * @param value
	 */
	public void setByte(long address,RiscVValue value);
	
	/**
	 * Gets the Word in from the cache at the specified address.
	 * @param address
	 * @return
	 */
	public RiscVValue32 getWord(long address);
	/**
	 * Gets the Half in from the cache at the specified address.
	 * @param address
	 * @return
	 */
	public RiscVValue16 getHalf(long address);
	/**
	 * Gets the Byte in from the cache at the specified address.
	 * @param address
	 * @return
	 */
	public RiscVValue8 getByte(long address);
	
	public boolean getLastHit();

	
	public int getLastAction();
	List<Long> getCachedAddresses();
	int getSize();
}
