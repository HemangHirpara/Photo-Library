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
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

import java.io.*;
import java.net.URL;
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
    @FXML private Button searchDate_btn, search_btn, searchAnd_btn, searchOr_btn;
    @FXML private ComboBox<String> tagtype1_cb,tagval1_cb,tagtype2_cb, tagval2_cb;
    @FXML private DatePicker from_date, to_date;
    @FXML private TextField status_ta;
    @FXML private ListView album_list, tagsList;

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

        tagtype1_cb.setItems(FXCollections.observableList(tagTypes));
        tagval1_cb.setItems(FXCollections.observableList(tagValues));
        tagtype2_cb.setItems(FXCollections.observableList(tagTypes));
        tagval2_cb.setItems(FXCollections.observableList(tagValues));
    }

    /**
     * Search albums for photos based on single tag name-value pair
     * @param event onclick event to open photo view with results
     */
    public void searchSingle(ActionEvent event) {
        if(search_btn.getText().equals("Search by Single Tag")){
            tagtype1_cb.setDisable(false);
            tagval1_cb.setDisable(false);
            search_btn.setText("Go");
            searchAnd_btn.setDisable(true);
            searchOr_btn.setDisable(true);
        }
        else {
            if(tagtype1_cb.getSelectionModel().getSelectedItem() == null|| tagval1_cb.getSelectionModel().getSelectedItem() == null)
            {
                status_ta.setText("missing value or type");
                resetSearch();
                return;
            }
            Tag one = new Tag(tagtype1_cb.getSelectionModel().getSelectedItem(), tagval1_cb.getSelectionModel().getSelectedItem());
            List<Photo> result = new ArrayList<>();
            for(Photo p : allPhotos){
                for(Tag t : p.getTags()){
                    if(t.equals(one)){
                        result.add(p);
                    }
                }
            }
            sendResults(event, result);
            resetSearch();
        }
    }

    /**
     * Search albums for photos based on conjunction of two tag name-value pair
     * @param event onclick event to open photo view with results
     */
    public void searchAnd(ActionEvent event) {
        if(searchAnd_btn.getText().equals("Search by Two Tags (AND)")){
            tagtype1_cb.setDisable(false);
            tagval1_cb.setDisable(false);
            tagtype2_cb.setDisable(false);
            tagval2_cb.setDisable(false);
            searchAnd_btn.setText("Go");
            search_btn.setDisable(true);
            searchOr_btn.setDisable(true);
        }
        else
        {
            if(tagtype1_cb.getSelectionModel().getSelectedItem() == null|| tagval1_cb.getSelectionModel().getSelectedItem() == null || tagtype2_cb.getSelectionModel().getSelectedItem() == null|| tagval2_cb.getSelectionModel().getSelectedItem() == null)
            {
                status_ta.setText("missing value or type");
                resetSearch();
                return;
            }
            Tag one = new Tag(tagtype1_cb.getSelectionModel().getSelectedItem(), tagval1_cb.getSelectionModel().getSelectedItem());
            Tag two = new Tag(tagtype2_cb.getSelectionModel().getSelectedItem(), tagval2_cb.getSelectionModel().getSelectedItem());
            if(one.equals(two)){
                status_ta.setText("tag 1 and 2 are same");
                resetSearch();
                return;
            }
            List<Photo> result = new ArrayList<>();
            List<Tag> temp;
            for(Photo p : allPhotos){
               temp = p.getTags();
               if(temp.contains(one) && temp.contains(two))
                   result.add(p);
            }
            sendResults(event, result);
            resetSearch();
        }
    }

    /**
     * Search albums for photos based on disjunction of two tag name-value pair
     * @param event onclick event to open photo view with results
     */
    public void searchOr(ActionEvent event) {
        if(searchOr_btn.getText().equals("Search by Two Tags (OR)")){
            tagtype1_cb.setDisable(false);
            tagval1_cb.setDisable(false);
            tagtype2_cb.setDisable(false);
            tagval2_cb.setDisable(false);
            searchOr_btn.setText("Go");
            searchAnd_btn.setDisable(true);
            search_btn.setDisable(true);
        }
        else
        {
            if(tagtype1_cb.getSelectionModel().getSelectedItem() == null|| tagval1_cb.getSelectionModel().getSelectedItem() == null || tagtype2_cb.getSelectionModel().getSelectedItem() == null|| tagval2_cb.getSelectionModel().getSelectedItem() == null)
            {
                status_ta.setText("missing value or type");
                resetSearch();
                return;
            }
            Tag one = new Tag(tagtype1_cb.getSelectionModel().getSelectedItem(), tagval1_cb.getSelectionModel().getSelectedItem());
            Tag two = new Tag(tagtype2_cb.getSelectionModel().getSelectedItem(), tagval2_cb.getSelectionModel().getSelectedItem());
            if(one.equals(two)){
                status_ta.setText("tag 1 and 2 are same");
                resetSearch();
                return;
            }
            List<Photo> result = new ArrayList<>();
            List<Tag> temp;
            for(Photo p : allPhotos){
                temp = p.getTags();
                if(temp.contains(one) || temp.contains(two))
                    result.add(p);
            }
            sendResults(event, result);
            resetSearch();
        }
    }

    /**
     * Reset search fields and buttons to initial values
     */
    public void resetSearch(){
        tagtype1_cb.setDisable(true);
        tagval1_cb.setDisable(true);
        tagtype2_cb.setDisable(true);
        tagval2_cb.setDisable(true);
        search_btn.setDisable(false);
        search_btn.setText("Search by Single Tag");
        searchOr_btn.setDisable(false);
        searchOr_btn.setText("Search by Two Tags (OR)");
        searchAnd_btn.setDisable(false);
        searchAnd_btn.setText("Search by Two Tags (AND)");
    }

    /**
     * Search albums for photos based on start and end dates of photos
     * @param event onclick event to open photo view with results
     */
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
            if(from_date.getValue() == null || to_date.getValue() == null) {
                status_ta.setText("missing date range");
                return;
            }
            Date lowerLimit = Date.from(from_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date upperLimit = Date.from(to_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if(lowerLimit.compareTo(upperLimit) > 0) {
                status_ta.setText("end cannot come before the beginning");
                return;
            }

            // make result
            List<Photo> result = new ArrayList<>();
            for(Photo p : allPhotos){
                if(p.getDateTaken().compareTo(lowerLimit) >= 0 && p.getDateTaken().compareTo(upperLimit) <= 0){
                    result.add(p);
                }
            }
            sendResults(event, result);
        }

    }

    /**
     * Open a photo view with search results
     * @param event onclick event to open photo view with results
     * @param result List<Photo> results to display in photo view
     */
    private void sendResults(ActionEvent event, List<Photo> result) {
        if(result.size() == 0)
            status_ta.setText("No photos found");
        else {
            Album resultAlbum = new Album("Search Results");
            resultAlbum.setPhotos(result);
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoSystem.fxml"));
                Parent parent = loader.load();
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

    /**
     * Create new album button functionality
     */
    public void createBtnAction() {
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
     * Edit album (name) button functionality
     */
    public void edit_BtnAction() {
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
     */
    public void deleteBtnAction() {
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
     * Cancel album create/edit/delete button functionality
     */
    public void cancelBtnAction() {
        status_ta.setText("action canceled");
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
     * Open album button functionality, opens selected album to display all photos + details
     * @param event onclick event to open album view
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
     */
    public void displayDetails() {
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
     * Reset text fields and buttons to initial values for album create/edit/delete
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

