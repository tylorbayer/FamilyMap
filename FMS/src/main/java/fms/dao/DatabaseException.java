package fms.dao;


/** Exception class to isolate where SQLExceptions occur within the DAOs
 *
 * @author TylorBayer
 */
class DatabaseException extends Exception {
    /** Normal exception constructor
     *
     * @param s Error message containing location of thrown exception
     * @param throwable will throw SQLException
     */
    public DatabaseException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
