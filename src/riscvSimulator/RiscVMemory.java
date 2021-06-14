package riscvSimulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class RiscVMemory {

	private HashMap<Long, RiscVValue8> memory;
	private HashMap<Long, RiscVValue8> cache;
	private LinkedList<Long> cachedAdresses;
	static public int cacheSize = 5;
	
	
	public RiscVMemory(){
		this.memory = new HashMap<Long, RiscVValue8>();
		this.cache = new HashMap<Long, RiscVValue8>();
		this.cachedAdresses = new LinkedList<>();
	}
	
	public RiscVValue8 loadByte(Long addr, boolean useCache){
		if (useCache) {
			if (checkCache(addr)) {
				return getCacheByte(addr);
			}
		}
		if (this.memory.containsKey(addr))
			return this.memory.get(addr).copy();
		else
			return new RiscVValue8(0);
	}
	
	public RiscVValue16 loadHalf(long addr,boolean useCache){ 
		if (useCache) {
			if (checkCache((int)addr)) {
				return getCacheHalf(addr);
			}
		}
		RiscVValue8 v1 = this.loadByte(addr, false);
		RiscVValue8 v2 = this.loadByte((addr+1), false);

		return new RiscVValue16(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8));
	}
	
	public RiscVValue32 loadWord(long addr,boolean useCache){
		if (useCache) {
			if (checkCache((int)addr)) {
				return getCacheWord(addr);
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
			if (checkCache((int) addr)) {
				updateByteCache(value,addr);
			}
			else {
				addByteToCache(value,addr);
			}
		}
		this.memory.put(addr, new RiscVValue8(value.getUnsignedValue() & 0xff));
	}
	
	public void storeHalf(long addr, RiscVValue word, boolean useCache){
		if (useCache) {
			if (checkCache((int) addr)) {
				updateHalfCache(word,addr);
			}
			else {
				addHalfToCache(word,addr);
			}
		}
		this.storeByte((int) addr, new RiscVValue8(word.getUnsignedValue() & 0xff), false);
		this.storeByte((int) addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff), false);
	}
	
	public void storeWord(long addr, RiscVValue word,boolean useCache){
		if (useCache) {
			if (checkCache((int) addr)) {
				updateWordCache(word,addr);
			}
			else {
				addWordToCache(word,addr);
			}
		}
		this.storeByte((int) addr, new RiscVValue8(word.getUnsignedValue() & 0xff), false);
		this.storeByte((int) addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff), false);
		this.storeByte((int) addr+2, new RiscVValue8((word.getUnsignedValue()>>16) & 0xff), false);
		this.storeByte((int) addr+3, new RiscVValue8((word.getUnsignedValue()>>24) & 0xff), false);
		
		System.out.println("Writing at 0x" + Long.toHexString(addr) + " : 0x" + Long.toHexString(word.getUnsignedValue()));
	}
	
	
	
	
	
	/*--------------------CACHE--------------------*/
	
	private RiscVValue8 loadByteCache(long addr){
		if (this.cache.containsKey(addr))
			return this.cache.get(addr).copy();
		else
			return new RiscVValue8(0);
	}
	
	public RiscVValue16 loadHalfCache(long addr){ 
		RiscVValue8 v1 = this.loadByteCache(addr);
		RiscVValue8 v2 = this.loadByteCache((addr+1));

		return new RiscVValue16(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8));
	}
	
	public RiscVValue32 loadWordCache(long addr){
		RiscVValue8 v1 = this.loadByteCache(addr);
		RiscVValue8 v2 = this.loadByteCache((addr+1));
		RiscVValue8 v3 = this.loadByteCache((addr+2));
		RiscVValue8 v4 = this.loadByteCache((addr+3));
		
		return new RiscVValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24));
	}
	
	
	
	public void storeWordCache(long addr, RiscVValue word){
		this.storeByteCache(addr, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.storeByteCache(addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
		this.storeByteCache(addr+2, new RiscVValue8((word.getUnsignedValue()>>16) & 0xff));
		this.storeByteCache(addr+3, new RiscVValue8((word.getUnsignedValue()>>24) & 0xff));
	}
	
	
	public void storeHalfCache(long addr, RiscVValue word){
		this.storeByteCache( addr, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.storeByteCache(addr+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
	}
	
	
	public void storeByteCache(long addr, RiscVValue value){
		this.cache.put(addr, new RiscVValue8(value.getUnsignedValue() & 0xff));
	}
	
	private boolean checkCache(long addr) {
		if (cachedAdresses.contains(addr)) {
			System.out.println("Cache - Hit  : " + addr);
			return true;
		}
		System.out.println("Cache - Miss : " + addr);
		return false;
	}
	
	private void updateWordCache(RiscVValue word,long addr) {
		cachedAdresses.remove(cachedAdresses.indexOf(addr));
		cachedAdresses.addFirst(addr);
		storeWordCache(addr, word);
	}
	private void updateHalfCache(RiscVValue word,long addr) {
		cachedAdresses.remove(cachedAdresses.indexOf(addr));
		cachedAdresses.addFirst(addr);
		storeHalfCache(addr, word);
	}
	private void updateByteCache(RiscVValue word,long addr) {
		cachedAdresses.remove(cachedAdresses.indexOf(addr));
		cachedAdresses.addFirst(addr);
		storeByteCache(addr, word);
	}
	
	private void addWordToCache(RiscVValue word,long addr) {
		cachedAdresses.addFirst(addr);
		if (cachedAdresses.size() > cacheSize) {
			long rmAddress = cachedAdresses.removeLast();
			cache.remove(rmAddress);
			cache.remove(rmAddress+1);
			cache.remove(rmAddress+2);
			cache.remove(rmAddress+3);
		}
		
		storeWordCache(addr, word);
	}
	private void addHalfToCache(RiscVValue word,long addr) {
		cachedAdresses.addFirst(addr);
		if (cachedAdresses.size() > cacheSize) {
			long rmAddress = cachedAdresses.removeLast();
			cache.remove(rmAddress);
			cache.remove(rmAddress+1);
		}
		storeHalfCache(addr, word);
	}
	private void addByteToCache(RiscVValue word,long addr) {
		cachedAdresses.addFirst(addr);
		if (cachedAdresses.size() > cacheSize) {
			long rmAddress = cachedAdresses.removeLast();
			cache.remove(rmAddress);
			
		}
		storeByteCache(addr, word);
	}
	
	private RiscVValue32 getCacheWord(long addr) {
		cachedAdresses.remove(cachedAdresses.indexOf(addr));
		cachedAdresses.addFirst(addr);
		return loadWordCache(addr);
	}
	
	private RiscVValue16 getCacheHalf(long addr) {
		cachedAdresses.remove(cachedAdresses.indexOf(addr));
		cachedAdresses.addFirst(addr);
		return loadHalfCache(addr);
	}
	private RiscVValue8 getCacheByte(long addr) {
		cachedAdresses.remove(cachedAdresses.indexOf(addr));
		cachedAdresses.addFirst(addr);
		return loadByteCache(addr);
	}
	
	public List<Long> getCachedAddresses() {
		return (List<Long>) cachedAdresses.clone();
	}
}
