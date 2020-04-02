package hhh23pdp83;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author hhh23 and pdp83
 * Controller for the login page
 */
public class LoginController implements Initializable {

    @FXML private TextField username_tf;
    ObservableList<User> users = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        users.add(new User("Poojan"));
        users.add(new User("hemang"));
    }

    private ObservableList<User> loadUsers() {
        // temp code
        ObservableList<User> temp = FXCollections.observableArrayList();

        return temp;
    }

    /**
     * handles which scene to goto depending on the username
     * @param event on login_btn pressed
     * @throws IOException incase there is a error setting or creating the new scene
     */
    public void loginBtnAction(ActionEvent event) throws IOException {
        // this gets the stage created in the main() of Photos.java
        FXMLLoader loader = new FXMLLoader();
        if(username_tf.getText().equals("")){
            username_tf.setText("cannot be blank");
            return;
        }
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        if(username_tf.getText().equals("admin")){
            // get location of the fxml's controller
            loader.setLocation(getClass().getResource("adminSystem.fxml"));
            // load the loader
            Parent adminSystemView = loader.load();
            // get controller
            AdminSystemController adminController = loader.getController();
            // use the initData() created to pass in the data
            adminController.initData();
            Scene adminSystemScene = new Scene(adminSystemView);
            window.setScene(adminSystemScene);
            window.show();
        }
        else
        {
            /**
             * goto User view
             * Still need to figure out how to pass information between scenes
             */
        }
    }

    public ObservableList<User> getUsers() { return this.users; }
    public void setUsers(ObservableList<User> users) { this.users = users; }
}
