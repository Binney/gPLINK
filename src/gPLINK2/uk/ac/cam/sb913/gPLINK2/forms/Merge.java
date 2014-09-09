package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;
//ok this has been a very difficult form to do grrrrr!
/**
 * Merge two file sets.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class Merge extends Form{
	/**
	 * The name of the form
	 */
	public static String name = "Merge Filesets";
	private TextSelectPanel targetText;
	private BinSelectPanel targetBin;
	private ListSelectPanel targetTXT;
	private JTabbedPane mergeInputs;
	
	//the various options
	private JRadioButton recode, recodeAD, recode12, recodeHV, makeBin;
	private JPanel recodeOp;
	
	private JRadioButton op1, op2, op3, op4, op5, op6, op7;
	private JPanel optional;
		
	public Merge(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
		//doens't make any sense to have the filter or threshold
		//...options turned on for this form
		super.filterButton.setEnabled(false);
		super.thresholdButton.setEnabled(false);
		//this.buttons.disableFT();
		
	}
	
	protected void initalize() {		
		targetBin = new BinSelectPanel(parent);
		targetText = new TextSelectPanel(parent);
		targetTXT = new ListSelectPanel(parent);
		
		mergeInputs = new JTabbedPane();
		
		recodeOp = new JPanel();
		recode = new JRadioButton("Standard fileset (--recode)");
		recodeAD = new JRadioButton("Raw genotype file (--recodeAD)");
		recode12 = new JRadioButton("Standard fileset w/ allele recoding (--recode12)");
		recodeHV = new JRadioButton("Haploview fileset (--recodeHV)");
		makeBin = new JRadioButton("Binary fileset (--make-bed)");
		
		optional = new JPanel();
		op1 = new JRadioButton("1) Consensus call (default)");
		op2 = new JRadioButton("2) Only overwrite calls missing in original");
		op3 = new JRadioButton("3) Only overwrite calls not missing in new");
		op4 = new JRadioButton("4) Never overwrite");
		op5 = new JRadioButton("5) Always overwrite");
    	op6 = new JRadioButton("6) Report all mismatches");
    	op7 = new JRadioButton("7) Report non-missing mismatches");
    	
    	validBody = targetBin.validInput;
	}

	void createMergeTarget(){
		mergeInputs.addTab("Merge Binary Fileset", targetBin);
		mergeInputs.addTab("Merge Standard Fileset", targetText);
		mergeInputs.addTab("Merge Multiple Filesets", targetTXT);
		ChangeListener checkMergeTab = new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				op6.setEnabled(mergeInputs.getSelectedIndex() != 2);
				op7.setEnabled(mergeInputs.getSelectedIndex() != 2);
				isBodyValid();
			}
		};
		
		mergeInputs.addChangeListener(checkMergeTab);
		
	}
	
	void createRecodeOps(){
		
		recodeOp.setLayout(new BoxLayout(recodeOp, BoxLayout.PAGE_AXIS));
		
		recode.setSelected(true);
		
		recodeOp.add(recode);
		recodeOp.add(recode12);
		recodeOp.add(recodeAD);
		recodeOp.add(recodeHV);
		recodeOp.add(makeBin);
		
		ButtonGroup recodeGroup = new ButtonGroup();
		recodeGroup.add(recode);
		recodeGroup.add(recode12);
		recodeGroup.add(recodeAD);
		recodeGroup.add(recodeHV);
		recodeGroup.add(makeBin);
		
	}
	
	void createOptions(){
		optional.setLayout(new BoxLayout(optional, BoxLayout.PAGE_AXIS));
		
    	ButtonGroup optionGroup = new ButtonGroup();
    	optionGroup.add(op1);
    	op1.setSelected(true);
    	optionGroup.add(op2);
    	optionGroup.add(op3);
    	optionGroup.add(op4);
    	optionGroup.add(op5);
    	optionGroup.add(op6);
    	optionGroup.add(op7);
    	
    	optional.add(op1);
    	optional.add(op2);
    	optional.add(op3);
    	optional.add(op4);
    	optional.add(op5);
    	optional.add(op6);
    	optional.add(op7);
    	
	}
	
	@Override
	protected JPanel createBody() {
		initalize();
		JPanel body = new JPanel();
		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
			
		createOptions();
		createMergeTarget();
		createRecodeOps();
		
		body.add(mergeInputs);
		JPanel formating = new JPanel();
		formating.setLayout(new BoxLayout(formating, BoxLayout.LINE_AXIS));
		formating.add(recodeOp);
		formating.add(new JSeparator(SwingConstants.VERTICAL));
		formating.add(optional);
		body.add(formating);
		return body;
	}

	@Override
	protected String processBody() {
		String ans = "";
				
		if(mergeInputs.getSelectedIndex() == 0)
			ans += targetBin.getCommand();
		if(mergeInputs.getSelectedIndex() == 1)
			ans += targetText.getCommand();
		if(mergeInputs.getSelectedIndex() == 2)
			ans += targetTXT.getCommand();
		
		if(op2.isSelected() && op2.isEnabled())
			ans += " --merge-mode 2";
		if(op3.isSelected() && op3.isEnabled())
			ans += " --merge-mode 3";
		if(op4.isSelected() && op4.isEnabled())
			ans += " --merge-mode 4";
		if(op5.isSelected() && op5.isEnabled())
			ans += " --merge-mode 5";
		if(op6.isSelected() && op6.isEnabled())
			ans += " --merge-mode 6";
		if(op7.isSelected() && op7.isEnabled())
			ans += " --merge-mode 7";
		
		if(recode.isSelected())
			ans += " --recode";
		if(recodeAD.isSelected())
			ans += " --recodeAD";
		if(recode12.isSelected())
			ans += " --recode12";
		if(recodeHV.isSelected())
			ans += " --recodeHV";
		if(makeBin.isSelected())
			ans += " --make-bed";
		
		return ans;
	}

	@Override
	protected void isBodyValid() {
		if(((mergeInputs.getSelectedIndex() == 0) && !targetBin.validInput)
			||	((mergeInputs.getSelectedIndex() == 1) && !targetText.validInput)
			|| ((mergeInputs.getSelectedIndex() == 2) && !targetTXT.validInput )){
//			information.setText("Enter valid target for merging");
			validBody = false;
		} else if(!(recode.isSelected() && recode.isEnabled() )
				|| (recodeAD.isSelected() && recodeAD.isEnabled())
				|| (recodeHV.isSelected() && recodeHV.isEnabled())
				|| (recode12.isSelected() && recode12.isEnabled())
				|| (makeBin.isSelected() && makeBin.isEnabled()) ){
//			information.setText("Must select a coding option.");
			validBody = false;
		} else		
			validBody = true;
		okForm();
	}

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
	
	private class TextSelectPanel extends JPanel{
		public boolean validInput = false;
		private float my_alignment = Component.RIGHT_ALIGNMENT;
		
		private JComboBox stdShort;
		private JCheckBox stdCheck;
		private JTextField pedField, mapField;
		private BrowseButton pedBrowse, mapBrowse;
		private DocumentListener textValidDL = new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				TextSelectPanel.this.checkValid();
			}
			public void removeUpdate(DocumentEvent e) {
				TextSelectPanel.this.checkValid();
			}
		};
		
		private void createStd(){
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setAlignmentX(my_alignment);
			
			JPanel quick = new JPanel();
			quick.setBorder(new TitledBorder("Quick Fileset"));
			quick.setAlignmentX(my_alignment);
			quick.add(stdCheck);
			quick.add(stdShort);
			add(quick);
			
			add(Merge.this.bundle(new JLabel(".ped file"), pedField, pedBrowse));
			
			add(Merge.this.bundle(new JLabel(".map file"), mapField, mapBrowse));
			
		}
		
		public void checkValid(){
			if(stdCheck.isSelected()){
				validInput = (stdShort.getSelectedItem()!= null 
						&& !stdShort.getSelectedItem().toString().equals(""));
			}else{
				validInput = !pedField.getText().equals("")
							&& !mapField.getText().equals("");
			}
			isBodyValid();
		}
		
		public TextSelectPanel(FormCreator fc){
			ArrayList<String> stdRoots= Merge.this.input.getStdRoots();
			stdShort = new JComboBox<String>(stdRoots.toArray(new String[stdRoots.size()]));
			pedField = new JTextField(20);
			pedField.getDocument().addDocumentListener(textValidDL);
			pedField.setEnabled(false);
			mapField = new JTextField(20);
			mapField.setEnabled(false);
			mapField.getDocument().addDocumentListener(textValidDL);
			pedBrowse = new BrowseButton(pedField, ".ped", "PED file");
			pedBrowse.setEnabled(false);
			mapBrowse = new BrowseButton(mapField, ".map", "MAP field");
			mapBrowse.setEnabled(false);
			stdCheck = new JCheckBox();
			stdCheck.setSelected(true);
			stdCheck.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					boolean state = stdCheck.isSelected();
					stdShort.setEnabled(state);
					pedField.setEnabled(!state);
					mapField.setEnabled(!state);
					pedBrowse.setEnabled(!state);
					mapBrowse.setEnabled(!state);
					TextSelectPanel.this.checkValid();
				}
			});
			
			createStd();
			
			validInput = (stdShort.getSelectedItem()!= null 
					&& !stdShort.getSelectedItem().toString().equals(""));
		}
		
		public String getCommand(){
			String ans = "--merge";
			if(stdCheck.isSelected()){
				String root = stdShort.getSelectedItem().toString();
				// TODO again with the ancestry calling... this is not Who Do You Think You Are
				ans += " "+ FileInfo.quote(parent.getFrame().data.getHomeFolder() + root + ".ped")
					+ " " + FileInfo.quote(parent.getFrame().data.getHomeFolder() + root + ".map");
			} else{
				ans += " " + FileInfo.quote(pedField.getText())
					+ " " + FileInfo.quote(mapField.getText());
			}
			return ans;
		}
		
		
	}
	
	private class BinSelectPanel extends JPanel{
		public boolean validInput = false;
		private float my_alignment = Component.RIGHT_ALIGNMENT;
		private JCheckBox flagShort;
		private JTextField bedField, bimField, famField;
		private BrowseButton bedBrowse, bimBrowse, famBrowse;
		private JComboBox binShort;
		private DocumentListener binValidDL = new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				BinSelectPanel.this.checkValid();
			}
			public void removeUpdate(DocumentEvent e) {
				BinSelectPanel.this.checkValid();
			}
		};
		
		public void checkValid(){
			if(flagShort.isSelected()){
				validInput = (binShort.getSelectedItem()!= null 
						&& !binShort.getSelectedItem().toString().equals(""));
			}else{
				validInput = !bedField.getText().equals("")
							&& !bimField.getText().equals("")
							&& !famField.getText().equals("");
			}
			isBodyValid();
		}
		
		private BinSelectPanel(FormCreator fc){
			ArrayList<String> binRoots = Merge.this.input.getBinRoots();
			binShort = new JComboBox<String>(binRoots.toArray(new String[binRoots.size()]));
			bedField = new JTextField(20);
			bedField.setEnabled(false);
			bedField.getDocument().addDocumentListener(binValidDL);
			bedBrowse = new BrowseButton(bedField, ".bed", "BED file");
			bedBrowse.setEnabled(false);
			bimField = new JTextField(20);
			bimField.setEnabled(false);
			bimField.getDocument().addDocumentListener(binValidDL);
			bimBrowse = new BrowseButton(bimField, ".bim", "BIM file");
			bimBrowse.setEnabled(false);
			famField = new JTextField(20);
			famField.setEnabled(false);
			famField.getDocument().addDocumentListener(binValidDL);
			famBrowse = new BrowseButton(famField, ".fam", "FAM file");
			famBrowse.setEnabled(false);
			flagShort = new JCheckBox();
			flagShort.setSelected(true);
			flagShort.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					boolean state = flagShort.isSelected();
					binShort.setEnabled(state);
					bedField.setEnabled(!state);
					bedBrowse.setEnabled(!state);
					bimField.setEnabled(!state);
					bimBrowse.setEnabled(!state);
					famField.setEnabled(!state);
					famBrowse.setEnabled(!state);
					BinSelectPanel.this.checkValid();
				}
			});
			
			createBinary();
			
			validInput = (binShort.getSelectedItem()!= null 
					&& !binShort.getSelectedItem().toString().equals(""));
		}
		
		private void createBinary(){
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setAlignmentX(my_alignment);
			
			JPanel quick = new JPanel();
			quick.setBorder(new TitledBorder("Quick Fileset"));
			quick.setAlignmentX(my_alignment);
			quick.add(flagShort);
			quick.add(binShort);
			add(quick);
			
			add(Merge.this.bundle(new JLabel(".bed file"), 
					bedField, bedBrowse));
			add(Merge.this.bundle(new JLabel(".bim file"),
					bimField, bimBrowse));
			add(Merge.this.bundle(new JLabel("fam file"), 
					famField, famBrowse));
				
		}
		
		public String getCommand(){
			String ans = " --bmerge";
			if(flagShort.isSelected()){
				String root = binShort.getSelectedItem().toString();
				ans += " "+ FileInfo.quote(parent.getFrame().data.getHomeFolder() + root + ".bed") 
					+ " " + FileInfo.quote(parent.getFrame().data.getHomeFolder() + root + ".bim")
					+ " " + FileInfo.quote(parent.getFrame().data.getHomeFolder() + root + ".fam");
			} else{
				ans += " " + FileInfo.quote(bedField.getText())
					+ " " + FileInfo.quote(bimField.getText()) 
					+ " " + FileInfo.quote(famField.getText());
			}
			return ans;
		}
	}
	
	private class ListSelectPanel extends JPanel{
		public boolean validInput = false;
		
		private BrowseButton listBrowse;
		private JTextField listField;
		
		public void checkValid(){
			validInput = !listField.getText().equals("");
			isBodyValid();
		}
		
		private JPanel createPanel(){
			JPanel ans = new JPanel();
			ans.add(new JLabel("--merge-list "));
			ans.add(listField);
			ans.add(listBrowse);
			return ans;
		}
		
		private ListSelectPanel(FormCreator fc){
			listField = new JTextField(20);
			listField.getDocument().addDocumentListener(new DocumentListener(){
				public void changedUpdate(DocumentEvent e) {}
				public void insertUpdate(DocumentEvent e) {
					ListSelectPanel.this.checkValid();
				}
				public void removeUpdate(DocumentEvent e) {
					ListSelectPanel.this.checkValid();
				}
			});
			listBrowse = new BrowseButton(listField, ".txt", "TEXT files");
			
			add(createPanel());
		}
		
		public String getCommand(){
			String ans = "--merge-list " + FileInfo.quote(listField.getText());
			
			return ans;
		}
	}

}
