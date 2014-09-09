package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * A form that allows the user to create a Haplotype-based C/C test plink
 * command line
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class HapCCAssoc extends Form{
	/**
	 * The name of this form.
	 */
	public static String name = "Haplotype-based C/C tests";
	private JRadioButton hapListButton, hapWindowButton;
	private JTextField hapListText, hapWindowText;
	private BrowseButton hapListBrowse;
	
	/**
	 * Create a Haplotype-based C/C test
	 * @param f FormCreator the form is attached to
	 */
	public HapCCAssoc(FormCreator f) {
		super(f,name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		
		JPanel body = new JPanel();
		
		hapWindowText = new JTextField(10);
		hapWindowText.getDocument().addDocumentListener(validateBodyDL);
		hapListText = new JTextField(20);
		hapListText.getDocument().addDocumentListener(validateBodyDL);
		
		hapListBrowse = new BrowseButton(hapListText, 
				".hlist", "Haplotype list (*.hlist)");
		
		hapWindowButton = new JRadioButton("Sliding window (--hap-assoc --hap-window)"); 
		hapWindowButton.addActionListener(validateBodyAL);
		bundel(hapWindowButton, hapWindowText);
		
		hapListButton = new JRadioButton("Haplotype list (--hap-assoc --hap)");
		bundel(hapListButton, hapListText, hapListBrowse);
		hapListButton.setSelected(true);
		hapListButton.addActionListener(validateBodyAL);
		
		ButtonGroup options = new ButtonGroup();
		options.add(hapWindowButton);
		options.add(hapListButton);
		
		body.setLayout(new GridBagLayout());
		body.setBorder(new TitledBorder("Haplotype specification"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		body.add(hapListButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(hapListText,c);
		c.weightx = 0;
		c.gridx = 2;
		body.add(hapListBrowse, c);
		c.gridy = 1;
		c.gridx = 0;
		body.add(hapWindowButton, c);
		c.gridwidth = 2;
		c.gridx = 1;
		c.weightx = 1;
		body.add(hapWindowText, c);
		
		return body;
	}

	@Override
	protected void isBodyValid() {
		if( hapListButton.isSelected() 
				&& hapListText.getText().length() == 0){
//			information.setText("Must select a file associated with the --hap flag.");
			validBody =  false;
			
		}
		if( hapWindowButton.isSelected() 
				 && !hapWindowText.getText().matches("^\\d+$")){
//			information.setText("Must enter a numeric value associated with " +
//					"the --hap-window flag.");
			validBody = false;
		}
		validBody = true;
		
		okForm();
	}

	@Override
	protected String processBody() {
		String ans = " --hap-assoc";
		if(hapListButton.isSelected())
			ans += " --hap " + FileInfo.quote(hapListText.getText());
		if(hapWindowButton.isSelected())
			ans += " --hap-window " + hapWindowText.getText();
		return ans;
	}

}
