package riscvSimulator.caches.lineCache;

import java.util.HashMap;
import java.util.Map;

import riscvSimulator.caches.DataEntry;
import riscvSimulator.values.RiscVValue;
import riscvSimulator.values.RiscVValue16;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class CacheItem {
	Map<Long,DataEntry> data;
	long tag;
	public CacheItem(long tag) {
		this.data = new HashMap<>();
		this.tag = tag;
	}
	
	public RiscVValue32 getWord(long offset) {
		if (!data.containsKey(offset)) return null;
		return data.get(offset).getWord();
	}
	
	public RiscVValue16 getHalf(long offset,long selector) {
		if (!data.containsKey(offset)) return null;
		return data.get(offset).getHalf(selector);
	}
	
	public RiscVValue8 getByte(long offset,long selector) {
		if (!data.containsKey(offset)) return null;
		return data.get(offset).getByte(selector);
	}
	
	public void setWord(long offset,RiscVValue data) {
		//We must use this before all sets to get the values from ram
		DataEntry entry = new DataEntry();
		entry.setWord(data);
		this.data.put(offset,entry);
	}
	
	public void setHalf(long offset,long selector,RiscVValue data) {
		DataEntry entry = this.data.get(offset);
		entry.setHalf(data, selector);
	}
	public void setByte(long offset,long selector,RiscVValue data) {	
		DataEntry entry = this.data.get(offset);
		entry.setByte(data, selector);
	}
	
	@Override
	public String toString() {
		return "<Tag:" + tag + data.toString() + ">";
	}
	
}
