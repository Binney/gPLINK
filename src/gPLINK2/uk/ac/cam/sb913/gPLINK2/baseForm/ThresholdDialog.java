package gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.GenFileSet;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.Merge;

/**
 * An extention of JDialog to modify thresholds in the plink command.
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public class ThresholdDialog extends JDialog {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(ThresholdDialog.class);
	
	/************* Default parameters ***/
	/**
	 * The default minor allelic frequency
	 */
	private static Double MAF_DEFAULT;
	
	/**
	 * The default maximum minor allelic frequency
	 */
	private static Double MAXMAF_DEFAULT;
	
	/**
	 * The default maximum per-SNP missing
	 */
	private static Double GENO_DEFAULT;
	
	/**
	 * The default maximum per-person missing
	 */
	private static Double MIND_DEFAULT;
	
	/**
	 * The form that this dialog is attached to
	 */
	private Form parent;
	
	/**
	 * Fields for user entered thresholds
	 */
	private JTextField maf, maxMaf, geno,
		mind, hwe, mendell, mendell2;
	
	/**
	 * Buttons to flag user specified thresholds
	 */
	private JCheckBox mafButton, maxMafButton, genoButton, 
		hweButton, mendellButton, mindButton;
	
	/**
	 * Buttons to operate this dialog
	 */
	private JButton okButton, resetButton, cancelButton;
	
	private WindowListener properClosing = new WindowListener(){

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {//			put the ok buttons back on the parent form
			parent.openButtons();}
		public void windowClosing(WindowEvent e) {
//			put the ok buttons back on the parent form
			parent.openButtons();
		}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	};
	
	/**
	 * Process the dialog to command line.
	 */
	ActionListener process = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
			//if the flag is selected then put it in the threshold
			if(mafButton.isSelected())
				parent.addThreshold(" --maf " + maf.getText());
			if(maxMafButton.isSelected())
				parent.addThreshold(" --max-maf " + maxMaf.getText());
			if(genoButton.isSelected())
				parent.addThreshold(" --geno " + geno.getText());
			if(mindButton.isSelected())
				parent.addThreshold(" --mind " + mind.getText());
			if(hweButton.isSelected())
				parent.addThreshold(" --hwe " + hwe.getText());
			if(mendellButton.isSelected())
				parent.addThreshold(" --me " + mendell.getText() + " " + mendell2.getText());
			//We are done!
			dispose();
		}
	};
	
	private void validateButtons(){
		if(mafButton.isSelected() &&
				!(maf.getText().matches("[0\\s].\\d*")|| 
				maf.getText().matches("[10]")|| 
				maf.getText().matches("1\\.0*"))){
			okButton.setEnabled(false);
			return;
			
		}
		if(maxMafButton.isSelected() &&
				!(maxMaf.getText().matches("[0\\s].\\d*")|| 
				maxMaf.getText().matches("[10]")|| 
				maxMaf.getText().matches("1\\.0*"))){
			okButton.setEnabled(false);
			return;
			
		}
		if(genoButton.isSelected() &&
				!(geno.getText().matches("[0\\s].\\d*")|| 
				geno.getText().matches("[10]")|| 
				geno.getText().matches("1\\.0*"))){
			okButton.setEnabled(false);
			return;
		}
		if(mindButton.isSelected() &&
				!(mind.getText().matches("[0\\s].\\d*")|| 
				mind.getText().matches("[10]")|| 
				mind.getText().matches("1\\.0*"))){
			okButton.setEnabled(false);
			return;
		}
		if(hweButton.isSelected() &&
				!(hwe.getText().matches("[0\\s].\\d*")|| 
				hwe.getText().matches("[10]")|| 
				hwe.getText().matches("1\\.0*"))){
			okButton.setEnabled(false);
			return;
			
		}
		if(mendellButton.isSelected() &&(
				!(mendell.getText().matches("[0\\s].\\d*")|| 
					mendell.getText().matches("[10]")|| 
					mendell.getText().matches("1\\.0*")) ||
					!(mendell2.getText().matches("[0\\s].\\d*")|| 
							mendell2.getText().matches("[10]")|| 
							mendell2.getText().matches("1\\.0*")))
					){
				okButton.setEnabled(false);
				return;
			
		}
		okButton.setEnabled(true);
	}
	
	ItemListener validateOK = new ItemListener(){

		public void itemStateChanged(ItemEvent e) {
			
			logger.info("[validateOK]");
			validateButtons();
		}
		
	};
	
	DocumentListener validateDL = new DocumentListener(){

		public void changedUpdate(DocumentEvent e) {
			}

		public void insertUpdate(DocumentEvent e) {
			validateButtons();
		}

		public void removeUpdate(DocumentEvent e) {
			validateButtons();
		}
	};
	
	/**
	 * Reset all the defaults
	 */
	ActionListener reset = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			maf.setText(MAF_DEFAULT.toString());
			maxMaf.setText(MAXMAF_DEFAULT.toString());
			geno.setText(GENO_DEFAULT.toString());
			mind.setText(MIND_DEFAULT.toString());
			hwe.setText("");
			mendell.setText("");
			mendell2.setText("");
		}
		
	};
	
	/**
	 * Cancel the dailog and throw it away without changing things
	 */
	ActionListener cancel = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};
	
	/**
	 * Initalize all the variables and fields.
	 *
	 */
	
	private void initalize(){
		//set the defaults depending on what test we are running
		if(parent.getClass().equals(GenFileSet.class)
				|| parent.getClass().equals(Merge.class)){
			MAF_DEFAULT = 0.0;
			GENO_DEFAULT = 1.0;
			MIND_DEFAULT = 1.0;
		} else {
			MAF_DEFAULT = 0.01;
			GENO_DEFAULT = 0.1;
			MIND_DEFAULT = 0.1;
		}
		MAXMAF_DEFAULT = 1.0;
		
		//initalize the fields
		maf = new JTextField(MAF_DEFAULT.toString(), 10);
		maf.getDocument().addDocumentListener(validateDL);
		maxMaf = new JTextField(MAXMAF_DEFAULT.toString(), 10);
		maxMaf.getDocument().addDocumentListener(validateDL);
		geno = new JTextField(GENO_DEFAULT.toString(), 10);
		geno.getDocument().addDocumentListener(validateDL);
		hwe = new JTextField(10);
		hwe.getDocument().addDocumentListener(validateDL);
		mind = new JTextField(MIND_DEFAULT.toString(), 10);
		mind.getDocument().addDocumentListener(validateDL);
		mendell = new JTextField(10);
		mendell.getDocument().addDocumentListener(validateDL);
		mendell2 = new JTextField(10);
		mendell2.getDocument().addDocumentListener(validateDL);
		
		mafButton = new JCheckBox("Minor allele frequency " +
				"(--maf)");
		mafButton.addItemListener(validateOK);
		Form.bundel(mafButton, maf);
		maxMafButton = new JCheckBox("Maximum minor allele " +
				"frequency (--max-maf)");
		maxMafButton.addItemListener(validateOK);
		Form.bundel(maxMafButton, maxMaf);
		genoButton = new JCheckBox("Maximum SNP missingness " +
				"rate (--geno)");
		genoButton.addItemListener(validateOK);
		Form.bundel(genoButton, geno);
		hweButton = new JCheckBox("Hardy Weinberg equilibrium " +
				"(--hwe)");
		hweButton.addItemListener(validateOK);
		Form.bundel(hweButton, hwe);
		
		JTextField [] temp = {mendell, mendell2};
		mendellButton = new JCheckBox("Mendel errors (--me)");
		mendellButton.addItemListener(validateOK);
		Form.bundel(mendellButton, temp);
		
		mindButton = new JCheckBox("Maximum individual " +
				"missingness rate (--mind)");
		mindButton.addItemListener(validateOK);
		Form.bundel(mindButton, mind);
		
	}
	
	/**
	 * Create a dialog to allow the user to specify threshold values
	 * @param givenParent form that this dialog is anchored to
	 */
	public ThresholdDialog(Form givenParent){
		super(givenParent, "Threshold");
		parent = givenParent;
		
		parent.closeButtons();
		parent.resetThreshold();
		initalize();
		
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel mafPanel = new JPanel();
		mafPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		mafPanel.add(mafButton);
		mafPanel.add(maf);
		
		JPanel maxPanel = new JPanel();
		maxPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		maxPanel.add(maxMafButton);
		maxPanel.add(maxMaf);
		
		JPanel genoPanel = new JPanel();
		genoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		genoPanel.add(genoButton);
		genoPanel.add(geno);
		
		JPanel mindPanel = new JPanel();
		mindPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		mindPanel.add(mindButton);
		mindPanel.add(mind);
		
		JPanel hwePanel = new JPanel();
		hwePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		hwePanel.add(hweButton);
		hwePanel.add(hwe);
		
		JPanel mendPanel = new JPanel();
		mendPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		mendPanel.add(mendellButton);
		mendPanel.add(new JLabel("(family)"));
		mendPanel.add(mendell);
		mendPanel.add(new JLabel("(snp)"));
		mendPanel.add(mendell2);

		JPanel buttonPanel = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(process);
		buttonPanel.add(okButton);
		resetButton = new JButton("Restore Default");
		resetButton.addActionListener(reset);
		buttonPanel.add(resetButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(cancel);
		buttonPanel.add(cancelButton);
		
		
		getContentPane().add(mafPanel);
		getContentPane().add(maxPanel);
		getContentPane().add(genoPanel);
		getContentPane().add(mindPanel);
		getContentPane().add(hwePanel);
		getContentPane().add(mendPanel);
		getContentPane().add(buttonPanel);
		
		this.addWindowListener(properClosing);
		
		pack();
		setVisible(true);
	}
}
