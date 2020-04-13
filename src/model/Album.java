package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hhh23 and pdp83
 * Will hold a List of Photos
 */
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name; // name of album
    private List<Photo> photos; // photos in the album
    private Date start;
    private Date end;

    public Album(String name){
        this.name = name;
        photos = new ArrayList<>();
        start = null;
        end = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getStartString() {
        if(this.start == null)
            return "-";
        return new SimpleDateFormat("MM/dd/yy/").format(this.getStart());
    }

    private Date getStart() {
        return this.start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getEndString() {
        if(this.end == null)
            return "-";
        return new SimpleDateFormat("MM/dd/yy/").format(this.getEnd());
    }

    private Date getEnd() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


    public List<Photo> getPhotos(){return  this.photos; }

    public String toString() {return  this.name;}

    public int getNumPhotos(){
        if(photos.size() == 0)
            return 0;
        return photos.size();
    }

    public boolean equals(Album that) {return this.getName().toLowerCase().equals(that.getName().toLowerCase());}

}
