package fmshared.fmrequest;


/** A class to store Login Request information for login calls from the web api
 *
 * @author TylorBayer
 */
public class LoginRequest {
    /** User name of user trying to log in */
    String userName;
    /** Password of user trying to log in */
    String password;

    /** Constructor for use within the FMS
     *
     * @param userName User name of user trying to log in
     * @param password Password of user trying to log in
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
