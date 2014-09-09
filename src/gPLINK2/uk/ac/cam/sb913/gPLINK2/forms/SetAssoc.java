package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;
/**
 * A Set based association file.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class SetAssoc extends Form {
	/**
	 * The name of this form.
	 */
	public static String name = "Set-based Association Tests";
	private JRadioButton t2Button, setButton;
	private JCheckBox mpButton, 
				setMinButton, setMaxButton;
	private JTextField setText, mpText, setMinText, setMaxText;
	private BrowseButton setBrowse;
	
	/**
	 * Create the set based association form.
	 * @param mf The FormCreator that this is attached to.
	 */
	public SetAssoc(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		setMinText = new JTextField(10);
		setMinText.getDocument().addDocumentListener(validateBodyDL);
		setMaxText = new JTextField(10);
		setMaxText.getDocument().addDocumentListener(validateBodyDL);
		mpText = new JTextField(10);
		mpText.getDocument().addDocumentListener(validateBodyDL);
		setText = new JTextField(20);
		setText.getDocument().addDocumentListener(validateBodyDL);
		setBrowse = new BrowseButton(setText, ".set", "SET");
		
		t2Button = new JRadioButton("Hotelling's T(2) for C/C (--T2)");
		t2Button.addActionListener(validateBodyAL);
		setButton = new JRadioButton("Set-based association test (--assoc)");
		setButton.addActionListener(validateBodyAL);
		//pButton = new myCheckBox("Adaptive permutation mode (--perm)");
		mpButton = new JCheckBox("max(T) permutaion mode (--mperm)");
		mpButton.addActionListener(validateBodyAL);
		bundel(mpButton, mpText);
		mpButton.setSelected(true);
		mpButton.setEnabled(false);
		setMinButton = new JCheckBox("Minimum # SNPs in set (--set-min)");
		setMinButton.addActionListener(validateBodyAL);
		bundel(setMinButton, setMinText);
		setMaxButton = new JCheckBox("Maximum # SNPs in set (--set-max)"); 
		setMaxButton.addActionListener(validateBodyAL);
		bundel(setMaxButton, setMaxText);

		ButtonGroup g1 = new ButtonGroup();
		g1.add(t2Button);
		t2Button.setSelected(true);
		g1.add(setButton);
		
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = 2;
		body.add(new JLabel("SET file selection (--set)"),c);
		c.gridy = 1;
		body.add(bundel(setText, setBrowse), c);
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridy = 2;
		c.gridx = 0;
		body.add(new JLabel("max(T) permutaion mode (--mperm)"), c);
		c.weightx = 1;
		c.gridx = 1;
		body.add(mpText, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.weightx = 0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		body.add(t2Button, c);
		c.gridy = 5;
		body.add(setButton, c);
		c.gridy = 6;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridwidth = 1;
		c.gridy = 7;
		body.add(setMinButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(setMinText, c);
		c.gridy = 8;
		c.gridx = 0;
		c.weightx = 0;
		body.add(setMaxButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(setMaxText, c);
		
		return body;
	}

	@Override
	protected String processBody() {
		
		String ans = " --set " + FileInfo.quote(setText.getText());

		ans += " --mperm " + mpText.getText();
		
		if(t2Button.isSelected())
			ans += " --T2";
		if(setButton.isSelected())
			ans += " --assoc";
		
		if(setMinButton.isSelected())
			ans += " --set-min " + setMinText.getText();
		
		if(setMaxButton.isSelected())
			ans += " --set-max " + setMaxText.getText();

		return ans;
	}

	@Override
	protected void isBodyValid() {
		if(setText.getText().length() == 0){
//			information.setText("Must have a valid file for --set.");
			validBody = false;
		}else if(!(mpText.getText().matches("\\d+"))){
//			information.setText("Fill in a valid --mperm value.");
			validBody = false;
		} else
			validBody = true;
		
		okForm();
	}
}
