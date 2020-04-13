package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.List;

public class AdminSystemController extends Controller{
    @FXML private TextField newUsername_tf;
    @FXML private Button createUser_btn;
    @FXML private Button deleteUser_btn;
    @FXML private Button cancel_btn;
    @FXML private TextArea status_ta;
    @FXML private ListView users_list;


    private List<User> userList;
    private ObservableList<User> observableUserList;


    public void initData(List<User> userList){
        this.userList = userList;
        observableUserList = FXCollections.observableList(this.userList);
        users_list.setItems(observableUserList);
        this.users_list.getSelectionModel().select(0);

    }

    public void createBtnAction(ActionEvent actionEvent) {
        status_ta.setText("Creating user");
        if(createUser_btn.getText().equals("Create User")){
            users_list.setDisable(true);
            deleteUser_btn.setVisible(false);
            cancel_btn.setVisible(true);
            createUser_btn.setText("Confirm");
            newUsername_tf.setVisible(true);
        }
        else
        {
            String new_usr = newUsername_tf.getText();
            // go through to check for duplicates
            for(User user : observableUserList){
                if(user.getUsername().equals(new_usr)){
                    status_ta.setText("user already exists");
                    resetFields();
                    return;
                }
            }

            this.userList.add(new User(new_usr));
            observableUserList = FXCollections.observableList(userList);
            users_list.setItems(observableUserList);
            users_list.refresh();
            updateData();
            status_ta.setText("User added successfully");
            resetFields();
        }

    }

    public void deleteBtnAction(ActionEvent actionEvent) {
        status_ta.setText("Deleting selected user");
        if(deleteUser_btn.getText().equals("Delete User")){
            if(users_list.getSelectionModel().getSelectedItem() == null){
                resetFields();
                return;
            }
            users_list.setDisable(true);
            createUser_btn.setVisible(false);
            deleteUser_btn.setText("Confirm");
            cancel_btn.setVisible(true);
        }
        else{
            observableUserList.remove(users_list.getSelectionModel().getSelectedItem());
            users_list.setItems(observableUserList);
            users_list.refresh();
            status_ta.setText("Delete Successful.");
            updateData();
            resetFields();
        }
    }

    private void updateData() {
        try {
            FileOutputStream fos = new FileOutputStream(getDataPath());
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(userList);
            ous.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetFields() {
        users_list.setDisable(false);
        createUser_btn.setVisible(true);
        deleteUser_btn.setVisible(true);
        createUser_btn.setText("Create User");
        deleteUser_btn.setText("Delete User");
        cancel_btn.setVisible(false);
        newUsername_tf.setText("");
        newUsername_tf.setVisible(false);
    }


    public void cancelBtnAction(ActionEvent actionEvent) {
        status_ta.setText("Cancelled Action");
        resetFields();
    }

    @FXML
    public void quitBtnAction(ActionEvent event) {
        updateData();
        System.exit(1);
    }
}
