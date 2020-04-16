package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Tag Object to represent tags on a photo
 * @author Hemang Hirpara hhh23
 * @author Poojan Patel pdp83
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;

    /**
     * Constructor for a Tag, a key-value pair
     * @param name name of tag
     * @param val value of tag
     */
    public Tag(String name, String val) {
        this.name = name.toLowerCase();
        this.value = val;
    }

    /**
     * getter -> return name of tag
     * @return String tag name
     */
    public String getName() {
        return name;
    }

    /**
     * getter -> return value of tag
     * @return String tag value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Equals method to compare two tags based on both name and value
     * @param o
     * @return true if both tags are equal, false otherwise
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Tag))
            return false;
        Tag op = (Tag) o;
        return this.name.equals(op.name) && this.getValue().equals(op.getValue());
    }

    /**
     * Override toString method to properly display tag name and value
     * @return String representing a tag
     */
    public String toString()
    {
        return name + ": " + getValue();
    }

}
