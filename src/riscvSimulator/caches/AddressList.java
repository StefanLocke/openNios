package riscvSimulator.caches;

import java.util.LinkedList;

public class AddressList extends LinkedList<Long>{

	public void add(long address) {
		if (this.contains(address)) {
			this.remove(address);
		}
		this.addFirst(address);
	}
}
