package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
//import javafx.scene.input.KeyCombination;
//import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Main extends Application {
		//A stage is the container for everything else, like a JFrame swing
		// Scene is like a JPanel in swing
		//scene-graph hierarchical tree of node, it is a tree data struc. that holds and arranges components like buttons text boxes and images
		public static void main(String[] args) {
			Application.launch(args);
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("mainBuilder.fxml"));
			Scene mainScene = new Scene(root, Color.BEIGE);
			mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			//Use below if I have multiple scenes.
//			String css = this.getClass().getResource("application.css").toExternalForm());
//			scene.getStylesheets().add(css);
			
//			Image icon = new Image("icon.png");
//			stage.getIcons().add(icon);
			stage.setResizable(false);
	
			stage.setScene(mainScene);
			stage.show();
				
			
			
			
		}

}
