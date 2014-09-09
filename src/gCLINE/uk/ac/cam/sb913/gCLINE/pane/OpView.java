package gCLINE.uk.ac.cam.sb913.gCLINE.pane;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.PopUpMenu;
import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.OperationInfo;

/**
 * Look at the operation tree.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public abstract class OpView{
	
	private static Logger logger = Logger.getLogger(OpView.class);
	/**
	 * JTree to show the Record
	 */
	protected JTree myTree;
	/**
	 * Record that we are looking at
	 */
	private Record data;
	
	private StartFrame frame;
	
	private FileView fileViewer;
	
	/**
	 * Listen for a tree selection that will activate the popup menu
	 * to manipulate the project annotation and open in/out files.
	 */
	private MouseListener treeEvents = new MouseListener() {
		public void mousePressed(MouseEvent e) {
			checkEvent(e);
		}
		public void mouseReleased(MouseEvent e) {
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
				
				//check validity of the selection
				if (OpView.this.myTree.getSelectionModel()
						.getSelectionPaths() != null) {
					//get the file if it is selected
					String [] filename = new String [1];
					if(OpView.this.myTree
							.getSelectionPaths()[0].getPathCount() >= 4){
						filename[0] = OpView.this.myTree.getSelectionPaths()[0].getPath()[3].toString();
					} else {
						filename = null;
					}
					//get the operation if it is selected
					String [] opname = new String [1];
					if(OpView.this.myTree
							.getSelectionPaths()[0].getPathCount() >= 2){
						String temp = OpView.this.myTree.getSelectionPaths()[0].getPath()[1].toString();
						temp = temp.split(":")[0];
						opname[0] = temp;
					} else {
						opname = null;
					}
					//show the popup
					createPopUp(frame, data, filename, opname).show(e.getComponent(), e.getX(), e.getY());
				} 
			}else {
				logger.info("[treeEvents.checkEvent(MouseEvent)] event not triggered");
			}
		}
	};
	
	/**
	 * Move the selected node up the tree.
	 */
	private Action swapOpDown = new AbstractAction(){
		public void actionPerformed(ActionEvent e) {
			//get the current selected path
			TreePath current = myTree.getSelectionPath();
			//get the current row
			int currentrow =  myTree.getSelectionRows()[0];
			//get the current operation node
			DefaultMutableTreeNode currentOp = 
				(DefaultMutableTreeNode)((current.getPath())[1]);
			//get the following operation node
			DefaultMutableTreeNode next = currentOp.getNextSibling();
			//check to see if you are at the end of the tree
			if(next != null){
				//swap the operation
				((DefaultMutableTreeNode)next.getParent()).insert(currentOp, currentrow+1);
				//let the tree model know that we moved nodes around
				//...so that the changes show on the GUI
				data.reload();
				//select the node we just moved
				myTree.addSelectionRow(currentrow+1);
			}
		}
	};
	
	/**
	 * Move the selected node down the tree.
	 */
	private Action swapOpUp = new AbstractAction(){
		public void actionPerformed(ActionEvent e) {
			//get the current selected path
			TreePath current = myTree.getSelectionPath();
			//get the current row
			int currentrow = myTree.getSelectionRows()[0];
			//get the current operation node
			DefaultMutableTreeNode currentOp = 
				(DefaultMutableTreeNode)((current.getPath())[1]);
			//get the previous operation node
			DefaultMutableTreeNode previous = currentOp.getPreviousSibling();
			//check to see if we are in the start of the tree
			if(previous != null){
				//swap the operatoin
				((DefaultMutableTreeNode)previous.getParent()).insert(currentOp, currentrow-1);
				//let the tree model know that we moved nodes around
				//...so that the changes show on the GUI
				data.reload();
				//select the node we just moved
				myTree.setSelectionRow(currentrow-1);
			}
		}
	};
	
	protected abstract PopUpMenu createPopUp(
			StartFrame mf, Record d,
			String [] givenFiles, String [] givenOp);
	
	/**
	 * Create an operation view
	 * @param d The Record that we want to look at
	 */
	public OpView(Record d, FileView f){
		super();
		//initialise
		data = d;
		frame = data.frame;
		fileViewer = f;
		myTree = new JTree(data);
		myTree.setEditable(true);
		
		myTree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				if(myTree.getSelectionCount() == 0)
					return;
				File logFile = ((OperationInfo)(myTree.getSelectionPath().getPath()[1]))
					.getLog(data.getLocalFolder(), data.getLogExt());
				if(logFile == null){
					fileViewer.viewFile("");
					logger.info("[OpView(...)] no log file found to read");
				}else{
					fileViewer.viewFile(logFile.toString());
					logger.info("[OpView(...)] reading: "+logFile.getName());
				}
			}
		});
		//we don't actually want to see the root
		//...which is the local project name
		myTree.setRootVisible(false);
		myTree.setShowsRootHandles(true);
		myTree.setDragEnabled(true);
//		listen for popup events on the tree
		myTree.addMouseListener(treeEvents);
		
		////////////////////////////////////////////////
		//***Move the operations up and down the tree***
		////////////////////////////////////////////////
		myTree.getInputMap().put(KeyStroke.getKeyStroke
				("control 1"), "move up");
		myTree.getActionMap().put("move up", swapOpUp);
		myTree.getInputMap().put(KeyStroke.getKeyStroke
				("control 2"), "move down");
		myTree.getActionMap().put("move down", swapOpDown);
		myTree.setTransferHandler(new FileTransferHandler());
		myTree.setDragEnabled(true);
	}
	
	/**
	 * Wrap this view up in a JScrollPane so that
	 * we can add it to a frame.
	 * @return A JScrollPane that contains the operation
	 * tree.
	 */
	public JScrollPane toJScrollPane(){
		JScrollPane ans = new JScrollPane(myTree);
		ans.setBorder(new TitledBorder("Operations Viewer"));
		return ans;
	}
}
