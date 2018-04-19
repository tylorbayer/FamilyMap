package fmshared.fmresult;


/** A class to store Login Result information for login calls from the web api
 *
 * @author TylorBayer
 */
public class LoginResult {

    /** Auth token received upon successful login */
    String authToken;
    /** User name of successfully logged in user */
    String userName;
    /** PersonID name of successfully logged in user */
    String personID;
    String firstName;
    String lastName;
    /** Error message (if error). Errors: Request property missing or has invalid value, Internal server error. */
    String message;

    boolean hasError = false;


    /** Constructor for use within FMS
     *
     * @param authToken Auth token received upon successful login (null if error)
     * @param userName User name of successfully logged in user (null if error)
     * @param personID PersonID name of successfully logged in user (null if error)
     */
    public LoginResult(String authToken, String userName, String personID, String firstName, String lastName) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public LoginResult(String message) {
        this.message = message;
        hasError = true;
    }

    public boolean hasError() {
        return hasError;
    }

    public String[] getValues() {
        return new String[] {firstName, lastName};
    }

    public String getDataReqInfo() {
        return authToken;
    }

    public String getPersonID() {
        return personID;
    }
}
