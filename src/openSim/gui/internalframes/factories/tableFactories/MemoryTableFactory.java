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

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;


import alternateSimulator.Simulator;
import openSim.exception.MemoryException;
import openSim.gui.MainFrame;
import openSim.gui.Preference;
import openSim.gui.command.userLevel.CommandChangeMemory;
import openSim.gui.internalframes.renderer.ChangeableFrameTableCellRenderer;
import openSim.gui.internalframes.util.NotSelectableTableModel;
import riscvSimulator.values.RiscVValue32;

public class MemoryTableFactory extends TableFactory
{

    private int rows;
    private int startAddr;
    private Simulator simulator;

    public MemoryTableFactory(int rows, int startAddr)
    {
        this.rows = rows;
        this.startAddr = startAddr;
        simulator = MainFrame.getInstance().getSimulator();
    }

    @Override
    public JTable createTable()
    {
        model = new NotSelectableTableModel();
        table = new JTable(model);
        table.setFocusable(false);
        model.addColumn("address");
        model.addColumn("value");

        for (long i = 0; i < rows; ++i)
		{
        	RiscVValue32 value = simulator.getMemory().loadWord(startAddr + i * 4,false);

		    final Object secondItem;
		    if (Preference.displayMemoryAsHex())
		        secondItem = Long.toHexString(value.getUnsignedValue());
		    else
		        secondItem = Long.toString(value.getUnsignedValue());

		    model.addRow(new Object[]
		            { "0x"+Long.toHexString(startAddr + i * 4), secondItem });
		}

        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setMaxWidth(150);
        tcm.getColumn(1).setMaxWidth(150);
        table.setDefaultRenderer(Object.class, new ChangeableFrameTableCellRenderer());

        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);

                if (e.getClickCount() == 2)
                    new CommandChangeMemory(Integer.parseInt(
                            model.getValueAt(row, 0).toString().substring(2),
                            16)).execute();
            }

        });

        table.setFillsViewportHeight(true);
        return table;
    }

}
