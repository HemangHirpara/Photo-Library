package model;

import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Photo object
 * Stores: Date, Tags, and Location(maybe)
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Time dateTaken;
    private List<Tag> tags;
    private String caption;
    private File photoFile;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Photo(String caption, File photoFile){
        this.path = photoFile.getPath();
        this.caption = caption.equals("") ? "N/A" : caption;
        this.dateTaken = new Time(photoFile.lastModified());
        this.tags = new ArrayList<Tag>();
        this.photoFile = photoFile;
    }
    public Time getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Time dateTaken) {
        this.dateTaken = dateTaken;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }

    public void addTag(Tag tag){
        if (tags == null) tags = new ArrayList<Tag>();
        for (Tag t : tags){
            if (t.equals(tag)){
                System.out.println("Tag already exists for this photo");
                return;
            }
        }
        tags.add(tag);
    }

    public void deleteTag(int i){
        if (tags == null) return;
        tags.remove(i);
    }

    public boolean equals(Object o){
        if (o == null || !(o instanceof Photo))
            return false;
        Photo op = (Photo) o;
        return this.photoFile.equals(op.photoFile);
    }
}
