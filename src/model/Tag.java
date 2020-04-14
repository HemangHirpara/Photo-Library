package model;

import java.io.Serializable;

/**
 * Tag Object to represent tags on a photo
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name, value;

    /**
     * Constructor for a Tag, a key-value pair
     * @param name name of tag
     * @param val value of tag
     */
    public Tag(String name, String val) {
        this.name = name.toLowerCase();
        this.value = val.toLowerCase();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Equals method to compare two tags based on both name and value
     * @param that
     * @return true if both tags are equal, false otherwise
     */
    public boolean equals(Tag that) {
        return name.equals(that.name) && value.equals(that.value);
    }

    /**
     * Override toString method to properly display tag name and value
     * @return String representing a tag
     */
    public String toString()
    {
        return name + ": " + value;
    }
}
