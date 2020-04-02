package hhh23pdp83;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import javafx.collections.ObservableList;

import java.io.IOException;

public class AdminSystemController {
    @FXML private TextField newUsername_tf;
    @FXML private Button createUser_btn;
    @FXML private Button deleteUser_btn;
    @FXML private Button confirm_btn;
    @FXML private Button cancel_btn;
    @FXML private Button logout_btn;
    @FXML private Button quit_btn;
    @FXML private TextArea status_ta;
    @FXML private TableView users_table;

    public void initData(){
        LoginController a = new LoginController();
        for(User b:  a.getUsers())
            System.out.println(b.getUsername());
    }

    /**
     * handles which scene to goto depending on the username
     * @param event on login_btn pressed
     * @throws IOException incase there is a error setting or creating the new scene
     */
    public void logoutBtnAction(ActionEvent event) throws IOException {
        // this gets the stage created in the main() of Photos.java
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent loginView = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene loginScene = new Scene(loginView);
        window.setScene(loginScene);
        window.show();
    }

}
