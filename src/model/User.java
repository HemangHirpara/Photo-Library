package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a User for Photo System
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Album> albums;

    /**
     * getter -> returns list of albums
     * @return List<Album>
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * setter -> sets list of albums
     * @param albums
     */
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    /**
     * Constructor for User
     * @param username Username for user
     */
    public User(String username) {
        this.username = username;
        albums = new ArrayList<>();
    }

    /**
     * getter -> returns username
     * @return String username
     */
    public String getUsername() {return this.username;}

    /**
     * toString method override to display username
     * @return
     */
    @Override
    public String toString() {return this.username;}

    /**
     * Equal method to check if users are the same based on name
     * @param that
     * @return
     */
    public boolean equals(User that){
        return this.getUsername().equals(that.getUsername());
    }
}
