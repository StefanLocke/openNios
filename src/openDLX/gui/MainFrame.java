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
package openDLX.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.undo.UndoManager;

import alternateSimulator.Simulator;
import openDLX.config.GlobalConfig;
import openDLX.gui.GUI_CONST.OpenDLXSimState;
import openDLX.gui.command.EventCommandLookUp;
import openDLX.gui.command.userLevel.CommandExitProgram;
import openDLX.gui.dialog.Input;
import openDLX.gui.dialog.Output;
import openDLX.gui.internalframes.RiscVFrame;
import openDLX.gui.internalframes.concreteframes.ClockCycleFrame;
import openDLX.gui.internalframes.concreteframes.CodeFrame;
import openDLX.gui.internalframes.concreteframes.LogFrame;
import openDLX.gui.internalframes.concreteframes.MemoryFrame;
import openDLX.gui.internalframes.concreteframes.RegisterFrame;
import openDLX.gui.internalframes.concreteframes.StatisticsFrame;
import openDLX.gui.internalframes.concreteframes.editor.EditorFrame;
import openDLX.gui.menubar.MainFrameMenuBarFactory;
import openDLX.gui.menubar.StateValidator;
import openDLX.gui.util.PipelineExceptionHandler;
import openDLX.util.TrapObservableDefault;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, ItemListener
{
    public static final int RUN_SPEED_DEFAULT = 16;

    // MainFrame is a Singleton.
    // hence it has a private constructor
    private static final MainFrame mf = new MainFrame();

    public Output output;
    public Input input;

    private Simulator simulator = null;
    private UndoManager undoMgr;
    private EditorFrame editor;
    private JPanel desktop;
    
    
    private JPanel EditorFrameContainer;
    private JPanel StatisticsFrameContainer;
    private JPanel CodeFrameContainer;
    private JPanel MemoryFrameContainer;
    private JPanel RegisterFrameContainer;
    private JPanel ClockCycleFrameContainer;
    private JPanel LogFrameContainer;
    
    private boolean updateAllowed = true;
    private int runSpeed = RUN_SPEED_DEFAULT;
    private boolean pause = false;
    private OpenDLXSimState state = OpenDLXSimState.IDLE;
    private File configFile;
    private JMenuBar menuBar;
    private JMenuItem forwardingMenuItem;
    private PipelineExceptionHandler pexHandler = null;
    private String loadedCodeFilePath="code.s";//default

    private MainFrame()
    {
        initialize();
        final ImageIcon icon = new ImageIcon(getClass().getResource("/img/openDLX-quadrat128x128.png"), "openDLX icon");
        setIconImage(icon.getImage());

        setTitle("OpenNios " + GlobalConfig.VERSION);
    }

    //thus it has a static access method
    public static MainFrame getInstance()
    {
        return mf;
    }

    //main frame delegates all incoming events caused by its submembers to command classes
    @Override
    public void actionPerformed(ActionEvent e)
    {
        EventCommandLookUp.get(e.getSource()).execute();
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        EventCommandLookUp.get(e.getSource()).execute();
    }

    private void initialize()
    {
        undoMgr = new UndoManager();
        configFile = new File("./config.cfg");
        //uses a factory to outsource creation of the menuBar
        MainFrameMenuBarFactory menuBarFactory = new MainFrameMenuBarFactory(this, this, this);
        Hashtable<String, JMenuItem> importantItems = new Hashtable<>();
        menuBar = menuBarFactory.createJMenuBar(importantItems);
        setJMenuBar(menuBar);
        forwardingMenuItem = importantItems.get(MainFrameMenuBarFactory.STRING_MENU_SIMULATOR_FORWARDING);

        setMinimumSize(new Dimension(200, 200));
        desktop = new JPanel();
        desktop.setLayout(new BoxLayout(desktop, BoxLayout.X_AXIS));
        desktop.setBackground(Color.WHITE);
        setContentPane(desktop);

        
	   EditorFrameContainer = new JPanel();
	   EditorFrameContainer.setBorder(new LineBorder(Color.black));
	   EditorFrameContainer.setLayout(new BoxLayout(EditorFrameContainer,BoxLayout.Y_AXIS));
	   
	   StatisticsFrameContainer = new JPanel();
	   StatisticsFrameContainer.setBorder(new LineBorder(Color.black));
	   StatisticsFrameContainer.setLayout(new BoxLayout(StatisticsFrameContainer,BoxLayout.Y_AXIS));
	   
	   CodeFrameContainer = new JPanel();
	   CodeFrameContainer.setBorder(new LineBorder(Color.black));
	   CodeFrameContainer.setLayout(new BoxLayout(CodeFrameContainer,BoxLayout.Y_AXIS));
	   
	   MemoryFrameContainer = new JPanel();
	   MemoryFrameContainer.setBorder(new LineBorder(Color.black));
	   MemoryFrameContainer.setLayout(new BoxLayout(MemoryFrameContainer,BoxLayout.Y_AXIS));
	   
	   RegisterFrameContainer = new JPanel();
	   RegisterFrameContainer.setBorder(new LineBorder(Color.black));
	   RegisterFrameContainer.setLayout(new BoxLayout(RegisterFrameContainer,BoxLayout.Y_AXIS));
	   
	   ClockCycleFrameContainer = new JPanel();
	   ClockCycleFrameContainer.setBorder(new LineBorder(Color.black));
	   ClockCycleFrameContainer.setLayout(new BoxLayout(ClockCycleFrameContainer,BoxLayout.Y_AXIS));
	   
	   LogFrameContainer = new JPanel();
	   LogFrameContainer.setBorder(new LineBorder(Color.black));
	   LogFrameContainer.setLayout(new BoxLayout(LogFrameContainer,BoxLayout.Y_AXIS));
	   
       JPanel leftSection = new JPanel();
       JPanel leftSectionLower = new JPanel();
       leftSectionLower.setLayout(new BoxLayout(leftSectionLower, BoxLayout.Y_AXIS));
       BoxLayout b = new BoxLayout(leftSectionLower, BoxLayout.Y_AXIS);
  
       leftSection.setLayout(new BoxLayout(leftSection, BoxLayout.Y_AXIS));
       leftSection.add(EditorFrameContainer);
       leftSection.add(leftSectionLower);
       leftSectionLower.add(StatisticsFrameContainer);
       leftSectionLower.add(LogFrameContainer);
       
       leftSection.setBackground(Color.RED);
       
       leftSectionLower.setBackground(Color.green);
       
       JPanel rightSection = new JPanel();
       JPanel rightSectionUpper = new JPanel();
       rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
       rightSectionUpper.setLayout(new BoxLayout(rightSectionUpper, BoxLayout.X_AXIS));
       rightSection.add(rightSectionUpper);
       rightSection.add(ClockCycleFrameContainer);
       rightSectionUpper.add(CodeFrameContainer);
       rightSectionUpper.add(RegisterFrameContainer);
       rightSectionUpper.add(MemoryFrameContainer);
       
       desktop.add(leftSection);
       desktop.add(rightSection);
       
       editor = EditorFrame.getInstance(this);
        editor.setUndoManager(undoMgr);
        EditorFrameContainer.add(editor);

        output = Output.getInstance(mf);
        input = Input.getInstance(mf);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(200, 200));
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);

       
       
        setOpenDLXSimState(OpenDLXSimState.IDLE);
        pexHandler = new PipelineExceptionHandler(this);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                MainFrame frame = (MainFrame)e.getSource();
                CommandExitProgram exit = new CommandExitProgram(frame);
                if (exit.close())
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

    }

    //INTERFACE
    public Simulator getSimulator()
    {
        return simulator;
    }

    public JPanel[] getinternalFrames()
    {
    	
    	JPanel[] panes = new JPanel[100];
    	panes[0] = (JPanel) EditorFrameContainer.getComponent(0);
    	panes[1] = (JPanel) StatisticsFrameContainer.getComponent(0);
    	panes[2] = (JPanel) CodeFrameContainer.getComponent(0);
    	panes[3] = (JPanel) MemoryFrameContainer.getComponent(0);
    	panes[4] = (JPanel) RegisterFrameContainer.getComponent(0);
    	panes[5] = (JPanel) ClockCycleFrameContainer.getComponent(0);
    	panes[6] = (JPanel) LogFrameContainer.getComponent(0);
  
        return panes;
    }

    public void setSimulator(Simulator openDLXSim)
    {
        this.simulator = openDLXSim;
        pexHandler.setSimulator(openDLXSim);
    }

    public void setOpenDLXSimState(OpenDLXSimState s)
    {
        this.state = s;
        StateValidator.validateMenu(menuBar, s);
        editor.validateButtons(getOpenDLXSimState());
    }

    public OpenDLXSimState getOpenDLXSimState()
    {
        return this.state;
    }

    public boolean isRunning()
    {
        return (state == OpenDLXSimState.RUNNING);
    }

    public boolean isExecuting()
    {
        return (state == OpenDLXSimState.EXECUTING);
    }

    public boolean isLazy()
    {
        return (state == OpenDLXSimState.IDLE);
    }

    public boolean isUpdateAllowed()
    {
        return updateAllowed;
    }

    public void setUpdateAllowed(boolean updateAllowed)
    {
        this.updateAllowed = updateAllowed;
    }

    public void addInternalFrame(RiscVFrame mif)
    {
        if (mif instanceof ClockCycleFrame) {
        	ClockCycleFrameContainer.add(mif);
        	return;
        }
        if (mif instanceof CodeFrame) {
        	CodeFrameContainer.add(mif);
        	return;
        }
        if (mif instanceof LogFrame) {
        	LogFrameContainer.add(mif);
        	return;
        }
        if (mif instanceof MemoryFrame) {
        	MemoryFrameContainer.add(mif);
        	return;
        }
        if (mif instanceof RegisterFrame) {
        	RegisterFrameContainer.add(mif);
        	return;
        }
        if (mif instanceof StatisticsFrame) {
        	StatisticsFrameContainer.add(mif);
        	return;
        }
    }
   

    public String getEditorText()
    {
        return editor.getText();
    }


    public EditorFrame getEditor()
    {
        return editor;
    }
    
    public void setEditorText(String text)
    {
        editor.setText(text);

    }

    public void colorEditorLine(int l)
    {
        editor.colorLine(l);
    }

    public void insertEditorText(String text)
    {
        editor.insertText(text);

    }

    public void setEditorSavedState()
    {
        editor.setSavedState();
    }

    public boolean isEditorTextSaved()
    {
        return editor.isTextSaved();
    }

    public void setRunSpeed(int speed)
    {
        this.runSpeed = speed;
    }

    public int getRunSpeed()
    {
        return runSpeed;
    }

//    public File getConfigFile()
//    {
//        return configFile;
//    }

    public void setConfigFile(File configFile)
    {
        this.configFile = configFile;
    }

    public boolean isPause()
    {
        return pause;
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;
    }

    public PipelineExceptionHandler getPipelineExceptionHandler()
    {
        return pexHandler;
    }

    public String getLoadedCodeFilePath()
    {
        return loadedCodeFilePath;
    }

    public void setLoadedCodeFilePath(String loadedCodeFilePath)
    {
        this.loadedCodeFilePath = loadedCodeFilePath;
    }

    public JMenuItem getForwardingMenuItem()
    {
        return forwardingMenuItem;
    }

    public UndoManager getEditorUndoManager() {
        return undoMgr;
    }

}
