package fmshared.fmresult;


/** A class to store Fill Result information for fill calls from the web api.
 *
 * @author TylorBayer
 */
public class FillResult {

    /** Success or error message. Errors: Invalid username or generations parameter, Internal server error. */
    String message;


    /** Constructor for use within FMS.
     *
     * @param message Success or error message.
     */
    public FillResult(String message) {
        this.message = message;
    }
}
