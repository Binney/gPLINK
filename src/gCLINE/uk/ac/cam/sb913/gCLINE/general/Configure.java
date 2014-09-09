package gCLINE.uk.ac.cam.sb913.gCLINE.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;

/**
 * Configure local and remote parameters. In general
 * this will include what user defined alternative 
 * editor gCLINE is useing. In particular this will
 * also include additional file browsers, like haploview,
 * location of analysis excicutable, PLINK or WHAP.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public abstract class Configure extends JDialog {
	/**
	 * Access
	 */
	protected Record data;
	
	private GCFileChooser localChooser;
	protected JTextField editor;
	
	protected JTextField updateField;
	
	/**
	 * Set all the configurations in the project
	 * and save these configurations.
	 *
	 */
	public abstract void process();
	
	private JPanel createUpdate(){
		JPanel ans = new JPanel();
		ans.setBorder(new TitledBorder("Automatic Updater"));
		ans.add(new JLabel("Update file information every "));
		ans.add(updateField);
		ans.add(new JLabel("seconds"));
		return ans;
	}
	
	/**
	 * Create a JPanel that contains alternative
	 * editor
	 * @return JPanel that contians the alternative editor
	 * field and buttons to manipulate that.
	 */
	private JPanel createAltEditor(){
		JPanel ans = new JPanel();
		ans.setBorder(new TitledBorder("Alternative Editor"));
		
		//create a chooser for the browse button
		localChooser = new GCFileChooser(data.frame, null, 
				true, false,
				data.getConn(), "");
		localChooser.addFileChoosenListener(new FileChoosenListener(){
			public void fileChoosenOccures(FileChoosenEvent evt) {
				editor.setText(localChooser.fileName);
			}

		});
		
		//create the browse button
		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				localChooser.showChooser();
			}
		});
		
		//create a button to test the editor
		JButton test = new JButton("Test");
		test.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){	
			 new Thread(new RunCommand(editor.getText(), data, true)).start();
		}});
		ans.add(editor);
		ans.add(browse);
		ans.add(test);
		return ans;
		
	}
	
	/**
	 * Create a panel that has both the ok and cancel
	 * buttons.
	 * @return JPanel that contains but the ok and
	 * cancel buttons.
	 */
	private JPanel createButton(){
		JPanel ans = new JPanel();
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				process();
				Configure.this.dispose();
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Configure.this.dispose();
			}
		});
		
		ans.add(ok);
		ans.add(cancel);
		return ans;
	}
	
	protected abstract JPanel createFormSpecific();
	
	/**
	 * Create a configuration dialog that edits
	 * the configuration of the current record.
	 * @param d Record that we are editing the configuration
	 */
	public Configure(Record d){
		super(d.frame, "Configuration");
		data = d;
		editor = new JTextField(data.getAltEditor(), 30);
		if(data.isRemote())
			updateField = new JTextField(new Integer(data.getRemoteUpdateSec()).toString(), 4);
		else
			updateField = new JTextField(new Integer(data.getLocalUpdateSec()).toString(), 4);
		
		getRootPane().setLayout(new BoxLayout(getRootPane(), BoxLayout.PAGE_AXIS));
		getRootPane().add(createUpdate());
		getRootPane().add(createAltEditor());
		getRootPane().add(createButton());
		
	}
	
	public void makeVisible(){
		pack();
		setVisible(true);
	}
	
}
