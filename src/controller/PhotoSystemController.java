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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import photos15.PhotoThumbnail;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Extends Controller class to represent Photo function
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class PhotoSystemController extends Controller implements Initializable{
    @FXML private ListView<Photo> images_list;
    @FXML private TextField status_ta;
    @FXML private ChoiceBox<Tag> tags_cb;
    @FXML private TextField cap_tf;
    @FXML private TextField date_tf;
    @FXML private TextField tagtype_tf;
    @FXML private TextField tagval_tf;
    @FXML private TextField albname_tf;
    @FXML private Button add_tag_btn;
    @FXML private Button del_tag_btn;
    @FXML private Button edit_cap_btn;
    @FXML private ImageView img;

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

        if(album.getPhotos().size() > 0){
            images_list.getSelectionModel().selectFirst();
            //displayDetails(images_list.getSelectionModel().getSelectedItem());
            //set tags dropdown list
            tags_cb.setItems(FXCollections.observableList(images_list.getSelectionModel().getSelectedItem().getTags()));
        }

    }

    public void addBtnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Photo");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png"))){
                status_ta.setText("Invalid file type: " + extension);
                return;
            }
            //make photo
            Photo photoToAdd = new Photo(selectedFile.getName(),selectedFile);
            //check for duplicates here
            boolean res = album.addPhoto(photoToAdd);
            if(res)
            {
                images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
                updateData();
            }
            else{
                status_ta.setText("Duplicate Photo Found");
            }

        }
    }

    public void removeBtnAction(ActionEvent event) {
        images_list.setDisable(true);
        Photo toDelete = images_list.getSelectionModel().getSelectedItem();
        if(album.removePhoto(toDelete)){
            status_ta.setText("success");
            images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
            updateData();
        }
        else
        {
            status_ta.setText("action failed");
        }

        images_list.setDisable(false);
    }

    public void openBtnAction(ActionEvent actionEvent) {
        Photo toOpen = images_list.getSelectionModel().getSelectedItem();
        displayDetails(toOpen);
        img.setImage(new Image(toOpen.getPhotoFile().toURI().toString(),340,340,true,true));
    }

    public void addTagBtnAction(ActionEvent actionEvent) {
        //on click, make all text fields disappear, bring focus to tag text fields
        if(add_tag_btn.getText().equals("Add Tag")) {
            images_list.setDisable(true);
            images_list.setEditable(false);
            cap_tf.setDisable(true);
            date_tf.setDisable(true);
            albname_tf.setDisable(true);
            tags_cb.setDisable(true);
            del_tag_btn.setVisible(false);
            edit_cap_btn.setVisible(false);
            tagtype_tf.setEditable(true);
            tagval_tf.setEditable(true);
            tagval_tf.requestFocus();
            tagtype_tf.requestFocus();
            tagval_tf.setPromptText("Enter A Tag Value");
            tagtype_tf.setPromptText("Enter A Tag Type");
            add_tag_btn.setText("Confirm");
        }
        else if(add_tag_btn.getText().equals("Confirm")) {
            //error check, do no allow duplicate tags
            Tag t = new Tag(tagtype_tf.getText(), tagval_tf.getText());
            images_list.getSelectionModel().getSelectedItem().addTag(t);
            add_tag_btn.setText("Add Tag");
            updateData();
            resetFields();
        }
    }

    public void editCaptionBtnAction(ActionEvent actionEvent) {
        if(edit_cap_btn.getText().equals("Edit Caption")) {
            images_list.setDisable(true);
            images_list.setEditable(false);
            tags_cb.setDisable(true);
            date_tf.setDisable(true);
            tagtype_tf.setDisable(true);
            tagval_tf.setDisable(true);
            albname_tf.setDisable(true);
            del_tag_btn.setVisible(false);
            add_tag_btn.setVisible(false);
            cap_tf.setEditable(true);
            cap_tf.requestFocus();
            cap_tf.setPromptText("Enter Caption for photo");
            edit_cap_btn.setText("Confirm");
        }
        else if(edit_cap_btn.getText().equals("Confirm")){
            images_list.getSelectionModel().getSelectedItem().setCaption(cap_tf.getText());
            images_list.refresh();
            edit_cap_btn.setText("Edit Caption");
            updateData();
            resetFields();
        }
    }

    public void deleteTagBtnAction(ActionEvent actionEvent) {
    }

    public void cancelBtnAction(ActionEvent actionEvent) {
        resetFields();
    }

    /**
     * Quit application button functionality
     * @param event
     */
    public void quitBtnAction(ActionEvent event) {
        updateData();
        System.exit(1);
    }

    private void resetFields() {
        images_list.setDisable(false);
        images_list.setEditable(true);
        cap_tf.setDisable(false);
        cap_tf.setEditable(false);
        date_tf.setDisable(false);
        albname_tf.setDisable(false);
        tagtype_tf.setDisable(false);
        tagval_tf.setDisable(false);
        tagval_tf.setEditable(false);
        tagtype_tf.setEditable(false);
        tags_cb.setDisable(false);
        add_tag_btn.setVisible(true);
        del_tag_btn.setVisible(true);
        edit_cap_btn.setVisible(true);
        add_tag_btn.setText("Add Tag");
        del_tag_btn.setText("Delete Tag");
        edit_cap_btn.setText("Edit Caption");
        tagval_tf.setPromptText("");
        tagtype_tf.setPromptText("");
        cap_tf.setPromptText("");
        tagval_tf.setText("");
        tagtype_tf.setText("");
        cap_tf.setText(images_list.getSelectionModel().getSelectedItem().getCaption());
    }

    /**
     * Display photo details based on input photo
     * @param a photo details to display
     */
    private void displayDetails(Photo a){
        if(a == null){
            cap_tf.setText("null");
            date_tf.setText("null");
            albname_tf.setText("null");
            tags_cb.setItems(null);
        }else
        {
            cap_tf.setText(a.getCaption());
            date_tf.setText(a.getDateString());
            albname_tf.setText(album.getName());
            tags_cb.setItems(FXCollections.observableList(a.getTags()));
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

    public void backBtnAction(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userSystem.fxml"));
            Parent parent = (Parent) loader.load();
            UserSystemController controller = loader.getController();
            Scene photoScene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            controller.initData(this.userList, user);
            stage.setScene(photoScene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
