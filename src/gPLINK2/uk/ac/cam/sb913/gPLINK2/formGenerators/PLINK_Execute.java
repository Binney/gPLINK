package gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

import org.apache.log4j.Logger;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.GPLINK;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.CalculationInfo;

/**
 * Create a dialog that will pass the plink
 * command to either a local or remote computer
 * depending on weather the project is flagged as
 * remote or local.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class PLINK_Execute extends FormCreator {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(PLINK_Execute.class);


	/**
	 * Create an instance of this dialog with the relevant command.
	 * @param givenFrame The GPLINK instance in which we want
	 * to store the operation.
	 * @param cmd A String that is the command, note that
	 * filenames with spaces must be quoted [\"\"]
	 */
	public PLINK_Execute(GPLINK givenFrame, String cmd){
		this(givenFrame);
		addCommand(cmd);
        logger.info("[PLINK_Execute](GPLINK, String) created given cmd template");

	}

	/**
	 * Create an instance of this dialog with an empty command
	 * slot and no command-constructing forms
	 * @param givenFrame The GPLINK instance in which we want
	 * to store the operation.
	 */
	public PLINK_Execute(GPLINK givenFrame) {

		super(givenFrame,"Execute Command");
		
		logger.info("[PLINK_Execute](GPLINK) loading new window");
		//initialize the fields
		frame = givenFrame;
		setupWindow();
        logger.info("[PLINK_Execute](GPLINK) created empty command template");

		this.setVisible(false);
		this.setEnabled(false);
		System.out.println("[FormCreator](GPLINK, String) created anew and set invisible");
	}

	/**
	 * Create an instance of this dialog, but immediately
	 * hide it and create visible child dialog form of
	 * relevant type.
	 * @param givenFrame The GPLINK instance in which we want
	 * to store the operation.
	 * @param formType The type of form to create instantly.
	 */
	public PLINK_Execute(GPLINK givenFrame, FormType formType) {
		this(givenFrame);
		showForm(formType);
		logger.info("[PLINK_Execute](GPLINK) loading new window with form " + formType.toString());
	}


	public void addCommand(String cmd) {
		addCommand(cmd, "");
	}

	public void addCommand(String cmd, String description) {
		if(!cmd.matches(".*\\-\\-gplink.*")){
			logger.info("[PLINK_Execute.addCommand(String, String)] adding \"--gplink\" to the end");
			cmd += " --gplink";
		}
		commandClineField.setText(cmd);
		commandDescriptionField.setText(description);

		this.setEnabled(true);
		this.setVisible(true);
	}

	private void setupWindow() {

		initComponents();
		setVisible(true);

	}

    private void initComponents() {

    	commandClineField = new JTextField();
        commandClineLabel = new JLabel();
        commandDescriptionField = new JTextField();
        commandDescriptionLabel = new JLabel();

        okButton = new JButton();
        editCommandButton = new JButton();
        cancelButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(2147483647, 155));
        setMinimumSize(new java.awt.Dimension(400, 153));
        setPreferredSize(new java.awt.Dimension(617, 154));

        commandClineField.setText("");
        commandClineLabel.setText("Command");

        commandDescriptionField.setText("");
        commandDescriptionLabel.setText("Description");

        okButton.setText("OK");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clickOk();
			}
		});

        editCommandButton.setText("Edit");
		editCommandButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clickEdit();
			}
		});

        cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				cancelOperation();
			}
		});

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(commandDescriptionLabel)
                            .addComponent(commandClineLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(commandClineField, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editCommandButton))
                            .addComponent(commandDescriptionField))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(commandClineField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(commandClineLabel)
                    .addComponent(editCommandButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(commandDescriptionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(commandDescriptionLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    @Override
    public void setEnabled(boolean value) {

    	commandClineField.setEnabled(value);
        commandClineLabel.setEnabled(value);
        commandDescriptionField.setEnabled(value);
        commandDescriptionLabel.setEnabled(value);

        okButton.setEnabled(value);
        editCommandButton.setEnabled(value);
        cancelButton.setEnabled(value);
        logger.info("[PLINK_Execute].setEnabled(boolean) setting enability");
    }

    private void clickOk() {
		if (commandClineField.getText().equals("")) {
			// TODO does this get a proper user info flash or summat?
			logger.warn("[PLINK_Execute] please enter a command!");
		} else {
			try {
				CalculationInfo opInfo = new CalculationInfo(commandClineField.getText(), commandDescriptionField.getText(), null);
				frame.data.appendOperationToTree(opInfo, null);
				// Parsing was successful; execute latest added command!
				opInfo.execute((Record)frame.data);
			} catch (Exception e) {
				// Parsing failed so launch another dialog.
				// TODO would be nice to flash a message or something to say what failed?
				logger.warn("[PLINK_Execute(GPLINK, String)]" +
						" can not add operation, not executing command.");
				new PLINK_Execute(frame, commandClineField.getText());
			}

			//dispose the execute dialog
			PLINK_Execute.this.dispose();
		}
	}

	private void clickEdit() {
		// TODO determine which form was used, and open it; if command line is empty, then open a menu
		// to select form opening stuff
		if (commandClineField.getText()=="") {
			// No command has been entered so give form opening options

		} else {
			// A command has already been partially constructed so load the relevant form
			// TODO figure out which form to load
		}
	}

	public void cancelOperation() {
		// TODO give a "are you sure you want to quit?" dialog

		PLINK_Execute.this.dispose();
	}

	// TODO insert implementations of abstract methods from FormCreator here - 


	@Override
	public void submit() {
		return;
	}

}
