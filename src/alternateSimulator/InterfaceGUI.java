package alternateSimulator;

import java.util.Properties;

import openSim.util.ClockCycleLog;
import openSim.util.Statistics;

public class InterfaceGUI {


    
    public InterfaceGUI(){
    }

    
    public void clearCycleLog(){
        ClockCycleLog.getCycleLog().clear();
    }

    public void addCycleLog(CycleDescription desc){
    	ClockCycleLog.add(desc);
    }
}

