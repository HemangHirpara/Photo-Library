package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Extends Controller class to represent login function
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class LoginController extends Controller implements Initializable {

    @FXML private TextField username_tf;
    List<User> userList;

    /**
     * Initialize application with user list stored in data.dat
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File data = new File(getDataPath());
        // if file doesnt exist create it
        if(!(data.exists() || data.isFile())){
            // add stock user
            User stock;
            try {
                data.createNewFile();
                Album sAlbum = new Album("Stock Album");
                String path = "data/stock/";
                File photo;
                for(int i = 0; i <= 5; i++){
                    photo = new File(path + "img" + Integer.toString(i) + ".jpg");
                    if(photo != null){
                        Photo toAdd = new Photo(photo.getName(), photo);
                        sAlbum.addPhoto(toAdd);
                    }
                }
                stock = new User("stock");
                stock.getAlbums().add(sAlbum);
                userList = new ArrayList<>();
                userList.add(stock);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // serialize the data
            try {
                FileOutputStream fos = new FileOutputStream(getDataPath());
                ObjectOutputStream ous = new ObjectOutputStream(fos);
                ous.writeObject(userList);
                ous.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            // read in the data from the file
            try {
                FileInputStream fis = new FileInputStream(getDataPath());
                ObjectInputStream ois = new ObjectInputStream(fis);
                userList = (ArrayList<User>) ois.readObject();
                ois.close();
                fis.close();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * handles which scene to goto depending on the username
     * @param event on login_btn pressed
     * @throws IOException incase there is a error setting or creating the new scene
     */
    public void loginBtnAction(ActionEvent event) throws IOException {
        //String usr = username_tf.getText().toLowerCase();
        String usr = username_tf.getText();
        FXMLLoader loader;
        Parent parent;

        User currUser = null;
        for(User u: userList) {
            if(usr.equals(u.getUsername()))
                currUser = u;
        }

        if((usr.equals("") || currUser == null) && !usr.equals("admin")){
            username_tf.setText("invalid username");
            return;
        }

        if(usr.equals("admin")){
            loader = new FXMLLoader(getClass().getResource("/view/adminSystem.fxml"));
            parent = loader.load();
            AdminSystemController controller = loader.getController();
            Scene adminScene = new Scene(parent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            controller.initData(this.userList);
            stage.setScene(adminScene);
            stage.centerOnScreen();
            stage.show();
        }
        else {
            loader = new FXMLLoader(getClass().getResource("/view/userSystem.fxml"));
            parent = loader.load();
            UserSystemController controller = loader.getController();
            Scene userScene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controller.initData(this.userList, currUser);
            stage.setScene(userScene);
            stage.centerOnScreen();
            stage.show();
        }
    }
}
