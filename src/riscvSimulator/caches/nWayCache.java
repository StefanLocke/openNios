package riscvSimulator.caches;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import riscvSimulator.caches.SingleWayCache.CacheItem;
import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class nWayCache implements RiscVCache {
	
	public static final int setColumn = 0;
	public static final int tagColumn = 1;
	public static final int valueColumn = 2;
	List<Map<Long,CacheItem>> cache;
	int setLength;
	int tagLength;
	public int wayCount;
	LinkedList<Long> cachedAddresses;
	
	public nWayCache(int setLength,int wayCount) {
		cache = new LinkedList<>();
		for (int i = 0 ; i < wayCount; i++) {
			cache.add(i, new HashMap<>());
		}
		
		this.wayCount = wayCount;
		this.setLength = setLength;
		this.tagLength = 32 - setLength - 2;
		cachedAddresses = new LinkedList<>();
	}
	
	
	
	@Override
	public boolean checkCache(long address) {
		/*long set = getSet(address);
		long tag = getTag(address);
		for (int i = 0; i < wayCount ; i++) {
			CacheItem item = get(i,set);
			if (item != null) {
				if (item.tag == tag)  
					return true;
			}
		}
		return false;*/
		return cachedAddresses.contains(address);
	}

	@Override
	public void setWord(long address, RiscVValue value) {
		/*long set = getSet(address);
		LinkedList<Long> addresses = new LinkedList<>();
		for (int i = 0 ; i < wayCount; i++) {
			if (cache.get(i).get(set) != null)
				addresses.add(cache.get(i).get(set).address);
		}
		if (addresses.size() < wayCount) {
			addWord(addresses.size(),address,value);
			return;
		}
		LinkedList<Long> cachedAddressesInSets = new LinkedList<>();
		for (Long l :cachedAddresses) {
			if (getSet(l) == set)
				cachedAddressesInSets.addLast(l);
		}
		long last = cachedAddressesInSets.getLast();
		for (int i = 0 ; i < wayCount; i++) {
			if (cache.get(i).get(set) != null) {
				if (cache.get(i).get(set).address == last) {
					addWord(i,address,value);
					return;
				}
			}
		}*/
		long set = getSet(address);
		for (int i = 0 ; i < wayCount ; i++) {
			if (cache.get(i).get(set) == null ) {
				addWord(i,address,value);
				return;
			}
		}
		
		int lastIndex = 0;
		for (int i = 0 ; i < wayCount ; i++) {
			long tempAddrs = cache.get(i).get(set).address;
			int tmpIdx = cachedAddresses.indexOf(tempAddrs);
			if (tmpIdx > lastIndex) {
				lastIndex = tmpIdx ;	
			}
		}
		System.out.println("Last index is " + lastIndex);
		for (int i = 0 ; i < wayCount ; i++) {
			System.out.println("comparing : " + cachedAddresses.get(lastIndex) + " with " + cache.get(i).get(set).address);
			if (cachedAddresses.get(lastIndex) == cache.get(i).get(set).address) {
				cachedAddresses.remove(lastIndex);
				addWord(i,address,value);
				return;
			}
		}
	}
	public void addWord(int way, long address, RiscVValue value) {
		long set = getSet(address);
		long tag = getTag(address);
		RiscVValue8 b1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		RiscVValue8 b2 = new RiscVValue8((value.getUnsignedValue() >> 8) & 0xff);
		RiscVValue8 b3 = new RiscVValue8((value.getUnsignedValue() >> 16) & 0xff);
		RiscVValue8 b4 = new RiscVValue8((value.getUnsignedValue() >> 24) & 0xff);
		cache.get(way).put(set, new CacheItem(tag,address, b4, b3, b2, b1));
		cachedAddresses.addFirst(address);
	}

	@Override
	public void setHalf(long address, RiscVValue value) {
		long set = getSet(address);
		LinkedList<Long> addresses = new LinkedList<>();
		for (int i = 0 ; i < wayCount; i++) {
			if (cache.get(i).get(set) != null)
				addresses.add(cache.get(i).get(set).address);
		}
		if (addresses.size() < wayCount) {
			addHalf(addresses.size(),address,value);
			return;
		}
		LinkedList<Long> cachedAddressesInSets = new LinkedList<>();
		for (Long l :cachedAddresses) {
			if (getSet(l) == set)
				cachedAddressesInSets.addLast(l);
		}
		long last = cachedAddressesInSets.getLast();
		for (int i = 0 ; i < wayCount; i++) {
			if (cache.get(i).get(set) != null) {
				if (cache.get(i).get(set).address == last) {
					addHalf(i,address,value);
					return;
				}
			}
		}
		
		
	}
	public void addHalf(int way, long address, RiscVValue value) {
		long set = getSet(address);
		long tag = getTag(address);
		RiscVValue8 b1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		RiscVValue8 b2 = new RiscVValue8((value.getUnsignedValue() >> 8) & 0xff);
		cache.get(way).put(set, new CacheItem(tag,address, null, null, b2, b1));
		cachedAddresses.addFirst(address);
	}

	@Override
	public void setByte(long address, RiscVValue value) {
		long set = getSet(address);
		LinkedList<Long> addresses = new LinkedList<>();
		for (int i = 0 ; i < wayCount; i++) {
			if (cache.get(i).get(set) != null)
				addresses.add(cache.get(i).get(set).address);
		}
		if (addresses.size() < wayCount) {
			addByte(addresses.size(),address,value);
			return;
		}
		LinkedList<Long> cachedAddressesInSets = new LinkedList<>();
		for (Long l :cachedAddresses) {
			if (getSet(l) == set)
				cachedAddressesInSets.addLast(l);
		}
		long last = cachedAddressesInSets.getLast();
		for (int i = 0 ; i < wayCount; i++) {
			if (cache.get(i).get(set) != null) {
				if (cache.get(i).get(set).address == last) {
					addByte(i,address,value);
					return;
				}
			}
		}
		
	}
	public void addByte(int way, long address, RiscVValue value) {
		long set = getSet(address);
		long tag = getTag(address);
		RiscVValue8 b1 = new RiscVValue8(value.getUnsignedValue() & 0xff);
		cache.get(way).put(set, new CacheItem(tag,address, null, null, null, b1));
		cachedAddresses.addFirst(address);
		
	}


	@Override
	public RiscVValue32 getWord(long address) {
		long set = getSet(address);
		long tag = getTag(address);
		for (int i = 0; i < wayCount; i++) {
			if (cache.get(i).get(set).tag == tag) {
				return getWord(i,address);
			}
		}
		return new RiscVValue32(0);
	}
	public RiscVValue32 getWord(int way, long address) {
		long set = getSet(address);
		long tag = getTag(address);
		CacheItem item = cache.get(way).get(set);
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
		for (int i = 0; i < wayCount; i++) {
			if (cache.get(i).get(set).tag == tag) {
				return getHalf(i,address);
			}
		}
		return new RiscVValue16(0);
	}
	public RiscVValue16 getHalf(int way, long address) {
		long set = getSet(address);
		long tag = getTag(address);
		CacheItem item = cache.get(way).get(set);
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
		for (int i = 0; i < wayCount; i++) {
			if (cache.get(i).get(set).tag == tag) {
				return getByte(i,address);
			}
		}
		return new RiscVValue8(0);
	}
	public RiscVValue8 getByte(int way, long address) {
		long set = getSet(address);
		long tag = getTag(address);
		CacheItem item = cache.get(way).get(set);
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
	

	@Override
	public int getSize() {
	
		return (int)Math.pow(2, setLength);
	}
	
	private CacheItem get(int way,long set) {
		
		return cache.get(way).get(set);
		
	}
	
	private void set(int way, long set,CacheItem item) {
		 cache.get(way).put(set, item);	
	}
	
	public long findTag(int way,long set) {
		CacheItem item = cache.get(way).get(set);
		if (item == null) {
			
			return 0;
		}
		return item.tag;
	}
	
	public RiscVValue8 findByte(int way, long set, int byteNumber) {
		CacheItem item = cache.get(way).get(set);
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
	
	class CacheItem {
		public CacheItem(long tag,long address,RiscVValue8 Byte4, RiscVValue8 Byte3, RiscVValue8 Byte2,RiscVValue8 Byte1) {
			this.Byte1 = Byte1;
			this.Byte2 = Byte2;
			this.Byte3 = Byte3;
			this.Byte4 = Byte4;
			this.tag = tag;
			this.address = address;
		}
		long address;
		long tag;
		RiscVValue8 Byte1; //LSB
		RiscVValue8 Byte2;
		RiscVValue8 Byte3;
		RiscVValue8 Byte4; //MSB
		
		
		public String toString() {
			return "tag :" + tag + " Value :" + Byte4.toString() + " " + Byte3.toString() + " " + Byte2.toString() + " " + Byte1.toString();
		}
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

}
