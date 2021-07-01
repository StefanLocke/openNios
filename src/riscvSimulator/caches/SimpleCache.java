package riscvSimulator.caches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class SimpleCache implements RiscVCache{
	private HashMap<Long, RiscVValue8> cache;
	private LinkedList<Long> cachedAdresses;
	static public int cacheSize = 5;
	
	public SimpleCache() {
		this.cache = new HashMap<Long, RiscVValue8>();
		this.cachedAdresses = new LinkedList<>();
	}
	
	
	@Override
	public boolean checkCache(long address) {
		if (cachedAdresses.contains(address)) {
			System.out.println("Cache - Hit  : " + address);
			return true;
		}
		System.out.println("Cache - Miss : " + address);
		return false;
	}
	@Override
	public void setWord(long address, RiscVValue value) {
		cachedAdresses.addFirst(address);
		if (cachedAdresses.size() > cacheSize) {
			long rmAddress = cachedAdresses.removeLast();
			cache.remove(rmAddress);
			cache.remove(rmAddress+1);
			cache.remove(rmAddress+2);
			cache.remove(rmAddress+3);
		}
		storeWordCache(address, value);
	}
	@Override
	public void setHalf(long address, RiscVValue value) {
		cachedAdresses.addFirst(address);
		if (cachedAdresses.size() > cacheSize) {
			long rmAddress = cachedAdresses.removeLast();
			cache.remove(rmAddress);
			cache.remove(rmAddress+1);
		}
		storeHalfCache(address, value);
	}
	@Override
	public void setByte(long address, RiscVValue value) {
		cachedAdresses.addFirst(address);
		if (cachedAdresses.size() > cacheSize) {
			long rmAddress = cachedAdresses.removeLast();
			cache.remove(rmAddress);
			
		}
		storeByteCache(address, value);
	}
	@Override
	public RiscVValue32 getWord(long address) {
		cachedAdresses.remove(cachedAdresses.indexOf(address));
		cachedAdresses.addFirst(address);
		return loadWordCache(address);
	}
	@Override
	public RiscVValue16 getHalf(long address) {
		cachedAdresses.remove(cachedAdresses.indexOf(address));
		cachedAdresses.addFirst(address);
		return loadHalfCache(address);
	}
	@Override
	public RiscVValue8 getByte(long address) {
		cachedAdresses.remove(cachedAdresses.indexOf(address));
		cachedAdresses.addFirst(address);
		return loadByteCache(address);
	}
	
	
	
	private RiscVValue8 loadByteCache(long address){
		if (this.cache.containsKey(address))
			return this.cache.get(address).copy();
		else
			return new RiscVValue8(0);
	}
	
	public RiscVValue16 loadHalfCache(long address){ 
		RiscVValue8 v1 = this.loadByteCache(address);
		RiscVValue8 v2 = this.loadByteCache((address+1));

		return new RiscVValue16(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8));
	}
	
	public RiscVValue32 loadWordCache(long address){
		RiscVValue8 v1 = this.loadByteCache(address);
		RiscVValue8 v2 = this.loadByteCache((address+1));
		RiscVValue8 v3 = this.loadByteCache((address+2));
		RiscVValue8 v4 = this.loadByteCache((address+3));
		
		return new RiscVValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24));
	}
	
	
	
	public void storeWordCache(long address, RiscVValue word){
		this.storeByteCache(address, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.storeByteCache(address+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
		this.storeByteCache(address+2, new RiscVValue8((word.getUnsignedValue()>>16) & 0xff));
		this.storeByteCache(address+3, new RiscVValue8((word.getUnsignedValue()>>24) & 0xff));
	}
	
	
	public void storeHalfCache(long address, RiscVValue word){
		this.storeByteCache( address, new RiscVValue8(word.getUnsignedValue() & 0xff));
		this.storeByteCache(address+1, new RiscVValue8((word.getUnsignedValue()>>8) & 0xff));
	}
	
	
	public void storeByteCache(long address, RiscVValue value){
		this.cache.put(address, new RiscVValue8(value.getUnsignedValue() & 0xff));
	}
	

	public List<Long> getCachedAddresses() {
		return (List<Long>) cachedAdresses.clone();
	}
	
	public int getSize() {
		return 5;
	}
	
	
}
