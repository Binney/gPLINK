package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class CondHaploAssoc extends Form {

	public static String name = "Conditional Haplotype Association";
	
	/**
	 * The JRadioButton that flags the
	 * '--independent-effect (SNP, SNP, ...)' option
	 */
	private JRadioButton indpEffRadioButton;
	/**
	 * The JTextField that holds the snp list
	 * for the '--independent-effect (SNP, SNP, ...) option
	 */
	private JTextField indpEffField;
	/**
	 * The JRadioButton that flags the
	 * '--control (SNP/Haplo, SNP/Haplo, ...)' option
	 */
	private JRadioButton contRadioButton;
	/**
	 * The JTextField that holds the snp/haplotype list
	 * for the '--control (SNP/Haplo, SNP/Haplo, ...) option
	 */
	private JTextField contField;
	/**
	 * The JRadioButton that flags the
	 * '--specific-haplotype (Haplo)' option
	 */
	private JRadioButton specHaploRadioButton;
	/**
	 * The JTextField that holds the haplotype
	 * for the 'specific-haplotype (Haplo)' option
	 */
	private JTextField specHaploField;
	/**
	 * The JRadioButton that flags the
	 * '--alt-snp (SNP, ...)' option
	 */
	private JRadioButton altSnpRadioButton;
	/**
	 * The JTextField that holds the SNP9s)
	 * for the '--alt-snp (SNP, ...)' option
	 */
	private JTextField altSnpField;
	/**
	 * The JRadioButton that flags the
	 * '--null-snp (SNP, ...)' option
	 */
	private JRadioButton nullSnpRadioButton;
	/**
	 * The JTextField that holds the SNP(s)
	 * for the '--null-snp (SNP, ...)' option
	 */
	private JTextField nullSnpField;
	/**
	 * The JRadioButton that flags the
	 * '--alt-group (Haplo, ...)' option
	 */
	private JRadioButton altGroupRadioButton;
	/**
	 * The JTextField that holds the Haplotype(s)
	 * for the '--alt-group (Haplo, ...)' option
	 */
	private JTextField altGroupField;
	/**
	 * The JRadioButton that flags the 
	 * '--null-group (Haplo, ...)' option
	 */
	private JRadioButton nullGroupRadioButton;
	/**
	 * The JTextField that holds the Haplotype(s)
	 * for the '--null-group (Haplo, ...)' option
	 */
	private JTextField nullGroupField;
	/**
	 * The JRadioButton that flags the
	 * '--test-snp (SNP, ...)' option
	 */
	private JRadioButton testSnpRadioButton;
	/**
	 * The JTextField that holds the SNP(s)
	 * for the '--test-snp (SNP, ...)' option
	 */
	private JTextField testSnpField;
	

	/**
	 * The JTextField that holds the SNP(s)
	 * for the '--chap --hap-snps (SNP-SNP, SNP,...)' 
	 */
	private JTextField hapSnpField;
	/**
	 * The JCheckBox that flags the
	 * '--mhf (double)' option
	 */
	private JCheckBox mhfCheckBox;
	/**
	 * The JTextField that holds the double
	 * for the '--mhf (double)' option
	 */
	private JTextField mhfField;
	/**
	 * The JCheckBox that flags the
	 * '--covar (file)' option
	 */
	private JCheckBox covCheckBox;
	/**
	 * The JTextField that holds the file
	 * for the 'covar (file)' option
	 */
	private JTextField covField;
	/**
	 * The BrowseButton that selects the file
	 * for the 'covar (file)' option
	 */
	private BrowseButton covButton;
	
	/**
	 * The JTextField that holds the SNP
	 * for the '--condition (SNP)' option
	 */
	private JTextField condField;
	
	
	
	public CondHaploAssoc(FormCreator f){
		super(f, name);
		
		pack();
		setVisible(true);
	}
	@Override
	protected JPanel createBody() {
		
		ItemListener selectReq = new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				
				if(((JRadioButton) arg0.getItem()).isSelected() == false)
					return;
				
				if(arg0.getItem().equals(altSnpRadioButton)
						|| arg0.getItem().equals(nullSnpRadioButton)){
					boolean altFlag = altSnpRadioButton.isSelected();
					boolean nullFlag = nullSnpRadioButton.isSelected();
					deselectAll(altSnpRadioButton, nullSnpRadioButton);
					altSnpRadioButton.setSelected(altFlag);
					nullSnpRadioButton.setSelected(nullFlag);
				} else if(arg0.getItem().equals(altGroupRadioButton)
						|| arg0.getItem().equals(nullGroupRadioButton)){
					boolean altFlag = altGroupRadioButton.isSelected();
					boolean nullFlag = nullGroupRadioButton.isSelected();
					deselectAll(altGroupRadioButton, nullGroupRadioButton);
					altGroupRadioButton.setSelected(altFlag);
					nullGroupRadioButton.setSelected(nullFlag);
				} else{
					deselectAll((JRadioButton) arg0.getItem(), new JRadioButton());
				}
			}
			private void deselectAll(JRadioButton temp1, JRadioButton temp2){
				if(!temp1.equals(indpEffRadioButton)
						&& !temp2.equals(indpEffRadioButton))
					indpEffRadioButton.setSelected(false);
				if(!temp1.equals(contRadioButton)
						&& !temp2.equals(contRadioButton))
					contRadioButton.setSelected(false);
				if(!temp1.equals(specHaploRadioButton)
						&& !temp2.equals(specHaploRadioButton))
					specHaploRadioButton.setSelected(false);
				if(!temp1.equals(altSnpRadioButton)
						&& !temp2.equals(altSnpRadioButton))
					altSnpRadioButton.setSelected(false);
				if(!temp1.equals(nullSnpRadioButton)
						&& !temp2.equals(nullSnpRadioButton))
					nullSnpRadioButton.setSelected(false);
				if(!temp1.equals(altGroupRadioButton)
						&& !temp2.equals(altGroupRadioButton))
					altGroupRadioButton.setSelected(false);
				if(!temp1.equals(nullGroupRadioButton)
						&& !temp2.equals(nullGroupRadioButton))
					nullGroupRadioButton.setSelected(false);
				if(!temp1.equals(testSnpRadioButton)
						&& !temp2.equals(testSnpRadioButton))
				testSnpRadioButton.setSelected(false);
			}
		};
		
		JPanel body = new JPanel();
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		
		hapSnpField = new JTextField(15);
		hapSnpField.getDocument().addDocumentListener(validateBodyDL);
		JPanel temp = new JPanel();
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx ++;
		temp.add(new JLabel("List of SNPs to phase: (--chap --hap-snps)"));
		c1.weightx=0.5;
		c1.gridx++;
		temp.add(hapSnpField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy = 0;
		body.add(temp, c);
		
		indpEffRadioButton = new JRadioButton("Test of independent SNP effects (--indpendent-effect)");
		indpEffField = new JTextField(15);
		indpEffField.getDocument().addDocumentListener(validateBodyDL);
		indpEffRadioButton.addActionListener(validateBodyAL);
		indpEffRadioButton.addItemListener(selectReq);
		bundel(indpEffRadioButton, indpEffField);
		temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.fill = GridBagConstraints.BOTH;
		temp.add(indpEffRadioButton, c1);
		c1.gridx ++;
		c1.weightx=0.5;
		temp.add(indpEffField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		contRadioButton = new JRadioButton("Control SNPs/haplotypes (--control)");
		contField = new JTextField(15);
		contField.getDocument().addDocumentListener(validateBodyDL);
		contRadioButton.addActionListener(validateBodyAL);
		contRadioButton.addItemListener(selectReq);
		bundel(contRadioButton, contField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(contRadioButton, c1);
		c1.gridx ++;
		c1.weightx=0.5;
		temp.add(contField,c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		specHaploRadioButton = new JRadioButton("Test specific haplotypes (--specific-haplotype)");
		specHaploField = new JTextField(15);
		specHaploField.getDocument().addDocumentListener(validateBodyDL);
		specHaploRadioButton.addActionListener(validateBodyAL);
		specHaploRadioButton.addItemListener(selectReq);
		bundel(specHaploRadioButton, specHaploField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(specHaploRadioButton, c1);
		c1.weightx = 0.5;
		c1.gridx ++;
		temp.add(specHaploField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		altSnpRadioButton = new JRadioButton("Specify alternate model SNPs (--alt-snp)");
		altSnpField = new JTextField(15);
		altSnpField.getDocument().addDocumentListener(validateBodyDL);
		altSnpRadioButton.addActionListener(validateBodyAL);
		altSnpRadioButton.addItemListener(selectReq);
		bundel(altSnpRadioButton, altSnpField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(altSnpRadioButton, c1);
		c1.weightx = 0.5;
		c1.gridx ++;
		temp.add(altSnpField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		nullSnpRadioButton = new JRadioButton("Specify null model SNPs (--null-snp)");
		nullSnpField = new JTextField(15);
		nullSnpField.getDocument().addDocumentListener(validateBodyDL);
		nullSnpRadioButton.addActionListener(validateBodyAL);
		nullSnpRadioButton.addItemListener(selectReq);
		bundel(nullSnpRadioButton, nullSnpField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(nullSnpRadioButton, c1);
		c1.weightx= 0.5;
		c1.gridx ++;
		temp.add(nullSnpField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		altGroupRadioButton = new JRadioButton("Specify alternate model haplogroups (--alt-group)");
		altGroupField = new JTextField(15);
		altGroupField.getDocument().addDocumentListener(validateBodyDL);
		altGroupRadioButton.addActionListener(validateBodyAL);
		altGroupRadioButton.addItemListener(selectReq);
		bundel(altGroupRadioButton, altGroupField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(altGroupRadioButton, c1);
		c1.weightx = 0.5;
		c1.gridx++;
		temp.add(altGroupField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		nullGroupRadioButton = new JRadioButton("Specify null model haplogroups (--null-group)");
		nullGroupField = new JTextField(15);
		nullGroupField.getDocument().addDocumentListener(validateBodyDL);
		nullGroupRadioButton.addActionListener(validateBodyAL);
		nullGroupRadioButton.addItemListener(selectReq);
		bundel(nullGroupRadioButton, nullGroupField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(nullGroupRadioButton, c1);
		c1.weightx=0.5;
		c1.gridx++;
		temp.add(nullGroupField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		testSnpRadioButton = new JRadioButton("Test conditioning SNP effects (--test-snp)");
		testSnpField = new JTextField(15);
		condField = new JTextField(15);
		condField.getDocument().addDocumentListener(validateBodyDL);
		testSnpField.getDocument().addDocumentListener(validateBodyDL);
		testSnpRadioButton.addActionListener(validateBodyAL);
		testSnpRadioButton.addItemListener(selectReq);
		bundel(testSnpRadioButton, new JTextField [] {testSnpField, condField});
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(testSnpRadioButton, c1);
		c1.weightx=0.5;
		c1.gridx++;
		temp.add(testSnpField, c1);
		c1.weightx = 0;
		c1.gridx ++;
		temp.add(new JLabel("List of conditioning SNPs (--condition)"),c1);
		c1.weightx = 0.5;
		c1.gridx ++;
		temp.add(condField);
		c1.gridx ++;
		c1.weightx = 1;
		temp.add(new JLabel(), c1);
		c.gridy ++;
		body.add(temp, c);
		
		JPanel opt = new JPanel();
		opt.setLayout(new GridBagLayout());
		opt.setBorder(new TitledBorder("Optional"));
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.BOTH;
		c2.weightx = 1;
		mhfCheckBox = new JCheckBox("Minimum haplotype frequency (--mhf)");
		mhfField = new JTextField(15);
		mhfField.getDocument().addDocumentListener(validateBodyDL);
		mhfCheckBox.addActionListener(validateBodyAL);
		bundel(mhfCheckBox, mhfField);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(mhfCheckBox, c1);
		c1.weightx=0.5;
		c1.gridx++;
		temp.add(mhfField, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c2.gridy ++;
		opt.add(temp, c2);
		
		covCheckBox = new JCheckBox ("Covariates (--covar)");
		covField = new JTextField(15);
		covButton = new BrowseButton(covField);
		covField.getDocument().addDocumentListener(validateBodyDL);
		covCheckBox.addActionListener(validateBodyAL);
		bundel(covCheckBox, covField, covButton);
		temp = new JPanel();
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		temp.setLayout(new GridBagLayout());
		c1.gridx = 0;
		temp.add(covCheckBox, c1);
		c1.weightx=0.5;
		c1.gridx++;
		temp.add(covField, c1);
		c1.weightx=0;
		c1.gridx++;
		temp.add(covButton, c1);
		c1.weightx = 1;
		c1.gridx ++;
		temp.add(new JLabel(), c1);
		c2.gridy ++;
		opt.add(temp, c2);
		
		c.gridy ++;
		body.add(opt, c);
		
		return body;
	}

	@Override
	protected void isBodyValid() {

		validBody = true;
		
		if(hapSnpField.getText().matches("^\\s*$")
			|| (indpEffRadioButton.isSelected() && indpEffField.getText().matches("^\\s*$"))
			|| (contRadioButton.isSelected() && contField.getText().matches("^\\s*$"))
			|| (specHaploRadioButton.isSelected() && specHaploField.getText().matches("^\\s*$"))
			|| (altSnpRadioButton.isSelected() && altSnpField.getText().matches("^\\s*$"))
			|| (nullSnpRadioButton.isSelected() && nullSnpField.getText().matches("^\\s*$"))
			|| (altGroupRadioButton.isSelected() && altGroupField.getText().matches("^\\s*$"))
			|| (nullGroupRadioButton.isSelected() && nullGroupField.getText().matches("^\\s*$"))
			|| (testSnpRadioButton.isSelected() && (testSnpField.getText().matches("^\\s*$") || condField.getText().matches("^\\s*$")))
			|| (mhfCheckBox.isSelected() && !mhfField.getText().matches("\\d*\\.?\\d+"))
			|| (covCheckBox.isSelected() && covField.getText().matches("^\\s+$")))
			validBody = false;
		
		okForm();
	}

	@Override
	protected String processBody() {
		String ans = "--chap --hap-snps " + hapSnpField.getText();
		if(indpEffRadioButton.isSelected())
			ans += " --independent-effect " + indpEffField.getText();
		if(contRadioButton.isSelected())
			ans += " --control " + contField.getText();
		if(specHaploRadioButton.isSelected())
			ans += " --specific-haplotype " + specHaploField.getText();
		if(altSnpRadioButton.isSelected())
			ans += " --alt-snp " + altSnpField.getText();
		if(nullSnpRadioButton.isSelected())
			ans += " --null-snp " + nullSnpField.getText();
		if(altGroupRadioButton.isSelected())
			ans += " --alt-group " + altGroupField.getText();
		if(nullGroupRadioButton.isSelected())
			ans += " --null-group " + nullGroupField.getText();
		if(testSnpRadioButton.isSelected())
			ans += " --test-snp " + testSnpField.getText() + " --condition " + condField.getText();
		if(mhfCheckBox.isSelected())
			ans += " --mhf " + mhfField.getText();
		if(covCheckBox.isSelected())
			ans += " --covar " + FileInfo.quote(covField.getText());
		
		return ans.trim();
	}


}
