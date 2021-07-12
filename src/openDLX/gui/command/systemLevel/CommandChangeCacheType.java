package openDLX.gui.command.systemLevel;

import alternateSimulator.Simulator;
import openDLX.gui.MainFrame;
import openDLX.gui.command.Command;
import riscvSimulator.RiscVMemory;
import riscvSimulator.caches.RiscVCache;

public class CommandChangeCacheType implements Command {

	
	
	private Simulator sim;
	private String name;
	
	public CommandChangeCacheType(MainFrame mf,String cacheTypeName) {
		this.sim = mf.getSimulator();
		this.name = cacheTypeName;
	}
	
	@Override
	public void execute() {
		RiscVMemory memory = sim.getMemory();
		if (name == "nWayCache") {
			memory.switchCacheType(RiscVCache.CacheType.nWayCache);
			return;
		}
		if (name == "LineCache") {
			memory.switchCacheType(RiscVCache.CacheType.LineCache);
			return;
		}
		System.out.println("CommandChangeCacheType : Unknown cache Type : " + name);
	}

}
