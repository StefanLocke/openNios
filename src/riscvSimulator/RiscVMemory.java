package riscvSimulator;

import java.util.HashMap;
import java.util.List;

import riscvSimulator.caches.RiscVCache;
import riscvSimulator.caches.SimpleCache;
import riscvSimulator.caches.SingleWayCache;
import riscvSimulator.caches.nWayCache;
import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class RiscVMemory {

	private HashMap<Long, RiscVValue8> memory;
	private RiscVCache cache;
	public RiscVCache getCache() {
		return cache;
	}

	
	public RiscVMemory(){
		this.memory = new HashMap<Long, RiscVValue8>();
		cache = new nWayCache(3, 4);
	}
	
	public RiscVValue8 loadByte(Long addr, boolean useCache){
		if (useCache) {
			if (cache.checkCache(addr)) {
				return cache.getByte(addr);
			}
		}
		if (this.memory.containsKey(addr))
			return this.memory.get(addr).copy();
		else
			return new RiscVValue8(0);
	}
	
	public RiscVValue16 loadHalf(long addr,boolean useCache){ 
		if (useCache) {
			if (cache.checkCache((int)addr)) {
				return cache.getHalf(addr);
			}
		}
		RiscVValue8 v1 = this.loadByte(addr, false);
		RiscVValue8 v2 = this.loadByte((addr+1), false);

		return new RiscVValue16(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8));
	}
	
	public RiscVValue32 loadWord(long addr,boolean useCache){
		if (useCache) {
			if (cache.checkCache((int)addr)) {
				return cache.getWord(addr);
			}
		}
		RiscVValue8 v1 = this.loadByte(addr, false);
		RiscVValue8 v2 = this.loadByte((addr+1), false);
		RiscVValue8 v3 = this.loadByte((addr+2), false);
		RiscVValue8 v4 = this.loadByte((addr+3), false);
		
		return new RiscVValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24));
	}
	
	
	public void storeByte(long addr, RiscVValue value, boolean useCache){
		if (useCache) {
			if (cache.checkCache((int) addr)) {
				cache.updateByte(addr,value);
			}
			else {
				cache.addByte(addr,value);
			}
		}
		this.memory.put(addr, new RiscVValue8(value.getUnsignedValue() & 0xff));
	}
	
	public void storeHalf(long addr, RiscVValue word, boolean useCache){
		if (useCache) {
			if (cache.checkCache((int) addr)) {
				cache.updateHalf(addr,word);
			}
			else {
				cache.addHalf(addr,word);
			}
		}
		this.storeByte((int) addr, new RiscVValue8(word.getUnsignedValue() & 0xff), false);
		this.storeByte((int) addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff), false);
	}
	
	public void storeWord(long addr, RiscVValue word,boolean useCache){
		if (useCache) {
			if (cache.checkCache((int) addr)) {
				cache.updateWord(addr,word);
			}
			else {
				System.out.println("Adding new word ");
				cache.addWord(addr,word);
			}
		}
		this.storeByte((int) addr, new RiscVValue8(word.getUnsignedValue() & 0xff), false);
		this.storeByte((int) addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff), false);
		this.storeByte((int) addr+2, new RiscVValue8((word.getUnsignedValue()>>16) & 0xff), false);
		this.storeByte((int) addr+3, new RiscVValue8((word.getUnsignedValue()>>24) & 0xff), false);
		
		System.out.println("Writing at 0x" + Long.toHexString(addr) + " : 0x" + Long.toHexString(word.getUnsignedValue()));
	}
	
	
	public List<Long> getCachedAddresses() {
		return (List<Long>) cache.getCachedAddresses();
	}
	
	
	/*--------------------CACHE--------------------*/
	
	
}
