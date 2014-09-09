package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Creates a form that does a basic --assoc test
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class AlleleAssoc extends Form {
	/**
	 * The name of the form
	 */
	public static String name = "Allelic Association Tests";
	/**
	 * Flag the --perm option
	 */
	private JCheckBox permButton;
	/**
	 * Flag the --mperm option
	 */
	private JCheckBox mpermButton;
	/**
	 * Flag the ci option
	 */
	private JCheckBox ciButton;
	/**
	 * Flag the --adjust button
	 */
	private JCheckBox adjustButton;
	
	//user input parameters
	/**
	 * Allow the user to set the --ci parameter
	 */
	private JTextField ciText;
	/**
	 * Allow the user to set the --mperm perameter
	 */
	private JTextField mpermText;
	/**
	 * The default ci parameter is 0.95
	 */
	static private Double CI_DEFAULT = new Double(0.95);
	/**
	 * The default mperm perameter is 1000
	 */
	static private Integer MPERM_DEFAULT = new Integer(1000);
	
	/**
	 * Create and show an allelic assocation test form.
	 * @param f the FormCreator that created this.
	 */
	public AlleleAssoc(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	/**
	 * create the body specific to this form.
	 */
	@Override
	protected JPanel createBody() {
		validBody = true;
		//inialize everything
		JPanel body = new JPanel();
		ciText = new JTextField(CI_DEFAULT.toString(), 4);
		ciText.getDocument().addDocumentListener(validateBodyDL);
		mpermText = new JTextField(MPERM_DEFAULT.toString(), 4);
		mpermText.getDocument().addDocumentListener(validateBodyDL);

		ciButton = new JCheckBox("Confidence interval, C/C only (--ci)");
		ciButton.addActionListener(validateBodyAL);
		mpermButton = new JCheckBox("max(T) permutation mode (--mperm)");
		mpermButton.addActionListener(validateBodyAL);
		permButton = new JCheckBox("Adaptive permutation mode (--perm)");
		permButton.addActionListener(validateBodyAL);
		adjustButton = new JCheckBox("Adjusted p-values (--adjust)");
		adjustButton.addActionListener(validateBodyAL);
		
		//toggle the mperm and perm options but also let the
		//...user select neither
		ActionListener toggle = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if(e.getSource().equals(mpermButton)
					&& mpermButton.isSelected())
					permButton.setSelected(false);
				if(e.getSource().equals(permButton)
					&& permButton.isSelected())
					mpermButton.setSelected(false);
			}
		};
		permButton.addActionListener(toggle);
		mpermButton.addActionListener(toggle);
		
		//set the layout for this body
		body.setLayout(new GridBagLayout());
		//declare and initalze the constraits for this layout
		GridBagConstraints c = new GridBagConstraints();
		//expand all the components in this layout to fill all
		//...avaliable horizontal space
		//c.fill = GridBagConstraints.HORIZONTAL;	
		c.anchor = GridBagConstraints.LINE_START;
		//resize the width to fill all availible space
		c.weightx = 1;
		//add a label to tell the user what form you are using
		body.add(new JLabel("Basic allelic single SNP " +
				"association [C/C or QT] (--assoc)"), c);
		//add the next component to the next level
		c.gridy = 1;
		//create a new panel for the --ci option to make
		//...the layout pretty
		//add the --ci <Double> to the form
		body.add(Form.bundel(ciButton, ciText), c);
		//move down
		c.gridy = 2;
		//add the --adjust option to the form
		body.add(adjustButton, c);
		//move down
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		//create a new panel to group the permulation options
		JPanel constraits = new JPanel();
		//tell the user what's in here
		constraits.setBorder(new TitledBorder("Permutation options "));
		//use a simple layout
		constraits.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		//c1.fill = GridBagConstraints.BOTH;
		c1.anchor = GridBagConstraints.LINE_START;
		//add the --perm option
		constraits.add(permButton, c1);
		c1.gridy = 1;
		c1.weightx = 1;
		//ya we could have used gridbag but desided to nest another jpanel
		//...if it bugs you change it we also tie the
		//...enabled state of mpermtext to it's button
		constraits.add(Form.bundel(mpermButton, mpermText), c1);
		//add the final componet
		body.add(constraits, c);
		return body;
	}

	/**
	 * Pull the commandline from this form
	 */
	@Override
	protected String processBody() {
		String ans = "--assoc";
		if(ciButton.isSelected())
			ans +=" --ci " + ciText.getText();
		if(mpermButton.isSelected())
			ans += " --mperm " + mpermText.getText();
		if(permButton.isSelected())
			ans += " --perm";
		if(adjustButton.isSelected())
			ans += " --adjust";
		return ans;
	}

	/**
	 * Return true. --assoc is a vaild command by itself.
	 */
	@Override
	protected void isBodyValid() {
		if(ciButton.isSelected() && !ciText.getText().matches("\\d*\\.?\\d+")){
			validBody = false;
		} else if(mpermButton.isSelected() && !mpermText.getText().matches("\\d*\\.?\\d+")){
			validBody = false;
		} else
			validBody = true;
		okForm();
	}	
}
