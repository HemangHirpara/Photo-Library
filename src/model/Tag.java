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

    public Tag(String name, String val) {
        this.name = name;
        this.value = val;
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

    public boolean equals(Tag that) {
        return name.equals(that.name) && value.equals(that.value);
    }

    public String toString()
    {
        return name + ": " + value;
    }
}
