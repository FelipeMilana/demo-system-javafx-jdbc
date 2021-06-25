package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		//use functional programming
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView(); 
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		//use functional programming
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView(); 
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	/*We use Synchronized because we need that processment don't stop
	 * during mult-threading
	 */
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			//load About.fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			//Catch a mainScene
			Scene mainScene = Main.getMainScene();
			
			/*Acess the first item on mainScene(ScrollPane) and after we acess 
			 * the content of this ScrollPane(VBox) and add to mainVBox.
			 */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//Acess children of mainVBox and get the first item(MenuBar) and add to mainMenu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//We clear this children
			mainVBox.getChildren().clear();
			
			//we add mainMenu(MenuBar) to children
			mainVBox.getChildren().add(mainMenu);
			
			//And we add the children of newVBox to children
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//returns a generics controller
			T controller = loader.getController();
			
			//initialize controller
			initializingAction.accept(controller);
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
