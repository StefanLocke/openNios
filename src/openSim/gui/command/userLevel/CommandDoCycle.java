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
package openSim.gui.command.userLevel;

import alternateSimulator.Simulator;
import openSim.exception.PipelineException;
import openSim.gui.MainFrame;
import openSim.gui.command.Command;
import openSim.gui.command.systemLevel.CommandSimulatorFinishedInfo;
import openSim.gui.command.systemLevel.CommandUpdateFrames;

public class CommandDoCycle implements Command
{

    private MainFrame mf;

    public CommandDoCycle(MainFrame mf)
    {
        this.mf = mf;
    }

    @Override
    public void execute()
    {

        if (mf.isExecuting() && mf.isUpdateAllowed())
        {
            Simulator simulator = mf.getSimulator();
            try
            {
                simulator.step();
            }
            catch (PipelineException e)
            {
                mf.getPipelineExceptionHandler().handlePipelineExceptions(e);
            }

            if (!simulator.isFinished())
            {
                new CommandUpdateFrames(mf).execute();
            }
            else
            {
                mf.setUpdateAllowed(false);
                new CommandSimulatorFinishedInfo().execute();
            }
        }

    }

}
