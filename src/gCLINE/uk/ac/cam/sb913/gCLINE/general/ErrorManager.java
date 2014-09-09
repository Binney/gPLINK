package gCLINE.uk.ac.cam.sb913.gCLINE.general;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * An error manager who creates errors and
 * messages attaced to a given JFrame.
 * @author Kathe Todd-Brown
 *
 */
public class ErrorManager {
	
	/**
	 * The JFrame the messages are attaced to.
	 */
	private JFrame rootFrame;
	
	/**
	 * The maximum number of characters per line.
	 */
	private static int max_line_length = 40;
	
	/**
	 * The only constructor.
	 * @param givenFrame a JFrame that the messages
	 * are attached to.
	 */
	public ErrorManager(JFrame givenFrame){
		rootFrame = givenFrame;
	}
	
	/**
	 * Insert \n into a string to text to wrap the
	 * text at a given length.
	 * @param text A String that you want to be wrapped.
	 * @return A String that has text wrapped
	 */
	private static String wrap(String text){
		//split the message at the white spaces and
		//...put the message back together such that
		//...there is a \n atleast every N charactures

		if(text.length() > max_line_length){
			int end = text.length();
			int index = 0;
			for(int i = 0; i < end; i ++){
				if(text.charAt(index) == '\n'){
					index = 0;
				} else {
					index ++;
					if(index > max_line_length &&
							text.charAt(i) == (' ')){
						text = text.substring(0, i) + " \n"
							+ text.substring(i+1, text.length());
						index = 0;
					}
					
				}
			}
			/*int index = max_line_length - 1;
			while(index < text.length()){
				if(text.charAt(index) == (' ')){
					text = text.substring(0, index) + " \n"
						+ text.substring(index, text.length());
					index = index + max_line_length;
				}
				else{
					index ++;
				}
			}*/
		}
		return text;
	}
	
	/**
	 * Create an error message with a citation.
	 * @param message A string that holds a message for the
	 * user.
	 * @param citation A string that allows the programmers
	 * to know where the message came from for debugging purposes.
	 */
	public void createError(String message, String citation){
		new myDialog(rootFrame, "gPLINK error", 
				wrap(message));// + " \n " + wrap(citation));
		/*JOptionPane.showMessageDialog(rootFrame,
			    wrap(message) + " \n " + wrap(citation),
			    "gCLINE error",
			    JOptionPane.ERROR_MESSAGE);
			    */
	}
	
	/**
	 * Create a message with a citation.
	 * @param message A string that hodls a message for the
	 * user.
	 * @param citation A string that allows the programers
	 * to know where the message came from for debugging purposes.
	 */
	public void createMessage(String message, String citation){
		new myDialog(rootFrame, "gPLINK message", 
				wrap(message));// + " \n " + wrap(citation));
		/*JOptionPane.showMessageDialog(rootFrame,
				wrap(message) + " \n " + wrap(citation),
				"gCLINE message",
				JOptionPane.PLAIN_MESSAGE);
				*/
	}
	
	@SuppressWarnings("serial")
	private class myDialog extends JDialog{
		
		public myDialog(JFrame root, String title, String message){
			super(root, title);
			this.getContentPane().setLayout( new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			JTextArea text = new JTextArea(message);
			text.setEditable(false);
			getContentPane().add(text, c);
			c.gridy = 1;
			JButton ok = new JButton("OK");
			ok.setAlignmentX(Component.CENTER_ALIGNMENT);
			ok.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					myDialog.this.dispose();
				}
			});
			getContentPane().add(ok, c);
			pack();
			setVisible(true);
			//make this dialog on top
			this.setAlwaysOnTop(true);
		}
	}
	
}
