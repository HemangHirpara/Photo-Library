package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Abstract class to represent controller for each view
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public abstract class Controller {
    private final String dataPath = "data/data.dat";

    public String getDataPath() {
        return dataPath;
    }

    /**
     * Handles functionality when logout button is pressed. Saves all data in application
     * @param event
     * @throws IOException
     */
    public void logoutBtnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent parent = (Parent) loader.load();
        Scene loginScene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }
}
