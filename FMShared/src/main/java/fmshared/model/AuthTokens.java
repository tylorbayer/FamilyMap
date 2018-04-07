package fmshared.model;


import java.util.UUID;

/** A class to store AuthToken information from the FMS database and use in memory
 *
 * @author TylorBayer
 */
public class AuthTokens extends FMSModel {
    /** Authorization token assigned at login */
    private String authToken;
    /** ID of person to which the auth token is assigned */
    private String username;


    /** Constructor for use within FMS
     *
     * @param username ID of person to which the auth token is assigned (non-empty string)
     */
    public AuthTokens(String username) {
        this.username = username;

        values.add(UUID.randomUUID().toString());
        values.add(username);
    }

    /** Constructor for GSON */
    public AuthTokens() {}
}
