package models;
import java.io.Serializable;
import java.time.*;
import java.util.HashMap;

/**
 * Photo object
 * Stores: Date, Tags, and Location(maybe)
 */
public class Photo implements Serializable {
    private LocalDate dateTaken;
    private  HashMap<String, String> tags;
}
