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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
     * @param event
     */
    public void quitBtnAction(ActionEvent event) {
        updateData();
        System.exit(1);
    }

    public void backBtnAction(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoSystem.fxml"));
            Parent parent = (Parent) loader.load();
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

    public void nextImage(ActionEvent actionEvent) {
        if(counter+1<image_list.size()) {
            counter++;
            img_slide.setImage(new Image(image_list.get(counter).getPhotoFile().toURI().toString(), 340, 340, true, true));
            cap_lbl.setText(image_list.get(counter).getCaption());
        }
        return;
    }

    public void prevImage(ActionEvent actionEvent) {
        if(counter-1 >= 0){
            counter--;
            img_slide.setImage(new Image(image_list.get(counter).getPhotoFile().toURI().toString(),340,340,true,true));
            cap_lbl.setText(image_list.get(counter).getCaption());
        }
        return;
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
