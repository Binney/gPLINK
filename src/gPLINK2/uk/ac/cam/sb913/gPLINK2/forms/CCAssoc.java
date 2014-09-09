package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class CCAssoc extends Form {

	static public String name = "Genotypic C/C association tests";
	JCheckBox ciButton, adjustButton, cellButton;
	
	JRadioButton  mperm2Button, perm2Button, mGenButton, 
	mTrendButton, mDomButton, mRecButton, mBestButton;
	
	JTextField ciText, cText, mperm2Text;
	
	static private Double CI_DEFAULT = new Double(0.95);
	static private Integer MPERM_DEFAULT = new Integer(1000);
	static private Integer CELL_DEFAULT = new Integer(5);

	public CCAssoc(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
		
		ciText = new JTextField(CI_DEFAULT.toString(), 4);
		ciText.getDocument().addDocumentListener(validateBodyDL);
		cText = new JTextField(CELL_DEFAULT.toString(), 4);
		cText.getDocument().addDocumentListener(validateBodyDL);
		mperm2Text = new JTextField(MPERM_DEFAULT.toString(), 4);
		mperm2Text.getDocument().addDocumentListener(validateBodyDL);
		cellButton = new JCheckBox("Minimum required observation per cell (--cell)"); 
		cellButton.addActionListener(validateBodyAL);
		bundel(cellButton, cText); 
		
		ciButton = new JCheckBox("Confidence intervals (--ci)");
		ciButton.addActionListener(validateBodyAL);
		bundel(ciButton, ciText); 
		
		adjustButton = new JCheckBox("Adjusted p-values (--adjust)"); 
		adjustButton.addActionListener(validateBodyAL);
		mperm2Button = new JRadioButton("max(T) permutation mode (--mperm)");
		mperm2Button.addActionListener(validateBodyAL);
		bundel(mperm2Button, mperm2Text);
		
		perm2Button = new JRadioButton("Adaptive permutation mode (--perm)");
		perm2Button.addActionListener(validateBodyAL);
		mBestButton = new JRadioButton("Permute best test (default)");
		mBestButton.addActionListener(validateBodyAL);
		mGenButton = new JRadioButton("Permute genotypic test (--model-gen)");
		mGenButton.addActionListener(validateBodyAL);
		mTrendButton = new JRadioButton("Permute trend test (--model-trend)"); 
		mTrendButton.addActionListener(validateBodyAL);
		mDomButton = new JRadioButton("Permute dominant test (--model-dom)");
		mDomButton.addActionListener(validateBodyAL);
		mRecButton = new JRadioButton("Permute recessive test (--model-rec)");
		mRecButton.addActionListener(validateBodyAL);
		
		ButtonGroup g1 = new ButtonGroup();
		g1.add(mBestButton);
		g1.add(mGenButton);
		g1.add(mDomButton);
		g1.add(mRecButton);
		g1.add(mTrendButton);
	
		ActionListener changePerm = new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(mperm2Button.isSelected())
					perm2Button.setSelected(false);
			}
			
		};
		ActionListener changeMperm = new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(perm2Button.isSelected())
					mperm2Button.setSelected(false);
			}
			
		};
		
		perm2Button.addActionListener(changeMperm);
		mperm2Button.addActionListener(changePerm);
		
		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
	
		body.add(Box.createRigidArea(new Dimension(0,10)));
		
		body.add( new JLabel("Genotypic association tests (C/C) (--model)"));
		
		body.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel cellPanel = new JPanel();
		cellPanel.add(cellButton);
		cellPanel.add(cText);
		cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.X_AXIS));
		cellPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		body.add(cellPanel);
		
		JPanel ciPanel = new JPanel();
		ciPanel.add(ciButton);
		ciPanel.add(ciText);
		ciPanel.setLayout(new BoxLayout(ciPanel, BoxLayout.X_AXIS));
		ciPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		body.add(ciPanel);

		adjustButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		body.add(adjustButton);
		
		body.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel constraints = new JPanel();
		constraints.setBorder(new TitledBorder("Permutation options"));
		constraints.setLayout(new BoxLayout(constraints, BoxLayout.Y_AXIS));
		constraints.setAlignmentX(Component.LEFT_ALIGNMENT);
			
		JPanel mp2Panel = new JPanel();
		mp2Panel.add(mperm2Button);
		mp2Panel.add(mperm2Text);
		mp2Panel.setLayout(new BoxLayout(mp2Panel, BoxLayout.X_AXIS));
		mp2Panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		constraints.add(mp2Panel);
		
		perm2Button.setAlignmentX(Component.LEFT_ALIGNMENT);
		constraints.add(perm2Button);

		constraints.add(Box.createRigidArea(new Dimension(0,15)));
		
		mBestButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		mBestButton.setSelected(true);
		constraints.add(mBestButton);

		mGenButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		constraints.add(mGenButton);
		
		mTrendButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		constraints.add(mTrendButton);
		
		mDomButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		constraints.add(mDomButton);
		
		mRecButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		constraints.add(mRecButton);
		
		body.add(constraints);
		return body;
	}

	@Override
	protected void isBodyValid() {
		if( (cellButton.isSelected() && !cText.getText().matches("\\d*\\.?\\d+"))){
//			information.setText("Must enter a value associated with the --cell flag.");
			validBody = false;
		}else if(ciButton.isSelected() && !ciText.getText().matches("\\d*\\.?\\d+")){
//			information.setText("Must enter a value associated with the --ci flag.");
			validBody = false;
		}else if(mperm2Button.isSelected() && !mperm2Text.getText().matches("\\d*\\.?\\d+")){
//			information.setText("Must enter a value associated with the --mperm flag.");
			validBody = false;
		}else validBody = true;
		
		okForm();
	}

	@Override
	protected String processBody() {
	
		String ans = "";
		
		ans +="--model";

		if(ciButton.isSelected())
			ans +=" --ci " + ciText.getText();
		if(mperm2Button.isSelected())
			ans += " --mperm " + mperm2Text.getText();
		if(perm2Button.isSelected())
			ans += " --perm";
		
		if(adjustButton.isSelected())
			ans += " --adjust";
		if(cellButton.isSelected())
			ans += " --cell " + cText.getText();

		if(mGenButton.isSelected())
			ans += " --model-gen";
		if(mTrendButton.isSelected())
			ans += " --model-trend";
		if(mDomButton.isSelected())
			ans += " --model-dom";
		if(mRecButton.isSelected())
			ans += " --model-rec";
		
		return ans;

	}

}
