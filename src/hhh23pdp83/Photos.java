package hhh23pdp83;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Photos extends Application {

    /**
     * used for serialization
     */
    private static final String dir = "userData";
    private static final String filename = "userData.dat";
    private List<User> userList;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Photos15");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 500, 300));
        loadData();
        primaryStage.show();
    }

    public void loadData(){
        try {
            FileInputStream fis = new FileInputStream(dir + File.separator + filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userList = (ArrayList<User>) ois.readObject();
            if(userList == null)
                userList = new ArrayList<>();
            ois.close();
            fis.close();
            System.out.println("user lists loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("user lists created");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
