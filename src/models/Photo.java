package models;
import java.time.*;
import java.util.HashMap;

/**
 * Photo object
 * Stores: Date, Tags, and Location(maybe)
 */
public class Photo {
    private LocalDate dateTaken;
    private  HashMap<String, String> tags;
}
