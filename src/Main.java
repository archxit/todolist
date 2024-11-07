import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("todo.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 450, 730);
            scene.getStylesheets().add(getClass().getResource("todo.css").toExternalForm());
            primaryStage.setTitle("TaskEZ");
            Image icon = new Image("TaskEZ.png");
            
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}