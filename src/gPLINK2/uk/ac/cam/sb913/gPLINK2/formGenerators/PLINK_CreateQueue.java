package gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.QueueItem;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.QueueInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.GPLINK;

/**
 * Create a dialog which can create multiple commands,
 * both PLINK-specific and generic, enter them into a queue,
 * and save and execute the entire queue.
 */
@SuppressWarnings("serial")
public class PLINK_CreateQueue extends FormCreator {

	private static Logger logger = Logger.getLogger(PLINK_CreateQueue.class);

	public static String name = "Create queue";
	// todo also a checkbox for input files to be from the previous operation's output

	/**
	 * The JMenu offering all types of operation
	 */
	private JPopupMenu formSelectMenu;

	/** 
	 * A reorderable JList holding all
	 * the calculations in the queue so far
	 */
	private JList <QueueItem> queueList;

    /**
     * A ScrollPane into which the list of calculations can fit.
     */
    private JScrollPane queueScrollPane;

	/**
	 * A mutable list holding all calculations in the queue so far.
	 */
	private DefaultListModel <QueueItem> queueItems;

	/**
	 * A text field to enter the queue name into.
	 */
	private JTextField queueNameField;

	/**
	 * A label saying "Queue name" next to the name entry field.
	 */
	private JLabel queueNameLabel;

	/**
	 * A text field for entering the queue description.
	 */
	private JTextField queueDescriptionField;

	/**
	 * A label saying "Queue description" next to the description entry field.
	 */
	private JLabel queueDescriptionLabel;

	/**
	 * A text field holding the selected command.
	 */
	private JTextField commandClineField;

	/**
	 * A label saying "Command" next to the command entry field.
	 */
	private JLabel commandClineLabel;

	/**
	 * A text field holding the description of the selected command.
	 */
	private JTextField commandDescriptionField;

	/**
	 * A label saying "Command description" next to the description entry field.
	 */
	private JLabel commandDescriptionLabel;

	/**
	 * A button to delete the selected command.
	 */
	private JButton deleteCommandButton;

	/**
	 * A button to move the selected command forward one place in the queue.
	 */
	private JButton moveUpButton;

	/**
	 * A button to move the selected command back one place in the queue.
	 */
	private JButton moveDownButton;

	/**
	 * A button to add a command to the queue by opening form options.
	 */
	private JButton addCommandButton;

	/**
	 * Add a new command with a placeholder description.
	 * @param cmd A String containing the command to add.
	 */
	@Override
	public void addCommand(String cmd) {
		addCommand(cmd, "Insert description here");
	}

	/**
	 * Create a new QueueItem and add it to the list, or attach the
	 * given command and description to an existing command if one is
	 * already selected. 
	 * @param cmd A String containing the command to add.
	 * @param description A String containing a human-readable description of this command.
	 */
	@Override
	public void addCommand(String cmd, String description) {
		if (queueList.isSelectionEmpty()) {
			// Add new item to the end of the ListModel
			queueItems.addElement(new QueueItem(cmd, description));
            queueList.setSelectedIndex(queueItems.getSize()-1);
            logger.info("[PLINK_CreateQueue].addCommand(String, String) created and appended a new calculation");
		} else {
			// Replace selected item
			int i = queueList.getSelectedIndex();
			queueList.clearSelection();
			queueItems.setElementAt(new QueueItem(cmd, description), i);
			logger.info("[PLINK_CreateQueue].addCommand(String, String) replaced existing calculation");
			queueList.setSelectedIndex(i);
		}
        this.setVisible(true);
        this.setEnabled(true);
	}

	public PLINK_CreateQueue(GPLINK mf){
		super(mf, name);
		frame = mf;

        initComponents();
		setVisible(true);
        logger.info("[PLINK_CreateQueue](GPLINK) created queue template");
	}


