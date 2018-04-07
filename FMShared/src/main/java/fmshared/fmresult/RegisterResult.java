package fmshared.fmresult;


/** A class to store Register Result information for register calls from the web api
 *
 * @author TylorBayer
 */
public class RegisterResult {

    /** Auth token string received from login request */
    String authToken;
    /** User name passed with request */
    String userName;
    /** PersonID of the user's generated Person object */
    String personID;
    /** Error message (if error). Errors: Request property missing or has invalid value, Username already taken by another user, Internal server error. */
    String message;

    boolean hasError = false;


    /** Constructor for use within FMS
     *
     * @param authToken Auth token string received from login request (null if error)
     * @param userName User name passed with request (null if error)
     * @param personID PersonID of the user's generated Person object (null if error)
     */
    public RegisterResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public RegisterResult(String message) {
        this.message = message;
        hasError = true;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getPersonID() {
        return personID;
    }

    public String getDataReqInfo() {
        return authToken;
    }
}
