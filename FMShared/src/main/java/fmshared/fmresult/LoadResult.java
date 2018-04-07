package fmshared.fmresult;


/** A class to store Load Result information for load calls from the web api.
 *
 * @author TylorBayer
 */
public class LoadResult {

    /** Success or error message. Errors: Invalid request data (missing values, invalid values, etc.), Internal server error. */
    String message;


    /** Constructor for use within FMS.
     *
     * @param message Success or error message.
     */
    public LoadResult(String message) {
        this.message = message;
    }
}
