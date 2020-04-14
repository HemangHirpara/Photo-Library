package model;

import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Photo object
 * Stores: Date, Tags, and Location(maybe)
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date dateTaken;
    private List<Tag> tags;
    private String caption;
    private File photoFile;
    private String path;

    /**
     * Constructor for photo object
     * @param caption Initial caption for photo
     * @param photoFile File object for photo
     */
    public Photo(String caption, File photoFile){
        this.path = photoFile.getPath();
        this.caption = caption.equals("") ? "N/A" : caption;
        this.dateTaken = new Date(photoFile.lastModified());
        this.tags = new ArrayList<Tag>();
        this.photoFile = photoFile;
    }

    /**
     * getter -> return Date object of photo
     * @return Date object, last modified value of photo
     */
    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Time dateTaken) {
        this.dateTaken = dateTaken;
    }

    /**
     * getter -> return list of tags for photo
     * @return List<Tag> object containing all tags for photo
     */
    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * getter -> return caption for photo
     * @return String caption of photo
     */
    public String getCaption() {
        return caption;
    }

    /**
     * setter -> set caption for photo
     * @param caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * getter -> get File object for photo
     * @return File object associated with photo
     */
    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * Add tag to tag list
     * @param tag tag to be added
     * @return true if tag was added, false if tag cannot be added
     */
    public boolean addTag(Tag tag){
        if (tags == null) tags = new ArrayList<Tag>();
        for (Tag t : tags){
            if (t.equals(tag)){
                return false;
            }
        }
        tags.add(tag);
        return true;
    }

    /**
     * Delete tag from tag list for photo
     * @param t tag to be removed
     * @return true if tag was removed, false if tag cannot be removed
     */
    public boolean deleteTag(Tag t){
        if (tags == null) return false;
        for(Tag tag  : tags)
        {
            if(t.equals(tag)) {
                tags.remove(tag);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the date of this photo in String formatted 'MM/dd/yy'.
     * @return the date of this photo in 'MM/dd/yy'
     */
    public String getDateString(){
        return new SimpleDateFormat("MM/dd/yy").format(this.dateTaken);
    }

    /**
     * Equals method to compare two photo objects based on objects
     * @param o
     * @return
     */
    public boolean equals(Object o){
        if (o == null || !(o instanceof Photo))
            return false;
        Photo op = (Photo) o;
        return this.photoFile.equals(op.photoFile);
    }
}
