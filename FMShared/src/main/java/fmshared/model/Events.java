package fmshared.model;


/** A class to store Event information from the FMS database and use in memory
 *
 * @author TylorBayer
 */
public class Events extends FMSModel {
    /** Unique identifier for this event */
    private String eventID;
    /** User (username) to which this person belongs */
    private String descendant;
    /** ID of person to which this event belongs */
    private String personID;
    /** Latitude of event't location */
    private String latitude;
    /** Longitude of event't location */
    private String longitude;
    /** Country in which event occurred */
    private String country;
    /** City in which event occurred */
    private String city;
    /** Type of event */
    private String eventType;
    /** Year in which event occurred */
    private String year;


    /** Constructor for use within FMS
     *
     * @param eventID Unique identifier for this event generated by UUID (non-empty string)
     * @param descendant User (username) to which this person belongs (non-empty string)
     * @param personID ID of person to which this event belongs (non-empty string)
     * @param latitude Latitude of event't location (non-empty string)
     * @param longitude Longitude of event't location (non-empty string)
     * @param country Country in which event occurred (non-empty string)
     * @param city City in which event occurred (non-empty string)
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year Year in which event occurred (non-empty string)
     */
    public Events(String eventID, String descendant, String personID, String latitude, String longitude,
                  String country, String city, String eventType, String year) {

        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;

        values.add(eventID);
        values.add(descendant);
        values.add(personID);
        values.add(latitude);
        values.add(longitude);
        values.add(country);
        values.add(city);
        values.add(eventType);
        values.add(year);
    }

    public String getEventID() {
        return eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public String getYear() {
        return year;
    }

    public void populateValues() {
        values.add(eventID);
        values.add(descendant);

        values.add(personID);
        values.add(latitude);
        values.add(longitude);
        values.add(country);
        values.add(city);
        values.add(eventType);
        values.add(year);
    }

    /** Constructor for GSON */
    public Events() {}
}
