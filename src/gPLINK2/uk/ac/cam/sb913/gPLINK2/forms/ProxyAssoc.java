package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/*
 * Form for Proxy association
 */
@SuppressWarnings("serial")
public class ProxyAssoc extends Form {

	public static String name = "Proxy association";
	/**
	 * The JRadioButton that flags the '--proxy-assoc'
	 * option.
	 */
	private JRadioButton assocRadioButton;
	/**
	 * The JRadioButton that flags the '--proxy-impute'
	 * option.
	 */
	private JRadioButton imputeRadioButton;
	/**
	 * The JRadioButton that flags the 
	 * '(SNP)' option
	 */
	private JRadioButton snpRadioButton;
	/**
	 * The JTextField that holds the snp
	 * reference for the '(SNP)'
	 */
	private JTextField snpField;
	/**
	 * The JRadioButton that flags the
	 * 'all --proxy-list (file)' option
	 */
	private JRadioButton allRadioButton;
	/**
	 * The JTextField that flags the 
	 * optional proxy list association with
	 * '--proxy-list (file)'
	 */
	private JTextField allField;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-window (int)'
	 */
	private JCheckBox windowCheckBox;
	/**
	 * The JTextField that gets the integer
	 * for '--proxy-window (int)'
	 */
	private JTextField windowField;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-kb (int)'
	 */
	private JCheckBox kbCheckBox;
	/**
	 * The JTextField that gets the integer
	 * for '--proxy-kb (int)'
	 */
	private JTextField kbField;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-r2-filters (double x3)'
	 */
	private JCheckBox r2CheckBox;
	/**
	 * The JTextField that gets the double
	 * for '--proxy-r2-filters (double x3)'
	 */
	private JTextField r2Field1;
	/**
	 * The JTextField that gets the double
	 * for '--proxy-r2-filters (double x3)'
	 */
	private JTextField r2Field2;
	/**
	 * The JTextField that gets the double
	 * for '--proxy-r2-filters (double x3)'
	 */
	private JTextField r2Field3;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-glm --covar (file)'
	 */
	private JCheckBox glmCheckBox;
	/**
	 * The JTextField that gets the file
	 * for '--proxy-glm --covar(file)'
	 */
	private JTextField glmField;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-drop'
	 */
	private JCheckBox dropCheckBox;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-dosage'
	 */
	private JCheckBox dosageCheckBox;
	/**
	 * The JCheckBox that flags the optional
	 * argument '--proxy-snp-filter (number)'
	 */
	private JCheckBox snpFilterCheckBox;
	/**
	 * The JTextField that gets the number for
	 * '--proxy-snp-filter (number)'
	 */
	private JTextField snpFilterField;
	/**
	 * 
	 * @param f
	 */
	public ProxyAssoc(FormCreator f) {
		super(f, name);
		
		snpRadioButton.setSelected(true);
		
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		//declare the elements
		snpRadioButton = new JRadioButton("Single reference SNP");
		snpField = new JTextField(10);
		allRadioButton = new JRadioButton("Multiple reference SNPs (all);  [Optional --proxy-list]");
		allField = new JTextField(15);
		allField.getDocument().addDocumentListener(validateBodyDL);
		BrowseButton allBrowse = new BrowseButton(allField);
		assocRadioButton = new JRadioButton("--proxy-assoc");
		glmCheckBox = new JCheckBox("--proxy-glm (optional --covar");
		glmField = new JTextField(15);
		BrowseButton glmBrowse = new BrowseButton(glmField);
		imputeRadioButton = new JRadioButton("--proxy-impute");
		dropCheckBox = new JCheckBox("--proxy-drop");
		dosageCheckBox = new JCheckBox("--proxy-dosage");
		windowCheckBox = new JCheckBox("--proxy-window");
		windowField= new JTextField(10);
		kbCheckBox = new JCheckBox("--proxy-kb");
		kbField = new JTextField(10);
		r2CheckBox = new JCheckBox("--proxy-r2");
		r2Field1 = new JTextField(4);
		r2Field2 = new JTextField(4);
		r2Field3 = new JTextField(4);
		snpFilterCheckBox = new JCheckBox("--proxy-maxsnp");
		snpFilterField = new JTextField(10);
		
		//link everything up
		assocRadioButton.addActionListener(validateBodyAL);
		imputeRadioButton.addActionListener(validateBodyAL);
		snpRadioButton.addActionListener(validateBodyAL);
		snpField.getDocument().addDocumentListener(validateBodyDL);
		bundel(snpRadioButton, snpField);
		allRadioButton.addActionListener(validateBodyAL);
		allField.getDocument().addDocumentListener(validateBodyDL);
		bundel(allRadioButton, allField, allBrowse);
		ButtonGroup g1 = new ButtonGroup();
		g1.add(snpRadioButton);
		g1.add(allRadioButton);
		snpRadioButton.setSelected(true);
		ItemListener assocAL = new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(assocRadioButton.isSelected()){
					glmCheckBox.setEnabled(true);
					dropCheckBox.setEnabled(true);
				}else{
					glmCheckBox.setSelected(false);
					glmCheckBox.setEnabled(false);
					dropCheckBox.setSelected(false);
					dropCheckBox.setEnabled(false);
				}
			}
		};
		assocRadioButton.addItemListener(assocAL);
		ItemListener imputeAL = new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(imputeRadioButton.isSelected()){
					dosageCheckBox.setEnabled(true);
				}else{
					dosageCheckBox.setEnabled(false);
					dosageCheckBox.setSelected(false);
				}
			}
		};
		imputeRadioButton.addItemListener(imputeAL);
		ButtonGroup g2 = new ButtonGroup();
		g2.add(assocRadioButton);
		g2.add(imputeRadioButton);
		assocRadioButton.setSelected(true);
		dosageCheckBox.setEnabled(false);
		dosageCheckBox.addActionListener(validateBodyAL);
		dropCheckBox.addActionListener(validateBodyAL);
		glmCheckBox.addActionListener(validateBodyAL);
		glmField.getDocument().addDocumentListener(validateBodyDL);
		bundel(glmCheckBox, glmField, glmBrowse);
		windowCheckBox.addActionListener(validateBodyAL);
		windowField.getDocument().addDocumentListener(validateBodyDL);
		bundel(windowCheckBox, windowField);
		kbCheckBox.addActionListener(validateBodyAL);
		kbField.getDocument().addDocumentListener(validateBodyDL);
		bundel(kbCheckBox, kbField);
		r2CheckBox.addActionListener(validateBodyAL);
		r2Field1.getDocument().addDocumentListener(validateBodyDL);
		r2Field2.getDocument().addDocumentListener(validateBodyDL);
		r2Field3.getDocument().addDocumentListener(validateBodyDL);
		bundel(r2CheckBox, new JTextField[] {r2Field1, r2Field2, r2Field3});
		snpFilterCheckBox.addActionListener(validateBodyAL);
		snpFilterField.getDocument().addDocumentListener(validateBodyDL);
		bundel(snpFilterCheckBox, snpFilterField);
		
		//lay things out
		JPanel body = new JPanel();
		body.setLayout(new GridBagLayout());
		GridBagConstraints c0 = new GridBagConstraints();
		c0.fill = GridBagConstraints.BOTH;
		c0.weightx = 1;
		//holds the required options
		JPanel req = new JPanel();
		req.setBorder(new TitledBorder("Required snp flags"));
		req.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.BOTH;
		c2.weightx = 1;
		JPanel temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		c1.gridx++;
		temp.add(snpRadioButton,c1);
		c1.weightx = 0.5;
		c1.gridx ++;
		temp.add(snpField,c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c2.gridy ++;
		req.add(temp, c2);
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		temp.add(allRadioButton, c1);
		c1.gridx = 1;
		temp.add(new JLabel("Optional list: (--proxy-list)"), c1);
		c1.gridx = 2;
		c1.weightx = 1;
		temp.add(allField, c1);
		c1.gridx = 3;
		c1.weightx = 0;
		temp.add(allBrowse, c1);
		c1.gridx = 4;
		c1.weightx = 1;
		temp.add(new JLabel(), c1);
		c2.gridy++;
		req.add(temp, c2);
		c0.gridy = 0;
		body.add(req, c0);
		
		JPanel req2 = new JPanel();
		req2.setBorder(new TitledBorder("Required test flags"));
		req2.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.BOTH;
		c2.gridy ++;
		c2.weightx = 1;
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		c1.gridx++;
		temp.add(assocRadioButton, c1);
		c1.gridx ++;
		c1.weightx = 1;
		temp.add(new JLabel(), c1);
		req2.add(temp, c2);
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		c1.gridx++;
		c1.insets = new Insets(0,20,0,0);  //left padding
		temp.add(glmCheckBox, c1);
		c1.gridx ++;
		c1.insets = new Insets(0,0,0,0);  //no padding
		temp.add(glmField, c1);
		c1.gridx ++;
		temp.add(glmBrowse, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(")"), c1);
		c2.gridy ++;
		req2.add(temp, c2);
		
		c2.gridx = 0;
		c2.gridy ++;
		c2.gridx = 0;
		c2.weightx = 1;
		c2.insets = new Insets(0,20,0,0);  //left padding
		req2.add(dropCheckBox, c2);
		c2.gridwidth = 3;
		c2.gridx = 0;
		c2.gridy ++;
		c2.insets = new Insets(0,0,0,0);  //no padding
		req2.add(imputeRadioButton, c2);
		c2.gridwidth = 3;
		c2.gridx = 0;
		c2.gridy ++;
		c2.insets = new Insets(0,20,0,0);  //left padding
		req2.add(dosageCheckBox, c2);
		c0.gridy ++;
		body.add(req2, c0);
		
		//holds the optional flags
		JPanel opt = new JPanel();
		opt.setBorder(new TitledBorder("Optional"));
		opt.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		//add window
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		temp.add(windowCheckBox, c1);
		c1.weightx = 0.5;
		c1.gridx = 1;
		temp.add(windowField, c1);
		c1.gridx = 2;
		c1.weightx = 1;
		//add this for padding
		temp.add(new JLabel(), c1);
		//add it to opt
		c.gridy ++;
		opt.add(temp, c);
		
		//add kb
		
		//create local panel
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		c1.gridx ++;
		temp.add(kbCheckBox, c1);
		c1.weightx = 0.5;
		c1.gridx ++;
		temp.add(kbField, c1);
		c1.gridx ++; 
		c1.weightx = 1;
		//add this for padding
		temp.add(new JLabel(), c1);
		//add it to opt
		c.gridy ++;
		opt.add(temp, c);
		
		//add r2
		//create local panel
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		temp.add(r2CheckBox, c1);
		c1.weightx = 0.25;
		c1.gridx = 1;
		temp.add(r2Field1, c1);
		c1.gridx ++;
		temp.add(r2Field2, c1);
		c1.gridx ++;
		temp.add(r2Field3, c1);
		c1.gridx ++;
		c1.weightx = 1;
		//add this for padding
		temp.add(new JLabel(), c1);
		//add it to opt
		c.gridy ++;
		opt.add(temp, c);
		
//		add kb
		//create local panel
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0;
		temp.add(snpFilterCheckBox, c1);
		c1.weightx = 0.5;
		c1.gridx = 1;
		temp.add(snpFilterField, c1);
		c1.gridx = 2; 
		c1.weightx = 1;
		//add this for padding
		temp.add(new JLabel(), c1);
		//add it to opt
		c.gridy ++;
		opt.add(temp, c);
		
		c0.gridy++;
		body.add(opt, c0);
		return body;
	}

	@Override
	protected void isBodyValid() {
		validBody = true;
		if((snpRadioButton.isSelected() && snpField.getText().matches("^\\s*$"))
			|| (windowCheckBox.isSelected() && !windowField.getText().matches("\\d+"))
			|| (kbCheckBox.isSelected() && !kbField.getText().matches("\\d+"))
			|| (r2CheckBox.isSelected() && !r2Field1.getText().matches("\\d*\\.?\\d+")
					&& !r2Field2.getText().matches("\\d*\\.?\\d+")
					&& !r2Field3.getText().matches("\\d*\\.?\\d+"))
			|| (snpFilterCheckBox.isSelected() && !snpFilterField.getText().matches("\\d+"))){
			validBody = false;
		}
		
		okForm();
	}

	@Override
	protected String processBody() {
		String ans = new String();
		if(assocRadioButton.isSelected()){
			ans += " --proxy-assoc";
		}
		if(imputeRadioButton.isSelected()){
			ans += " --proxy-impute";
		}
		if(snpRadioButton.isSelected()){
			ans += " " + snpField.getText();
		}
		if(allRadioButton.isSelected()){
			ans += " all";
			if(!allField.getText().matches("^\\s*$"))
				ans += " --proxy-list " + FileInfo.quote(allField.getText().trim());
		}
		if(glmCheckBox.isSelected()){
			ans += " --proxy-glm";
			if(!glmField.getText().matches("^\\s*$")){
				ans += " --covar " + glmField.getText();
			}
		}
		if(dropCheckBox.isSelected()){
			ans += " --proxy-drop";
		}
		if(dosageCheckBox.isSelected()){
			ans += " --proxy-dosage";
		}
		if(windowCheckBox.isSelected()){
			ans += " --proxy-window " + windowField.getText();
		}
		if(kbCheckBox.isSelected()){
			ans += " --proxy-kb " + kbField.getText();
		}
		if(r2CheckBox.isSelected()){
			ans += " --proxy-r2 " + r2Field1.getText() + " " + r2Field2.getText() + " " + r2Field3.getText();
		}
		if(snpFilterCheckBox.isSelected()){
			ans += " --proxy-maxsnp " + snpFilterField.getText();
		}
	
		ans.trim();
		return ans;
	}

}
