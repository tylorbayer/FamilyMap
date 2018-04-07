package fmshared.fmresult;


/** A class to store Clear Result information for clear calls from the web api.
 *
 * @author TylorBayer
 */
public class ClearResult {

    /** Success or error message. Errors: Internal server error. */
    String message;


    /** Constructor for use within FMS.
     *
     * @param message Success or error message.
     */
    public ClearResult(String message) {
        this.message = message;
    }
}
