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
import java.util.ArrayList;
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
    @FXML private ComboBox<Tag> tags_cb;
    @FXML private ComboBox<String> tagtypes_cb;
    @FXML private TextField cap_tf, date_tf, tagtype_tf, tagval_tf, albname_tf, new_name;
    @FXML private Button add_tag_btn, del_tag_btn,edit_cap_btn, cancel_btn1, new_tag_btn;
    @FXML private Button add_btn, remove_btn, move_btn, copy_btn, cancel_btn, create_alb, cancel3_btn;
    @FXML private Label move_lbl;
    @FXML private ImageView img;
    @FXML private ComboBox album_cb;
    @FXML private CheckBox isSingle_cb;
    @FXML private Label photos_lbl;

    private Stage stage;
    private User user;
    private Album album;
    private List<User> userList;
    private List<String> tagTypes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Initialize application with data passed. Initial view of photo system with details populated
     * @param userList List of users allowed on Photo system
     * @param user current user logged in
     * @param album album to view
     * @param stage stage to freeze when adding new photo
     */
    public void initData(List<User> userList, User user, Album album, Stage stage) {
        this.stage = stage;
        this.user = user;
        this.userList = userList;
        this.album = album;
        photos_lbl.setText(album.getName() + " - Photos");
        images_list.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
            @Override
            public ListCell<Photo> call(ListView<Photo> photoListView) {
                return new PhotoThumbnail();
            }
        });

        images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));

        album_cb.setItems(FXCollections.observableList(user.getAlbums()));
        if (user.getAlbums().size() > 0)
            album_cb.getSelectionModel().selectFirst();

        if(album.getName().equals("Search Results")){
            create_alb.setVisible(true);
            new_name.setVisible(true);
        }
    }

    /**
     * Display Tag details for photo
     */
    public void tagSelected() {
        if(tags_cb.getSelectionModel().getSelectedItem() != null){
            tagtype_tf.setText(tags_cb.getSelectionModel().getSelectedItem().getName());
            tagval_tf.setText(tags_cb.getSelectionModel().getSelectedItem().getValue());
        }
        else{
            tagtype_tf.setText("");
            tagval_tf.setText("");
        }
    }

    /**
     * Display tag type in tag type field
     */
    public void displayTagType() {
        if(tagtypes_cb.getItems().size() != 0)
            tagtype_tf.setText(tagtypes_cb.getSelectionModel().getSelectedItem());
    }

    /**
     * Delete tag button functionality, allow a tag to be deleted from an image
     */
    public void deleteTagBtnAction() {
        Photo p = images_list.getSelectionModel().getSelectedItem();
        Tag t = tags_cb.getSelectionModel().getSelectedItem();
        if(p == null || t == null){
            status_ta.setText("no image/tag selected");
            resetFields();
            return;
        }
        boolean res = p.deleteTag(t);
        if(res){
            status_ta.setText("tag deleted");
            ObservableList<Tag> tagList = FXCollections.observableList(p.getTags());
            tags_cb.setItems(tagList);
            updateData();
        }
        else
            status_ta.setText("delete failed");
    }

    /**
     * Add tag button functionality, add new tag to a photo from list of tag types available
     */
    public void addTagBtnAction() {
        if(add_tag_btn.getText().equals("Add Tag")){
            cap_tf.setDisable(true);
            date_tf.setDisable(true);
            albname_tf.setDisable(true);
            tags_cb.setDisable(true);
            del_tag_btn.setVisible(false);
            edit_cap_btn.setVisible(false);
            cancel_btn1.setVisible(true);
            tagtypes_cb.setDisable(false);
            tagval_tf.setEditable(true);
            tagval_tf.requestFocus();
            add_tag_btn.setText("Confirm");
            new_tag_btn.setVisible(false);
            tagtype_tf.setDisable(true);
            images_list.setDisable(true);
            if(tagtypes_cb.getItems().size() == 0){
                status_ta.setText("No tag types, create new tag");
                resetFields();
            }
            else{
                tagtypes_cb.getSelectionModel().selectFirst();
                tagtype_tf.setText(tagtypes_cb.getSelectionModel().getSelectedItem());
            }
        }
        else {
            if(tagtypes_cb.getSelectionModel().getSelectedItem().equals("")){
                status_ta.setText("invalid type");
                resetFields();
            }
            else if(tagval_tf.getText().equals("")){
                status_ta.setText("invalid value");
                resetFields();
            }
            else {
                // get photo
                Photo p = images_list.getSelectionModel().getSelectedItem();
                //make new tag
                Tag newTag = new Tag(tagtypes_cb.getSelectionModel().getSelectedItem(), tagval_tf.getText());
                // check if the tag is single val and already exists
                if(p.getTagTypes().get(newTag.getName())){
                    for(Tag t : p.getTags()){
                        if(t.getName().equals(newTag.getName())) {
                            status_ta.setText("Can only have one");
                            resetFields();
                            return;
                        }
                    }
                }
                if(p.addTag(newTag)){
                    status_ta.setText("tag added");
                    ObservableList<Tag> tagList = FXCollections.observableList(p.getTags());
                    tags_cb.setItems(tagList);
                    updateData();
                }
                else
                    status_ta.setText("tag already exists");
                tagtypes_cb.getSelectionModel().clearSelection();
                resetFields();
            }
        }
    }

    /**
     * New tag button functionality, create a new tag type, either single or multivalued
     */
    public void newTagBtnAction() {
        if(new_tag_btn.getText().equals("New Tag")){
            if(images_list.getSelectionModel().getSelectedItem() == null) {
                status_ta.setText("No image selected");
                return;
            }
            cap_tf.setDisable(true);
            date_tf.setDisable(true);
            albname_tf.setDisable(true);
            tags_cb.setDisable(true);
            add_tag_btn.setVisible(false);
            del_tag_btn.setVisible(false);
            edit_cap_btn.setVisible(false);
            cancel_btn1.setVisible(true);
            tagtype_tf.setEditable(true);
            tagtype_tf.requestFocus();
            tagtypes_cb.setDisable(true);
            tagtype_tf.setPromptText("Tag Type");
            tagval_tf.setDisable(true);
            new_tag_btn.setText("Confirm");
            isSingle_cb.setVisible(true);
            isSingle_cb.setDisable(false);
            images_list.setDisable(true);
        }
        else {
            //create new tag and add to list
            Photo p = images_list.getSelectionModel().getSelectedItem();
            p.getTagTypes().put(tagtype_tf.getText(), isSingle_cb.isSelected());
            tagTypes = new ArrayList<>(p.getTagTypes().keySet());
            tagtypes_cb.setItems(FXCollections.observableList(tagTypes));
            updateData();
            tagval_tf.setDisable(false);
            add_tag_btn.setVisible(true);
            new_tag_btn.setText("New Tag");
            resetFields();
        }
    }

    /**
     * Add button functionality, add a new image to album, only allow jpg, jpeg, and png image formats
     */
    public void addBtnAction() {
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
            if(res) {
                images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
                updateData();
            }
            else
                status_ta.setText("Duplicate Photo Found");
        }
    }

    /**
     * Remove button functionality, remove selected image from album
     */
    public void removeBtnAction() {
        images_list.setDisable(true);
        Photo toDelete = images_list.getSelectionModel().getSelectedItem();
        if(album.removePhoto(toDelete)){
            status_ta.setText("success");
            images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
            updateData();
        }
        else
            status_ta.setText("action failed");

        images_list.setDisable(false);
    }

    /**
     * Open button functionality to open selected image in display area
     */
    public void openBtnAction() {
        Photo toOpen = images_list.getSelectionModel().getSelectedItem();
        if(toOpen == null)
            status_ta.setText("There are no photos");
        else {
            displayDetails(toOpen);
            img.setImage(new Image(toOpen.getPhotoFile().toURI().toString(), 700, 350, true, true));
        }
    }

    /**
     * Edit caption button functionality, allow changing of image caption
     */
    public void editCaptionBtnAction() {
        if(images_list.getSelectionModel().getSelectedItem() == null){
            status_ta.setText("no image selected");
            resetFields();
            return;
        }
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
            cancel_btn1.setVisible(true);
            cap_tf.requestFocus();
            cap_tf.setPromptText("Enter Caption for photo");
            edit_cap_btn.setText("Confirm");
        }
        else if(edit_cap_btn.getText().equals("Confirm")){
            images_list.getSelectionModel().getSelectedItem().setCaption(cap_tf.getText());
            images_list.refresh();
            edit_cap_btn.setText("Edit Caption");
            status_ta.setText("Caption Updated");
            updateData();
            resetFields();
        }
    }

    /**
     * Cancel button functionality for photo tag/caption functions
     */
    public void cancelBtnAction() {
        resetFields();
    }

    /**
     * Quit application button functionality
     */
    public void quitBtnAction() {
        updateData();
        System.exit(1);
    }

    /**
     * Reset buttons, textfields, and listview to initial values, for photo tag/caption functions
     */
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
        cancel_btn1.setVisible(false);
        isSingle_cb.setVisible(false);
        tagtypes_cb.setDisable(true);
        new_tag_btn.setVisible(true);
        new_tag_btn.setText("New Tag");
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
            ObservableList<Tag> tagList = FXCollections.observableList(a.getTags());
            tags_cb.setItems(tagList);
            tags_cb.getSelectionModel().selectFirst();
            // sets the tag types
            tagTypes = new ArrayList<>(a.getTagTypes().keySet());
            tagtypes_cb.setItems(FXCollections.observableList(tagTypes));
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

    /**
     * Back button functionality to return to previous window
     * @param event onclick event, change to previous user view
     */
    public void backBtnAction(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userSystem.fxml"));
            Parent parent = loader.load();
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

    /**
     * Move button functionality to move, thereby deleting, an image from current album to another album
     */
    public void moveBtnAction() {
        images_list.setDisable(true);
        if(move_btn.getText().equals("Move")) {
            move_btn.setText("Confirm");
            copy_btn.setVisible(false);
            move_cpy_reset(true);
        }else
        {
            Album toMoveto = (Album) album_cb.getSelectionModel().getSelectedItem();
            Photo toMove = images_list.getSelectionModel().getSelectedItem();
            if(toMoveto.equals(album)|| toMove == null || toMoveto.getPhotos().contains(toMove))
            {
                status_ta.setText("cannot move ");
                move_btn.setText("Move");
                images_list.setDisable(false);
                copy_btn.setVisible(true);
                move_cpy_reset(false);
                return;
            }
            copy_btn.setVisible(true);
            move_cpy_reset(false);
            toMoveto.addPhoto(toMove);
            album.removePhoto(toMove);
            images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
            updateData();
            move_btn.setText("Move");
            images_list.setDisable(false);
            status_ta.setText("moved photo");
        }

    }

    /**
     * Copy button functionality to copy, without removing, an image from current album to another album
     */
    public void copyBtnAction() {
        images_list.setDisable(true);
        if(copy_btn.getText().equals("Copy")) {
            copy_btn.setText("Confirm");
            move_btn.setVisible(false);
            move_cpy_reset(true);
        }else
        {
            Album toCopyto = (Album) album_cb.getSelectionModel().getSelectedItem();
            Photo toCopy = images_list.getSelectionModel().getSelectedItem();
            if(toCopyto.equals(album) || toCopy == null || toCopyto.getPhotos().contains(toCopy))
            {
                status_ta.setText("cannot copy");
                copy_btn.setText("Copy");
                images_list.setDisable(false);
                move_btn.setVisible(true);
                move_cpy_reset(false);
                return;
            }
            toCopyto.addPhoto(toCopy);
            images_list.setItems(FXCollections.observableArrayList(album.getPhotos()));
            updateData();
            move_btn.setVisible(true);
            move_cpy_reset(false);
            copy_btn.setText("Copy");
            images_list.setDisable(false);
            status_ta.setText("copied photo");

        }
    }

    /**
     * Reset fields and buttons modified by move and copy function
     * @param b boolean reset fields
     */
    public void move_cpy_reset(boolean b){
        move_lbl.setVisible(b);
        album_cb.setVisible(b);
        add_btn.setVisible(!b);
        remove_btn.setVisible(!b);
        cancel_btn.setVisible(b);
    }

    /**
     * Cancel move and copy actions and reset values
     */
    public void move_cpy_cancel() {
        status_ta.setText("cancelled move/copy action");
        copy_btn.setText("Copy");
        move_btn.setText("Move");
        copy_btn.setVisible(true);
        move_btn.setVisible(true);
        images_list.setDisable(false);
        move_cpy_reset(false);
    }

    /**
     * Slideshow button functionality, opens a window to show slideshow
     * @param event onclick event, change to slideshow view
     */
    public void slideshow(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/slideshow.fxml"));
            Parent parent = loader.load();
            SlideshowController controller = loader.getController();
            Scene photoScene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            if(album.getPhotos().size() == 0) {
                status_ta.setText("Album has no images to show");
                return;
            }
            controller.initData(this.userList, user, album);
            stage.setScene(photoScene);
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Create new album button functionality, creates new album from search results
     */
    public void createAlbum() {
        if(create_alb.getText().equals("Create Album")){
            create_alb.setText("Confirm");
            cancel3_btn.setVisible(true);
        }else {
            String newName = new_name.getText();
            if(newName.equals(""))
            {
                status_ta.setText("No name inputted");
                cancel3_btn.setVisible(false);
                create_alb.setText("Create Album");
                return;
            }
            album.setName(newName);
            for (Album a : user.getAlbums()) {
                if(a.equals(album)){
                    status_ta.setText("duplicate album name");
                    create_alb.setText("Create Album");
                    cancel3_btn.setVisible(false);
                    new_name.setText("");
                    return;
                }
            }
            user.getAlbums().add(album);
            photos_lbl.setText(album.getName() + " - Photos");
            cancel3_btn.setVisible(false);
            create_alb.setText("Create Album");
            updateData();
        }

    }

    /**
     * Cancel create new album function
     */
    public void cancelNewAlbum() {
        cancel3_btn.setVisible(false);
        status_ta.setText("cancelled create album");
        create_alb.setText("Create Album");
    }
}
