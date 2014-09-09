package gCLINE.uk.ac.cam.sb913.gCLINE.tester;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import gCLINE.uk.ac.cam.sb913.gCLINE.PopUpMenu;
import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.AutoUpdater;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.Configure;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sshtools.j2ssh.SshClient;

@SuppressWarnings("serial")
public final class TestFrame extends StartFrame {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger 
		= Logger.getLogger(TestFrame.class);
	
	public myProject data;
	
	/**
	 * Create a windowListener that will properly
	 * close this class.
	 */
	{properClosing = new WindowListener(){

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowClosing(WindowEvent e) {
			logger.info("GPLINK is closing (2)");
			if(data != null)
				data.properClosing();
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	};
	;}
	
	public TestFrame() {
		super("Test gCLINE");
		//BasicConfigurator.configure();
		Logger.getLogger("com.sshtools").setLevel(Level.OFF);
		Logger.getLogger("gCLINE.uk.ac.cam.sb913.gCLINE").setLevel(Level.INFO);
		
		data = new myProject(this);
		layoutPanels(data);
		addWindowListener(properClosing);
	}


	class StartFrameConfig extends Configure{

		public StartFrameConfig(Record d) {
			super(d);
			getRootPane().add(createFormSpecific(), 1);
		}

		@Override
		public JPanel createFormSpecific() {
			JPanel ans = new JPanel();
			return ans;
		}

		@Override
		public void process() {
			data.setAltEditor(editor.getText());
			
			data.saveConfig();
			
		}
		
	}
	
	class myPopUpMenu extends PopUpMenu{

		public myPopUpMenu(StartFrame mf, Record d, String[] givenFiles, String[] givenOp) {
			super(mf, d, givenFiles, givenOp, "");
		}

		@Override
		protected ActionListener createExecute() {
			return null;
		}
		
	}
	
	class myOpView extends OpView{

		public myOpView(Record d, FileView f) {
			super(d, f);
		}

		@Override
		protected PopUpMenu createPopUp(StartFrame mf, Record d, String[] givenFiles, String[] givenOp) {
			return new myPopUpMenu(mf, d, givenFiles, givenOp);
		}
		
	}
	
	class myFolderView extends FolderView{

		public myFolderView(Record d) {
			super(TestFrame.this, d);
		}

		@Override
		protected PopUpMenu createPopUp(StartFrame mf, Record d, String[] givenFiles, String[] givenOp) {
			return new myPopUpMenu(mf, d, givenFiles, givenOp);
		}
		
	}
	
	class myProject extends Record{

		public myProject(TestFrame frame) {
			super(frame);
		}

		public myProject(TestFrame parent, String text, String text2, String text3, String text4, String text5, SshClient conn) {
			super(parent, text, text2, text3, text4, text5, conn);
		}

		public myProject(TestFrame parent, String localFolder) {
			super(parent, localFolder);
		}

		@Override
		public void setAutoUpdater(AutoUpdater given) {
			
		}

		@Override
		protected void saveRemoteConfig() {
			
		}

		@Override
		protected void loadRemoteConfig() {
			
		}
		
	}
	
	
	/**
	 * The main fuction that is where it all starts
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
	        UIManager.setLookAndFeel(
	        		//UIManager.getCrossPlatformLookAndFeelClassName()
	        		UIManager.getSystemLookAndFeelClassName()
	        		);
	    } catch (Exception e) { 
	    	System.err.println("Couldn't set look and feel, using default: MainFrame.java");
	    }
	    
		//String testfile = "C:\\Documents and Settings\\Kathe\\My Documents\\gCLINETest"; 
	
		TestFrame mw = new TestFrame();
		
		//if(mw.data.removeOperation("testOp1"))
		//	System.out.println("Sucessfully removed!");
		//mw.opViewer.updateTree();
		mw.setVisible(true);
		mw.pack();
	}


	@Override
	protected void setVersion() {
		super.version = "0.001";
		
	}

	public void layoutPanels(myProject d){
		//set the old parameters to null to make
		//...sure that we don't get any memory leaks
		data = null;
		fileViewer = null;
		folderViewer = null;
		opViewer = null;
		
		//reset the data
		data = d;
		fileViewer = new FileView();
		folderViewer = new myFolderView(data);
		opViewer = new myOpView(data, fileViewer);
		
		//place everything in split panels to get
		//...things organized
		JSplitPane splitPaneHorz = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                opViewer.toJScrollPane(),
				fileViewer);
		JSplitPane splitPaneVert = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				folderViewer.toJScrollPane(),
				splitPaneHorz);
		//remove everything
		getContentPane().removeAll();
		//add the split pane
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPaneVert, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.PAGE_END);
		//set up the menu bar
		setJMenuBar(new MenuBarMain(this, new StartFrameConfig(data)));
		//pack everything up
		pack();
	}

	@Override
	public void layoutPanels() {
		layoutPanels(new myProject(this));
	}


	@Override
	public void setBrowseOnly(boolean state) {
		
	}

}
