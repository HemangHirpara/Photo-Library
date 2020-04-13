package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Photo object
 * Stores: Date, Tags, and Location(maybe)
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate dateTaken;
    private  HashMap<String, String> tags;
}
