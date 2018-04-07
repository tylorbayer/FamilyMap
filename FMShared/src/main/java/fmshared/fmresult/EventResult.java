package fmshared.fmresult;


import fmshared.model.Events;

/** A class to store Event Result information for event calls from the web api
 *
 * @author TylorBayer
 */
public class EventResult {

    /** Array of Events objects */
    Events[] data;
    /** Error message (if error). Errors: Invalid auth token, Invalid eventID parameter, Requested
     * event does not belong to this OR Errors: Invalid auth token, Internal server error.  */
    String message;


    /** Constructor for use within FMS
     *
     * @param events Array of Events objects (null if error)
     */
    public EventResult(Events[] events) {
        this.data = events;
    }

    public EventResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Events[] getEvents() {
        return data;
    }
}
