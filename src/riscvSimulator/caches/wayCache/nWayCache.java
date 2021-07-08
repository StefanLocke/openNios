package riscvSimulator.caches.wayCache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.DataEntry;
import riscvSimulator.caches.RiscVCache;
import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class nWayCache implements RiscVCache {
	
	public static final int setColumn = 0;
	public static final int tagColumn = 1;
	public static final int valueColumn = 2;
	Map<Long,CacheItem> cache;
	int setLength;
	int tagLength;
	public int wayCount;
	RiscVMemory memory;
	
	private long lastAddress = 0;
	private long lastTag = 0;
	private long lastSet = 0;
	private long lastSelector = 0;
	private long lastValue = 0;
	
	private int lastWay = 0;
	private int lastHitMiss = 0;
	private int lastAction = 0;
	
	public nWayCache(int setLength,int wayCount,RiscVMemory mem) {
		cache = new HashMap<Long,CacheItem>();	
		this.wayCount = wayCount;
		this.setLength = setLength;
		this.tagLength = 32 - setLength - 2;
		memory = mem;
	}
	
	
	
	@Override
	public boolean checkCache(long address) {
		lastAddress = address;
		long tag = getTag(address);
		lastTag = tag;
		long set = getSet(address);
		lastSet = set;
		long selector = getSelector(address);
		lastSelector = selector;
		
		CacheItem item = cache.get(set);
		if (item == null) {
			fillCache(address);
			lastHitMiss = 0;
			return false;
		}
		DataItem data = item.getFromTag(tag);
		if (data == null) {
			fillCache(address);
			lastHitMiss = 0;
			return false;
		}
		lastHitMiss = 1;
		return true;
	}
	
	private void fillCache(long address) {
		long tag = getTag(address);
		long set = getSet(address);
		CacheItem item = cache.get(set);
		if (item == null) {
			item = new CacheItem(wayCount);
			cache.put(set, item);
		}
		item.putWord(tag, memory.loadWord(address, false));
	}

	@Override
	public void setWord(long address, RiscVValue value) {
		lastAction = WRITE_ACTION;
		long tag = getTag(address);
		long set = getSet(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			item.putWord(tag, value);
		}
	}
	

	@Override
	public void setHalf(long address, RiscVValue value) {
		lastAction = WRITE_ACTION;
		long tag = getTag(address);
		long set = getSet(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			item.putHalf(tag,selector, value);
		}
	}
	
	@Override
	public void setByte(long address, RiscVValue value) {
		lastAction = WRITE_ACTION;
		long tag = getTag(address);
		long set = getSet(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			item.putByte(tag,selector, value);
		}
	}
	


	@Override
	public RiscVValue32 getWord(long address) {
		lastAction = READ_ACTION;
		long tag = getTag(address);
		long set = getSet(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			DataItem data = item.getFromTag(tag);
			return data.data.getWord();
		}
		
		return null;
	}
	

	@Override
	public RiscVValue16 getHalf(long address) {
		lastAction = READ_ACTION;
		long tag = getTag(address);
		long set = getSet(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			DataItem data = item.getFromTag(tag);
			return data.data.getHalf(selector);
		}
		
		return null;
		
	}
	

	@Override
	public RiscVValue8 getByte(long address) {
		lastAction = READ_ACTION;
		long tag = getTag(address);
		long set = getSet(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			DataItem data = item.getFromTag(tag);
			return data.data.getByte(selector);
		}
		item.putWord(tag, memory.loadWord(address, false));
		return null;
	}
	
	@Override
	public List<Long> getCachedAddresses() {
		
		return null;
	}
	

	@Override
	public int getSize() {
	
		return (int)Math.pow(2, setLength);
	}
	
	
	

	
	class CacheItem {
		public CacheItem(int wayCount) {
			data = new DataItem[wayCount];
			history = new LinkedList<>();
		}
		LinkedList<Long> history;
		DataItem[] data;
		
		public DataItem getFromTag(long tag) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] != null) {
					if (data[i].tag == tag) {
						if (history.contains(tag)) history.remove(tag);
						history.addFirst(tag);
						lastWay = i;
						return data[i];
					}
				}
			}
			return null;
			
		}
		
		public void putWord(long tag,RiscVValue value) {
			DataItem d = getFromTag(tag);
			if (d != null) {
				d.data.setWord(value);
				return;
			}
			
			int indexToModify = 0;
			for (int i = 0; i < data.length; i++) {
				lastWay = i;
				if (data[i] == null) {
					data[i] = new DataItem();
					data[i].tag = tag;
					data[i].data = new DataEntry();
					data[i].data.setWord(value);
					return;
				}
				if (data[i].tag == tag) {
					data[i].data.setWord(value);
					return;
				}
				if (data[i].tag == history.getLast()) {
					indexToModify = i;
				}
			}
			lastWay = indexToModify;
			data[indexToModify].tag = tag;
			data[indexToModify].data.setWord(value);
		}
		public void putHalf(long tag,long selector,RiscVValue value) {
			int indexToModify = 0;
			for (int i = 0; i < data.length; i++) {
				if (data[i].tag == tag) {
					data[i].data.setHalf(value, selector);
					return;
				}
				if (data[i].tag == history.getLast()) {
					indexToModify = i;
				}
			}
			System.out.println("Bad location, should not be here - 181");
			data[indexToModify].tag = tag;
			data[indexToModify].data.setHalf(value, selector);
		}
		public void putByte(long tag,long selector,RiscVValue value) {
			int indexToModify = 0;
			for (int i = 0; i < data.length; i++) {
				if (data[i].tag == tag) {
					data[i].data.setByte(value, selector);
					return;
				}
				if (data[i].tag == history.getLast()) {
					indexToModify = i;
				}
			}
			System.out.println("Bad location, should not be here - 181");
			data[indexToModify].tag = tag;
			data[indexToModify].data.setByte(value, selector);
		}
		
		
		@Override
		public String toString() {
			String s = "";
			for (int i = 0; i < data.length ; i++) {
				if (data[i] != null)
					s = s + "(Way:" + i + " -" + data[i].toString() + ")";
			}
			return s;
		}
	}
	
	class DataItem {
		long tag;
		DataEntry data;
		
		@Override
		public String toString() {
			
			return "[Tag:" + tag + " -" +data.toString() + "]";
		}
	}
	
	private long getTag(long address) {
		long tag = (address >> 2 + setLength) & (long)(Math.pow(2, 32 - setLength - 2)-1);
		//System.out.println("Tag for " + address + " is " + tag);
		return tag;

	}
	
	private long getSet(long address) {
		long set = (address >> 2) & (long)(Math.pow(2, setLength)-1);
		//System.out.println("Set for " + address + " is " + set);
		return set;
	}
	private long getSelector(long address) {
		long set = address & 0b11;
		//System.out.println("Offset for " + address + " is " + set);
		return set;
	}

	public RiscVValue8 findByte(long set,long way, long selector) {
		CacheItem item = cache.get(set);
		if (item != null) {
			DataItem data = item.data[(int) way];
			if (data != null) {
				return data.data.getByte(selector);
			}
		}
		return new RiscVValue8(0);
	}
	
	public long findTag(long set,int way) {
		CacheItem item = cache.get(set);
		if (item != null) {
			DataItem data = item.data[way];
			if (data != null) {
				return data.tag;
			}
		}
		return 0;
	}
	@Override
	public String toString() {
		String r = "";
		for (Long l : cache.keySet()) {
			r = r + "(Set:" + l + " " + cache.get(l).toString() + ")\n";
		}
		return r;
	}

	public int getSetLength() {
		return setLength;
	}
	
	public int getWayCount() {
		return wayCount;
	}


	@Override
	public int getLastAction() {
		
		return lastAction;
	}



	public long getLastAddress() {
		// TODO Auto-generated method stub
		return lastAddress;
	}



	public long getLastSet() {
		// TODO Auto-generated method stub
		return lastSet;
	}



	public long getLastTag() {
		// TODO Auto-generated method stub
		return lastTag;
	}



	public long getLastValue() {
		// TODO Auto-generated method stub
		return lastValue;
	}



	public int getLastWay() {
		// TODO Auto-generated method stub
		return lastWay;
	}



	public long getLastSelector() {
		// TODO Auto-generated method stub
		return lastSelector;
	}



	public int getLastHitMiss() {
		// TODO Auto-generated method stub
		return lastHitMiss;
	}
}
