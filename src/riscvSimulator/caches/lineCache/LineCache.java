package riscvSimulator.caches.lineCache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.RiscVCache;
import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class LineCache implements RiscVCache {

	public int setLength;
	public int offsetLength;
	RiscVMemory memory;
	Map<Long,LinkedList<Long>> addresses; //List of sets and addresses at the set
	Map<Long,CacheItem> cache;
	public LineCache(int setSize , int offsetSize, RiscVMemory memory) {
		cache = new HashMap<>();
		this.setLength = setSize; 
		this.offsetLength = offsetSize;
		this.memory = memory;
	}
	public static final int ACTION_INPUT = 1;
	public static final int ACTION_OUTPUT = 2;
	private long lastAddress = 0;
	private long lastSet = 0;
	private long lastTag = 0;
	private long lastOffset = 0;
	private long lastSelector= 0;
	private int lastAction = -1;
	private long lastTagChange = -1;
	private int lastHit = -1;
	@Override
	public boolean checkCache(long address) {
		lastAddress = address;
		long set = getSet(address);
		lastSet = set;
		long tag = getTag(address);
		lastTag = tag;
		long offset = getOffset(address);
		lastOffset = offset;
		long selector = getSelector(address);
		lastSelector = selector;
		CacheItem item = cache.get(set);
		if (item != null && item.tag == tag) { 
			lastHit = 1;
			return true;
		}
		fillCache(address);
		lastHit = 0;
		return false;
	}
	
	private void fillCache(long address) {
		long set = getSet(address);
		long tag = getTag(address);
		lastTagChange = tag;
		int mask = (int) Math.pow(2, 2 + offsetLength);
		long maskedAddress = (address / mask) * mask;
		CacheItem tmp = new CacheItem(tag);
		
		for (int i = 0; i < Math.pow(2, offsetLength); i++) {
			
			tmp.setWord(i, memory.loadWord(maskedAddress + i*4, false));
		}
		cache.put(set, tmp);
	}

	@Override
	public void setWord(long address, RiscVValue value) {
		lastAction = ACTION_INPUT;
		long set = getSet(address);
		long tag = getTag(address);
		long offset = getOffset(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			if (item.tag == tag) {
				item.setWord(offset, value);
				return;
			}
		}
		CacheItem tmp = new CacheItem(tag);
		tmp.setWord(offset, value);
		cache.put(set, tmp);
		
	}

	@Override
	public void setHalf(long address, RiscVValue value) {
		lastAction = ACTION_INPUT;
		long set = getSet(address);
		long tag = getTag(address);
		long offset = getOffset(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			if (item.tag == tag) {
				item.setHalf(offset,selector, value);
				return;
			}
		}
		System.out.println("This should not apprear -- line cache 70");
		
	}

	@Override
	public void setByte(long address, RiscVValue value) {
		lastAction = ACTION_INPUT;
		long set = getSet(address);
		long tag = getTag(address);
		long offset = getOffset(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		if (item != null) {
			if (item.tag == tag) {
				item.setByte(offset,selector, value);
				return;
			}
		}
		System.out.println("This should not apprear -- line cache 87");
	}

	@Override
	public RiscVValue32 getWord(long address) {
		lastAction = ACTION_OUTPUT;
		long set = getSet(address);
		long tag = getTag(address);
		long offset = getOffset(address);
		CacheItem item = cache.get(set);
		
		return item.getWord(offset);
	}

	@Override
	public RiscVValue16 getHalf(long address) {
		lastAction = ACTION_OUTPUT;
		long set = getSet(address);
		long tag = getTag(address);
		long offset = getOffset(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		
		return item.getHalf(offset,selector);
	}

	@Override
	public RiscVValue8 getByte(long address) {
		lastAction = ACTION_OUTPUT;
		long set = getSet(address);
		long tag = getTag(address);
		long offset = getOffset(address);
		long selector = getSelector(address);
		CacheItem item = cache.get(set);
		
		return item.getByte(offset,selector);
	}

	@Override
	public List<Long> getCachedAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		
		return (int) Math.pow(2, setLength);
	}
	
	private long getTag(long address) {
		int shift = 2 + setLength + offsetLength;
		long tag = (address >> shift) & (long)(Math.pow(2, 32 - shift)-1);
		//System.out.println("Tag for " + address + " is " + tag);
		return tag;

	}
	
	private long getSet(long address) {
		int shift = 2 + offsetLength;
		long set = (address >> shift) & (long)(Math.pow(2, setLength)-1);
		//System.out.println("Set for " + address + " is " + set);
		return set;
	}
	
	private long getOffset(long address) {
		int shift = 2 ;
		long set = (address >> shift) & (long)(Math.pow(2, offsetLength)-1);
		//System.out.println("Offset for " + address + " is " + set);
		return set;
	}

	private long getSelector(long address) {
		long set = address & 0b11;
		//System.out.println("Offset for " + address + " is " + set);
		return set;
	}
	
	public RiscVValue8 findByte(long set, long data, long selector) {
		CacheItem tmp = cache.get(set);
		if (tmp == null) return new RiscVValue8(0);
		
		return tmp.getByte(data, selector);
	}
	
	public long findTag(long set) {
		CacheItem tmp = cache.get(set);
		if (tmp == null) return 0;
		return tmp.tag;
	}
	
	public int getLastHit() {
		return lastHit;
	}
	public long getLastAddress() {
		return lastAddress;
	}
	public long getLastSet() {
		return lastSet;
	}
	public long getLastTag() {
		return lastTag;
	}
	public long getLastOffset() {
		return lastOffset;
	}
	public long getLastSelector() {
		return lastSelector;
	}
	public int getLastAction() {
		return lastAction;
	}
	public long getLastTagChange() {
		return lastTagChange;
	}
	@Override
	public String toString() {
		String r = "";
		for (Long l : cache.keySet()) {
			r = r + "(Set:" + l + " " + cache.get(l).toString() + ")\n";
		}
		return r;
	}
}
