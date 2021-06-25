package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {
	
	/*Method responsable for show an alert. AlertType is an enumeration and
	 * which changes the icons of alerts
	 */
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	/*Method responsable for show an alert confirming any action. This method returns an object
	 * (ButtonType) from Optional, that uses yes or no.
	 */
	public static Optional<ButtonType> showConfirmation(String title, String content){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		
		//use to show the window and wait the action
		return alert.showAndWait();
	}
}