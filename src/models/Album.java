package models;

import java.util.List;

/**
 * @author hhh23 and pdp83
 * Will hold a List of Photos
 */
public class Album {
    private String name; // name of album
    private List<Photo> photos; // photos in the album

    public Album(){

    }

    public List<Photo> getPhotos(){return  this.photos; }

}
