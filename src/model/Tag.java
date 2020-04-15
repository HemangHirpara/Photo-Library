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
    private List<String> value;
    private boolean isMult;

    /**
     * Constructor for a Tag, a key-value pair
     * @param name name of tag
     * @param val value of tag
     */
    public Tag(String name, String val, boolean isMult) {
        this.name = name.toLowerCase();
        this.value = new ArrayList<>();
        if(!isMult){
            value.add(val);
        }else
            processValue(val);
    }

    public void processValue(String val){
        String[] values = val.split(",");
        for(String s: values)
            this.value.add(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {return this.value;}

    public String getValue() {
        String res = "";
        if(value.size() == 1)
            return value.get(0);
        for(int i = 0; i < value.size()-1;i++)
            res = res + value.get(i) + ", ";
        res+= value.get(value.size()-1);

        return  res;
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
