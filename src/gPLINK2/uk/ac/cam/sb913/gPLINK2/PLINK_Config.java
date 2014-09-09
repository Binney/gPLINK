package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.general.Configure;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;

/**
 * 
 * A Configure dialog that is specific to PLINK.
 * Specifically this allows the user to set:
 * PLINK exicutable path name, PLINK prefix,
 * Haploview jar path name, and Haploview append command.
 * 
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class PLINK_Config extends Configure {
	/**
	 * A JTextField for the plink path.
	 */
	private JTextField plinkPath;
	/**
	 * A JTextField for the plink prefix command.
	 */
	private JTextField plinkPrefix;
	/**
	 * A JTextField for the haploview jar path.
	 */
	private JTextField haploPath;
	/**
	 * A JTextField for the haploview append command.
	 */
	private JTextField haploAppend;
	/**
	 * The project we are configuring.
	 */
	private Project data;
	private GPLINK frame;
	/**
	 * Create an instance of the configuration dialog.
	 * @param f The GPLINK instance we are working with.
	 */
	public PLINK_Config(GPLINK f) {
		super(f.data);
		//initalize things
		data = f.data;
		frame = f;
		plinkPath = new JTextField(data.getPlinkPath(),30);
		plinkPrefix = new JTextField(data.getPlinkPrefix(), 10);
		haploPath = new JTextField(data.getHaploPath(), 30);
		haploAppend = new JTextField(data.getHaploAppend(), 10);
		//add the PLINK config stuff
		getRootPane().add(createFormSpecific(), 1);
	}
	/**
	 * Layout the PLINK configuration fields.
	 * @return A panel that is then added to the
	 * configuration dialog that contains the PLINK
	 * specific configuration files.
	 */
	@Override
	protected JPanel createFormSpecific() {
		JPanel ans = new JPanel();
		ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		ans.add(createHaplo());
		ans.add(createPlink());
		return ans;
	}
	/**
	 * Layout the haploview configuration field.
	 * @return A panel that contains the haploview fields.
	 */
	private JPanel createHaplo(){
		JPanel ans = new JPanel();
		ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		ans.setBorder(new TitledBorder("Haploview settings"));
		
		JPanel temp1 = new JPanel();
		temp1.add(new JLabel("Haploview path:"));
		temp1.add(haploPath);
		temp1.add(new BrowseButton(haploPath, true));
		
		JPanel temp2 = new JPanel();
		temp2.add(new JLabel("Haploview append:"));
		temp2.add(haploAppend);
		
		ans.add(temp1);
		ans.add(temp2);
		return ans;
	}
	
	/**
	 * Layout the plink configuration fields.
	 * @return A panel that contains the plink fields.
	 */
	private JPanel createPlink(){
		JPanel ans = new JPanel();
		ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		ans.setBorder(new TitledBorder("PLINK settings"));
		
		JPanel temp1 = new JPanel();
		temp1.add(new JLabel("PLINK path:"));
		temp1.add(plinkPath);
		temp1.add(new BrowseButton(plinkPath));
		
		JPanel temp2 = new JPanel();
		temp2.add(new JLabel("PLINK prefix:"));
		temp2.add(plinkPrefix);
		
		ans.add(temp2);
		ans.add(temp1);
		return ans;
	}
	

	/**
	 * Set the fields in the project to reflect
	 * their corrisponding fields in this dialog.
	 */
	@Override
	public void process() {
		//if the timing for the auto-updater changes
		//...then reset the autoupdater
		if(data.isRemote()){
			if(!new Integer(data.getRemoteUpdateSec()).toString().equals(updateField.getText())){
				data.setRemoteUpdateSec(new Integer(updateField.getText()));
			
				frame.layoutPanels(data);
			}
		}else{
			if(!new Integer(data.getLocalUpdateSec()).toString().equals(updateField.getText())){
				data.setLocalUpdateSec(new Integer(updateField.getText()));
			
				frame.layoutPanels(data);
			}
		}
		data.setAltEditor(editor.getText());
		data.setPlinkPath(plinkPath.getText());
		data.setPlinkPrefix(plinkPrefix.getText());
		data.setHaploAppend(haploAppend.getText());
		data.setHaploPath(haploPath.getText());
		data.saveConfig();
	}
	
	/**
	 * A subclass that creates a browse button
	 * that passes the file name to a target 
	 * JTextField.
	 * @author Kathe Todd-Brown
	 *
	 */
	private class BrowseButton extends JButton{
		/**
		 * A JTextField where we want the file stored.
		 */
		private JTextField target;
		/**
		 * A GCFileChooser we use to pick the file.
		 */
		private GCFileChooser pick;
		
		private boolean flagLocal;
		/**
		 * Create a browse button.
		 * @param givenTarget A JTextField we want the
		 * file name to go to.
		 */
		BrowseButton(JTextField givenTarget){
			super("Browse");
			//set everyting up
			target = givenTarget;
			//add the action listener
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					pick = new GCFileChooser(
							PLINK_Config.this, 
							null, 
							!data.isRemote(), 
							false,
							data.getConn(), 
							data.getHomeFolder());
					pick.addFileChoosenListener(new FileChoosenListener(){
						public void fileChoosenOccures(FileChoosenEvent evt) {
							target.setText(pick.fileName);
						}});
					pick.showChooser();
				}
			});
		}
		BrowseButton(JTextField givenTarget, boolean local){
			super("Browse");
			//set everyting up
			target = givenTarget;
			flagLocal = local;
			//add the action listener
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					pick = new GCFileChooser(
							PLINK_Config.this, 
							null, 
							flagLocal, 
							false,
							null, 
							data.getHomeFolder());
					pick.addFileChoosenListener(new FileChoosenListener(){
						public void fileChoosenOccures(FileChoosenEvent evt) {
							target.setText(pick.fileName);
						}});
					pick.showChooser();
				}
			});
		}
	}
	
}
