package gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;

/**
 * An extention of JDialog that asks the user to set the 
 * filter parameters that are then passed to the calling form.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class FilterDialog extends JDialog {
	
	private Form parent;
	
	private JComboBox chrCBox, toCBox, fromCBox, eeCBox, krCBox,
		sexFilterCBox, ccFilterCBox, fFilterCBox;
	private JTextField
		setField, geneField, fromField, toField, 
		SNPField, windowField, eeField, krField,
		filterFileField, filterKeyField, mfilterField;
	private JCheckBox
		chrButton, sgButton, ftButton, swButton, 
		eeButton, krButton,
		fileFilterButton, sexFilterButton, ccFilterButton, fFilterButton;
	private BrowseButton
		setButton, eeBrowse, krBrowse, filterBrowse;
	private JButton okButton, cancelButton;
	JLabel geneLable, toLabel, wLabel;
	
	private String [] lengthOpt1 = new String []{"SNP"};
	private String [] lengthOpt2 = new String []{"kb", "Mb", "bp"};
	
	private ActionListener pickMap = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(chrButton) ){
				if(chrButton.isSelected()){
					chrCBox.setEnabled(true);

					swButton.setSelected(false);
					SNPField.setEditable(false);
					windowField.setEditable(false);
				}else{
					chrCBox.setEnabled(false);
				}
			}

			if(e.getSource().equals(swButton)){
				if(swButton.isSelected()){
					SNPField.setEditable(true);
					windowField.setEditable(true);
					
					chrButton.setSelected(false);
					chrCBox.setEnabled(false);
					
					ftButton.setSelected(false);
					fromCBox.setEnabled(false);
					fromField.setEditable(false);
					toCBox.setEnabled(false);
					toField.setEditable(false);
				}else{
					SNPField.setEditable(false);
					windowField.setEditable(false);
				}
				
			}
			
			if(e.getSource().equals(ftButton)){
				if(ftButton.isSelected()){
					fromCBox.setEnabled(true);
					fromField.setEditable(true);
					toCBox.setEnabled(true);
					toField.setEditable(true);
					
					swButton.setSelected(false);
					SNPField.setEditable(false);
					windowField.setEditable(false);
				}else{
					fromCBox.setEnabled(false);
					fromField.setEditable(false);
					toCBox.setEnabled(false);
					toField.setEditable(false);
				}
			}
		}
	};
	
	private ActionListener pickList = new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(sgButton)){
				if(sgButton.isSelected()){
					setField.setEditable(true);
					setButton.setEnabled(true);
					geneField.setEditable(true);
					
					eeButton.setSelected(false);
					eeCBox.setEnabled(false);
					eeField.setEditable(false);
					eeBrowse.setEnabled(false);
				} else {
					setField.setEditable(false);
					setButton.setEnabled(false);
					geneField.setEditable(false);
				}
			}
			
			if(e.getSource().equals(eeButton)){
				if(eeButton.isSelected()){
					eeCBox.setEnabled(true);
					eeField.setEditable(true);
					eeBrowse.setEnabled(true);
					
					sgButton.setSelected(false);
					setField.setEditable(false);
					setButton.setEnabled(false);
					geneField.setEditable(false);
				}else{
					eeCBox.setEnabled(false);
					eeField.setEditable(false);
					eeBrowse.setEnabled(false);
				}
			}
			
			if(e.getSource().equals(krButton)){
				if(krButton.isSelected()){
					krCBox.setEnabled(true);
					krField.setEditable(true);
					krBrowse.setEnabled(true);
				}else{
					krCBox.setEnabled(false);
					krField.setEditable(false);
					krBrowse.setEnabled(false);
				}
			}
			
			
		}
		
	};
	
	private ActionListener pickFilter = new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(fileFilterButton)){
				boolean state = fileFilterButton.isSelected();
				filterBrowse.setEnabled(state);
				filterFileField.setEditable(state);
				filterKeyField.setEditable(state);
				mfilterField.setEditable(state);
			}
			if(e.getSource().equals(sexFilterButton)){
				boolean state = sexFilterButton.isSelected();
				sexFilterCBox.setEnabled(state);
			}
			if(e.getSource().equals(ccFilterButton)){
				boolean state = ccFilterButton.isSelected();
				ccFilterCBox.setEnabled(state);
			}
			if(e.getSource().equals(fFilterButton)){
				boolean state = fFilterButton.isSelected();
				fFilterCBox.setEnabled(state);				
			}
		}
	};
	
	private void initalize(){
		
		chrCBox = new JComboBox(new String []{
								" 1", " 2", " 3", " 4", " 5",
								" 6", " 7", " 8", " 9", "10",
								"11", "12", "13", "14", "15",
								"16","17", "18", "19", "20", 
								"21", "22", " X", " Y"});
		chrCBox.setSelectedIndex(0);
		chrCBox.setPreferredSize(new Dimension(chrCBox.getPreferredSize().width*2,
				                                chrCBox.getPreferredSize().height));
		chrCBox.setEnabled(false);
		chrButton = 
			new JCheckBox("Chromosome (--chr) ");
		chrButton.addItemListener(changeFromTo);
		chrButton.addActionListener(pickMap);

		swButton = 
			new JCheckBox("Specific SNP (--snp) ");
		swButton.addActionListener(pickMap);
		
		SNPField = 
			new JTextField(10);
		wLabel = new JLabel("Optional kb window (--window)");
		windowField =
			new JTextField(10);
			
		ftButton =
			new JCheckBox("--from");
		ftButton.addActionListener(pickMap);
		fromCBox = new JComboBox(lengthOpt1);
		fromCBox.setSelectedIndex(0);
		fromCBox.setEnabled(false);
		fromField =
			new JTextField(10);
		toLabel = new JLabel(" --to");
		toCBox = new JComboBox(lengthOpt1);
		toCBox.setSelectedIndex(0);
		toCBox.setEnabled(false);
		toField =
			new JTextField(10);
		
		sgButton = 
			new JCheckBox("SNP set-file (--set) ", null);
		sgButton.addActionListener(pickList);
		setField =
			new JTextField(10);
		setButton = //new JButton("Browse");
			new BrowseButton(setField, ".set", "SET file");
		geneLable = new JLabel("Optional specific gene (--gene) ");
		geneField =
			new JTextField(5);
		
		eeButton =
			new JCheckBox("SNPs --");
		eeButton.addActionListener(pickList);
		eeCBox = new JComboBox(new String []{"extract", "exclude"});
		eeCBox.setEnabled(false);
		eeField = 
			new JTextField(10);
		eeBrowse = 
			new BrowseButton(eeField, ".list", "LIST file");
		
		krButton = 
			new JCheckBox("Individuals --");
		krButton.addActionListener(pickList);
		krCBox = new JComboBox(new String []{"keep", "remove"});
		krCBox.setEnabled(false);
		krField =
			new JTextField(10);
		krBrowse =
			new BrowseButton(krField, ".list", "LIST file");
		
		
		sexFilterCBox = new JComboBox(new String[]{"males", "females"});
		sexFilterCBox.setEnabled(false);
		ccFilterCBox = new JComboBox(new String []{"cases", "controls"});
		ccFilterCBox.setEnabled(false);
		fFilterCBox = new JComboBox(new String []{"founders", "nonfounders"});
		fFilterCBox.setEnabled(false);
		filterFileField = new JTextField(10);
		filterKeyField = new JTextField(5);
		mfilterField = new JTextField(5);
		filterBrowse = new BrowseButton(filterFileField,".txt", "Text file");
		fileFilterButton = new JCheckBox("Filter variables (--filter) ", null);
		fileFilterButton.addActionListener(pickFilter);
		sexFilterButton = new JCheckBox("Sex filter --filter-", null);
		sexFilterButton.addActionListener(pickFilter);
		ccFilterButton = new JCheckBox("Individuals --", null);
		ccFilterButton.addActionListener(pickFilter);
		fFilterButton = new JCheckBox("Individuals --", null);
		fFilterButton.addActionListener(pickFilter);
		
		okButton = new JButton("OK");
		okButton.addActionListener(process);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(cancel);
		
	}

	private void create(){
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel chrPanel = new JPanel();
		chrPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		chrPanel.add(chrButton);
		chrPanel.add(chrCBox);
		
		JPanel sgPanel = new JPanel();
		sgPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		sgPanel.add(sgButton);
		sgPanel.add(setField);
		sgPanel.add(setButton);
		sgPanel.add(geneLable);
		sgPanel.add(geneField);
		
		JPanel ftPanel = new JPanel();
		ftPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		ftPanel.add(ftButton);
		ftPanel.add(fromCBox);
		ftPanel.add(fromField);
		ftPanel.add(toLabel);
		ftPanel.add(toCBox);
		ftPanel.add(toField);
		
		JPanel swPanel = new JPanel();
		swPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		swPanel.add(swButton);
		swPanel.add(SNPField);
		swPanel.add(wLabel);
		swPanel.add(windowField);
		
		JPanel eePanel = new JPanel();
		eePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		eePanel.add(eeButton);
		eePanel.add(eeCBox);
		eePanel.add(eeField);
		eePanel.add(eeBrowse);
		
		JPanel krPanel = new JPanel();
		krPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		krPanel.add(krButton);
		krPanel.add(krCBox);
		krPanel.add(krField);
		krPanel.add(krBrowse);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		JPanel mapPanel = new JPanel();
		mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.PAGE_AXIS));
		mapPanel.setBorder(new TitledBorder("By Map"));
		mapPanel.add(chrPanel);
		mapPanel.add(ftPanel);
		mapPanel.add(swPanel);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
		listPanel.setBorder(new TitledBorder("By List"));
		listPanel.add(sgPanel);
		listPanel.add(eePanel);
		listPanel.add(krPanel);
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		filterPanel.add(fileFilterButton);
		filterPanel.add(filterFileField);
		filterPanel.add(filterBrowse);
		filterPanel.add(filterKeyField);
		filterPanel.add(new JLabel("Optional column (--mfilter)"));
		filterPanel.add(mfilterField);
		
		JPanel sexFilterPanel = new JPanel();
		sexFilterPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		sexFilterPanel.add(sexFilterButton);
		sexFilterPanel.add(sexFilterCBox);
		
		JPanel ccFilterPanel = new JPanel();
		ccFilterPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		ccFilterPanel.add(ccFilterButton);
		ccFilterPanel.add(ccFilterCBox);
		
		JPanel fFilterPanel = new JPanel();
		fFilterPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		fFilterPanel.add(fFilterButton);
		fFilterPanel.add(fFilterCBox);
		
		JPanel indPanel = new JPanel();
		indPanel.setLayout(new BoxLayout(indPanel, BoxLayout.PAGE_AXIS));
		indPanel.setBorder(new TitledBorder("By Individual SNP"));
		indPanel.add(filterPanel);
		indPanel.add(sexFilterPanel);
		indPanel.add(ccFilterPanel);
		indPanel.add(fFilterPanel);
		
		getContentPane().add(mapPanel);
		getContentPane().add(listPanel);
		getContentPane().add(indPanel);
		getContentPane().add(buttonPanel);
	}
	
	/**
	 * Constructor
	 * @param owner The Forms that this dialog is modifying.
	 */
	public FilterDialog(Form owner){
		super(owner, "Filter SNPs and/or Individuals");
		parent = owner;
		parent.resetFilter();
		//don't let the user edit the form until we have closed this dialog
		parent.closeButtons();
		
		initalize();
		create();
		
		this.addWindowListener(properClosing);
		
		pack();
		setVisible(true);
	}
	
	
	private ItemListener changeFromTo = new ItemListener(){
		public void itemStateChanged(ItemEvent arg0) {
			fromCBox.removeAllItems();
			toCBox.removeAllItems();
			if(chrButton.isSelected() == true){
				for(int i = 0; i< lengthOpt2.length; i++){
					fromCBox.addItem(lengthOpt2[i]);
					toCBox.addItem(lengthOpt2[i]);
				}
			}
			else{
				for(int i = 0; i< lengthOpt1.length; i++){
					fromCBox.addItem(lengthOpt1[i]);
					toCBox.addItem(lengthOpt1[i]);
				}
			}
				
		}
	};
	
	private WindowListener properClosing = new WindowListener(){

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {
			parent.openButtons();
		}
		public void windowClosing(WindowEvent e) {
			parent.openButtons();
		}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	};
	
	private ActionListener cancel = new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			parent.resetFilter();
			dispose();			
		}
		
	};
	
	private ActionListener process = new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			
			if (chrButton.isSelected()){
				parent.addFilter(" --chr " + (String) chrCBox.getSelectedItem());
			}
			if(sgButton.isSelected()){
				parent.addFilter(" --set " + FileInfo.quote(setField.getText()));
				if(!geneField.getText().equals(""))	
					parent.addFilter(" --gene " + geneField.getText());
			}
			if(ftButton.isSelected()){
				parent.addFilter(" --from");
				if(fromCBox.getSelectedItem().toString().equals("kb"))
					parent.addFilter("-kb");
				if(fromCBox.getSelectedItem().toString().equals("Mb"))
					parent.addFilter("-mb");
				if(fromCBox.getSelectedItem().toString().equals("bp"))
					parent.addFilter("-bp");
				parent.addFilter(" " + fromField.getText() +" --to");
				if(toCBox.getSelectedItem().toString().equals("kb"))
					parent.addFilter("-kb");
				if(toCBox.getSelectedItem().toString().equals("Mb"))
					parent.addFilter("-mb");
				if(toCBox.getSelectedItem().toString().equals("bp"))
					parent.addFilter("-bp");
				parent.addFilter(" " + toField.getText());
			}

			if(swButton.isSelected()){
				if ( ! SNPField.getText().equals("") )  
					parent.addFilter(" --snp " + SNPField.getText());
				if ( ! windowField.getText().equals("") )  
					parent.addFilter(" --window " + windowField.getText());								
			}

			if(eeButton.isSelected()){
				if ( ! eeField.getText().equals("") )  
				{
					if (eeCBox.getSelectedItem().toString().equals("extract"))
						parent.addFilter(" --extract " 
								+ FileInfo.quote(eeField.getText()));				
					else 
						parent.addFilter(" --exclude " 
								+ FileInfo.quote(eeField.getText()));				
				}				
			}
		
			if(krButton.isSelected())
			{
				if ( ! krField.getText().equals("") )  
				{
					if (krCBox.getSelectedItem().toString().equals("keep"))
						parent.addFilter(" --keep " 
								+ FileInfo.quote(krField.getText()));				
					else 
						parent.addFilter(" --remove " 
								+ FileInfo.quote(krField.getText()));				
				}								
			}
			
			if(fileFilterButton.isSelected()){
				parent.addFilter(" --filter "
						+ FileInfo.quote(filterFileField.getText())
						+ " "
						+ filterKeyField.getText());
				if(!mfilterField.getText().equals(""))
					parent.addFilter(" --mfilter "
						+ mfilterField.getText());
			}
			
			if(sexFilterButton.isSelected()){
				parent.addFilter(" --filter-"
						+ sexFilterCBox.getSelectedItem().toString());
			}
			
			if(ccFilterButton.isSelected()){
				parent.addFilter(" --filter-"
						+ ccFilterCBox.getSelectedItem().toString());
			}
			if(fFilterButton.isSelected()){
				parent.addFilter(" --filter-"
						+ fFilterCBox.getSelectedItem().toString());
			}
			
			dispose();
		}
		
	};
	
	@SuppressWarnings({ "serial", "unused" })
	protected class BrowseButton extends JButton{
		
		private String suffix;
		private String suffixDesc;
		private JTextField target;
		private GCFileChooser pick;
		
		public BrowseButton(JTextField givenTarget){
			this(givenTarget, null, null);
		}
		
		public BrowseButton(JTextField givenTarget, 
				String givenSuffix, 
				String givenSuffixDesc){
			super("Browse");
			target = givenTarget;
			suffix = givenSuffix;
			suffixDesc = givenSuffixDesc;
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					FileFilter temp = null;
					
					if(suffix != null){
						temp = new FileFilter (){
							@Override
							public boolean accept(File f) {
								return f.getAbsolutePath().endsWith(suffix);
							}
							@Override
							public String getDescription() {
								return suffixDesc;
							}};
					}
					
					// TODO EW EW EW multiple levels of ancestral tree requests? there has GOT to be a better way to do this
					pick = new GCFileChooser(
							FilterDialog.this, 
							temp, 
							!parent.parent.isRemote(), 
							false,
							parent.parent.getConn(), 
							parent.parent.getHomeFolder());
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
