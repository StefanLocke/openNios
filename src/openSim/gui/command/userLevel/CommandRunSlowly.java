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

import openSim.gui.MainFrame;
import openSim.gui.command.Command;
import openSim.gui.command.systemLevel.ThreadCommandRunSlowly;
import openSim.gui.dialog.Player;

public class CommandRunSlowly implements Command
{

    private MainFrame mf;

    public CommandRunSlowly(MainFrame mf)
    {
        this.mf = mf;
    }

    @Override
    public void execute()
    { //check if state is executing and check if the current openDLX has finished (when it has finished ->updates are no longer allowed)
        if (mf.isExecuting() && mf.isUpdateAllowed())
        {
            new Thread(new ThreadCommandRunSlowly(mf)).start();
            new Player(mf);
        }
    }

}
