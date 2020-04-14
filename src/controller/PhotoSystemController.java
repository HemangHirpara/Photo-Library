package controller;

import javafx.collections.FXCollections;
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
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.User;
import photos15.PhotoThumbnail;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class PhotoSystemController extends Controller implements Initializable{
    @FXML private ListView<Photo> images_list;
    @FXML private TextArea status_ta;

    private Stage stage;
    private User user;
    private Album album;
    private List<User> userList;
    private Album photoAlbum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(List<User> userList, User user, Album album, Stage stage){
        this.stage = stage;
        this.user = user;
        this.userList = userList;
        this.album = album;
        images_list.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
            @Override
            public ListCell<Photo> call(ListView<Photo> photoListView) {
                return new PhotoThumbnail();
            }
        });
        images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
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
            //get image
            Image newImage = new Image(selectedFile.toURI().toString());
            // get date
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(selectedFile.lastModified());
            //make photo
            Photo photoToAdd = new Photo(selectedFile.getName(),selectedFile);
            images_list.getItems().add(photoToAdd);
            album.getPhotos().add(photoToAdd);
            updateData();
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
}
