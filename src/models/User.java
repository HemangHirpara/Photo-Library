package models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hhh23 and pdp83
 * holds all the albums owned by a user
 */
public class User implements Serializable {
    String username;
    List<Album> albums;

    public User(String username) {
        this.username = username;
        albums = new ArrayList<>();
    }

    /**
     * getter -> returns username
     * @return String username
     */
    public String getUsername() {return this.username;}
}
