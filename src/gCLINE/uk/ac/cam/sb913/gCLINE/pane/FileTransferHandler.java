package gCLINE.uk.ac.cam.sb913.gCLINE.pane;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.KeyWords;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.CalculationInfo;

@SuppressWarnings("serial")
public class FileTransferHandler extends TransferHandler {
	
	/**
	 * A logger for this class
	 */
	private Logger logger = Logger.getLogger(FileTransferHandler.class);
	
	private void importString(JComponent c, String str){
		//check that the class is a jtree
		if(!c.getClass().equals(JTree.class))
			return;
		//cast the target formally as a jtree
		JTree target = (JTree)c;
		
		//figure out where we are dropping the files
		String opName = null;
		if(target.getSelectionPath().getPathCount() >= 2){
			String temp1 = target.getSelectionPath().getPath()[1].toString();
			opName = temp1.split(":")[0];
		}
		String fileType = KeyWords.INFILE_KEY;
		if(target.getSelectionPath().getPathCount() >= 2
				&& target.getSelectionPath().getPath()[2].toString().equals(CalculationInfo.OUTPUT_LABEL)){
			fileType = KeyWords.OUTFILE_KEY;
		}
		if(opName == null)
			return;
		
		//split up each line
		String[] files = str.split("\\n");
		
		for(String fileStr: files){
			
			String filename = null;
			String description = null;
			String[] temp = fileStr.split(FolderView.FILE_DESCRIPT_SEPERATOR, 2);
		
			filename = temp[0];
			if(temp.length == 2){
				description = temp[1];
			}
		
			((Record)target.getModel()).getOp(opName).addFile(fileType, filename, null, description);
			logger.info("[importString(JComponent, String)] " + filename +" is added to " + opName);
		}
	}
	
	 public boolean canImport(JComponent c, DataFlavor[] flavors) {
		 for (int i = 0; i < flavors.length; i++) {
			 if (DataFlavor.stringFlavor.equals(flavors[i])) {
				 return true;
			 }	
		 }
		 return false;
	 }
	 
	 public boolean importData(JComponent c, Transferable t) {
	        if (canImport(c, t.getTransferDataFlavors())) {
	            try {
	                String str = (String)t.getTransferData(DataFlavor.stringFlavor);
	                importString(c, str);
	                return true;
	            } catch (UnsupportedFlavorException ufe) {
	            } catch (IOException ioe) {
	            }
	        }

	        return false;
	    }
	    
}
