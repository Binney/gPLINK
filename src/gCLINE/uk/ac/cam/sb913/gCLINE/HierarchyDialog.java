package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.util.HashSet;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.OperationInfo;

/**
 * 
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class HierarchyDialog extends JDialog {

	/**
	 * Define a static logger variable so that it references the
	 */
	private static Logger logger = Logger.getLogger(HierarchyDialog.class);
	
	JTree ancestors;
	JTree descendants;
	Record data;
	
	String opName;
	String fileName;
	
	private String myArrayString(String [] info){
		String ans = "<html> ";
		
		if(info.length > 0)
			ans = ans + info[0];
		
		for(int i =1; i < info.length; i++){
			ans = ans + " <br> " + info[i];
		}
		ans = ans + " </html>";
		//System.out.println(ans);
		return ans;
	}
	
	private void createTree(){
		//create the top node for both trees and fill
		//...them with logical nonsence
		DefaultMutableTreeNode childTop = new DefaultMutableTreeNode("");
		DefaultMutableTreeNode parentTop = new DefaultMutableTreeNode("");
		//fill either as an operation or file depending
		//...on what is not null
		if(opName != null){
			//set the operation names in the trees
			childTop = new DefaultMutableTreeNode(opName);
			parentTop = new DefaultMutableTreeNode(opName);
			//populate the trees
			childrenOp(opName, childTop);
			parentOp(opName, parentTop);
		} else if(fileName != null){
			childTop = new DefaultMutableTreeNode(fileName);
			parentTop = new DefaultMutableTreeNode(fileName);
			childrenFile(new String [] {fileName}, childTop);
			parentFile(new String [] {fileName}, parentTop);
		}
		//set the trees to their top nodes
		ancestors = new JTree(parentTop);
		descendants = new JTree(childTop);
	}
	
	private String [] getOutFiles(String opName){
		
		ArrayList<FileInfo> temp = data.getOp(opName).getOutputFiles();
		String [] ans = new String [temp.size()];
		for(int i = 0; i < ans.length; i++){
			ans[i] = temp.get(i).toString();
		}
		return ans;
	}
	
	private String [] getInFiles(String opName){
		ArrayList<FileInfo> temp = data.getOp(opName).getInputFiles();
		String [] ans = new String [temp.size()];
		for(int i = 0; i < ans.length; i++){
			ans[i] = temp.get(i).toString();
		}
		return ans;
	}
	
	private void childrenOp(String op, DefaultMutableTreeNode parent){
		String [] outfiles = getOutFiles(op);
		
		DefaultMutableTreeNode temp = new DefaultMutableTreeNode(myArrayString(outfiles));
		childrenFile(outfiles, temp);
		parent.add(temp);
		
	}
	
	private void parentOp(String op, DefaultMutableTreeNode parent){
		String [] outfiles = getInFiles(op);
		DefaultMutableTreeNode temp = new DefaultMutableTreeNode(myArrayString(outfiles));
		parentFile(outfiles, temp);
		parent.add(temp);
		
		
		
	}
	
	
	private String [] getChildOp(String file){
		ArrayList <OperationInfo> allOp = data.getAllOp();
		HashSet <String> ansHash = new HashSet<String>();
		
		for(OperationInfo op: allOp){
			ArrayList<FileInfo> infiles = op.getInputFiles();
			for(FileInfo temp:infiles){
				if((temp.toString()).equals(file)){
					ansHash.add(op.getName());
					break;
				}
			}
		}
		String [] ans = new String [ansHash.size()];
		int i =0;
		for(String name: ansHash){
			ans[i] = name;
			i++;
		}
		
		return ans;
	}
	
	
	private String [] getParentOp(String file){
		ArrayList <OperationInfo> allOp = data.getAllOp();
		HashSet <String> ansHash = new HashSet<String>();
		
		for(OperationInfo op: allOp){
			ArrayList<FileInfo> outfiles = op.getOutputFiles();
			for(FileInfo temp:outfiles){
				if((temp.toString()).equals(file)){
					ansHash.add(op.getName());
					break;
				}
			}
		}
		String [] ans = new String [ansHash.size()];
		int i =0;
		for(String name: ansHash){
			ans[i] = name;
			i++;
		}
		
		return ans;
	}
	
	private void childrenFile(String [] files, DefaultMutableTreeNode parent){

		HashSet <String> allOp = new HashSet <String>();
		for(int i = 0; i < files.length; i ++){
			String [] childOp = getChildOp(files[i]);
			for(int j = 0; j < childOp.length; j++){
				allOp.add(childOp[j]);
			}
		}
		if(allOp == null
				|| allOp.size() == 0)
			return;
		
		for(String opName: allOp){
			DefaultMutableTreeNode temp = new DefaultMutableTreeNode(opName);
			childrenOp(opName, temp);
			parent.add(temp);
			
		}
	}
	private void parentFile(String [] files, DefaultMutableTreeNode parent){

		HashSet <String> allOp = new HashSet <String>();
		for(int i = 0; i < files.length; i ++){
			String [] parentOp = getParentOp(files[i]);
			for(int j = 0; j < parentOp.length; j++){
				allOp.add(parentOp[j]);
			}
		}
		if(allOp == null
				|| allOp.size() == 0)
			return;
		for(String opName: allOp){
			DefaultMutableTreeNode temp = new DefaultMutableTreeNode(opName);
			parentOp(opName, temp);
			parent.add(temp);
			
		}
	}
	
	/**
	 * Main constructor. Note that either the file or op string parameters is null.
	 * @param title a String that is the title of the dialog.
	 * @param mf a Mainframe that this dialog is attached to
	 * @param op a string identifying the operation that is the root
	 * @param file a string that identifies the file that is the root
	 */
	public HierarchyDialog(String title, StartFrame mf,
			Record d,
			String op, String file){
		//create the dialog and attach it to mf
		super(mf, title);
		logger.info("[HirarchyDialog(...)] starting constructor");
		//set the data structure we are working with
		data = d;
		//set the operation and file name, one of which
		//...is null
		opName = op;
		fileName = FileInfo.fileName(file);
		
		logger.info("[HirarchyDialog(...)] starting createTree()");
		//create the conent
		createTree();
		
		logger.info("[HirarchyDialog(...)] setting renderer");
		//set the row height to 0 so that it is
		//...computed for each row individually
		ancestors.setCellRenderer(new MyRenderer());
		ancestors.setRowHeight(0);
		descendants.setCellRenderer(new MyRenderer());
		descendants.setRowHeight(0);
		
		logger.info("[HirarchyDialog(...)] put it all together");
		//layout the ancestors and descendants in different tabs
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Ancestors", new JScrollPane(ancestors));
		tabs.add("Descendants", new JScrollPane(descendants));
		getContentPane().add(tabs);
		
		pack();
		setVisible(true);
	}
	
	class MyRenderer extends DefaultTreeCellRenderer {
	    //there are some undesirable side effects that
		//...we are avoiding with this. The direct leaves
		//...of selected nodes went white-on-white and 
		//...now there is just a box border instead
		// TODO ^ what?
	    public MyRenderer() {
	        super();
	        this.setBackgroundSelectionColor(this.getBackgroundNonSelectionColor());
	        this.setTextSelectionColor(this.getTextNonSelectionColor());
	       
	    }
	}
}
