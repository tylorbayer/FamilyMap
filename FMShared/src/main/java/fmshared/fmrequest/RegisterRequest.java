package fmshared.fmrequest;


/** A class to store Register Request information for register calls from the web api
 *
 * @author TylorBayer
 */
public class RegisterRequest {
    /** User name of user trying to register */
    private String userName;
    /** Password of user trying to register */
    private String password;
    /** Email address of user trying to register */
    private String email;
    /** First name of user trying to register */
    private String firstName;
    /** Last name of user trying to register */
    private String lastName;
    /** gender of user trying to register */
    private String gender;


    /** Constructor for use within FMS
     *
     * @param userName User name of user trying to register (non-empty string)
     * @param password Password of user trying to register (non-empty string)
     * @param email Email address of user trying to register (non-empty string)
     * @param firstName First name of user trying to register (non-empty string)
     * @param lastName Last name of user trying to register (non-empty string)
     * @param gender Gender name of user trying to register ('m' or 'f')
     */
    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
