package fmshared.fmresult;


import fmshared.model.Persons;

/** A class to store Person Result information for person calls from the web api.
 *
 * @author TylorBayer
 */
public class PersonResult {

    /** Array of Persons objects */
    private Persons[] data = null;
    /** Error message (if error). Errors: Invalid auth token, Invalid personID parameter, Requested person does not belong to OR Errors: Invalid auth token, Internal server error. */
    private String message;

    /** Constructor to be used within FMS.
     *
     * @param persons Array of Persons objects (null if error)
     */
    public PersonResult(Persons[] persons) {
        this.data = persons;
    }

    public PersonResult(String message) {
        this.message = message;
    }

    public Persons[] getPersons() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