    // <editor-fold defaultstate="collapsed" desc="Window setup">
	/**
	 * Create all the GUI components on the frame and resize, set texts, bind handlers etc.
	 */
	private void initComponents() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(637, 350));

        formSelectMenu = createFormPopupMenu();

        createQueueScrollList();

        createQueueNameLabel();
        createQueueNameField();
        createQueueDescriptionLabel();
        createQueueDescriptionField();
        createCommandCLineLabel();
        createCommandCLineField();
        createCommandDescriptionLabel();
        createCommandDescriptionField();

        createeditCommandButton();
        createdeleteCommandButton();
        createMoveUpButton();
        createMoveDownButton();
        createAddButton();
        createCancelButton();
        createOKButton();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(queueScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(commandClineLabel)
                            .addComponent(commandDescriptionLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(commandDescriptionField)
                            .addComponent(commandClineField)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editCommandButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteCommandButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moveUpButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moveDownButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addCommandButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(queueDescriptionLabel)
                            .addComponent(queueNameLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(queueNameField)
                            .addComponent(queueDescriptionField))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(queueNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(queueNameLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(queueDescriptionLabel)
                    .addComponent(queueDescriptionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(queueScrollPane, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(commandClineLabel)
                    .addComponent(commandClineField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(commandDescriptionLabel)
                    .addComponent(commandDescriptionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(editCommandButton)
                    .addComponent(deleteCommandButton)
                    .addComponent(moveUpButton)
                    .addComponent(okButton)
                    .addComponent(moveDownButton)
                    .addComponent(addCommandButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }

    private void createQueueScrollList() {
        queueScrollPane = new JScrollPane();
        queueList = new JList<QueueItem>();
        queueItems = new DefaultListModel<QueueItem>();
        queueList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
            	refreshCommandTextFields();
            }
        });
        queueList.setModel(queueItems);
        queueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        queueScrollPane.setViewportView(queueList);
        queueList.clearSelection();
    }
    
    private void refreshCommandTextFields() {
    	System.out.println("Refreshing command text fields");
    	if (queueList.isSelectionEmpty()) {
        	System.out.println("on nothing");
    		commandClineField.setText("");
    		commandDescriptionField.setText("");
    		commandClineField.setEnabled(false);
    		commandDescriptionField.setEnabled(false);
    	} else {
        	System.out.println("on an existing command");
            QueueItem selectedCommand = queueList.getSelectedValue();
            commandClineField.setText(selectedCommand.getCline());
            commandDescriptionField.setText(selectedCommand.toString());
            if (!commandClineField.isEnabled()) {
        		commandClineField.setEnabled(true);
        		commandDescriptionField.setEnabled(true);            	
            }
    	}
    }

    private void createQueueNameLabel() {
        queueNameLabel = new JLabel();
        queueNameLabel.setText("Queue name");
    }

    private void createQueueNameField() {
    	queueNameField = new JTextField();
        queueNameField.setToolTipText("Enter a name for this queue.");
    }

    private void createQueueDescriptionLabel() {
    	queueDescriptionLabel = new JLabel();
        queueDescriptionLabel.setText("Queue description");
    }

    private void createQueueDescriptionField() {
    	queueDescriptionField = new JTextField();
        queueDescriptionField.setToolTipText("Enter a description of what the queue does.");
    }

    private void createCommandCLineLabel() {
        commandClineLabel = new JLabel();
        commandClineLabel.setText("Command");
    }

    private void createCommandCLineField() {
        commandClineField = new JTextField();
        commandClineField.setToolTipText("Enter a command.");
        commandClineField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        	// All three of these events are required to detect when the text
        	// in the command field is changed, due to arcane truths of 
        	// Java Swing that would melt your puny human brain if you were
        	// to attempt to comprehend them.
        	public void changedUpdate(javax.swing.event.DocumentEvent e) {
        		updateSelectedItem();
        	}
        	public void removeUpdate(javax.swing.event.DocumentEvent e) {
        		updateSelectedItem();
        	}
        	public void insertUpdate(javax.swing.event.DocumentEvent e) {
        		updateSelectedItem();
        	}        	
        });
    }
    
    /**
     * Update the selected item with whatever text is in the text fields for 
     * command and description.
     */
    private void updateSelectedItem() {
    	if (!queueList.isSelectionEmpty()) {
    		updateCommand(commandClineField.getText(), commandDescriptionField.getText(), queueList.getSelectedIndex());
    	}
    }
    
    private void updateCommand(String command, String description, int index) {
    	// Replace selected item
		queueItems.setElementAt(new QueueItem(command, description), index);
		logger.info("[PLINK_CreateQueue].updateCommand(String, String, int) updated existing calculation");
	}

    private void createCommandDescriptionLabel() {
        commandDescriptionLabel = new JLabel();
        commandDescriptionLabel.setText("Description");
    }

    private void createCommandDescriptionField() {
		commandDescriptionField = new JTextField();
        commandDescriptionField.setToolTipText("Enter a description of this command.");
        commandDescriptionField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        	// All three of these events are required to register when the text in the 
        	// command description field is changed, because Java.
        	public void changedUpdate(javax.swing.event.DocumentEvent e) {
        		updateSelectedItem();
        	}
        	public void removeUpdate(javax.swing.event.DocumentEvent e) {
        		updateSelectedItem();
        	}
        	public void insertUpdate(javax.swing.event.DocumentEvent e) {
        		updateSelectedItem();
        	}        	
        });
    }

    private void createeditCommandButton() {
        editCommandButton = new JButton();
        editCommandButton.setText("Edit");
        editCommandButton.setToolTipText("Edit selected command.");
        editCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                editSelected();
            }
        });    }

    private void createdeleteCommandButton() {
        deleteCommandButton = new JButton();
        deleteCommandButton.setText("Delete");
        deleteCommandButton.setToolTipText("Delete selected command.");
        deleteCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteSelected();
            }
        });
    }

    private void createMoveUpButton() {
        moveUpButton = new JButton();
        moveUpButton.setText("Move up");
        moveUpButton.setToolTipText("Move selected operation forwards in the queue");
        moveUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                moveSelectedUp();
            }
        });
    }

    private void createMoveDownButton() {
    	moveDownButton = new JButton();
        moveDownButton.setText("Move down");
        moveDownButton.setToolTipText("Move selected operation backwards in the queue");
        moveDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                moveSelectedDown();
            }
        });
    }

    private void createAddButton() {
    	addCommandButton = new JButton();
        addCommandButton.setText("Add");
        addCommandButton.setToolTipText("Add a new operation to the queue");
        addCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addNewCommand();
            }
        });
    }

    private void createCancelButton() {
        cancelButton =  new JButton();
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Cancel without saving?");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelOperation();
            }
        });
    }
	
    private void createOKButton() {
        okButton = new JButton();
        okButton.setText("OK");
        okButton.setToolTipText("Save and run queue");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                submit();
            }
        });
    }
    // </editor-fold>

    @Override
    public void setEnabled(boolean value) {
        queueList.setEnabled(value);
        queueScrollPane.setEnabled(value);
        queueNameField.setEnabled(value);
        queueDescriptionField.setEnabled(value);
        commandClineField.setEnabled(value);
        commandDescriptionField.setEnabled(value);
        editCommandButton.setEnabled(value);
        deleteCommandButton.setEnabled(value);
        moveUpButton.setEnabled(value);
        moveDownButton.setEnabled(value);
        addCommandButton.setEnabled(value);
        okButton.setEnabled(value);
    }

	/**
	 * Edit the selected command by either launching the menu to create a new
	 * form or by recreating the form used to make the command in the first place.
	 */
    private void editSelected() {
    	if (queueList.getSelectedValue().getCline().equals("Add command here!")) {
    		// You have selected a blank command so show the form menu
            formSelectMenu.show(this, 0, addCommandButton.getHeight());
    	} else {
    		// You are trying to edit a previously generated command so open
    		// up the relevant form with all the right boxes checked
    		// TODO go through all forms defining new constructors which take a String of the
    		// current cline so you know which boxes to check. ie the reverse of "processBody"
    	}
    }

    /**
     * Delete the selected command from the queue, after prompting the user to continue.
     */
    private void deleteSelected() {
        int index = queueList.getSelectedIndex();
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete operation", dialogButton);
        if ((index >= 0) && (dialogResult==0)) {
          queueItems.remove(index);
        }
    }

    /**
     * Move the selected command one place forward in the queue.
     */
    private void moveSelectedUp() {
        int index = queueList.getSelectedIndex();
        if (index > 0) {
            swapItems(index, index - 1);
        }
    }

    /**
     * Move the selected command one place back in the queue.
     */
    private void moveSelectedDown() {
        int index = queueList.getSelectedIndex();
        if (index < queueItems.size()-1) {
            swapItems(index, index + 1);
        }
    }

    /**
     * Swap the commands at the two given indices. Assumes you have selected a and are
     * moving it to b, and so will select b when you're done.
     * @param a the index of the first command
     * @param b the index of the second command
     */
    private void swapItems(int a, int b) {
        QueueItem tmp = queueItems.getElementAt(a);
        queueItems.setElementAt(queueItems.getElementAt(b), a);
        queueItems.setElementAt(tmp, b);
        queueList.setSelectedIndex(b);
    }

    private void addNewCommand() {
        // Add a blank command to the bottom of the queue
    	queueList.clearSelection();
        addCommand("Add command here!", "New command");
        queueList.setSelectedIndex(queueItems.getSize()-1);

        // Show menu to construct a new command - TODO this doesn't show up in the right place
        java.awt.Point p = addCommandButton.getLocationOnScreen();
        formSelectMenu.show(this, p.x, p.y + addCommandButton.getHeight());
    }

    public void cancelOperation() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entire queue?", "Cancel queue", dialogButton);
        if (dialogResult==0) {
            PLINK_CreateQueue.this.dispose();
          }
    }

	public void submit() {
		if (queueItems.size()==0) {
			JOptionPane.showMessageDialog(this, "Please enter at least one operation.");
			return;
		} else if (queueNameField.getText().isEmpty() || queueDescriptionField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a name and description for this queue.");
			return;
		}

		QueueInfo queue = new QueueInfo(frame.data);
        try {
            queue = new QueueInfo(queueNameField.getText(), queueDescriptionField.getText(), null, frame.data);
            System.out.println("Adding " + queue.getName() + " to project");
            frame.data.addQueue(queue);
            // TODO get queue and shove in queue variable 
        } catch (Exception e) {
            // TODO Inform user that queue could not be added
            logger.warn("[PLINK_CreateQueue] failed to add queue to project.");
            return;            
        }

        System.out.println("Now doing the operations");
        try {
			for (int i = 0; i < queueItems.size(); i++) {
			    QueueItem currentOp = queueItems.getElementAt(i);
			    // parse here
			    if (queue.addCalculation(currentOp.getCline(), currentOp.toString())) {
			    	System.out.println("Successfully added " + currentOp.toString());
			    	// Yay!
			    } else {
					int result = JOptionPane.showConfirmDialog(this,
				         "Couldn't add command " + i + " to queue.\nIgnore and keep going?", "Error encountered", JOptionPane.YES_NO_OPTION);
					// TODO more informative error messages, please!
					if (result == JOptionPane.NO_OPTION) {
						// TODO delete the queue from project, reactivate CreateQueue dialog, and return. 
					}
	
			    }
			    // TODO log addition of operation
			    
			}
			// TODO if you press "no" it doesn't bother executing the queue and instead
			// leaves it in the operation view pane for later execution - TODO need a button in the operation pane for that!
			int result = JOptionPane.showConfirmDialog(this,
					"Successfully parsed queue and saved to project.\nBegin execution?", "Queue saved", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				queue.execute(frame.data);
	
			this.dispose(); // TODO should that be "PLINK_CreateQueue.this.dispose" by analogy with PLINK_Execute?
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(this, "Error encountered trying to add all operations to project. Please double check they're all valid.\nError message follows:\n" + e.getMessage());
        }
	}

}
