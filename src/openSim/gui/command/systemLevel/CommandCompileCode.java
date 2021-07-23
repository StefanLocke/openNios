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
package openSim.gui.command.systemLevel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import openSim.config.ArchCfg;
import openSim.gui.MainFrame;
import openSim.gui.command.Command;

public class CommandCompileCode implements Command
{
	public static final String compilerNameWindowsNios = "/external_bin/win-nios2-elf-as";
	public static final String compilerNameMacNios= "/external_bin/mac-nios2-elf-as";
	public static final String compilerNameLinuxNios = "/external_bin/lnx-nios2-elf-as";
	
	public static final String compilerNameWindowsRV = "/external_bin/win-riscv32-unknown-elf-as.exe";
	public static final String compilerNameMacRV= "/external_bin/lnx-riscv32-unknown-elf-as";
	public static final String compilerNameLinuxRV = "/external_bin/riscv64-zephyr-elf-as";
	
	public static final String linkerNameWindowsRV = "/external_bin/win-riscv32-unknown-elf-ld.exe";
	public static final String linkerNameLinuxRV = "/external_bin/riscv64-zephyr-elf-ld";
	
	public static final String outPutNameNios = "./nios2-elf-as";
	public static final String generatedAssemblerRV = "./rv-elf-as.exe";
	public static final String generatedLinkerRV = "./rv-elf-ld.exe";
	
	public static final String lnxasArgs = "-march=rv32im ";
	public static final String lnxldArgs = "-m elf32lriscv ";
	
	public static final String unLinkedElf = "./file.elf";
	public static final String LinkedElf = "./linkedfile.elf";

	
    private File codeFile = null; // in 
    private File configFile = null; // out
    private MainFrame mf;

    public CommandCompileCode(MainFrame mf, File in)
    {
        codeFile = in;
        this.mf = mf;
    }

    @Override
    public void execute()
    {
    	try
    	{
            if (codeFile != null)
            {
            	
            	File oldElfFile = new File(unLinkedElf);
            	if (oldElfFile.exists())
            		oldElfFile.delete();
            	
                String codeFilePath = codeFile.getAbsolutePath().replace("\\", "/");
                Runtime rt = Runtime.getRuntime();
                
                InputStream is;
                InputStream isl;
                
                String system = System.getProperty("os.name");
                System.out.println("Current system is : " + system);
                String asArgs = "";
                String ldArgs = "";
                if (system.matches(".*Windows.*")) {
            		is = this.getClass().getResource(compilerNameWindowsRV).openStream();
            		isl = this.getClass().getResource(linkerNameWindowsRV).openStream();
                }
                else if (system.matches(".*Mac.*")) {
        			is = this.getClass().getResource(compilerNameMacRV).openStream();
        			isl = this.getClass().getResource(linkerNameLinuxRV).openStream();
                	}
                
        		else {
        			asArgs = lnxasArgs;
        			ldArgs = lnxldArgs;
        			is = this.getClass().getResource(compilerNameLinuxRV).openStream();
        			isl= this.getClass().getResource(linkerNameLinuxRV).openStream();
        		}
                OutputStream os = new FileOutputStream(generatedAssemblerRV);
                
                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                
                is.close();
                os.close();
                
                OutputStream osl = new FileOutputStream(generatedLinkerRV);
                
                byte[] bl = new byte[2048];
                int lengthl;

                while ((lengthl = isl.read(bl)) != -1) {
                    osl.write(bl, 0, lengthl);
                }
                
                isl.close();
                osl.close();
                
                File file = new File(generatedAssemblerRV);
                file.setExecutable(true);
                
                File filel = new File(generatedLinkerRV);
                filel.setExecutable(true);
                
                Process ps = rt.exec(generatedAssemblerRV + " " + asArgs +codeFilePath + " -o " + unLinkedElf);
                ps.waitFor();
                Process ps2 = rt.exec(generatedLinkerRV + " " + ldArgs + unLinkedElf + " -o " + LinkedElf);
                //Process ps = rt.exec("riscv32-unknown-elf-as.exe" + " " + codeFilePath + " -o ./file.elf");
                ps2.waitFor();
                ArrayList<String> errorMessage = getStringFromInputStream(ps.getErrorStream());
                
                if (errorMessage.size() != 0){
                	System.out.println("Errors while assembling");
	                String errorDisplayed = "";
	                for (String oneLine : errorMessage){
	            			errorDisplayed += oneLine + "\n";
	                }
	                JOptionPane.showMessageDialog(mf, errorDisplayed);
	                
                }
                errorMessage = getStringFromInputStream(ps2.getErrorStream());
                if (errorMessage.size() != 0){
                	System.out.println("Errors while linking");
	                String errorDisplayed = "";
	                for (String oneLine : errorMessage) {
	            			errorDisplayed += oneLine + "\n";
	                }
	                JOptionPane.showMessageDialog(mf, errorDisplayed);
	                
                }
                
            }     
        }
        catch (Exception e)
        {
           System.err.println(e.toString());
           e.printStackTrace();
           JOptionPane.showMessageDialog(mf, "Compiling/Assembling Code Failed");
        }
    }
    
	// convert InputStream to String
	private static ArrayList<String> getStringFromInputStream(InputStream is) {

		ArrayList<String> result = new ArrayList<String>();
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				result.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;

	}

    public File getConfigFile()
    {
        return configFile;
    }

}
