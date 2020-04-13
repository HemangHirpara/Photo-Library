package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hhh23 and pdp83
 * holds all the albums owned by a user
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Album> albums;


    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public User(String username) {
        this.username = username;
        albums = new ArrayList<>();
    }

    /**
     * getter -> returns username
     * @return String username
     */
    public String getUsername() {return this.username;}

    @Override
    public String toString() {return this.username;}

    public boolean equals(User that){
        return this.getUsername().equals(that.getUsername());
    }
}
