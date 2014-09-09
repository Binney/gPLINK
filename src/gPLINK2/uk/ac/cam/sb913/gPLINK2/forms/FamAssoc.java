package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class FamAssoc extends Form {

	static public String name = "Family Based Association (TDT)";
	JRadioButton tdtButton, pooButton;
	JCheckBox mpermButton, ciButton,
		permButton, matButton, patButton, parent1Button, parent2Button;
	
	JTextField mpermText, ciText;
	
	static Double CI_DEFAULT = new Double(0.95);
	static Integer MPERM_DEFAULT = new Integer(1000);
	
	public FamAssoc(FormCreator f) {
		super(f, name);
		validBody = true;
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		setStuff();
		
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.weightx = 1;
		body.add(tdtButton, c);
		c.gridy = 1;
		body.add(pooButton, c);
		c.gridy = 2;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		body.add(ciButton, c);
		c.weightx = 1;
		c.gridx = 1;
		body.add(ciText, c);
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridy = 5;
		body.add(mpermButton, c);
		c.weightx = 1;
		c.gridx = 1;
		body.add(mpermText, c);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 6;
		body.add(permButton, c);
		c.gridy = 7;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridy = 8;
		body.add(parent1Button, c);
		c.gridy = 9;
		body.add(parent2Button, c);
		c.gridy = 10;
		body.add(matButton, c);
		c.gridy = 11;
		body.add(patButton, c);
		return body;
	}

	@Override
	protected String processBody() {
		String ans = "";
		
		if(tdtButton.isSelected())
			ans += " --tdt";
		if(parent1Button.isSelected())
			ans += " --parentdt1";
		if(parent2Button.isSelected())
			ans += " --parentdt2";
		if(permButton.isSelected())
			ans += " --perm";
		if(mpermButton.isSelected())
			ans += " --mperm " + mpermText.getText();
		if(pooButton.isSelected())
			ans += " --tdt --poo";
		if(matButton.isSelected())
			ans += " --mat";
		if(patButton.isSelected())
			ans += " --pat";
		if(ciButton.isSelected())
			ans += " --ci " + ciText.getText();
		ans = ans.replaceFirst(" ", "");
		return ans;
	}

	@Override
	protected void isBodyValid() {
		//since either the tdt or the poo buttoon is always selected
		//...the body of this form is always valid
	}


	protected void setStuff() {
		mpermText = new JTextField(MPERM_DEFAULT.toString(), 4); 
		ciText = new JTextField(CI_DEFAULT.toString(), 4);
		
		tdtButton = new JRadioButton("Basic TDT (--tdt)");
		pooButton = new JRadioButton("Parent-of-origin (--tdt --poo)"); 
		
		ButtonGroup bases = new ButtonGroup();
		bases.add(tdtButton);
		tdtButton.setSelected(true);
		bases.add(pooButton);
		
		permButton = new JCheckBox("Adaptive permutations (--perm)"); 
		mpermButton = new JCheckBox("max(T) permutations (--mperm)"); 
		bundel(mpermButton, mpermText);
		ciButton = new JCheckBox("Confidence interval on OR (--ci)"); 
		bundel(ciButton, ciText);
		matButton = new JCheckBox("Permutation for POO maternal transmissions " +
				"(--mat)");
		patButton = new JCheckBox("Permutation for POO paternal transmissions " +
				"(--pat)");
		parent1Button = new JCheckBox("Permutation for parental discordance " +
				"test (--parentdt1)");
		parent2Button = new JCheckBox("Permutation for combined parenTDT " +
				"(--parentdt2)");
		

		ActionListener changepm = new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(mpermButton))
					permButton.setSelected(false);
				if(e.getSource().equals(permButton))
					mpermButton.setSelected(false);
			}
			
		};
		
		permButton.addActionListener(changepm);
		mpermButton.addActionListener(changepm);
		
		ActionListener changeC = new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(matButton)){
					patButton.setSelected(false);
					parent1Button.setSelected(false);
					parent2Button.setSelected(false);
				}
				if(e.getSource().equals(patButton)){
					matButton.setSelected(false);
					parent1Button.setSelected(false);
					parent2Button.setSelected(false);
				}
				if(e.getSource().equals(parent1Button)){
					patButton.setSelected(false);
					matButton.setSelected(false);
					parent2Button.setSelected(false);
				}
				if(e.getSource().equals(parent2Button)){
					patButton.setSelected(false);
					parent1Button.setSelected(false);
					matButton.setSelected(false);
				}
			}
			
		};
		matButton.addActionListener(changeC);
		patButton.addActionListener(changeC);
		parent1Button.addActionListener(changeC);
		parent2Button.addActionListener(changeC);
		
	}

}
