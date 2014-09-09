package gCLINE.uk.ac.cam.sb913.gCLINE.pane;

import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.PopUpMenu;
import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;

/**
 * Create a folder viewer that can generate a
 * JScrollPane.
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public abstract class FolderView{
	private static Logger logger = Logger.getLogger(FolderView.class);
	
	public static String FILE_DESCRIPT_SEPERATOR = ":";
	/**
	 * A Record to access the files in the home
	 * directory and global descriptions
	 */
	private Record data;
	private StartFrame frame;
	/**
	 * A DefaultList Model that we can modify 
	 * dynamically if there is something to change
	 */
	//private DefaultListModel listModel;
	
	private JList list;
	
	private String getFilename(String fileDescript){
		String [] temp = fileDescript.split(":");
		return temp[0];
	}
	/**
	 * Update the folder view to reflect changes in
	 * the file/file descriptions.
	 */
	public class UpdateJList implements Runnable{
		boolean force = false;
		public UpdateJList(boolean given_force){
			force = given_force;
		}
		public void run() {
			logger.info("[run()@UpdateJList] Entering");

			//grab the file list
			ArrayList <String> fileList = data.getHomeFiles();
			DefaultListModel listModel = (DefaultListModel) list.getModel();
			
			if(force || fileList.size() != listModel.size()){
			
				logger.info("[run()@UpdateJList] removing elements");
				//clear out the old elements
				listModel.clear();
				logger.info("[run()@UpdateJList] adding "+ fileList.size() +" new files" );
				logger.info("[run()@UpdateJList] checking the size of the listModel " + listModel.getSize());
				
				//populate the list
				for(String filename: fileList){
					//get the description
					String description = data.getGlobalNote(filename);
					//if the description is there!
					//logger.info("[update()] adding " +filename + ": " + description);
					if(description != null && !description.matches("^\\s*$"))
						listModel.addElement(filename + FILE_DESCRIPT_SEPERATOR + " " + description);
					else
						listModel.addElement(filename);
				}
				logger.info("[run()@UpdateJList] checking the size of the listModel " + listModel.getSize());
				
			}
			
			logger.info("[run()@UpdateJList] ...exiting");
		}
		
	}
	
	/**
	 * Create a folder viewer.
	 * @param d The Record that this viewer reflects.
	 */
	public FolderView(StartFrame f, Record d){
		super();
		//initalize stuff
		data = d;
		frame = f;
		list = new JList(new DefaultListModel());
		//populate this
		EventQueue.invokeLater(new UpdateJList(false));
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addMouseListener(launchPopup);
		list.setDragEnabled(true);
	}
	/**
	 * Create a JScrollPane that holds the
	 * list.
	 * @return JScrollPane that views the list
	 */
	public JScrollPane toJScrollPane(){
		JScrollPane ans = new JScrollPane(list);
		if(data != null)
			ans.setBorder(new TitledBorder("Folder viewer: " + FileInfo.fileName(data.getHomeFolder())));
		else
			ans.setBorder(new TitledBorder("Folder viewer"));
		return ans; 
	}
	/**
	 * Listen for a tree selection that will activate the popup menu
	 * to manipulate the project annotation and open in/out files.
	 */
	private MouseListener launchPopup = new MouseListener() {
		public void mousePressed(MouseEvent e) {
			logger.info("[treeEvent.checkEvent(MouseEvent)] mousePressed!");
			checkEvent(e);
		}
		public void mouseReleased(MouseEvent e) {
			logger.info("[treeEvent.checkEvent(MouseEvent)] mousePressed!");
			checkEvent(e);}
		public void mouseClicked(MouseEvent e) {return;}
		public void mouseEntered(MouseEvent e) {return;}
		public void mouseExited(MouseEvent e) {return;}
		/**
		 * If this is a popup event then create a popup menu.
		 * @param e the mouse event that triggered the tree selection
		 */
		private void checkEvent(MouseEvent e) {
			logger.info("[treeEvents.checkEvent(MouseEvent)] entering");
			//check for popup trigger
			if (e.isPopupTrigger()) {
				logger.info("[treeEvents.checkEvent(MouseEvent)] PopUp triggered");
				Object [] temp = FolderView.this.list.getSelectedValues();
				String [] selectFiles = new String [temp.length];
				for(int i = 0; i < temp.length; i ++){
					selectFiles[i] = getFilename(temp[i].toString());
				}
				//show the popup
				createPopUp(frame, data, selectFiles, null).show(e.getComponent(), e.getX(), e.getY());
			} else {
				logger.info("[treeEvents.checkEvent(MouseEvent)] event not triggered");
				
			}
		}
	};
    
	protected abstract PopUpMenu createPopUp(
			StartFrame mf, Record d,
			String [] givenFiles, String [] givenOp);
	
}
