package photos15;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main Class to run photo library. Handles a single user, stores information for multiple users with serialized data
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene loginScene = new Scene(root);

            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Photos15");
            primaryStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
