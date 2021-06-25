package gui.util;

import javafx.scene.control.TextField;

public class Constraints {

	/*Method responsable to control enter text to only 
	 * integer number. First, we catch the text and add a listener. If new
	 * value don't be an integer number, we let the old value.
	 */
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*")) {
				txt.setText(oldValue);
			}
		});
	}

	/*Method responsable to control enter text to a max value.First, we 
	 * catch the text and add a listener. If new value has a lenght bigger
	 * than max value, we let old value.
	 */
	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && newValue.length() > max) {
				txt.setText(oldValue);
			}
		});
	}

	/*Method responsable to control enter text to only 
	 * double number. First, we catch the text and add a listener. If new
	 * value don't be a double number, we let the old value.
	 */
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(oldValue);
			}
		});
	}
}
