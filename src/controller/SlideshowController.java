package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Extends Controller class to represent Slideshow function
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class SlideshowController extends Controller implements Initializable {

    @FXML private ImageView img_slide;
    @FXML private Label cap_lbl;
    List<User> userList;
    User user;
    Album album;
    List<Photo> image_list;
    int counter;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Initialize Slideshow album to show
     * @param userList list of users allowed on photo system
     * @param user current user logged in
     * @param album album to display in slideshow
     */
    public void initData(List<User> userList, User user, Album album){
        this.userList = userList;
        this.user = user;
        this.album = album;
        this.image_list = album.getPhotos();
        counter = 0;
        img_slide.setImage(new Image(image_list.get(counter).getPhotoFile().toURI().toString(),340,340,true,true));
        cap_lbl.setText(image_list.get(counter).getCaption());
    }

    /**
     * Quit application button functionality
     */
    public void quitBtnAction() {
        updateData();
        System.exit(1);
    }

    /**
     * Back button functionality to return to previous window
     * @param event onclick event to return to previous user view
     */
    public void backBtnAction(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoSystem.fxml"));
            Parent parent = loader.load();
            PhotoSystemController controller = loader.getController();
            Scene photoScene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            controller.initData(this.userList, user, album, stage);
            stage.setScene(photoScene);
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Next button functionality to move slideshow to next image in album
     */
    public void nextImage() {
        if(counter+1<image_list.size()) {
            counter++;
            img_slide.setImage(new Image(image_list.get(counter).getPhotoFile().toURI().toString(), 340, 340, true, true));
            cap_lbl.setText(image_list.get(counter).getCaption());
        }
    }

    /**
     * Previous button functionality to move slideshow to previous image in album
     */
    public void prevImage() {
        if(counter-1 >= 0){
            counter--;
            img_slide.setImage(new Image(image_list.get(counter).getPhotoFile().toURI().toString(),340,340,true,true));
            cap_lbl.setText(image_list.get(counter).getCaption());
        }
    }

    /**
     * Update data into data.dat file for serialization
     */
    private void updateData() {
        try {
            FileOutputStream fos = new FileOutputStream(getDataPath());
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(userList);
            ous.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
