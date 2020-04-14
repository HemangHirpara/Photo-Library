package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PhotoSystemController extends Controller implements Initializable{
    @FXML private ListView images_list;
    @FXML private TextArea status_ta;

    private Stage stage;
    private User user;
    private Album album;
    private ObservableList<Photo> observablePhotoList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(List<User> userList, User user, Album album, Stage stage){
        this.stage = stage;
        this.user = user;
        this.album = album;
        //get photos from current album and add to observable list and display
        for(Photo p : album.getPhotos()) {
            observablePhotoList.add(p);
        }
        images_list.setItems(observablePhotoList);
    }

    public void addBtnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Photo");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            System.out.println(selectedFile.getName() + " : " + selectedFile.getPath());
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png"))){
                status_ta.setText("Invalid file type: " + extension);
                return;
            }
            //create a photo object from input, gave it a random caption for now
            Photo photoToAdd = new Photo(selectedFile, "test photo");
            observablePhotoList.add(photoToAdd);
            images_list.setItems(observablePhotoList);
            images_list.refresh();
        }
    }
}
