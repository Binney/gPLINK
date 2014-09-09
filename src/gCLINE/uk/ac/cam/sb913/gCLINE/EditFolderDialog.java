package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;

@SuppressWarnings("serial")
public class EditFolderDialog extends JDialog {

	StartFrame frame;
	Record data;
	JPanel filePanel;
	JPanel buttonPanel;
	JLabel [] fileLabels;
	JTextField [] fileDescription;
	String [] names;
	
	private JButton ok, cancel;
	
	public EditFolderDialog(StartFrame mf, Record d,
			String [] fileName){
		super(mf, "Edit Global File");
		frame = mf;
		data = d;
		names = fileName;
		filePanel = new JPanel();
		filePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		createFiles();
		createButtons();
		
		for(int i = 0; i < fileName.length; i++){
			c.gridx = 0;
			c.gridy = i;
			c.weightx = 0;
			filePanel.add(fileLabels[i], c);
			c.weightx = 1;
			c.gridx = 1;
			filePanel.add(fileDescription[i], c);
		}
		
		this.getContentPane().setLayout(
				new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.getContentPane().add(new JScrollPane(filePanel));
		this.getContentPane().add(buttonPanel);
		pack();
		setVisible(true);
	}

	private void createButtons() {
		buttonPanel = new JPanel();
		ok = new JButton("OK");
		ok.addActionListener(setDescription);
		cancel = new JButton("Cancel");
		cancel.addActionListener(close);
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
	}

	private void createFiles() {
		fileLabels = new JLabel[names.length];
		fileDescription = new JTextField[names.length];
		
		for(int i = 0; i < names.length; i++){
			fileLabels[i] = new JLabel(names[i]);
			fileDescription[i] = new JTextField(data.getGlobalNote(names[i]), 10);
		}	
	}
	
	private ActionListener close = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			EditFolderDialog.this.dispose();
		}
	};
	
	private ActionListener setDescription = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < fileLabels.length; i ++){
				data.setGlobalFileDesc(
							fileLabels[i].getText(), fileDescription[i].getText());
			}
			EventQueue.invokeLater(frame.folderViewer.new UpdateJList(true));
			EditFolderDialog.this.dispose();
		}
	};
}
