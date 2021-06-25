package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static Scene mainScene;
	
	/*Implementing abstract method from Application
	 * class. We load and show a MainView.fxml. 
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//load MainView
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			//methods to fix menubar
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			//create a scene with an empty AnchorPane
			mainScene = new Scene(scrollPane);
			
			//Set a mainscene and a title.
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFx application");
			primaryStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	//main method
	public static void main(String[] args) {
		//Static method to start JavaFx
		launch(args);
	}
}
