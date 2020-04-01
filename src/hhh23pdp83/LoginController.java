package hhh23pdp83;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author hhh23 and pdp83
 * Controller for the login page
 */
public class LoginController implements Initializable {

    @FXML private TextField username_tf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * handles which scene to goto depending on the username
     * @param event on login_btn pressed
     * @throws IOException incase there is a error setting or creating the new scene
     */
    public void loginBtnAction(ActionEvent event) throws IOException {
        // this gets the stage created in the main() of Photos.java
        if(username_tf.getText().equals("")){
            username_tf.setText("cannot be blank");
            return;
        }
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        if(username_tf.getText().equals("admin")){
            Parent adminSystemView = FXMLLoader.load(getClass().getResource("adminSystem.fxml"));
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
}
