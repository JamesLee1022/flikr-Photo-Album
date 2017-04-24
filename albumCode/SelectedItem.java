package albumCode;

import javax.swing.JLabel;

/**
 * Class to maintain any selected item from the list
 * @author James Lee
 *
 */
public class SelectedItem {
	
	/**
	 * constructor for the class
	 * @param label
	 * @param url
	 */
	SelectedItem(JLabel label, String url) {
		this.label = label;
		this.url = url;
	}
	JLabel label;
	String url;
}
