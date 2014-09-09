package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Create a form that allows the user to test for Allele
 * frequency. (plink command: --freq, --counts, --within)
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class AlleleFreq extends Form {

	/**
	 * The name of this form.
	 */
	static public String name  = "Allele Frequencies";	
	/**
	 * Buttons that allow the user to select the --freq 
	 */
	private JRadioButton freqButton;
	/**
	 * Buttons that allow the user to select the --count 
	 */
	private JRadioButton countButton;
	/**
	 * Allows the user to specify an optional --within
	 */
	private JRadioButton withinButton;
	/**
	 * Picks the --within file
	 */
	private JTextField withinField;
	/**
	 * button pick the file associated with --within
	 */
	private Form.BrowseButton pickWithin;
	
	/**
	 * creates a new Allele frequency form.
	 * @param f the FormCreator that created this.
	 */
	public AlleleFreq(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}
	
	/**
	 * Create the body for this form.
	 */
	@Override
	protected  JPanel createBody() {
		ActionListener validateAL = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				isBodyValid();
			}
		}; 
		DocumentListener validateDL = new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				isBodyValid();
			}
			public void removeUpdate(DocumentEvent e) {
				isBodyValid();
			}
		};
		
		JPanel body = new JPanel();
		
		//initialize the form
		freqButton = new JRadioButton("Frequencies (--freq)");
		freqButton.addActionListener(validateAL);
		freqButton.setSelected(true);
		validBody = true;
		countButton = new JRadioButton("Frequency Counts (--freq --counts)");
		countButton.addActionListener(validateAL);
		withinButton = new JRadioButton("Frequencies Stratify by (--freq --within)");
		withinButton.addActionListener(validateAL);
		withinField = new JTextField(30);
		withinField.getDocument().addDocumentListener(validateDL);
		pickWithin = new BrowseButton(withinField, null, null);
		
		//put the --freq and --count in a buton group so that
		//...the user can only use one or the other and is forced
		//...to select atleast one to excicute the form.
		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(freqButton);
		pickOne.add(countButton);
		pickOne.add(withinButton);
		
		//set the layout for the form
		body.setLayout(new GridBagLayout());
		//declare and initalze the constraits for this layout
		GridBagConstraints c = new GridBagConstraints();
		//expand all the components in this layout to fill all
		//...avaliable space
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		//add the frequency button to the layout with the given
		//...constraints
		body.add(freqButton,c);
		//move down before adding the next one
		c.gridy = 1;
		body.add(countButton,c);
		//move down again
		c.gridy = 2;
		body.add(bundel(withinButton, withinField, pickWithin), c);
		
		return body;
	}

	/**
	 * check to see if the commands selected are valid. Either
	 * --freq or --count must be selected and if --within is selected
	 * then there must be a valid file entered in the field.
	 */
	@Override
	protected void isBodyValid() {
		if( withinButton.isSelected() 
				&& withinField.getText().equals("")){
//			information.setText("When --within is selected you must specify a file.");
			validBody = false;
		}
		else 
			validBody = true;
		
		okForm();

	}

	/**
	 * Pull the command line from the form.
	 */
	@Override
	protected String processBody() {
		String ans = "--freq";
		if(countButton.isSelected())
			ans += " --counts";
		if(withinButton.isSelected())
			//since within takes a filename we need to throw
			//...quotes around it if we are in a window's os
			ans += " --within " + FileInfo.quote(withinField.getText());
		return ans;
	}

}
