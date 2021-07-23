/*******************************************************************************
 * openDLX - A DLX/MIPS processor simulator.
 * Copyright (C) 2013 The openDLX project, University of Augsburg, Germany
 * Project URL: <https://sourceforge.net/projects/opendlx>
 * Development branch: <https://github.com/smetzlaff/openDLX>
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, see <LICENSE>. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package openSim.gui.internalframes.factories.tableFactories;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;


import alternateSimulator.Simulator;
import openSim.exception.MemoryException;
import openSim.gui.MainFrame;
import openSim.gui.internalframes.renderer.CodeFrameTableCellRenderer;
import openSim.gui.internalframes.util.NotSelectableTableModel;
import riscvSimulator.InstructionRiscV;
import riscvSimulator.values.RiscVValue32;

public class CodeTableFactory extends TableFactory
{

    private Simulator simulator;

    public CodeTableFactory(Simulator simulator)
    {
        this.simulator = simulator;
    }

    @Override
    public JTable createTable()
    {

        model = new NotSelectableTableModel();
        table = new JTable(model);
        table.setFocusable(false);

        model.addColumn("address");
        model.addColumn("code hex");
        model.addColumn("code Risc-V");

        //default max width values change here
        TableColumnModel tcm = table.getColumnModel();
        final int defaultWidth = 150;
        tcm.getColumn(0).setMaxWidth(defaultWidth);
        tcm.getColumn(1).setMaxWidth(defaultWidth);
        tcm.getColumn(2).setMaxWidth(defaultWidth);
        table.setDefaultRenderer(Object.class, new CodeFrameTableCellRenderer());

        //insert code
        int start;
        if (!simulator.getConfig().containsKey("text_begin"))
            start = (int) simulator.getRegisterFile().getPC().getUnsignedValue();
        else
            start = stringToInt(simulator.getConfig().getProperty("text_begin"));

        int end = simulator.getSimCycles();
        if (!simulator.getConfig().containsKey("text_end"))
            end = start + 4 * simulator.getSimCycles();
        else
            end = stringToInt(simulator.getConfig().getProperty("text_end"));

        for (long addr = start; addr < end; addr += 4)
		{
        	RiscVValue32 instBinary = simulator.getMemory().loadWord(addr,false);
		    
        	InstructionRiscV instr = new InstructionRiscV(instBinary.getUnsignedValue(), addr, -1);
		    

		    model.addRow(new Object[]
		            {
		                Long.toHexString(addr),
		                //Long.toHexString(instBinary.getUnsignedValue()),
		                String.format("%1$08x",instBinary.getUnsignedValue()),
		                instr.toString()
		            });
		}
        return table;
    }

    private int stringToInt(String s)
    {
        return Long.decode(s).intValue();
    }

}
