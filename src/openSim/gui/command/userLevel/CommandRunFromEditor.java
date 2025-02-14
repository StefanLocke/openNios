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

import java.io.File;

import javax.swing.JInternalFrame;

import openSim.gui.MainFrame;
import openSim.gui.command.Command;
import openSim.gui.command.systemLevel.CommandCompileCode;
import openSim.gui.command.systemLevel.CommandResetSimulator;
import openSim.gui.command.systemLevel.CommandSaveFrameConfigurationSysLevel;
import openSim.gui.command.systemLevel.CommandStartExecuting;
import openSim.gui.command.systemLevel.CommandWriteToTmpFile;
import openSim.gui.internalframes.concreteframes.editor.EditorFrame;

public class CommandRunFromEditor implements Command
{

    private EditorFrame ef;

    public CommandRunFromEditor(EditorFrame ef)
    {
        this.ef = ef;
    }

    @Override
    public void execute()
    {
        MainFrame mf = MainFrame.getInstance();
        if (!mf.isRunning())
        {
            //save current window position
            new CommandSaveFrameConfigurationSysLevel(mf).execute();
            //create new temporary file
            CommandWriteToTmpFile c10 = new CommandWriteToTmpFile(ef.getText());
            c10.execute();
            File tmpFile = c10.getTmpFile();

            if (tmpFile != null)
            {
                CommandCompileCode c8 = new CommandCompileCode(mf, tmpFile);
                c8.execute();
                File configFile = c8.getConfigFile();

                JInternalFrame[] intFrames = mf.getinternalFrames();
                String[] intFrameOrder = new String[intFrames.length];
                for (int i = 0; i < intFrames.length; ++i)
                    intFrameOrder[i] = intFrames[i].getTitle();

                //initialize openDLX and create internal frames, set status to executing
                new CommandStartExecuting(mf, intFrameOrder).execute();
            
            }
        }
    }

}
