package gCLINE.uk.ac.cam.sb913.gCLINE.data.infos;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.KeyWords;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * A class that contains file information.
 * <p> This includes the file name, global and local
 * descriptions that pertain to that file. Note that
 * this is attached to a OperationInfo which dictates
 * what is "local" to this FileInfo.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class FileInfo extends DefaultMutableTreeNode 
					implements KeyWords {
	
	/**
	 * A string that holds the absolute file name.
	 */
	private String filename;
	/**
	 * A string that holds the global description of
	 * this file.
	 */
	private String globalDesc;
	/**
	 * A string that holds an operation specific 
	 * description of this file.
	 */
	private String localDesc;
	
	/**
	 * Set the local description for this file.
	 * @param newDesc A String with the new description.
	 */
	public void setLocalDesc(String newDesc){
		localDesc = newDesc;
		this.removeAllChildren();
		if(!(globalDesc.equals("") && localDesc.equals("")))
			this.add(new DefaultMutableTreeNode(globalDesc+"; " + localDesc));
	}
	
	/**
	 * Get the local description for the file.
	 * @return A String containing the local file
	 * description.
	 */
	public String getLocalDesc(){
		return localDesc;
	}
	
	/**
	 * Set the global description for this file.
	 * @param newDesc A string with the new description.
	 */
	public void setGlobalDesc(String newDesc){
		globalDesc = newDesc;
		this.removeAllChildren();
		if(!(globalDesc.equals("") && localDesc.equals("")))
			this.add(new DefaultMutableTreeNode(globalDesc+"; " + localDesc));
	}
	
	/**
	 * Get the global description for the file.
	 * @return A String containing the global file 
	 * description.
	 */
	public String getGlobalDesc(){
		return globalDesc;
	}
	
	/**
	 * fileName takes a full file name seperated by either \ or / and return
	 * the name and extention of that full name
	 * @param file a full file name
	 * @return the file name striped of the directory info
	 */
	public static String fileName(String file){
		if(file == null)
			return null;
		
		String [] temp = null;
		String tfile = file;
		
		if(tfile.endsWith("\"")){
			tfile = tfile.substring(0, tfile.length()-1);
		}
		
		if(tfile.contains("/")){
			temp = tfile.split("/");
		}
		else if(tfile.contains("\\")){
			temp = tfile.split("\\\\");
			
		} else {
			return tfile;
		}
		return temp[temp.length -1];
	}
	
	
	public static String quote(String given){
		String ans = given;
		if(given.matches(".*\\s+.*") 
				&& !(given.startsWith("\"") 
						&& given.endsWith("\""))){
			ans = "\"" + ans + "\"";
		}
		return ans;
	}
	/**
	 * Construct a FileInfo from direct strings.
	 * @param name a string holding the absolute file name
	 * @param global a string of the global description
	 * @param local a string of the local operation specific 
	 * description
	 */
	public FileInfo(String name, 
			String local, String global){
		filename = fileName(name);
		if(global != null)
			globalDesc = global;
		else
			globalDesc = "";
		
		if(local != null)
			localDesc = local;
		else
			localDesc = "";
		
		if(!(globalDesc.equals("") && localDesc.equals("")))
			super.add(new DefaultMutableTreeNode(global+"; " + local));
	}
	
	/**
	 * Create an empty FileInfo where the file name, local
	 * and global description all set to "".
	 *
	 */
	public FileInfo(){
		this("","","");
	}
	
	/**
	 * Get the full description with both the global
	 * and local description.
	 * @return A String that has the global and local
	 * file description seperated by a ";".
	 */
	public String getdescription(){
		return globalDesc + "; " + localDesc;
	}
	
	/**
	 * Convert the information in the FileInfo to an 
	 * Element.
	 * @param d A Document to generate the element from.
	 * @return An Element that represents the information
	 * in the FileInfo
	 */
	public Element asElement(Document d){
		Element file = d.createElement(FILE_KEY);
		file.setAttribute(NAME_KEY, filename);
		file.setTextContent(getLocalDesc());
		return file;
	}
	
	/**
	 * Show the file name as the string representation of
	 * this class.
	 * @return A String that is the file name. Note there
	 * is no description in this.
	 */
	@Override
	public String toString(){
		String ans = filename;
		
		return ans;
	}
	
	public static void main(String[] args) {
		//test the quotes
		String tester = "something with spaces";
		System.out.println(tester);
		System.out.println(FileInfo.quote(tester));
		String tester2 = "something_wihtout_spaces";
		System.out.println(tester2);
		System.out.println(FileInfo.quote(tester2));
	}
	
}
