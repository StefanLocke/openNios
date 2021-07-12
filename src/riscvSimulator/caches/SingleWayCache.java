package riscvSimulator.caches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class SingleWayCache implements RiscVCache{
	
	public static final int setColumn = 0;
	public static final int tagColumn = 1;
	public static final int valueColumn = 2;
	Map<Long,CacheItem> cache;
	int setLength;
	int tagLength;
	LinkedList<Long> cachedAddresses;
	
	public SingleWayCache(int setLength) {
		cache = new HashMap<>();
		this.setLength = setLength;
		this.tagLength = 32 - setLength - 2;
		cachedAddresses = new LinkedList<>();
	}
	
	@Override
	public boolean checkCache(long address) {
		System.out.println(toString());
		CacheItem item = cache.get(getSet(address));
		if (item == null) {
			System.out.println("Cache - Miss : " + address);
			return false;
		}
		if (item.tag == getTag(address)) {
			System.out.println("Cache - Hit  : " + address);
			return true;
		}
		System.out.println("Cache - Miss : " + address);
		return false;
	}

	@Override
	public void setWord(long address, RiscVValue value) {
		long set = getSet(address);
		long tag = getTag(address);
		RiscVValue8 b1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		RiscVValue8 b2 = new RiscVValue8((value.getUnsignedValue() >> 8) & 0xff);
		RiscVValue8 b3 = new RiscVValue8((value.getUnsignedValue() >> 16) & 0xff);
		RiscVValue8 b4 = new RiscVValue8((value.getUnsignedValue() >> 24) & 0xff);
		cache.put(set, new CacheItem(tag, b4, b3, b2, b1));
		cachedAddresses.addFirst(address);
		checkOverFlow();
		
	}

	@Override
	public void setHalf(long address, RiscVValue value) {
		long set = getSet(address);
		long tag = getTag(address);
		RiscVValue8 b1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		RiscVValue8 b2 = new RiscVValue8((value.getUnsignedValue() >> 8) & 0xff);
		cache.put(set, new CacheItem(tag, null, null, b2, b1));
		cachedAddresses.addFirst(address);
		checkOverFlow();
	}

	@Override
	public void setByte(long address, RiscVValue value) {
		long set = getSet(address);
		long tag = getTag(address);
		RiscVValue8 b1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		cache.put(set, new CacheItem(tag, null, null, null, b1));
		cachedAddresses.addFirst(address);
		checkOverFlow();
	}

	
	@Override
	public RiscVValue32 getWord(long address) {
		long set = getSet(address);
		long tag = getTag(address);
		CacheItem item = cache.get(set);
		if (item == null)
			return new RiscVValue32(0);
		if (item.tag != tag) 
			return new RiscVValue32(0);
		cachedAddresses.remove(address);
		cachedAddresses.addFirst(address);
		return new RiscVValue32(item.Byte4.getUnsignedValue() << 24 | (item.Byte3.getUnsignedValue() << 16 | (item.Byte2.getUnsignedValue() << 8 | (item.Byte1.getUnsignedValue()))));
	}

	@Override
	public RiscVValue16 getHalf(long address) {
		long set = getSet(address);
		long tag = getTag(address);
		CacheItem item = cache.get(set);
		if (item == null)
			return new RiscVValue16(0);
		if (item.tag != tag) 
			return new RiscVValue16(0);
		cachedAddresses.remove(address);
		cachedAddresses.addFirst(address);
		return new RiscVValue16(item.Byte2.getUnsignedValue() << 8 | (item.Byte1.getUnsignedValue()));
	}

	@Override
	public RiscVValue8 getByte(long address) {
		long set = getSet(address);
		long tag = getTag(address);
		CacheItem item = cache.get(set);
		if (item == null)
			return new RiscVValue8(0);
		if (item.tag != tag) 
			return new RiscVValue8(0);
		cachedAddresses.remove(address);
		cachedAddresses.addFirst(address);
		return new RiscVValue8(item.Byte1.getUnsignedValue());
	}

	@Override
	public List<Long> getCachedAddresses() {
		
		return cachedAddresses;
	}
	
	private long getTag(long address) {
		long tag = (address >> 2 + setLength) & (long)(Math.pow(2, 32 - setLength - 2)-1);
		System.out.println("Tag for " + address + " is " + tag);
		return tag;

	}
	
	private long getSet(long address) {
		long set = (address >> 2) & (long)(Math.pow(2, setLength)-1);
		System.out.println("Set for " + address + " is " + set);
		return set;
	}
	
	private void removeItem(long address) {
		cachedAddresses.remove(address);
	}
	
	private void checkOverFlow() {
		if (cachedAddresses.size() > Math.pow(2, setLength)) {
			long addressToRemove = cachedAddresses.removeLast();
			removeItem(addressToRemove);
		}
	}
	
	class CacheItem {
		public CacheItem(long tag,RiscVValue8 Byte4, RiscVValue8 Byte3, RiscVValue8 Byte2,RiscVValue8 Byte1) {
			this.Byte1 = Byte1;
			this.Byte2 = Byte2;
			this.Byte3 = Byte3;
			this.Byte4 = Byte4;
			this.tag = tag;
		}
		long tag;
		RiscVValue8 Byte1; //LSB
		RiscVValue8 Byte2;
		RiscVValue8 Byte3;
		RiscVValue8 Byte4; //MSB
		
		
		public String toString() {
			return "tag :" + tag + " Value :" + Byte4.toString() + " " + Byte3.toString() + " " + Byte2.toString() + " " + Byte1.toString();
		}
	}
	
	
	public int getSize() {
		return (int)Math.pow(2, setLength);
	}
	
	public long findTag(long set) {
		CacheItem item = cache.get(set);
		if (item == null) {
			
			return 0;
		}
		return item.tag;
	}
	
	public RiscVValue8 findByte(long set, int byteNumber) {
		CacheItem item = cache.get(set);
		if (item == null) {
			return new RiscVValue8(0);
		}
		switch (byteNumber) {
			case 1 : return item.Byte1;
			case 2 : return item.Byte2;
			case 3 : return item.Byte3;
			case 4 : return item.Byte4;
			default : return new RiscVValue8(0);
		}
		
		
	}
	
	public String toString() {
		String s = "[";
		for (Long l : cache.keySet()) {
			s = s + "Set :" + l + " " +cache.get(l).toString() + ", ";
		}
		
		return s + "]";
	}

	@Override
	public int getLastAction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getLastHit() {
		// TODO Auto-generated method stub
		return false;
	}
}
