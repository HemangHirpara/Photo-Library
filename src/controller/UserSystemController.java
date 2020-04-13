package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Album;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.spec.ECField;
import java.util.List;
import java.util.ResourceBundle;

public class UserSystemController extends Controller implements Initializable {
    @FXML private TextField name_tf, numPhotos_tf, start_tf, end_tf;
    @FXML private Button create_btn,delete_btn, edit_btn, cancel_btn, open_btn;
    @FXML private TextArea status_ta;
    @FXML private ListView album_list;;


    private User curr_user;
    private List<User> userList;
    private List<Album> albums;
    private ObservableList<Album> observableAlbumList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(List<User> userList, User user){
        this.userList = userList;
        this.curr_user = user;
        this.albums = curr_user.getAlbums();
        observableAlbumList = FXCollections.observableList(this.albums);
        this.album_list.setItems(observableAlbumList);
    }

    public void createBtnAction(ActionEvent event) {
        status_ta.setText("creating Album");
        if(create_btn.getText().equals("Create")){
            create_btn.setText("Confirm");
            delete_btn.setVisible(false);
            edit_btn.setVisible(false);
            cancel_btn.setVisible(true);
            name_tf.setEditable(true);
            name_tf.setText("enter new name here");
            name_tf.requestFocus();
        }
        else
        {
            String alb = name_tf.getText();
            for(Album a : albums){
                if(a.getName().equals(alb)){
                    status_ta.setText("albums already exists");
                    resetFields();
                    return;
                }
            }

            this.albums.add(new Album(alb));
            curr_user.setAlbums(this.albums);
            observableAlbumList = FXCollections.observableList(albums);
            album_list.setItems(observableAlbumList);
            album_list.refresh();
            updateData();
            resetFields();
        }

    }

    public void edit_BtnAction(ActionEvent event) {
        status_ta.setText("editing Album");
        String org = "";
        if(edit_btn.getText().equals("Edit")){
            if(album_list.getSelectionModel().getSelectedItem() == null){
                resetFields();
                status_ta.setText("no albums to edit");
                return;
            }
            edit_btn.setText("Confirm");
            delete_btn.setVisible(false);
            create_btn.setVisible(false);
            cancel_btn.setVisible(true);
            name_tf.setEditable(true);
            name_tf.setText("enter new name here");
            name_tf.requestFocus();
            album_list.setDisable(true);
            org = album_list.getSelectionModel().getSelectedItem().toString();
        }
        else {
            String newName = name_tf.getText();
            //check if new name is already there
            for(Album a: albums){
                if(newName.equals(a.getName()) || newName.equals(org)){
                    status_ta.setText("try another name");
                    return;
                }
            }
            Album toEdit = (Album) album_list.getSelectionModel().getSelectedItem();
            toEdit.setName(newName);
            album_list.refresh();
            updateData();
            resetFields();
        }
    }

    public void deleteBtnAction(ActionEvent event) {
        status_ta.setText("deleting Album");
        if(delete_btn.getText().equals("Delete")){
            if(album_list.getSelectionModel().getSelectedItem() == null){
                resetFields();
                return;
            }
            delete_btn.setText("Confirm");
            create_btn.setVisible(false);
            edit_btn.setVisible(false);
            cancel_btn.setVisible(true);
        }
        else
        {
            this.albums.remove(album_list.getSelectionModel().getSelectedItem());
            curr_user.setAlbums(this.albums);
            observableAlbumList = FXCollections.observableList(albums);
            album_list.setItems(observableAlbumList);
            album_list.refresh();
            updateData();
            resetFields();
        }
    }

    public void cancelBtnAction(ActionEvent event) {
        status_ta.setText("action canceled");
        resetFields();
    }

    public void quitBtnAction(ActionEvent event) {
        updateData();
        System.exit(1);
    }

    public void openBtnAction(ActionEvent event) {
        Album toOpen = (Album) album_list.getSelectionModel().getSelectedItem();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoSystem.fxml"));
            Parent parent = (Parent) loader.load();
            PhotoSystemController controller = loader.getController();
            Scene photoScene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            controller.initData(this.userList, curr_user, toOpen);
            stage.setScene(photoScene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayDetails(MouseEvent mouseEvent) {
        Album temp = (Album) album_list.getSelectionModel().getSelectedItem();
        displayDetails(temp);

    }

    private void displayDetails(Album a){
        if(a == null){
            name_tf.setText("null");
            numPhotos_tf.setText("null");
            start_tf.setText("null");
            end_tf.setText("null");
        }else
        {
            name_tf.setText(a.getName());
            numPhotos_tf.setText(a.getNumPhotos()+"");
            start_tf.setText(a.getStartString());
            end_tf.setText(a.getEndString());
        }

    }

    private void resetFields() {
        name_tf.setEditable(false);
        create_btn.setText("Create");
        create_btn.setVisible(true);
        edit_btn.setText("Edit");
        edit_btn.setVisible(true);
        delete_btn.setText("Delete");
        delete_btn.setVisible(true);
        cancel_btn.setVisible(false);
        name_tf.setText("no album selected");
        numPhotos_tf.setText("no album selected");
        start_tf.setText("no album selected");
        end_tf.setText("no album selected");
        album_list.setDisable(false);
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
}

