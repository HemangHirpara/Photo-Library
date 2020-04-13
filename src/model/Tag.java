package model;

import java.io.Serializable;

public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name, value;

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
