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
package openDLX.config;

import java.util.Properties;

import openDLX.gui.Preference;

public class ArchCfg
{

    // forwarding implies the two boolean: use_forwarding and use_load_stall_bubble
    public static boolean use_forwarding = Preference.pref.getBoolean(Preference.forwardingPreferenceKey, true);

    // TODO: rename variable
    public static boolean  use_load_stall_bubble = Preference.pref.getBoolean(Preference.mipsCompatibilityPreferenceKey, true);


    public static final String[] GP_NAMES_NIOS =
    {
        "x0 ", "x1 ", "x2 ", "x3 ", "x4 ", "x5 ", "x6 ", "x7 ", "x8 ", "x9 ", "x10", "x11", "x12", "x13", "x14", "x15",
        "x16", "x17", "x18", "x19", "x20", "x21", "x22", "x23", "x24", "x25", "x26", "x27", "x28", "x29", "x30", "x31"
    };
    
    public static final String[] GP_NAMES_NIOS_ABI =
        {
            "zero ", "ra ", "sp ", "gp ", "tp ", "t0 ", "t1 ", "t2 ", "s0/fp ", "s1 ", "a0", "a1", "a2", "a3", "a4", "a5",
            "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
        };
    
    public static final String[] GP_NAMES_NIOS_DESCRIPTION =
        {
            "Hard-wired zero", "Return address", "Stack pointer", "Global pointer", "Thread pointer", "Temporaries", "Temporaries", "Temporaries", "Saved register/frame pointer", 
            "Saved register ", "Function arguments/return values", "Function arguments/return values", "Function arguments", "Function arguments", "Function arguments", "Function arguments",
            "Function arguments", "Function arguments", "Saved registers", "Saved registers", "Saved registers", "Saved registers", "Saved registers", "Saved registers", "Saved registers", "Saved registers", "Saved registers", "Saved registers",
            "Temporaries", "Temporaries", "Temporaries", "Temporaries"
        };

//   public static String assemblerPath = "/home/nanjing/Documents/lab2/nios2-elf-as";

    public static String assemblerPath = "external_bin/lnx-nios2-elf-as";

//   public static String assemblerPath = "/local/altera/14.0/nios2eds/bin/gnu/H-x86_64-pc-linux-gnu/bin/nios2-elf-as";
   // public static String assemblerPath = "c:\\altera\\12.1sp1\\nios2eds\\bin\\gnu\\H-i686-mingw32\\bin\\nios2-elf-as";

    public static int max_cycles = Preference.pref.getInt(Preference.maxCyclesPreferenceKey, 10000);
    
    public static int execute_stage = Preference.pref.getInt(Preference.numberExecuteStage, 3);

    public static int memory_stage = Preference.pref.getInt(Preference.numberMemoryStage, 2);
    
    public static String cache_type = Preference.pref.get(Preference.cacheType, "lineCache");

    
    public static void registerArchitectureConfig(Properties config)
    {
        ArchCfg.use_forwarding = getUseForwardingCfg(config);
        ArchCfg.use_load_stall_bubble = getUseLoadStallBubble(config);
    }


    private static boolean getUseForwardingCfg(Properties config)
    {
            return (((config.getProperty("use_forwarding")).toLowerCase()).equals("true"))
                    || ((config.getProperty("use_forwarding")).equals("1"));
    }

    private static boolean getUseLoadStallBubble(Properties config)
    {
        return (((config.getProperty("use_load_stall_bubble")).toLowerCase()).equals("true"))
                    || ((config.getProperty("use_load_stall_bubble")).equals("1"));
    }

    public static String getRegisterDescription(int reg_id)
    {
        return GP_NAMES_NIOS[reg_id];
    }
    
    public static String getRegisterABI(int reg_id)
    {
        return GP_NAMES_NIOS_ABI[reg_id];
    }
    
    public static String getRegisterDescv(int reg_id)
    {
        return GP_NAMES_NIOS_DESCRIPTION[reg_id];
    }

    public static int getRegisterCount()
    {

        return 32;
    }

}
