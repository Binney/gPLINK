package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;



import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;


/**
 * Form for the clustering command --cluster
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public class Clustering extends Form {

	static public String name = "Clustering";
	//There are lots of options for this filter
	private JCheckBox ppcButton, ccButton, kButton, ibmButton, 
		matchButton, matchTButton,
		qmatchButton, mcButton, mccButton;
	
	private JTextField readGText, ppcText, pibsGText, mcText, 
		mcc1Text, mcc2Text, kText, ibmText, 
		matchText, matchTText, qmatchText, qtText;
	
	private BrowseButton readGBrowse, matchBrowse, matchTBrowse, 
		qmatchBrowse, qmatchQTBrowse;
	
	
	static Double PIBS_G_DEFAULT = new Double(500);
	static Double PCC_DEFAULT = new Double(0);
		
	
		
	/**
	 * create a show a new Clustering Form
	 * @param f the FormCreator that created this.
	 */
	public Clustering(FormCreator f) {
		super(f, name);
		//doens't make any sence to have the filter or threshold
		//...optionsn turned on for this form
		super.filterButton.setEnabled(false);
		super.thresholdButton.setEnabled(false);
		//this.buttons.disableFT();
		pack();
		setVisible(true);
	}

	/**
	 * create the body of the form
	 *
	 */
	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		readGText = new JTextField(10);
		readGText.getDocument().addDocumentListener(validateBodyDL);
		readGText.setEditable(true);
		ppcText = new JTextField(PCC_DEFAULT.toString(), 4);
		ppcText.getDocument().addDocumentListener(validateBodyDL);
		pibsGText = new JTextField(PIBS_G_DEFAULT.toString(), 4);
		pibsGText.getDocument().addDocumentListener(validateBodyDL);
		mcText = new JTextField(4);
		mcText.getDocument().addDocumentListener(validateBodyDL);
		mcc1Text = new JTextField(4);
		mcc1Text.getDocument().addDocumentListener(validateBodyDL);
		mcc2Text = new JTextField(4);
		mcc2Text.getDocument().addDocumentListener(validateBodyDL);
		kText = new JTextField(4);
		kText.getDocument().addDocumentListener(validateBodyDL);
		ibmText = new JTextField(4); 
		ibmText.getDocument().addDocumentListener(validateBodyDL);
		matchText = new JTextField(10);
		matchText.getDocument().addDocumentListener(validateBodyDL);
		matchTText = new JTextField(10);
		matchTText.getDocument().addDocumentListener(validateBodyDL);
		qmatchText = new JTextField(10);
		qmatchText.getDocument().addDocumentListener(validateBodyDL);
		qtText = new JTextField(10);
		qtText.getDocument().addDocumentListener(validateBodyDL);
		
		readGBrowse = new BrowseButton(readGText, ".genome", "GENOME File");
		matchBrowse = new BrowseButton(matchText, ".match", "MATCH File");
		matchBrowse.setEnabled(false);
		matchTBrowse = new BrowseButton(matchTText, ".bt", "MATCH-TYPE");
		matchTBrowse.setEnabled(false);
		qmatchBrowse = new BrowseButton(qmatchText, ".match", "MATCH");
		qmatchBrowse.setEnabled(false);
		qmatchQTBrowse = new BrowseButton(qtText, ".qt", "QT");
		qmatchQTBrowse.setEnabled(false);
		
		JTextField [] temp = {ppcText, pibsGText};
		ppcButton = new JCheckBox("Pairwise population concordance threshold (--pcc)");
		bundel(ppcButton, temp, new BrowseButton[]{});
		
		ibmButton = new JCheckBox("Identity by missingness (--ibm)");
		ibmButton.addActionListener(validateBodyAL);
		bundel(ibmButton, ibmText);
		
		ccButton = new JCheckBox("Phenotype constraint (-cc)");
		ccButton.addActionListener(validateBodyAL);
		
		kButton = new JCheckBox("Number of clusters (--K)");
		kButton.addActionListener(validateBodyAL);
		bundel(kButton, kText);
		
		matchButton = new JCheckBox("External categorical matching " +
				"(--match)"); 
		matchButton.addActionListener(validateBodyAL);
		bundel(matchButton, matchText, matchBrowse);
		
		matchTButton = new JCheckBox("Positive/negative matches" +
				"(--match-type)"); 
		matchTButton.addActionListener(validateBodyAL);
		bundel(matchTButton, matchTText, matchTBrowse);
		
		JTextField [] temp3 = {qmatchText, qtText};
		BrowseButton [] temp4 = {qmatchBrowse, qmatchQTBrowse};
		qmatchButton = new JCheckBox("External quantitative matching " +
				"(--qmatch)");
		qmatchButton.addActionListener(validateBodyAL);
		bundel(qmatchButton, temp3, temp4);
		
		mcButton = new JCheckBox("Maximum cluster size (--mc)");
		mcButton.addActionListener(validateBodyAL);
		bundel(mcButton, mcText);
		
		JTextField [] temp2 = {mcc1Text, mcc2Text};
		mccButton = new JCheckBox("Maximum number of cases/controls per " +
				"cluster (--mcc)");
		mccButton.addActionListener(validateBodyAL);
		bundel(mccButton, temp2, new BrowseButton[]{});
		
		//make the layout easy
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel readGPanel = new JPanel();
		readGPanel.setLayout(new BoxLayout(readGPanel, BoxLayout.LINE_AXIS));
		readGPanel.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		readGPanel.add(new JLabel("IBS distance file (--read-genome)"), c1);
		c1.weightx = 1;
		c1.gridx = 1;
		readGPanel.add(readGText, c1);
		c1.weightx = 0;
		c1.gridx = 2;
		readGPanel.add(readGBrowse, c1);
		body.add(readGPanel, c);

		
		//add the optional constraints
		JPanel constraints = new JPanel();
		constraints.setBorder(
				new TitledBorder("Optional clustering constraints"));
		constraints.setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 1;
		JPanel ppcPanel = new JPanel();
		ppcPanel.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		ppcPanel.add(ppcButton, c2);
		c2.gridx = 1;
		ppcPanel.add(ppcText, c2);
		c2.gridx = 2;
		ppcPanel.add(new JLabel("--ppc-gap "), c2);
		c2.gridx = 3;
		ppcPanel.add(pibsGText, c2);
		c2.gridx = 4;
		c2.weightx = 1;
		//This feels like a hack but I can't think of the pretty way to do this
		ppcPanel.add(new JLabel(), c2); 
		constraints.add(ppcPanel, c1);
		JPanel ibmPanel = new JPanel();
		ibmPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		ibmPanel.add(ibmButton, c2);
		c2.gridx = 1;
		ibmPanel.add(ibmText, c2);
		c2.gridx = 2;
		c2.weightx = 1;
		ibmPanel.add(new JLabel(), c2);
		c1.gridy = 1;
		constraints.add(ibmPanel, c1);
		
		c1.gridy = 2;
		constraints.add(ccButton, c1);
		JPanel mcPanel = new JPanel();
		mcPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		mcPanel.add(mcButton, c2);
		c2.gridx = 1;
		mcPanel.add(mcText, c2);
		c2.gridx = 2;
		c2.weightx = 1;
		mcPanel.add(new JLabel(), c2);
		c1.gridy = 3;
		constraints.add(mcPanel, c1);
		
		JPanel mccPanel = new JPanel();
		mccPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		mccPanel.add(mccButton, c2);
		c2.gridx = 1;
		mccPanel.add(mcc1Text, c2);
		c2.gridx = 2;
		mccPanel.add(mcc2Text, c2);
		c2.gridx = 3;
		c2.weightx = 1;
		mccPanel.add(new JLabel(), c2);
		c1.gridy = 4;
		constraints.add(mccPanel, c1);
		JPanel kPanel = new JPanel();
		kPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		kPanel.add(kButton, c2);
		c2.gridx = 1;
		kPanel.add(kText, c2);
		c2.gridx = 2;
		c2.weightx = 1;
		kPanel.add(new JLabel(), c2);
		c1.gridy = 5;
		constraints.add(kPanel, c1);

		
		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		matchPanel.add(matchButton, c2);
		c2.gridx = 1;
		c2.weightx = 1;
		matchPanel.add(matchText, c2);
		c2.gridx = 2;
		c2.weightx = 0;
		matchPanel.add(matchBrowse, c2);
		c1.gridy = 6;
		constraints.add(matchPanel, c1);
		JPanel matchTPanel = new JPanel();
		matchTPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		matchTPanel.add(matchTButton, c2);
		c2.gridx = 1;
		c2.weightx = 1;
		matchTPanel.add(matchTText, c2);
		c2.gridx = 2;
		c2.weightx = 0;
		matchTPanel.add(matchTBrowse, c2);
		c1.gridy = 7;
		constraints.add(matchTPanel, c1);
		JPanel qmatchQTPanel = new JPanel();
		qmatchQTPanel.setLayout(new GridBagLayout());
		c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		qmatchQTPanel.add(qmatchButton, c2);
		c2.gridwidth = 1;
		c2.gridx = 1;
		c2.weightx = 1;
		qmatchQTPanel.add(qmatchText, c2);
		c2.weightx = 0;
		c2.gridx = 2;
		qmatchQTPanel.add(qmatchBrowse, c2);
		c2.gridy = 1;
		c2.gridx = 0;
		c2.fill = GridBagConstraints.NONE;
		c2.anchor = GridBagConstraints.EAST;
		JLabel qtlabel = new JLabel("Thresholds (--qt) ");
		qmatchQTPanel.add(qtlabel, c2);
		c2.weightx = 1;
		c2.gridx = 1;
		c2.fill = GridBagConstraints.HORIZONTAL;
		qmatchQTPanel.add(qtText, c2);
		c2.weightx = 0;
		c2.gridx = 2;
		qmatchQTPanel.add(qmatchQTBrowse, c2);
		c1.gridy = 8;
		constraints.add(qmatchQTPanel, c1);
		
		c.gridy = 1;
		body.add(constraints, c);
		return body;
	}

	@Override
	protected void isBodyValid() {
		
		boolean ans = readGText.getText().length() > 0;
		
		if(ppcButton.isSelected())
			ans = ans && !ppcText.getText().equals("")
				&& !pibsGText.getText().equals("");
		if(ibmButton.isSelected())
			ans = ans && !ibmText.getText().equals("");
		if(mcButton.isSelected())
			ans = ans && !mcText.getText().equals("");
		if(mccButton.isSelected())
			ans = ans && !mcc1Text.getText().equals("")
				&& !mcc2Text.getText().equals("");
		if(kButton.isSelected())
			ans = ans && !kText.getText().equals("");
		if(matchButton.isSelected())
			ans = ans && !matchText.getText().equals("");
		if(matchTButton.isSelected())
			ans = ans && !matchTText.getText().equals("");
		if(qmatchButton.isSelected())
			ans = ans && !qmatchText.getText().equals("")
				&& !qtText.getText().equals("");
		
//		if(ans == false)
//			information.setText("There are either conflicting flags\n" +
//					"set or a selected flag does not have a required parameter.");
		
		validBody = ans;
		okForm();
	}

	/**
	 * process the body of the form to generate the plink command
	 * @return the command generated by this form
	 */
	@Override
	protected String processBody() {
		String ans = "--read-genome " 
			+ FileInfo.quote(readGText.getText())
			+ " --cluster";
		
		if(ppcButton.isSelected()){
			ans += " --ppc " + ppcText.getText();
			ans += " --ppc-gap " + pibsGText.getText();
		}
		if(ccButton.isSelected()){
			ans +=" --cc";
		}
		if(mcButton.isSelected()){
			ans += " --mc " + mcText.getText();
		}
		if(mccButton.isSelected()){
			ans += " --mcc " + mcc1Text.getText() + " " + mcc2Text.getText();
		}
		if(kButton.isSelected()){
			ans += " --K " + kText.getText();
		}
		if(ibmButton.isSelected()){
			ans += " --ibm " + ibmText.getText();
		} 
		if(matchButton.isSelected()){
			ans += " --match " + FileInfo.quote(matchText.getText());
		}
		if(matchTButton.isSelected()){
			ans += " --match-type " + FileInfo.quote(matchTText.getText());
		}
		if(qmatchButton.isSelected()){
			ans += " --qmatch " + FileInfo.quote(qmatchText.getText())
				+ " --qt " + FileInfo.quote(qtText.getText());
		}
		
		return ans;
	}

}
