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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


/**
 * Extends Controller class to represent User function
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class UserSystemController extends Controller implements Initializable {
    @FXML private TextField name_tf, numPhotos_tf, start_tf, end_tf;
    @FXML private Button create_btn,delete_btn, edit_btn, cancel_btn, open_btn;
    @FXML private Button searchTag_btn, searchDate_btn, add_tag_btn, del_tag_btn;
    @FXML private CheckBox and_cb, or_cb;
    @FXML private ComboBox tagtype_cb, tagval_cb;
    @FXML private DatePicker from_date, to_date;
    @FXML private TextArea status_ta;
    @FXML private ListView album_list;

    private User curr_user;
    private List<User> userList;
    // all the albums the user has
    private List<Album> albums;
    // all the photos the user has
    private List<Photo> allPhotos;
    // all the tag types
    private List<String> tagTypes;
    //all the tag values
    private List<String> tagValues;
    private ObservableList<Album> observableAlbumList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Initialize fields for user that has logged into the system
     * @param userList list of users for system
     * @param user current user logged in
     */
    public void initData(List<User> userList, User user){
        this.userList = userList;
        this.curr_user = user;
        this.albums = curr_user.getAlbums();
        for(Album a: albums)
            a.updateDates();
        observableAlbumList = FXCollections.observableList(this.albums);
        this.album_list.setItems(observableAlbumList);

        if(albums.size() > 0){
            album_list.getSelectionModel().selectFirst();
            displayDetails((Album) album_list.getSelectionModel().getSelectedItem());
        }

        // fill allPhotos, tagTypes, tagValues
        allPhotos = new ArrayList<>();
        tagTypes = new ArrayList<>();
        tagValues = new ArrayList<>();

        for(Album a: albums){
            for(Photo photo : a.getPhotos()){
                if(!allPhotos.contains(photo))
                    allPhotos.add(photo);
                for(String type : photo.getTagTypes().keySet())
                    if(!tagTypes.contains(type))
                        tagTypes.add(type);
                // get all tagTypes and values
                for(Tag t : photo.getTags()){
                    //get all values
                    if(!tagValues.contains(t.getValue()))
                        tagValues.add(t.getValue());
                }
            }
        }

        tagtype_cb.setItems(FXCollections.observableList(tagTypes));
        tagval_cb.setItems(FXCollections.observableList(tagValues));
    }

    /**
     * Create album button functionality
     * @param event
     */
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

    /**
     * Edit album button functionality
     * @param event
     */
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

    /**
     * Delete album button functionality
     * @param event
     */
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

    /**
     * Cancel button functionality
     * @param event
     */
    public void cancelBtnAction(ActionEvent event) {
        status_ta.setText("action canceled");
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

    /**
     * Open album button functionality
     * @param event
     */
    public void openBtnAction(ActionEvent event) {
        Album toOpen = (Album) album_list.getSelectionModel().getSelectedItem();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoSystem.fxml"));
            Parent parent = (Parent) loader.load();
            PhotoSystemController controller = loader.getController();
            Scene photoScene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            if(toOpen == null) {
                status_ta.setText("Select an album to open");
                return;
            }
            controller.initData(this.userList, curr_user, toOpen, stage);
            stage.setScene(photoScene);
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Display album details on mouse click
     * @param mouseEvent
     */
    public void displayDetails(MouseEvent mouseEvent) {
        Album temp = (Album) album_list.getSelectionModel().getSelectedItem();
        displayDetails(temp);
    }

    /**
     * Display album details based on input album
     * @param a album details to display
     */
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
            start_tf.setText((a.getNumPhotos() == 0) ? "-" : a.getStartString());
            end_tf.setText((a.getNumPhotos() == 0) ? "-" : a.getEndString());
        }
    }

    /**
     * Reset text fields and buttons to initial values
     */
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTagBtn(ActionEvent actionEvent) {
    }

    public void delTagBtn(ActionEvent actionEvent) {
    }

    public void searchTag(ActionEvent actionEvent) {
    }

    public void searchDate(ActionEvent event) {
        if(searchDate_btn.getText().equals("Search by Date")){
            searchDate_btn.setText("Go");
            from_date.setDisable(false);
            to_date.setDisable(false);
        }
        else
        {
            searchDate_btn.setText("Search by Date");
            from_date.setDisable(true);
            to_date.setDisable(true);
            if(from_date.getValue() == null || to_date.getValue() == null)
            {
                status_ta.setText("missing date range");
                return;
            }
            Date lowerLimit = Date.from(from_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date upperLimit = Date.from(to_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if(lowerLimit.compareTo(upperLimit) > 0)
            {
                status_ta.setText("end cannot come before the beginning");
                return;
            }

            // make result
            List<Photo> result = new ArrayList<>();
            for(Photo p : allPhotos){
                System.out.println(p.getDateTaken().toString());
                if(p.getDateTaken().compareTo(lowerLimit) >= 0 && p.getDateTaken().compareTo(upperLimit) <= 0){
                    result.add(p);
                }
            }

            if(result.size() == 0)
                status_ta.setText("No photos found");
            else {
                Album resultAlbum = new Album("Search Results");
                resultAlbum.setPhotos(result);
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoSystem.fxml"));
                    Parent parent = (Parent) loader.load();
                    PhotoSystemController controller = loader.getController();
                    Scene photoScene = new Scene(parent);
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    controller.initData(this.userList, curr_user, resultAlbum, stage);
                    stage.setScene(photoScene);
                    stage.centerOnScreen();
                    stage.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}

