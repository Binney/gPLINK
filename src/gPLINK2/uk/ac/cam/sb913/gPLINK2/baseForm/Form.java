package gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.GPLINK;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public abstract class Form extends JDialog {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(Form.class);
	/**
	 * The FormCreator dialog which created this - could be single-command PLINK_Execute or multi-command PLINK_CreateQueue.
	 */
	public FormCreator parent;
	/**
	 * The InputPane for this form.
	 * This is made public so that the merge form
	 * can access certain functions.
	 */
	public InputPane input;
	/**
	 * The OutputPane for this form.
	 */
	protected OutputPane output;
	private String filter = "";
	
	public void resetFilter(){
		filter = "";
	}
	
	public void addFilter(String newParam){
		filter += newParam;
	}
	
	private String threshold = "";
	
	public void resetThreshold(){
		threshold = "";
	}
	
	public void addThreshold(String newParam){
		threshold += newParam;
	}
	
	/**
	 * Create the body for this form.
	 * @return A JPanel that holds the body
	 * of this form.
	 */
	protected abstract JPanel createBody();
	
	/**
	 * Flag if the body of the form is filled
	 * out correctly.
	 */
	protected boolean validBody = false;
	
	/**
	 * The ok button on the form, this is
	 * a class variable because it is en/disabled
	 * in okForm()
	 */
	private JButton ok;
	private JButton cancel;
	protected JButton filterButton;
	protected JButton thresholdButton;
	protected ActionListener validateBodyAL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			isBodyValid();
		}
	}; 
	protected DocumentListener validateBodyDL = new DocumentListener(){
		public void changedUpdate(DocumentEvent e) {}
		public void insertUpdate(DocumentEvent e) {
			isBodyValid();
		}
		public void removeUpdate(DocumentEvent e) {
			isBodyValid();
		}
	};
	/**
	 * Create the commandline based on the fields
	 * of this form.
	 * @return A String that is the PLINK command
	 * for this from. Note that this includes the
	 * any noted plink prefixes and the plink
	 * path.
	 */
	protected abstract String processBody();
	/**
	 * Check to see if the body is correctly filled
	 * out. If it is then the forms validBody boolean
	 * flag is set to true, otherwise it is set to false.
	 */
	protected abstract void isBodyValid();
	/**
	 * Enable and disable the ok button to reflect
	 * if the form is correctly or incorrectly filled
	 * out.
	 *
	 */
	protected void okForm(){
		logger.info("(okForm()) Entering okForm");
		if(input.validInput 
				&& output.validOutput 
				&& this.validBody)
			ok.setEnabled(true);
		else
			ok.setEnabled(false);
	}
	
	public void closeButtons(){
		ok.setEnabled(false);
		cancel.setEnabled(false);
		filterButton.setEnabled(false);
		thresholdButton.setEnabled(false);
	}
	
	public void openButtons(){
		ok.setEnabled(true);
		cancel.setEnabled(true);
		filterButton.setEnabled(true);
		thresholdButton.setEnabled(true);
		isBodyValid();
	}
	
	/**
	 * Create a JPanel that holds the ok/cancel
	 * buttons for this form.
	 * @return A JPanel that has the ok/cancel/threshold/filter
	 * buttons that will be located at the end of the form.
	 */
	private JPanel createButtonPane(){
		JPanel ans = new JPanel();
		
		filterButton = new JButton("Filter");
		filterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new FilterDialog(Form.this);
			}
		});
		
		thresholdButton = new JButton("Threshold");
		thresholdButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new ThresholdDialog(Form.this);
			}
		});
		
		ok = new JButton("OK");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO check parent can't end up null
				System.out.println("FORM.java: checking for nullness of parent before clicking ok!");
				if (parent == null) {
					System.out.println("Sadly, parent is null :(");
					// TODO
					return;
				}
				parent.addCommand(process(), getTitle());
				parent.setEnabled(true);
				Form.this.dispose();
			}
		});
		ok.setEnabled(false);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logger.info("(createButtonPane())Cancel button activated");
				parent.setEnabled(true);
				Form.this.dispose();
			}
		});
		
		ans.add(filterButton);
		ans.add(thresholdButton);
		ans.add(ok);
		ans.add(cancel);
		return ans;
	}
	/**
	 * Process forms in general.
	 * @return A String that is the command line to be run.
	 */
	private String process(){
		String cmd = parent.getFrame().data.getPlinkPrefix()
		+ " " + FileInfo.quote(parent.getFrame().data.getPlinkPath())
		+ " " + filter
		+ " " + threshold
		+ " " + input.process() 
		+ " " + processBody()
		+ " " + output.process();
		
		return cmd;
	}

	/**
	 * 
	 * @param p
	 * @param name
	 */
	public Form(FormCreator p, String name){
		// TODO double check parent.getFrame() isn't being abused; especially given
		// we're no longer passing in GPLINK arguments anywhere, only FormCreator
		super(p, name);
		parent = p;
		GPLINK frame = parent.getFrame();
		// TODO can the InputPane and OutputPanes be rewritten taking only FormCreator arguments?
		input = new InputPane(frame, this);
		output = new OutputPane(this, 
				frame.data.getLogExt(), 
				frame.data.getHomeFiles(), 
				frame.data.getHomeFolder());
		createBody();

		this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		getContentPane().add(new JScrollPane(input));
		getContentPane().add(new JScrollPane(createBody()));
		getContentPane().add(output);
		getContentPane().add(createButtonPane());
	}
	
	static public JPanel bundel(final JToggleButton toggle, 
			final JTextField text, final JButton button){
		toggle.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				text.setEnabled(toggle.isSelected());
				button.setEnabled(toggle.isSelected());
			}
		});
		
		text.setEnabled(toggle.isSelected());
		button.setEnabled(toggle.isSelected());
		JPanel ans = new JPanel();
		ans.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		ans.add(toggle, c);
		c.gridx = 1;
		c.weightx = 1;
		ans.add(text, c);
		c.weightx = 0;
		c.gridx = 2;
		ans.add(button, c);
		return ans;
	}
	
	static public JPanel bundel(final JToggleButton toggle,
			final JTextField text){
		toggle.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				text.setEnabled(toggle.isSelected());
			}
		});
		
		text.setEnabled(toggle.isSelected());
		JPanel ans = new JPanel();
		ans.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		ans.add(toggle, c);
		c.gridx = 1;
		ans.add(text, c);
		return ans;
	}
	
	static public  void bundel(final JToggleButton toggle, 
			final JTextField [] texts, final BrowseButton [] buttons){
		toggle.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				boolean selected = toggle.isSelected();
				for(JTextField f : texts)
					f.setEnabled(selected);
				for(BrowseButton b: buttons)
					b.setEnabled(selected);
			}
		});
		boolean selected = toggle.isSelected();
		for(JTextField f : texts)
			f.setEnabled(selected);
		for(BrowseButton b: buttons)
			b.setEnabled(selected);
	}
	
	static public void bundel(final JToggleButton toggle, 
			final JTextField [] texts){
		toggle.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				boolean selected = toggle.isSelected();
				for(JTextField f : texts)
					f.setEnabled(selected);
			}
		});
		boolean selected = toggle.isSelected();
		for(JTextField f : texts)
			f.setEnabled(selected);
	}
	
	static public JPanel bundel(JTextField text, JButton button){
		JPanel ans = new JPanel();
		ans.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		ans.add(text, c);
		c.weightx = 0;
		c.gridx = 2;
		ans.add(button, c);
		return ans;
	}
	
	@SuppressWarnings({ "serial", "unused" })
	public class BrowseButton extends JButton{
		
		private String suffix;
		private String suffixDesc;
		private JTextField target;
		private GCFileChooser pick;
		
		public BrowseButton(JTextField givenTarget){
			this(givenTarget, null, null);
		}
		
		public BrowseButton(JTextField givenTarget, 
				String givensuffix, 
				String givensuffixDesc){
			super("Browse");
			target = givenTarget;
			suffix = givensuffix;
			suffixDesc = givensuffixDesc;
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					FileFilter temp = null;
					
					if(suffix != null){
						temp = new FileFilter (){
							@Override
							public boolean accept(File f) {
								return f.getAbsolutePath().endsWith(suffix);
							}
							@Override
							public String getDescription() {
								return suffixDesc;
							}};
					}
					
					pick = new GCFileChooser(
							Form.this, 
							temp, 
							!parent.getFrame().data.isRemote(), 
							false,
							parent.getFrame().data.getConn(), 
							parent.getFrame().data.getHomeFolder());
					pick.addFileChoosenListener(new FileChoosenListener(){
						public void fileChoosenOccures(FileChoosenEvent evt) {
							target.setText(pick.fileName);
						}});
					pick.showChooser();
				}
			});
		}
	}
}
