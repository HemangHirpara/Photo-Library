package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Album Object to represent a photo Album
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name; // name of album
    private List<Photo> photos; // photos in the album
    private Date start;
    private Date end;

    /**
     * Constructor for Album
     * @param name Name of album
     */
    public Album(String name){
        this.name = name;
        photos = new ArrayList<>();
        start = null;
        end = null;
    }

    /**
     * getter -> returns name of album
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * setter -> set album name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter -> set photo list for album
     * @param photos
     */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    /**
     * getter -> returns start date as a string
     * @return String date
     */
    public String getStartString() {
        if(this.start == null || photos.size() < 0)
            return "-";
        Date min = photos.get(0).getDateTaken();
        for(Photo p : photos){
            Date temp = p.getDateTaken();
            if(temp.compareTo(min) < 0)
                min = temp;
        }
        return new SimpleDateFormat("MM/dd/yy/").format(min);
    }

    /**
     * getter -> returns start date as a Date
     * @return Date start date
     */
    private Date getStart() {
        return this.start;
    }

    /**
     * setter -> set start date
     * @param start
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * getter -> returns end date as a string
     * @return String date
     */
    public String getEndString() {
        if(this.end == null)
            return "-";
        return new SimpleDateFormat("MM/dd/yy/").format(this.getEnd());
    }

    /**
     * getter -> returns end date as a Date
     * @return Date end date
     */
    private Date getEnd() {
        return this.end;
    }

    /**
     * setter -> set end date
     * @param end
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * getter -> returns List of Photos in album
     * @return List<Photo>
     */
    public List<Photo> getPhotos(){return  this.photos; }

    public String toString() {return  this.name;}

    /**
     * return the number of photos in the album
     * @return int number of photos
     */
    public int getNumPhotos(){
        if(photos.size() == 0)
            return 0;
        return photos.size();
    }

    /**
     * equals method to check if two albums are equal based on album name
     * @param that
     * @return
     */
    public boolean equals(Album that) {return this.getName().toLowerCase().equals(that.getName().toLowerCase());}

}
