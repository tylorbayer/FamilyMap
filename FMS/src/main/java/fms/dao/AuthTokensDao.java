package fms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fmshared.model.AuthTokens;


/** Class to access the AuthTokens table in the FMS_database, extends FMSDao.
 *
 * @author TylorBayer
 */
public class AuthTokensDao extends FMSDao {

    /** Adds auth token to the AuthTokens table, calls the FMSDao insert method
     *
     * @param authTokens Array of AuthTokens objects to be added
     * @throws DatabaseException Thrown if insert method fails
     */
    public static void insert(AuthTokens[] authTokens) throws DatabaseException {
        String insertSQL = "INSERT INTO AuthTokens (AuthToken, Username) VALUES (?, ?);";

        try {
            for (AuthTokens authToken:authTokens) {

                ArrayList<String> values = authToken.getValues();

                insert(insertSQL, values);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("UserDao insert failed", e);
        }
    }

    /** Clears all data from AuthTokens table, calls the FMSDao clear method
     * @throws DatabaseException Thrown if clear method fails
     */
    public static void clear() throws DatabaseException {
        String clearSQL = "DELETE FROM AuthTokens";

        try {
            clear(clearSQL);
        }
        catch (SQLException e) {
            throw new DatabaseException("AuthTokens clear failed", e);
        }
    }

    /** Checks to see if auth token is valid
     *
     * @param authToken Auth token to be checked
     * @throws DatabaseException Thrown if verify method fails
     * @return true if valid, false if invalid
     */
    public static String verify(String authToken) throws DatabaseException, SQLException {
        String sqlQuery = "SELECT Username FROM AuthTokens WHERE AuthToken = ?";

        try {
            String[] values = {authToken};

            ResultSet resultSet = select(sqlQuery, values);

            return resultSet.getString(1);
        }
        catch (Exception e) {
            return null;
        }
        finally {
            closeConnection();
        }
    }
}
