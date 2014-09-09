package gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.GPLINK;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form.BrowseButton;

/**
 * A Tabbed Pane subclass that contains the 
 * binary, standard and alternative phenotype
 * options. This is designed to be included in a
 * a form.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public final class InputPane extends JTabbedPane {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(InputPane.class);
	/**
	 * The GPLINK istance this is attached to.
	 */
	private GPLINK parent;
	
	/**
	 * The dialog that we want to attach our file
	 * name browsing window to.
	 */
	private Form parentForm;
	public boolean validInput = false;
	private static float my_alignment = Component.RIGHT_ALIGNMENT;
	////////
	//binary input components
	////////
	/**
	 * A JCheckBox that flags a quick binary input file
	 * format (--bfile filename).
	 */
	private JCheckBox binCheck;
	/**
	 * A JComboBox that holds the quick binary file roots
	 */
	private JComboBox binShort;
	/**
	 * A JTextField that holds the bed file name
	 */
	private JTextField bedField;
	/**
	 * A BrowseButton that selects the bed file
	 */
	private BrowseButton bedBrowse;
	/**
	 * A JTextField that holds the bim file name.
	 */
	private JTextField bimField;
	/**
	 * A BrowseButton thta selects the bim file
	 */
	private BrowseButton bimBrowse;
	/**
	 * A JTextField that holds the fam file name.
	 */
	private JTextField famField;
	/**
	 * A BrowseButton that selects the fam file.
	 */
	private BrowseButton famBrowse;
	
	////////
	//standard input components
	////////
	/**
	 * A JCheckBox that flags the short standard input
	 * format (--file fileroot)
	 */
	private JCheckBox stdCheck;
	/**
	 * A JComboBox that holds the file root options
	 * for the short standard input format
	 */
	private JComboBox stdShort;
	/**
	 * A JTextField that holds the ped file name
	 */
	private JTextField pedField;
	/**
	 * A BrowseButton that selects the ped file.
	 */
	private BrowseButton pedBrowse;
	/**
	 * A JTextField that holds the map file name.
	 */
	private JTextField mapField;
	/**
	 * A BrowseButton that selects the map file.
	 */
	private BrowseButton mapBrowse;
	
	////////
	//alternative pheno components
	////////
	/**
	 * A JCheckBox that flags an alternative phenotype
	 */
	private JCheckBox useAlt;
	/**
	 * A JTextField that holds the alternative pheotype
	 * file name.
	 */
	private JTextField altField;
	/**
	 * A BrowseButton that selects the alternative pheotype
	 * file name.
	 */
	private BrowseButton altBrowse;
	/**
	 * A JCheckBox that adds a column option to the alternative
	 * pheontype
	 */
	private JCheckBox useColm;
	/**
	 * A JTextField that contains the column you wish
	 * to use on the alternative pheotype file.
	 */
	private JTextField mphenoText;
	
	/**
	 * Create a JPanel that contains a lable, textfield
	 * and browse button. This allows us to bundle these
	 * more effectively
	 * @param label A JLabel that describes this panel.
	 * @param field A JTextField that holds the file name.
	 * @param browse A BrowseButton that selectes the file name.
	 * @return A JPanel that contains the given label, field
	 * and browse button.
	 */
	private JPanel bundle(JLabel label, JTextField field,
			BrowseButton browse){
		JPanel ans = new JPanel();
		//ans.setBorder(new LineBorder(Color.black));
		ans.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		ans.setAlignmentX(my_alignment);
		//ans.setLayout(new BoxLayout(ans, BoxLayout.LINE_AXIS));
		ans.add(label, c);
		c.gridx = 1;
		c.weightx = 1;
		ans.add(field, c);
		c.gridx = 2;
		c.weightx = 0;
		ans.add(browse, c);
		return ans;
	}
	/**
	 * Create  JPanel that contains the options for
	 * a standard text input files.
	 * @return A JPanel that contains the standard input options.
	 */
	private JPanel createStd(){
		JPanel ans = new JPanel();
		ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		ans.setAlignmentX(my_alignment);
		
		JPanel quick = new JPanel();
		quick.setLayout(new BoxLayout(quick, BoxLayout.LINE_AXIS));
		quick.setBorder(new TitledBorder("Quick Fileset"));
		quick.setAlignmentX(my_alignment);
		quick.add(stdCheck);
		stdCheck.setAlignmentX(my_alignment);
		quick.add(stdShort);
		stdShort.setAlignmentX(my_alignment);
		ans.add(quick);
		
		ans.add(bundle(new JLabel(".ped file"), pedField, pedBrowse));
		ans.add(bundle(new JLabel(".map file"), mapField, mapBrowse));
		
		return ans;
	}
	/**
	 * Create a JPanel that has the binary input options.
	 * @return A JPanel that contains the binary input options.
	 */
	private JPanel createBinary(){
		JPanel ans = new JPanel();
		ans.setAlignmentX(my_alignment);
		ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		
		JPanel quick = new JPanel();
		quick.setLayout(new BoxLayout(quick, BoxLayout.LINE_AXIS));
		quick.setBorder(new TitledBorder("Quick Fileset"));
		quick.setAlignmentX(my_alignment);
		quick.add(binCheck);
		binCheck.setAlignmentX(my_alignment);
		quick.add(binShort);
		binShort.setAlignmentX(my_alignment);
		
		ans.add(quick);
		ans.add(bundle(new JLabel(".bed file"), bedField, bedBrowse));
		ans.add(bundle(new JLabel(".bim file"), bimField, bimBrowse));
		ans.add(bundle(new JLabel(".fam file"), famField, famBrowse));
		
		return ans;		
	}
	/**
	 * Create a JPanel that contains the alternative
	 * phenotype options.
	 * @return A JPanel that contains the alternative
	 * pheotype options.
	 */
	private JPanel createAltPheno(){
		JPanel ans = new JPanel();
		//ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		ans.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		ans.setAlignmentX(my_alignment);
		
		JPanel altPanel = new JPanel();
		altPanel.setLayout(new BoxLayout(altPanel, BoxLayout.LINE_AXIS));
		altPanel.setAlignmentX(my_alignment);
		altPanel.add(useAlt);
		altPanel.add(altField);
		altPanel.add(altBrowse);
		ans.add(altPanel, c);
		
		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BoxLayout(colPanel, BoxLayout.LINE_AXIS));
		colPanel.setAlignmentX(my_alignment);
		colPanel.add(useColm);
		colPanel.add(mphenoText);
		mphenoText.setMaximumSize(mphenoText.getPreferredSize());
		colPanel.add(Box.createHorizontalGlue());
		c.gridy = 1;
		ans.add(colPanel, c);
		
		c.gridy = 2;
		ans.add(new JLabel("Note: You must tab to the Standard/Binary " +
				"Input tabs after setting the alternative phenotype."), c);
		
		return ans;
	}
	/**
	 * Get a list of possible short standard roots.
	 * This is done by grabing all the files in the home
	 * directory and saving the file roots that have both
	 * ped and map files.
	 * @return A ArrayList<String> of possible short standard
	 * roots in the home folder.
	 */
	public ArrayList<String> getStdRoots(){
		ArrayList<String>fileList = parent.data.getHomeFiles();
		HashMap<String, Integer> allfileroots = new HashMap<String, Integer>();
		//go through all the files and count the ped/map files
		//...for each fileroot
		for(String file: fileList){
			if(file.endsWith(".ped") 
					|| file.endsWith(".map")){
			//take the last three off
			String fileroot = FileInfo.fileName(file);
			fileroot = fileroot.substring(0, fileroot.length() - 4);
			if(allfileroots.containsKey(fileroot))
				allfileroots.put(fileroot, allfileroots.get(fileroot)+1);
			else
				allfileroots.put(fileroot, new Integer(1));
			}
		}
		
		//if the fileroot has 2 map/ped flags then add
		//it to the list of possible file roots
		ArrayList <String>ans = new ArrayList <String> ();
		if(allfileroots.isEmpty())
			ans.add("");
		else{
			for( String key : allfileroots.keySet()){
				if(allfileroots.get(key).equals(new Integer(2)))
					ans.add(key);
			}
		}
		Collections.sort(ans);
		return ans;
	}
	/**
	 * Get a list of possible short binary roots.
	 * This is done by grabing all the files in the home
	 * directory and saving the file roots that have 
	 * bed, bim and fam files.
	 * @return A ArrayList<String> of possible short binary
	 * roots in the home folder.
	 */
	public ArrayList<String> getBinRoots(){
		ArrayList<String>fileList = parent.data.getHomeFiles();
		
		HashMap<String, Integer> allfileroots = new HashMap<String, Integer>();
		//go through all the files and count the bed/bim/fam files
		//...for each fileroot
		for(String file: fileList){
			if(file.endsWith(".bed") 
					|| file.endsWith(".fam")
					|| file.endsWith("bim")){
			//take the last three off
			String fileroot = FileInfo.fileName(file);
			fileroot = fileroot.substring(0, fileroot.length() - 4);
			if(allfileroots.containsKey(fileroot))
				allfileroots.put(fileroot, allfileroots.get(fileroot)+1);
			else
				allfileroots.put(fileroot, new Integer(1));
			}
		}
		//if the fileroot has 3 bed/bim/fam flags then add
		//it to the list of possible file roots
		ArrayList <String>ans = new ArrayList <String> ();
		if(allfileroots.isEmpty())
			ans.add("");
		else{
			for( String key : allfileroots.keySet()){
				if(allfileroots.get(key).equals(new Integer(3)))
					ans.add(key);
			}
		}
	
		Collections.sort(ans);
		return ans;
	}
	
	/**
	 * Intialize all the private class variables
	 * we all so add any ItemListeners here.
	 *
	 */
	private void intialize(){
		
		ArrayList<String> stdRoots = getStdRoots();
		stdShort = new JComboBox<String>(stdRoots.toArray(new String[stdRoots.size()]));
		pedField = new JTextField(30);
		pedField.getDocument().addDocumentListener(checkFields);
		pedBrowse = parentForm.new BrowseButton(pedField, ".ped", "PED file");
		mapField = new JTextField(30);
		mapField.getDocument().addDocumentListener(checkFields);
		mapBrowse = parentForm.new BrowseButton(mapField, ".map", "MAP file");
		stdCheck = new JCheckBox();
		stdCheck.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				logger.info("(itemStateChanged) standard check box toggled");
				boolean state = stdCheck.isSelected();
				stdShort.setEnabled(state);
				pedField.setEnabled(!state);
				pedBrowse.setEnabled(!state);
				mapField.setEnabled(!state);
				mapBrowse.setEnabled(!state);
				if(okInput() != validInput){
					validInput = !validInput;
					parentForm.okForm();
				}
			}
		});
		stdCheck.setSelected(true);
		
		ArrayList<String> binRoots = getBinRoots();
		binShort = new JComboBox<String>(binRoots.toArray(new String[binRoots.size()]));
		bedField = new JTextField(30);
		bedField.getDocument().addDocumentListener(checkFields);
		bedBrowse = parentForm.new BrowseButton(bedField, ".bed", "BED file");
		bimField = new JTextField(30);
		bimField.getDocument().addDocumentListener(checkFields);
		bimBrowse = parentForm.new BrowseButton(bimField, ".bim", "BIM file");
		famField = new JTextField(30);
		famField.getDocument().addDocumentListener(checkFields);
		famBrowse = parentForm.new BrowseButton(famField, ".fam", "FAM file");
		binCheck = new JCheckBox();
		binCheck.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				logger.info("(itemStateChanged) binary check box toggled");
				boolean state = binCheck.isSelected();
				binShort.setEnabled(state);
				bedField.setEnabled(!state);
				bedBrowse.setEnabled(!state);
				bimField.setEnabled(!state);
				bimBrowse.setEnabled(!state);
				famField.setEnabled(!state);
				famBrowse.setEnabled(!state);
				if(okInput() != validInput){
					validInput = !validInput;
					parentForm.okForm();
				}
			}
		});
		binCheck.setSelected(true);
				
		altField = new JTextField(20);
		altField.getDocument().addDocumentListener(checkFields);
		altBrowse = parentForm.new BrowseButton(altField, "phe", "PHE file");
		mphenoText = new JTextField(5);
		mphenoText.getDocument().addDocumentListener(checkFields);
		useColm = new JCheckBox("use column (--mpheno)");
		useColm.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				logger.info("(itemStateChanged) column check box toggled");
				boolean state = useColm.isSelected();
				mphenoText.setEnabled(state);
				if(okInput() != validInput){
					validInput = !validInput;
					parentForm.okForm();
				}
			}
		});
		useColm.setSelected(false);
		
		useAlt = new JCheckBox("use alternative phenotype");
		useAlt.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				logger.info("(itemStateChanged) alternative phenotype check box toggled");
				boolean state = useAlt.isSelected();
				altField.setEnabled(state);
				altBrowse.setEnabled(state);
				useColm.setEnabled(state);
				if(okInput() != validInput){
					validInput = !validInput;
					parentForm.okForm();
				}
			}
		});
		useAlt.setSelected(false);
	}
	/**
	 * Create an input pane for PLINK forms.
	 * @param p The GPLINK instance that this form
	 * works with.
	 */
	public InputPane(GPLINK p, Form p2){
		super();
		parent = p;
		parentForm = p2;
		intialize();
		this.addTab("Binary Input", createBinary());
		this.addTab("Standard Input", createStd());
		this.addTab("Alternate Phenotype", createAltPheno());
		this.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				logger.info("(stateChanged) shifting tabs.");
				if(okInput() != validInput){
					validInput = !validInput;
					parentForm.okForm();
				}
			}
		});
		validInput = okInput();
		
	}
	/**
	 * Based on the fields in the input pane generate
	 * the input section of the PLINK command line.
	 * @return A String that is the input section of a 
	 * PLINK command.
	 */
	public String process(){
		String ans = "";
		if(getSelectedIndex() == 0){
			if(binCheck.isSelected())
				ans = "--bfile " + FileInfo.quote(parent.data.getHomeFolder() + binShort.getSelectedItem().toString());
			else{
				ans = "--bed " + FileInfo.quote(bedField.getText())
					+ " --bim " + FileInfo.quote(bimField.getText())
					+ " --fam " + FileInfo.quote(famField.getText());
			}
		}else if(getSelectedIndex() == 1){
			if(stdCheck.isSelected()){
				ans = "--file " + FileInfo.quote(parent.data.getHomeFolder() + stdShort.getSelectedItem().toString());
			} else {
				ans = "--map " + FileInfo.quote(mapField.getText())
				+ " --ped " + FileInfo.quote(pedField.getText());
			}
		}
		if(useAlt.isSelected()){
			ans = ans + " --pheno " + FileInfo.quote(altField.getText());
			if(useColm.isSelected())
				ans = ans + " --mpheno " + mphenoText.getText();
		}
		return ans;
	}
	/**
	 * Check to see if the form is filled out correctly.
	 * @return true if this form is filled out in a valid
	 * way, false otherwise.
	 */
	public boolean okInput(){
		logger.info("(okInput()) entering input pane check");
		if(!(getSelectedIndex() == 0
				|| getSelectedIndex() == 1)){
			return false;
		}
		//check the binary input
		if(getSelectedIndex() == 0){
			if(binCheck.isSelected()){
				if(binShort.getSelectedItem() == null ||
						binShort.getSelectedItem().toString().matches("^\\s*$"))
					return false;
			}else {
				if((bedField.getText().matches("^\\s*$")
						|| bedField.getText().equals(""))
					||  (bimField.getText().matches("^\\s*$")
						|| bimField.getText().equals(""))
					||  (famField.getText().matches("^\\s*$")
						|| famField.getText().equals("")))
					return false;
			}
		}
		//check the standard input
		if(getSelectedIndex() == 1){
			if(stdCheck.isSelected()){
				if(stdShort.getSelectedItem() == null ||
						stdShort.getSelectedItem().toString().matches("\\s"))
					return false;
			}else {
				if((mapField.getText().matches("\\s")
						|| mapField.getText().equals(""))
					||  (pedField.getText().matches("\\s")
							|| pedField.getText().equals("")))
					return false;
			}
		}
		//nothing was caught
		return true;
	}
	
	DocumentListener checkFields = new DocumentListener(){

		public void changedUpdate(DocumentEvent e) {
			
		}
		public void insertUpdate(DocumentEvent e) {
			if(okInput() != validInput){
				validInput = !validInput;
				parentForm.okForm();
			}
		}
		public void removeUpdate(DocumentEvent e) {
			if(okInput() != validInput){
				validInput = !validInput;
				parentForm.okForm();
			}
		}
		
	};
	
	
}
